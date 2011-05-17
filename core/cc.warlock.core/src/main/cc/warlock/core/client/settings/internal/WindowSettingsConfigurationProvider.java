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

import cc.warlock.core.client.settings.IWindowSettings;
import cc.warlock.core.client.settings.IWindowSettingsProvider;
import cc.warlock.core.configuration.ConfigurationProvider;

public class WindowSettingsConfigurationProvider extends ConfigurationProvider implements IWindowSettingsProvider {

	protected HashMap<String, IWindowSettings> windowSettings = new HashMap<String, IWindowSettings>();
	
	public WindowSettingsConfigurationProvider(Preferences parentNode) {
		super(parentNode, "windows");
		
		try {
			for(String windowId : getNode().childrenNames()) {
				windowSettings.put(windowId, new WindowSettings(getNode(), windowId));
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Collection<IWindowSettings> getWindowSettings() {
		return windowSettings.values();
	}

	public IWindowSettings getWindowSettings(String windowId) {
		return windowSettings.get(windowId);
	}

	public IWindowSettings getOrCreateWindowSettings(String windowId) {
		IWindowSettings settings = windowSettings.get(windowId);
		if(settings == null) {
			settings = new WindowSettings(getNode(), windowId);
			windowSettings.put(windowId, settings);
		}
		return settings;
	}
	
	public void removeWindowSettings(String id) {
		windowSettings.remove(id);
		getNode().remove(id);
	}

}
