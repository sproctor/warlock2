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
package cc.warlock.core.client.settings.macro.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IMacro;
import cc.warlock.core.client.settings.macro.IMacroProvider;
import cc.warlock.core.configuration.WarlockSetting;

/**
 * Macros defined by this provider are command-based only. 
 * Everything else will be a system macro, probably needing special configuration, or more foresight in this class.
 *  
 * @author marshall
 */
public class MacroConfigurationProvider extends WarlockSetting implements IMacroProvider {

	protected HashMap<String, MacroSetting> macros = new HashMap<String, MacroSetting>();
	
	private int nextID = 0;
	
	public MacroConfigurationProvider (Preferences parentNode)
	{
		super(parentNode, "macros");
		
		try {
			for(String macroId : getNode().childrenNames()) {
				try {
					int id = Integer.parseInt(macroId);
					if(id >= nextID)
						nextID = id + 1;
				} catch(NumberFormatException e) {
					// Don't care
				}
				macros.put(macroId, new MacroSetting(getNode(), macroId));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public MacroSetting createMacro() {
		MacroSetting macro = new MacroSetting(getNode(), Integer.toString(nextID));
		nextID++;
		macros.put(Integer.toString(nextID), macro);
		
		return macro;
	}

	public MacroSetting getMacro(int keycode, int modifiers) {
		for (MacroSetting macro : macros.values()) {
			if (macro.getKeyCode() == keycode && macro.getModifiers() == modifiers) {
				return macro;
			}
		}
		return null;
	}
	
	public MacroSetting getOrCreateMacro(int keycode, int modifiers) {
		MacroSetting macro = getMacro(keycode, modifiers);
		if(macro == null) {
			macro = createMacro();
			macro.setKeyCode(keycode);
			macro.setModifiers(modifiers);
		}
		
		return macro;
	}

	public Collection<MacroSetting> getMacros() {
		return macros.values();
	}

	public boolean removeMacro(IMacro macro) {
		for(Entry<String, MacroSetting> entry : macros.entrySet()) {
			if(entry.getValue().equals(macro)) {
				macros.remove(entry.getKey());
				return true;
			}
		}
		return false;
	}
	
	/*public void replaceMacro(IMacro originalMacro, IMacro newMacro) {
		int index = macros.indexOf(originalMacro);
		if (index > -1) {
			macros.set(index, newMacro);
		}
	}*/

}
