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
package cc.warlock.rcp.telnet.core.client;

import java.io.IOException;

import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IWarlockSkin;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.internal.WarlockClient;
import cc.warlock.core.client.internal.WarlockMonospace;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.network.Connection;
import cc.warlock.core.network.IConnection;
import cc.warlock.core.network.IConnection.ErrorType;
import cc.warlock.core.network.IConnectionListener;
import cc.warlock.rcp.telnet.ui.DefaultSkin;

/**
 * @author Will Robertson
 *
 */
public class TelnetClient extends WarlockClient {
	protected String hostname;
	protected String characterName, clientId;
	protected IWarlockSkin skin;
	protected IWarlockStyle commandStyle;
	
	public TelnetClient ()
	{
		super();
		
		characterName = "<telnet>";
		skin = new DefaultSkin();
		commandStyle = new WarlockStyle();
		commandStyle.setBackgroundColor(new WarlockColor("#000033"));
		
		//getClientSettings().addClientSettingProvider(MacroRegistry.instance());
	}
	
	/* (non-Javadoc)
	 * @see cc.warlock.core.client.internal.WarlockClient#connect(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void connect(String server, int port, String key) throws IOException {
		// TODO Auto-generated method stub
		hostname = server;
		connection = new Connection();
		connection.addConnectionListener(new IConnectionListener () {
			public void connected(IConnection connection) {
				WarlockClientRegistry.clientConnected(TelnetClient.this);
			}
			public void connectionError(IConnection connection,
					ErrorType errorType) {
			}
			public void dataReady(IConnection connection, String data) {
				// Push the raw input into the Stream
				WarlockString string = new WarlockString(data);
				string.addStyle(WarlockMonospace.getInstance());
				getDefaultStream().put(string);
			}
			public void dataSent (IConnection connection, String data) {
				
			}
			public void disconnected(IConnection connection) {
				WarlockClientRegistry.clientDisconnected(TelnetClient.this);
			}
		});
		
		clientId = "telnet:" + hostname + ":" + port + "@" + hashCode();
		connection.connect(server, port);
	}

	
	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockClient#getCharacterName()
	 */
	public String getCharacterName() {
		return characterName;
	}
	
	public void setCharacterName(String name) {
		characterName = name;
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockClient#getClientId()
	 */
	public String getClientId() {
		return clientId;
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockClient#getCommandStyle()
	 */
	public IWarlockStyle getCommandStyle() {
		return commandStyle;
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockClient#getSkin()
	 */
	public IWarlockSkin getSkin() {
		return skin;
	}

	@Override
	public IStream createStream(String streamName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IClientSettings getClientSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVariable(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeVariable(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVariable(String id, String value) {
		// TODO Auto-generated method stub
		
	}

}
