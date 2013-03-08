package cc.warlock.core.script.wsl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.internal.RegexMatch;
import cc.warlock.core.script.wsl.internal.IWSLCommand;
import cc.warlock.core.script.wsl.internal.IWSLCommandDefinition;
import cc.warlock.core.script.wsl.internal.IWSLValue;
import cc.warlock.core.script.wsl.internal.WSLString;

public class WSLScriptContext implements Runnable {
	
	private static final Pattern commandPattern = Pattern.compile("^([\\w]+)(\\s+(.*))?");
	private static final Pattern gosubArgRegex = Pattern.compile("(?:(['\"])(.*?)(?<!\\\\)(?>\\\\\\\\)*\\1|([^\\s]+))");
	
	private final WSLScript script;
	private IWSLCommand nextCommand;
	private IWSLCommand curCommand;
	private String curCommandString;
	private HashMap<String, IWSLValue> localVariables = new HashMap<String, IWSLValue>();
	private Stack<WSLFrame> callstack = new Stack<WSLFrame>();
	private boolean lastCondition = false;
	private ArrayList<WSLMatch> matches = new ArrayList<WSLMatch>();
	private BlockingQueue<String> matchQueue;
	
	private class WSLFrame {
		private IWSLCommand returnCommand;
		private HashMap<String, IWSLValue> localVariables;
		
		@SuppressWarnings("unchecked")
		public WSLFrame(IWSLCommand command, HashMap<String, IWSLValue> variables) {
			this.returnCommand = command;
			this.localVariables = (HashMap<String, IWSLValue>)variables.clone();
		}

		public void restore() {
			WSLScriptContext.this.localVariables = localVariables;
			nextCommand = returnCommand;
		}
	}
	
	private class WSLMatch implements IMatch {
		private IMatch match;
		private String label;
		
		public WSLMatch(String label, IMatch match) {
			this.label = label;
			this.match = match;
		}
		
		@Override
		public boolean matches(String text) {
			return match.matches(text);
		}
		
		public void run() {
			setVariablesFromMatch(match);
			gotoLabel(label);
		}

		@Override
		public String getText() {
			return match.getText();
		}

		@Override
		public Collection<String> groups() {
			return match.groups();
		}
	}
	
	public WSLScriptContext(WSLScript script) {
		this.script = script;

		int line = 0;
		nextCommand = null;
		while(nextCommand == null) {
			nextCommand = script.getLine(line);
			line++;
		}
		
	}
	
	public WSLScriptContext(WSLScript script, IWSLCommand command) {
		this.script = script;
		this.nextCommand = command;
	}
	
	public void run() {
		while(script.isRunning() && nextCommand != null) {
			curCommand = nextCommand;
			nextCommand = curCommand.getNext();
			
			// crazy dance to make sure we're not suspended and not in a roundtime
			try {
				if(!curCommand.isInstant())
					script.getCommands().waitForRoundtime();
				while(script.getCommands().isSuspended()) {
					script.getCommands().waitForResume();
					if(!curCommand.isInstant())
						script.getCommands().waitForRoundtime();
				}
			} catch(InterruptedException e) {
				
			} finally {
				if(!script.isRunning())
					break;
			}
			
			try {
				curCommand.execute(this);
			} catch(InterruptedException e) {
				
			}
		}
		
	}
	public boolean localVariableExists(String name) {
		return localVariables.containsKey(name);
	}
	
	public IWSLValue getLocalVariable(String name) {
		return localVariables.get(name);
	}
	
	public void execute(String line) throws InterruptedException {
		curCommandString = line;
		Matcher m = commandPattern.matcher(line.trim());
		
		if (!m.find()) {
			return;
		}
		
		String commandName = m.group(1).toLowerCase();
		String arguments = m.group(3);
		if(arguments == null) arguments = "";
		
		IWSLCommandDefinition command = WSLScriptCommands.getCommand(commandName);
		if(command != null) {
			scriptDebug(3, "command: " + line);
			command.execute(this, arguments);
		} else {
			scriptError("Invalid command on line (" + curCommand.getLineNumber() + "): " + line);
		}
	}
	
	public void scriptError(String message) {
		script.getCommands().error(curCommand.getLineNumber(), message, curCommandString);
	}
	
	public void scriptWarning(String message) {
		script.getCommands().warning(curCommand.getLineNumber(), message, curCommandString);
	}
	
	public void scriptDebug (int level, String message) {
		script.getCommands().debug(level, curCommand == null ? -1 : curCommand.getLineNumber(), message);
	}
	
	protected void deleteLocalVariable(String name) {
		localVariables.remove(name);
		scriptDebug(2, "Removed local variable \"" + name + "\"");
	}
	
	public void setLocalVariable(String name, String value) {
		setLocalVariable(name, new WSLString(value));
	}
	
