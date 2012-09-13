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
package cc.warlock.rcp.ui.style;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IWarlockFont;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.client.settings.internal.WindowConfigurationProvider;
import cc.warlock.rcp.ui.IStyleProvider;
import cc.warlock.rcp.ui.StyleRangeWithData;
import cc.warlock.rcp.util.ColorUtil;
import cc.warlock.rcp.util.FontUtil;

public class DefaultStyleProvider implements IStyleProvider {
	
	protected IClientSettings clientSettings;
	
	public DefaultStyleProvider (IClientSettings clientSettings) {
		this.clientSettings = clientSettings;
	}
	
	public StyleRangeWithData getStyleRange (IWarlockStyle style)
	{	
		StyleRangeWithData range = new StyleRangeWithData();
		
		range.fontStyle = SWT.NORMAL;
		if (style.isBold())
			range.fontStyle |= SWT.BOLD;
		if (style.isItalic())
			range.fontStyle |= SWT.ITALIC;
		if (style.isUnderline())
			range.underline = true;
		
		if (style.isMonospace()) {
			// FIXME: this will cause it to ignore bold/italic
			IWarlockFont font = WindowConfigurationProvider.getProvider(clientSettings).getMainWindowSettings().getColumnFont();
			if (!font.isDefaultFont() && Display.getDefault().getFontList(font.getFamilyName(), true).length > 0)
			{
				range.font = FontUtil.warlockFontToFont(font);
			} else {
				range.font = JFaceResources.getTextFont();
			}
		}

		WarlockColor foreground = style.getForegroundColor();
		WarlockColor background = style.getBackgroundColor();
		if (foreground != null && !foreground.isDefault())
			range.foreground = ColorUtil.warlockColorToColor(foreground);
		if (background != null && !background.isDefault())
			range.background = ColorUtil.warlockColorToColor(background);
		
		return range;
	}
}
