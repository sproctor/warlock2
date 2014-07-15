package cc.warlock.core.script.wsl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.internal.WarlockPattern;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.configuration.ScriptConfiguration;
import cc.warlock.core.script.internal.RegexMatch;
import cc.warlock.core.script.internal.TextMatch;
import cc.warlock.core.script.wsl.internal.IWSLCommandDefinition;
import cc.warlock.core.script.wsl.internal.IWSLValue;
import cc.warlock.core.script.wsl.internal.WSLAbstractNumber;

public class WSLScriptCommands {
	private static final String argSeparator = "\\s+";
	private static WSLScriptCommands instance;
	
	private HashMap<String, IWSLCommandDefinition> wslCommands =
		new HashMap<String, IWSLCommandDefinition>();
	
	public static synchronized WSLScriptCommands instance() {
		if(instance == null)
			instance = new WSLScriptCommands();
		return instance;
	}
	
	public static IWSLCommandDefinition getCommand(String name) {
		return instance().wslCommands.get(name);
	}
	
	private WSLScriptCommands() {
		// add command handlers
		addCommandDefinition("addtohighlightstrings", new WSLCommandAddHighlight());
		addCommandDefinition("deletefromhighlightstrings", new WSLCommandDeleteHighlight());
		addCommandDefinition("clearwindow", new WSLCommandClearWindow());
		addCommandDefinition("counter", new WSLCommandCounter());
		addCommandDefinition("deletelocalvariable", new WSLCommandDeleteLocalVariable());
		addCommandDefinition("deletevariable", new WSLCommandDeleteVariable());
		addCommandDefinition("debug", new WSLCommandDebug());
		addCommandDefinition("echo", new WSLCommandEcho());
		addCommandDefinition("else", new WSLCommandElse());
		addCommandDefinition("exit", new WSLCommandExit());
		addCommandDefinition("getcomponent", new WSLCommandGetComponent());
		addCommandDefinition("getstatus", new WSLCommandGetStatus());
		addCommandDefinition("gettime", new WSLCommandGetTime());
		addCommandDefinition("gettitle", new WSLCommandGetTitle());
		addCommandDefinition("getvital", new WSLCommandGetVital());
		addCommandDefinition("gosub", new WSLCommandGosub());
		addCommandDefinition("goto", new WSLCommandGoto());
		for(int i = 0; i <= 9; i++) {
			addCommandDefinition("if_" + i, new WSLCommandIf_(String.valueOf(i)));
		}
		addCommandDefinition("match", new WSLCommandMatch());
		addCommandDefinition("matchre", new WSLCommandMatchRe());
		addCommandDefinition("matchwait", new WSLCommandMatchWait());
		addCommandDefinition("math", new WSLCommandMath());
		addCommandDefinition("move", new WSLCommandMove());
		addCommandDefinition("nextroom", new WSLCommandNextRoom());
		addCommandDefinition("openwindow", new WSLCommandOpenWindow());
		addCommandDefinition("pause", new WSLCommandPause());
		addCommandDefinition("playsound", new WSLCommandPlaySound());
		addCommandDefinition("print", new WSLCommandPrint());
		addCommandDefinition("put", new WSLCommandPut());
		addCommandDefinition("random", new WSLCommandRandom());
		addCommandDefinition("return", new WSLCommandReturn());
		addCommandDefinition("run", new WSLCommandRun());
		addCommandDefinition("save", new WSLCommandSave());
		addCommandDefinition("setlocalvariable", new WSLCommandSetLocalVariable());
		addCommandDefinition("setvariable", new WSLCommandSetVariable());
		addCommandDefinition("shift", new WSLCommandShift());
		addCommandDefinition("timer", new WSLCommandTimer());
		addCommandDefinition("wait", new WSLCommandWait());
		addCommandDefinition("waitfor", new WSLCommandWaitFor());
		addCommandDefinition("waitforre", new WSLCommandWaitForRe());
	}
	
	private void addCommandDefinition (String name, IWSLCommandDefinition command) {
		wslCommands.put(name, command);
	}
	
	protected class WSLCommandSave implements IWSLCommandDefinition {
		
		public void execute(WSLScriptContext cx, String arguments) {
			cx.getScript().setSpecialVariable("s", arguments);
		}
	}

