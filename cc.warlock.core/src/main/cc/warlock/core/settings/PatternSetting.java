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
package cc.warlock.core.settings;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;

import cc.warlock.core.client.IWarlockPattern;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockPattern;

public class PatternSetting extends WarlockSetting implements IWarlockSetting, IWarlockPattern {

	private IWarlockPattern pattern;
	
	public PatternSetting (IWarlockSetting parent, String path) {
		super(parent, path);
		
		String text = getNode().get("pattern", "");
		boolean literal = getNode().getBoolean("literal", true);
		boolean caseSensitive = getNode().getBoolean("case-sensitive", false);
		boolean fullWord = getNode().getBoolean("full-word", true);
		StyleSetting style = new StyleSetting(this, "style");
		pattern = new WarlockPattern(text, literal, caseSensitive, fullWord, style);
		WarlockPreferencesScope.addPreferenceChangeListener(this.getNode(), new IPreferenceChangeListener() {
			public void preferenceChange(PreferenceChangeEvent event) {
				if(event.getKey().equals("pattern")) {
					if(!pattern.getText().equals(event.getNewValue()))
						pattern.setText((String)event.getNewValue());
				} else if(event.getKey().equals("literal")) {
					if(!Boolean.getBoolean((String)event.getNewValue()))
						pattern.setLiteral(Boolean.getBoolean((String)event.getNewValue()));
				} else if(event.getKey().equals("case-sensitive")) {
					if(!Boolean.getBoolean((String)event.getNewValue()))
						pattern.setCaseSensitive(Boolean.getBoolean((String)event.getNewValue()));
				} else if(event.getKey().equals("full-word")) {
					if(!Boolean.getBoolean((String)event.getNewValue()))
						pattern.setFullWordMatch(Boolean.getBoolean((String)event.getNewValue()));
				}
			}
		});
	}
	
	public Pattern getPattern() throws PatternSyntaxException{
		return pattern.getPattern();
	}
	
	public boolean matches(String text) {
		return pattern.matches(text);
	}
	
	public String getText() {
		return pattern.getText();
	}
	
	public void setText(String text) throws PatternSyntaxException {
		getNode().put("pattern", text);
		pattern.setText(text);
		this.notifyListenersChanged();
	}
	
	public void setLiteral (boolean literal) throws PatternSyntaxException
	{
		getNode().putBoolean("literal", literal);
		pattern.setLiteral(literal);
		this.notifyListenersChanged();
	}
	
	public void setCaseSensitive (boolean caseSensitive)
	{
		getNode().putBoolean("case-sensitive", caseSensitive);
		pattern.setCaseSensitive(caseSensitive);
		this.notifyListenersChanged();
	}
	
	public void setFullWordMatch (boolean fullWordMatch)
	{
		getNode().putBoolean("full-word", fullWordMatch);
		pattern.setFullWordMatch(fullWordMatch);
		this.notifyListenersChanged();
	}
	
	public boolean isLiteral() {
		return pattern.isLiteral();
	}
	
	public boolean isCaseSensitive() {
		return pattern.isCaseSensitive();
	}
	
	public boolean isFullWordMatch () {
		return pattern.isFullWordMatch();
	}
	
	public IWarlockStyle getStyle() {
		return this.pattern.getStyle();
	}
	
	public void setStyle(IWarlockStyle style) {
		// Not supported
	}
}
