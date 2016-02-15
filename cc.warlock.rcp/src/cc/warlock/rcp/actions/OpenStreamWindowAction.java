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
package cc.warlock.rcp.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;

import cc.warlock.rcp.views.StreamView;

public class OpenStreamWindowAction extends Action {

	private String title, streamName, viewPrefix;
	
	public OpenStreamWindowAction (String title, String streamName, String viewPrefix)
	{
		super(title, Action.AS_CHECK_BOX);
		//this.setImageDescriptor(WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_WINDOW));
		
		this.title = title;
		this.streamName = streamName;
		this.viewPrefix = viewPrefix;
	}
	
	@Override
	public void runWithEvent(Event e) {
		StreamView streamView = StreamView.getViewForName(streamName);
		if (streamView != null) {
			streamView.hide();
			setChecked(false);
			//setImageDescriptor(WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_WINDOW));
		} else {
			streamView = StreamView.getOrCreateViewForStream(viewPrefix, streamName);
			setChecked(true);
			//setImageDescriptor(WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_STOP));
		}
		
		e.doit = false;
		
		//streamView.setStreamName(streamName);
		//streamView.setViewTitle(title);
		
		//this.set
	}
	
	@Override
	public String getText() {
 		return title;
	}
}
