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
 * Created on Jan 13, 2005
 */
package cc.warlock.rcp.ui.network;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.network.IConnection;
import cc.warlock.core.network.IConnectionListener;
import cc.warlock.core.network.IConnection.ErrorType;

/**
 * @author Marshall
 */
public class SWTConnectionListenerAdapter implements IConnectionListener {

	private IConnectionListener listener;
	
	public SWTConnectionListenerAdapter (IConnectionListener listener)
	{
		this.listener = listener;
	}
	
	private class ConnectedRunnable implements Runnable
	{
		public IConnection connection;
		
		public void run () {
			listener.connected(connection);
		}
	}
	
	public void connected(IConnection connection) {
		ConnectedRunnable connectedRunnable = new ConnectedRunnable();
		connectedRunnable.connection = connection;
		Display.getDefault().asyncExec(connectedRunnable);
	}
	
	private class DataReadyRunnable implements Runnable
	{
		private IConnection connection;
		private String data;
		
		public DataReadyRunnable(IConnection connection, String data) {
			this.connection = connection;
			this.data = data;
		}
		public void run () {
			listener.dataReady(connection, data);
		}
	}
	
	private class DataSentRunnable implements Runnable
	{
		private IConnection connection;
		private String data;
		
		public DataSentRunnable(IConnection connection, String data) {
			this.connection = connection;
			this.data = data;
		}
		public void run () {
			listener.dataSent(connection, data);
		}
	}
	
	public void dataReady(IConnection connection, String data) {
		Display.getDefault().asyncExec(new DataReadyRunnable(connection, data));
	}
	
	public void dataSent(IConnection connection, String data) {
		Display.getDefault().asyncExec(new DataSentRunnable(connection, data));
	}

	private class DisconnectedRunnable implements Runnable
	{
		public IConnection connection;
		
		public void run () {
			listener.disconnected(connection);
		}
	}
	
	public void disconnected(IConnection connection) {
		DisconnectedRunnable disconnectedRunnable = new DisconnectedRunnable();
		disconnectedRunnable.connection = connection;
		Display.getDefault().asyncExec(disconnectedRunnable);
	}

	private class ConnectionRefusedRunnable implements Runnable
	{
		public IConnection connection;
		public ErrorType errorType;
		
		public void run () {
			listener.connectionError(connection, errorType);
		}
	}
	
	public void connectionError(IConnection connection, ErrorType errorType) {
		ConnectionRefusedRunnable runnable = new ConnectionRefusedRunnable();
		runnable.connection = connection;
		runnable.errorType = errorType;
		Display.getDefault().asyncExec(runnable);
	}
}
