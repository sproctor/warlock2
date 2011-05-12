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
package cc.warlock.core.client.settings;

import java.util.Collection;
import java.util.List;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.settings.macro.IMacro;
import cc.warlock.core.client.settings.macro.IMacroCommand;
import cc.warlock.core.client.settings.macro.IMacroVariable;

/**
 * @author marshall
 *
 */
public interface IClientSettings {

	public IWarlockClient getClient();
	
	public int getVersion();
	
	public Collection<IHighlightString> getHighlightStrings();
	public IWarlockStyle getNamedStyle(String name);
	
	public Collection<IVariable> getVariables();
	public IVariable getVariable(String identifier);
	
	public Collection<IIgnore> getIgnores();
	
	public Collection<IWindowSettings> getWindowSettings();
	public IWindowSettings getWindowSettings (String windowId);
	
	public Collection<IMacro> getMacros();
	public IMacro getMacro (int keycode, int modifiers);
	
	public List<? extends IMacroVariable> getAllMacroVariables();
	public IMacroVariable getMacroVariable (String id);
	
	public List<? extends IMacroCommand> getAllMacroCommands();
	public IMacroCommand getMacroCommand(String id);

	public IWindowSettings getMainWindowSettings();

	void dispose();
}
