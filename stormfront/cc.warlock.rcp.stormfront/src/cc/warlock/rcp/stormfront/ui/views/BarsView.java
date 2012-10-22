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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockDialog;
import cc.warlock.rcp.stormfront.ui.StormFrontDialogControl;
import cc.warlock.rcp.ui.WarlockProgressBar;
import cc.warlock.rcp.ui.client.SWTPropertyListener;
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
	protected Color roundtimeFG, roundtimeBG, roundtimeBorder,
		casttimeFG, casttimeBG, casttimeBorder;
	
	protected Composite rtBarWOCT, rtBarWCT = null;
	protected PageBook rtPageBook = null;
	
	protected WarlockProgressBar roundtime, roundtime2, casttime;
	protected StormFrontDialogControl minivitals;
	
	protected HashMap<IWarlockClient, SWTPropertyListener<Integer>> rtListeners =
		new HashMap<IWarlockClient, SWTPropertyListener<Integer>>();
	protected HashMap<IWarlockClient, SWTPropertyListener<Integer>> ctListeners =
		new HashMap<IWarlockClient, SWTPropertyListener<Integer>>();
	protected HashMap<IWarlockClient, SWTPropertyListener<IWarlockDialog>> mvListeners =
		new HashMap<IWarlockClient, SWTPropertyListener<IWarlockDialog>>();

	protected IWarlockClient activeClient;
	protected ArrayList<IWarlockClient> clients = new ArrayList<IWarlockClient>();
	
	public BarsView() {
		instance = this;
		
		GameView.addGameViewFocusListener(new IGameViewFocusListener () {
			public void gameViewFocused(GameView gameView) {
				if (gameView instanceof StormFrontGameView)
				{
					BarsView.this.gameViewFocused((StormFrontGameView)gameView);
				}
			}
		});
	}

	protected void setActiveClient (IWarlockClient client)
	{
		if (client == null) return;
		
		this.activeClient = client;
		
		if (!clients.contains(client))
		{
			SWTPropertyListener<Integer> rtListener =
				new SWTPropertyListener<Integer>(new RoundtimeListener(client));
			client.getTimer("roundtime").getProperty().addListener(rtListener);
			rtListeners.put(client, rtListener);
			
			SWTPropertyListener<Integer> ctListener =
				new SWTPropertyListener<Integer>(new CasttimeListener(client));
			client.getTimer("casttime").getProperty().addListener(ctListener);
			ctListeners.put(client, ctListener);
			
			SWTPropertyListener<IWarlockDialog> mvListener =
				new SWTPropertyListener<IWarlockDialog>(
						new MinivitalsListener(minivitals, client));
			client.getDialog("minivitals").addListener(mvListener);
			mvListeners.put(client, mvListener);
			
			clients.add(client);
			
		} else {
			rtListeners.get(client).propertyChanged(client.getTimer("roundtime").getValue());
			ctListeners.get(client).propertyChanged(client.getTimer("casttime").getValue());
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
		
		/* Initialize the rtPageBook so it comes first */
		rtPageBook = new PageBook(top, SWT.NONE);
		rtPageBook.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		minivitals = new StormFrontDialogControl(top, SWT.NONE);
		minivitals.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		
		initBarColors();
		
		rtBarWOCT = new Composite(rtPageBook, SWT.NONE);
		layout.marginWidth = layout.marginHeight = layout.horizontalSpacing = 0;
		rtBarWOCT.setLayout(layout);
		rtBarWOCT.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		rtBarWCT = new Composite(rtPageBook, SWT.NONE);
		layout.marginWidth = layout.marginHeight = layout.horizontalSpacing = 0;
		rtBarWCT.setLayout(layout);
		rtBarWCT.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		roundtime = new WarlockProgressBar(rtBarWOCT, SWT.NONE);
		roundtime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));
		roundtime.setMinimum(0); roundtime.setMaximum(0); roundtime.setLabel("roundtime: 0");
		roundtime.setBackground(roundtimeBG); roundtime.setForeground(roundtimeFG); roundtime.setBorderColor(roundtimeBorder);
