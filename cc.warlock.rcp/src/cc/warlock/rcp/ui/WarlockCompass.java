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

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
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

import cc.warlock.core.client.Command;
import cc.warlock.core.client.ICompass;
import cc.warlock.core.client.ICompass.DirectionType;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.rcp.ui.client.SWTPropertyListener;
import cc.warlock.rcp.ui.style.CompassTheme;

/**
 * This is our custom compass that is drawn on top of the text widget and is movable like a normal window (within the text area for now).
 * @author marshall
 */
public class WarlockCompass extends Canvas {

	private Cursor moveCursor;
	private CompassTheme theme;
	private IWarlockClient client;
	private IPropertyListener<ICompass> listener = new SWTPropertyListener<ICompass>(new IPropertyListener<ICompass>() {
		@Override
		public void propertyChanged(ICompass value) {
			redraw();
		}
	});
	private double scale = 1.0;
	private Image scaledImage;
	private HashMap<DirectionType, Image> scaledDirections = new HashMap<DirectionType, Image>();
	
	public WarlockCompass (final Composite parent, int style, CompassTheme theme) {
		
		super(parent, style);
		this.theme = theme;
		moveCursor = new Cursor(getDisplay(), SWT.CURSOR_HAND);
		setScale(parent.getSize().y - 10);
		
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				moveCursor.dispose();
				moveCursor = null;
				if(client != null)
					client.getCompass().removeListener(listener);
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
				// TODO Should we use this to check the mouse wasn't moved while clicked?
			}
			public void mouseUp(MouseEvent e) {
				click(e.x, e.y);
			}
		});
		parent.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				setScale(parent.getSize().y);
			}
		});
		addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				if(getDirectionAt(e.x, e.y) != null)
					WarlockCompass.this.setCursor(moveCursor);
				else
					WarlockCompass.this.setCursor(null);
			}
		});
	}
	
	private void setScale(int windowHeight) {
		ImageData data = theme.getMainImage().getImageData();
		int height = data.height;
		int width = data.width;
		if(height > windowHeight)
			scale = (double)windowHeight / (double)height;
		else
			scale = 1.0;
		if(scaledImage != null)
			scaledImage.dispose();
		if(windowHeight > 0) {
			scaledImage = new Image(getDisplay(), data.scaledTo((int)(width*scale),
				(int)(height*scale)));
		} else {
			scaledImage = null;
			scale = 1.0;
		}
		for (DirectionType direction : DirectionType.values()) {
			if (direction != DirectionType.None) {
				Image oldImage = scaledDirections.get(direction);
				if(oldImage != null)
					oldImage.dispose();
				ImageData dirData = theme.getDirectionImage(direction).getImageData();
				Image newImage = null;
				int dirHeight = (int)(dirData.height*scale);
				int dirWidth = (int)(dirData.width*scale);
				if(dirHeight > 0 && dirWidth > 0)
					newImage = new Image(getDisplay(), dirData.scaledTo(dirWidth, dirHeight));
				scaledDirections.put(direction, newImage);
			}
		}
	}
	
	private void drawCompass (GC gc) {
		if(this.isDisposed() || gc.isDisposed())
			return;
		Image image = scaledImage == null ? theme.getMainImage() : scaledImage;
		gc.drawImage(image, 0, 0);
		if (client != null && client.getCompass() != null && client.getCompass().get() != null) {
			for (DirectionType direction : DirectionType.values()) {
				if (direction != DirectionType.None && client.getCompass().get().getDirections().contains(direction)) {
					Point point = theme.getDirectionPosition(direction);
					Image dirImage = scaledDirections.get(direction);
					if(dirImage != null)
						gc.drawImage(dirImage, (int)(point.x*scale), (int)(point.y*scale));
				}
			}
		}
	}
	
	protected void click(int x, int y) {
		DirectionType direction = getDirectionAt(x, y);
		if(direction != null)
			client.send(new Command(direction.getName()));
	}
	
	private DirectionType getDirectionAt(int x, int y) {
		if (client == null || client.getCompass() == null || client.getCompass().get() == null)
			return null;
		for (DirectionType direction : DirectionType.values()) {
			if (direction != DirectionType.None && client.getCompass().get().getDirections().contains(direction)) {
				Point point = theme.getDirectionPosition(direction);
				int dirx = (int)(point.x * scale);
				int diry = (int)(point.y * scale);
				int width = (int)(theme.getDirectionWidth(direction) * scale);
				int height = (int)(theme.getDirectionHeight(direction) * scale);
				if(x >= dirx && x <= dirx + width && y >= diry && y <= diry + height) {
					return direction;
				}
			}
		}
		return null;
	}
	
	public void setClient(IWarlockClient client) {
		if(this.client != null)
			this.client.getCompass().removeListener(listener);
		client.getCompass().addListener(listener);
		this.client = client;
	}
}
