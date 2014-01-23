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
/*
 * Created on Dec 31, 2004
 */
package cc.warlock.core.settings;

import cc.warlock.core.client.IProfile;



/**
 * @author Marshall
 */
public class ProfileSetting extends WarlockSetting implements IProfile {

	protected String characterId, name, gameCode, gameName, viewId;
	
	public ProfileSetting (IWarlockSetting parent, String path) {
		super(parent, path);
		
		this.characterId = getNode().get("character-id", null);
		this.name = getNode().get("name", null);
		this.gameCode = getNode().get("game-code", null);
		this.gameName = getNode().get("game-name", null);
		this.viewId = getNode().get("view-id", null);
	}
	
	/**
	 * @return Returns the gameCode.
	 */
	@Override
	public String getGameCode() {
		return gameCode;
	}
	
	/**
	 * @param gameCode The gameCode to set.
	 */
	@Override
	public void setGameCode(String gameCode) {
		getNode().put("game-code", gameCode);
		this.gameCode = gameCode;
	}
	
	/**
	 * @return Returns the gameName.
	 */
	@Override
	public String getGameName() {
		return gameName;
	}
	/**
	 * @param gameName The gameName to set.
	 */
	@Override
	public void setGameName(String gameName) {
		getNode().put("game-name", gameName);
		this.gameName = gameName;
	}
	
	@Override
	public String getCharacterId() {
		return characterId;
	}
	
	@Override
	public void setCharacterId(String id) {
		getNode().put("character-id", id);
		this.characterId = id;
	}
	
	@Override
	public String getCharacterName() {
		return name;
	}
	
	@Override
	public void setCharacterName(String name) {
		getNode().put("name", name);
		this.name = name;
	}
	
	@Override
	public String getViewId() {
		return viewId;
	}
	
	@Override
	public void setViewId(String viewId) {
		getNode().put("view-id", viewId);
		this.viewId = viewId;
	}
	
	@Override
	public Account getAccount() {
		return AccountProvider.getInstance().getAccountByProfile(this);
	}
}
