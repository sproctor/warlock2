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

import java.util.HashMap;

import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.settings.IWarlockSetting;
import cc.warlock.core.settings.StyleSetting;
import cc.warlock.core.settings.WarlockSetting;

public class PresetStyleConfigurationProvider extends WarlockSetting
{
	public static final String ID = "presets";
	
	private static HashMap<String, IWarlockStyle> defaultStyles = new HashMap<String, IWarlockStyle>();
	
	protected HashMap<String, IWarlockStyle> styleSettings = new HashMap<String, IWarlockStyle>();
	
	static {
		setDefaultStyle(WarlockStyle.BOLD, "#FFFF00", null, false);
		setDefaultStyle(WarlockStyle.ROOM_NAME, "#FFFFFF", "#0000FF", true);
		setDefaultStyle(WarlockStyle.SPEECH, "#80FF80", null, false);
		setDefaultStyle(WarlockStyle.THOUGHT, "#FF8000", null, false);
		//setDefaultStyle("cmdline", "#FFFFFF", "#000000", false);
		setDefaultStyle(WarlockStyle.WHISPER, "#80FFFF", null, false);
		setDefaultStyle(WarlockStyle.WATCHING, "#FFFF00", null, false);
		setDefaultStyle(WarlockStyle.LINK, "#62B0FF", null, false);
		setDefaultStyle(WarlockStyle.SELECTED_LINK, "#000000", "#62B0FF", false);
		setDefaultStyle(WarlockStyle.COMMAND, "#FFFFFF", "#404040", false);
		setDefaultStyle(WarlockStyle.ECHO, "#FFFF80", "#000000", false);
		setDefaultStyle(WarlockStyle.DEBUG, "#FFFFFF", "#800000", false);
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

	public IWarlockStyle getStyle(String id) {
		IWarlockStyle style = styleSettings.get(id);
		if(style == null) {
			IWarlockStyle defaults = defaultStyles.get(id);
			if(defaults != null) {
				style = new StyleSetting(this, id, true);
				style.setBackgroundColor(defaults.getBackgroundColor());
				style.setForegroundColor(defaults.getForegroundColor());
				style.setFullLine(defaults.isFullLine());
			}
		}
		
		return style;
	}

	public IWarlockStyle getOrCreateStyle(String id) {
		IWarlockStyle style = getStyle(id);
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
	
	private static void setDefaultStyle(String name, String fg, String bg, boolean fullLine) {
		IWarlockStyle style = new WarlockStyle(name);
		style.setForegroundColor(fg == null ? WindowConfigurationProvider.defaultFgColor : new WarlockColor(fg));
		style.setBackgroundColor(bg == null ? WindowConfigurationProvider.defaultBgColor : new WarlockColor(bg));
		style.setFullLine(fullLine);
		defaultStyles.put(name, style);
	}
	
	public static IWarlockStyle getDefaultStyle(String name) {
		return defaultStyles.get(name);
	}
	
	public static PresetStyleConfigurationProvider getProvider(IClientSettings clientSettings) {
		return (PresetStyleConfigurationProvider)clientSettings.getProvider(ID);
	}
}
