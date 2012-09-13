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
package cc.warlock.core.stormfront.settings.internal;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.client.settings.internal.ClientSettings;
import cc.warlock.core.client.settings.internal.ColorFontSetting;
import cc.warlock.core.configuration.IWarlockSetting;
import cc.warlock.core.configuration.IWarlockSettingFactory;
import cc.warlock.core.stormfront.settings.ICommandLineSettings;

/**
 * @author marshall
 *
 */
public class CommandLineSettings extends ColorFontSetting implements ICommandLineSettings
{
	public static final String ID = "command-line";
	
	protected WarlockColor barColor = new WarlockColor(WarlockColor.DEFAULT_COLOR);
	
	static {
		ClientSettings.registerProviderFactory(ID, new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new CommandLineSettings(parentNode);
			}
		});
	}
	
	private CommandLineSettings (Preferences parentNode) {
		super(parentNode, ID);
		
		String colorString = getNode().get("bar-color", null);
		if(colorString != null)
			barColor = new WarlockColor(colorString);
	}
	
	public WarlockColor getBarColor() {
		return barColor;
	}

	public void setBarColor(WarlockColor barColor) {
		getNode().put("bar-color", barColor.toString());
		
		this.barColor = barColor;
	}
	
	public static CommandLineSettings getProvider(IClientSettings clientSettings) {
		return (CommandLineSettings)clientSettings.getProvider(ID);
	}
}
