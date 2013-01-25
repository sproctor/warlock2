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

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.configuration.IWarlockSetting;
import cc.warlock.core.configuration.WarlockSetting;

public class PresetStyleConfigurationProvider extends WarlockSetting
{
	public static final String ID = "presets";
	
	private static HashMap<String, IWarlockStyle> defaultStyles = new HashMap<String, IWarlockStyle>();
	
	protected HashMap<String, IWarlockStyle> styleSettings = new HashMap<String, IWarlockStyle>();
	
	public static final String PRESET_BOLD = "bold";
	public static final String PRESET_ROOM_NAME = "roomName";
	public static final String PRESET_SPEECH = "speech";
	public static final String PRESET_WHISPER = "whisper";
	public static final String PRESET_THOUGHT = "thought";
	public static final String PRESET_WATCHING = "watching";
	public static final String PRESET_LINK = "link";
	public static final String PRESET_SELECTED_LINK = "selectedLink";
	public static final String PRESET_COMMAND = "command";
	
	static {
		setDefaultStyle("bold", "#FFFF00", null);
		setDefaultStyle("roomName", "#FFFFFF", "#0000FF");
		setDefaultStyle("speech", "#80FF80", null);
		setDefaultStyle("thought", "#FF8000", null);
		setDefaultStyle("cmdline", "#FFFFFF", "#000000");
		setDefaultStyle("whisper", "#80FFFF", null);
		setDefaultStyle("watching", "#FFFF00", null);
		setDefaultStyle("link", "#62B0FF", null);
		setDefaultStyle("selectedLink", "#000000", "#62B0FF");
		setDefaultStyle("command", "#FFFFFF", "#404040");
	}
	
	public PresetStyleConfigurationProvider(IWarlockSetting parent) {
		super(parent, ID);
		
		try {
			for(String id : getNode().childrenNames()) {
				styleSettings.put(id, new StyleSetting(this, id, true));
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Collection<IWarlockStyle> getStyles() {
		return styleSettings.values();
	}

	public IWarlockStyle getStyle(String id) {
		IWarlockStyle style = styleSettings.get(id);
		if(style == null)
			style = defaultStyles.get(id);
		
		return style;
	}

	public IWarlockStyle getOrCreateStyle(String id) {
		IWarlockStyle style = styleSettings.get(id);
		if(style == null) {
			style = new StyleSetting(this, id, true);
			styleSettings.put(id, style);
		}
		return style;
	}

	public void removeStyle(String id) {
		styleSettings.remove(id);
		getNode().remove(id);
	}
	
	private static void setDefaultStyle(String name, String fg, String bg) {
		IWarlockStyle style = new WarlockStyle(name);
		style.setForegroundColor(fg == null ? WindowConfigurationProvider.defaultFgColor : new WarlockColor(fg));
		style.setBackgroundColor(bg == null ? WindowConfigurationProvider.defaultBgColor : new WarlockColor(bg));
		defaultStyles.put(name, style);
	}
	
	public static IWarlockStyle getDefaultStyle(String name) {
		return defaultStyles.get(name);
	}
	
	public static PresetStyleConfigurationProvider getProvider(IClientSettings clientSettings) {
		return (PresetStyleConfigurationProvider)clientSettings.getProvider(ID);
	}
}
