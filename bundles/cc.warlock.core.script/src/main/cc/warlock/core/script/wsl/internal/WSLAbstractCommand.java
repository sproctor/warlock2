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


abstract public class WSLAbstractCommand implements IWSLCommand {
	
	private int lineNumber;
	private boolean instant = false;
	private WSLScript script;
	private boolean hasNext;
	private WSLAbstractCommand nextCommand = null;
	
	public WSLAbstractCommand(int lineNumber, WSLScript script) {
		this.lineNumber = lineNumber;
		this.setScript(script);
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public void setInstant(boolean i) {
		instant = i;
	}
	
	public boolean isInstant() {
		return instant;
	}
	
	abstract public void execute(WSLScriptContext cx) throws InterruptedException;

	final public WSLAbstractCommand getNext() {
		if(hasNext)
			return nextCommand;
		WSLAbstractCommand next = null;
		int i = this.getLineNumber() + 1;
		while(next == null && i < getScript().numLines()) {
			next = getScript().getLine(i);
			i++;
		}
		return next;
	}
	
	public void setNext(WSLAbstractCommand command) {
		nextCommand = command;
		hasNext = true;
	}

	public WSLScript getScript() {
		return script;
	}

	public void setScript(WSLScript script) {
		this.script = script;
	}
}
