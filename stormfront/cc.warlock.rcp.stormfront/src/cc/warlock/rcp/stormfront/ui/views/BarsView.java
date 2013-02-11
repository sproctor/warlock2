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

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockDialogListener;
import cc.warlock.rcp.stormfront.ui.StormFrontDialogControl;
import cc.warlock.rcp.ui.client.SWTWarlockDialogListener;
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
	
	protected static BarsView instance;
	
	protected StormFrontDialogControl minivitals;
	
	protected HashMap<IWarlockClient, SWTWarlockDialogListener> mvListeners =
		new HashMap<IWarlockClient, SWTWarlockDialogListener>();

	protected IWarlockClient activeClient;
	protected ArrayList<IWarlockClient> clients = new ArrayList<IWarlockClient>();
	
	public BarsView() {
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
		minivitals.setDialog(client.getDialog("minivitals"));
		
		if (!clients.contains(client)) {
			SWTWarlockDialogListener mvListener = new SWTWarlockDialogListener(
						new MinivitalsListener(minivitals, client));
			client.getDialog("minivitals").addListener(mvListener);
			mvListeners.put(client, mvListener);
			
			clients.add(client);
			
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite top = new Composite (parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		top.setLayout(layout);
		top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		minivitals = new StormFrontDialogControl(top, SWT.NONE);
		minivitals.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
	}
	
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
			if(activeClient == client)
				control.redraw();
		}
		
	}
	
	public static BarsView getDefault ()
	{
		return instance;
	}
}
