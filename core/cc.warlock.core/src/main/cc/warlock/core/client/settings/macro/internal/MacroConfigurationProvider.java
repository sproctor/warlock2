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

import org.osgi.service.prefs.BackingStoreException;
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
	
	public MacroConfigurationProvider (Preferences parentNode)
	{
		super(parentNode, "macros");
		
		try {
			for(String macroId : getNode().childrenNames()) {
				macros.put(macroId, new MacroSetting(getNode(), macroId));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public MacroSetting createMacro(String keyString) {
		MacroSetting macro = macros.get(keyString);
		if(macro == null) {
			macro = new MacroSetting(getNode(), keyString);
			macros.put(keyString, macro);
		}
		
		return macro;
	}

	public MacroSetting getMacro(String keyString) {
		return macros.get(keyString);
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
