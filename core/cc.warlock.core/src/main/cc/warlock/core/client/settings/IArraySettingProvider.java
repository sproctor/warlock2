package cc.warlock.core.client.settings;

import java.util.Collection;

public interface IArraySettingProvider<T> extends IClientSettingProvider {
	public T createSetting ();
	public Collection<T> getSettings();
	public void insertSetting(int index, T string);
	public void removeSetting (T setting);
}
