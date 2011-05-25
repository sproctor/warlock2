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
}
