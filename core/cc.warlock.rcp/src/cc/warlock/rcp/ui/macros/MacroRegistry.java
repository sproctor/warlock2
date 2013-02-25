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

import java.util.ArrayList;
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
import cc.warlock.core.util.Pair;
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
	public ArrayList<Pair<String, Integer>> keys = new ArrayList<Pair<String, Integer>>();
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
		addMacro(new WarlockMacro("{Return}", getKeyString(SWT.CR, 0)));
		addMacro(new WarlockMacro("{StopScript}", getKeyString(SWT.ESC, 0)));
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
		outerloop: while(true) {
			for(Map.Entry<String, Integer> entry : mods.entrySet()) {
				if(keyString.startsWith(entry.getKey() + "+")) {
					keyString = keyString.substring(entry.getKey().length() + 1);
					continue outerloop;
				}	
			}
			break;
		}
		Integer keycode = null;
		for(Pair<String, Integer> mapping : keys) {
			if(mapping.first().equals(keyString)) {
				keycode = mapping.second();
				break;
			}
		}
		if(keycode != null)
			return keycode;
		if (keyString.length() == 0)
			return 0;
		return keyString.charAt(0);
	}
	
	public int getModifiers(String keyString) {
		int modifiers = 0;
		for(Map.Entry<String, Integer> entry : mods.entrySet()) {
			if(keyString.startsWith(entry.getKey() + "+")) {
				keyString = keyString.substring(entry.getKey().length() + 1);
				modifiers |= entry.getValue();
			}
				
		}
		return modifiers;
	}
	
	public String getKeyString(int keycode, int modifiers) {
		String key = null;
		for(Pair<String, Integer> entry : keys) {
			if(keycode == entry.second()) {
				key = entry.first();
				break;
			}
		}
		if(key == null)
			key = String.valueOf((char)keycode);
		
		String modString = "";
		for(Map.Entry<String, Integer> entry : mods.entrySet()) {
			if((modifiers & entry.getValue()) != 0) {
				modString += entry.getKey() + "+";
			}
		}
		
		return modString + key;
	}
	
	private void mapKey(String keyName, int keyCode) {
		keys.add(new Pair<String, Integer>(keyName, keyCode));
	}
	
	private void loadKeys() {
		mapKey("Keypad 0", SWT.KEYPAD_0);
		mapKey("Keypad 1", SWT.KEYPAD_1);
		mapKey("Keypad 2", SWT.KEYPAD_2);
		mapKey("Keypad 3", SWT.KEYPAD_3);
		mapKey("Keypad 4", SWT.KEYPAD_4);
		mapKey("Keypad 5", SWT.KEYPAD_5);
		mapKey("Keypad 6", SWT.KEYPAD_6);
		mapKey("Keypad 7", SWT.KEYPAD_7);
		mapKey("Keypad 8", SWT.KEYPAD_8);
		mapKey("Keypad 9", SWT.KEYPAD_9);
		mapKey("Keypad +", SWT.KEYPAD_ADD);
		mapKey("Keypad /", SWT.KEYPAD_DIVIDE);
		mapKey("Keypad *", SWT.KEYPAD_MULTIPLY);
		mapKey("Keypad -", SWT.KEYPAD_SUBTRACT);
		mapKey("Keypad .", SWT.KEYPAD_DECIMAL);
		mapKey("Keypad Enter", SWT.KEYPAD_CR);
		mapKey("Enter", (int)SWT.CR);
		
		mapKey("F1", SWT.F1);
		mapKey("F2", SWT.F2);
		mapKey("F3", SWT.F3);
		mapKey("F4", SWT.F4);
		mapKey("F5", SWT.F5);
		mapKey("F6", SWT.F6);
		mapKey("F7", SWT.F7);
		mapKey("F8", SWT.F8);
		mapKey("F9", SWT.F9);
		mapKey("F10", SWT.F10);
		mapKey("F11", SWT.F11);
		mapKey("F12", SWT.F12);
		
		mapKey("Page Up", SWT.PAGE_UP);
		mapKey("Page Down", SWT.PAGE_DOWN);
		mapKey("Up", SWT.ARROW_UP);
		mapKey("Down", SWT.ARROW_DOWN);
		mapKey("Tab", (int)SWT.TAB);
		mapKey("Home", SWT.HOME);
		mapKey("End", SWT.END);
		mapKey("Esc", (int) SWT.ESC);
		
		mods.put("Alt", SWT.ALT);
		mods.put("Shift", SWT.SHIFT);
		mods.put("Ctrl", SWT.CTRL);
		mods.put("Cmd", SWT.COMMAND);
	}
}
