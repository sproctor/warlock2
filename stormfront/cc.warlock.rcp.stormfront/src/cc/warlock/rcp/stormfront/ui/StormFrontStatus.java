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
package cc.warlock.rcp.stormfront.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import cc.warlock.core.client.ICharacterStatus;
import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.settings.WindowConfigurationProvider;
import cc.warlock.rcp.ui.client.SWTPropertyListener;
import cc.warlock.rcp.util.ColorUtil;

public class StormFrontStatus extends Composite {

	protected Label[] statusLabels = new Label[6];
	protected IWarlockClient client;
	protected SWTPropertyListener<String> statusListener = new SWTPropertyListener<String>(new IPropertyListener<String>() {
		@Override
		public void propertyChanged(String value) {
			updateStatus();
		}
	});
	protected DecorationOverlayIcon multipleStatus;
	protected Image multipleStatusImage;
	private SWTPropertyListener<Integer> rtListener = new SWTPropertyListener<Integer>(new IPropertyListener<Integer>() {
		public void propertyChanged(Integer value) {
			if (value == 0)
				statusLabels[4].setText("");
			else
				statusLabels[4].setText(value.toString());
		}
	});
	private SWTPropertyListener<Integer> ctListener = new SWTPropertyListener<Integer>(new IPropertyListener<Integer>() {
		public void propertyChanged(Integer value) {
			if (value == 0)
				statusLabels[5].setText("");
			else
				statusLabels[5].setText(value.toString());
		}
	});
	
	public StormFrontStatus (Composite parent) {
		super(parent, SWT.BORDER);
		GridLayout layout = new GridLayout(statusLabels.length, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		this.setLayout(layout);

		for (int i = 0; i < statusLabels.length; i++) {
			statusLabels[i] = new Label(this, SWT.CENTER);
			statusLabels[i].setImage(StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_STATUS_BLANK));
			GridData data = new GridData(GridData.FILL | SWT.CENTER, GridData.FILL | SWT.CENTER, true, true);
			data.minimumWidth = 24;
			statusLabels[i].setLayoutData(data);
		}
		
		setColors(new Color(getDisplay(), 240, 240, 255), new Color(getDisplay(), 25, 25, 50));
	}
	
	protected void clear ()
	{
		for (Label label : statusLabels) {
			label.setImage(StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_STATUS_BLANK));
		}
	}
	
	protected void setStatusImage(int place, String imageId)
	{
		if (statusLabels[place] == null)
			return;
		
		statusLabels[place].setImage(StormFrontSharedImages.getImage(imageId));
	}
	
	private void updateStatus() {

		ICharacterStatus status = client.getCharacterStatus();

		if (status.hasStatus(ICharacterStatus.StatusType.Invisible)) {
			if (status.hasStatus(ICharacterStatus.StatusType.Standing))
				setStatusImage(0, StormFrontSharedImages.IMG_STATUS_INVIS_STANDING);
			else if (status.hasStatus(ICharacterStatus.StatusType.Sitting))
				setStatusImage(0, StormFrontSharedImages.IMG_STATUS_INVIS_SITTING);
			else if (status.hasStatus(ICharacterStatus.StatusType.Kneeling))
				setStatusImage(0, StormFrontSharedImages.IMG_STATUS_INVIS_KNEELING);
			else if (status.hasStatus(ICharacterStatus.StatusType.Prone))
				setStatusImage(0, StormFrontSharedImages.IMG_STATUS_INVIS_PRONE);
		} else {
			if (status.hasStatus(ICharacterStatus.StatusType.Standing))
				setStatusImage(0, StormFrontSharedImages.IMG_STATUS_STANDING);
			else if (status.hasStatus(ICharacterStatus.StatusType.Sitting))
				setStatusImage(0, StormFrontSharedImages.IMG_STATUS_SITTING);
			else if (status.hasStatus(ICharacterStatus.StatusType.Kneeling))
				setStatusImage(0, StormFrontSharedImages.IMG_STATUS_KNEELING);
			else if (status.hasStatus(ICharacterStatus.StatusType.Prone))
				setStatusImage(0, StormFrontSharedImages.IMG_STATUS_PRONE);
		}

		if (status.hasStatus(ICharacterStatus.StatusType.Joined))
			setStatusImage(1, StormFrontSharedImages.IMG_STATUS_JOINED);
		else
			setStatusImage(1, StormFrontSharedImages.IMG_STATUS_BLANK);

		if (status.hasStatus(ICharacterStatus.StatusType.Hidden))
			setStatusImage(2, StormFrontSharedImages.IMG_STATUS_HIDDEN);
		else if (status.hasStatus(ICharacterStatus.StatusType.Dead))
			setStatusImage(2, StormFrontSharedImages.IMG_STATUS_DEAD);
		else
			setStatusImage(2, StormFrontSharedImages.IMG_STATUS_BLANK);

		if (statusLabels[3] == null)
			return;
		
		Image baseImage = StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_STATUS_BLANK);
		ImageDescriptor overlays[] = new ImageDescriptor[] { null, null, null, null, null };
		
		if (status.hasStatus(ICharacterStatus.StatusType.Bleeding)) {
			overlays[0] = StormFrontSharedImages.getImageDescriptor(StormFrontSharedImages.IMG_STATUS_BLEEDING);
		}
		if (status.hasStatus(ICharacterStatus.StatusType.Stunned)) {
			overlays[1] = StormFrontSharedImages.getImageDescriptor(StormFrontSharedImages.IMG_STATUS_STUNNED);
		}
		if (status.hasStatus(ICharacterStatus.StatusType.Webbed)) {
			overlays[3] = StormFrontSharedImages.getImageDescriptor(StormFrontSharedImages.IMG_STATUS_WEBBED);
		}

		Image oldImage = multipleStatusImage;
		
		multipleStatus = new DecorationOverlayIcon(baseImage, overlays);
		multipleStatusImage = multipleStatus.createImage();
		
		statusLabels[3].setImage(multipleStatusImage);
		
		if (oldImage != null)
			oldImage.dispose();
	}
	
	protected void setColors (Color fg, Color bg)
	{
		for (int i = 0; i < 4; i++) {
			statusLabels[i].setForeground(fg);
			statusLabels[i].setBackground(bg);
		}
		statusLabels[4].setForeground(new Color(getDisplay(), 225, 50, 50));
		statusLabels[4].setBackground(bg);
		statusLabels[5].setForeground(new Color(getDisplay(), 50, 50, 225));
		statusLabels[5].setBackground(bg);
	}
	
	public void loadSettings (IClientSettings settings) {	
		Color bg = ColorUtil.warlockColorToColor(WindowConfigurationProvider.getProvider(settings).getDefaultBackground());
		Color fg = ColorUtil.warlockColorToColor(WindowConfigurationProvider.getProvider(settings).getDefaultForeground());
		
		setColors(fg, bg);
	}
	
	public void setClient (IWarlockClient client) {
		if (client == null || client == this.client)
			return;
		
		client.getCharacterStatus().removeListener(statusListener);
		this.client = client;
		
		clear();
		client.getCharacterStatus().addListener(statusListener);
		client.getTimer("roundtime").getProperty().addListener(rtListener);
		client.getTimer("casttime").getProperty().addListener(ctListener);
		
		IClientSettings settings = client.getClientSettings();
		if(settings != null)
			loadSettings(settings);
	}
	
}
