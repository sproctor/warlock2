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
package cc.warlock.core.script.wsl.internal;

import cc.warlock.core.script.wsl.WSLScript;
import cc.warlock.core.script.wsl.WSLScriptContext;

public class WSLActionRemove extends WSLAbstractCommand {

	private IWSLValue when;
	
	public WSLActionRemove(int lineNum, WSLScript script, IWSLValue when) {
		super(lineNum, script);
		
		this.when = when;
	}

	public void execute(WSLScriptContext cx) {
		cx.getScript().getCommands().removeAction(when.toString(cx));
		cx.scriptDebug(2, "Action removed \"" + when + "\"");
	}

	@Override
	public String toString() {
		return "action remove " + when.toString(null);
	}
}
