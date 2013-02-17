package cc.warlock.core.settings;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.osgi.service.prefs.BackingStoreException;


public class WarlockPreferencesScope {
	private static WarlockPreferencesScope instance = new WarlockPreferencesScope();
	private WarlockPreferences preferences = new WarlockPreferences(ConfigurationScope.INSTANCE.getNode("cc.warlock"));
	
	protected WarlockPreferencesScope() {
		System.out.println("Configuration location: " + ConfigurationScope.INSTANCE.getLocation().toString());
		try {
			preferences.sync();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public static WarlockPreferencesScope getInstance() {
		return instance;
	}
	
	public WarlockPreferences getNode() {
		return preferences;
	}
	
	public void addNodeChangeListener(IWarlockSetting setting, INodeChangeListener listener) {
		ConfigurationScope.INSTANCE.getNode(setting.getNode().absolutePath()).addNodeChangeListener(listener);
	}
	
	public void addPreferenceChangeListener(IWarlockSetting setting, IPreferenceChangeListener listener) {
		ConfigurationScope.INSTANCE.getNode(setting.getNode().absolutePath()).addPreferenceChangeListener(listener);
	}
	
	public void flush() {
		try {
			preferences.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
