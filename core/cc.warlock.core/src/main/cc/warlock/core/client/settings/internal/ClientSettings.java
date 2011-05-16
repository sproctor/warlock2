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

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.logging.LoggingConfiguration;
import cc.warlock.core.client.settings.IClientSettingProvider;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.client.settings.IHighlightString;
import cc.warlock.core.client.settings.IPatternSetting;
import cc.warlock.core.client.settings.IVariable;
import cc.warlock.core.client.settings.IWindowSettings;
import cc.warlock.core.client.settings.macro.internal.MacroConfigurationProvider;
import cc.warlock.core.client.settings.macro.internal.MacroSetting;


/**
 * This is the default Client Settings implementation, based on our XML Configuration backend.
 * This class includes a single default implementations for each {@link IClientSettingProvider}
 * @author marshall
 */
public class ClientSettings implements IClientSettings {
	
	public static final String WINDOW_MAIN = "main";
	
	private IWarlockClient client;
	protected int version;
	private Preferences node;
	private boolean newSettings;

	protected HighlightConfigurationProvider highlightConfigurationProvider;
	protected IgnoreConfigurationProvider ignoreConfigurationProvider;
	protected TriggerConfigurationProvider triggerConfigurationProvider;
	protected VariableConfigurationProvider variableConfigurationProvider;
	protected MacroConfigurationProvider macroConfigurationProvider;
	protected WindowSettingsConfigurationProvider windowSettingsProvider;
	private PresetSettingsConfigurationProvider presetSettingsProvider;
	private LoggingConfiguration loggingSettings;
	
	// TODO: store these in settings
	private WarlockColor defaultWindowBackground, defaultWindowForeground;
	
	public ClientSettings (IWarlockClient client, String clientId) {
		this.client = client;
		String nodeName = "clients/" + clientId;
		Preferences parentNode = WarlockPreferences.getInstance().getNode();
		try {
			newSettings = !parentNode.nodeExists(nodeName);
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
		this.node = parentNode.node(nodeName);
		
		highlightConfigurationProvider = new HighlightConfigurationProvider(node);
		ignoreConfigurationProvider = new IgnoreConfigurationProvider(node);
		triggerConfigurationProvider = new TriggerConfigurationProvider(node);
		variableConfigurationProvider = new VariableConfigurationProvider(node);
		macroConfigurationProvider = new MacroConfigurationProvider(node);
		windowSettingsProvider = new WindowSettingsConfigurationProvider(node);
		presetSettingsProvider = new PresetSettingsConfigurationProvider(node);
		loggingSettings = new LoggingConfiguration(node);
		
		defaultWindowForeground = new WarlockColor("#F0F0FF");
		defaultWindowBackground = new WarlockColor("191932");
	}
	
	protected Preferences getNode() {
		return node;
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
	
	public MacroSetting getMacro(int keycode, int modifiers) {
		return macroConfigurationProvider.getMacro(keycode, modifiers);
	}
	
	public IWarlockStyle getNamedStyle(String name) {
		return presetSettingsProvider.getStyle(name);
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
			bg = defaultWindowBackground;
		return bg;
	}
	
	public WarlockColor getDefaultForeground() {
		WarlockColor fg = this.getMainWindowSettings().getForegroundColor();
		if(fg == null || fg.isDefault())
			fg = defaultWindowForeground;
		return fg;
	}
	
	public WarlockColor getWindowBackground(String windowId) {
		WarlockColor bg = getWindowSettings(windowId).getBackgroundColor();
		if(bg == null || bg.isDefault()) {
			bg = this.getMainWindowSettings().getBackgroundColor();
			if(bg == null || bg.isDefault())
				bg = defaultWindowBackground;
		}
		return bg;
	}
	
	public WarlockColor getWindowForeground(String windowId) {
		WarlockColor fg = getWindowSettings(windowId).getForegroundColor();
		if(fg == null || fg.isDefault()) {
			fg = this.getMainWindowSettings().getForegroundColor();
			if(fg == null || fg.isDefault())
				fg = defaultWindowForeground;
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
	
	public PresetSettingsConfigurationProvider getPresetSettingsProvider() {
		return presetSettingsProvider;
	}
	
	public IWindowSettings getMainWindowSettings() {
		return windowSettingsProvider.getOrCreateWindowSettings(WINDOW_MAIN);
	}

	public LoggingConfiguration getLoggingSettings() {
		return loggingSettings;
	}
	
	public boolean isNewSettings() {
		return newSettings;
	}

	@Override
	public IWarlockClient getClient() {
		return client;
	}
}
