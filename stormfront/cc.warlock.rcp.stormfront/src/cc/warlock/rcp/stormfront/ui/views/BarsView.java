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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockClientAdapter;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.internal.ClientProperty;
import cc.warlock.core.stormfront.client.BarStatus;
import cc.warlock.core.stormfront.client.IStormFrontClient;
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
		casttimeFG, casttimeBG, casttimeBorder,
		healthFG, healthBG, healthBorder,
		manaFG, manaBG, manaBorder,
		fatigueFG, fatigueBG, fatigueBorder,
		spiritFG, spiritBG, spiritBorder;
	
	protected Composite rtBarWOCT, rtBarWCT = null;
	protected PageBook rtPageBook = null;
	
	protected WarlockProgressBar health, fatigue, spirit, mana, roundtime, casttime;
	
	protected SWTPropertyListener<Integer> rtListener;
	protected SWTPropertyListener<Integer> ctListener;
	protected SWTPropertyListener<BarStatus> barListener;
	protected IStormFrontClient activeClient;
	protected ArrayList<IStormFrontClient> clients = new ArrayList<IStormFrontClient>();
	
	public BarsView() {
		instance = this;
		rtListener = new SWTPropertyListener<Integer>(new RoundtimeListener());
		ctListener = new SWTPropertyListener<Integer>(new CasttimeListener());
		barListener = new SWTPropertyListener<BarStatus>(new BarListener());
		
		WarlockClientRegistry.addWarlockClientListener(new WarlockClientAdapter() {
			public void clientConnected(final IWarlockClient client) {
				if (client instanceof IStormFrontClient)
				{
					Display.getDefault().asyncExec(new Runnable() {
						public void run () {
							setActiveClient((IStormFrontClient)client);
						}
					});
				}
			}
		});
		
		GameView.addGameViewFocusListener(new IGameViewFocusListener () {
			public void gameViewFocused(GameView gameView) {
				if (gameView instanceof StormFrontGameView)
				{
					BarsView.this.gameViewFocused((StormFrontGameView)gameView);
				}
			}
		});
	}

	protected void setActiveClient (IStormFrontClient client)
	{
		if (client == null) return;
		
		this.activeClient = client;
		
		if (!clients.contains(client))
		{
			client.getHealth().addListener(barListener);
			client.getMana().addListener(barListener);
			client.getSpirit().addListener(barListener);
			client.getFatigue().addListener(barListener);
			client.getRoundtime().addListener(rtListener);
			client.getCasttime().addListener(ctListener);
			clients.add(client);
			
		} else {
			barListener.propertyChanged(client.getHealth(), null);
			barListener.propertyChanged(client.getMana(), null);
			barListener.propertyChanged(client.getSpirit(), null);
			barListener.propertyChanged(client.getFatigue(), null);
			rtListener.propertyChanged(client.getRoundtime(), null);
			ctListener.propertyChanged(client.getCasttime(), null);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite top = new Composite (parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = layout.marginHeight = layout.horizontalSpacing = 0;
		top.setLayout(layout);
		top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		/* Initialize the rtPageBook so it comes first */
		rtPageBook = new PageBook(top, SWT.NONE);
		rtPageBook.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		Composite barComposite = new Composite(top, SWT.NONE);
		layout = new GridLayout(4, false);
		layout.marginWidth = layout.marginHeight = layout.horizontalSpacing = 0;
		barComposite.setLayout(layout);
		barComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
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
		
		roundtime = new WarlockProgressBar(rtBarWCT, SWT.NONE);
		roundtime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));
		roundtime.setMinimum(0); roundtime.setMaximum(0); roundtime.setLabel("roundtime: 0");
		roundtime.setBackground(roundtimeBG); roundtime.setForeground(roundtimeFG); roundtime.setBorderColor(roundtimeBorder);
//		roundtime.setSize(300, 5); //roundtime.setShowText(false);
		
		casttime = new WarlockProgressBar(rtBarWCT, SWT.NONE);
		casttime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));
		casttime.setMinimum(0); casttime.setMaximum(0); casttime.setLabel("casttime: 0");
		casttime.setBackground(casttimeBG); casttime.setForeground(casttimeFG); casttime.setBorderColor(casttimeBorder);
		
		rtPageBook.showPage(rtBarWOCT);
		
		health = new WarlockProgressBar(barComposite, SWT.NONE);
		health.setMinimum(0); health.setMaximum(100); health.setSelection(100); health.setLabel("health 100%");
		health.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		health.setBackground(healthBG); health.setForeground(healthFG); health.setBorderColor(healthBorder);
		
		mana = new WarlockProgressBar(barComposite, SWT.NONE);
		mana.setMinimum(0); mana.setMaximum(100); mana.setSelection(100); mana.setLabel("mana 100%");
		mana.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		mana.setBackground(manaBG); mana.setForeground(manaFG); mana.setBorderColor(manaBorder);
		
		fatigue = new WarlockProgressBar(barComposite, SWT.NONE);
		fatigue.setMinimum(0); fatigue.setMaximum(100); fatigue.setSelection(100); fatigue.setLabel("fatigue 100%");
		fatigue.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		fatigue.setBackground(fatigueBG); fatigue.setForeground(fatigueFG); fatigue.setBorderColor(fatigueBorder);
		
		spirit = new WarlockProgressBar(barComposite, SWT.NONE);
		spirit.setMinimum(0); spirit.setMaximum(100); spirit.setSelection(100); spirit.setLabel("spirit 100%");
		spirit.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		spirit.setBackground(spiritBG); spirit.setForeground(fatigueFG); spirit.setBorderColor(spiritBorder);
	}

	private void initBarColors() {
		Display display = getSite().getShell().getDisplay();
		
		healthBG = new Color(display, 0x80, 0, 0);
		healthFG = new Color(display, 255, 255, 255);
		healthBorder = new Color(display, 0x79, 0x6a, 0x6a);
		
		manaBG = new Color(display, 0, 0, 0xff);
		manaFG = new Color(display, 255, 255, 255);
		manaBorder = new Color(display, 0x72, 0x72, 0xff);
		
		fatigueBG = new Color(display, 0xd0, 0x98, 0x2f);
		fatigueFG = new Color(display, 0, 0, 0);
		fatigueBorder = new Color(display, 0xde, 0xcc, 0xaa);
		
		spiritBG = new Color(display, 150, 150, 150);
		spiritFG = new Color(display, 0, 0, 0);
		spiritBorder = new Color(display, 225, 225, 225);
		
		roundtimeBG = new Color(display, 151, 0, 0);
		roundtimeFG = new Color(display, 0, 0, 0);
		roundtimeBorder = new Color(display, 151, 130, 130);
		
		casttimeBG = new Color(display, 0, 0, 151);
		casttimeFG = new Color(display, 255, 255, 255);
		casttimeBorder = new Color(display, 130, 130, 151);
	}
	
	protected void gameViewFocused (StormFrontGameView gameView)
	{
		setActiveClient(gameView.getStormFrontClient());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private class RoundtimeListener implements IPropertyListener<Integer> {
		int roundtimeLength = -1;
		
		public void propertyActivated(IProperty<Integer> property) {	}

		public void propertyCleared(IProperty<Integer> property, Integer oldValue) {	}
		
		public void propertyChanged(IProperty<Integer> property, Integer oldValue) {
			if (property == null || property.getName() == null || !property.getName().equals("roundtime")) return;

			if (property instanceof ClientProperty)
			{
				ClientProperty<Integer> clientProperty = (ClientProperty<Integer>) property;
				if (clientProperty.getClient() == activeClient)
				{
					if (property.get() == 0) {
						roundtimeLength = -1;
						roundtime.setSelection(0);
						roundtime.setLabel("no roundtime");
					} else {
						if (roundtimeLength != activeClient.getRoundtimeLength())
						{
							roundtimeLength = activeClient.getRoundtimeLength();
							roundtime.setMaximum(roundtimeLength * 1000);
							roundtime.setMinimum(0);
						}
						roundtime.setSelection(property.get() * 1000);
						roundtime.setLabel("roundtime: " + property.get() + " seconds");
					}
				}
			}
		}
	}
	
	private class CasttimeListener implements IPropertyListener<Integer> {
		int casttimeLength = -1;
		
		public void propertyActivated(IProperty<Integer> property) {	}

		public void propertyCleared(IProperty<Integer> property, Integer oldValue) {	}
		
		public void propertyChanged(IProperty<Integer> property, Integer oldValue) {
			if (property == null || property.getName() == null || !property.getName().equals("casttime")) return;

			if (property instanceof ClientProperty)
			{
				ClientProperty<Integer> clientProperty = (ClientProperty<Integer>) property;
				if (clientProperty.getClient() == activeClient)
				{
					if (property.get() == 0) {
						casttimeLength = -1;
						casttime.setSelection(0);
						casttime.setLabel("no casttime");
						rtPageBook.showPage(rtBarWOCT);
					} else {
						if (casttimeLength != activeClient.getCasttimeLength())
						{
							casttimeLength = activeClient.getCasttimeLength();
							casttime.setMaximum(casttimeLength * 1000);
							casttime.setMinimum(0);
						}
						casttime.setSelection(property.get() * 1000);
						casttime.setLabel("casttime: " + property.get() + " seconds");
						rtPageBook.showPage(rtBarWCT);
					}
				}
			}
		}
	}
	
	private class BarListener implements IPropertyListener<BarStatus> {
		
		public void propertyActivated(IProperty<BarStatus> property) { }
		public void propertyCleared(IProperty<BarStatus> property, BarStatus oldValue) { }
		
		public void propertyChanged(IProperty<BarStatus> property, BarStatus oldValue) {
			if (property == null || property.get() == null || property.getName() == null) return;

			if (property instanceof ClientProperty)
			{
				ClientProperty<BarStatus> clientProperty = (ClientProperty<BarStatus>) property;
				if (clientProperty.getClient() == activeClient)
				{

					if (property.getName().equals("health"))
					{
						health.setSelection(property.get().getValue());
						health.setLabel(property.get().getText());
					}
					else if (property.getName().equals("mana"))
					{
						mana.setSelection(property.get().getValue());
						mana.setLabel(property.get().getText());
					}
					else if (property.getName().equals("spirit"))
					{
						spirit.setSelection(property.get().getValue());
						spirit.setLabel(property.get().getText());
					}
					else if (property.getName().equals("fatigue"))
					{
						fatigue.setSelection(property.get().getValue());
						fatigue.setLabel(property.get().getText());
					}
				}
			}
		}
	}
	
	
	public void roundtimeChanged(IWarlockClient source, final int roundtime) {
		if(source == activeClient)
			BarsView.this.roundtime.setSelection(roundtime);
	}
	
	public static BarsView getDefault ()
	{
		return instance;
	}
}
