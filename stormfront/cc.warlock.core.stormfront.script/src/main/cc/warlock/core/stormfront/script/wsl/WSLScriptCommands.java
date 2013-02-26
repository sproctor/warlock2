package cc.warlock.core.stormfront.script.wsl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.warlock.core.client.IStream;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.internal.WarlockHighlight;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.configuration.ScriptConfiguration;
import cc.warlock.core.script.internal.RegexMatch;
import cc.warlock.core.script.internal.TextMatch;
import cc.warlock.core.stormfront.script.wsl.internal.IWSLCommandDefinition;
import cc.warlock.core.stormfront.script.wsl.internal.IWSLValue;
import cc.warlock.core.stormfront.script.wsl.internal.WSLAbstractNumber;
import cc.warlock.core.stormfront.script.wsl.internal.WSLBoolean;
import cc.warlock.core.stormfront.script.wsl.internal.WSLNumber;

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
		
		public void execute(WSLScript script, String arguments) {
			script.setSpecialVariable("s", arguments);
		}
	}

	protected class WSLCommandDebug implements IWSLCommandDefinition {
		private Pattern format = Pattern.compile("(\\w+)");
		
		public void execute(WSLScript script, String arguments) {
			
			if (arguments == null || arguments.length() == 0) {
				if(script.getDebugLevel() < 1)
					script.setDebugLevel(1);
				return;
			}
			
			Matcher m = format.matcher(arguments);
			if (!m.matches()) {
				script.scriptWarning("Invalid arguments to command \"debug\"");
				return;
			}

			String arg = m.group(1);
			
			if(arg.equalsIgnoreCase("on") || arg.equalsIgnoreCase("true")) {
				if(script.getDebugLevel() < 1)
					script.setDebugLevel(1);
			} else if(arg.equalsIgnoreCase("off") || arg.equalsIgnoreCase("false")) {
				script.setDebugLevel(0);
			} else {
				try {
					int level = Integer.parseInt(arg);
					script.setDebugLevel(level);
				} catch(NumberFormatException e) {
					script.scriptWarning("Invalid argument to command \"debug\": " + arg);
				}
			}
		}
	}
	
	protected class WSLCommandShift implements IWSLCommandDefinition {
		
		public void execute (WSLScript script, String arguments) {
			boolean local = arguments.equalsIgnoreCase("local");
			
			StringBuffer allArgs = new StringBuffer();
			for (int i = 1; ; i++) {
				String nextVar = Integer.toString(i + 1);
				boolean exists = local ? script.localVariableExists(nextVar)
						: script.variableExists(nextVar);
				if (!exists)
				{
					if (local) {
						script.setLocalVariable("0", allArgs.toString());
						script.deleteLocalVariable(Integer.toString(i));
					} else {
						script.setSpecialVariable("0", allArgs.toString());
						script.setSpecialVariable(Integer.toString(i), "");
					}
					break;
				}
				else
				{
					String arg = local ? script.getLocalVariable(nextVar).toString()
							: script.getVariable(nextVar).toString();
					if (arg == null)
						script.scriptError("String error in arguments.");
					if(allArgs.length() > 0)
						allArgs.append(" ");
					allArgs.append(arg);
					if (local) {
						script.setLocalVariable(Integer.toString(i), arg);
					} else {
						script.setSpecialVariable(Integer.toString(i), arg);
					}
				}
			}
		}
	}

	protected class WSLCommandDeleteVariable implements IWSLCommandDefinition {
		
		public void execute (WSLScript script, String arguments) {
			String name = arguments.split(argSeparator)[0];
			script.deleteVariable(name);
		}
	}
	
	protected class WSLCommandDeleteLocalVariable implements IWSLCommandDefinition {
		
		public void execute (WSLScript script, String arguments) {
			String name = arguments.split(argSeparator)[0];
			script.deleteLocalVariable(name);
		}
	}

	protected class WSLCommandSetVariable implements IWSLCommandDefinition {
		
		private Pattern format = Pattern.compile("^([^\\s]+)(\\s+(.+)?)?$");
		
		public void execute (WSLScript script, String arguments) {
			Matcher m = format.matcher(arguments);
			if (m.find())
			{
				String name = m.group(1);
				String value = m.group(3);
				if(value == null)
					value = " ";
				
				script.scriptDebug(1, "setVariable: " + name + "=" + value);
				script.setGlobalVariable(name, value);
			} else {
				script.scriptWarning("Invalid arguments to setvariable");
			}
		}
	}
	
	protected class WSLCommandSetLocalVariable implements IWSLCommandDefinition {
		
		private Pattern format = Pattern.compile("^([^\\s]+)(\\s+(.+)?)?$");
		
		public void execute (WSLScript script, String arguments) {
			Matcher m = format.matcher(arguments);
			if (m.find())
			{
				String name = m.group(1);
				String value = m.group(3);
				if(value == null)
					value = " ";
				
				script.scriptDebug(1, "setLocalVariable: " + name + "=" + value);
				script.setLocalVariable(name, value);
			} else {
				script.scriptError("Invalid arguments to setLocalVariable");
			}
		}
	}
	
	protected class WSLCommandGoto implements IWSLCommandDefinition {
		
		public void execute (WSLScript script, String arguments) {
			if(arguments.length() > 0) {
				String[] args = arguments.split(argSeparator);
				String label = args[0];
				script.gotoLabel(label);
			} else {
				script.scriptError("Invalid arguments to goto");
			}
		}
	}
	
	protected class WSLCommandGosub implements IWSLCommandDefinition {
		
		private Pattern format = Pattern.compile("^([^\\s]+)\\s*(.*)?$");
		
		public void execute (WSLScript script, String arguments) {
			Matcher m = format.matcher(arguments);
			
			if (m.find()) {
				script.gosub(m.group(1), m.group(2));
			} else {
				script.scriptError("Invalid arguments to gosub");
			}
		}
	}

	protected class WSLCommandMatchWait implements IWSLCommandDefinition {

		public void execute (WSLScript script, String arguments) throws InterruptedException {
			double time;

			if(arguments.length() > 0) {
				String[] args = arguments.split(argSeparator);

				try {
					time = Double.parseDouble(args[0]);
				} catch(NumberFormatException e) {
					script.scriptError("Non-numeral \"" + args[0] + "\" passed to matchwait");
					return;
				}
			} else {
				time = 0;
			}

			script.matchWait(time);
		}
	}

	protected class WSLCommandMatchRe implements IWSLCommandDefinition {

		private Pattern format = Pattern.compile("^([^\\s]+)\\s+/(.*)/(\\w*)");

		public void execute (WSLScript script, String arguments) {
			Matcher m = format.matcher(arguments);

			if (m.find()) {
				String label = m.group(1);
				String regex = m.group(2);
				boolean caseInsensitive = m.group(3).contains("i");

				script.addMatchRe(label,
						new RegexMatch(regex, caseInsensitive));
			} else {
				script.scriptError("Invalid arguments to matchre");
			}
		}
	}
		
	protected class WSLCommandMatch implements IWSLCommandDefinition {

		private Pattern format = Pattern.compile("^([^\\s]+)\\s+(.*)$");

		public void execute (WSLScript script, String arguments) {
			Matcher m = format.matcher(arguments);

			if (m.find()) {
				String label = m.group(1);
				String text = m.group(2);
				
				script.addMatch(label, new TextMatch(text));
			} else {
				script.scriptError("Invalid arguments to match");
			}
		}
	}

	protected class WSLCommandCounter implements IWSLCommandDefinition {

		public void execute (WSLScript script, String arguments) {
			if (arguments.length() == 0) {
				script.scriptError("You must provide an argument to counter");
				return;
			}

			doMath(script, "c", arguments);

		}
	}

	protected class WSLCommandMath implements IWSLCommandDefinition {

		public void execute (WSLScript script, String arguments) {
			String[] args = arguments.split(argSeparator, 2);
			if (args.length < 2) {
				script.scriptError("Not enough arguments to math");
				return;
			}

			doMath(script, args[0], args[1]);

		}
	}

	protected class WSLCommandWaitForRe implements IWSLCommandDefinition {

		private Pattern format = Pattern.compile("^/(.*)/(\\w*)");

		public void execute (WSLScript script, String arguments) throws InterruptedException {
			Matcher m = format.matcher(arguments);

			if (m.find()) {
				String regex = m.group(1);
				String flags = m.group(2);
				boolean ignoreCase = flags != null && flags.contains("i");

				RegexMatch match = new RegexMatch(regex, ignoreCase);

				script.scriptCommands.waitFor(match);
				script.setVariablesFromMatch(match);
			} else {
				script.scriptError("Invalid arguments to waitforre");
			}
		}
	}

	protected class WSLCommandWaitFor implements IWSLCommandDefinition {

		public void execute (WSLScript script, String arguments) throws InterruptedException {
			if (arguments.length() >= 1)
			{
				IMatch match = new TextMatch(arguments);
				script.scriptCommands.waitFor(match);

			} else {
				script.scriptError("Invalid arguments to waitfor");
			}
		}
	}

	protected class WSLCommandWait implements IWSLCommandDefinition {

		public void execute (WSLScript script, String arguments) throws InterruptedException {
			script.scriptCommands.waitForPrompt();
		}
	}

	protected class WSLCommandPut implements IWSLCommandDefinition {

		public void execute(WSLScript script, String arguments) throws InterruptedException {
			if(arguments.startsWith(ScriptConfiguration.instance().getScriptPrefix())) {
				// Quit this script if we're starting another one
				script.stop();
				ScriptEngineRegistry.startScript(script.getViewer(), arguments.substring(1));
			} else {
				script.scriptCommands.put(arguments, script.getLineNum());
			}
		}
	}

	protected class WSLCommandPlaySound implements IWSLCommandDefinition {
		public void execute(WSLScript script, String arguments) throws InterruptedException {

			File file = new File(arguments);
			if (file.exists()) {
				try {
					script.scriptCommands.playSound(new FileInputStream(file));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				script.scriptError("Sound file \"" + file + "\" not found.");
			}
		}
	}

	protected class WSLCommandRun implements IWSLCommandDefinition {

		public void execute(WSLScript script, String arguments) throws InterruptedException {
			ScriptEngineRegistry.startScript(script.getViewer(), arguments);
		}
	}

	protected class WSLCommandEcho implements IWSLCommandDefinition {

		public void execute (WSLScript script, String arguments)
		{
			script.scriptCommands.echo(arguments);
		}
	}

	protected class WSLCommandPause implements IWSLCommandDefinition {

		public void execute (WSLScript script, String arguments) throws InterruptedException
		{
			double time;

			if(arguments.length() > 0) {
				String[] args = arguments.split(argSeparator);

				try {
					time = Double.parseDouble(args[0]);
				} catch(NumberFormatException e) {
					script.scriptError("Non-numeral \"" + args[0] + "\" passed to pause");
					return;
				}
			} else {
				time = 1;
			}
			script.scriptCommands.pause(time);
		}
	}

	protected class WSLCommandMove implements IWSLCommandDefinition {

		public void execute (WSLScript script, String arguments) throws InterruptedException
		{
			script.scriptCommands.move(arguments, script.getLineNum());
		}
	}

	protected class WSLCommandNextRoom implements IWSLCommandDefinition {

		public void execute (WSLScript script, String arguments) throws InterruptedException
		{
			script.scriptCommands.waitNextRoom();
		}
	}

	protected class WSLCommandExit implements IWSLCommandDefinition {

		public void execute (WSLScript script, String arguments) {
			// TODO figure out if we should make this call here or elsewhere
			script.stop();
		}
	}

	protected class WSLCommandIf_ implements IWSLCommandDefinition {
		private String variableName;
		
		public WSLCommandIf_ (String variableName) {
			this.variableName = variableName;
		}

		public void execute (WSLScript script, String arguments) throws InterruptedException {
			if (script.variableExists(variableName) &&
					script.getVariable(variableName).toString().length() > 0)
				script.execute(arguments);
		}
	}

	private Pattern getFormat = Pattern.compile("^([^\\s]+)(\\s+(.*))?");
	private class WSLCommandGetVital implements IWSLCommandDefinition {

		public void execute(WSLScript script, String arguments) {
			Matcher m = getFormat.matcher(arguments);

			if(m.find()) {
				String var = m.group(1);
				// If we have more words after the variable, use that for the vital name
				String vital = m.group(3);
				if(vital == null)
					vital = var;

				script.setGlobalVariable(var, script.getClient().getProperty(vital).get());
			} else {
				script.scriptError("Invalid arguments to random");
			}
		}
	}

	private class WSLCommandGetTitle implements IWSLCommandDefinition {

		public void execute(WSLScript script, String arguments) {
			Matcher m = getFormat.matcher(arguments);

			if(m.find()) {
				String var = m.group(1);
				// If we have more words after the variable, use that for the stream name
				String streamName = m.group(3);
				if(streamName == null)
					streamName = var;

				IStream stream = script.getClient().getStream(streamName);
				if(stream == null)
					script.scriptWarning("Stream \"" + streamName + "\" does not exist.");
				else
					script.setGlobalVariable(var, script.getClient().getStreamTitle(streamName));
			} else {
				script.scriptError("Invalid arguments to random");
			}
		}
	}
	
	private class WSLCommandGetTime implements IWSLCommandDefinition {
		
		private Pattern format = Pattern.compile("^([^\\s]+)");
		
		public void execute(WSLScript script, String arguments) {
			Matcher m = format.matcher(arguments);

			if(m.find()) {
				String varName = m.group(1);

				script.setGlobalVariable(varName, new WSLNumber(System.currentTimeMillis() / 100));
			} else {
				script.scriptError("Invalid arguments to random");
			}
		}
	}

	private class WSLCommandGetStatus implements IWSLCommandDefinition {
		
		public void execute(WSLScript script, String arguments) {
			Matcher m = getFormat.matcher(arguments);

			if(m.find()) {
				String var = m.group(1);
				// If we have more words after the variable, use that for the status name
				String statusName = m.group(3);
				if(statusName == null)
					statusName = var;
				
				boolean status = script.getClient().getCharacterStatus().hasStatus(statusName);
				script.setGlobalVariable(var, new WSLBoolean(status));
			} else {
				script.scriptError("Invalid arguments to random");
			}
		}
	}
	
	private class WSLCommandGetComponent implements IWSLCommandDefinition {

		public void execute(WSLScript script, String arguments) {
			Matcher m = getFormat.matcher(arguments);

			if(m.find()) {
				String var = m.group(1);
				// If we have more words after the variable, use that for the vital name
				String componentName = m.group(3);
				if(componentName == null)
					componentName = var;

				String component = script.getClient().getComponent(componentName);
				if(component != null)
					script.setGlobalVariable(var, component);
				else
					script.scriptDebug(0, "Component (" + componentName + ") does not exist.");
			} else {
				script.scriptError("Invalid arguments to random");
			}
		}
	}
	
	private class WSLCommandRandom implements IWSLCommandDefinition {

		private Pattern format = Pattern.compile("^(\\d+)\\s+(\\d+)");

		public void execute(WSLScript script, String arguments) {
			Matcher m = format.matcher(arguments);

			if(m.find()) {
				int min = Integer.parseInt(m.group(1));
				int max = Integer.parseInt(m.group(2));
				int r = min + new Random().nextInt(max - min + 1);

				script.setSpecialVariable("r", Integer.toString(r));
			} else {
				script.scriptError("Invalid arguments to random");
			}
		}
	}

	private class WSLCommandTimer implements IWSLCommandDefinition {

		private Pattern format = Pattern.compile("^(\\w+)(\\s+([^\\s]+))?");

		public void execute(WSLScript script, String arguments) {
			Matcher m = format.matcher(arguments);

			if(m.find()) {
				String command = m.group(1);
				String timerName = m.group(3);
				if(timerName == null)
					timerName = "t";
				IWSLValue var = script.getVariable(timerName);
				if(var instanceof ScriptTimer || var == null) {
					ScriptTimer timer = (ScriptTimer)var;
					if(command.equals("start")) {
						if(timer == null) {
							timer = new ScriptTimer();
							script.setSpecialVariable(timerName, timer);
						}
						timer.start();
					} else if(command.equals("stop")) {
						if(timer == null) {
							script.scriptWarning("Timer \"" + timerName + "\" undefined.");
						} else {
							timer.stop();
						}
					} else if(command.equals("clear")) {
						if(timer == null) {
							script.scriptWarning("Timer \"" + timerName + "\" undefined.");
						} else {
							timer.clear();
						}
					} else {
						script.scriptError("Invalid command \"" + command + "\" given to timer");
					}
				} else {
					script.scriptError("Variable \"" + timerName + "\" is not a timer.");
				}
			} else {
				script.scriptError("Invalid arguments to timer");
			}
		}
	}
	
	private class WSLCommandElse implements IWSLCommandDefinition {

		public void execute (WSLScript script, String arguments) throws InterruptedException {
			if (!script.getLastCondition())
			{
				script.execute(arguments);
			}
		}
	}

	protected class WSLCommandReturn implements IWSLCommandDefinition {

		public void execute (WSLScript script, String arguments) {
			script.gosubReturn();
		}
	}

	protected class WSLCommandOpenWindow implements IWSLCommandDefinition {
		public void execute(WSLScript script, String arguments) {
			String[] windows = arguments.split("\\s+");
			for(String name : windows) {
				script.getCommands().openWindow(name);
			}
		}
	}
	
	protected class WSLCommandPrint implements IWSLCommandDefinition {
		private Pattern format = Pattern.compile("^([^\\s]+)(\\s+(.+))?");
		
		public void execute(WSLScript script, String arguments) {
			Matcher m = format.matcher(arguments);

			if(m.find()) {
				String name = m.group(1);
				String text = m.groupCount() > 2 ? m.group(3) : "";
				script.getCommands().printToWindow(name, text + "\n");
			} else {
				script.scriptError("Invalid arguments to print.");
			}
		}
	}
	
	protected class WSLCommandClearWindow implements IWSLCommandDefinition {
		public void execute(WSLScript script, String arguments) {
			String[] windows = arguments.split("\\s+");
			for(String name : windows) {
				script.getCommands().clearWindow(name);
			}
		}
	}
	
	private class ScriptTimer extends WSLAbstractNumber {
		private long timerStart = -1L;
		private long timePast = 0L;
		
		public double toDouble() {
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
		
		public void execute(WSLScript script, String arguments) {
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
						script.scriptError("Unexpected result in AddHighlight.");
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
				script.scriptError("No \"string\" given.");
				return;
			}
			
			WarlockStyle style = new WarlockStyle();
			if(fgcolor != null)
				style.setForegroundColor(new WarlockColor(fgcolor));
			if(bgcolor != null)
				style.setBackgroundColor(new WarlockColor(bgcolor));
			style.setFullLine(fullLine);
			
			WarlockHighlight highlight = new WarlockHighlight(string);
			highlight.setLiteral(literal);
			highlight.setFullWordMatch(fullWord);
			highlight.setCaseSensitive(caseSensitive);
			highlight.setStyle(style);
			
			script.addHighlight(highlight);
		}
	}
	
	protected class WSLCommandDeleteHighlight implements IWSLCommandDefinition {
		private Pattern format = Pattern.compile("(\\S+)=(\"([^\"]*)\"|(\\S*))");
		
		public void execute(WSLScript script, String arguments) {
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
						script.scriptError("Unexpected result in DeleteHighlight.");
						return;
					}
				}
				
				if(key.equalsIgnoreCase("string")) {
					string = value;
				}
			}
			
			if(string == null) {
				script.scriptError("No \"string\" given.");
				return;
			}
			
			script.removeHighlight(string);
		}
	}
	
	private void doMath(WSLScript script, String targetVar, String arguments) {
		String[] args = arguments.split(argSeparator);
		if (args.length < 1) {
			script.scriptError("No operator for math");
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
				script.scriptError("Operand must be a number");
				return;
			}
		} else {
			operand = 1;
			operandIsDefault = true;
		}

		if ("set".equalsIgnoreCase(operator))
		{
			script.setGlobalVariable(targetVar, new WSLNumber(operand));
			return;
		}

		double value;
		if(script.variableExists(targetVar)) {
			try {
				value = script.getVariable(targetVar).toDouble();
			} catch(NumberFormatException e) {
				script.scriptError("The variable \"" + targetVar + "\" must be a number to do math with it");
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
				script.scriptError("Cannot divide by zero");
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
			script.scriptError("Unrecognized math command \"" + operator + "\"");
			return;
		}
		script.setGlobalVariable(targetVar, new WSLNumber(newValue));
	}
}
