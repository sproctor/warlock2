package cc.warlock.core.settings;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferenceNodeVisitor;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class WarlockPreferences implements IEclipsePreferences {
	private Preferences preferences;

	public WarlockPreferences(Preferences preferences) {
		this.preferences = preferences;
	}
	
	@Override
	public void put(String key, String value) {
		try {
			preferences.sync();
			preferences.put(key, value);
			preferences.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String get(String key, String def) {
		try {
			preferences.sync();
			return preferences.get(key, def);
		} catch(BackingStoreException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void remove(String key) {
		try {
			preferences.sync();
			preferences.remove(key);
			preferences.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void clear() throws BackingStoreException {
		preferences.sync();
		preferences.clear();
		preferences.flush();
	}

	@Override
	public void putInt(String key, int value) {
		try {
			preferences.sync();
			preferences.putInt(key, value);
			preferences.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getInt(String key, int def) {
		try {
			preferences.sync();
			return preferences.getInt(key, def);
		} catch(BackingStoreException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public void putLong(String key, long value) {
		try {
			preferences.sync();
			preferences.putLong(key, value);
			preferences.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getLong(String key, long def) {
		try {
			preferences.sync();
			return preferences.getLong(key, def);
		} catch(BackingStoreException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public void putBoolean(String key, boolean value) {
		try {
			preferences.sync();
			preferences.putBoolean(key, value);
			preferences.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean getBoolean(String key, boolean def) {
		try {
			preferences.sync();
			return preferences.getBoolean(key, def);
		} catch(BackingStoreException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public void putFloat(String key, float value) {
		try {
			preferences.sync();
			preferences.putFloat(key, value);
			preferences.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public float getFloat(String key, float def) {
		try {
			preferences.sync();
			return preferences.getFloat(key, def);
		} catch(BackingStoreException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public void putDouble(String key, double value) {
		try {
			preferences.sync();
			preferences.putDouble(key, value);
			preferences.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public double getDouble(String key, double def) {
		try {
			preferences.sync();
			return preferences.getDouble(key, def);
		} catch(BackingStoreException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public void putByteArray(String key, byte[] value) {
		try {
			preferences.sync();
			preferences.putByteArray(key, value);
			preferences.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getByteArray(String key, byte[] def) {
		try {
			preferences.sync();
			return preferences.getByteArray(key, def);
		} catch(BackingStoreException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public String[] keys() throws BackingStoreException {
		preferences.sync();
		return preferences.keys();
	}

	@Override
	public String[] childrenNames() throws BackingStoreException {
		preferences.sync();
		return preferences.childrenNames();
	}

	@Override
	public Preferences parent() {
		try {
			preferences.sync();
			return new WarlockPreferences(preferences.parent());
		} catch(BackingStoreException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean nodeExists(String pathName) throws BackingStoreException {
		preferences.sync();
		return preferences.nodeExists(pathName);
	}

	@Override
	public String name() {
		return preferences.name();
	}

	@Override
	public String absolutePath() {
		return preferences.absolutePath();
	}

	@Override
	public void flush() throws BackingStoreException {
		preferences.sync();
		preferences.flush();
	}

	@Override
	public void sync() throws BackingStoreException {
		preferences.sync();
	}

	@Override
	public void addNodeChangeListener(INodeChangeListener listener) {
		if(preferences instanceof IEclipsePreferences) {
			((IEclipsePreferences)preferences).addNodeChangeListener(listener);
		}
	}

	@Override
	public void removeNodeChangeListener(INodeChangeListener listener) {
		if(preferences instanceof IEclipsePreferences) {
			((IEclipsePreferences)preferences).removeNodeChangeListener(listener);
		}
	}

	@Override
	public void addPreferenceChangeListener(IPreferenceChangeListener listener) {
		if(preferences instanceof IEclipsePreferences) {
			((IEclipsePreferences)preferences).addPreferenceChangeListener(listener);
		}
	}

	@Override
	public void removePreferenceChangeListener(
			IPreferenceChangeListener listener) {
		if(preferences instanceof IEclipsePreferences) {
			((IEclipsePreferences)preferences).removePreferenceChangeListener(listener);
		}
	}

	@Override
	public void removeNode() throws BackingStoreException {
		preferences.sync();
		Preferences parent = preferences.parent();
		preferences.removeNode();
		parent.flush();
	}

	@Override
	public Preferences node(String path) {
		return new WarlockPreferences(preferences.node(path));
	}

	@Override
	public void accept(IPreferenceNodeVisitor visitor)
			throws BackingStoreException {
		if(preferences instanceof IEclipsePreferences) {
			((IEclipsePreferences)preferences).accept(visitor);
		}
	}

	public void move(String path) {
		try {
			preferences.sync();
			Preferences newPrefs = preferences.parent().node(path);
			for(String key : preferences.keys()) {
				newPrefs.put(key, preferences.get(key, null));
			}
			for(String name : preferences.childrenNames()) {
				new WarlockPreferences(preferences.node(name)).reparent(newPrefs);
			}
			preferences.flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	private void reparent(Preferences parent) {
		try {
			Preferences newPrefs = parent.node(preferences.name());
			for(String key : preferences.keys()) {
				newPrefs.put(key, preferences.get(key, null));
			}
			for(String name : preferences.childrenNames()) {
				new WarlockPreferences(preferences.node(name)).reparent(newPrefs);
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
