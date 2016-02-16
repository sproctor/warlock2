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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.tuple.Pair;

import cc.warlock.core.client.ICharacterStatus;
import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.ICompass;
import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.IWarlockPattern;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.Property;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.WarlockStyle;
import cc.warlock.core.client.WarlockTimer;
import cc.warlock.core.client.logging.SimpleLogger;
import cc.warlock.core.client.settings.ClientSettings;
import cc.warlock.core.client.settings.HighlightConfigurationProvider;
import cc.warlock.core.client.settings.MacroConfigurationProvider;
import cc.warlock.core.network.IConnection;
import cc.warlock.core.settings.MacroSetting;
import cc.warlock.core.settings.WarlockPreferencesScope;


/**
 * @author Marshall
 */
public class WarlockClient implements IWarlockClient {

	private IConnection connection;
	private IWarlockClientViewer viewer;
	private IWarlockClientListener listener;
	private String lastCommand;
	private Collection<IRoomListener> roomListeners = Collections.synchronizedCollection(new ArrayList<IRoomListener>());
	private Property<ICompass> compass = new Property<ICompass>(null);
	private SimpleLogger logger;
	private ArrayList<IStream> streams = new ArrayList<IStream>();
	private final IStream mainStream;
	private ArrayList<Entry<String, IStreamListener>> streamListeners = new ArrayList<Entry<String, IStreamListener>>();
	private ArrayList<Collection<? extends IWarlockPattern>> highlightLists = new ArrayList<Collection<? extends IWarlockPattern>>();
	private ICharacterStatus status;
	private HashMap<String, WarlockTimer> timers = new HashMap<String, WarlockTimer>();
	private HashMap<String, Pair<String, String>> components = new HashMap<String, Pair<String, String>>();
	private HashMap<String, WarlockDialog> dialogs = new HashMap<String, WarlockDialog>();
	private HashMap<String, IProperty<String>> properties = new HashMap<String, IProperty<String>>();
	private String gameCode;
	private String playerId;

	private ClientSettings clientSettings;
	
	public WarlockClient () {
		logger = new SimpleLogger(this);
		mainStream = createStream(IWarlockClient.MAIN_STREAM_NAME);
		
		status = new CharacterStatus(this);
		
		listener = new IWarlockClientListener() {
			@Override
			public void clientDisconnected(IWarlockClient client) {
				if (client == WarlockClient.this && logger != null) {
					logger.flush();
				}
			}
			@Override
			public void clientConnected(IWarlockClient client) {}
			@Override
			public void clientSettingsLoaded(IWarlockClient client) {
				highlightLists.add(HighlightConfigurationProvider.getHighlights(getClientSettings()));
			}
		};
		WarlockClientRegistry.addWarlockClientListener(listener);
	}
	
	@Override
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
	@Override
	public IConnection getConnection() {
		return connection;
	}
	
	@Override
	public void setConnection(IConnection connection) {
		this.connection = connection;
	}
	
