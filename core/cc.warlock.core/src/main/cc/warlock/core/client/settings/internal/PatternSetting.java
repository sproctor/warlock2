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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.IPatternSetting;
import cc.warlock.core.configuration.WarlockSetting;

public class PatternSetting extends WarlockSetting implements IPatternSetting {

	protected Pattern pattern;
	
	protected String text;
	protected boolean literal = false;
	protected boolean fullWord = true;
	protected boolean caseSensitive = false;
	protected boolean updateDeferred = true;
	
	public PatternSetting (Preferences parentNode, String path) {
		super(parentNode, path);
		
		this.text = getNode().get("pattern", "");
		this.literal = getNode().getBoolean("literal", false);
		this.caseSensitive = getNode().getBoolean("case-sensitive", false);
		this.fullWord = getNode().getBoolean("full-word", true);
		this.updateDeferred = true;
	}
	
	protected void update() throws PatternSyntaxException {
		if (text != null && text.length() > 0) {
			String s = this.text;
			int flags = 0;
			if (literal) {
				s = Pattern.quote(s);
			}
			if (!caseSensitive) {
				flags |= Pattern.CASE_INSENSITIVE;
			}
			if (fullWord) {
				s = "(^|\\b|\\s|\\p{Punct})" + s + "($|\\b|\\s|\\p{Punct})";
			}
			
			pattern = Pattern.compile(s, flags);
		} else {
			pattern = null;
		}
		updateDeferred = false;
	}
	
	public Pattern getPattern() throws PatternSyntaxException{
		if (updateDeferred)
			update();
		return pattern;
	}
	
	public boolean matches(String text) {
		Matcher m = getPattern().matcher(text);
		return m.matches();
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) throws PatternSyntaxException {
		updateDeferred = true;
		getNode().put("pattern", text);
		this.text = text;
	}
	
	public void setLiteral (boolean literal) throws PatternSyntaxException
	{
		updateDeferred = true;
		getNode().putBoolean("literal", literal);
		this.literal = literal;
	}
	
	public void setCaseSensitive (boolean caseSensitive)
	{
		updateDeferred = true;
		getNode().putBoolean("case-sensitive", caseSensitive);
		this.caseSensitive = caseSensitive;
	}
	
	public void setFullWordMatch (boolean fullWordMatch)
	{
		updateDeferred = true;
		getNode().putBoolean("full-word", fullWordMatch);
		this.fullWord = fullWordMatch;
	}
	
	public boolean isLiteral() {
		return literal;
	}
	
	public boolean isCaseSensitive() {
		return caseSensitive;
	}
	
	public boolean isFullWordMatch () {
		return fullWord;
	}
}
