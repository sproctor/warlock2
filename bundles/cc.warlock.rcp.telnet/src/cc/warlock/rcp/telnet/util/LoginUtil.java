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
package cc.warlock.rcp.telnet.util;

import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.internal.WarlockClient;
import cc.warlock.core.client.internal.WarlockMonospace;
import cc.warlock.core.network.Connection;
import cc.warlock.core.network.IConnection;
import cc.warlock.core.network.IConnection.ErrorType;
import cc.warlock.core.network.IConnectionListener;
import cc.warlock.rcp.application.WarlockPerspectiveFactory;
import cc.warlock.rcp.telnet.ui.views.TelnetGameView;
import cc.warlock.rcp.util.RCPUtil;
import cc.warlock.rcp.views.GameView;

/**
 * @author Will Robertson
 *
 */
public class LoginUtil {

	public static void connect (GameView gameView, String gameHost, String gamePort)
	{
		String server = gameHost;
		int port = Integer.parseInt (gamePort);

		final IWarlockClient client = new WarlockClient(); //Warlock2Plugin.getDefault().getCurrentClient();
		gameView.setClient(client);
		
		try {
			System.out.println("Connecting!");
			Connection connection = new Connection();
			connection.addConnectionListener(new IConnectionListener () {
				public void connected(IConnection connection) {
					WarlockClientRegistry.clientConnected(client);
				}
				public void connectionError(IConnection connection,
						ErrorType errorType) {
				}
				public void dataReady(IConnection connection, String data) {
					// Push the raw input into the Stream
					WarlockString string = new WarlockString(data);
					string.addStyle(WarlockMonospace.getInstance());
					getMainStream().put(string);
				}
				public void dataSent (IConnection connection, String data) {
					
				}
				public void disconnected(IConnection connection) {
					WarlockClientRegistry.clientDisconnected(client);
				}
			});
			connection.connect(server, port);
			gameView.setFocus();
		} catch (IOException e) {
			String errorConnectMessage =
			"******************************************************************\n" +
			"* The connection was refused, possibly meaning the server is currently down,\n" +
			"* or your internet connection is not active\n" +
			"******************************************************************\n";
			
			client.getMainStream().put(new WarlockString(errorConnectMessage, WarlockMonospace.getInstance()));
		}
	}
	
	public static void connectAndOpenGameView (String gameHost, String gamePort, String characterName)
	{
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (!page.getPerspective().getId().equals(WarlockPerspectiveFactory.WARLOCK_PERSPECTIVE_ID))
		{
			RCPUtil.openPerspective(WarlockPerspectiveFactory.WARLOCK_PERSPECTIVE_ID);
		}
		
		TelnetGameView firstEmptyView = null;
		for (GameView view : GameView.getOpenGameViews()) {
			if (!(view instanceof TelnetGameView))
				continue;

			if (view.getClient() == null
					|| view.getClient().getConnection() == null
					|| !view.getClient().getConnection().isConnected()) {
				firstEmptyView = (TelnetGameView) view;
				System.out.println("Found inactive TelnetGameView");
				break;
			}
		}
		
		if (firstEmptyView != null)
		{
			// reuse the existing view if it's already created
			
			GameView.initializeGameView(firstEmptyView);
			connect(firstEmptyView, gameHost, gamePort);
		}
		else 
		{
			connect((TelnetGameView) TelnetGameView.createNext(TelnetGameView.VIEW_ID, characterName), gameHost, gamePort);
		}
	}
	
	public static void showConnectionError (ErrorType errorType)
	{
		switch (errorType) {
			case ConnectionRefused: {
				MessageDialog.openError(Display.getDefault().getActiveShell(), "Connection Refused", 
					"The connection was refused. This could be caused by a server outage, or a firewall blocking access.");
			} break;
			case UnknownHost: {
				MessageDialog.openError(Display.getDefault().getActiveShell(), "Unable to resolve hostname", 
					"There was an error connecting: The hostname was returned as unknown by your DNS.");
			} break;
			default: break;
		}
	}
}
