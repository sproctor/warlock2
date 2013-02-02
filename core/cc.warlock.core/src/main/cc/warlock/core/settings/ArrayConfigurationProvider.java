package cc.warlock.core.settings;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.NodeChangeEvent;
import org.osgi.service.prefs.BackingStoreException;


public abstract class ArrayConfigurationProvider<T> extends WarlockSetting implements IArraySettingProvider<T> {
	protected HashMap<String, T> settings = new HashMap<String, T>();
	//private int nextId = 0;
	
	public ArrayConfigurationProvider(IWarlockSetting parent, String path) {
		super(parent, path);
		
		try {
			for(String id : getNode().childrenNames()) {
				T setting = loadSetting(id);
				if (setting != null)
					settings.put(id, loadSetting(id));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public T createSetting ()
	{
		
		int n = 1;
		String id = Integer.toString(n);
		try {
			getNode().sync();
			while(getNode().nodeExists(id)) {
				id = Integer.toString(n);
				n++;
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
			return null;
		}
		T setting = loadSetting(id);
		if (setting == null)
			return null;
		settings.put(id, setting);
		WarlockPreferences.getInstance().addNodeChangeListener(this, new INodeChangeListener() {
			public void added(NodeChangeEvent event) {
				String id = event.getChild().name();
				if(!settings.containsKey(id)) {
					T setting = loadSetting(id);
					settings.put(id, setting);
				}
			}
			public void removed(NodeChangeEvent event) {
				String id = event.getChild().name();
				settings.remove(id);
			}
		});
		this.flush();
		this.notifyListenersChanged();
		return setting;
	}
	
	protected abstract T loadSetting(String id);
	
	public Collection<T> getSettings() {
		return Collections.unmodifiableCollection(settings.values());
	}
	
	public void insertSetting(int index, T setting) {
		String id = Integer.toString(index);
		T oldSetting = settings.remove(id);
		if(oldSetting != null) {
			getNode().remove(id);
			insertSetting(index + 1, oldSetting);
		}
		settings.put(id, setting);
		this.notifyListenersChanged();
	}
	
	public void removeSetting (T setting) {
		for(Entry<String, T> entry : settings.entrySet()) {
			if(setting.equals(entry.getValue())) {
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
	
	public void clear() {
		// Remove children
		for(Entry<String, T> entry : settings.entrySet()) {
			try {
				getNode().node(entry.getKey()).removeNode();
			} catch(BackingStoreException e) {
				e.printStackTrace();
			}
		}
		settings.clear();
	}
}
