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
import cc.warlock.core.script.IScriptFileInfo;
import cc.warlock.core.script.internal.RegexMatch;
import cc.warlock.core.script.internal.TextMatch;
import cc.warlock.core.script.javascript.JavascriptScript.StopException;

public class JavascriptCommands {

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
	
	public JavascriptCommands(JavascriptScript script) {
		this.script = script;
	}
	
	public void echo(String text) throws InterruptedException {
		script.checkStop();
		
		script.getCommands().echo(text);
	}
	
	public void echo()  throws InterruptedException {
		script.checkStop();
		
		script.getCommands().echo("");
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
				script.getCommands().debug("ERROR:  Cannot include specified file: " + otherScript);
			}
		}
		return false;
	}

	public void move(String direction) {
		script.checkStop();
		
		try {
			// TODO: figure out the line number
			script.getCommands().move(direction, -1);
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}

	public void pause(double seconds) {
		script.checkStop();
		
		try {
			script.getCommands().pause(seconds);
			script.getCommands().waitForRoundtime();
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}
	
	public void pause() {
		script.checkStop();
		
		try {
			script.getCommands().waitForRoundtime();
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}

	public void put(String text) {
		script.checkStop();
		
		try {
			script.getCommands().put(text, -1);
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
			script.getCommands().waitFor(match);
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}

	public void waitForPrompt() {
		script.checkStop();
		
		try {
			script.getCommands().waitForPrompt();
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}

	public void exit() throws Error {
		script.getCommands().stop();
		
		throw script.new StopException();
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
				script.getCommands().debug(e.getMessage());
				this.cancel();
			} catch(StopException e) {
				this.cancel();
			} catch(Exception e) {
				script.getCommands().debug(e.getMessage());
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
				script.getCommands().playSound(new FileInputStream(file));
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
		script.getCommands().addAction(command, match);
	}

	public void removeAction(String text) {
		script.checkStop();

		script.getCommands().removeAction(text);
	}

	public void removeAction(IMatch action) {
		script.checkStop();

		script.getCommands().removeAction(action);
	}

	public void clearActions() {
		script.checkStop();

		script.getCommands().clearActions();
	}

	public void waitForRoundtime() {
		script.checkStop();

		try {
			script.getCommands().waitForRoundtime();
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}

	public void waitNextRoom() {
		script.checkStop();

		try {
			script.getCommands().waitNextRoom();
		} catch(InterruptedException e) {
			script.checkStop();
		}
	}

	public String getComponent(String component) {
		return script.getClient().getComponent(component);
	}

	public String getVariable(String name) {
		return script.getCommands().getStoredVariable(name);
	}

	public String getVital(String name) {
		return script.getClient().getProperty(name).get();
	}

	public void setVariable(String name, String value) {
		script.getCommands().setStoredVariable(name, value);
	}
}