	protected class WSLCommandDebug implements IWSLCommandDefinition {
		private Pattern format = Pattern.compile("(\\w+)");
		
		public void execute(WSLScriptContext cx, String arguments) {
			
			if (arguments == null || arguments.length() == 0) {
				if(cx.getScript().getDebugLevel() < 1)
					cx.getScript().setDebugLevel(1);
				return;
			}
			
			Matcher m = format.matcher(arguments);
			if (!m.matches()) {
				cx.scriptWarning("Invalid arguments to command \"debug\"");
				return;
			}

			String arg = m.group(1);
			
			if(arg.equalsIgnoreCase("on") || arg.equalsIgnoreCase("true")) {
				if(cx.getScript().getDebugLevel() < 1)
					cx.getScript().setDebugLevel(1);
			} else if(arg.equalsIgnoreCase("off") || arg.equalsIgnoreCase("false")) {
				cx.getScript().setDebugLevel(0);
			} else {
				try {
					int level = Integer.parseInt(arg);
					cx.getScript().setDebugLevel(level);
				} catch(NumberFormatException e) {
					cx.scriptWarning("Invalid argument to command \"debug\": " + arg);
				}
			}
		}
	}
	
	protected class WSLCommandShift implements IWSLCommandDefinition {
		
		public void execute (WSLScriptContext cx, String arguments) {
			boolean local = arguments.equalsIgnoreCase("local");
			
			StringBuffer allArgs = new StringBuffer();
			for (int i = 1; ; i++) {
				String nextVar = Integer.toString(i + 1);
				boolean exists = local ? cx.localVariableExists(nextVar)
						: cx.getScript().variableExists(nextVar);
				if (!exists) {
					if (local) {
						cx.setLocalVariable("0", allArgs.toString());
						cx.deleteLocalVariable(Integer.toString(i));
					} else {
						cx.getScript().setSpecialVariable("0", allArgs.toString());
						cx.getScript().setSpecialVariable(Integer.toString(i), "");
					}
					break;
				} else {
					String arg = local ? cx.getLocalVariable(nextVar).toString(cx)
							: cx.getScript().getVariable(nextVar).toString(cx);
					if (arg == null)
						cx.scriptError("String error in arguments.");
					if(allArgs.length() > 0)
						allArgs.append(" ");
					allArgs.append(arg);
					if (local) {
						cx.setLocalVariable(Integer.toString(i), arg);
					} else {
						cx.getScript().setSpecialVariable(Integer.toString(i), arg);
					}
				}
			}
		}
	}

	protected class WSLCommandDeleteVariable implements IWSLCommandDefinition {
		
		public void execute (WSLScriptContext cx, String arguments) {
			String name = arguments.split(argSeparator)[0];
			cx.getScript().deleteVariable(name);
		}
	}
	
	protected class WSLCommandDeleteLocalVariable implements IWSLCommandDefinition {
		
		public void execute (WSLScriptContext cx, String arguments) {
			String name = arguments.split(argSeparator)[0];
			cx.deleteLocalVariable(name);
		}
	}

	protected class WSLCommandSetVariable implements IWSLCommandDefinition {
		
		private Pattern format = Pattern.compile("^([^\\s]+)(\\s+(.+)?)?$");
		
		public void execute (WSLScriptContext cx, String arguments) {
			Matcher m = format.matcher(arguments);
			if (m.find())
			{
				String name = m.group(1);
				String value = m.group(3);
				if(value == null)
					value = " ";
				
				cx.scriptDebug(1, "setVariable: " + name + "=" + value);
				if(cx.getScript().hasSpecialVariable(name))
					cx.scriptError("Cannot overwrite special variable \"" + name + "\"");
				cx.getScript().setGlobalVariable(name, value);
			} else {
				cx.scriptWarning("Invalid arguments to setvariable");
			}
		}
	}
	
	protected class WSLCommandSetLocalVariable implements IWSLCommandDefinition {
		
		private Pattern format = Pattern.compile("^([^\\s]+)(\\s+(.+)?)?$");
		
