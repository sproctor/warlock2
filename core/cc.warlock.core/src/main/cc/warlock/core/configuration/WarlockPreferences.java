package cc.warlock.core.configuration;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class WarlockPreferences {
	private static WarlockPreferences instance = new WarlockPreferences();
	protected ConfigurationScope scope = new ConfigurationScope();
	private Preferences preferences = scope.getNode("cc.warlock");
	
	protected WarlockPreferences() {
		System.out.println("Configuration location: " + scope.getLocation().toString());
	}
	
	public static WarlockPreferences getInstance() {
		return instance;
	}
	
	public Preferences getNode() {
		return preferences;
	}
	
	/*public void addNodeChangeListener(String path, INodeChangeListener listener) {
		getScope().getNode(path).addNodeChangeListener(listener);
	}*/
	
	/*public void addPreferenceChangeListener(String path, IPreferenceChangeListener listener) {
		getScope().getNode(path).addPreferenceChangeListener(listener);
	}*/
	
	public void flush() {
		try {
			preferences.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
