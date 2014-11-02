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
package cc.warlock.rcp.stormfront.ui.menu;

import java.util.ArrayList;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import cc.warlock.core.client.IStream;
import cc.warlock.rcp.actions.OpenStreamWindowAction;
import cc.warlock.rcp.views.GameView;
import cc.warlock.rcp.views.StreamView;


public class StreamWindowContributionItem extends CompoundContributionItem {
	
	/*private class ScriptManagerAction extends Action {
		
		private static final String title = "Scripts Manager";
		
		public ScriptManagerAction() {
			super(title, Action.AS_PUSH_BUTTON);
		}
		
		public void run() {
			try {
				ScriptManager view = (ScriptManager)
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ScriptManager.VIEW_ID, null, IWorkbenchPage.VIEW_VISIBLE);
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public String getText() {
	 		return title;
		}
	}*/
	
	@Override
	protected IContributionItem[] getContributionItems() {	
		ArrayList<IContributionItem> items = new ArrayList<IContributionItem>();
		GameView gameView = GameView.getGameViewInFocus();
		if (gameView != null && gameView.getClient() != null) {
			for (IStream stream : gameView.getClient().getStreams()) {
				String location = StreamView.RIGHT_STREAM_PREFIX;
				if (stream.getLocation().equals("center")) {
					location = StreamView.TOP_STREAM_PREFIX;
				} else if (stream.getLocation().equals("left")) {
					location = StreamView.LEFT_STREAM_PREFIX;
				}
				items.add(streamContribution(stream.getTitle(),
						stream.getName(), location));
			}
		}
		return items.toArray(new IContributionItem[items.size()]);
	}
	
	protected IContributionItem streamContribution(String label, String streamName, String prefix)
	{
		return new ActionContributionItem(new OpenStreamWindowAction(label, streamName, prefix));
	}
}
