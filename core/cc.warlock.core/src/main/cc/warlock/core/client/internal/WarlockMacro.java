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
/*
 * Created on Mar 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.client.internal;

import cc.warlock.core.client.IMacro;
import cc.warlock.core.client.IMacroHandler;
import cc.warlock.core.client.IWarlockClientViewer;

public class WarlockMacro implements IMacro {
	private String keyString;
	private String command;
	private IMacroHandler handler;
	
	public static String getKeyString(String key, int modifiers) {
		String keyString = key;
		if((modifiers | IMacro.ALT) != 0)
			keyString = "Alt+" + keyString;
		if((modifiers | IMacro.CTRL) != 0)
			keyString = "Ctrl+" + keyString;
		return keyString;
	}
	
	public WarlockMacro (String command, String keyString) {
		this.keyString = keyString;
		this.command = command;
		this.handler = new CommandMacroHandler(command);
	}
	
	public WarlockMacro (String command, String key, int modifiers) {
		this(command, getKeyString(key, modifiers));
	}
	
	
	public String getKeyString() {
		return keyString;
	}
	
	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}
	
	public void setKeyString(String key, int modifiers) {
		setKeyString(getKeyString(key, modifiers));
	}
	
	public void setHandler(IMacroHandler handler) {
		this.handler = handler;
	}
	
	public IMacroHandler getHandler() {
		return this.handler;
	}
	
	public void execute(IWarlockClientViewer viewer) {
		handler.handleMacro(this, viewer);
	}
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
}