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

package cc.warlock.core.stormfront.client.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.internal.DefaultMacros;
import cc.warlock.core.client.internal.WarlockClient;
import cc.warlock.core.client.internal.WarlockMacro;
import cc.warlock.core.client.settings.ClientSettings;
import cc.warlock.core.client.settings.MacroConfigurationProvider;
import cc.warlock.core.settings.MacroSetting;
import cc.warlock.core.settings.WarlockPreferencesScope;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.network.StormFrontConnection;

/**
 * @author Marshall
 */
public class StormFrontClient extends WarlockClient implements IStormFrontClient, IRoomListener {

	//protected StringBuffer buffer = new StringBuffer();
	//protected Property<String> roomDescription = new Property<String>();
	protected String gameCode, playerId;
	//protected StormFrontServerSettings serverSettings;

	protected HashMap<String, String> commands;
	
	public StormFrontClient(String gameCode) {
		super();
		
		this.gameCode = gameCode;
		
		WarlockClientRegistry.clientCreated(this);
	}

	@Override
	public void connect(String server, int port, String key) throws IOException {
		connection = new StormFrontConnection(this, key);
		connection.connect(server, port);
		
		WarlockClientRegistry.clientConnected(this);
	}
	

	@Override
	public String getPlayerId() {
		return playerId;
	}
	
	@Override
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
		
		clientSettings = ClientSettings.getClientSettings(playerId);
		
		if(clientSettings.isNewSetting()) {
			for(WarlockMacro macro : DefaultMacros.instance().getCollection()) {
				MacroSetting smacro = MacroConfigurationProvider.getProvider(clientSettings).createSetting();
				smacro.setCommand(macro.getCommand());
				smacro.setKeyString(macro.getKeyString());
			}
			WarlockPreferencesScope.getInstance().flush();
		}
		// TODO: import server settings
		//serverSettings = new StormFrontServerSettings();
		//clientSettings.addChildProvider(serverSettings);
		
		WarlockClientRegistry.clientSettingsLoaded(this);
	}
	
	@Override
	public String getCharacterName() {
		if (clientSettings == null)
			return null;
		return clientSettings.getName();
	}
	
	@Override
	public void setCharacterName(String name) {
		if (clientSettings != null)
			clientSettings.setName(name);
	}
	
	@Override
	public String getGameCode() {
		return gameCode;
	}
	
	@Override
	public String getClientId() {
		//return gameCode + "_" + playerId;
		return playerId;
	}
	
	@Override
	public void launchURL(String url) {
		try {
			viewer.launchURL(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void appendImage(String pictureId) {
		try {
			URL url = new URL("http://www.play.net/bfe/DR-art/" + pictureId + "_t.jpg");
			viewer.appendImage(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
