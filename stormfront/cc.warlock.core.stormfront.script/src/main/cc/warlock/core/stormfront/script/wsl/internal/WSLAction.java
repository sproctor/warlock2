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
package cc.warlock.core.stormfront.script.wsl.internal;

import java.util.regex.PatternSyntaxException;

import cc.warlock.core.script.internal.RegexMatch;
import cc.warlock.core.stormfront.script.wsl.WSLScript;
import cc.warlock.core.stormfront.script.wsl.WSLScriptContext;

public class WSLAction extends WSLAbstractCommand {

	private WSLAbstractCommand command;
	private IWSLValue when;
	private RegexMatch match;
	
	public WSLAction(int lineNum, WSLScript script, WSLAbstractCommand command, IWSLValue when) {
		super(lineNum, script);
		
		this.command = command;
		this.when = when;
	}
	
	private class WSLActionAdapter implements Runnable {
		
		public void run() {
			/*
			script.setVariablesFromMatch(match);
			try {
				if(!command.isInstant())
					script.getCommands().waitForRoundtime();
				while(script.getCommands().isSuspended()) {
					script.getCommands().waitForResume();
					if(!command.isInstant())
						script.getCommands().waitForRoundtime();
				}
				command.execute();
			} catch(InterruptedException e) {
				// TODO - what to do here?
			}*/
		}
	}
	
	public void execute(WSLScriptContext cx) {
		try {
			match = new RegexMatch(when.toString(cx).trim());
			cx.scriptDebug(2, "Action added \"" + command + "\" when \"" + when + "\"");
			cx.getScript().getCommands().addAction(new WSLActionAdapter(), match);
		} catch(PatternSyntaxException e) {
			cx.scriptError("Bad regex \"" + when.toString(cx).trim() + "\" in action");
		}
	}

}
