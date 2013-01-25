/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2013, Warlock LLC, and individual contributors as indicated
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
package cc.warlock.rcp.stormfront.ui.views;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.client.ICompass;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.rcp.ui.WarlockCompass;
import cc.warlock.rcp.ui.client.SWTPropertyListener;
import cc.warlock.rcp.ui.style.CompassThemes;
import cc.warlock.rcp.views.GameView;
import cc.warlock.rcp.views.IGameViewFocusListener;

public class CompassView extends ViewPart {
	public static final String VIEW_ID = "cc.warlock.rcp.stormfront.ui.views.CompassView";
	
	protected static CompassView instance;
	protected IWarlockClient activeClient;
	protected HashMap<IWarlockClient, WarlockCompass> clients = new HashMap<IWarlockClient, WarlockCompass>();
	protected PageBook book;
	
	public CompassView () {
		instance = this;
		
		GameView.addGameViewFocusListener(new IGameViewFocusListener () {
			public void gameViewFocused(GameView gameView) {
				setActiveClient(gameView.getClient());
			}
		});
	}
	
	protected void setActiveClient (IWarlockClient client)
	{
		if (client == null || activeClient == client)
			return;
		
		activeClient = client;
		
		if (!clients.containsKey(client)) {
			WarlockCompass compass = new WarlockCompass(book, SWT.NONE, CompassThemes.getCompassTheme("small"), client);
			clients.put(client, compass);
			client.getCompass().addListener(new SWTPropertyListener<ICompass>(compass));
			book.showPage(compass);
		} else {
			WarlockCompass compass = clients.get(client);
			book.showPage(compass);
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		book = new PageBook(parent, SWT.NONE);
		book.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}
	
	public void setFocus() {
		// Nothing to do
	}
}