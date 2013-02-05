package cc.warlock.core.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.NodeChangeEvent;
import org.osgi.service.prefs.BackingStoreException;


public abstract class ArrayConfigurationProvider<T extends IWarlockSetting> extends WarlockSetting implements IArraySettingProvider<T> {
	private ArrayList<T> settings = new ArrayList<T>();
	//private int nextId = 0;
	
	public ArrayConfigurationProvider(IWarlockSetting parent, String path) {
		super(parent, path);
		
		try {
			for(String name : getNode().childrenNames()) {
				try {
					int id = Integer.parseInt(name);
					T setting = loadSetting(name);
					if (setting != null)
						add(id, setting);
				} catch(NumberFormatException e) {
					// Don't load the setting
				}
			}
			this.compact();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	private void compact() {
		Iterator<T> iter = settings.iterator();
		int id = 0;
		int n = 0;
		while(iter.hasNext()) {
			T curr = iter.next();
			if(curr != null) {
				if(n != id)
					curr.getNode().move(Integer.toString(n));
				n++;
			}
			id++;
		}
	}
	
	private void add(int id, T setting) {
		while(id > settings.size()) {
			settings.add(null);
		}
		if(id < settings.size() && settings.get(id) == null)
			settings.remove(id);
		settings.add(id, setting);
	}
	
	public T createSetting ()
	{
		int id = 0;
		String name = Integer.toString(id);
		try {
			while(getNode().nodeExists(name)) {
				id++;
				name = Integer.toString(id);
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
			return null;
		}
		T setting = loadSetting(name);
		if (setting == null)
			return null;
		add(id, setting);
		WarlockPreferencesScope.getInstance().addNodeChangeListener(this, new INodeChangeListener() {
			public void added(NodeChangeEvent event) {
				String name = event.getChild().name();
				int id = Integer.parseInt(name);
				if(id >= settings.size() || settings.get(id) == null) {
					T setting = loadSetting(name);
					settings.add(id, setting);
				}
			}
			public void removed(NodeChangeEvent event) {
				String id = event.getChild().name();
				settings.remove(id);
			}
		});
		this.notifyListenersChanged();
		return setting;
	}
	
	protected abstract T loadSetting(String id);
	
	public Collection<T> getSettings() {
		return Collections.unmodifiableCollection(settings);
	}
	
	public void insertSetting(int index, T setting) {
		String name = Integer.toString(index);
		if(index < settings.size()) {
			T oldSetting = settings.get(index);
			if(oldSetting != null) {
				getNode().remove(name);
				insertSetting(index + 1, oldSetting);
			}
			settings.remove(index);
		}
		add(index, setting);
		this.notifyListenersChanged();
	}
	
	public void removeSetting (T setting) {
		Iterator<T> iter = settings.iterator();
		int i = 0;
		while(iter.hasNext()) {
			T curr = iter.next();
			if(setting.equals(curr)) {
				iter.remove();
				try {
					String id = Integer.toString(i);
					getNode().node(id).removeNode();
				} catch(BackingStoreException e) {
					e.printStackTrace();
				}
				break;
			}
			i++;
		}
		this.compact();
		this.notifyListenersChanged();
	}
	
	public void clear() {
		// Remove children
		try {
			for(String id : getNode().childrenNames()) {
				getNode().node(id).removeNode();
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
		settings.clear();
	}
}
