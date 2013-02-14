/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2010, Warlock LLC, and individual contributors as indicated
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

package cc.warlock.rcp.stormfront.ui;

import java.util.HashMap;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IWarlockDialogData;
import cc.warlock.core.client.IWarlockDialogListener;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.internal.WarlockDialog;
import cc.warlock.rcp.ui.client.SWTWarlockDialogListener;
import cc.warlock.rcp.util.ColorUtil;

/**
 * @author Marshall
 * 
 * This is a custom progress bar that mimics the L&F of StormFront's status bars.
 * It's sort of a dirty hack, but it suffices for now. It needs to handle being in a LayoutManager better...
 */
public class StormFrontDialogControl extends Canvas {
	
	private int borderWidth = 1;
	private WarlockDialog dialog;
	
	private HashMap<String, ColorGroup> colors = new HashMap<String, ColorGroup>();
	private ColorGroup defaultColors;
	private IWarlockDialogListener listener = new SWTWarlockDialogListener(new IWarlockDialogListener() {
		@Override
		public void dialogChanged() {
			redraw();
		}
	});
	
	private class ColorGroup {
		public Color fg;
		public Color bg;
		public Color border;
		
		public ColorGroup(Display display, String fgColor, String bgColor, String borderColor) {
			fg = new Color(display, ColorUtil.warlockColorToRGB(new WarlockColor(fgColor)));
			bg = new Color(display, ColorUtil.warlockColorToRGB(new WarlockColor(bgColor)));
			border = new Color(display, ColorUtil.warlockColorToRGB(new WarlockColor(borderColor)));
		}
	}
	
	public StormFrontDialogControl (Composite composite, int style) {
		super(composite, style);
		Display display = getDisplay();
		
		colors.put("health", new ColorGroup(display, "#FFFFFF", "#800000", "#796a6a"));
		colors.put("mana", new ColorGroup(display, "#FFFFFF", "#0000FF", "#7272FF"));
		colors.put("stamina", new ColorGroup(display, "#000000", "#D0982F", "#DECCAA"));
		colors.put("spirit", new ColorGroup(display, "#000000", "#969696", "#E1E1E1"));
		colors.put("concentration", new ColorGroup(display, "#000000", "#00FF00", "#E1E1E1"));
		defaultColors = new ColorGroup(display, "#000000", "#969696", "#E1E1E1");
		
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if(dialog == null)
					return;
				
				Rectangle bounds = getBounds();
				
				synchronized(dialog) {
					for(IWarlockDialogData progressBar : dialog.getElements()) {
						int pbWidth = progressBar.getWidth();
						int pbLeft = progressBar.getLeft();
						int pbValue = progressBar.getValue();

						// This should probably all be abstracted out
						int fullBarWidth = pbWidth * bounds.width / 100;
						int barWidth = fullBarWidth - 2 * borderWidth;
						int barHeight = bounds.height - 2 * borderWidth;
						int filledWidth = pbValue * barWidth / 100;
						int left = pbLeft * bounds.width / 100;

						ColorGroup cg = colors.get(progressBar.getId());
						if(cg == null)
							cg = defaultColors;

						// Draw the border
						e.gc.setForeground(cg.border);
						e.gc.setLineWidth(borderWidth);
						e.gc.drawRectangle(left, 0, fullBarWidth, bounds.height);

						// draw the filled part of the rectangle
						Color gradientColor = getGradientColor(25, true, cg.bg);
						e.gc.setBackground(gradientColor);
						e.gc.setForeground(cg.bg);
						e.gc.fillGradientRectangle(borderWidth + left, borderWidth,
								filledWidth, barHeight, false);

						// draw the background
						e.gc.setBackground(cg.border);
						e.gc.fillRectangle(borderWidth + left + filledWidth,
								borderWidth, barWidth - filledWidth, barHeight);

						e.gc.setForeground(cg.fg);

						String text = progressBar.getText();
						Point extent = e.gc.textExtent(text);

						int text_left = left + (barWidth - extent.x) / 2;
						int text_top = (bounds.height - 2 * borderWidth - e.gc.getFontMetrics().getHeight()) / 2;
						e.gc.drawText (text, text_left, text_top, true);
					}
				}
			}
		});
	}
	
	private Color getGradientColor (int factor, boolean lighter, Color color) {
		
		int red = 0;
		int green = 0;
		int blue = 0;
		
		if (lighter) {
			red = color.getRed() < (255 - factor) ? color.getRed() + factor : 255;
			green = color.getGreen() < (255 - factor) ? color.getGreen() + factor : 255;
			blue = color.getBlue() < (255 - factor) ? color.getBlue() + factor : 255;
		} else {
			red = color.getRed() > factor ? color.getRed() - factor : 0;
			green = color.getRed() > factor ? color.getRed() - factor : 0;
			blue = color.getRed() > factor ? color.getRed() - factor : 0;
		}
		
		return new Color(getShell().getDisplay(), red, green, blue);
	}
	
	public void setDialog(WarlockDialog dialog) {
		if(this.dialog != null)
			this.dialog.removeListener(listener);
		this.dialog = dialog;
		dialog.addListener(listener);
	}
	
}
