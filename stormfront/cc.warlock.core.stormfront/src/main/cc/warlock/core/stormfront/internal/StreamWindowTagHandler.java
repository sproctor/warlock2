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
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class StreamWindowTagHandler extends DefaultTagHandler {

	public StreamWindowTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "streamWindow" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		String subtitle = attributes.getValue("subtitle");
		String title = attributes.getValue("title");
		String id = attributes.getValue("id");
		
		if (id != null && title != null)
		{
			if (subtitle != null)
				handler.getClient().getStream(id).setSubtitle(subtitle);
			
			handler.getClient().getStream(id).setTitle(title);
		}
		
		String ifClosed = attributes.getValue("ifClosed");
		if(ifClosed != null)
			handler.getClient().getStream(id).setClosedTarget(ifClosed);
		
		String closedStyle = attributes.getValue("styleIfClosed");
		if(closedStyle != null)
			handler.getClient().getStream(id).setClosedStyle(closedStyle);
	}
}
