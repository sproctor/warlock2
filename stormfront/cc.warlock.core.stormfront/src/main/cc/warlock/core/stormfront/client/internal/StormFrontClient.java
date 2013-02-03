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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.internal.DefaultMacro;
import cc.warlock.core.client.internal.Property;
import cc.warlock.core.client.internal.Stream;
import cc.warlock.core.client.internal.WarlockClient;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.client.settings.ClientSettings;
import cc.warlock.core.client.settings.MacroConfigurationProvider;
import cc.warlock.core.client.settings.PresetStyleConfigurationProvider;
import cc.warlock.core.settings.ConfigurationUtil;
import cc.warlock.core.settings.MacroSetting;
import cc.warlock.core.settings.WarlockPreferencesScope;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.client.IStormFrontClientViewer;
import cc.warlock.core.stormfront.network.StormFrontConnection;
import cc.warlock.core.stormfront.xml.StormFrontDocument;
import cc.warlock.core.stormfront.xml.StormFrontElement;
import cc.warlock.core.util.Pair;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StormFrontClient extends WarlockClient implements IStormFrontClient, IRoomListener {

	protected StringBuffer buffer = new StringBuffer();
	protected Property<String> roomDescription = new Property<String>();
	protected String gameCode, playerId;
	protected ClientSettings clientSettings;
	//protected StormFrontServerSettings serverSettings;

	protected HashMap<String, String> commands;
	
	public StormFrontClient(String gameCode) {
		super();
		
		this.gameCode = gameCode;
		
		WarlockClientRegistry.activateClient(this);
	}

	public void connect(String server, int port, String key) throws IOException {
		connection = new StormFrontConnection(this, key);
		connection.connect(server, port);
		
		WarlockClientRegistry.clientConnected(this);
	}
	
	public void streamCleared() {
		// TODO Auto-generated method stub
		
	}

	public String getPlayerId() {
		return playerId;
	}
	
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
		
		clientSettings = ClientSettings.getClientSettings(playerId);
		
		if(clientSettings.isNewSetting()) {
			for(DefaultMacro macro : viewer.getDefaultMacros()) {
				MacroSetting smacro = MacroConfigurationProvider.getProvider(clientSettings).createSetting();
				smacro.setCommand(macro.getCommand());
				smacro.setKeyString(macro.getKeyString());
			}
			try {
				WarlockPreferencesScope.getInstance().getNode().flush();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		// TODO: import server settings
		//serverSettings = new StormFrontServerSettings();
		//clientSettings.addChildProvider(serverSettings);
		
		WarlockClientRegistry.clientSettingsLoaded(this);
	}
	
	public IClientSettings getClientSettings() {
		return clientSettings;
	}
	
	public String getCharacterName() {
		if (clientSettings == null)
			return null;
		return clientSettings.getName();
	}
	
	public void setCharacterName(String name) {
		if (clientSettings != null)
			clientSettings.setName(name);
	}
	
	public String getGameCode() {
		return gameCode;
	}
	
	public String getClientId() {
		//return gameCode + "_" + playerId;
		return playerId;
	}
	
	@Override
	public IStream getDefaultStream() {
		IStream stream = this.getStream(DEFAULT_STREAM_NAME);
		if(stream != null)
			return stream;
		
		stream = createStream(DEFAULT_STREAM_NAME);
		stream.setLogging(true);
		return stream;
	}
	
	@Override
	protected void finalize() throws Throwable {
		WarlockClientRegistry.removeClient(this);
		super.finalize();
	}
	
	public IWarlockStyle getCommandStyle() {
		IWarlockStyle style = getNamedStyle(PresetStyleConfigurationProvider.PRESET_COMMAND);
		if (style == null) {
			return new WarlockStyle();
		}
		return style;
	}
	
	public void loadCmdlist()
	{
		try {
			commands  = new HashMap<String, String>();
			FileInputStream stream = new FileInputStream(ConfigurationUtil.getConfigurationFile("cmdlist1.xml"));
			StormFrontDocument document = new StormFrontDocument(stream);
			stream.close();
			
			StormFrontElement cmdlist = document.getRootElement();
			for (StormFrontElement cliElement : cmdlist.elements())
			{
				if(cliElement.getName().equals("cli")) {
					String coord = cliElement.attributeValue("coord");
					String command = cliElement.attributeValue("command");
					
					if(coord != null && command != null)
						commands.put(coord, command);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getCommand(String coord) {
		if(commands == null) return null;
		return commands.get(coord);
	}
	
	public void launchURL(String url) {
		if (viewer instanceof IStormFrontClientViewer)
		{
			try {
				((IStormFrontClientViewer)viewer).launchURL(new URL(url));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void appendImage(String pictureId) {
		try {
			URL url = new URL("http://www.play.net/bfe/DR-art/" + pictureId + "_t.jpg");
			
			if (viewer instanceof IStormFrontClientViewer)
				((IStormFrontClientViewer) viewer).appendImage(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startedDownloadingServerSettings() {
		if (viewer instanceof IStormFrontClientViewer)
			((IStormFrontClientViewer)viewer).startedDownloadingServerSettings();
	}
	
	public void finishedDownloadingServerSettings(String str) {
		File settingsFile = ConfigurationUtil.getConfigurationFile("serverSettings_" + getClientId() + ".xml");
		InputStream inStream = new ByteArrayInputStream(str.getBytes());
		
		try {
			FileWriter writer = new FileWriter(settingsFile);

			StormFrontDocument document = new StormFrontDocument(inStream);
			document.saveTo(writer, true);
			
			writer.close();
			inStream.close();
			buffer.setLength(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			InputStream stream = new FileInputStream(settingsFile);
			
			//serverSettings.importServerSettings(stream, clientSettings);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (viewer instanceof IStormFrontClientViewer)
			((IStormFrontClientViewer)viewer).finishedDownloadingServerSettings();
	}
	
	public void receivedServerSetting(String setting) {
		if (viewer instanceof IStormFrontClientViewer)
			((IStormFrontClientViewer)viewer).receivedServerSetting(setting);
	}
	
	@Override
	public IStream createStream(String streamName) {
		synchronized(streams) {
			IStream stream = getStream(streamName);
			if(stream == null) {
				stream = new Stream(this, streamName);
				streams.put(streamName, stream);
				for(Iterator<Pair<String, IStreamListener>> iter =
						potentialListeners.iterator(); iter.hasNext(); ) {
					Pair<String, IStreamListener> pair = iter.next();
					if(pair.first().equals(streamName)) {
						stream.addStreamListener(pair.second());
						iter.remove();
					}
				}
				// TODO: or should this always be called?
				stream.create();
			}
			return stream;
		}
	}
}
