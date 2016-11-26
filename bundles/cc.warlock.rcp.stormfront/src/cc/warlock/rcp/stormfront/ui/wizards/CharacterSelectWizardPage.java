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
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.stormfront.ui.wizards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import cc.warlock.core.client.IProfile;
import cc.warlock.core.client.internal.Profile;
import cc.warlock.core.settings.Account;
import cc.warlock.core.settings.ProfileSetting;
import cc.warlock.core.stormfront.network.ISGEConnectionListener;
import cc.warlock.core.stormfront.network.ISGEGame;
import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.rcp.stormfront.adapters.SWTSGEConnectionListenerAdapter;
import cc.warlock.rcp.stormfront.ui.util.LoginUtil;
import cc.warlock.rcp.stormfront.ui.views.StormFrontGameView;
import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.ui.WarlockWizardDialog;

/**
 * @author Marshall
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class CharacterSelectWizardPage extends WizardPage {

	private Table characters;
	private Map<String, String> characterMap;
	private TableViewer characterViewer;
	private String selectedCharacterCode;
	private Listener listener;
	private boolean complete;

	public CharacterSelectWizardPage(SGEConnection connection) {
		super("Select a character.", "Please select a character,", WarlockSharedImages
				.getImageDescriptor(WarlockSharedImages.IMG_WIZBAN_WARLOCK));
		complete = false;

		this.characterMap = new HashMap<String, String>();

		listener = new Listener();
		connection.addSGEConnectionListener(new SWTSGEConnectionListenerAdapter(listener));
		System.out.println("CharacterSelectWizardPage constructor");
	}

	public void createControl(Composite parent) {
		Composite controls = new Composite(parent, SWT.NONE);
		FillLayout layout = new FillLayout();
		controls.setLayout(layout);

		characters = new Table(controls, SWT.BORDER | SWT.SINGLE);
		characters.setEnabled(false);

		characterViewer = new TableViewer(characters);
		characterViewer.setContentProvider(new CharacterContentProvider());
		characterViewer.setLabelProvider(new CharacterLabelProvider());
		characterViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				characterSelected(event.getSelection());
				complete = true;
				getWizard().getContainer().updateButtons();
			}
		});

		characterViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				characterSelected(characterViewer.getSelection());

				complete = true;
				if (getWizard().getContainer() instanceof WarlockWizardDialog) {
					WarlockWizardDialog dialog = (WarlockWizardDialog) getWizard().getContainer();
					dialog.finish();
				}
			}
		});
		setControl(controls);
	}

	private class CharacterContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Map<?, ?>) {
				Map<?, ?> characterMap = (Map<?, ?>) inputElement;
				return characterMap.keySet().toArray();
			}
			return null;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	private class CharacterLabelProvider extends LabelProvider {

		public String getText(Object element) {
			return (String) characterMap.get(element);
		}

		public Image getImage(Object element) {
			if (element.equals(SGEConnection.NEW_CHARACTER_CODE)) {
				return WarlockSharedImages.getImage(WarlockSharedImages.IMG_NEW_CHARACTER);
			} else {
				return WarlockSharedImages.getImage(WarlockSharedImages.IMG_CHARACTER);
			}
		}
	}

	private void characterSelected(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection selection2 = (IStructuredSelection) selection;
			selectedCharacterCode = (String) selection2.getFirstElement();
		}
	}

	/**
	 * @return Returns the selectedCharacterCode.
	 */
	public String getSelectedCharacterCode() {
		return selectedCharacterCode;
	}

	public String getSelectedCharacterName() {
		return (String) characterMap.get(selectedCharacterCode);
	}

	public Listener getListener() {
		return listener;
	}

	public boolean isPageComplete() {
		return complete;
	}

	public class Listener implements ISGEConnectionListener {
		private IProgressMonitor monitor;

		public void setProgressMonitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

		public void charactersReady(SGEConnection connection, Map<String, String> characters) {
			// CharacterSelectWizardPage.this.characterMap.clear();
			CharacterSelectWizardPage.this.characterMap = characters;
			characterMap.put(SGEConnection.NEW_CHARACTER_CODE, "Create a new character");

			characterViewer.setInput(characters);
			CharacterSelectWizardPage.this.characters.setEnabled(true);
		}

		public void readyToPlay(SGEConnection connection, Map<String, String> loginProperties) {
			if (monitor != null) {
				monitor.worked(1);
				// monitor.done();
			}

			GameSelectWizardPage gameSelectPage = (GameSelectWizardPage) getPreviousPage();
			AccountWizardPage accountPage = (AccountWizardPage) getPreviousPage().getPreviousPage();

			Account account = accountPage.getSavedAccount();
			IProfile profile = null;
			if (account != null && !getSelectedCharacterCode().equals(SGEConnection.NEW_CHARACTER_CODE)) {
				for (ProfileSetting curProfile : account.getProfiles()) {
					if (selectedCharacterCode.equals(curProfile.getCharacterId())
							&& gameSelectPage.getSelectedGameCode().equals(curProfile.getGameCode())) {
						profile = curProfile;
						break;
					}
				}
				
				if (profile == null) {
					boolean response = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
							"Save Profile?", "Would you like to save the character \""
									+ getSelectedCharacterName() + "\" as a new profile?");

					if (response) {
						profile = account.getProfileProvider().createProfile(getSelectedCharacterCode(),
								getSelectedCharacterName(), gameSelectPage.getSelectedGameCode(),
								gameSelectPage.getSelectedGameName());
						account.getProfileProvider().flush();
					} else {
						profile = new Profile(account, getSelectedCharacterCode(),
								getSelectedCharacterName(), gameSelectPage.getSelectedGameCode(),
								gameSelectPage.getSelectedGameName());
					}
				} // TODO else update profile
			}

			StormFrontGameView gameView = LoginUtil.connectAndOpenGameView(loginProperties, getSelectedCharacterName());
			gameView.setProfile(profile);
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
		public void sgeError(SGEConnection connection, int errorCode) {
			// TODO Auto-generated method stub
			
		}
	}

}
