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

import java.util.HashMap;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import cc.warlock.core.client.ICompass.DirectionType;
import cc.warlock.rcp.ui.WarlockSharedImages;


public class CompassTheme {

	private String name, title, description;
	private Image mainImage;
	private HashMap<DirectionType, CompassDirection> directions = new HashMap<DirectionType, CompassDirection>();
	
	public class CompassDirection {
		private Point position;
		private Image image;
		
		public CompassDirection(Point position, Image image) {
			this.position = position;
			this.image = image;
		}
		
		public Point getPosition() {
			return position;
		}
		
		public Image getImage() {
			return image;
		}
		
		public int getWidth() {
			return image.getImageData().width;
		}
		
		public int getHeight() {
			return image.getImageData().height;
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Image getMainImage() {
		return mainImage;
	}
	
	public void setMainImage(String mainImagePath) {
		this.mainImage = WarlockSharedImages.getImage(mainImagePath);
	}
	
	public Image getDirectionImage (DirectionType direction)
	{
		return directions.get(direction).getImage();
	}
	
	public void setDirection (DirectionType direction, String position, String imagePath)
	{
		String[] xy = position.split(",");
		directions.put(direction, new CompassDirection(new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])),
				WarlockSharedImages.getImage(imagePath)));
	}
	
	public Point getDirectionPosition (DirectionType direction)
	{
		return directions.get(direction).getPosition();
	}
	
	public int getDirectionWidth(DirectionType direction) {
		return directions.get(direction).getWidth();
	}
	
	public int getDirectionHeight(DirectionType direction) {
		return directions.get(direction).getHeight();
	}
}
