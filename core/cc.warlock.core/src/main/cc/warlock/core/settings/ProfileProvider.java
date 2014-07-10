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



public class ProfileProvider extends ArrayConfigurationProvider<ProfileSetting> implements IWarlockSetting {
	
	public ProfileProvider(IWarlockSetting parent) {
		super(parent, "profiles");
	}
	
	protected ProfileSetting loadSetting(String id) {
		return new ProfileSetting(this, id);
	}
	
	public ProfileSetting createProfile (String id, String name, String gameCode, String gameName) {
		ProfileSetting profile = createSetting();
		profile.setCharacterId(id);
		profile.setCharacterName(name);
		profile.setGameCode(gameCode);
		profile.setGameName(gameName);
		
		flush();
		return profile;
	}
	
	/*public ProfileSetting getProfileByCharacterName (String characterName)
	{	
		for (ProfileSetting profile : getSettings()) {
			if (characterName.equals(profile.getName()))
				return profile;
		}
		return null;
	}*/
	
	public ProfileSetting getProfileByViewId (String viewId)
	{	
		for (ProfileSetting profile : getSettings()) {
			if (viewId.equals(profile.getViewId()))
				return profile;
		}
		return null;
	}
	
	public static Collection<ProfileSetting> getAllProfiles() {
		ArrayList<ProfileSetting> profiles = new ArrayList<ProfileSetting>();
		for(Account account : AccountProvider.getInstance().getSettings()) {
			profiles.addAll(account.getProfiles());
		}
		return profiles;
	}
}
