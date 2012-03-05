package cc.warlock.core.client.settings.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.configuration.IArraySettingProvider;
import cc.warlock.core.configuration.WarlockSetting;

public abstract class ArrayConfigurationProvider<T> extends WarlockSetting implements IArraySettingProvider<T> {
	protected HashMap<String, T> settings = new HashMap<String, T>();
	private int nextId = 0;
	
	public ArrayConfigurationProvider(Preferences parentNode, String path) {
		super(parentNode, path);
		
		try {
			for(String id : getNode().childrenNames()) {
				try {
					int n = Integer.parseInt(id);
					if(n >= nextId)
						nextId = n + 1;
				} catch(NumberFormatException e) {
					// Don't care
				}
				settings.put(id, loadSetting(id));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public T createSetting ()
	{
		String id = Integer.toString(nextId);
		T setting = loadSetting(id);
		settings.put(id, setting);
		nextId++;
		this.notifyListenersChanged();
		return setting;
	}
	
	protected abstract T loadSetting(String id);
	
	public Collection<T> getSettings() {
		return Collections.unmodifiableCollection(settings.values());
	}
	
	public void insertSetting(int index, T string) {
		String id = Integer.toString(index);
		T oldSetting = settings.remove(id);
		if(oldSetting != null) {
			getNode().remove(id);
			insertSetting(index + 1, oldSetting);
		}
		settings.put(id, string);
		this.notifyListenersChanged();
	}
	
	public void removeSetting (T setting) {
		for(Entry<String, T> entry : settings.entrySet()) {
			if(entry.getValue() == setting) {
				settings.remove(entry.getKey());
				try {
					getNode().node(entry.getKey()).removeNode();
				} catch(BackingStoreException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		this.notifyListenersChanged();
	}
	
}
