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
 * Created on Sep 20, 2004
 */
package cc.warlock.core.stormfront.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import cc.warlock.core.network.IConnection;
import cc.warlock.core.network.IConnectionListener;
import cc.warlock.core.network.LineConnection;
import cc.warlock.core.settings.Account;
import cc.warlock.core.settings.AccountProvider;
import cc.warlock.core.settings.ProfileSetting;

/**
 * @author Marshall
 */
public class SGEConnection extends LineConnection implements IConnectionListener {

	public static final String SGE_SERVER = "eaccess.play.net";
	public static final int SGE_PORT = 7900;

	public static final int INVALID_PASSWORD = 0;
	public static final int INVALID_ACCOUNT = 1;
	public static final int ACCOUNT_REJECTED = 2;
	public static final int LOGIN_SUCCESS = 3;
	public static final int ACCOUNT_EXPIRED = 4;
	
	public static final String PROPERTY_KEY = "KEY";
	public static final String PROPERTY_GAMEHOST = "GAMEHOST";
	public static final String PROPERTY_GAMEPORT = "GAMEPORT";
	
	public static final String NEW_CHARACTER_CODE = "0";
	
	protected static final int SGE_NONE = 0;
	protected static final int SGE_INITIAL = 1;
	protected static final int SGE_ACCOUNT = 2;
	protected static final int SGE_MENU = 3;
	protected static final int SGE_GAME = 4;
	protected static final int SGE_PICK = 5;
	protected static final int SGE_CHARACTERS = 6;
	protected static final int SGE_LOAD = 7;
	
	protected static final int LOGIN_READY = 0;
	protected static final int LOGIN_FINISHED = 1;
	protected static final int GAMES_READY = 2;
	protected static final int CHARACTERS_READY = 3;
	protected static final int READY_TO_PLAY = 4;
	protected static final int SGE_ERROR = 5;
	
	protected int state, errorCode;
	protected String passwordHash;
	protected ArrayList<ISGEConnectionListener> sgeListeners;
	private boolean retrieveGameInfo = true;
	
	protected HashMap<String, String> characters, loginProperties;
	protected ArrayList<SGEGame> games;
	
	protected ListIterator<SGEGame> gameIterator;
	protected SGEGame currentGame;
	//protected boolean retrievingGames = false;
	
	public SGEConnection ()
	{
		super();
		addConnectionListener(this);
		
		state = SGE_NONE;
		sgeListeners = new ArrayList<ISGEConnectionListener>();
		games = new ArrayList<SGEGame>();
		characters = new HashMap<String, String>();
		loginProperties = new HashMap<String, String>();
	}
	
	protected void resetState () {
		state = SGE_NONE;
		passwordHash = null;
		games.clear();
		characters.clear();
		loginProperties.clear();
	}
	
