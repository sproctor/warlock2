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
/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.client.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import cc.warlock.core.client.ICharacterStatus;
import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.ICommandHistory;
import cc.warlock.core.client.ICompass;
import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.IWarlockDialog;
import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.WarlockTimer;
import cc.warlock.core.client.logging.IClientLogger;
import cc.warlock.core.client.logging.SimpleLogger;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.client.settings.IVariable;
import cc.warlock.core.client.settings.IWarlockSettingListener;
import cc.warlock.core.client.settings.internal.HighlightConfigurationProvider;
import cc.warlock.core.client.settings.internal.PresetStyleConfigurationProvider;
import cc.warlock.core.client.settings.internal.VariableConfigurationProvider;
import cc.warlock.core.configuration.IWarlockSetting;
import cc.warlock.core.network.IConnection;
import cc.warlock.core.util.Pair;


/**
 * @author Marshall
 */
public abstract class WarlockClient implements IWarlockClient, IWarlockSettingListener {

	protected IConnection connection;
	protected IWarlockClientViewer viewer;
	protected IWarlockClientListener listener;
	private String lastCommand;
	protected ICommandHistory commandHistory = new CommandHistory();
	protected String streamPrefix;
	private Collection<IRoomListener> roomListeners = Collections.synchronizedCollection(new ArrayList<IRoomListener>());
	protected Property<ICompass> compass = new Property<ICompass>(null);
	protected IClientLogger logger;
	protected HashMap<String, IStream> streams = new HashMap<String, IStream>();
	protected ArrayList<Pair<String, IStreamListener>> potentialListeners = new ArrayList<Pair<String, IStreamListener>>();
	private ArrayList<Collection<IWarlockHighlight>> highlightLists = null; // Highlights from scripts
	private ArrayList<IWarlockHighlight> cachedHighlights = null;
	private ICharacterStatus status;
	private HashMap<String, WarlockTimer> timers = new HashMap<String, WarlockTimer>();
	private HashMap<String, String> components = new HashMap<String, String>();
	private HashMap<String, IStream> componentStreams = new HashMap<String, IStream>();
	private HashMap<String, Property<IWarlockDialog>> dialogs =
			new HashMap<String, Property<IWarlockDialog>>();
	private HashMap<String, IProperty<String>> properties = new HashMap<String, IProperty<String>>();
	
	public WarlockClient () {
		streamPrefix = "client:" + hashCode() + ":";
		
		status = new CharacterStatus(this);
		
		listener = new WarlockClientListener() {
			@Override
			public void clientDisconnected(IWarlockClient client) {
				if (client == WarlockClient.this && logger != null) {
					logger.flush();
				}
			}

			@Override
			public void clientActivated(IWarlockClient client) {}

			@Override
			public void clientConnected(IWarlockClient client) {}

			@Override
			public void clientRemoved(IWarlockClient client) {}

			@Override
			public void clientSettingsLoaded(IWarlockClient client) {
				// if (getClientSettings().getLoggingSettings().getLogFormat().equals(LoggingConfiguration.LOG_FORMAT_TEXT))
					logger = new SimpleLogger(WarlockClient.this);
			}
		};
		WarlockClientRegistry.addWarlockClientListener(listener);
	}
	
