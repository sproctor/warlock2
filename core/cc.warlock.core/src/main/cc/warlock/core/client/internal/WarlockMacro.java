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
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.settings.macro.IMacroHandler;

public class WarlockMacro implements IMacro
{
	protected int keycode;
	protected int modifiers;
	protected IMacroHandler handler;
	
	public WarlockMacro (int keycode, int modifiers, IMacroHandler handler)
	{
		this.keycode = keycode;
		this.modifiers = modifiers;
		this.handler = handler;
	}
	
	public WarlockMacro (int keycode, IMacroHandler handler)
	{
		this(keycode, IMacro.NO_MODIFIERS, handler);
	}
	
	public int getKeyCode() {
		return this.keycode;
	}
	
	public void setKeyCode(int keycode) {
		this.keycode = keycode;
	}
	
	public int getModifiers() {
		return modifiers;
	}
	
	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
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