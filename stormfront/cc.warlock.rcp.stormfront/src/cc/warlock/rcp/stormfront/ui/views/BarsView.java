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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewerListener;
import cc.warlock.rcp.stormfront.ui.StormFrontDialogControl;
import cc.warlock.rcp.stormfront.ui.StormFrontStatus;
import cc.warlock.rcp.ui.WarlockCompass;
import cc.warlock.rcp.ui.client.SWTWarlockClientViewerListener;
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
	private HashMap<GameView, Composite> pages = new HashMap<GameView, Composite>();
	private CompassTheme theme = CompassThemes.getCompassTheme("small");
	private GameView activeGame;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		container.setLayout(layout);
		
		book = new PageBook(container, SWT.NONE);
		book.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		setGame(GameView.getGameViewInFocus());
		
		GameView.addGameViewFocusListener(new IGameViewFocusListener () {
			public void gameViewFocused(GameView gameView) {
				setGame(gameView);
			}
		});
	}
	
	protected void setGame(GameView game) {
		if(game == null || activeGame == game)
			return;
		
		Composite page = pages.get(game);
		if(page == null) {
			page = new BarsPageView(book, game);
			pages.put(game, page);
		}
		book.showPage(page);
	}
	
	private class BarsPageView extends Composite {
		
		StormFrontEntry entry;
		StormFrontStatus status;
		WarlockCompass compass;
		StormFrontDialogControl minivitals;
		
		public BarsPageView(Composite parent, GameView view) {
			super(parent, SWT.NONE);
			
			GridLayout layout = new GridLayout(3, false);
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.horizontalSpacing = 0;
			layout.verticalSpacing = 0;
			this.setLayout(layout);
			
			entry = new StormFrontEntry(this, view);
			GridData entryData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			entryData.heightHint = 22;
			entry.getWidget().setLayoutData(entryData);
			
			status = new StormFrontStatus(this, view);
			status.setLayoutData(new GridData(SWT.CENTER, SWT.BEGINNING, false, false));
			
			compass = new WarlockCompass(this, SWT.NONE, theme);
			compass.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 2));
			
			minivitals = new StormFrontDialogControl(this, SWT.NONE);
			minivitals.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
			
			view.setEntry(entry);
			setClient(view.getClient());
			view.addClientViewerListener(new SWTWarlockClientViewerListener(new IWarlockClientViewerListener() {
				@Override
				public void clientChanged(IWarlockClient client) {
					setClient(client);
				}
			}));
		}
		
		public void setClient(IWarlockClient client) {
			if(client != null) {
				minivitals.setDialog(client.getDialog("minivitals"));
				compass.setClient(client);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		// Don't need to do anything
	}
	
}
