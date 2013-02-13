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
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.stormfront.ui.views;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;
import cc.warlock.core.client.IWarlockDialogListener;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.rcp.stormfront.ui.StormFrontDialogControl;
import cc.warlock.rcp.stormfront.ui.StormFrontStatus;
import cc.warlock.rcp.ui.WarlockCompass;
import cc.warlock.rcp.ui.client.SWTWarlockClientListener;
import cc.warlock.rcp.ui.client.SWTWarlockDialogListener;
import cc.warlock.rcp.ui.style.CompassTheme;
import cc.warlock.rcp.ui.style.CompassThemes;
import cc.warlock.rcp.views.GameView;
import cc.warlock.rcp.views.IGameViewFocusListener;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BarsView extends ViewPart {

	public static final String VIEW_ID = "cc.warlock.rcp.stormfront.ui.views.BarsView";
	
	private PageBook book;
	//private StormFrontStatus status;
	//private WarlockCompass compass;
	//private StormFrontDialogControl minivitals;
	//private HashMap<GameView, SWTWarlockDialogListener> mvListeners =
		//new HashMap<GameView, SWTWarlockDialogListener>();
	private GameView activeView;
	private HashMap<GameView, Composite> pages = new HashMap<GameView, Composite>();
	private CompassTheme theme = CompassThemes.getCompassTheme("small");
	private Composite parent;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		
		book = new PageBook(parent, SWT.NONE);
		//Composite top = new Composite (parent, SWT.NONE);
		//book.setLayout(new GridLayout(1, false));
		book.setLayout(new FillLayout());
		
		// Create page book
		/*book = new PageBook(top, SWT.NONE);
		book.setLayout(new FillLayout());
		GridData bookData = new GridData(SWT.FILL, SWT.FILL, true, true);
		bookData.heightHint = 25;
		book.setLayoutData(bookData);
		
		status = new StormFrontStatus(top);
		status.getWidget().setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 2));
		
		compass = new Warlock
		minivitals = new StormFrontDialogControl(top, SWT.NONE);
		GridData mvData = new GridData(SWT.FILL, SWT.END, true, false);
		mvData.heightHint = 20;
		minivitals.setLayoutData(mvData);*/

		/*for (IWarlockClient client : WarlockClientRegistry.getActiveClients()) {
			addClient(client);
		}*/
		
		/*WarlockClientRegistry.addWarlockClientListener(new SWTWarlockClientListener(new IWarlockClientListener() {
			@Override
			public void clientCreated(IWarlockClient client) {}
			@Override
			public void clientConnected(IWarlockClient client) {
				addClient(client);
				if(client == activeView.getClient())
					minivitals.setDialog(client.getDialog("minivitals"));
			}
			@Override
			public void clientDisconnected(IWarlockClient client) {}
			@Override
			public void clientSettingsLoaded(IWarlockClient client) {}
		}));*/
		
		setView(GameView.getGameViewInFocus());
		
		GameView.addGameViewFocusListener(new IGameViewFocusListener () {
			public void gameViewFocused(GameView gameView) {
				setView(gameView);
			}
		});
	}
	
	protected void setView(GameView view) {
		if(activeView == view)
			return;
		activeView = view;
		IWarlockClient client = view.getClient();
		
		Composite page = pages.get(view);
		if(page == null) {
			page = new BarsPageView(book, view);
			pages.put(view, page);
		}
		
		book.showPage(page);
	}
	
	private class BarsPageView extends Composite {
		
		GameView view;
		StormFrontEntry entry;
		StormFrontStatus status;
		WarlockCompass compass;
		StormFrontDialogControl minivitals;
		
		public BarsPageView(Composite Parent, GameView view) {
			super(parent, SWT.NONE);
			this.view = view;
			
			this.setLayout(new GridLayout(3, false));
			
			entry = new StormFrontEntry(this, view);
			//entry.getWidget().setLayout(new FillLayout());
			GridData entryData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			entryData.heightHint = 25;
			entry.getWidget().setLayoutData(entryData);
			
			status = new StormFrontStatus(this);
			status.getWidget().setLayoutData(new GridData(SWT.CENTER, SWT.BEGINNING, false, false, 1, 2));
			
			compass = new WarlockCompass(this, SWT.NONE, theme, parent);
			GridData compassData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 2);
			compass.setLayoutData(compassData);
			
			minivitals = new StormFrontDialogControl(this, SWT.NONE);
			//minivitals.setSize(300, 15);
			GridData mvData = new GridData(SWT.FILL, SWT.FILL, true, true);
			//mvData.heightHint = 20;
			minivitals.setLayoutData(mvData);
			
			view.setEntry(entry);
		}
		
		public void setClient(IWarlockClient client) {
			if(client != null) {
				minivitals.setDialog(client.getDialog("minivitals"));
				SWTWarlockDialogListener mvListener = new SWTWarlockDialogListener(
						new MinivitalsListener(minivitals, client));
				client.getDialog("minivitals").addListener(mvListener);
				compass.setClient(client);
				status.setActiveClient(client);
			}
		}
		
		
	}
	
/*	private void addClient (IWarlockClient client) {
		if(clients.contains(client))
			return;
		SWTWarlockDialogListener mvListener = new SWTWarlockDialogListener(
				new MinivitalsListener(minivitals, client));
		client.getDialog("minivitals").addListener(mvListener);
		mvListeners.put(client, mvListener);

		clients.add(client);
	}*/
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private class MinivitalsListener implements IWarlockDialogListener {
		
		private StormFrontDialogControl control;
		private IWarlockClient client;
		
		public MinivitalsListener(StormFrontDialogControl control, IWarlockClient client) {
			this.control = control;
			this.client = client;
		}
		
		public void dialogChanged() {
			if(activeView.getClient() == client)
				control.redraw();
		}
		
	}
}
