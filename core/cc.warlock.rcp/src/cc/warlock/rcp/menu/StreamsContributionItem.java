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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;

import cc.warlock.rcp.actions.StreamShowAction;
import cc.warlock.rcp.views.DebugView;
import cc.warlock.rcp.views.GameView;

/**
 * @author Will Robertson
 * Streams Menu Contribution - Adds all menu items to preferences.
 */
public class StreamsContributionItem extends CompoundContributionItem  {

	// Moved hard settings to cc.warlock.userstreams.ui.views/UserStream.java

	protected IContributionItem createStreamContributionItem (String name)
	{
		return new ActionContributionItem(new StreamShowAction(name));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.actions.CompoundContributionItem#getContributionItems()
	 */
	@Override
	protected IContributionItem[] getContributionItems() {
		// Add Menu Items
		ArrayList<IContributionItem> items = new ArrayList<IContributionItem>();
		items.add(new ActionContributionItem(new DebugAction()));
		items.add(createStreamContributionItem("Events"));
		items.add(createStreamContributionItem("Conversations"));
		items.add(createStreamContributionItem("Healing"));
		
		return items.toArray(new IContributionItem[items.size()]); 
	}
	
	private class DebugAction extends Action {
		
		private static final String title = "Debug";
		
		public DebugAction() {
			super(title, Action.AS_CHECK_BOX);
		}
		
		@Override
		public void run() {
			try {
				DebugView view = (DebugView)
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DebugView.VIEW_ID, null, IWorkbenchPage.VIEW_VISIBLE);
				GameView inFocus = GameView.getGameViewInFocus();
				if (inFocus != null) {
					view.setClient(inFocus.getClient());
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public String getText() {
	 		return title;
		}
	}
}
