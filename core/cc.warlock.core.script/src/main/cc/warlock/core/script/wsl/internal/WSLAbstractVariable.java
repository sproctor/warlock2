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
package cc.warlock.core.script.wsl.internal;

import cc.warlock.core.script.wsl.WSLScriptContext;

abstract public class WSLAbstractVariable extends WSLAbstractString {
	
	protected IWSLValue variableName;
	private String prefix;
	
	public WSLAbstractVariable(IWSLValue var, String prefix) {
		variableName = var;
		this.prefix = prefix;
	}
	
	@Override
	public String toString(WSLScriptContext cx) {
		IWSLValue value = getVariable(cx);
		if(value == null) {
			return prefix + variableName.toString(cx);
		}
		return value.toString(cx);
	}
	
	@Override
	public boolean toBoolean(WSLScriptContext cx) {
		if(!variableExists(cx))
			return false;
		return super.toBoolean(cx);
	}
	
	abstract protected IWSLValue getVariable(WSLScriptContext cx);
	abstract protected boolean variableExists(WSLScriptContext cx);
}