//		roundtime.setSize(300, 5); //roundtime.setShowText(false);
		
		roundtime2 = new WarlockProgressBar(rtBarWCT, SWT.NONE);
		roundtime2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));
		roundtime2.setMinimum(0); roundtime2.setMaximum(0); roundtime2.setLabel("roundtime: 0");
		roundtime2.setBackground(roundtimeBG); roundtime2.setForeground(roundtimeFG); roundtime2.setBorderColor(roundtimeBorder);
//		roundtime2.setSize(300, 5); //roundtime2.setShowText(false);
		
		casttime = new WarlockProgressBar(rtBarWCT, SWT.NONE);
		casttime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));
		casttime.setMinimum(0); casttime.setMaximum(0); casttime.setLabel("casttime: 0");
		casttime.setBackground(casttimeBG); casttime.setForeground(casttimeFG); casttime.setBorderColor(casttimeBorder);
		
		rtPageBook.showPage(rtBarWOCT);
		
	}

	private void initBarColors() {
		Display display = getSite().getShell().getDisplay();
		
		roundtimeBG = new Color(display, 151, 0, 0);
		roundtimeFG = new Color(display, 0, 0, 0);
		roundtimeBorder = new Color(display, 151, 130, 130);
		
		casttimeBG = new Color(display, 0, 0, 151);
		casttimeFG = new Color(display, 255, 255, 255);
		casttimeBorder = new Color(display, 130, 130, 151);
	}
	
	protected void gameViewFocused (StormFrontGameView gameView)
	{
		setActiveClient(gameView.getClient());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private class MinivitalsListener implements IPropertyListener<IWarlockDialog> {
		private StormFrontDialogControl control;
		private IWarlockClient client;
		
		public MinivitalsListener(StormFrontDialogControl control, IWarlockClient client) {
			this.control = control;
			this.client = client;
		}
		
		public void propertyChanged(IWarlockDialog msg) {
			if(activeClient == client)
				control.sendMessage(msg);
		}
		
	}
	
	private class RoundtimeListener implements IPropertyListener<Integer> {
		int roundtimeLength = -1;
		private IWarlockClient client;

		public RoundtimeListener(IWarlockClient client) {
			this.client = client;
		}
		
		public void propertyChanged(Integer value) {
			if (client != activeClient)
				return;
			
			if (value == null || value == 0) {
				roundtimeLength = -1;
				roundtime.setSelection(0);
				roundtime2.setSelection(0);
				roundtime.setLabel("no roundtime");
				roundtime2.setLabel("no roundtime");
			} else {
				int length = activeClient.getTimer("roundtime").getLength();
				if (roundtimeLength != length)
				{
					roundtimeLength = length;
					roundtime.setMaximum(roundtimeLength * 1000);
					roundtime2.setMaximum(roundtimeLength * 1000);
					roundtime.setMinimum(0);
					roundtime2.setMinimum(0);
				}
				roundtime.setSelection(value * 1000);
				roundtime2.setSelection(value * 1000);
				roundtime.setLabel("roundtime: " + value + " seconds");
				roundtime2.setLabel("roundtime: " + value + " seconds");
			}
		}
	}
	
	private class CasttimeListener implements IPropertyListener<Integer> {
		int casttimeLength = -1;
		private IWarlockClient client;
		
		public CasttimeListener(IWarlockClient client) {
			this.client = client;
		}

		public void propertyChanged(Integer value) {
			if (client != activeClient)
				return;
				
			if (value == null || value == 0) {
				casttimeLength = -1;
				casttime.setSelection(0);
				casttime.setLabel("no casttime");
				rtPageBook.showPage(rtBarWOCT);
			} else {
				int length = activeClient.getTimer("casttime").getLength();
				if (casttimeLength != length)
				{
					casttimeLength = length;
					casttime.setMaximum(casttimeLength * 1000);
					casttime.setMinimum(0);
				}
				casttime.setSelection(value * 1000);
				casttime.setLabel("casttime: " + value + " seconds");
				rtPageBook.showPage(rtBarWCT);
			}
		}
	}
	
	public void roundtimeChanged(IWarlockClient source, final int roundtime) {
		if(source == activeClient) {
			BarsView.this.roundtime.setSelection(roundtime);
			BarsView.this.roundtime2.setSelection(roundtime);
		}
	}
	
	public static BarsView getDefault ()
	{
		return instance;
	}
}