		public void execute (WSLScriptContext cx, String arguments) {
			Matcher m = format.matcher(arguments);
			if (m.find())
			{
				String name = m.group(1);
				String value = m.group(3);
				if(value == null)
					value = " ";
				
				cx.scriptDebug(1, "setLocalVariable: " + name + "=" + value);
				cx.setLocalVariable(name, value);
			} else {
				cx.scriptError("Invalid arguments to setLocalVariable");
			}
		}
	}
	
	protected class WSLCommandGoto implements IWSLCommandDefinition {
		
		public void execute (WSLScriptContext cx, String arguments) {
			if(arguments.length() > 0) {
				String[] args = arguments.split(argSeparator);
				String label = args[0];
				cx.gotoLabel(label);
			} else {
				cx.scriptError("Invalid arguments to goto");
			}
		}
	}
	
	protected class WSLCommandGosub implements IWSLCommandDefinition {
		
		private Pattern format = Pattern.compile("^([^\\s]+)\\s*(.*)?$");
		
		public void execute (WSLScriptContext cx, String arguments) {
			Matcher m = format.matcher(arguments);
			
			if (m.find()) {
				cx.gosub(m.group(1), m.group(2));
			} else {
				cx.scriptError("Invalid arguments to gosub");
			}
		}
	}

	protected class WSLCommandMatchWait implements IWSLCommandDefinition {

		public void execute (WSLScriptContext cx, String arguments) throws InterruptedException {
			double time;

			if(arguments.length() > 0) {
				String[] args = arguments.split(argSeparator);

				try {
					time = Double.parseDouble(args[0]);
				} catch(NumberFormatException e) {
					cx.scriptError("Non-numeral \"" + args[0] + "\" passed to matchwait");
					return;
				}
			} else {
				time = 0;
			}

			cx.matchWait(time);
		}
	}

	protected class WSLCommandMatchRe implements IWSLCommandDefinition {

		private Pattern format = Pattern.compile("^([^\\s]+)\\s+/(.*)/(\\w*)");

		public void execute (WSLScriptContext cx, String arguments) {
			Matcher m = format.matcher(arguments);

			if (m.find()) {
				String label = m.group(1);
				String regex = m.group(2);
				boolean caseInsensitive = m.group(3).contains("i");

				cx.addMatch(label, new RegexMatch(regex, caseInsensitive));
			} else {
				cx.scriptError("Invalid arguments to matchre");
			}
		}
	}
		
	protected class WSLCommandMatch implements IWSLCommandDefinition {

		private Pattern format = Pattern.compile("^([^\\s]+)\\s+(.*)$");

		public void execute (WSLScriptContext cx, String arguments) {
			Matcher m = format.matcher(arguments);

			if (m.find()) {
				String label = m.group(1);
				String text = m.group(2);
				
				cx.addMatch(label, new TextMatch(text));
			} else {
				cx.scriptError("Invalid arguments to match");
			}
		}
	}

	protected class WSLCommandCounter implements IWSLCommandDefinition {

		public void execute (WSLScriptContext cx, String arguments) {
			if (arguments.length() == 0) {
				cx.scriptError("You must provide an argument to counter");
				return;
			}

			doMath(cx, "c", arguments);

		}
	}

	protected class WSLCommandMath implements IWSLCommandDefinition {

		public void execute (WSLScriptContext cx, String arguments) {
			String[] args = arguments.split(argSeparator, 2);
			if (args.length < 2) {
				cx.scriptError("Not enough arguments to math");
				return;
			}

			doMath(cx, args[0], args[1]);

		}
	}

	protected class WSLCommandWaitForRe implements IWSLCommandDefinition {

		private Pattern format = Pattern.compile("^/(.*)/(\\w*)");

		public void execute (WSLScriptContext cx, String arguments) throws InterruptedException {
			Matcher m = format.matcher(arguments);

			if (m.find()) {
				String regex = m.group(1);
				String flags = m.group(2);
				boolean ignoreCase = flags != null && flags.contains("i");

				RegexMatch match = new RegexMatch(regex, ignoreCase);

				cx.getScript().getCommands().waitFor(match);
				cx.setVariablesFromMatch(match);
			} else {
				cx.scriptError("Invalid arguments to waitforre");
			}
		}
	}

	protected class WSLCommandWaitFor implements IWSLCommandDefinition {

