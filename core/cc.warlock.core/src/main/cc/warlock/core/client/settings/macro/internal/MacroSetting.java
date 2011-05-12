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
package cc.warlock.core.client.settings.macro.internal;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IMacro;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.settings.internal.ClientSetting;
import cc.warlock.core.client.settings.macro.CommandMacroHandler;
import cc.warlock.core.client.settings.macro.IMacroHandler;

public class MacroSetting extends ClientSetting implements IMacro
{
	protected int keycode;
	protected int modifiers;
	private String command;
	protected IMacroHandler handler;
	private boolean updateDeferred;
	
	public MacroSetting (Preferences parentNode, String path)
	{
		super(parentNode, path);
		
		keycode = getNode().getInt("keycode", -1);
		modifiers = getNode().getInt("modifiers", -1);
		command = getNode().get("command", null);
		updateDeferred = true;
	}
	
	public int getKeyCode() {
		return this.keycode;
	}
	
	public void setKeyCode(int keycode) {
		getNode().putInt("keycode", keycode);
		
		this.keycode = keycode;
	}
	
	public int getModifiers() {
		return modifiers;
	}
	
	public void setModifiers(int modifiers) {
		getNode().putInt("modifiers", modifiers);
		
		this.modifiers = modifiers;
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
	
	@Override
	public String toString() {
		String str = "Macro (keycode="+keycode+",modifiers="+modifiers+",handler="+handler.toString();
		return str;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IMacro) {
			IMacro other = (IMacro)obj;
			
			return (keycode == other.getKeyCode() && modifiers == other.getModifiers());
		}
		return super.equals(obj);
	}
	
}