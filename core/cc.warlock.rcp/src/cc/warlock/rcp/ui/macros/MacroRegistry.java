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
package cc.warlock.rcp.ui.macros;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.swt.SWT;

import cc.warlock.core.client.IMacro;
import cc.warlock.core.client.IMacroCommand;
import cc.warlock.core.client.IMacroVariable;
import cc.warlock.core.client.internal.WarlockMacro;
import cc.warlock.rcp.plugin.Warlock2Plugin;


/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MacroRegistry {
	private static MacroRegistry instance = new MacroRegistry();
	
	private HashMap<String, IMacro> macros = new HashMap<String, IMacro>();
	
	// TODO - determine if variables and commands should be synchronized
	private HashMap<String, IMacroVariable> variables = new HashMap<String, IMacroVariable>();
	private HashMap<String, IMacroCommand> commands = new HashMap<String, IMacroCommand>();
	public HashMap<String, Integer> keys = new HashMap<String, Integer>();
	public HashMap<String, Integer> mods = new HashMap<String, Integer>();
	
	private MacroRegistry () {
		loadKeys();
		loadBaseMacros();
		loadVariables();
		loadCommands();
	}
	
	public static MacroRegistry instance ()
	{	
		return instance;
	}
	
	public Collection<IMacroVariable> getMacroVariables ()
	{
		return variables.values();
	}
	
	public Collection<IMacroCommand> getMacroCommands() {
		return commands.values();
	}
	
	private void addMacro (IMacro macro)
	{
		macros.put(macro.getKeyString(), macro);
	}
	
	private void loadBaseMacros () {
		addMacro(new WarlockMacro(getKeyString(SWT.CR, 0), "{Return}"));
		addMacro(new WarlockMacro(getKeyString(SWT.ESC, 0), "{StopScript}"));
	}
	
	public IMacro getMacro(int keycode, int modifiers) {
		return macros.get(getKeyString(keycode, modifiers));
	}
	
	public void setMacroVariable(String id, IMacroVariable variable) {
		variables.put(id, variable);
	}
	
	public IMacroVariable getMacroVariable(String id) {
		return variables.get(id);
	}
	
	public void removeMacroVariable(IMacroVariable variable) {
		variables.remove(variable.getIdentifier());
	}
	
	public void addMacroCommand(IMacroCommand command) {
		commands.put(command.getIdentifier(), command);
	}
	
	public IMacroCommand getMacroCommand(String id) {
		return commands.get(id);
	}
	
	private void loadCommands () {
		try {
			IExtension[] extensions = Warlock2Plugin.getDefault().getExtensions("cc.warlock.rcp.macroCommands");
			for (int i = 0; i < extensions.length; i++) {
				IExtension ext = extensions[i];
				IConfigurationElement[] ce = ext.getConfigurationElements();
				
				for (int j = 0; j < ce.length; j++) {
					Object obj = ce[j].createExecutableExtension("classname");
					
					if (obj instanceof IMacroCommand)
					{
						IMacroCommand command = (IMacroCommand) obj;
						commands.put(command.getIdentifier(), command);
					}
				}
			}
		} catch (InvalidRegistryObjectException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private void loadVariables () {
		try {
			IExtension[] extensions = Warlock2Plugin.getDefault().getExtensions("cc.warlock.rcp.macroVariables");
			for (int i = 0; i < extensions.length; i++) {
				IExtension ext = extensions[i];
				IConfigurationElement[] ce = ext.getConfigurationElements();
				
				for (int j = 0; j < ce.length; j++) {
					Object obj = ce[j].createExecutableExtension("classname");
					
					if (obj instanceof IMacroVariable)
					{
						IMacroVariable var = (IMacroVariable) obj;
						variables.put(var.getIdentifier(), var);
					}
				}
			}
		} catch (InvalidRegistryObjectException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public int getKeycode(String keyString) {
		for(Map.Entry<String, Integer> entry : mods.entrySet()) {
			if(keyString.startsWith(entry.getKey() + "-")) {
				keyString = keyString.substring(entry.getKey().length() + 1);
			}
		}
		Integer keycode = keys.get(keyString);
		if(keycode != null)
			return keycode;
		if (keyString.length() == 0)
			return 0;
		return keyString.charAt(0);
	}
	
	public int getModifiers(String keyString) {
		int modifiers = 0;
		for(Map.Entry<String, Integer> entry : mods.entrySet()) {
			if(keyString.startsWith(entry.getKey() + "-")) {
				keyString = keyString.substring(entry.getKey().length() + 1);
				modifiers |= entry.getValue();
			}
				
		}
		return modifiers;
	}
	
	public String getKeyString(int keycode, int modifiers) {
		String key = null;
		for(Map.Entry<String, Integer> entry : keys.entrySet()) {
			if(keycode == entry.getValue()) {
				key = entry.getKey();
				break;
			}
		}
		if(key == null)
			key = String.valueOf((char)keycode);
		
		String modString = "";
		for(Map.Entry<String, Integer> entry : mods.entrySet()) {
			if((modifiers & entry.getValue()) != 0) {
				modString = modString + entry.getKey() + "-";
			}
		}
		
		return modString + key;
	}
	
	private void loadKeys() {
		keys.put("Keypad 0", SWT.KEYPAD_0);
		keys.put("Keypad 1", SWT.KEYPAD_1);
		keys.put("Keypad 2", SWT.KEYPAD_2);
		keys.put("Keypad 3", SWT.KEYPAD_3);
		keys.put("Keypad 4", SWT.KEYPAD_4);
		keys.put("Keypad 5", SWT.KEYPAD_5);
		keys.put("Keypad 6", SWT.KEYPAD_6);
		keys.put("Keypad 7", SWT.KEYPAD_7);
		keys.put("Keypad 8", SWT.KEYPAD_8);
		keys.put("Keypad 9", SWT.KEYPAD_9);
		keys.put("Keypad +", SWT.KEYPAD_ADD);
		keys.put("Keypad /", SWT.KEYPAD_DIVIDE);
		keys.put("Keypad *", SWT.KEYPAD_MULTIPLY);
		keys.put("Keypad -", SWT.KEYPAD_SUBTRACT);
		keys.put("Keypad .", SWT.KEYPAD_DECIMAL);
		keys.put("Keypad Enter", SWT.KEYPAD_CR);
		keys.put("Enter", (int)SWT.CR);
		
		keys.put("F1", SWT.F1);
		keys.put("F2", SWT.F2);
		keys.put("F3", SWT.F3);
		keys.put("F4", SWT.F4);
		keys.put("F5", SWT.F5);
		keys.put("F6", SWT.F6);
		keys.put("F7", SWT.F7);
		keys.put("F8", SWT.F8);
		keys.put("F9", SWT.F9);
		keys.put("F10", SWT.F10);
		keys.put("F11", SWT.F11);
		keys.put("F12", SWT.F12);
		
		keys.put("Page Up", SWT.PAGE_UP);
		keys.put("Page Down", SWT.PAGE_DOWN);
		keys.put("UP", SWT.ARROW_UP);
		keys.put("DOWN", SWT.ARROW_DOWN);
		keys.put("Tab", (int)SWT.TAB);
		keys.put("Home", SWT.HOME);
		keys.put("End", SWT.END);
		keys.put("Esc", (int) SWT.ESC);
		
		mods.put("Alt", SWT.ALT);
		mods.put("Shift", SWT.SHIFT);
		mods.put("Ctrl", SWT.CTRL);
		mods.put("Cmd", SWT.COMMAND);
	}
}