	public void connect ()
	{
		try {
			if (!connected) {
				resetState();
				connect (SGE_SERVER, SGE_PORT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void addSGEConnectionListener (ISGEConnectionListener listener)
	{
		sgeListeners.add (listener);
	}
	
	public void login (String username, String password)
	{
		if(username == null || password == null) {
			resetState();
			errorCode = INVALID_PASSWORD;
			fireEvent(SGE_ERROR);
			return;
		}
		
		state = SGE_ACCOUNT;
		
		// This is an ugly hack. If we try to append the encrypted password as a "String", the java language String will
		// try interpreting the characters in that string as Unicode, and fail miserably, giving us a botched encrypted string.
		// The only way to make this work the right way is manually cast each and every "char" primitive to "byte" and send it
		// raw over the socket.
		
		try {
			byte usernameBytes[] = username.getBytes();
			byte bytes[] = new byte[2 + usernameBytes.length + 1 + password.length() + 1];
			bytes[0] = (byte) 'A'; bytes[1] = (byte) '\t';
			
			System.arraycopy(usernameBytes, 0, bytes, 2, usernameBytes.length);
			bytes[usernameBytes.length + 2] = (byte)'\t';
			
			byte encrypted[] = encryptPassword(password, passwordHash);
			for (int i = 0; i < password.length(); i++)
			{
				int index = usernameBytes.length + 3 + i;
				bytes[index] = encrypted[i];
			}
			
			bytes[bytes.length - 1] = (byte) '\n';
			send (bytes);
			
		} catch (IOException e) {
			e.printStackTrace();
			resetState();
			fireEvent(SGE_ERROR);
		}
	}
	
	protected byte[] encryptPassword (String password, String hash)
	{
		byte encrypted[] = new byte[33];
		for (int i = 0; i < 32 && password.length() > i  && hash.length() > i; i++)
		{
			encrypted[i] = (byte) ((hash.charAt(i)  ^ (password.charAt(i) - 32)) + 32);
		}
		
		return encrypted;
	}
	
	public void selectGame (String gameCode)
	{
		state = SGE_GAME;
		
		try {
			sendLine("G\t" +gameCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void selectCharacter (String characterCode)
	{
		state = SGE_CHARACTERS;
		
		try {
			sendLine("L\t"+characterCode+"\tSTORM");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connected(IConnection connection) {
		try {
			connection.sendLine("K");
			state = SGE_INITIAL;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class SGEGame implements ISGEGame {
		public AccountStatus accountStatus;
		public int interval;
		public String gameCode, gameName;
		private boolean usable = true;
		public HashMap<GameURL, String> gameURLs = new HashMap<GameURL, String>();
		
		public AccountStatus getAccountStatus() {
			return accountStatus;
		}
		
		public int getAccountStatusInterval() {
			return interval;
		}
		
		public String getGameCode() {
			return gameCode;
		}
		
		public String getGameName() {
			return gameName;
		}
		
		public String getGameURL(GameURL url) {
			if (!gameURLs.containsKey(url)) {
				return null;
			}
			
			String gameUrl = gameURLs.get(url);
			if (gameUrl.indexOf("http://") == -1
				&& gameUrl.indexOf("https://") == -1)
			{
				String root = gameURLs.get(GameURL.Root);
				return "http://www.play.net/" + root + "/" + gameUrl;
			} else {
				return gameUrl;
			}
		}
		
		public void setUsable(boolean u) {
			usable = u;
		}
		
		public boolean isUsable() {
			return usable;
		}
	}
	
	@Override
	public void dataReady(IConnection connection, String line) {
		try {
			
			//System.out.println("SGE: " + line);
			
			if (state == SGE_INITIAL)
			{
				passwordHash = line;
				fireEvent (LOGIN_READY);
				return;
			}
			
			switch (line.charAt(0))
			{
				/* response from user/pass sending */
				case 'A':
				{
					if (line.indexOf("REJECT") != -1)
					{
						errorCode = ACCOUNT_REJECTED;
					}
					else if (line.indexOf("PASSWORD") != -1)
					{
						errorCode = INVALID_PASSWORD;
					}
					else if (line.indexOf("NORECORD") != -1)
					{
						errorCode = INVALID_ACCOUNT;
					}
					else errorCode = LOGIN_SUCCESS;

					
					if (errorCode == LOGIN_SUCCESS) {
						sendLine("M");
						state = SGE_GAME;

						fireEvent (LOGIN_FINISHED);
					}
					else {
						fireEvent (SGE_ERROR);
					}

				} break;
				
				/* Server is giving us a list of games */
				case 'M':
				{
					games.clear();
					String tokens[] = line.split("\t");
					for (int i = 1; i < tokens.length; i+=2)
					{
						SGEGame game = new SGEGame();
						game.gameCode = tokens[i];
						game.gameName = tokens[i+1];
						
						games.add(game);
					}
					
					if (retrieveGameInfo)
					{
						//retrievingGames = true;
						gameIterator = games.listIterator();
						if (gameIterator.hasNext())
						{
							currentGame = gameIterator.next();
							sendLine("N\t" + currentGame.gameCode);
						}
					} else {
						fireEvent(GAMES_READY);
					}
					
				} break;
				
				// Server is responding with game details
				case 'N':
					// require production and storm
					if(!line.contains("PRODUCTION") || !line.contains("STORM"))
						currentGame.setUsable(false);
					if (gameIterator.hasNext()) {
						currentGame = gameIterator.next();
						sendLine("N\t" + currentGame.gameCode);
					} else {
						fireEvent(GAMES_READY);
					}
					break;
					
				/* Server is responding with Game Details */
				case 'G':
				{
					/* TODO: We should check this, it could be useful.
					String tokens[] = line.split("\t");
					if ("NORMAL".equals(tokens[2])) {
						currentGame.accountStatus = AccountStatus.Normal;
					} else if ("TRIAL".equals(tokens[2])) {
						currentGame.accountStatus = AccountStatus.Trial;
					} else if ("EXPIRED".equals(tokens[2])) {
						currentGame.accountStatus = AccountStatus.Expired;
					} else {
						currentGame.accountStatus = AccountStatus.Unknown;
					}

					int startIndex = 3;
					if (currentGame.accountStatus != AccountStatus.Unknown)
					{
						startIndex = 4;
						currentGame.interval = Integer.parseInt(tokens[3]);
					}

					for (int i = startIndex; i < tokens.length; i++)
					{
						if (tokens[i].indexOf("=") != -1)
						{
							String keyval[] = tokens[i].split("=");

							GameURL url = GameURL.getURL(keyval[0]);
							if (url != null) {
								currentGame.gameURLs.put(url, keyval[1]);
							}
						}
					}*/

					sendLine("C");
				}
				break;
				
				/* Server is giving us a list of characters in that game */
				case 'C':
				{
					characters.clear();
					String tokens[] = line.split("\t");
					for (int i = 5; i < tokens.length; i+=2)
					{
						characters.put(tokens[i], tokens[i+1]);
					}
					fireEvent (CHARACTERS_READY);
				} break;
				
				/* Server is responding to a request to play a character */
				case 'L':
				{
					String tokens[] = line.split("\t");
					String loginResponse = tokens[1];
					if ("OK".equals(loginResponse))
					{
						for (int i = 2; i < tokens.length; i++)
						{
							String property[] = tokens[i].split("=");
							loginProperties.put(property[0], property[1]);
						}

						fireEvent(READY_TO_PLAY);
					}
					else if ("PROBLEM".equals(loginResponse))
					{
						errorCode = ACCOUNT_EXPIRED;
						fireEvent(SGE_ERROR);
					}
					
					disconnect();
				} break;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void fireEvent (final int event)
	{
		for (ISGEConnectionListener listener : sgeListeners) {
			switch (event) {
			case LOGIN_READY:
				listener.loginReady(SGEConnection.this);
				break;
			case LOGIN_FINISHED:
				listener.loginFinished(SGEConnection.this);
				break;
			case GAMES_READY:
				listener.gamesReady(SGEConnection.this, games);
				break;
			case CHARACTERS_READY:
				listener.charactersReady(SGEConnection.this, characters);
				break;
			case READY_TO_PLAY:
				listener.readyToPlay(SGEConnection.this, loginProperties);
				break;
			case SGE_ERROR:
				listener.sgeError(SGEConnection.this, errorCode);
				break;
			}
		}	
	}
	
	public void disconnected(IConnection connection) {
	}
	
	private static class AutoLoginListener implements ISGEConnectionListener
	{
		public ProfileSetting profile;
		public boolean loggedIn = false;
		public Map <String,String> properties = null;
		
		@Override
		public void loginReady(SGEConnection connection) {
			Account account = AccountProvider.getInstance().getAccountByProfile(profile);
			connection.login(account.getAccountName(), account.getPassword());
		}
		
		@Override
		public void gamesReady(SGEConnection connection,
				List<? extends ISGEGame> games) {
			connection.selectGame(profile.getGameCode());
		}
		
		@Override
		public void charactersReady(SGEConnection connection,
				Map<String, String> characters) {
			connection.selectCharacter(profile.getCharacterId());
		}
		
		@Override
		public void readyToPlay(SGEConnection connection,
				Map<String, String> loginProperties) {
			this.properties = loginProperties;
			this.loggedIn = true;
		}

		@Override
		public void loginFinished(SGEConnection connection) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sgeError(SGEConnection connection, int errorCode) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public static Map<String, String> autoLogin (ProfileSetting profile, ISGEConnectionListener extraListener)
	{
		SGEConnection connection = new SGEConnection();
		connection.setRetrieveGameInfo(false);
		AutoLoginListener listener = new AutoLoginListener();
		listener.profile = profile;
		
		connection.addSGEConnectionListener(listener);
		if (extraListener != null) {
			connection.addSGEConnectionListener(extraListener);
		}
			
		connection.connect();
		while (!listener.loggedIn) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return listener.properties;
	}

	public void setRetrieveGameInfo(boolean retrieveGameInfo) {
		this.retrieveGameInfo = retrieveGameInfo;
	}

	public void connectionError(IConnection connection, ErrorType errorType) {	
	}

	@Override
	public void dataSent(IConnection connection, String data) {
		// TODO Auto-generated method stub
		
	}
	
	/*@Override
	public void sendLine(String line) throws IOException {
		System.out.println("SGE out: " + line);
		super.sendLine(line);
	}*/
	
	@Override
	public void passThrough() {
		// makes no sense for us
	}
}
