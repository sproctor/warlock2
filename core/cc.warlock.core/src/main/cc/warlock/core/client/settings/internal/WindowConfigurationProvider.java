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
import java.util.Collections;
import java.util.HashMap;

import cc.warlock.core.client.IWarlockFont;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.client.settings.IWindowSettings;
import cc.warlock.core.client.settings.IWindowSettingsProvider;
import cc.warlock.core.configuration.IWarlockSetting;
import cc.warlock.core.configuration.WarlockSetting;

public class WindowConfigurationProvider extends WarlockSetting implements IWindowSettingsProvider
{
	public static final String ID = "windows";
	public static final String WINDOW_MAIN = "main";
	
	// TODO: store these in settings
	public static WarlockColor defaultBgColor = new WarlockColor("191932");
	public static WarlockColor defaultFgColor = new WarlockColor("#F0F0FF");
	
	protected HashMap<String, IWindowSettings> windowSettings = new HashMap<String, IWindowSettings>();
	
	public WindowConfigurationProvider(IWarlockSetting parent) {
		super(parent, ID);
		
		try {
			for(String windowId : getNode().childrenNames()) {
				windowSettings.put(windowId, new WindowSettings(this, windowId));
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Collection<IWindowSettings> getWindowSettings() {
		return Collections.unmodifiableCollection(windowSettings.values());
	}

	public IWindowSettings getWindowSettings(String windowId) {
		return windowSettings.get(windowId);
	}

	public IWindowSettings getOrCreateWindowSettings(String windowId) {
		IWindowSettings settings = windowSettings.get(windowId);
		if(settings == null) {
			settings = new WindowSettings(this, windowId);
			windowSettings.put(windowId, settings);
		}
		return settings;
	}
	
	public IWindowSettings getMainWindowSettings() {
		return getOrCreateWindowSettings(WINDOW_MAIN);
	}
	
	public void removeWindowSettings(String id) {
		windowSettings.remove(id);
		getNode().remove(id);
	}

	public WarlockColor getDefaultBackground() {
		WarlockColor bg = null;
		IWindowSettings mainWindow = getMainWindowSettings();
		if(mainWindow != null)
			bg = mainWindow.getBackgroundColor();
		if(bg == null || bg.isDefault())
			bg = defaultBgColor;
		return bg;
	}
	
	public WarlockColor getDefaultForeground() {
		WarlockColor fg = null;
		IWindowSettings mainWindow = getMainWindowSettings();
		if(mainWindow != null)
			fg = mainWindow.getForegroundColor();
		if(fg == null || fg.isDefault())
			fg = defaultFgColor;
		return fg;
	}
	

	public WarlockColor getWindowBackground(String windowId) {
		WarlockColor bg = null;
		IWindowSettings wsettings = getWindowSettings(windowId);
		if(wsettings != null)
			bg = wsettings.getBackgroundColor();
		if(bg == null || bg.isDefault())
			bg = getDefaultBackground();
		return bg;
	}
	
	public WarlockColor getWindowForeground(String windowId) {
		WarlockColor fg = null;
		IWindowSettings wsettings = getWindowSettings(windowId);
		if(wsettings != null)
			fg = wsettings.getForegroundColor();
		if(fg == null || fg.isDefault())
			fg = getDefaultForeground();
		return fg;
	}
	
	public IWarlockFont getWindowFont(String windowId) {
		IWarlockFont font = null;
		IWindowSettings wsettings = getWindowSettings(windowId);
		if(wsettings != null)
			font = wsettings.getFont();
		if(font == null || font.isDefaultFont()) {
			IWindowSettings mainWindow = getMainWindowSettings();
			if(mainWindow != null)
				font = mainWindow.getFont();
		}
		return font;
	}
	
	public static WindowConfigurationProvider getProvider(IClientSettings clientSettings) {
		return (WindowConfigurationProvider)clientSettings.getProvider(ID);
	}
}
