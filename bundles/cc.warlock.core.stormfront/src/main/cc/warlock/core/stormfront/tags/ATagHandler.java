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

import cc.warlock.core.client.Command;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockStyle;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.settings.CmdlistSettings;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class ATagHandler extends DefaultTagHandler {

	WarlockStyle style;
	boolean requestedList = false;
	int menuCount = 0;
	
	private class CommandRunner implements Runnable {
		private IWarlockClient client;
		private String coord;
		private String noun;
		//private String exist;
		
		CommandRunner(IWarlockClient client, String coord, String noun, String exist) {
			this.client = client;
			this.coord = coord;
			this.noun = noun;
			//this.exist = exist;
		}
		
		public void run() {
			String command = CmdlistSettings.getProvider(client.getClientSettings()).getCli(coord).getCommand();
			if(command != null) {
				if(noun != null) {
					command = command.replaceAll("@", noun);
				}
				client.send(new Command(command, true));
			}
			
		}

	}
	
	private class CommandMenuRunner implements Runnable {
		private IWarlockClient client;
		private String noun;
		private String exist;
		
		CommandMenuRunner(IWarlockClient client, String noun, String exist) {
			this.client = client;
			this.noun = noun;
			this.exist = exist;
		}
		
		public void run() {
			if(exist != null) {
				menuCount++;
				String id = String.valueOf(menuCount);
				String command = "_menu #" + exist + " " + id;
				client.send(new Command(command, false));

				client.getViewer().createMenu(id);
				getHandler().setMenuData(id, noun);
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
			getHandler().removeStyle(style);
			style = null;
		}
		String coord = attributes.getValue("coord");
		String noun = attributes.getValue("noun");
		String exist = attributes.getValue("exist");

		style = new WarlockStyle();
		style.setUnderline(true);
		if(coord != null) {
			style.setAction(new CommandRunner(getHandler().getClient(), coord, noun, exist));
		} else {
			style.setAction(new CommandMenuRunner(getHandler().getClient(), noun, exist));
		}
		getHandler().addStyle(style);
		
		// TODO this should probably be done elsewhere
		if(!requestedList) {
			String command = "_menu update "
					+ CmdlistSettings.getProvider(getHandler().getClient().getClientSettings()).getTimestamp();
			getHandler().getClient().send(new Command(command, false));
			requestedList = true;
		}
	}
	
	@Override
	public void handleEnd(String rawXML) {
		if(style != null) {
			getHandler().removeStyle(style);
			style = null;
		}
	}
}
