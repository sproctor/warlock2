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
package cc.warlock.core.network;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

import cc.warlock.core.client.IWarlockClient;

/**
 * @author marshall
 */
public abstract class Connection implements IConnection {

	private Socket socket;
	private ArrayList<IConnectionListener> connectionListeners = new ArrayList<IConnectionListener>();
	private boolean connected = false;
	
	private String host = null;
	private int port = -1;
	
	public void connect (String host, int port)
		throws IOException
	{
		try {
			socket = new Socket(host, port);
			
			new Thread(createPollingRunnable(socket)).start();
		} catch (ConnectException e) {
			if (e.getMessage().contains("refused")) {
				connectionError(ErrorType.ConnectionRefused);
			}
		} catch(UnknownHostException e) {
			connectionError(ErrorType.UnknownHost);
		}
	}
	
	protected Runnable createPollingRunnable (Socket socket) throws IOException {
		return new EventPollThread(socket);
	}
	
	protected void connectionError (ErrorType errorType)
	{
		for (IConnectionListener listener : connectionListeners)
		{
			listener.connectionError(this, errorType);
		}
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void disconnect()
		throws IOException
	{
		socket.shutdownOutput();
	}
	
	public void addConnectionListener (IConnectionListener listener)
	{
		connectionListeners.add(listener);
	}
	
	public void removeConnectionListener (IConnectionListener listener) {
		connectionListeners.remove(listener);
	}
	
	public void send (String toSend)
		throws IOException
	{
		socket.getOutputStream().write(toSend.getBytes());
		listenersDataSent(toSend);
	}
	
	public void send (byte[] bytes)
		throws IOException
	{
		socket.getOutputStream().write(bytes);
		listenersDataSent(bytes.toString());
	}
	
	public void sendLine (String line)
		throws IOException
	{
		send (line + "\n");
	}
	
	public IWarlockClient getClient() {
		return null;
	}
	
	private void listenersDataSent (String data) {
		for (IConnectionListener listener : connectionListeners) {
			listener.dataSent(this, data);
		}
	}
	
	protected class EventPollThread implements Runnable
	{
		protected Reader reader;
		
		public EventPollThread(Socket socket) throws IOException {
			reader = createReader(socket);
		}
		
		public void run ()
		{
			while (true)
			{
				if (!connected) {
					if (isSocketActive()) {
						connected = true;
						listenersConnected();
					}
				} else {
					if (!isSocketActive()) {
						connected = false;
						listenersDisconnected();
						break;
					} else {
						try {
							readData(reader);
						} catch(Exception e) {
							e.printStackTrace();
							break;
						}
					}
				}
			}
		}
		
		protected Reader createReader (Socket socket)
		throws IOException
		{
			return new InputStreamReader(socket.getInputStream());
		}

		protected void readData (Reader reader) throws IOException {
			char cbuf[] = new char[1024];

			int charsRead = reader.read(cbuf);
			listenersGotData(new String(cbuf, 0, charsRead));
		}
		
		private void listenersDisconnected ()
		{
			for (IConnectionListener listener : connectionListeners) {
				listener.disconnected(Connection.this);
			}
		}
		
		private void listenersConnected ()
		{
			for (IConnectionListener listener : connectionListeners) {
				listener.connected(Connection.this);
			}
		}
		
		private void listenersGotData (String data)
		{
			for (IConnectionListener listener : connectionListeners) {
				listener.dataReady(Connection.this, data);
			}
		}
		
		private boolean isSocketActive() {
			if (!socket.isConnected())
				return false;
			if (socket.isClosed())
				return false;
			if (socket.isInputShutdown())
				return false;
			if (socket.isOutputShutdown())
				return false;
			
			return true; // If we reach here... we're most likely still connected
		}
	}
	
	protected Collection<IConnectionListener> getConnectionListeners() {
		return connectionListeners;
	}
}