package cc.warlock.core.settings;

import org.osgi.service.prefs.Preferences;


public class AccountProvider extends ArrayConfigurationProvider<Account> {
	private static AccountProvider instance = new AccountProvider();
	
	private AccountProvider() {
		super(null, "accounts");
	}
	
	@Override
	protected Preferences getParentNode() {
		return WarlockPreferencesScope.getInstance().getNode();
	}
	
	public static AccountProvider getInstance() {
		return instance;
	}
	
	protected Account loadSetting(String id) {
		return new Account(this, id);
	}
	
	public Account getAccount(String accountName) {
		for(Account account : this.getSettings()) {
			if(account.getAccountName().equals(accountName))
				return account;
		}
		return null;
	}
	
	public Account getAccountByProfile(ProfileSetting profile) {
		for(Account account : this.getSettings()) {
			if(account.getProfiles().contains(profile))
				return account;
		}
		return null;
	}
	
	/*public ProfileSetting getProfileByCharacterName (String characterName) {
		for (Account account : this.getSettings()) {
			ProfileSetting profile = account.getProfileProvider().getProfileByCharacterName(characterName);
			if (profile != null)
				return profile;
		}
		return null;
	}*/
	
	public ProfileSetting getProfileByViewId (String viewId) {
		for (Account account : this.getSettings()) {
			ProfileSetting profile = account.getProfileProvider().getProfileByViewId(viewId);
			if (profile != null)
				return profile;
		}
		return null;
	}
}
