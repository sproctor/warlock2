/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.core.client.settings.internal;

import java.util.Collection;
import java.util.HashMap;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.settings.IHighlightProvider;

public class HighlightConfigurationProvider extends ArrayConfigurationProvider<HighlightSetting> implements IHighlightProvider
{
	protected HashMap<String, IWarlockStyle> namedStyles = new HashMap<String, IWarlockStyle>();
	
	public HighlightConfigurationProvider (Preferences parentNode) {
		super(parentNode, "highlights");
	}
	
	protected HighlightSetting loadSetting(String id) {
		return new HighlightSetting(getNode(), id);
	}
	
	public IWarlockStyle getNamedStyle(String name) {
		return namedStyles.get(name);
	}

	public Collection<IWarlockStyle> getNamedStyles() {
		return namedStyles.values();
	}
	
	public void addNamedStyle (String name, IWarlockStyle style)
	{
		namedStyles.put(name, style);
	}
	
	/*public void replaceHighlightString(IHighlightString originalString,
			IHighlightString newString) {
		for(Entry<String, IHighlightString> entry : highlights.entrySet()) {
			if(entry.getValue().equals(originalString)) {
				highlights.put(entry.getKey(), newString);
				getNode().put(entry.getKey(), newString)
				break;
			}
		}
		int index = highlights.indexOf(originalString);
		if (index > -1) {
			highlights.set(index, newString);
		}
	}*/
}
