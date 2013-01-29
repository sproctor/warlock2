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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;

import cc.warlock.core.client.IWarlockPattern;
import cc.warlock.core.client.internal.WarlockPattern;
import cc.warlock.core.configuration.IWarlockSetting;
import cc.warlock.core.configuration.WarlockPreferences;
import cc.warlock.core.configuration.WarlockSetting;

abstract public class AbstractPatternSetting<T extends WarlockPattern> extends WarlockSetting implements IWarlockSetting, IWarlockPattern {

	protected T pattern;
	
	public AbstractPatternSetting (IWarlockSetting parent, String path) {
		super(parent, path);
		
		String text = getNode().get("pattern", "");
		boolean literal = getNode().getBoolean("literal", true);
		boolean caseSensitive = getNode().getBoolean("case-sensitive", false);
		boolean fullWord = getNode().getBoolean("full-word", true);
		pattern = createPattern(text, literal, caseSensitive, fullWord);
		WarlockPreferences.getInstance().addPreferenceChangeListener(this, new IPreferenceChangeListener() {
			public void preferenceChange(PreferenceChangeEvent event) {
				if(event.getKey().equals("pattern")) {
					pattern.setText(getNode().get("pattern", ""));
				} else if(event.getKey().equals("literal")) {
					pattern.setLiteral(getNode().getBoolean("literal", true));
				} else if(event.getKey().equals("case-sensitive")) {
					pattern.setCaseSensitive(getNode().getBoolean("case-sensitive", false));
				} else if(event.getKey().equals("full-word")) {
					pattern.setFullWordMatch(getNode().getBoolean("full-word", true));
				}
			}
		});
	}
	
	abstract protected T createPattern(String text, boolean literal, boolean caseSensitive, boolean fullWord);
	
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
		this.flush();
		this.notifyListenersChanged();
	}
	
	public void setLiteral (boolean literal) throws PatternSyntaxException
	{
		getNode().putBoolean("literal", literal);
		pattern.setLiteral(literal);
		this.flush();
		this.notifyListenersChanged();
	}
	
	public void setCaseSensitive (boolean caseSensitive)
	{
		getNode().putBoolean("case-sensitive", caseSensitive);
		pattern.setCaseSensitive(caseSensitive);
		this.flush();
		this.notifyListenersChanged();
	}
	
	public void setFullWordMatch (boolean fullWordMatch)
	{
		getNode().putBoolean("full-word", fullWordMatch);
		pattern.setFullWordMatch(fullWordMatch);
		this.flush();
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
}
