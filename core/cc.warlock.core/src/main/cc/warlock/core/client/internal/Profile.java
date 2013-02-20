package cc.warlock.core.client.internal;

import cc.warlock.core.client.IProfile;
import cc.warlock.core.settings.Account;

public class Profile implements IProfile {
	private String gameCode;
	private String gameName;
	private String characterId;
	private String name;
	private String viewId;
	private Account account;

	public Profile(Account account, String characterId, String name, String gameCode, String gameName) {
		this.characterId = characterId;
		this.name = name;
		this.gameCode = gameCode;
		this.gameName = gameName;
		this.account = account;
	}
	
	@Override
	public String getGameCode() {
		return gameCode;
	}

	@Override
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	@Override
	public String getGameName() {
		return gameName;
	}

	@Override
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	@Override
	public String getCharacterId() {
		return characterId;
	}

	@Override
	public void setCharacterId(String id) {
		this.characterId = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getViewId() {
		return viewId;
	}

	@Override
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	@Override
	public Account getAccount() {
		return account;
	}
}
