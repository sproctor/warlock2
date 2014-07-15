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
package cc.warlock.core.script.internal;

import java.util.ArrayList;
import java.util.Collection;

import cc.warlock.core.script.IMatch;

public class TextMatch implements IMatch {
	
	private String originalText;
	private String matchText;
	private String matchLine;//BFisher - For getting line we matched against.
	private String matchedText;
	private boolean ignoreCase;
	
	public TextMatch(String text) {
		this(text, true);
	}

	//BFisher - Return the line matched against for JS scripting.
	//See matches() below and matchLine above.
	public String getMatchedLine() {
		return this.matchLine;
	}
	
	public TextMatch(String text, boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		this.originalText = text;
		
		if(ignoreCase) {
			matchText = text.toLowerCase();
		} else {
			matchText = text;
		}
	}

	public boolean matches(String text) {
		if((ignoreCase && text.toLowerCase().contains(matchText)) || (!ignoreCase && text.matches(matchText))) {
			this.matchLine = text;
			int start = ignoreCase ? text.toLowerCase().indexOf(matchText.toLowerCase()) : text.indexOf(matchText);
			this.matchedText = text.substring(start, start + matchText.length());
			return true;
		}
		return false;
	}
	
	public String getText() {
		return originalText;
	}
	
	public Collection<String> groups() {
		ArrayList<String> groups = new ArrayList<String>();
		groups.add(matchedText);
		return groups;
	}
}
