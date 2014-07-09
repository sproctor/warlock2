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
package cc.warlock.core.client;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import cc.warlock.core.client.internal.WarlockDialog;
import cc.warlock.core.network.IConnection;

/**
 * @author Marshall
 * 
 * This is the main interface that will be passed around for other API functions to send data to the game,
 * notify Warlock of events, and get metadata about the current state, etc.
 * 
 * Extension writers who wish to add support for their game should start by extending this interface (see IStormFrontClient)
 */
public interface IWarlockClient extends IRoomListener {
	
	public static final String MAIN_STREAM_NAME = "main";
	
	/**
	 * Send command to the game.
	 * @param command The command to send.
	 */
	public void send(ICommand command);
	
	/**
	 * Sets a viewer for the client
	 * @param viewer The viewer to set
	 */
	public void setViewer (IWarlockClientViewer viewer);
	
	public IWarlockClientViewer getViewer();
	
	/**
	 * @param streamName The stream name
	 * @return The stream associated with the given name. If this stream does not exist, NULL will be returned.
	 */
	public IStream getStream(String streamName);
	
	/**
	 * Get the connection associated with this client.
	 * @@WARNING@@ 
	 * Do not use the raw connection unless you know what you are doing!
	 * @return
	 */
	public IConnection getConnection();
	
	public void flushStreams();
	
	/**
	 * Listen when the client has received a "nextRoom" event
	 * @param roomListener 
	 */
	public void addRoomListener(IRoomListener roomListener);
	
	/**
	 * Remove a room listener
	 * @param roomListener
	 */
	public void removeRoomListener(IRoomListener roomListener);
	
	/**
	 * @return The client's compass.
	 */
	public IProperty<ICompass> getCompass();
	
	/**
	 * @return The character status
	 */
	public ICharacterStatus getCharacterStatus();
	
	/**
	 * see IClientSettings 
	 * @return the settings for this client 
	 */
	public IClientSettings getClientSettings();
	
	public Iterator<IWarlockPattern> getHighlightsIterator();
	public void addHighlights(Collection<IWarlockPattern> highlights);
	public boolean removeHighlights(Collection<IWarlockPattern> highlights);
	
	/**
	 * @return A unique string identifying this client (this string should be a constant that can be restored from at a later time)
	 */
	public String getClientId();
	
	/**
	 * @return The character's name (display name for character)
	 */
	public String getCharacterName ();
	
	/**
	 * Set the character's name (this is used in various places)
	 */
	public void setCharacterName (String name);
	
	/**
	 * @return The last command sent
	 */
	public String getLastCommand();
	
	/**
	 * @return This client's logger (for use by streams etc)
	 */
	//public IClientLogger getLogger();
	
	public void playSound(InputStream stream);

	/**
	 * @return The collection of current streams output by the client.
	 */
	public Collection<IStream> getStreams();
	
	/**
	 * @return the stream associated with streamName
	 */
	public IStream createStream(String streamName);
	
	/**
	 * Adds a StreamListener to the stream associated with streamName immediately
	 *   if it exists, or delays until streamName is created
	 * @param streamName
	 * @param listener
	 */
	public void addStreamListener(String streamName, IStreamListener listener);
	
	public void removeStreamListener(String streamName, IStreamListener listener);
	
	/**
	 * @return The gameCode of the current player
	 */
	public String getGameCode();
	
	public void setGameCode(String gameCode);
	
	/**
	 * @return The player ID of the current player
	 */
	public String getPlayerId();
	
	public void setPlayerId(String playerId);
	
	/**
	 * @param name
	 * @return The named timer
	 */
	public WarlockTimer getTimer(String name);
	
	/**
	 * Sync the current time as perceived by the server.
	 * @param now Time the server thinks it is.
	 */
	public void syncTime(Long time);
	
	/**
	 * @param componentName
	 * @return The component with the passed in name
	 */
	public String getComponent(String componentName);
	
	public void setComponent (String componentName, String value, String stream);
	
	public void updateComponent(String name, WarlockString value);
	
	/**
	 * @return The associated SFDialog
	 */
	public WarlockDialog getDialog(String id);
	
	public IProperty<String> getProperty(String name);
	
	public void setProperty(String name, IProperty<String> property);
	
	public void setProperty(String name, String property);
	
	public void dispose();
	
	public void put(WarlockString text);
	
	public void put(String streamName, WarlockString text);
	
	public void echo(String text);
	
	public void echo(String text, IWarlockStyle style);
	
	public void echo(String streamName, String text);
	
	public void echo(String streamName, String text, IWarlockStyle style);
	
	public void prompt(String prompt);
	
	public void clear(String streamName);
	
	public String getStreamTitle(String streamName);
	
	public WarlockString getStreamHistory(String streamName);
	
	public void launchURL(String url);
	
	public void appendImage(String url);
}
