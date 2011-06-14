package cc.warlock.core.configuration;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class WarlockPreferences {
	private static WarlockPreferences instance = new WarlockPreferences();
	private ConfigurationScope scope = new ConfigurationScope();
	private IEclipsePreferences topLevel = scope.getNode("cc.warlock");
	
	protected WarlockPreferences() {
		System.out.println("Configuration location: " + scope.getLocation().toString());
	}
	
	public static WarlockPreferences getInstance() {
		return instance;
	}
	
	public IScopeContext getScope() {
		return scope;
	}
	
	public Preferences getNode() {
		return topLevel;
	}
	
	public void addNodeChangeListener(String path, INodeChangeListener listener) {
		getScope().getNode(path).addNodeChangeListener(listener);
	}
	
	public void addPreferenceChangeListener(String path, IPreferenceChangeListener listener) {
		getScope().getNode(path).addPreferenceChangeListener(listener);
	}
	
	public void flush() {
		try {
			topLevel.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
