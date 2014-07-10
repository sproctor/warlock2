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
package cc.warlock.core.settings;

import cc.warlock.core.client.IMacro;
import cc.warlock.core.client.IMacroHandler;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.internal.CommandMacroHandler;
import cc.warlock.core.client.internal.WarlockMacro;

public class MacroSetting extends WarlockSetting implements IMacro, IWarlockSetting
{
	private String keyString;
	private String command;
	private IMacroHandler handler;
	private boolean updateDeferred;
	
	public MacroSetting (IWarlockSetting parent, String path)
	{
		super(parent, path);
		
		this.keyString = getNode().get("keystring", null);
		this.command = getNode().get("command", null);
		updateDeferred = true;
	}
	
	public static String encodePath(String keyString) {
		String path = keyString.replaceAll("\\\\", "\\\\");
		path = path.replaceAll("/", "\\\\f");
		return path;
	}
	
	public static String decodePath(String path) {
		String keyString = path.replaceAll("\\\\\\\\", "\\\\");
		keyString = keyString.replaceAll("\\\\f", "/");
		return keyString;
	}
	
	public String getKeyString() {
		return keyString;
	}
	
	public void setKeyString(String keyString) {
		this.keyString = keyString;
		getNode().put("keystring", keyString);
		updateDeferred = true;
	}
	
	public void setKeyString(String key, int modifiers) {
		setKeyString(WarlockMacro.getKeyString(key, modifiers));
	}
	
	public void setCommand(String command) {
		getNode().put("command", command);
		
		updateDeferred = true;
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}
	
	private void updateHandler() {
		handler = new CommandMacroHandler(command);
		updateDeferred = false;
	}
	
	public IMacroHandler getHandler() {
		if(updateDeferred)
			updateHandler();
		
		return this.handler;
	}
	
	public void execute(IWarlockClientViewer viewer) {
		if(updateDeferred)
			updateHandler();
		
		handler.handleMacro(this, viewer);
	}
}