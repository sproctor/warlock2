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

import cc.warlock.core.client.IWarlockFont;
import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockPattern;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.logging.LoggingConfiguration;
import cc.warlock.core.client.settings.internal.HighlightConfigurationProvider;
import cc.warlock.core.client.settings.internal.PresetSettingsConfigurationProvider;
import cc.warlock.core.client.settings.macro.internal.MacroSetting;
import cc.warlock.core.configuration.IWarlockSetting;

/**
 * @author marshall
 *
 */
public interface IClientSettings extends IWarlockSetting {
	
	public int getVersion();
	
	public Collection<IWarlockHighlight> getHighlightStrings();
	public IWarlockStyle getNamedStyle(String name);
	
	public Collection<IVariable> getVariables();
	public IVariable getVariable(String identifier);
	
	public Collection<IWarlockPattern> getIgnores();
	
	public Collection<IWindowSettings> getWindowSettings();
	public IWindowSettings getWindowSettings (String windowId);
	
	public Collection<MacroSetting> getMacros();
	public MacroSetting getMacro (String keyString);

	public WarlockColor getDefaultBackground();
	public WarlockColor getDefaultForeground();
	
	public WarlockColor getWindowBackground(String windowId);
	public WarlockColor getWindowForeground(String windowId);
	public IWarlockFont getWindowFont(String windowId);
	
	public IWindowSettings getMainWindowSettings();
	public HighlightConfigurationProvider getHighlightConfigurationProvider();
	public PresetSettingsConfigurationProvider getPresetConfigurationProvider();
	public LoggingConfiguration getLoggingSettings();
}