	@Override
	public synchronized void send(ICommand command) {
		if(command.isVisible())
			mainStream.sendCommand(command);
		
		try {
			String commandString = command.getCommand();
			lastCommand = commandString;
			IConnection connection = this.getConnection();
			if(connection != null)
				connection.sendLine(commandString);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getLastCommand() {
		return lastCommand;
	}
	
	@Override
	public void setViewer(IWarlockClientViewer viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public IWarlockClientViewer getViewer() {
		return viewer;
	}
	
	@Override
	public IStream getStream(String streamName) {
		synchronized(streams) {
			for (IStream stream : streams) {
				if (streamName.equals(stream.getName()))
					return stream;
			}
			return null;
		}
	}
	
	@Override
	public Collection<IStream> getStreams() {
		return streams;
	}
	
	@Override
	public void addRoomListener(IRoomListener roomListener) {
		synchronized(roomListeners) {
			roomListeners.add(roomListener);
		}
	}
	
	@Override
	public void removeRoomListener(IRoomListener roomListener) {
		synchronized(roomListeners) {
			roomListeners.remove(roomListener);
		}
	}
	
	@Override
	public void nextRoom() {
		synchronized(roomListeners) {
			for(IRoomListener listener : roomListeners)
				listener.nextRoom();
		}
	}
	
	@Override
	public IProperty<ICompass> getCompass() {
		return compass;
	}
	
	public class MultiIterator<T> implements Iterator<T> {
		private Iterator<? extends Collection<? extends T>> it;
	    private Iterator<? extends T> innerIt;
	    private T next;
	    private boolean hasNext = true;
	    
		public MultiIterator(Collection<? extends Collection<? extends T>> collections) {
	        it = collections.iterator();    
	        prepareNext();
	    }

	    private void prepareNext() {
	        do {
	            if (innerIt == null || !innerIt.hasNext()) {
	                if (!it.hasNext()) {
	                    hasNext = false;
	                    return;
	                } else
	                    innerIt = it.next().iterator();
	            }
	        } while (!innerIt.hasNext());

	        next = innerIt.next();
	    }

	    @Override
	    public boolean hasNext() {
	        return hasNext;
	    }

	    @Override
	    public T next() {
	        if (!hasNext)
	            throw new NoSuchElementException();
	        T res = next;
	        prepareNext();
	        return res;
	    }

	    @Override
	    public void remove() {
	        // Unused
	    }
	}
	
	@Override
	public Iterator<IWarlockPattern> getHighlightsIterator() {
		return new MultiIterator<IWarlockPattern>(highlightLists);
	}
	
	@Override
	public void addHighlights(Collection<IWarlockPattern> highlights) {
		highlightLists.add(highlights);
	}
	
	@Override
	public boolean removeHighlights(Collection<IWarlockPattern> highlights) {
		return highlightLists.remove(highlights);
	}
	
	@Override
	public void playSound(InputStream stream) {
		viewer.playSound(stream);
	}
	
	@Override
	public IStream createStream(String streamName) {
		synchronized(streams) {
			IStream stream = getStream(streamName);
			if(stream == null) {
				stream = new Stream(this, streamName);
				streams.add(stream);
				for(Iterator<Entry<String, IStreamListener>> iter = streamListeners.iterator();
						iter.hasNext(); ) {
					Entry<String, IStreamListener> pair = iter.next();
					if(pair.getKey().equals(streamName)) {
						stream.addStreamListener(pair.getValue());
						iter.remove();
					}
				}
				//stream.create();
				stream.addStreamListener(logger);
			}
			return stream;
		}
	}
	
	@Override
	public void addStreamListener(String streamName, IStreamListener listener) {
		IStream stream = getStream(streamName);
		if(stream != null) {
			stream.addStreamListener(listener);
		} else {
			if(streamName == null) {
				// WTF?
				return;
			} else {
				streamListeners.add(new SimpleEntry<String, IStreamListener>(streamName, listener));
			}
		}
	}
	
	@Override
	public void removeStreamListener(String streamName, IStreamListener listener) {
		IStream stream = getStream(streamName);
		if(stream != null) {
			stream.removeStreamListener(listener);
		} else {
			for(Iterator<Entry<String, IStreamListener>> iter = streamListeners.iterator();
			iter.hasNext(); ) {
				Entry<String, IStreamListener> pair = iter.next();
				
				if(pair.getKey().equals(streamName) && pair.getValue() == listener) {
					iter.remove();
					break;
				}
			}
		}
	}
	
	@Override
	public ICharacterStatus getCharacterStatus() {
		return status;
	}
	
	@Override
	public WarlockTimer getTimer(String name) {
		WarlockTimer timer = timers.get(name);
		if(timer == null) {
			timer = new WarlockTimer();
			timers.put(name, timer);
		}
		
		return timer;
	}
	
	@Override
	public void syncTime(Long time) {
		for(WarlockTimer timer : timers.values()) {
			timer.sync(time);
		}
	}
	
	@Override
	public void setComponent (String componentName, String value, String streamName) {
		String name = componentName.toLowerCase();

		components.put(name, Pair.of(value, streamName));
	}
	
	@Override
	public void updateComponent(String componentName, WarlockString value) {
		String name = componentName.toLowerCase();

		Pair<String, String> oldPair = components.get(name);
		String streamName = oldPair != null ? oldPair.getRight() : null;
		components.put(name, Pair.of(value.toString(), streamName));
		
		IStream stream = streamName != null ? getStream(streamName) : null;
		// FIXME: The streams store them in a case-senstive fashion.
		if(stream != null)
			stream.updateComponent(componentName, value);
	}
	
	@Override
	public String getComponent(String name) {
		Pair<String, String> pair = components.get(name.toLowerCase());
		if (pair != null)
			return pair.getLeft();
		return null;
	}
	
	@Override
	public WarlockDialog getDialog(String id) {
		WarlockDialog dialog = dialogs.get(id);
		
		if(dialog == null) {
			dialog = new WarlockDialog();
			dialogs.put(id, dialog);
		}
		
		return dialog;
	}
	
	@Override
	public IProperty<String> getProperty(String name) {
		IProperty<String> property = properties.get(name);
		if(property == null) {
			property = new Property<String>();
			properties.put(name, property);
		}
		return property;
	}
	
	@Override
	public void setProperty(String name, IProperty<String> property) {
		properties.put(name, property);
	}
	
	@Override
	public void setProperty(String name, String value) {
		IProperty<String> property = getProperty(name);
		property.set(value);
	}
	
	
	@Override
	public String getGameCode() {
		return gameCode;
	}
	
	@Override
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
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
	public String getClientId() {
		return getGameCode() + getPlayerId();
	}
	
	@Override
	public IClientSettings getClientSettings() {
		return clientSettings;
	}
	
	@Override
	public synchronized void put(WarlockString text) {
		mainStream.put(text);
	}
	
	@Override
	public synchronized void put(String streamName, WarlockString text) {
		IStream stream = getStream(streamName);
		if(stream == null) {
			// panic
			throw new Error("Printing to uninstantiated stream");
		}
		stream.put(text);
		String closedTarget = stream.getClosedTarget();
		if(!viewer.isStreamOpen(streamName) && !closedTarget.equals("")) {
			String closedStyle = stream.getClosedStyle();
			WarlockString closedText = new WarlockString(text.toString(), new WarlockStyle(closedStyle));
			IStream targetStream = getStream(closedTarget);
			targetStream.put(closedText);
		}
	}
	
	@Override
	public synchronized void prompt(String prompt) {
		mainStream.prompt(prompt);
	}
	
	// TODO flush buffer before and after echo
	@Override
	public void echo(String text) {
		echo(text, WarlockStyle.echoStyle);
	}
	
	@Override
	public synchronized void echo(String text, IWarlockStyle style) {
		WarlockString string = new WarlockString(text);
		string.addStyle(style);

		mainStream.echo(string);
	}
	
	@Override
	public void echo(String streamName, String text) {
		echo(streamName, text, WarlockStyle.echoStyle);
	}
	
	@Override
	public synchronized void echo(String streamName, String text, IWarlockStyle style) {
		WarlockString string = new WarlockString(text, style);

		IStream stream = getStream(streamName);
		if(stream == null) {
			// panic
			throw new Error("Printing to uninstantiated stream");
		}
		stream.echo(string);
		String closedTarget = stream.getClosedTarget();
		if(!viewer.isStreamOpen(streamName) && !closedTarget.equals("")) {
			String closedStyle = stream.getClosedStyle();
			WarlockString closedText = new WarlockString(text, new WarlockStyle(closedStyle));
			IStream targetStream = getStream(closedTarget);
			targetStream.echo(closedText);
		}
	}
	
	@Override
	public synchronized void clear(String streamName) {
		getStream(streamName).clear();
	}
	
	@Override
	public void launchURL(String url) {
		try {
			getViewer().launchURL(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void appendImage(String urlString) {
		try {
			URL url = new URL(urlString);
			getViewer().appendImage(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
