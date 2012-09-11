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

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.internal.ArrayConfigurationProvider;
import cc.warlock.core.client.settings.macro.IMacroProvider;

/**
 * Macros defined by this provider are command-based only.
 *  
 * @author marshall
 */
public class MacroConfigurationProvider extends ArrayConfigurationProvider<MacroSetting> implements IMacroProvider {
	
	public MacroConfigurationProvider (Preferences parentNode)
	{
		super(parentNode, "macros");
	}
	
	protected MacroSetting loadSetting(String id) {
		boolean loading = false;
		try {
			loading = getNode().nodeExists(id);
		} catch(Exception e) {
			// don't need to do anything
		}
		MacroSetting macro = new MacroSetting(getNode(), id);
		
		// Old macros had no keyString. Remove them if we come across them
		if (loading && macro.getKeyString() == null) {
			this.removeSetting(macro);
			return null;
		}
		
		return macro;
	}
	
	public MacroSetting getMacro(String keyString) {
		for (MacroSetting macro : settings.values()) {
			if (macro.getKeyString().equals(keyString))
				return macro;
		}
		return null;
	}
}
