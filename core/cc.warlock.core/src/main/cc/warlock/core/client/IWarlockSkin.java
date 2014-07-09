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
package cc.warlock.core.client;



public interface IWarlockSkin {

	public static enum FontFaceType {
		MainWindow_FontFace, MainWindow_MonoFontFace, CommandLine_FontFace
	};
	
	public static enum ColorType {
		MainWindow_Background, MainWindow_Foreground, CommandLine_Background, CommandLine_Foreground, CommandLine_BarColor
	};
	
	public static enum FontSizeType {
		MainWindow_FontSize, MainWindow_MonoFontSize, CommandLine_FontSize
	};
	
	public WarlockColor getColor (ColorType type);
	
	public String getFontFace (FontFaceType type);
	
	public WarlockColor getDefaultWindowForeground();
	public WarlockColor getDefaultWindowBackground();
	
	public WarlockColor getDefaultForegroundColor (String styleName);
	public WarlockColor getDefaultBackgroundColor (String styleName);
	

	public WarlockColor getBackgroundColor(IWarlockPattern string);
	public WarlockColor getForegroundColor(IWarlockPattern string);
}
