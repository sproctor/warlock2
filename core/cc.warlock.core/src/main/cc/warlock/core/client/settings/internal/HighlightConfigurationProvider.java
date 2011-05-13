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
import java.util.Map.Entry;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.settings.IHighlightProvider;
import cc.warlock.core.client.settings.IHighlightString;

public class HighlightConfigurationProvider extends PatternConfigurationProvider implements IHighlightProvider
{
	protected HashMap<String, IHighlightString> highlights = new HashMap<String, IHighlightString>();
	protected HashMap<String, IWarlockStyle> namedStyles = new HashMap<String, IWarlockStyle>();
	
	private int nextID = 0;
	
	public HighlightConfigurationProvider (Preferences parentNode)
	{
		super(parentNode, "highlights");
		
		try {
			for(String highlightId : getNode().childrenNames()) {
				try {
					int id = Integer.parseInt(highlightId);
					if(id >= nextID)
						nextID = id + 1;
				} catch(NumberFormatException e) {
					// Don't care
				}
				highlights.put(highlightId, new HighlightSetting(getNode(), highlightId));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Collection<IHighlightString> getHighlightStrings() {
		return highlights.values();
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
	
	public HighlightSetting createHighlightString ()
	{
		HighlightSetting highlight = new HighlightSetting(getNode(), Integer.toString(nextID));
		nextID++;
		highlights.put(Integer.toString(nextID), highlight);
		
		return highlight;
	}
	
	public void insertHighlightString(int index, IHighlightString string) {
		IHighlightString oldHighlight = highlights.remove(Integer.toString(index));
		if(oldHighlight != null) {
			getNode().remove(Integer.toString(index));
			insertHighlightString(index + 1, oldHighlight);
		}
		highlights.put(Integer.toString(index), string);
	}
	
	public void removeHighlightString (IHighlightString string)
	{
		for(Entry<String, IHighlightString> entry : highlights.entrySet()) {
			if(entry.getValue().equals(string)) {
				highlights.remove(entry.getKey());
				getNode().remove(entry.getKey());
				break;
			}
		}
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