		public void execute (WSLScriptContext cx, String arguments) throws InterruptedException {
			if (arguments.length() >= 1) {
				IMatch match = new TextMatch(arguments);
				cx.getScript().getCommands().waitFor(match);
			} else {
				cx.scriptError("Invalid arguments to waitfor");
			}
		}
	}

	protected class WSLCommandWait implements IWSLCommandDefinition {

		public void execute (WSLScriptContext cx, String arguments) throws InterruptedException {
			cx.getScript().getCommands().waitForPrompt();
		}
	}

	protected class WSLCommandPut implements IWSLCommandDefinition {

		public void execute(WSLScriptContext cx, String arguments) throws InterruptedException {
			if(arguments.startsWith(ScriptConfiguration.instance().getScriptPrefix())) {
				// Quit this script if we're starting another one
				cx.getScript().stop();
				ScriptEngineRegistry.startScript(cx.getScript().getViewer(), arguments.substring(1));
			} else {
				cx.getScript().getCommands().put(arguments, cx.getLineNum());
			}
		}
	}

	protected class WSLCommandPlaySound implements IWSLCommandDefinition {
		public void execute(WSLScriptContext cx, String arguments) throws InterruptedException {

			File file = new File(arguments);
			if (file.exists()) {
				try {
					cx.getScript().getCommands().playSound(new FileInputStream(file));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				cx.scriptError("Sound file \"" + file + "\" not found.");
			}
		}
	}

	protected class WSLCommandRun implements IWSLCommandDefinition {

		public void execute(WSLScriptContext cx, String arguments) throws InterruptedException {
			ScriptEngineRegistry.startScript(cx.getScript().getViewer(), arguments);
		}
	}

	protected class WSLCommandEcho implements IWSLCommandDefinition {

		public void execute (WSLScriptContext cx, String arguments) throws InterruptedException {
			cx.getScript().getCommands().echo(arguments);
		}
	}

	protected class WSLCommandPause implements IWSLCommandDefinition {

		public void execute (WSLScriptContext cx, String arguments) throws InterruptedException
		{
			double time;

			if(arguments.length() > 0) {
				String[] args = arguments.split(argSeparator);

				try {
					time = Double.parseDouble(args[0]);
				} catch(NumberFormatException e) {
					cx.scriptError("Non-numeral \"" + args[0] + "\" passed to pause");
					return;
				}
			} else {
				time = 1;
			}
			cx.getScript().getCommands().pause(time);
		}
	}

	protected class WSLCommandMove implements IWSLCommandDefinition {

		public void execute (WSLScriptContext cx, String arguments) throws InterruptedException
		{
			cx.getScript().getCommands().move(arguments, cx.getLineNum());
		}
	}

	protected class WSLCommandNextRoom implements IWSLCommandDefinition {

		public void execute (WSLScriptContext cx, String arguments) throws InterruptedException
		{
			cx.getScript().getCommands().waitNextRoom();
		}
	}

	protected class WSLCommandExit implements IWSLCommandDefinition {

		public void execute (WSLScriptContext cx, String arguments) {
			// TODO figure out if we should make this call here or elsewhere
			cx.getScript().stop();
		}
	}

	protected class WSLCommandIf_ implements IWSLCommandDefinition {
		private String variableName;
		
		public WSLCommandIf_ (String variableName) {
			this.variableName = variableName;
		}

		public void execute (WSLScriptContext cx, String arguments) throws InterruptedException {
			boolean exists = cx.getScript().variableExists(variableName);
			int varLength = cx.getScript().getVariable(variableName).toString(cx).length();
			if (exists && varLength > 0)
				cx.execute(arguments);
		}
	}

	private Pattern getFormat = Pattern.compile("^([^\\s]+)(\\s+(.*))?");
	private class WSLCommandGetVital implements IWSLCommandDefinition {

		public void execute(WSLScriptContext cx, String arguments) {
			Matcher m = getFormat.matcher(arguments);

			if(m.find()) {
				String var = m.group(1);
				// If we have more words after the variable, use that for the vital name
				String vital = m.group(3);
				if(vital == null)
					vital = var;

				cx.getScript().setGlobalVariable(var, cx.getScript().getClient().getProperty(vital).get());
			} else {
				cx.scriptError("Invalid arguments");
			}
		}
	}

