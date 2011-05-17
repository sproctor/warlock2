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
package cc.warlock.core.configuration;

import org.osgi.service.prefs.Preferences;

/**
 * @author Marshall
 */
public class Profile extends WarlockSetting {

	protected String id, name, gameCode, gameName;
	private Account account;
	
	public Profile (Account account, Preferences parentNode, String path) {
		super(parentNode, path);
		
		this.id = getNode().get("id", null);
		this.name = getNode().get("name", null);
		this.gameCode = getNode().get("game-code", null);
		this.gameName = getNode().get("game-name", null);
		this.account = account;
	}
	
	/**
	 * @return Returns the gameCode.
	 */
	public String getGameCode() {
		return gameCode;
	}
	/**
	 * @param gameCode The gameCode to set.
	 */
	public void setGameCode(String gameCode) {
		getNode().put("game-code", gameCode);
		this.gameCode = gameCode;
	}
	/**
	 * @return Returns the gameName.
	 */
	public String getGameName() {
		return gameName;
	}
	/**
	 * @param gameName The gameName to set.
	 */
	public void setGameName(String gameName) {
		getNode().put("game-name", gameName);
		this.gameName = gameName;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		getNode().put("id", id);
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		getNode().put("name", name);
		this.name = name;
	}
	
	public Account getAccount() {
		return account;
	}
}
