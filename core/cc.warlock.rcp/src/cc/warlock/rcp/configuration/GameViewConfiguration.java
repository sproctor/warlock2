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
package cc.warlock.rcp.configuration;

import org.eclipse.jface.resource.JFaceResources;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.client.settings.internal.ClientSettings;
import cc.warlock.core.configuration.IWarlockSetting;
import cc.warlock.core.configuration.IWarlockSettingFactory;
import cc.warlock.core.configuration.WarlockSetting;

public class GameViewConfiguration extends WarlockSetting
{
	public static final String ID = "view";
	
	public static final WarlockColor defaultDefaultBgColor = new WarlockColor(25, 25, 50);
	public static final WarlockColor defaultDefaultFgColor = new WarlockColor(240, 240, 255);

	private int bufferLines;
	private WarlockColor defaultBackground = defaultDefaultBgColor;
	private WarlockColor defaultForeground = defaultDefaultFgColor;
	protected String defaultFontFace;
	protected int defaultFontSize;
	protected boolean suppressPrompt;
	
	static {
		ClientSettings.registerProviderFactory(ID, new IWarlockSettingFactory() {
			public IWarlockSetting createSetting (Preferences parentNode) {
				return new GameViewConfiguration(parentNode);
			}
		});
	}
	
	private GameViewConfiguration (Preferences parentNode)
	{
		super(parentNode, ID);
		
		String bgString = getNode().get("background", null);
		if(bgString != null)
			defaultBackground = new WarlockColor(bgString);
		String fgString = getNode().get("foreground", null);
		if(fgString != null)
			defaultForeground = new WarlockColor(fgString);
		
		defaultFontFace = getNode().get("face", JFaceResources.getDefaultFont().getFontData()[0].getName());
		defaultFontSize = getNode().getInt("size", JFaceResources.getDefaultFont().getFontData()[0].getHeight());
		
		bufferLines = getNode().getInt("buffer", 5000);
		suppressPrompt = getNode().getBoolean("suppress-prompt", false);
		
		// WarlockConfiguration.getMainConfiguration().addConfigurationProvider(this);
	}
	
	public int getBufferLines() {
		return bufferLines;
	}

	public void setBufferLines(int bufferLines) {
		getNode().putInt("buffer", bufferLines);
		this.bufferLines = bufferLines;
	}

	public WarlockColor getDefaultBackground() {
		return defaultBackground;
	}

	public void setDefaultBackground(WarlockColor defaultBackground) {
		getNode().put("background", defaultBackground.toString());
		this.defaultBackground = defaultBackground;
	}

	public WarlockColor getDefaultForeground() {
		return defaultForeground;
	}

	public void setDefaultForeground(WarlockColor defaultForeground) {
		getNode().put("foreground", defaultForeground.toString());
		this.defaultForeground = defaultForeground;
	}

	public String getDefaultFontFace() {
		return defaultFontFace;
	}

	public void setDefaultFontFace(String defaultFontFace) {
		getNode().put("face", defaultFontFace);
		this.defaultFontFace = defaultFontFace;
	}

	public int getDefaultFontSize() {
		return defaultFontSize;
	}

	public void setDefaultFontSize(int defaultFontSize) {
		getNode().putInt("size", defaultFontSize);
		this.defaultFontSize = defaultFontSize;
	}

	public boolean getSuppressPrompt() {
		return suppressPrompt;
	}
	
	public void setSuppressPrompt(boolean suppressPrompt) {
		getNode().putBoolean("suppress-prompt", suppressPrompt);
		this.suppressPrompt = suppressPrompt;
	}
	
	public static GameViewConfiguration getProvider(IClientSettings clientSettings) {
		return (GameViewConfiguration)clientSettings.getProvider(ID);
	}
}
