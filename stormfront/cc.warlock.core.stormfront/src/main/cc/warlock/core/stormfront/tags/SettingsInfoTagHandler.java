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

import java.io.IOException;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class SettingsInfoTagHandler extends DefaultTagHandler {
	
	public SettingsInfoTagHandler(IStormFrontProtocolHandler handler) {
		super (handler);
	}
	
	@Override
	public String getTagName() {
		return "settingsInfo";
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		//FIXME: this function is broken beyong belief. We're not dealing with server settings at all currently.
		
		// FIXME: the following try-block should be removed when this function is made to work again.
		try {
			getHandler().getClient().getConnection().sendLine("");
			return;
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		if (attributes.getAttribute("space") != null
			&& attributes.getAttribute("not") != null
			&& attributes.getAttribute("found") != null)
		{
			// This is a character that has no server settings, we need to immediately send our own
			// StormFrontServerSettings.sendInitialServerSettings(getHandler().getClient());
			
			//PromptTagHandler promptHandler = (PromptTagHandler) getHandler().getTagHandler(PromptTagHandler.class);
			// promptHandler.setWaitingForInitialStreams(true);
			return;
		}
		
		/* At some point we should check these values, and if the server sent
		 * us a version newer than our archived version of their settings
		 * we should prompt the user asking if they want to add the difference.
		String crc = attributes.getValue("crc");
		
		String major = attributes.getValue("major");
		if(major != null) {
			majorVersion = Integer.parseInt(attributes.getValue("major"));
		}
		
		String clientVersion = attributes.getValue("client");
		if(clientVersion != null) {
			getHandler().getClient().getServerSettings().setClientVersion(clientVersion);
		}
		*/
		
		/*String playerId = getHandler().getClient().getPlayerId();
		
		File serverSettings = ConfigurationUtil.getConfigurationFile("serverSettings_" + playerId + ".xml", false);
		if (!serverSettings.exists())
		{
			try {
				getHandler().getClient().getConnection().send("<sendSettings/>\n");
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				getHandler().getClient().getConnection().sendLine("");
			} catch(IOException e) {
				e.printStackTrace();
			}
		}*/
	}
}
