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
import java.util.Collection;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.actions.CompoundContributionItem;

import cc.warlock.core.settings.Account;
import cc.warlock.core.settings.AccountProvider;
import cc.warlock.core.settings.ProfileSetting;
import cc.warlock.rcp.stormfront.ui.StormFrontSharedImages;
import cc.warlock.rcp.stormfront.ui.actions.ProfileConnectAction;
import cc.warlock.rcp.ui.ConnectionAction;
import cc.warlock.rcp.ui.IConnectionCommand;
import cc.warlock.rcp.ui.IConnectionCommandProvider;


public class ProfileConnectContributionItem extends CompoundContributionItem implements IConnectionCommandProvider {

	public ProfileConnectContributionItem() {
		super();
	}
	
	public ProfileConnectContributionItem(String id) {
		super(id);
	}

	@Override
	protected IContributionItem[] getContributionItems() {
		ArrayList<IContributionItem> items = new ArrayList<IContributionItem>();
		
		for(Account account : AccountProvider.getInstance().getSettings()) {
			for (ProfileSetting profile : account.getProfiles()) {
				items.add(new ActionContributionItem(new ProfileConnectAction(profile)));
			}
		}
		
		return items.toArray(new IContributionItem[items.size()]);
	}
	
	public Collection<IConnectionCommand> getConnectionCommands () {
		ArrayList<IConnectionCommand> commands = new ArrayList<IConnectionCommand>();
		
		for(Account account : AccountProvider.getInstance().getSettings()) {
			for (ProfileSetting profile : account.getProfiles()) {
				commands.add(new ConnectionAction(new ProfileConnectAction(profile)));
			}
		}
		
		return commands;
	}
	
	public String getDescription(IHandler handler) {
		ProfileConnectAction phandler = (ProfileConnectAction)handler;
		
		return phandler.getProfile().getGameName() + ": " + phandler.getProfile().getCharacterName(); 
	}
	
	public Image getImage(IHandler handler) {
		return StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_CHARACTER);
	}
	
	public String getLabel(IHandler handler) {
		return ((ProfileConnectAction)handler).getProfile().getCharacterName();
	}
}
