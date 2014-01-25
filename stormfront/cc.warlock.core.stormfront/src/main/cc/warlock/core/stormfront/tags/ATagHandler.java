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
package cc.warlock.core.stormfront.tags;

import cc.warlock.core.client.internal.Command;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class ATagHandler extends DefaultTagHandler {

	WarlockStyle style;
	boolean requestedList = false;
	
	private class CommandRunner implements Runnable {
		private IStormFrontClient client;
		private String coord;
		private String noun;
		
		CommandRunner(IStormFrontClient client, String coord, String noun) {
			this.client = client;
			this.coord = coord;
			this.noun = noun;
		}
		
		public void run() {
			String command = client.getCommand(coord);
			if(command != null) {
				if(noun != null) {
					command = command.replaceAll("@", noun);
				}
				client.send(new Command(command, true));
			}
		}

	}
	
	public ATagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String getTagName() {
		return "a";
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		if(style != null) {
			handler.removeStyle(style);
			style = null;
		}
		String coord = attributes.getValue("coord");

		style = new WarlockStyle();
		style.setUnderline(true);
		if(coord != null) {
			String noun = attributes.getValue("noun");
			style.setAction(new CommandRunner(handler.getClient(), coord, noun));
			if(!requestedList) {
				handler.getClient().send(new Command("_menu update 1", true));
				requestedList = true;
			}
		}
		handler.addStyle(style);
	}
	
	@Override
	public void handleEnd(String rawXML) {
		if(style != null) {
			handler.removeStyle(style);
			style = null;
		}
	}
}
