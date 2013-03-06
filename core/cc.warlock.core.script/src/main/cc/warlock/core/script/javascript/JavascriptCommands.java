/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.core.script.javascript;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;

import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.script.IScriptFileInfo;
import cc.warlock.core.script.internal.RegexMatch;
import cc.warlock.core.script.internal.TextMatch;
import cc.warlock.core.script.javascript.JavascriptScript.StopException;

public class JavascriptCommands {

	private IScriptCommands commands;
	private JavascriptScript script;
	private HashMap<Integer, TimerTask> timeTasks = new HashMap<Integer, TimerTask>();
	private int nextTimerID = 1;
	
	protected class JSActionHandler implements Runnable 
	{
		private Function function;
		private RegexMatch match;
		
		public JSActionHandler (Function function, RegexMatch match)
		{
			this.function = function;
			this.match = match;
		}
		
		public void run() {
			Context.enter();
			try {
				Object[] arguments = new Object[0];
				Collection<String> matchGroups = match.groups();
				arguments = matchGroups.toArray(new String[matchGroups.size()]);
				
				function.call(getScript().getContext(), getScript().getScope(), null, arguments);
			} finally {
				Context.exit();
			}
		}
	}
	
	public JavascriptCommands(IScriptCommands commands, JavascriptScript script) {
		this.commands = commands;
		this.script = script;
	}
	
	public void echo(String text) throws InterruptedException {
		script.checkStop();
		
		commands.echo(text);
	}
	
	public void echo()  throws InterruptedException {
		script.checkStop();
		
		commands.echo("");
	}

	public boolean include (String otherScript)
	{
		script.checkStop();
		
		if (script.getScriptInfo() instanceof IScriptFileInfo)
		{
			IScriptFileInfo info = (IScriptFileInfo) script.getScriptInfo();
			
			File scriptFile = new File(otherScript);
			if (!scriptFile.exists())
			{
				scriptFile = new File(info.getScriptFile().getParentFile(), otherScript);
			}
			if (scriptFile.exists()) {
				try {
					FileReader reader = new FileReader(scriptFile);
					
					Script includedScript = 
						script.getContext().compileReader(reader, scriptFile.getName(), 1, null);
					
					includedScript.exec(script.getContext(), script.getScope());
					
					return true;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				commands.debug("ERROR:  Cannot include specified file: " + otherScript);
			}
		}
		return false;
	}

	public void move(String direction) {
		script.checkStop();
		
		try {
			// TODO: figure out the line number
			commands.move(direction,0);
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}

	public void pause(double seconds) {
		script.checkStop();
		
		try {
			commands.pause(seconds);
			commands.waitForRoundtime();
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}

	public void put(String text) {
		script.checkStop();
		
		try {
			commands.put(text,0);
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}
	
	public void waitFor(String string)
	{
		script.checkStop();
		
		waitFor(new TextMatch(string, true));
	}
	
	// Default to case sensitivity
	public void waitForRe(String string) {
		script.checkStop();
		
		waitForRe(string, false);
	}
	
	public void waitForRe(String string, Boolean ignoreCase)
	{
		script.checkStop();
		
		waitFor(new RegexMatch(string, ignoreCase));
	}

	public void waitFor(IMatch match) {
		script.checkStop();
		
		try {
			commands.waitFor(match);
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}

	public void waitForPrompt() {
		script.checkStop();
		
		try {
			commands.waitForPrompt();
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}

	public void exit() throws Error {
		commands.stop();
		
		throw script.new StopException();
	}
	
	public IScriptCommands getScriptCommands ()
	{
		script.checkStop();
		
		return commands;
	}
	
	public JavascriptScript getScript ()
	{
		return script;
	}
	
	private class CommandCallback extends TimerTask {
		private String command;
		
		public CommandCallback(String command) {
			this.command = command;
		}
		
		public void run() {
			Context.enter();
			try {
				script.checkStop();
				
				Script jsCommand = script.getContext().compileString(command, "callback", 0, null);

				jsCommand.exec(script.getContext(), script.getScope());
			} catch(EcmaError e) {
				commands.debug(e.getMessage());
				this.cancel();
			} catch(StopException e) {
				this.cancel();
			} catch(Exception e) {
				commands.debug(e.getMessage());
				e.printStackTrace();
				this.cancel();
			} finally {
				Context.exit();
			}
		}
	}
	
	public int setInterval(String command, double interval) {
		script.checkStop();
		
		int id = nextTimerID++;
		Timer timer = new Timer();
		
		CommandCallback c = new CommandCallback(command);
		timeTasks.put(id, c);
		timer.scheduleAtFixedRate(c, (long)(interval * 1000.0), (long)(interval * 1000.0));
		
		return id;
	}
	
	public int setTimeout(String command, double timeout) {
		script.checkStop();
		
		int id = nextTimerID++;
		Timer timer = new Timer();
		
		CommandCallback c = new CommandCallback(command);
		timeTasks.put(id, c);
		timer.schedule(c, (long)(timeout * 1000.0));
		
		return id;
	}
	
	public void playSound (String filename) {
		File file = new File(filename);
		if (file.exists()) {
			try {
				commands.playSound(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// IStormFrontScriptCommands delegated methods
	public void addAction(Function action, String text) {
		script.checkStop();

		RegexMatch match = new RegexMatch(text);
		JSActionHandler command = new JSActionHandler(action, match);
		commands.addAction(command, match);
	}

	public void removeAction(String text) {
		script.checkStop();

		commands.removeAction(text);
	}

	public void removeAction(IMatch action) {
		script.checkStop();

		commands.removeAction(action);
	}

	public void clearActions() {
		script.checkStop();

		commands.clearActions();
	}

	public void waitForRoundtime() {
		script.checkStop();

		try {
			commands.waitForRoundtime();
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}

	public void waitNextRoom() {
		script.checkStop();

		try {
			commands.waitNextRoom();
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}

	public String getComponent(String component) {
		return commands.getClient().getComponent(component);
	}

	public String getVariable(String name) {
		return commands.getStoredVariable(name);
	}

	public String getVital(String name) {
		return commands.getClient().getProperty(name).get();
	}

	public void setVariable(String name, String value) {
		commands.setStoredVariable(name, value);
	}
}
