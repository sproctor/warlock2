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
package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientConnectListener;

public class SWTWarlockClientConnectListener implements IWarlockClientConnectListener {

	private IWarlockClientConnectListener listener;
	
	public SWTWarlockClientConnectListener (IWarlockClientConnectListener listener)
	{
		this.listener = listener;
	}
	
	private class ConnectedListener implements Runnable {
		private IWarlockClient client;
		
		public ConnectedListener(IWarlockClient client) {
			this.client = client;
		}
		
		public void run () {
			listener.clientConnected(client);
		}
	}
	
	private class DisconnectedListener implements Runnable {
		private IWarlockClient client;
		
		public DisconnectedListener(IWarlockClient client) {
			this.client = client;
		}
		
		public void run () {
			listener.clientDisconnected(client);
		}
	}
	
	private class SettingsLoadedListener implements Runnable {
		private IWarlockClient client;
		
		public SettingsLoadedListener(IWarlockClient client) {
			this.client = client;
		}
		
		public void run () {
			listener.clientSettingsLoaded(client);
		}
	}
	
	protected void run(Runnable runnable) {
		Display.getDefault().asyncExec(new CatchingRunnable(runnable));
	}

	@Override
	public void clientConnected(IWarlockClient client) {
		run(new ConnectedListener(client));
	}

	@Override
	public void clientDisconnected(IWarlockClient client) {
		run(new DisconnectedListener(client));
	}
	
	@Override
	public void clientSettingsLoaded(IWarlockClient client) {
		run(new SettingsLoadedListener(client));
	}
}
