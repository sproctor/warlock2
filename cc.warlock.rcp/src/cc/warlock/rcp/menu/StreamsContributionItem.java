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

import java.util.HashMap;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

import cc.warlock.rcp.views.UserStream;

/**
 * @author Will Robertson
 * Streams Menu Contribution - Adds all menu items to preferences.
 */
public class StreamsContributionItem extends CompoundContributionItem  {
	// Moved hard settings to cc.warlock.userstreams.ui.views/UserStream.java

	private IContributionItem createStreamContributionItem (String name)
	{
		CommandContributionItemParameter param = new CommandContributionItemParameter(PlatformUI.getWorkbench(),
				UserStream.VIEW_ID + "." + name, "cc.warlock.rcp.command.streamshow", CommandContributionItem.STYLE_CHECK);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		param.parameters = params;
		return new CommandContributionItem(param);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.actions.CompoundContributionItem#getContributionItems()
	 */
	@Override
	protected IContributionItem[] getContributionItems() {
		// Add Menu Items
		return new IContributionItem[] {
				createStreamContributionItem("Events"),
				createStreamContributionItem("Conversations"),
				createStreamContributionItem("Healing")
		};
	}
}
