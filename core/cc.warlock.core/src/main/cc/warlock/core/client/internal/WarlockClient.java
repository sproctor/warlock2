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
import java.util.NoSuchElementException;

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
import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.WarlockTimer;
import cc.warlock.core.client.logging.SimpleLogger;
import cc.warlock.core.client.settings.ClientSettings;
import cc.warlock.core.client.settings.HighlightConfigurationProvider;
import cc.warlock.core.network.IConnection;
import cc.warlock.core.util.Pair;


/**
 * @author Marshall
 */
public abstract class WarlockClient implements IWarlockClient {

	protected IConnection connection;
	protected IWarlockClientViewer viewer;
	protected IWarlockClientListener listener;
	private String lastCommand;
	protected String streamPrefix;
	private Collection<IRoomListener> roomListeners = Collections.synchronizedCollection(new ArrayList<IRoomListener>());
	protected Property<ICompass> compass = new Property<ICompass>(null);
	protected SimpleLogger logger;
	protected HashMap<String, IStream> streams = new HashMap<String, IStream>();
	protected final IStream mainStream;
	protected ArrayList<Pair<String, IStreamListener>> streamListeners = new ArrayList<Pair<String, IStreamListener>>();
	private ArrayList<Collection<? extends IWarlockHighlight>> highlightLists = new ArrayList<Collection<? extends IWarlockHighlight>>();
	private ICharacterStatus status;
	private HashMap<String, WarlockTimer> timers = new HashMap<String, WarlockTimer>();
	private HashMap<String, String> components = new HashMap<String, String>();
	private HashMap<String, String> componentStreams = new HashMap<String, String>();
	private HashMap<String, WarlockDialog> dialogs = new HashMap<String, WarlockDialog>();
	private HashMap<String, IProperty<String>> properties = new HashMap<String, IProperty<String>>();
	protected ClientSettings clientSettings;
	
	public WarlockClient () {
		streamPrefix = "client:" + hashCode() + ":";
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
			public void clientCreated(IWarlockClient client) {}
			@Override
			public void clientConnected(IWarlockClient client) {}
			@Override
			public void clientSettingsLoaded(IWarlockClient client) {
				highlightLists.add(HighlightConfigurationProvider.getHighlights(getClientSettings()));
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
	
	public abstract void connect(String server, int port, String key) throws IOException;
	
	public synchronized void send(ICommand command) {
		mainStream.sendCommand(command);
		
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
	
	public Iterator<? extends IWarlockHighlight> getHighlightsIterator() {
		return new MultiIterator<IWarlockHighlight>(highlightLists);
	}
	
	@Override
	public void addHighlights(Collection<IWarlockHighlight> highlights) {
		highlightLists.add(highlights);
	}
	
	public boolean removeHighlights(Collection<IWarlockHighlight> highlights) {
		return highlightLists.remove(highlights);
	}
	
	public void playSound(InputStream stream) {
		viewer.playSound(stream);
	}
	
	@Override
	public IStream createStream(String streamName) {
		synchronized(streams) {
			IStream stream = getStream(streamName);
			if(stream == null) {
				stream = new Stream(this, streamName);
				streams.put(streamName, stream);
				for(Iterator<Pair<String, IStreamListener>> iter = streamListeners.iterator();
						iter.hasNext(); ) {
					Pair<String, IStreamListener> pair = iter.next();
					if(pair.first().equals(streamName)) {
						stream.addStreamListener(pair.second());
						iter.remove();
					}
				}
				// TODO: or should this always be called?
				stream.create();
			}
			stream.addStreamListener(logger);
			return stream;
		}
	}
	
	public void addStreamListener(String streamName, IStreamListener listener) {
		IStream stream = streams.get(streamName);
		if(stream != null)
			stream.addStreamListener(listener);
		else
			streamListeners.add(new Pair<String, IStreamListener>(streamName, listener));
	}
	
	public void removeStreamListener(String streamName, IStreamListener listener) {
		IStream stream = streams.get(streamName);
		if(stream != null) {
			stream.removeStreamListener(listener);
		} else {
			for(Iterator<Pair<String, IStreamListener>> iter = streamListeners.iterator();
			iter.hasNext(); ) {
				Pair<String, IStreamListener> pair = iter.next();
				
				if(pair.first().equals(streamName) && pair.second() == listener) {
					iter.remove();
					break;
				}
			}
		}
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
	
	public void setComponent (String componentName, String value, String streamName) {
		String name = componentName.toLowerCase();

		components.put(name, value);
		componentStreams.put(name, streamName);
	}
	
	public void updateComponent(String componentName, WarlockString value) {
		String name = componentName.toLowerCase();

		components.put(name, value.toString());
		
		String streamName = componentStreams.get(name);
		IStream stream = streamName == null ? null : streams.get(streamName);
		// FIXME: The streams store them in a case-senstive fashion.
		if(stream != null)
			stream.updateComponent(componentName, value);
	}
	
	public String getComponent(String name) {
		return components.get(name.toLowerCase());
	}
	
	public WarlockDialog getDialog(String id) {
		WarlockDialog dialog = dialogs.get(id);
		
		if(dialog == null) {
			dialog = new WarlockDialog();
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
	
	public IClientSettings getClientSettings() {
		return clientSettings;
	}
	
	public synchronized void put(WarlockString text) {
		mainStream.put(text);
	}
	
	public synchronized void put(String streamName, WarlockString text) {
		IStream stream = streams.get(streamName);
		if(stream == null) {
			// panic
			throw new Error("Printing to uninstantiated stream");
		}
		stream.put(text);
		String closedTarget = stream.getClosedTarget();
		if(!viewer.isStreamOpen(streamName) && !closedTarget.equals("")) {
			String closedStyle = stream.getClosedStyle();
			WarlockString closedText = new WarlockString(text.toString(), new WarlockStyle(closedStyle));
			IStream targetStream = streams.get(closedTarget);
			targetStream.put(closedText);
		}
	}
	
	public synchronized void prompt(String prompt) {
		mainStream.prompt(prompt);
	}
	
	// TODO flush buffer before and after echo
	public void echo(String text) {
		echo(text, WarlockStyle.echoStyle);
	}
	
	public synchronized void echo(String text, IWarlockStyle style) {
		WarlockString string = new WarlockString(text);
		string.addStyle(style);

		mainStream.echo(string);
	}
	
	public void echo(String streamName, String text) {
		echo(streamName, text, WarlockStyle.echoStyle);
	}
	
	public synchronized void echo(String streamName, String text, IWarlockStyle style) {
		WarlockString string = new WarlockString(text, style);

		IStream stream = streams.get(streamName);
		if(stream == null) {
			// panic
			throw new Error("Printing to uninstantiated stream");
		}
		stream.echo(string);
		String closedTarget = stream.getClosedTarget();
		if(!viewer.isStreamOpen(streamName) && !closedTarget.equals("")) {
			String closedStyle = stream.getClosedStyle();
			WarlockString closedText = new WarlockString(text, new WarlockStyle(closedStyle));
			IStream targetStream = streams.get(closedTarget);
			targetStream.echo(closedText);
		}
	}
	
	public synchronized void clear(String streamName) {
		streams.get(streamName).clear();
	}
	
	public synchronized String getStreamTitle(String streamName) {
		IStream stream = streams.get(streamName);
		return stream == null ? "" : stream.getFullTitle();
	}
	
	public synchronized WarlockString getStreamHistory(String streamName) {
		IStream stream = streams.get(streamName);
		return stream == null ? null : stream.getHistory();
	}
}
