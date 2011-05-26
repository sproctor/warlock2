package cc.warlock.core.configuration;

import cc.warlock.core.client.settings.internal.ArrayConfigurationProvider;

public class AccountProvider extends ArrayConfigurationProvider<Account> {
	private static AccountProvider instance = new AccountProvider();
	
	private AccountProvider() {
		super(WarlockPreferences.getInstance().getNode(), "accounts");
	}
	
	public static AccountProvider getInstance() {
		return instance;
	}
	
	protected Account loadSetting(String id) {
		return new Account(getNode(), id);
	}
	
	public Account getAccount(String accountName) {
		for(Account account : this.getSettings()) {
			if(account.getAccountName().equals(accountName))
				return account;
		}
		return null;
	}
}
