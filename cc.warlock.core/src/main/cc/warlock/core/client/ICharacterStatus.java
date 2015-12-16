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


/**
 * This interface represents the current status of the current character.
 * 
 * @author marshall
 *
 */
public interface ICharacterStatus extends IProperty<String> {

	public static enum StatusType {
		Kneeling, Prone, Sitting, Standing, Stunned, Bleeding, Hidden, Invisible, Dead, Webbed, Joined;
		
		public String toIconString() {
			return "Icon" + name().toUpperCase();
		}
		
		public static StatusType getStatusType (String name)
		{
			for (StatusType type : StatusType.values())
			{
				if (type.toIconString().equals(name))
					return type;
			}
			return null;
		}
	};
	
	public Boolean hasStatus(StatusType type);
	public Boolean hasStatus(String name);
	public void unset(String status);
}
