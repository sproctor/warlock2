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
package cc.warlock.core.settings;

import cc.warlock.core.client.IWarlockFont;
import cc.warlock.core.client.WarlockColor;

/**
 * @author marshall
 *
 */
public abstract class ColorFontSetting extends WarlockSetting implements IFontSetting {

	private WarlockColor foregroundColor = new WarlockColor(WarlockColor.DEFAULT_COLOR);
	private WarlockColor backgroundColor = new WarlockColor(WarlockColor.DEFAULT_COLOR);
	private FontSetting font;
	
	public ColorFontSetting (IWarlockSetting parent, String path)
	{
		super(parent, path);
		
		String fgcolor = getNode().get("fgcolor", null);
		if(fgcolor != null)
			foregroundColor = new WarlockColor(fgcolor);
		
		String bgcolor = getNode().get("bgcolor", null);
		if(bgcolor != null)
			backgroundColor = new WarlockColor(bgcolor);
		
		font = new FontSetting(this, "font");
	}

	public WarlockColor getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(WarlockColor foregroundColor) {
		this.getNode().put("fgcolor", foregroundColor.toString());
		
		this.foregroundColor = foregroundColor;
		this.notifyListenersChanged();
	}

	public WarlockColor getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(WarlockColor backgroundColor) {
		this.getNode().put("bgcolor", backgroundColor.toString());
		
		this.backgroundColor = backgroundColor;
		this.notifyListenersChanged();
	}

	public IWarlockFont getFont() {
		return font;
	}

}
