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
 */package cc.warlock.core.client;


/**
 * @author Marshall
 *
 * A macro represents a certain key code / modifier combination and the list of handlers (ordered by precedence) that are able/willing to handle the macro.
 */
public interface IMacro {

	public static final int NO_MODIFIERS = 0;
	
	public String getKeyString ();
	public void setKeyString (String keyString);
	
	//public void setCommand (String command);
	public IMacroHandler getHandler();
	
	public void execute(IWarlockClientViewer viewer);

}
