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

	private WarlockColor foregroundColor = new WarlockColor(WarlockColor.DEFAULT_COLOR);
	private WarlockColor backgroundColor = new WarlockColor(WarlockColor.DEFAULT_COLOR);
	private boolean fullLine;
	private String name;
	private String componentName;
	private Runnable action;
	private String sound = new String();
	private boolean bold;
	private boolean italic;
	private boolean underline;
	private boolean monospace;
	
	public StyleSetting (Preferences parentNode, String path)
	{
		super(parentNode, path);
	}

	public WarlockColor getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(WarlockColor foregroundColor) {
		if (!foregroundColor.equals(this.foregroundColor))
			this.getNode().put("fgcolor", foregroundColor.toString());
		
		this.foregroundColor = foregroundColor;
	}

	public WarlockColor getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(WarlockColor backgroundColor) {
		if (!backgroundColor.equals(this.backgroundColor)) 
			this.getNode().put("bgcolor", backgroundColor.toString());
		
		this.backgroundColor = backgroundColor;
	}

	@Override
	public Runnable getAction() {
		return action;
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
		return componentName;
	}

	@Override
	public void setAction(Runnable action) {
		// FIXME: does this need to be saved? I don't see a way to.
		
		this.action = action;
	}

	@Override
	public void setFullLine(boolean fullLine) {
		getNode().putBoolean("full-line", fullLine);
		this.fullLine = fullLine;
	}

	@Override
	public void setName(String name) {
		getNode().put("name", name);
		this.name = name;
	}

	@Override
	public void setComponentName(String componentName) {
		getNode().put("component", componentName);
		this.componentName = componentName;
	}

	@Override
	public String getSound() {
		return sound;
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
