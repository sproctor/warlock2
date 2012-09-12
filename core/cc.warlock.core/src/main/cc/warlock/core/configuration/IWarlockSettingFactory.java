package cc.warlock.core.configuration;

import org.osgi.service.prefs.Preferences;

public interface IWarlockSettingFactory {
	public IWarlockSetting createSetting (Preferences parentNode);
}
