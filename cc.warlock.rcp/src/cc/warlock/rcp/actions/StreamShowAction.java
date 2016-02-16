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
/**
 * 
 */
package cc.warlock.rcp.actions;

import java.util.Random;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import cc.warlock.rcp.views.UserStream;

/**
 * @author Will Robertson
 * Action for Selecting UserStreams from the Menu
 */
public class StreamShowAction extends AbstractHandler {
	public final static String VIEW_ID = "cc.warlock.rcp.actions.StreamShowAction";
	
	@Override
	public Object execute(ExecutionEvent event)
	throws ExecutionException {
		String name = event.getParameter("name");
		UserStream.getViewForUserStream(name);
		HandlerUtil.toggleCommandState(event.getCommand());
		//HandlerUtil.toggleCommandState(event.getCommand());
		return null;
	}
	
	protected static String generateUniqueId () {
		return new Random().nextInt() + "";
	}
}