	private class WSLCommandGetTitle implements IWSLCommandDefinition {

		public void execute(WSLScriptContext cx, String arguments) {
			Matcher m = getFormat.matcher(arguments);

			if(m.find()) {
				String var = m.group(1);
				// If we have more words after the variable, use that for the stream name
				String streamName = m.group(3);
				if(streamName == null)
					streamName = var;
				IWarlockClient client = cx.getScript().getClient();
				IStream stream = client.getStream(streamName);
				if(stream == null)
					cx.scriptWarning("Stream \"" + streamName + "\" does not exist.");
				else
					cx.getScript().setGlobalVariable(var, client.getStreamTitle(streamName));
			} else {
				cx.scriptError("Invalid arguments");
			}
		}
	}
	
	private class WSLCommandGetTime implements IWSLCommandDefinition {
		
		private Pattern format = Pattern.compile("^([^\\s]+)");
		
		public void execute(WSLScriptContext cx, String arguments) {
			Matcher m = format.matcher(arguments);

			if(m.find()) {
				String varName = m.group(1);

				cx.getScript().setGlobalVariable(varName, System.currentTimeMillis() / 100);
			} else {
				cx.scriptError("Invalid arguments");
			}
		}
	}

	private class WSLCommandGetStatus implements IWSLCommandDefinition {
		
		public void execute(WSLScriptContext cx, String arguments) {
			Matcher m = getFormat.matcher(arguments);

			if(m.find()) {
				String var = m.group(1);
				// If we have more words after the variable, use that for the status name
				String statusName = m.group(3);
				if(statusName == null)
					statusName = var;
				IWarlockClient client = cx.getScript().getClient();
				boolean status = client.getCharacterStatus().hasStatus(statusName);
				cx.getScript().setGlobalVariable(var, status);
			} else {
				cx.scriptError("Invalid arguments");
			}
		}
	}
	
	private class WSLCommandGetComponent implements IWSLCommandDefinition {

		public void execute(WSLScriptContext cx, String arguments) {
			Matcher m = getFormat.matcher(arguments);

			if(m.find()) {
				String var = m.group(1);
				// If we have more words after the variable, use that for the vital name
				String componentName = m.group(3);
				if(componentName == null)
					componentName = var;

				String component = cx.getScript().getClient().getComponent(componentName);
				if(component != null)
					cx.getScript().setGlobalVariable(var, component);
				else
					cx.scriptDebug(0, "Component (" + componentName + ") does not exist.");
			} else {
				cx.scriptError("Invalid arguments");
			}
		}
	}
	
	private class WSLCommandRandom implements IWSLCommandDefinition {

		private Pattern format = Pattern.compile("^(\\d+)\\s+(\\d+)");

		public void execute(WSLScriptContext cx, String arguments) {
			Matcher m = format.matcher(arguments);

			if(m.find()) {
				int min = Integer.parseInt(m.group(1));
				int max = Integer.parseInt(m.group(2));
				int r = min + new Random().nextInt(max - min + 1);

				cx.getScript().setSpecialVariable("r", Integer.toString(r));
			} else {
				cx.scriptError("Invalid arguments to random");
			}
		}
	}

	private class WSLCommandTimer implements IWSLCommandDefinition {

		private Pattern format = Pattern.compile("^(\\w+)(\\s+([^\\s]+))?");

		public void execute(WSLScriptContext cx, String arguments) {
			Matcher m = format.matcher(arguments);

			if(m.find()) {
				String command = m.group(1);
				String timerName = m.group(3);
				if(timerName == null)
					timerName = "t";
				IWSLValue var = cx.getScript().getVariable(timerName);
				if(var instanceof ScriptTimer || var == null) {
					ScriptTimer timer = (ScriptTimer)var;
					if(command.equals("start")) {
						if(timer == null) {
							timer = new ScriptTimer();
							cx.getScript().setSpecialVariable(timerName, timer);
						}
						timer.start();
					} else if(command.equals("stop")) {
						if(timer == null) {
							cx.scriptWarning("Timer \"" + timerName + "\" undefined.");
						} else {
							timer.stop();
						}
					} else if(command.equals("clear")) {
						if(timer == null) {
							cx.scriptWarning("Timer \"" + timerName + "\" undefined.");
						} else {
							timer.clear();
						}
					} else {
						cx.scriptError("Invalid command \"" + command + "\" given to timer");
					}
				} else {
					cx.scriptError("Variable \"" + timerName + "\" is not a timer.");
				}
			} else {
				cx.scriptError("Invalid arguments to timer");
			}
		}
	}
	
