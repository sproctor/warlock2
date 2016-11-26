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
/**
 * 
 */
package cc.warlock.rcp.menu;

import java.util.ArrayList;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import cc.warlock.core.client.IStream;
import cc.warlock.rcp.actions.StreamShowAction;
import cc.warlock.rcp.views.DebugView;
import cc.warlock.rcp.views.GameView;
import cc.warlock.rcp.views.StreamView;
import cc.warlock.rcp.views.UserStream;

/**
 * @author Will Robertson
 * Streams Menu Contribution - Adds all menu items to preferences.
 */
public class StreamsContributionItem extends CompoundContributionItem  {

	private IContributionItem createUserStreamItem (String name)
	{
		return new ActionContributionItem(new StreamShowAction("Custom - " + name, UserStream.VIEW_ID, "rightFolder." + name));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.actions.CompoundContributionItem#getContributionItems()
	 */
	@Override
	protected IContributionItem[] getContributionItems() {
		// Add Menu Items
		ArrayList<IContributionItem> items = new ArrayList<IContributionItem>();
		items.add(new ActionContributionItem(new StreamShowAction("Debug Console", DebugView.VIEW_ID, null)));
		for (String name : UserStream.getCustomViewNames()) {
			items.add(createUserStreamItem(name));
		}
		GameView gameView = GameView.getGameViewInFocus();
		if (gameView != null && gameView.getClient() != null) {
			for (IStream stream : gameView.getClient().getStreams()) {
				if (stream.getName().equals("main"))
					continue;
				String location = StreamView.RIGHT_STREAM_PREFIX;
				if (stream.getLocation().equals("center")) {
					location = StreamView.TOP_STREAM_PREFIX;
				} else if (stream.getLocation().equals("left")) {
					location = StreamView.LEFT_STREAM_PREFIX;
				}
				items.add(new ActionContributionItem(new StreamShowAction(stream.getTitle(),
						StreamView.STREAM_VIEW_PREFIX + location, stream.getName())));
			}
		}
		return items.toArray(new IContributionItem[items.size()]);
	}
}
