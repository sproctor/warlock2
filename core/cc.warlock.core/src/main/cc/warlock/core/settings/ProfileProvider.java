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
package cc.warlock.core.settings;

import java.util.ArrayList;
import java.util.Collection;



public class ProfileProvider extends ArrayConfigurationProvider<Profile> implements IWarlockSetting {
	
	public ProfileProvider(IWarlockSetting parent) {
		super(parent, "profiles");
	}
	
	protected Profile loadSetting(String id) {
		return new Profile(this, id);
	}
	
	public Profile createProfile (String id, String name, String gameCode, String gameName) {
		Profile profile = createSetting();
		profile.setCharacterId(id); //character id
		profile.setName(name);
		profile.setGameCode(gameCode);
		profile.setGameName(gameName);
		
		flush();
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
	
	public static Collection<Profile> getAllProfiles() {
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		for(Account account : AccountProvider.getInstance().getSettings()) {
			profiles.addAll(account.getProfiles());
		}
		return profiles;
	}
}