	private class WSLCommandElse implements IWSLCommandDefinition {

		public void execute (WSLScriptContext cx, String arguments) throws InterruptedException {
			if (!cx.getLastCondition())
			{
				cx.execute(arguments);
			}
		}
	}

	protected class WSLCommandReturn implements IWSLCommandDefinition {

		public void execute (WSLScriptContext cx, String arguments) {
			cx.gosubReturn();
		}
	}

	protected class WSLCommandOpenWindow implements IWSLCommandDefinition {
		public void execute(WSLScriptContext cx, String arguments) {
			String[] windows = arguments.split("\\s+");
			for(String name : windows) {
				cx.getScript().getCommands().openWindow(name);
			}
		}
	}
	
	protected class WSLCommandPrint implements IWSLCommandDefinition {
		private Pattern format = Pattern.compile("^([^\\s]+)(\\s+(.+))?");
		
		public void execute(WSLScriptContext cx, String arguments) {
			Matcher m = format.matcher(arguments);

			if(m.find()) {
				String name = m.group(1);
				String text = m.groupCount() > 2 ? m.group(3) : "";
				cx.getScript().getCommands().printToWindow(name, text + "\n");
			} else {
				cx.scriptError("Invalid arguments to print.");
			}
		}
	}
	
	protected class WSLCommandClearWindow implements IWSLCommandDefinition {
		public void execute(WSLScriptContext cx, String arguments) {
			String[] windows = arguments.split("\\s+");
			for(String name : windows) {
				cx.getScript().getCommands().clearWindow(name);
			}
		}
	}
	
	private class ScriptTimer extends WSLAbstractNumber {
		private long timerStart = -1L;
		private long timePast = 0L;
		
		@Override
		public double toDouble(WSLScriptContext cx) {
			if(timerStart < 0) return timePast / 1000;
			return (System.currentTimeMillis() - timerStart) / 1000;
		}
		
		public void start() {
			if(timerStart < 0)
				timerStart = System.currentTimeMillis() - timePast;
		}
		
		public void stop() {
			if(timerStart >= 0) {
				timePast = timerStart - System.currentTimeMillis();
				timerStart = -1L;
			}
		}
		
		public void clear() {
			timerStart = -1L;
			timePast = 0L;
		}
	}
	
	protected class WSLCommandAddHighlight implements IWSLCommandDefinition {
		private Pattern format = Pattern.compile("(\\S+)=(\"([^\"]*)\"|(\\S*))");
		
		public void execute(WSLScriptContext cx, String arguments) {
			Matcher m = format.matcher(arguments);
			String string = null;
			String fgcolor = null;
			String bgcolor = null;
			boolean fullLine = false;
			boolean literal = true;
			boolean fullWord = false;
			boolean caseSensitive = false;
			
			while(m.find()) {
				String key = m.group(1);
				
				// value in parens
				String value = m.group(3);
				if(value == null) {
					// value is a single word
					value = m.group(4);
					
					// weird match
					if(value == null) {
						cx.scriptError("Unexpected result in AddHighlight.");
						return;
					}
				}
				
				if(key.equalsIgnoreCase("string")) {
					string = value;
				} else if(key.equalsIgnoreCase("foreColor")) {
					fgcolor = value;
				} else if(key.equalsIgnoreCase("backColor")) {
					bgcolor = value;
				} else if(key.equalsIgnoreCase("highlightEntireLine")) {
					if(value.equalsIgnoreCase("true"))
						fullLine = true;
				} else if(key.equalsIgnoreCase("regex") || key.equalsIgnoreCase("regexp")) {
					if(value.equalsIgnoreCase("true"))
						literal = false;
				} else if(key.equalsIgnoreCase("notOnWordBoundary") || key.equalsIgnoreCase("matchPartialWord")) {
					if(value.equalsIgnoreCase("false"))
						fullWord = true;
				} else if(key.equalsIgnoreCase("caseInsensitive") || key.equalsIgnoreCase("ignoreCase")) {
					if(value.equalsIgnoreCase("false"))
						caseSensitive = true;
				}
			}
			
			if(string == null) {
				cx.scriptError("No \"string\" given.");
				return;
			}
			
			WarlockStyle style = new WarlockStyle();
			if(fgcolor != null)
				style.setForegroundColor(new WarlockColor(fgcolor));
			if(bgcolor != null)
				style.setBackgroundColor(new WarlockColor(bgcolor));
			style.setFullLine(fullLine);
			
			WarlockPattern highlight = new WarlockPattern(string);
			highlight.setLiteral(literal);
			highlight.setFullWordMatch(fullWord);
			highlight.setCaseSensitive(caseSensitive);
			highlight.setStyle(style);
			
			cx.getScript().addHighlight(highlight);
		}
	}
	
