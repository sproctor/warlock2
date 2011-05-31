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

import org.osgi.service.prefs.BackingStoreException;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.client.logging.LoggingConfiguration;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.client.settings.IHighlightString;
import cc.warlock.core.client.settings.IPatternSetting;
import cc.warlock.core.client.settings.IVariable;
import cc.warlock.core.client.settings.IWindowSettings;
import cc.warlock.core.client.settings.macro.internal.MacroConfigurationProvider;
import cc.warlock.core.client.settings.macro.internal.MacroSetting;
import cc.warlock.core.configuration.WarlockPreferences;
import cc.warlock.core.configuration.WarlockSetting;


/**
 * This is the default Client Settings implementation, based on our XML Configuration backend.
 * This class includes a single default implementations for each {@link IConfigurationProvider}
 * @author marshall
 */
public class ClientSettings extends WarlockSetting implements IClientSettings {
	
	public static final String WINDOW_MAIN = "main";
	
	private IWarlockClient client;
	protected int version;
	private HashMap<String, IWarlockStyle> defaultStyles = new HashMap<String, IWarlockStyle>();

	protected HighlightConfigurationProvider highlightConfigurationProvider;
	protected IgnoreConfigurationProvider ignoreConfigurationProvider;
	protected TriggerConfigurationProvider triggerConfigurationProvider;
	protected VariableConfigurationProvider variableConfigurationProvider;
	protected MacroConfigurationProvider macroConfigurationProvider;
	protected WindowSettingsConfigurationProvider windowSettingsProvider;
	private PresetSettingsConfigurationProvider presetSettingsProvider;
	private LoggingConfiguration loggingSettings;
	
	// TODO: store these in settings
	private WarlockColor defaultBgColor, defaultFgColor;
	
	public ClientSettings (IWarlockClient client, String clientId) {
		super(WarlockPreferences.getInstance().getNode(), "clients/" + clientId);
		this.client = client;
		
		highlightConfigurationProvider = new HighlightConfigurationProvider(getNode());
		ignoreConfigurationProvider = new IgnoreConfigurationProvider(getNode());
		triggerConfigurationProvider = new TriggerConfigurationProvider(getNode());
		variableConfigurationProvider = new VariableConfigurationProvider(getNode());
		macroConfigurationProvider = new MacroConfigurationProvider(getNode());
		windowSettingsProvider = new WindowSettingsConfigurationProvider(getNode());
		presetSettingsProvider = new PresetSettingsConfigurationProvider(getNode());
		loggingSettings = new LoggingConfiguration(getNode());
		
		defaultFgColor = new WarlockColor("#F0F0FF");
		defaultBgColor = new WarlockColor("191932");
		
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
	
	private void setDefaultStyle(String name, String fg, String bg) {
		IWarlockStyle style = new WarlockStyle(name);
		style.setForegroundColor(fg == null ? defaultFgColor : new WarlockColor(fg));
		style.setBackgroundColor(bg == null ? defaultBgColor : new WarlockColor(bg));
		defaultStyles.put(name, style);
	}
	
	public IWarlockStyle getDefaultStyle(String name) {
		return defaultStyles.get(name);
	}
	
	public Collection<IHighlightString> getHighlightStrings() {
		return highlightConfigurationProvider.getSettings();
	}
	
	public Collection<IPatternSetting> getIgnores() {
		return ignoreConfigurationProvider.getSettings();
	}
	
	public Collection<MacroSetting> getMacros() {
		return macroConfigurationProvider.getMacros();
	}
	
	public Collection<IVariable> getVariables() {
		return variableConfigurationProvider.getVariables();
	}
	
	public MacroSetting getMacro(String keyString) {
		return macroConfigurationProvider.getMacro(keyString);
	}
	
	public IWarlockStyle getNamedStyle(String name) {
		IWarlockStyle style = presetSettingsProvider.getStyle(name);
		if(style == null)
			style = defaultStyles.get(name);
		
		return style;
	}
	
	public int getVersion() {
		return version;
	}
	
	public Collection<IWindowSettings> getWindowSettings() {
		return windowSettingsProvider.getWindowSettings();
	}
	
	public IWindowSettings getWindowSettings(String windowId) {
		return windowSettingsProvider.getWindowSettings(windowId);
	}
	
	public WarlockColor getDefaultBackground() {
		WarlockColor bg = this.getMainWindowSettings().getBackgroundColor();
		if(bg == null || bg.isDefault())
			bg = defaultBgColor;
		return bg;
	}
	
	public WarlockColor getDefaultForeground() {
		WarlockColor fg = this.getMainWindowSettings().getForegroundColor();
		if(fg == null || fg.isDefault())
			fg = defaultFgColor;
		return fg;
	}
	
	public WarlockColor getWindowBackground(String windowId) {
		WarlockColor bg = getWindowSettings(windowId).getBackgroundColor();
		if(bg == null || bg.isDefault()) {
			bg = this.getMainWindowSettings().getBackgroundColor();
			if(bg == null || bg.isDefault())
				bg = defaultBgColor;
		}
		return bg;
	}
	
	public WarlockColor getWindowForeground(String windowId) {
		WarlockColor fg = getWindowSettings(windowId).getForegroundColor();
		if(fg == null || fg.isDefault()) {
			fg = this.getMainWindowSettings().getForegroundColor();
			if(fg == null || fg.isDefault())
				fg = defaultFgColor;
		}
		return fg;
	}
	
	public IVariable getVariable(String identifier) {
		return variableConfigurationProvider.getVariable(identifier);
	}

	public HighlightConfigurationProvider getHighlightConfigurationProvider() {
		return highlightConfigurationProvider;
	}

	public IgnoreConfigurationProvider getIgnoreConfigurationProvider() {
		return ignoreConfigurationProvider;
	}

	public TriggerConfigurationProvider getTriggerConfigurationProvider() {
		return triggerConfigurationProvider;
	}

	public VariableConfigurationProvider getVariableConfigurationProvider() {
		return variableConfigurationProvider;
	}

	public MacroConfigurationProvider getMacroConfigurationProvider() {
		return macroConfigurationProvider;
	}

	public WindowSettingsConfigurationProvider getWindowSettingsProvider() {
		return windowSettingsProvider;
	}
	
	public PresetSettingsConfigurationProvider getPresetConfigurationProvider() {
		return presetSettingsProvider;
	}
	
	public IWindowSettings getMainWindowSettings() {
		return windowSettingsProvider.getOrCreateWindowSettings(WINDOW_MAIN);
	}

	public LoggingConfiguration getLoggingSettings() {
		return loggingSettings;
	}

	public void flush() {
		try {
			getNode().flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public IWarlockClient getClient() {
		return client;
	}
}
