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
import java.util.Collections;
import java.util.HashMap;

import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.IWarlockFont;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.settings.IWarlockSetting;
import cc.warlock.core.settings.IWindowSettings;
import cc.warlock.core.settings.WarlockSetting;
import cc.warlock.core.settings.WindowSettings;

public class WindowConfigurationProvider extends WarlockSetting implements IWindowSettingsProvider
{
	public static final String ID = "windows";
	public static final String WINDOW_MAIN = "main";
	public static final String WINDOW_DEFAULT = "default";
	
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
	
	public IWindowSettings getDefaultWindowSettings() {
		return getOrCreateWindowSettings(WINDOW_DEFAULT);
	}
	
	public void removeWindowSettings(String id) {
		windowSettings.remove(id);
		getNode().remove(id);
	}

	public WarlockColor getDefaultBackgroundColor() {
		WarlockColor bg = null;
		IWindowSettings defaultSettings = getDefaultWindowSettings();
		if(defaultSettings != null)
			bg = defaultSettings.getBackgroundColor();
		if(bg == null || bg.isDefault())
			bg = defaultBgColor;
		return bg;
	}
	
	public WarlockColor getDefaultForegroundColor() {
		WarlockColor fg = null;
		IWindowSettings defaultSettings = getDefaultWindowSettings();
		if(defaultSettings != null)
			fg = defaultSettings.getForegroundColor();
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
			bg = getDefaultBackgroundColor();
		return bg;
	}
	
	public WarlockColor getWindowForeground(String windowId) {
		WarlockColor fg = null;
		IWindowSettings wsettings = getWindowSettings(windowId);
		if(wsettings != null)
			fg = wsettings.getForegroundColor();
		if(fg == null || fg.isDefault())
			fg = getDefaultForegroundColor();
		return fg;
	}
	
	// TODO Make this return type unmodifiable
	public IWarlockFont getWindowFont(String windowId) {
		IWarlockFont font = null;
		IWindowSettings wsettings = getWindowSettings(windowId);
		if(wsettings != null)
			font = wsettings.getFont();
		if(font == null || font.isDefaultFont()) {
			IWindowSettings defaultSettings = getDefaultWindowSettings();
			if(defaultSettings != null)
				font = defaultSettings.getFont();
		}
		return font;
	}
	
	public IWarlockFont getWindowMonoFont(String windowId) {
		IWarlockFont font = null;
		IWindowSettings wsettings = getWindowSettings(windowId);
		if(wsettings != null)
			font = wsettings.getColumnFont();
		if(font == null || font.isDefaultFont()) {
			IWindowSettings defaultSettings = getDefaultWindowSettings();
			if(defaultSettings != null)
				font = defaultSettings.getColumnFont();
		}
		return font;
	}
	
	public static WindowConfigurationProvider getProvider(IClientSettings clientSettings) {
		return (WindowConfigurationProvider)clientSettings.getProvider(ID);
	}
}