	public void setLocalVariable(String name, IWSLValue value) {
		localVariables.put(name, value);
		scriptDebug(2, "local variable: " + name + "=" + value.toString(this));
	}
	
	protected void gosub (String label, String arguments) {
		scriptDebug(2, "gosub: " + label + ", parameter string: " + arguments);
		WSLFrame frame = new WSLFrame(nextCommand, localVariables);
		callstack.push(frame);

		setLocalVariable("0", arguments);

		// parse the args, splitting on " and ', and leaving in \-escaped quotes
		Matcher m = gosubArgRegex.matcher(arguments);
		ArrayList<String> matchList = new ArrayList<String>();
		while (m.find()) {
			String phrase;
			if (m.group(2) != null) {
				// Add quoted string without the quotes
				phrase = m.group(2);
			} else {
				// Add unquoted word
				phrase = m.group();
			}
			
			phrase = phrase.replaceAll("\\\\(['\"])", "\\1");
			matchList.add(phrase);
		}
		
		int i = 1;
		for(String arg : matchList) {
			setLocalVariable(String.valueOf(i), arg);
			i++;
		}

		Integer line = script.labelIndex(label.toLowerCase());

		if (line != null) {
			gotoCommand(line);
		} else {
			scriptError("Invalid gosub statement, label \"" + label + "\" does not exist");
		}
	}
	
	protected void matchWait(double timeout) throws InterruptedException {
		/*
		 * Remove matchQueue before going into the wait rather than
		 * after coming out so we don't end up trashing another's queue
		 * as we come out.
		 */
		BlockingQueue<String> myQueue = matchQueue;
		matchQueue = null;
		
		try {
			boolean haveTimeout = timeout > 0.0;
			long timeoutEnd = 0L;
			if(haveTimeout)
				timeoutEnd = System.currentTimeMillis() + (long)(timeout * 1000.0);
			
			// run until we get a match or are told to stop
			while(true) {
				String text = null;
				// wait for some text
				if(haveTimeout) {
					long now = System.currentTimeMillis();
					if(timeoutEnd >= now)
						text = myQueue.poll(timeoutEnd - now, TimeUnit.MILLISECONDS);
					if(text == null) {
						scriptDebug(1, "matchwait timed out");
						return;
					}
				} else {
					text = myQueue.take();
				}
				// try all of our matches
				for(WSLMatch match : matches) {
					if(match.matches(text)) {
						Object[] matches = match.groups().toArray();
						if(matches.length > 0)
							scriptDebug(2, "Matched text \"" + matches[0] + "\"");
						else
							scriptWarning("Match with no text?");
						match.run();
						return;
					}
				}
			}
		} finally {
			matches.clear();
			script.getCommands().removeLineQueue(myQueue);
		}
	}
	
	protected void gotoCommand(int line) {
		nextCommand = script.getLine(line);
		while(nextCommand == null) {
			line++;
			if(line >= script.numLines())
				break;
			nextCommand = script.getLine(line);
		}
	}
	
	protected void gotoLabel (String label) {
		scriptDebug(2, "goto " + label);
		// remove ":" from labels
		int pos = label.indexOf(':');
		if(pos >= 0)
			label = label.substring(0, pos);
		
		int command = script.labelIndex(label.toLowerCase());
		
		if (command >= 0) {
			gotoCommand(command);
		} else {
			command = script.labelIndex("labelerror");
			if (command < 0) {
				scriptDebug(1, "Label \"" + label + "\" does not exist, going to \"labelerror\"");
				gotoCommand(command);
			} else {
				scriptError("Label \"" + label + "\" does not exist");
			}
		}
	}

	protected void gosubReturn () {
		if (callstack.empty()) {
			scriptError("Invalid use of return, not in a subroutine");
		} else {
			WSLFrame frame = callstack.pop();
			frame.restore();
		}
	}
	
	protected void addMatch(String label, IMatch match) {
		if(matchQueue == null)
			matchQueue = script.getCommands().createLineQueue();
		
		matches.add(new WSLMatch(label, match));
	}
	
	protected void addMatchRe(String label, RegexMatch match) {
		if(matchQueue == null)
			matchQueue = script.getCommands().createLineQueue();
	
		matches.add(new WSLMatch(label, match));
	}
	
	public void setVariablesFromMatch(IMatch match) {
		int i = 0;
		for(String var : match.groups()) {
			setLocalVariable(String.valueOf(i), var);
			i++;
		}
	}

	public void setLastCondition(boolean condition) {
		this.lastCondition = condition;
	}
	
	public boolean getLastCondition() {
		return lastCondition;
	}
	

	public int getLineNum () {
		return curCommand.getLineNumber();
	}
	
	public WSLScript getScript() {
		return script;
	}
}