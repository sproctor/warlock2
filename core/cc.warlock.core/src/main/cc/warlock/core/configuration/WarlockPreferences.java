package cc.warlock.core.configuration;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class WarlockPreferences {
	private static WarlockPreferences instance = new WarlockPreferences();
	protected ConfigurationScope scope = new ConfigurationScope();
	private Preferences preferences = scope.getNode("cc.warlock");
	
	protected WarlockPreferences() {
		System.out.println("Configuration location: " + scope.getLocation().toString());
		try {
			preferences.sync();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public static WarlockPreferences getInstance() {
		return instance;
	}
	
	public Preferences getNode() {
		return preferences;
	}
	
	public void addNodeChangeListener(IWarlockSetting setting, INodeChangeListener listener) {
		scope.getNode(setting.getNode().absolutePath()).addNodeChangeListener(listener);
	}
	
	public void addPreferenceChangeListener(IWarlockSetting setting, IPreferenceChangeListener listener) {
		scope.getNode(setting.getNode().absolutePath()).addPreferenceChangeListener(listener);
	}
	
	public void flush() {
		try {
			preferences.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
