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
package cc.warlock.core.configuration;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.internal.ArrayConfigurationProvider;


public class ProfileProvider extends ArrayConfigurationProvider<Profile> implements IConfigurationProvider {
	
	private Account account;
	
	public ProfileProvider(Account account, Preferences parentNode) {
		super(parentNode, "profiles");
	}
	
	protected Profile loadSetting(String id) {
		return new Profile(account, getNode(), id);
	}
	
	public Profile createProfile (String id, String name, String gameCode, String gameName) {
		Profile profile = createSetting();
		profile.setId(id); //character id
		profile.setName(name);
		profile.setGameCode(gameCode);
		profile.setGameName(gameName);
		
		return profile;
	}
	
	public Profile getProfileByCharacterName (String characterName)
	{	
		for (Profile profile : getSettings())
		{
			if (profile.getName().equals(characterName))
				return profile;
		}
		return null;
	}
}
