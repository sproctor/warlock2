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
package cc.warlock.core.test;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import cc.warlock.core.settings.AccountProvider;
import cc.warlock.core.settings.ProfileSetting;
import cc.warlock.core.stormfront.network.ISGEConnectionListener;
import cc.warlock.core.stormfront.network.ISGEGame;
import cc.warlock.core.stormfront.network.SGEConnection;

public class SGETest {
	protected Hashtable<String,Boolean> success = new Hashtable<String,Boolean>();

	@Test(timeout=30000)
	public void testAutoLogin() {
		List<String> profileNames = TestProperties.getList(TestProperties.PROFILE_NAMES);
		if (profileNames == null) {
			TestProperties.failProperty(TestProperties.PROFILE_NAMES);
		}
		
		for (final String profileName : profileNames)
		{
			ProfileSetting profile = AccountProvider.getInstance().getProfileByCharacterName(profileName);
			Assert.assertNotNull("Profile described by \"" + profileName + "\" was null!", profile);
			
			success.put(profileName, false);
			TestUtil.autoLogin(profile, new ISGEConnectionListener(){
				@Override
				public void readyToPlay(SGEConnection connection, java.util.Map<String,String> loginProperties) {
					success.put(profileName, true);
				}

				@Override
				public void loginReady(SGEConnection connection) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void loginFinished(SGEConnection connection) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void gamesReady(SGEConnection connection,
						List<? extends ISGEGame> games) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void charactersReady(SGEConnection connection,
						Map<String, String> characters) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void sgeError(SGEConnection connection, int errorCode) {
					// TODO Auto-generated method stub
					
				};
			});
			
			Assert.assertTrue(success.get(profileName));
		}
	}

}