	public void dispose() {
		// Disconnect if we havn't already.
		if (this.getConnection() != null && this.getConnection().isConnected())
		{
			try {
				this.getConnection().disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Flush Logger
		logger.flush();
		// Remove ourselves from the list of active clients.
		WarlockClientRegistry.removeWarlockClientListener(listener);
	}
	
	// IWarlockClient methods
	
	public ICommandHistory getCommandHistory() {
		return commandHistory;
	}
	
	public abstract void connect(String server, int port, String key) throws IOException;
	
	public void send(ICommand command) {
		getDefaultStream().sendCommand(command);
		
		try {
			String commandString = command.getCommand();
			lastCommand = commandString;
			if(connection != null)
				connection.send(commandString);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getLastCommand() {
		return lastCommand;
	}
	
	public void setViewer(IWarlockClientViewer viewer) {
		this.viewer = viewer;
	}
	
	public IWarlockClientViewer getViewer() {
		return viewer;
	}
	
	public IStream getDefaultStream() {
		return getStream(IWarlockClient.DEFAULT_STREAM_NAME);
	}
	
	public IStream getStream(String streamName) {
		synchronized(streams) {
			return streams.get(streamName);
		}
	}
	
	public Collection<IStream> getStreams() {
		return streams.values();
	}
	
	public IConnection getConnection() {
		return connection;
	}
	
	public void flushStreams() {
		synchronized(streams) {
			for(IStream stream : streams.values()) {
				stream.flush();
			}
		}
	}
	
	public void addRoomListener(IRoomListener roomListener) {
		synchronized(roomListeners) {
			roomListeners.add(roomListener);
		}
	}
	
	public void removeRoomListener(IRoomListener roomListener) {
		synchronized(roomListeners) {
			roomListeners.remove(roomListener);
		}
	}
	
	public void nextRoom() {
		synchronized(roomListeners) {
			for(IRoomListener listener : roomListeners)
				listener.nextRoom();
		}
	}
	
	public IProperty<ICompass> getCompass() {
		return compass;
	}
	
	public abstract IClientSettings getClientSettings();
	
	private void reloadHighlights() {
		cachedHighlights = new ArrayList<IWarlockHighlight>();
		cachedHighlights.addAll(HighlightConfigurationProvider.getHighlights(getClientSettings()));
		if(highlightLists != null) {
			for(Collection<IWarlockHighlight> collection : highlightLists) {
				cachedHighlights.addAll(collection);
			}
		}
	}
	
	public Collection<IWarlockHighlight> getHighlightStrings() {
		// Highlights from settings are cached. Cache is rebuilt whenever settings are changed
		if(cachedHighlights == null) {
			reloadHighlights();
			HighlightConfigurationProvider.getProvider(getClientSettings()).addListener(this);
		}
		return Collections.unmodifiableCollection(cachedHighlights);
	}
	
	public void addHighlights(Collection<IWarlockHighlight> highlights) {
		if(highlightLists == null)
			highlightLists = new ArrayList<Collection<IWarlockHighlight>>();
		
		highlightLists.add(highlights);
		reloadHighlights();
	}
	
	public boolean removeHighlights(Collection<IWarlockHighlight> highlights) {
		if(highlightLists == null)
			return false;
		
		boolean rv = highlightLists.remove(highlights);
		reloadHighlights();
		return rv;
	}
	
	public IClientLogger getLogger() {
		return logger;
	}
	
	public void playSound(InputStream stream) {
		viewer.playSound(stream);
	}
	
	public abstract IStream createStream(String streamName);
	
	public void addStreamListener(String streamName, IStreamListener listener) {
		IStream stream = streams.get(streamName);
		if(stream != null)
			stream.addStreamListener(listener);
		else
			potentialListeners.add(new Pair<String, IStreamListener>(streamName, listener));
	}
	
	public void removeStreamListener(String streamName, IStreamListener listener) {
		IStream stream = streams.get(streamName);
		if(stream != null) {
			stream.removeStreamListener(listener);
		} else {
			for(Iterator<Pair<String, IStreamListener>> iter = potentialListeners.iterator();
			iter.hasNext(); ) {
				Pair<String, IStreamListener> pair = iter.next();
				
				if(pair.first().equals(streamName) && pair.second() == listener) {
					iter.remove();
					break;
				}
			}
		}
	}
	
	public void settingChanged(IWarlockSetting setting) {
		reloadHighlights();
	}
	
	
	public String getVariable(String id) {
		IClientSettings clientSettings = getClientSettings();
		if(clientSettings == null)
			return null;
		IVariable var = VariableConfigurationProvider.getProvider(clientSettings).getVariable(id);
		if(var == null)
			return null;
		return var.getValue();
	}
	
	public void setVariable(String id, String value) {
		IClientSettings clientSettings = getClientSettings();
		if(clientSettings == null)
			return;
		VariableConfigurationProvider.getProvider(clientSettings).addVariable(id, value);
	}
	
	public void removeVariable(String id) {
		IClientSettings clientSettings = getClientSettings();
		if(clientSettings == null)
			return;
		VariableConfigurationProvider.getProvider(clientSettings).removeVariable(id);
	}
	
	public IWarlockStyle getNamedStyle(String id) {
		return PresetStyleConfigurationProvider.getProvider(getClientSettings()).getStyle(id);
	}
	
	public ICharacterStatus getCharacterStatus() {
		return status;
	}
	
	public WarlockTimer getTimer(String name) {
		WarlockTimer timer = timers.get(name);
		if(timer == null) {
			timer = new WarlockTimer();
			timers.put(name, timer);
		}
		
		return timer;
	}
	
	public void syncTime(Long time) {
		for(WarlockTimer timer : timers.values()) {
			timer.sync(time);
		}
	}
	
	public void setComponent (String componentName, String value, IStream stream) {
		String name = componentName.toLowerCase();

		components.put(name, value);
		componentStreams.put(name, stream);
	}
	
	public void updateComponent(String componentName, WarlockString value) {
		String name = componentName.toLowerCase();

		components.put(name, value.toString());
		
		IStream stream = componentStreams.get(name);
		// FIXME: The streams store them in a case-senstive fashion.
		if(stream != null)
			stream.updateComponent(componentName, value);
	}
	
	public String getComponent(String name) {
		return components.get(name.toLowerCase());
	}
	
	public Property<IWarlockDialog> getDialog(String id) {
		Property<IWarlockDialog> dialog = dialogs.get(id);
		
		if(dialog == null) {
			dialog = new Property<IWarlockDialog>();
			dialogs.put(id, dialog);
		}
		
		return dialog;
	}
	
	public IProperty<String> getProperty(String name) {
		IProperty<String> property = properties.get(name);
		if(property == null) {
			property = new Property<String>();
			properties.put(name, property);
		}
		return property;
	}
	
	public void setProperty(String name, IProperty<String> property) {
		properties.put(name, property);
	}
	
	public void setProperty(String name, String value) {
		IProperty<String> property = getProperty(name);
		property.set(value);
	}
	
	abstract public String getGameCode();
}
