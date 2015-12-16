package cc.warlock.core.client;

import cc.warlock.core.settings.Account;

public interface IProfile {
	public String getGameCode();
	public void setGameCode(String gameCode);
	public String getGameName();
	public void setGameName(String gameName);
	public String getCharacterId();
	public void setCharacterId(String id);
	public String getCharacterName();
	public void setCharacterName(String name);
	public String getViewId();
	public void setViewId(String viewId);
	public Account getAccount();
}
