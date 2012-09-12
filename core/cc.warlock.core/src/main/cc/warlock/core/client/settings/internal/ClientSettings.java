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

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockFont;
import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockPattern;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.client.logging.LoggingConfiguration;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.client.settings.IVariable;
import cc.warlock.core.client.settings.IWindowSettings;
import cc.warlock.core.client.settings.macro.internal.MacroConfigurationProvider;
import cc.warlock.core.client.settings.macro.internal.MacroSetting;
import cc.warlock.core.configuration.IWarlockSetting;
import cc.warlock.core.configuration.IWarlockSettingFactory;
import cc.warlock.core.configuration.WarlockPreferences;
import cc.warlock.core.configuration.WarlockSetting;


/**
 * This is the default Client Settings implementation, based on our XML Configuration backend.
 * This class includes a single default implementations for each {@link IConfigurationProvider}
 * @author marshall
 */
public class ClientSettings extends WarlockSetting implements IClientSettings {
	
	public static final String WINDOW_MAIN = "main";
	private static HashMap<String, IWarlockStyle> defaultStyles = new HashMap<String, IWarlockStyle>();
	private static HashMap<String, ClientSettings> clients = new HashMap<String, ClientSettings>();
	private static Preferences topNode = WarlockPreferences.getInstance().getNode().node("clients");
	private static HashMap<String, IWarlockSettingFactory> providerFactories = new HashMap<String, IWarlockSettingFactory>();
	
	protected int version;
	
	private HashMap<String, IWarlockSetting> providers = new HashMap<String, IWarlockSetting>();
	
	// TODO: store these in settings
	private static WarlockColor defaultBgColor = new WarlockColor("191932");
	private static WarlockColor defaultFgColor = new WarlockColor("#F0F0FF");
	
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
		
		try {
			for (String clientId : topNode.childrenNames()) {
				new ClientSettings(clientId);
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		
		registerProviderFactory("highlights", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new HighlightConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("ignores", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new IgnoreConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("triggers", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new TriggerConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("variables", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new VariableConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("macros", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new MacroConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("windows", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new WindowSettingsConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("presets", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new PresetSettingsConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("logs", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new LoggingConfiguration(parentNode);
			}
		});
	}
	
	
	public IWarlockSetting getProvider (String providerId) {
		IWarlockSetting provider = providers.get(providerId);
		if (provider == null) {
			IWarlockSettingFactory factory = providerFactories.get(providerId);
			if (factory == null)
				return null;
			provider = factory.createSetting(topNode);
		}
		return provider;
	}
	
	public static void registerProviderFactory (String providerId, IWarlockSettingFactory factory) {
		providerFactories.put(providerId, factory);
	}
	
	private ClientSettings (String clientId) {
		super(topNode, clientId);
	}
	
	public static ClientSettings getClientSettings (String clientId) {
		ClientSettings client = clients.get(clientId);
		if (client == null) {
			client = new ClientSettings (clientId);
			clients.put(clientId, client);
		}
		return client;
	}
	
	public static Collection<ClientSettings> getAllClientSettings () {
		return Collections.unmodifiableCollection(clients.values());
	}
	
	private static void setDefaultStyle(String name, String fg, String bg) {
		IWarlockStyle style = new WarlockStyle(name);
		style.setForegroundColor(fg == null ? defaultFgColor : new WarlockColor(fg));
		style.setBackgroundColor(bg == null ? defaultBgColor : new WarlockColor(bg));
		defaultStyles.put(name, style);
	}
	
	public static IWarlockStyle getDefaultStyle(String name) {
		return defaultStyles.get(name);
	}
	
	public Collection<IWarlockHighlight> getHighlightStrings() {
		return highlightConfigurationProvider.getSettings();
	}
	
	public Collection<IWarlockPattern> getIgnores() {
		return ignoreConfigurationProvider.getSettings();
	}
	
	public Collection<MacroSetting> getMacros() {
		return macroConfigurationProvider.getSettings();
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
}
