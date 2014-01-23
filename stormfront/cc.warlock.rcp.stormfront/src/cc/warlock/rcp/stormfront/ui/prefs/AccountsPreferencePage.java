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
package cc.warlock.rcp.stormfront.ui.prefs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import cc.warlock.core.settings.Account;
import cc.warlock.core.settings.AccountProvider;
import cc.warlock.core.settings.ProfileSetting;
import cc.warlock.rcp.ui.WarlockSharedImages;

public class AccountsPreferencePage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.stormfront.ui.prefs.accountsAndProfiles";
	protected ArrayList<Account> addedAccounts = new ArrayList<Account>();
	protected ArrayList<Account> removedAccounts = new ArrayList<Account>();
	protected HashMap<ProfileSetting, Account> addedProfiles = new HashMap<ProfileSetting, Account>();
	protected HashMap<ProfileSetting, Account> removedProfiles = new HashMap<ProfileSetting, Account>();
	
	protected TreeViewer accountViewer;
	protected Button removeAccount, editAccount, addProfile, removeProfile;
	
	@Override
	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Group accountGroup = new Group(main, SWT.NONE);
		accountGroup.setText("Accounts");
		accountGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		accountGroup.setLayout(new GridLayout(2, false));
		
		accountViewer = createAccountViewer(accountGroup);
		
		Composite buttonsComposite = new Composite(accountGroup, SWT.NONE);
		buttonsComposite.setLayout(new FillLayout(SWT.VERTICAL));
		buttonsComposite.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
		
		Button addAccount = new Button(buttonsComposite, SWT.PUSH);
		addAccount.setText("Add Account");
		addAccount.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_ADD));
		addAccount.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				addAccountClicked();
			}
		});
		
		removeAccount = new Button(buttonsComposite, SWT.PUSH);
		removeAccount.setText("Remove Account");
		removeAccount.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_REMOVE));
		removeAccount.setEnabled(false);
		removeAccount.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				removeAccountClicked();
			}
		});
		
		editAccount = new Button(buttonsComposite, SWT.PUSH);
		editAccount.setText("Edit Account");
		editAccount.setEnabled(false);
		editAccount.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				editAccountClicked();
			}
		});
		
		addProfile = new Button(buttonsComposite, SWT.PUSH);
		addProfile.setText("Add Profile");
		addProfile.setEnabled(false);
		addProfile.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_ADD));
		addProfile.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				addProfileClicked();
			}
		});
		
		removeProfile = new Button(buttonsComposite, SWT.PUSH);
		removeProfile.setText("Remove Profile");
		removeProfile.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_REMOVE));
		removeProfile.setEnabled(false);
		removeProfile.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				removeProfileClicked();
			}
		});
	
		updateData();
		return main;
	}
	
	protected TreeViewer createAccountViewer (Composite parent)
	{
		final TreeViewer viewer = new TreeViewer(parent, SWT.BORDER | SWT.SINGLE | SWT.H_SCROLL);
		viewer.getTree().setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, true, true));
		viewer.setContentProvider(new ITreeContentProvider() {
			public void dispose() {	}
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof Account)
				{
					return ((Account)parentElement).getProfiles().toArray();
				}
				return new Object[0];
			}
			
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof Collection<?>)
				{
					return ((Collection<?>)inputElement).toArray();
				} else if (inputElement instanceof Object[]){
					return (Object[]) inputElement;
				} else {
					return new Object[] { inputElement };
				}
			}
			
			public Object getParent(Object element) {
				if (element instanceof ProfileSetting) {
					return AccountProvider.getInstance().getAccountByProfile((ProfileSetting)element);
				}
				return null;
			}
			
			public boolean hasChildren(Object element) {
				if (element instanceof Account)
				{
					return ((Account)element).getProfiles().size() > 0;
				}
				return false;
			}
			
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
		});
		viewer.setLabelProvider(new ILabelProvider() {
			public void addListener(ILabelProviderListener listener) {}
			public void dispose() {}
			public Image getImage(Object element) {
				if (element instanceof ProfileSetting)
					return WarlockSharedImages.getImage(WarlockSharedImages.IMG_CHARACTER);
				return null;
			}
			public String getText(Object element) {
				if (element instanceof Account) {
					return ((Account)element).getAccountName();
				} else if (element instanceof ProfileSetting) {
					ProfileSetting profile = (ProfileSetting)element;
					return profile.getGameName() + " - " + profile.getCharacterName();
				}
				return "";
			}
			public boolean isLabelProperty(Object element, String property) { return true; }
			public void removeListener(ILabelProviderListener listener) {}
		});
		
		viewer.addFilter(new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof Account) {
					Account account = (Account) element;
					if (removedAccounts.contains(account))
						return false;
				} else if (element instanceof ProfileSetting) {
					ProfileSetting profile = (ProfileSetting) element;
					if (removedProfiles.containsKey(profile))
						return false;
				}
				return true;
			}
		});
		
		viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.getTree().setLinesVisible(true);
		viewer.getTree().setSize(300, viewer.getTree().getSize().y);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				if (selection.getFirstElement() instanceof Account) {
					accountSelected((Account)selection.getFirstElement());
				} else if (selection.getFirstElement() instanceof ProfileSetting) {
					profileSelected((ProfileSetting)selection.getFirstElement());
				}
			}
		});
		return viewer;
	}

	protected void updateData ()
	{
		accountViewer.setInput(AccountProvider.getInstance().getSettings());
		accountViewer.expandAll();
	}
	
	protected Account currentAccount;
	protected void accountSelected (Account account)
	{
		removeAccount.setEnabled(true);
		editAccount.setEnabled(true);
		removeProfile.setEnabled(false);
		addProfile.setEnabled(true);
		
		currentAccount = account;
		currentProfile = null;
	}
	
	protected ProfileSetting currentProfile;
	protected void profileSelected (ProfileSetting profile)
	{
		removeProfile.setEnabled(true);
		removeAccount.setEnabled(true);
		
		currentProfile = profile;
		currentAccount = AccountProvider.getInstance().getAccountByProfile(profile);
	}
	
	protected void removeProfileClicked() {
		if (currentProfile == null)
			return;
		
		removedProfiles.put(currentProfile, currentAccount);
		currentAccount.getProfileProvider().removeSetting(currentProfile);
		accountViewer.refresh();
	}

	protected void addProfileClicked() {
		if (currentAccount == null)
			return;
		
		ProfileEditDialog dialog = new ProfileEditDialog(getShell(), currentAccount);
		dialog.open();
	}

	protected void editAccountClicked() {
		if (currentAccount == null)
			return;
		
		AccountEditDialog dialog = new AccountEditDialog(getShell(), currentAccount.getAccountName(), currentAccount.getPassword());
		int response = dialog.open();
		if (response == Dialog.OK)
		{
			currentAccount.setAccountName(dialog.getUsername());
			currentAccount.setPassword(dialog.getPassword());

			accountViewer.update(currentAccount, new String[0]);
		}
	}

	protected void removeAccountClicked() {
		if (currentAccount == null)
			return;
		
		removedAccounts.add(currentAccount);
		AccountProvider.getInstance().removeSetting(currentAccount);
		
		currentAccount = null;
		currentProfile = null;
		
		removeAccount.setEnabled(false);
		removeProfile.setEnabled(false);
		
		accountViewer.refresh();
	}

	protected void addAccountClicked() {
		AccountEditDialog dialog = new AccountEditDialog(getShell());
		int response = dialog.open();
		if (response == Dialog.OK)
		{
			String username = dialog.getUsername();
			String password = dialog.getPassword();
			
			Account account = AccountProvider.getInstance().createSetting();
			account.setAccountName(username);
			account.setPassword(password);
			
			//accountViewer.add(account, new Object[0]);
			//addedAccounts.add(account);
			accountViewer.refresh();
		}
	}

	@Override
	public boolean performOk() {
		/* FIXME
		for (Account account : accounts)
		{
			if (account.needsUpdate())
			{
				ProfileProvider.instance().removeAccount(account.getOriginalAccount());
				ProfileProvider.instance().addAccount(account);
			}
		}
		*/
		
		/*
		for (Account account : removedAccounts)
		{
			
			ProfileProvider.instance().removeAccount(account.getOriginalAccount());
		}
		*/
		
		/*
		for (Account account : addedAccounts)
		{
			ProfileProvider.instance().addAccount(account);
		}
		*/
		
		AccountProvider.getInstance().flush();
		return true;
	}
}