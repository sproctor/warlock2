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

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;

/**
 * @author marshall
 *
 */
public class StyleSetting extends ClientSetting implements IWarlockStyle {

	private WarlockColor fgColor;
	private WarlockColor bgColor;
	private boolean fullLine;
	private String name;
	private String sound;
	private boolean bold;
	private boolean italic;
	private boolean underline;
	private boolean monospace;
	
	public StyleSetting (Preferences parentNode, String path) {
		this(parentNode, path, false);
	}
	
	public StyleSetting (Preferences parentNode, String path, boolean pathIsName)
	{
		super(parentNode, path);
		
		String fgString = getNode().get("fgcolor", null);
		fgColor = fgString == null ? new WarlockColor(WarlockColor.DEFAULT_COLOR) : new WarlockColor(fgString);
		
		String bgString = getNode().get("bgcolor", null);
		bgColor = bgString == null ? new WarlockColor(WarlockColor.DEFAULT_COLOR) : new WarlockColor(bgString);
		
		fullLine = getNode().getBoolean("full-line", false);
		
		if(pathIsName)
			name = path;
		
		sound = getNode().get("sound", null);
		bold = getNode().getBoolean("bold", false);
		italic = getNode().getBoolean("italic", false);
		underline = getNode().getBoolean("underline", false);
		monospace = getNode().getBoolean("monospace", false);
	}

	public WarlockColor getForegroundColor() {
		return fgColor;
	}

	public void setForegroundColor(WarlockColor foregroundColor) {
		this.getNode().put("fgcolor", foregroundColor.toString());
		
		this.fgColor = foregroundColor;
	}

	public WarlockColor getBackgroundColor() {
		return bgColor;
	}

	public void setBackgroundColor(WarlockColor backgroundColor) {
		this.getNode().put("bgcolor", backgroundColor.toString());
		
		this.bgColor = backgroundColor;
	}

	@Override
	public Runnable getAction() {
		return null;
	}

	@Override
	public boolean isFullLine() {
		return fullLine;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getComponentName() {
		return "";
	}

	@Override
	public void setAction(Runnable action) {
		// FIXME: this should probably throw an exception
	}

	@Override
	public void setFullLine(boolean fullLine) {
		getNode().putBoolean("full-line", fullLine);
		this.fullLine = fullLine;
	}

	@Override
	public String getSound() {
		return sound;
	}

	@Override
	public void setComponentName(String name) {
		// No component allow
	}
	
	@Override
	public void setSound(String sound) {
		getNode().put("sound", sound);
		this.sound = sound;
	}

	@Override
	public boolean isBold() {
		return bold;
	}

	@Override
	public boolean isItalic() {
		return italic;
	}

	@Override
	public boolean isUnderline() {
		return underline;
	}

	@Override
	public boolean isMonospace() {
		return monospace;
	}

	@Override
	public void setBold(boolean bold) {
		getNode().putBoolean("bold", bold);
		this.bold = bold;
	}

	@Override
	public void setItalic(boolean italic) {
		getNode().putBoolean("italic", italic);
		this.italic = italic;
	}

	@Override
	public void setUnderline(boolean underline) {
		getNode().putBoolean("underline", underline);
		this.underline = underline;
	}

	@Override
	public void setMonospace(boolean monospace) {
		getNode().putBoolean("monospace", monospace);
		this.monospace = monospace;
	}
	
}
