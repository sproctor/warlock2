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
 * Created on Jan 11, 2005
 */
package cc.warlock.core.stormfront.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.internal.WarlockMonospace;
import cc.warlock.core.network.IConnection;
import cc.warlock.core.network.IConnectionListener;
import cc.warlock.core.stormfront.internal.ParseException;
import cc.warlock.core.stormfront.internal.StormFrontProtocolHandler;
import cc.warlock.core.stormfront.internal.StormFrontProtocolParser;

/**
 * @author Sean Proctor
 * @author Marshall
 *
 * The Internal Storm Front protocol handler. Not meant to be instantiated outside of Warlock.
 */
public class StormFrontConnection implements IConnection
{
	protected StormFrontProtocolHandler handler;
	private StormFrontReader reader;
	private StormFrontProtocolParser parser;
	protected IWarlockClient client;
	protected String key, host;
	protected int port;
	protected ArrayList<IConnectionListener> listeners = new ArrayList<IConnectionListener>();
	protected Socket socket;
	protected boolean connected = false;
	
	public StormFrontConnection (IWarlockClient client, String key) {
		this.client = client;
		this.key = key;
		this.handler = new StormFrontProtocolHandler(client);
	}
	
	public void connect(String host, int port)
	throws IOException {
		this.host = host;
		this.port = port;
		
		this.socket = new Socket(host, port);
		connected = true;
		new Thread(new SFParser()).start();
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void addConnectionListener(IConnectionListener listener) {
		listeners.add(listener);
	}
	
	public void removeConnectionListener(IConnectionListener listener) {
		listeners.remove(listener);
	}
	
	public void disconnect() throws IOException {
		sendLine("quit");
	}
	
	public void send(byte[] bytes) throws IOException {
		socket.getOutputStream().write(bytes);
		for (IConnectionListener listener : listeners)
		{
			listener.dataSent(this, bytes.toString());
		}
	}
	
	public void send(String toSend) throws IOException {
		socket.getOutputStream().write(toSend.getBytes());
		for (IConnectionListener listener : listeners)
		{
			listener.dataSent(this, toSend);
		}
	}
	
	public void sendLine (String line)
	throws IOException {
		send ("<c>" + line + "\n");
	}
	
	public IWarlockClient getClient() {
		return client;
	}
	
	public void dataReady (String line)
	{
		for (IConnectionListener listener : listeners)
		{
			listener.dataReady(this, line);
		}
	}
	
	public void passThrough() {
		parser.passThrough();
	}
	
	protected void disconnected ()
	{	
		String message = 
			"******************************\n"+
			"* Disconnected from the game *\n" +
			"******************************\n";
		
		client.echo(message, WarlockMonospace.getInstance());
		connected = false;
		
		for (IConnectionListener listener : listeners)
		{
			listener.disconnected(this);
		}
		
		WarlockClientRegistry.clientDisconnected(client);
	}
	
	class SFParser implements Runnable {
		public void run() {
			try {
				sendLine(key);
				sendLine("/FE:STORMFRONT /VERSION:1.0.1.22 /XML");
				
				reader = new StormFrontReader(StormFrontConnection.this, socket.getInputStream());
				parser = new StormFrontProtocolParser(reader);
				parser.setHandler(handler);
				
				while(socket.isConnected()) {
					try {
						parser.Document();
						break;
					} catch (ParseException e) {
						e.printStackTrace();
						client.echo("\n*** Parse error ***\n");
						parser.ReInit(reader);
					}
				}
				
				disconnected();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getKey() {
		return key;
	}
	
	public StormFrontProtocolHandler getProtocolHandler() {
		return handler;
	}
}
