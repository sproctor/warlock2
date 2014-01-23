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
package cc.warlock.rcp.stormfront.ui.actions;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IProfile;
import cc.warlock.core.network.IConnection;
import cc.warlock.core.network.IConnection.ErrorType;
import cc.warlock.core.network.IConnectionListener;
import cc.warlock.core.settings.Account;
import cc.warlock.core.settings.AccountProvider;
import cc.warlock.core.stormfront.network.ISGEConnectionListener;
import cc.warlock.core.stormfront.network.ISGEGame;
import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.stormfront.adapters.SWTSGEConnectionListenerAdapter;
import cc.warlock.rcp.stormfront.ui.StormFrontSharedImages;
import cc.warlock.rcp.stormfront.ui.util.LoginUtil;
import cc.warlock.rcp.stormfront.ui.views.StormFrontGameView;
import cc.warlock.rcp.ui.network.SWTConnectionListenerAdapter;

public class ProfileConnectAction extends Action implements ISGEConnectionListener, IConnectionListener {
	private IProfile profile;
	private IProgressMonitor monitor;
	private boolean finished;
	private IStatus status;
	private StormFrontGameView gameView;

	public ProfileConnectAction(IProfile profile) {
		super(profile.getCharacterName(), StormFrontSharedImages.getImageDescriptor(StormFrontSharedImages.IMG_CHARACTER));
		setDescription(profile.getGameName() + " character \"" + profile.getCharacterName() + "\"");

		this.profile = profile;
	}

	@Override
	public void run() {
		finished = false;
		status = Status.OK_STATUS;

		Job connectJob = new Job("Logging into profile \"" + profile.getCharacterName() + "\"...") {
			protected IStatus run(IProgressMonitor monitor) {
				ProfileConnectAction.this.monitor = monitor;

				SGEConnection connection = new SGEConnection();
				connection.setRetrieveGameInfo(false);
				connection.addConnectionListener(new SWTConnectionListenerAdapter(ProfileConnectAction.this));
				connection.addSGEConnectionListener(new SWTSGEConnectionListenerAdapter(ProfileConnectAction.this));
				monitor.beginTask("Logging into profile \"" + profile.getCharacterName() + "\"...", 5);

				connection.connect();

				while (!ProfileConnectAction.this.finished) {
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							Display.getDefault().readAndDispatch();
						}
					});
				}

				return status;
			}
		};

		connectJob.setUser(true);
		connectJob.schedule();
	}

	public void loginReady(SGEConnection connection) {
		monitor.worked(1);

		if (!monitor.isCanceled()) {
			Account account = profile.getAccount();
			connection.login(account.getAccountName(), account.getPassword());
		} else {
			status = Status.CANCEL_STATUS;
			finished = true;
		}
	}

	public void loginFinished(SGEConnection connection) {
		monitor.worked(1);
	}

	public void sgeError(SGEConnection connection, int errorCode) {
		LoginUtil.showAuthenticationError(errorCode);
		this.status = new Status(IStatus.ERROR, Warlock2Plugin.PLUGIN_ID, LoginUtil.getAuthenticationError(errorCode));
		finished = true;
	}

	public void gamesReady(SGEConnection connection, List<? extends ISGEGame> games) {
		monitor.worked(1);

		if (!monitor.isCanceled()) {
			connection.selectGame(profile.getGameCode());
		} else {
			status = Status.CANCEL_STATUS;
			finished = true;
		}
	}

	public void charactersReady(SGEConnection connection, Map<String, String> characters) {
		monitor.worked(1);

		connection.selectCharacter(profile.getCharacterId());
	}

	public void readyToPlay(SGEConnection connection, Map<String, String> loginProperties) {
		monitor.worked(1);
		monitor.done();

		if (!monitor.isCanceled()) {
			// Check to see if there is an open gameView, and if it's connected.
			// If it is, open a new one.
			if (gameView == null) {
				gameView = LoginUtil.connectAndOpenGameView(loginProperties, profile.getCharacterName());
				gameView.setProfile(profile);
			} else {
				LoginUtil.connect(gameView, loginProperties);
				gameView.setProfile(profile);
			}
			IProfile curProfile;
			while ((curProfile = AccountProvider.getInstance().getProfileByViewId(gameView.getViewId())) != null) {
				curProfile.setViewId(null);
			}
			profile.setViewId(gameView.getViewId());
		} else {
			status = Status.CANCEL_STATUS;
		}

		finished = true;
	}

	public void setGameView(StormFrontGameView gameView) {
		this.gameView = gameView;
	}

	public IProfile getProfile() {
		return profile;
	}

	public void connectionError(IConnection connection, ErrorType errorType) {
		LoginUtil.showConnectionError(errorType);
	}

	@Override
	public void connected(IConnection connection) {
	}

	@Override
	public void dataReady(IConnection connection, String data) {
	}

	@Override
	public void disconnected(IConnection connection) {
	}

	@Override
	public void dataSent(IConnection connection, String data) {
	}
}
