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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.settings.IClientSettingProvider;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.client.settings.IHighlightProvider;
import cc.warlock.core.client.settings.IHighlightString;
import cc.warlock.core.client.settings.IIgnoreProvider;
import cc.warlock.core.client.settings.IPatternSetting;
import cc.warlock.core.client.settings.IVariable;
import cc.warlock.core.client.settings.IVariableProvider;
import cc.warlock.core.client.settings.IWindowSettings;
import cc.warlock.core.client.settings.IWindowSettingsProvider;
import cc.warlock.core.client.settings.macro.internal.MacroConfigurationProvider;
import cc.warlock.core.client.settings.macro.internal.MacroSetting;


/**
 * This is the default Client Settings implementation, based on our XML Configuration backend.
 * This class includes a single default implementations for each {@link IClientSettingProvider}
 * @author marshall
 */
@SuppressWarnings("unchecked")
public class ClientSettings implements IClientSettings {
	
	public static final String WINDOW_MAIN = "main";
	
	private IWarlockClient client;
	protected int version;
	private Preferences node;

	protected HighlightConfigurationProvider highlightConfigurationProvider;
	protected IgnoreConfigurationProvider ignoreConfigurationProvider;
	protected TriggerConfigurationProvider triggerConfigurationProvider;
	protected VariableConfigurationProvider variableConfigurationProvider;
	protected MacroConfigurationProvider macroConfigurationProvider;
	protected WindowSettingsConfigurationProvider windowSettingsProvider;
	
	public ClientSettings (IWarlockClient client, String clientId) {
		this.client = client;
		this.node = WarlockPreferences.getInstance().getNode().node("clients/" + clientId);
		
		highlightConfigurationProvider = new HighlightConfigurationProvider(node);
		ignoreConfigurationProvider = new IgnoreConfigurationProvider(node);
		triggerConfigurationProvider = new TriggerConfigurationProvider();
		variableConfigurationProvider = new VariableConfigurationProvider(node);
		macroConfigurationProvider = new MacroConfigurationProvider(node);
		windowSettingsProvider = new WindowSettingsConfigurationProvider(node);
	}
	
	protected Preferences getNode() {
		return node;
	}
	
	public Collection<IHighlightString> getHighlightStrings() {
		return highlightConfigurationProvider.getSettings();
	}
	
	public Collection<IPatternSetting> geIgnores() {
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
		for (IHighlightProvider provider : getAllProviders(IHighlightProvider.class)) {
			IWarlockStyle style = provider.getNamedStyle(name);
			if (style != null) {
				return style;
			}
		}
		return null;
	}
	
	public int getVersion() {
		return version;
	}
	
	public Collection<IWindowSettings> getAllWindowSettings() {
		return windowSettingsProvider.getWindowSettings();
	}
	
	public IWindowSettings getWindowSettings(String windowId) {
		for (IWindowSettings settings : getAllWindowSettings()) {
			if (settings.getId().equals(windowId)) return settings;
		}
		return null;
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
	
	public IWindowSettings getMainWindowSettings() {
		return windowSettingsProvider.getOrCreateWindowSettings(WINDOW_MAIN);
	}
}
