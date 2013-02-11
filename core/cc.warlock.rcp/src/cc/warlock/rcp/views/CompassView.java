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
package cc.warlock.rcp.views;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.rcp.ui.WarlockCompass;
import cc.warlock.rcp.ui.style.CompassThemes;

public class CompassView extends ViewPart {
	public static final String VIEW_ID = "cc.warlock.rcp.ui.views.CompassView";
	
	//protected static CompassView instance;
	protected IWarlockClient activeClient;
	protected HashMap<IWarlockClient, WarlockCompass> clients = new HashMap<IWarlockClient, WarlockCompass>();
	protected PageBook book;
	private IGameViewFocusListener listener = new IGameViewFocusListener () {
		public void gameViewFocused(GameView gameView) {
			setClient(gameView.getClient());
		}
	};
	
	public void setClient (IWarlockClient client) {
		activeClient = client;
		if (client == null)
			return;
		
		if(book.isDisposed())
			return;
		
		if (!clients.containsKey(client)) {
			WarlockCompass compass = new WarlockCompass(book, SWT.NONE, CompassThemes.getCompassTheme("small"), client);
			clients.put(client, compass);
			book.showPage(compass);
			compass.redraw();
		} else {
			WarlockCompass compass = clients.get(client);
			book.showPage(compass);
			compass.redraw();
		}	
	}
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		book = new PageBook(parent, SWT.NONE);
		book.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		GameView.addGameViewFocusListener(listener);
		GameView inFocus = GameView.getGameViewInFocus();
		if (inFocus != null)
			setClient(inFocus.getClient());
	}
	
	@Override
	public void dispose() {
		GameView.removeGameViewFocusListener(listener);
	}
	
	public void setFocus() {
		// Nothing to do
	}
}
