package cc.warlock.core.client.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cc.warlock.core.client.IWarlockDialogData;
import cc.warlock.core.client.IWarlockDialogListener;

public class WarlockDialog {
	Map<String, IWarlockDialogData> elements = Collections.synchronizedMap(new HashMap<String, IWarlockDialogData>());
	ArrayList<IWarlockDialogListener> listeners = new ArrayList<IWarlockDialogListener>();
	
	public synchronized void addElement(String name, IWarlockDialogData data) {
		elements.put(name, data);
		for(IWarlockDialogListener listener : listeners) {
			listener.dialogChanged();
		}
	}
	
	public synchronized IWarlockDialogData getElement(String name) {
		return elements.get(name);
	}
	
	public synchronized Collection<IWarlockDialogData> getElements() {
		return Collections.unmodifiableCollection(elements.values());
	}
	
	public void addListener(IWarlockDialogListener listener) {
		listeners.add(listener);
	}
}
