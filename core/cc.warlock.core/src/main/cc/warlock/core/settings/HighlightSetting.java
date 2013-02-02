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

import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockHighlight;

public class HighlightSetting extends AbstractPatternSetting<WarlockHighlight> implements IWarlockSetting, IWarlockHighlight {

	public HighlightSetting (IWarlockSetting parent, String path)
	{
		super(parent, path);
	}
	
	protected WarlockHighlight createPattern(String text, boolean literal, boolean caseSensitive, boolean fullWord) {
		StyleSetting style = new StyleSetting(this, "style");
		return new WarlockHighlight(text, literal, caseSensitive, fullWord, style);
	}
	
	public IWarlockStyle getStyle() {
		return this.pattern.getStyle();
	}
	
	public void setStyle(IWarlockStyle style) {
		// Not supported
	}
}

