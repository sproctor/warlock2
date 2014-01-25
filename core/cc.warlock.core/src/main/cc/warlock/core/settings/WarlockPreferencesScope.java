package cc.warlock.core.settings;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;


public class WarlockPreferencesScope {
	
	private static final WarlockPreferencesScope instance = new WarlockPreferencesScope();
	
	private IEclipsePreferences preferences = ConfigurationScope.INSTANCE.getNode("cc.warlock");
	
	protected WarlockPreferencesScope() {
		System.out.println("Configuration location: " + ConfigurationScope.INSTANCE.getLocation());
		try {
			preferences.sync();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public static WarlockPreferencesScope getInstance() {
		return instance;
	}
	
	public IEclipsePreferences getNode() {
		return preferences;
	}
	
	public static void addNodeChangeListener(Preferences node, INodeChangeListener listener) {
		ConfigurationScope.INSTANCE.getNode(node.absolutePath()).addNodeChangeListener(listener);
	}
	
	public static void addPreferenceChangeListener(Preferences node, IPreferenceChangeListener listener) {
		ConfigurationScope.INSTANCE.getNode(node.absolutePath()).addPreferenceChangeListener(listener);
	}
	
	public void flush() {
		try {
			preferences.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
