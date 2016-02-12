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
package cc.warlock.rcp.stormfront.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;
import cc.warlock.core.client.IWarlockClientViewerListener;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.settings.WindowConfigurationProvider;
import cc.warlock.core.settings.IWarlockSetting;
import cc.warlock.core.settings.IWarlockSettingListener;
import cc.warlock.rcp.stormfront.ui.StormFrontSharedImages;
import cc.warlock.rcp.ui.client.SWTPropertyListener;
import cc.warlock.rcp.ui.client.SWTWarlockClientListener;
import cc.warlock.rcp.ui.client.SWTWarlockClientViewerListener;
import cc.warlock.rcp.util.ColorUtil;
import cc.warlock.rcp.views.GameView;

public class HandsView extends Composite
{
	//public static final String VIEW_ID = "cc.warlock.rcp.stormfront.ui.views.HandsView";
	
	private GradientInfo leftHandInfo, rightHandInfo, spellInfo;
	private GameView viewer;
	private IWarlockSettingListener settingListener;
	
	private final SWTWarlockClientViewerListener viewerListener = new SWTWarlockClientViewerListener(new IWarlockClientViewerListener() {
		@Override
		public void clientChanged(IWarlockClient client) {
			if (client == null)
				return;
			
			clear();
			client.getProperty("left").addListener(new SWTPropertyListener<String>(
					new HandListener(leftHandInfo, "empty")));
			client.getProperty("right").addListener(new SWTPropertyListener<String>(
					new HandListener(rightHandInfo, "empty")));
			client.getProperty("spell").addListener(new SWTPropertyListener<String>(
					new HandListener(spellInfo, "none")));
			
			IClientSettings settings = client.getClientSettings();
			WarlockClientRegistry.addWarlockClientListener(clientListener);
			if(settings != null)
				loadSettings(settings);
		}
	});
	private SWTWarlockClientListener clientListener = new SWTWarlockClientListener(new IWarlockClientListener() {
		@Override
		public void clientConnected(IWarlockClient client) {}
		@Override
		public void clientDisconnected(IWarlockClient client) {}
		@Override
		public void clientSettingsLoaded(IWarlockClient client) {
			loadSettings(client.getClientSettings());
		}
	});
	
	public HandsView (Composite parent, GameView viewer) {
		super(parent, SWT.NONE);
		
		this.viewer = viewer;
		
		//parent.setLayout(new FillLayout());
		//parent.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);
		//main.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		leftHandInfo = new GradientInfo(this, StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_LEFT_HAND_SMALL));
		
		leftHandInfo.setForeground(new Color(getDisplay(), 240, 240, 255));
		leftHandInfo.setBackground(new Color(getDisplay(), 25, 25, 50));
		leftHandInfo.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		rightHandInfo = new GradientInfo(this, StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_RIGHT_HAND_SMALL));
			
		rightHandInfo.setForeground(new Color(getDisplay(), 240, 240, 255));
		rightHandInfo.setBackground(new Color(getDisplay(), 25, 25, 50));
		rightHandInfo.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		spellInfo = new GradientInfo(this, StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_SPELL_HAND_SMALL));
			
		spellInfo.setForeground(new Color(getDisplay(), 240, 240, 255));
		spellInfo.setBackground(new Color(getDisplay(), 25, 25, 50));
		spellInfo.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		clear();
		viewer.addClientViewerListener(viewerListener);
	}
	
	
	private class GradientInfo extends Canvas implements PaintListener
	{
		private String text;
		private Image image;
		private Color background, foreground, gradientColor;
		
		public GradientInfo (Composite parent, Image image)
		{
			super(parent, SWT.NONE);
			
			this.image = image;
			
			addPaintListener(this);
			this.foreground = new Color(getDisplay(), 255, 255, 255);
			this.background = new Color(getDisplay(), 0, 0, 0);
			this.gradientColor = getGradientColor(50, true);
		}
		
		private Color getGradientColor (int factor, boolean lighter)
		{
			int red = 0;
			int green = 0;
			int blue = 0;
			
			if (lighter) 
			{
				red = background.getRed() < (255 - factor) ? background.getRed() + factor : 255;
				green = background.getGreen() < (255 - factor) ? background.getGreen() + factor : 255;
				blue = background.getBlue() < (255 - factor) ? background.getBlue() + factor : 255;
			}
			else {
				red = background.getRed() > factor ? background.getRed() - factor : 0;
				green = background.getRed() > factor ? background.getRed() - factor : 0;
				blue = background.getRed() > factor ? background.getRed() - factor : 0;
			}
			
			return new Color(getShell().getDisplay(), red, green, blue);
		}
		
		@Override
		public void setForeground(Color color) {
			this.foreground = color;
			redraw();
		}
		
		@Override
		public void setBackground(Color color) {
			this.background = color;
			this.gradientColor = getGradientColor(125, true);
			
			redraw();
		}
		
		public void paintControl(PaintEvent e) {
			Rectangle bounds = getBounds();
			Rectangle imageBounds = image.getBounds();
			int tabPadding = 4;
			
			e.gc.setBackground(background);
			e.gc.setForeground(gradientColor);
			e.gc.fillRectangle(0, 0, bounds.width, bounds.height);
			if(text != null)
				e.gc.fillGradientRectangle(tabPadding/2, tabPadding/2, bounds.width - tabPadding/2, e.gc.textExtent(text).y, true);
			
			e.gc.setForeground(foreground);
			e.gc.drawImage(image, tabPadding/2, tabPadding/2);
			if (text != null)
				e.gc.drawText(text, imageBounds.width + tabPadding, tabPadding, true);
		}
		
		public void setText(String text) {
			this.text = text;
			redraw();
		}
	}

	protected void setColors (Color fg, Color bg)
	{
		leftHandInfo.setBackground(bg);
		leftHandInfo.setForeground(fg);
		
		rightHandInfo.setBackground(bg);
		rightHandInfo.setForeground(fg);
		
		spellInfo.setBackground(bg);
		spellInfo.setForeground(fg);
	}
	
	public void loadSettings (IClientSettings settings) {
		if(settings == null)
			return;
		
		WindowConfigurationProvider provider = WindowConfigurationProvider.getProvider(settings);
		Color bg = ColorUtil.warlockColorToColor(provider.getDefaultBackgroundColor());
		Color fg = ColorUtil.warlockColorToColor(provider.getDefaultForegroundColor());
		
		setColors(fg, bg);
		
		if(settingListener == null) {
			settingListener = new IWarlockSettingListener() {
				@Override
				public void settingChanged(IWarlockSetting setting) {
					loadSettings(viewer.getClient().getClientSettings());
				}
			};
			provider.addListener(settingListener);
		}
	}

	private class HandListener implements IPropertyListener<String> {
		private GradientInfo hand;
		private String emptyText;
		
		public HandListener(GradientInfo hand, String emptyText) {
			this.hand = hand;
			this.emptyText = emptyText;
		}
		
		public void propertyChanged(String value) {
			if(value == null)
				hand.setText(emptyText);
			else
				hand.setText(value);
		}
	}
	
	protected void clear ()
	{
		leftHandInfo.setText("Empty");
		rightHandInfo.setText("Empty");
		spellInfo.setText("None");
	}
}
