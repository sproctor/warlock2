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

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.IVariable;

public class Variable extends ClientSetting implements IVariable {

	protected String identifier, value;
	
	public Variable (Preferences parentNode, String identifier) {
		super(parentNode, identifier);
		
		this.identifier = identifier;
		this.value = getNode().get("value", null);
	}
	
	public Variable (Preferences parentNode, String identifier, String value) {
		super(parentNode, identifier);
		
		this.identifier = identifier;
		this.value = value;
		getNode().put("value", value);
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		getNode().parent().remove(this.identifier);
		this.identifier = identifier;
		changePath(identifier);
		getNode().put("value", this.value);
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		getNode().put("value", value);
		
		this.value = value;
	}
}
