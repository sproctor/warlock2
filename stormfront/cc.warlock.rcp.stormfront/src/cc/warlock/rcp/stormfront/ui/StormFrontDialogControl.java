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
import java.util.Map.Entry;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import cc.warlock.core.stormfront.IStormFrontDialogListener;

/**
 * @author Marshall
 * 
 * This is a custom progress bar that mimics the L&F of StormFront's status bars.
 * It's sort of a dirty hack, but it suffices for now. It needs to handle being in a LayoutManager better...
 */
public class StormFrontDialogControl extends Canvas implements IStormFrontDialogListener
{
	protected Font progressFont;
	protected Color borderColor;
	protected int width, height;
	protected int borderWidth;
	protected HashMap<String, ProgressBarData> progressBars = new HashMap<String, ProgressBarData>();
	
	public StormFrontDialogControl (Composite composite, int style)
	{
		super(composite, style);
		
		// defaults
		width = 100;
		height = 15;
		
		Font textFont = JFaceResources.getDefaultFont();
		FontData textData = textFont.getFontData()[0];
		int minHeight = 8;
		
		progressFont = new Font(getShell().getDisplay(),
			textData.getName(), (int)Math.max(minHeight,textData.getHeight()), textData.getStyle());
		
		borderColor = new Color(getShell().getDisplay(), 25, 25, 25);
		borderWidth = 1;
		
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle bounds = getBounds();
				int fullBarWidth = bounds.width - 2 * borderWidth;
				int fullBarHeight = bounds.height - 2 * borderWidth;
				
				e.gc.setForeground(borderColor);
				e.gc.setLineWidth(borderWidth);
				e.gc.drawRectangle(0, 0, bounds.width, bounds.height);
				
				e.gc.setFont (progressFont);
				
				for(Entry<String, ProgressBarData> entry : progressBars.entrySet()) {
					ProgressBarData progressBar = entry.getValue();
					
					int barWidth = progressBar.width * fullBarWidth / 100;
					int filledWidth = progressBar.value * barWidth / 100;
					int left = progressBar.left * fullBarWidth / 100;
					
					Color bgColor = getBgColor(entry.getKey());
					
					// draw the filled part of the rectangle
					Color gradientColor = getGradientColor(25, true, bgColor);
					e.gc.setBackground(gradientColor);
					e.gc.setForeground(bgColor);
					e.gc.fillGradientRectangle(borderWidth + left, borderWidth,
							filledWidth, fullBarHeight, false);
					
					// draw the background
					e.gc.setBackground(borderColor);
					e.gc.fillRectangle(borderWidth + left + filledWidth, borderWidth,
							fullBarWidth - left - filledWidth, fullBarHeight);
					
					Color textColor = getTextColor(entry.getKey());
					e.gc.setForeground(textColor);
					
					String text = progressBar.text;
					Point extent = e.gc.textExtent(text);
					
					int text_left = (bounds.width - 2 * borderWidth - extent.x) / 2;
					int text_top = (bounds.height - 2 * borderWidth - e.gc.getFontMetrics().getHeight()) / 2;
					e.gc.drawText (text, text_left, text_top, true);
				}
			}
		});
	}
	
	private Color getGradientColor (int factor, boolean lighter, Color color)
	{
		int red = 0;
		int green = 0;
		int blue = 0;
		
		if (lighter) 
		{
			red = color.getRed() < (255 - factor) ? color.getRed() + factor : 255;
			green = color.getGreen() < (255 - factor) ? color.getGreen() + factor : 255;
			blue = color.getBlue() < (255 - factor) ? color.getBlue() + factor : 255;
		}
		else {
			red = color.getRed() > factor ? color.getRed() - factor : 0;
			green = color.getRed() > factor ? color.getRed() - factor : 0;
			blue = color.getRed() > factor ? color.getRed() - factor : 0;
		}
		
		return new Color(getShell().getDisplay(), red, green, blue);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		
		redraw();
	}
	
	public Point computeSize(int wHint, int hHint, boolean changed) {
		
		return new Point (width, height);
	}
	
	public void dispose() {
		progressFont.dispose();
		
		super.dispose();
	}

	public void progressBar(String id, String text, int value, String left, String top,
			String width, String height) {
		progressBars.put(id, new ProgressBarData(text, value, Integer.parseInt(left),
				Integer.parseInt(top), Integer.parseInt(width), Integer.parseInt(height)));
		redraw();
	}
	
	protected class ProgressBarData {
		public String text;
		public int value;
		public int left;
		public int top;
		public int width;
		public int height;
		
		public ProgressBarData(String text, int value, int left, int top,
				int width, int height) {
			this.text = text;
			this.value = value;
			this.left = left;
			this.top = top;
			this.width = width;
			this.height = height;
		}
	}
	
	private Color getTextColor(String id) {
		Display display = Display.getDefault();
		
		if(id.equals("health"))
			return new Color(display, 255, 255, 255);
		
		return new Color(display, 255, 255, 255);
	}
	
	private Color getBgColor(String id) {
		Display display = Display.getDefault();
		
		if(id.equals("health"))
			return new Color(display, 0x80, 0, 0);
		
		return new Color(display, 225, 225, 225);
	}
}
