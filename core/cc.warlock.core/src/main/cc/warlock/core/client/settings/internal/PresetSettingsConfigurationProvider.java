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
package cc.warlock.core.client.settings.internal;

import java.util.Collection;
import java.util.HashMap;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.configuration.ConfigurationProvider;
import cc.warlock.core.configuration.IConfigurationProvider;

public class PresetSettingsConfigurationProvider extends ConfigurationProvider implements IConfigurationProvider {

	protected HashMap<String, IWarlockStyle> styleSettings = new HashMap<String, IWarlockStyle>();
	
	public PresetSettingsConfigurationProvider(Preferences parentNode) {
		super(parentNode, "presets");
		
		try {
			for(String id : getNode().childrenNames()) {
				styleSettings.put(id, new StyleSetting(getNode(), id, true));
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Collection<IWarlockStyle> getStyles() {
		return styleSettings.values();
	}

	public IWarlockStyle getStyle(String id) {
		return styleSettings.get(id);
	}

	public IWarlockStyle getOrCreateStyle(String id) {
		IWarlockStyle style = styleSettings.get(id);
		if(style == null) {
			style = new StyleSetting(getNode(), id);
			styleSettings.put(id, style);
		}
		return style;
	}

	public void removeStyle(String id) {
		styleSettings.remove(id);
		getNode().remove(id);
	}
}