	protected class WSLCommandDeleteHighlight implements IWSLCommandDefinition {
		private Pattern format = Pattern.compile("(\\S+)=(\"([^\"]*)\"|(\\S*))");
		
		public void execute(WSLScriptContext cx, String arguments) {
			Matcher m = format.matcher(arguments);
			String string = null;
			
			while(m.find()) {
				String key = m.group(1);
				
				// value in parens
				String value = m.group(3);
				if(value == null) {
					// value is a single word
					value = m.group(4);
					
					// weird match
					if(value == null) {
						cx.scriptError("Unexpected result in DeleteHighlight.");
						return;
					}
				}
				
				if(key.equalsIgnoreCase("string")) {
					string = value;
				}
			}
			
			if(string == null) {
				cx.scriptError("No \"string\" given.");
				return;
			}
			
			cx.getScript().removeHighlight(string);
		}
	}
	
	private void doMath(WSLScriptContext cx, String targetVar, String arguments) {
		String[] args = arguments.split(argSeparator);
		if (args.length < 1) {
			cx.scriptError("No operator for math");
			return;
		}

		String operator = args[0].trim().toLowerCase();

		double operand;
		boolean operandIsDefault;
		if (args.length > 1) {
			try {
				operand = Double.parseDouble(args[1].trim());
				operandIsDefault = false;
			} catch (NumberFormatException e) {
				cx.scriptError("Operand must be a number");
				return;
			}
		} else {
			operand = 1;
			operandIsDefault = true;
		}

		if ("set".equalsIgnoreCase(operator))
		{
			cx.getScript().setGlobalVariable(targetVar, operand);
			return;
		}

		double value;
		if(cx.getScript().variableExists(targetVar)) {
			try {
				value = cx.getScript().getVariable(targetVar).toDouble(cx);
			} catch(NumberFormatException e) {
				cx.scriptError("The variable \"" + targetVar + "\" must be a number to do math with it");
				return;
			}
		} else
			value = 0;

		double newValue;
		if ("add".equalsIgnoreCase(operator))
		{	
			newValue = value + operand;
		}
		else if ("subtract".equalsIgnoreCase(operator))
		{
			newValue = value - operand;
		}
		else if ("multiply".equalsIgnoreCase(operator))
		{
			newValue = value * operand;
		}
		else if ("divide".equalsIgnoreCase(operator))
		{
			if (operand == 0) {
				cx.scriptError("Cannot divide by zero");
				return;
			}
			newValue = value / operand;
		}
		else if ("modulus".equalsIgnoreCase(operator))
		{
			newValue = value % operand;
		}
		else if ("log".equalsIgnoreCase(operator))
		{
			if(operand != 1 && operand != 10)
				newValue = Math.log(value) / Math.log(operand);
			else
				newValue = Math.log10(value);
		}
		else if ("ln".equalsIgnoreCase(operator))
		{
			newValue = Math.log(value);
		}
		else if("truncate".equalsIgnoreCase(operator))
		{
			if(operandIsDefault || (long)operand == 0) {
				long temp = (long)value;
				newValue = (double)temp;
			} else {
				long temp = (long)(value * (10 ^ (long)operand));
				newValue = temp / (10 ^ (long)operand);
			}
		}
		else
		{
			cx.scriptError("Unrecognized math command \"" + operator + "\"");
			return;
		}
		cx.getScript().setGlobalVariable(targetVar, newValue);
	}
}
