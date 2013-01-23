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
package cc.warlock.rcp.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import cc.warlock.core.client.ICompass;
import cc.warlock.core.client.ICompass.DirectionType;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.rcp.ui.style.CompassTheme;

/**
 * This is our custom compass that is drawn on top of the text widget and is movable like a normal window (within the text area for now).
 * @author marshall
 */
public class WarlockCompass extends Canvas implements MouseListener, IPropertyListener<ICompass> {

	//private Point originalPosition = new Point(-1,-1);
	private Cursor moveCursor;
	private CompassTheme theme;
	private ICompass compass;
	
	private Image compassImage = WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_SMALL_MAIN);
	
	public WarlockCompass (Composite parent, int style, CompassTheme theme)
	{
		super(parent, style);
		this.theme = theme;
		
		moveCursor = new Cursor(parent.getDisplay(), SWT.CURSOR_HAND);
		compassImage = theme.getMainImage();
		
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				moveCursor.dispose();
				compassImage.dispose();
			}
		});
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				drawCompass(e.gc);
			}
		});
		
	}
	
	private void drawCompass (GC gc)
	{
		/* From old compass embedded in text widget
		if (x == -1)
			setX(text.getClientArea().width - compassBounds.width - 5);
		if (y == -1)
			setY(text.getClientArea().height - compassBounds.height - 5);
		*/
		
		gc.drawImage(compassImage, 0, 0);
		if (compass != null)
		{
			for (DirectionType direction : DirectionType.values())
			{
				if (direction != DirectionType.None && compass.getDirections().contains(direction))
				{
					Point point = theme.getDirectionPosition(direction);
					gc.drawImage(theme.getDirectionImage(direction), point.x, point.y);
				}
			}
		} else {
			// draw all "on" by default
			for (DirectionType direction : DirectionType.values())
			{
				if (direction != DirectionType.None)
				{
					Point point = theme.getDirectionPosition(direction);
					gc.drawImage(theme.getDirectionImage(direction), point.x, point.y);
				}
			}
		}
	}
	
	
	public void mouseDoubleClick(MouseEvent e) {}
	
	public void mouseDown(MouseEvent e) {
		// store position
	}
	
	public void mouseUp(MouseEvent e) {
		// make sure position is equal to down, then active the appropriate direction
	}
	
	public void propertyChanged(ICompass value) {
		compass = value;
		redraw();
	}

	public ICompass getCompass() {
		return compass;
	}

}
