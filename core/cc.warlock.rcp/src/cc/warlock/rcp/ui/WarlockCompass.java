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
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import cc.warlock.core.client.ICompass;
import cc.warlock.core.client.ICompass.DirectionType;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.internal.Command;
import cc.warlock.rcp.ui.client.SWTPropertyListener;
import cc.warlock.rcp.ui.style.CompassTheme;

/**
 * This is our custom compass that is drawn on top of the text widget and is movable like a normal window (within the text area for now).
 * @author marshall
 */
public class WarlockCompass extends Canvas implements IPropertyListener<ICompass> {

	private Cursor moveCursor;
	private CompassTheme theme;
	private IWarlockClient client;
	private IPropertyListener<ICompass> listener;
	private Image compassImage;// = WarlockSharedImages.getImage(WarlockSharedImages.IMG_COMPASS_SMALL_MAIN);
	
	public WarlockCompass (Composite parent, int style, CompassTheme theme, Composite container) {
		
		super(parent, style);
		this.theme = theme;
		
		moveCursor = new Cursor(parent.getDisplay(), SWT.CURSOR_HAND);
		compassImage = theme.getMainImage();
		//this.container = container;
		
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				moveCursor.dispose();
				moveCursor = null;
				if(listener != null)
					WarlockCompass.this.client.getCompass().removeListener(listener);
			}
		});
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				drawCompass(e.gc);
			}
		});
		addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {}
			public void mouseDown(MouseEvent e) {
				// Should we use this to check the mouse wasn't moved while clicked?
			}
			public void mouseUp(MouseEvent e) {
				click(new Point(e.x, e.y));
			}
		});
		getParent().addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				//System.out.println("compass: " + getParent().getSize());
				redraw();
			}
		});
	}
	
	private void drawCompass (GC gc) {
		if(this.isDisposed() || gc.isDisposed())
			return;
		
		/*ImageData imgData = compassImage.getImageData();
		int height = Math.min(getParent().getSize().y - 6, imgData.height);
		gc.setClipping(0, 0, imgData.width, height);
		System.out.println("height: " + height);
		System.out.println("compass: " + this.getSize());
		this.setSize(imgData.width, height);*/
		gc.drawImage(compassImage, 0, 0);
		if (client != null && client.getCompass() != null && client.getCompass().get() != null) {
			for (DirectionType direction : DirectionType.values()) {
				if (direction != DirectionType.None && client.getCompass().get().getDirections().contains(direction))
				{
					Point point = theme.getDirectionPosition(direction);
					gc.drawImage(theme.getDirectionImage(direction), point.x, point.y);
				}
			}
		} else {
			// draw all "on" by default
			for (DirectionType direction : DirectionType.values()) {
				if (direction != DirectionType.None) {
					Point point = theme.getDirectionPosition(direction);
					gc.drawImage(theme.getDirectionImage(direction), point.x, point.y);
				}
			}
		}
	}
	
	protected void click(Point c) {
		if (client == null || client.getCompass() == null || client.getCompass().get() == null)
			return;
		for (DirectionType direction : DirectionType.values()) {
			if (direction != DirectionType.None && client.getCompass().get().getDirections().contains(direction)) {
				Point point = theme.getDirectionPosition(direction);
				if(c.x >= point.x && c.x <= point.x + theme.getDirectionWidth(direction)
						&& c.y >= point.y && c.y <= point.y + theme.getDirectionHeight(direction)) {
					client.send(new Command(direction.getName()));
					break;
				}
			}
		}
	}
	
	public void propertyChanged(ICompass value) {
		redraw();
	}
	
	public void setClient(IWarlockClient client) {
		listener = new SWTPropertyListener<ICompass>(this);
		client.getCompass().addListener(listener);
		this.client = client;
	}
}
