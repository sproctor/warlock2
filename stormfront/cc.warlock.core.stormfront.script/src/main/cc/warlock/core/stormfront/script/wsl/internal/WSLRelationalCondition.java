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
package cc.warlock.core.stormfront.script.wsl.internal;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cc.warlock.core.stormfront.script.wsl.WSLScriptContext;

public class WSLRelationalCondition extends WSLAbstractBoolean {

	public enum RelationalOperator {
		GreaterThan			{ boolean compare(WSLScriptContext cx, IWSLValue arg1, IWSLValue arg2) { return arg1.toDouble(cx) > arg2.toDouble(cx); } },
		GreaterThanEqualTo	{ boolean compare(WSLScriptContext cx, IWSLValue arg1, IWSLValue arg2) { return arg1.toDouble(cx) >= arg2.toDouble(cx); } },
		LessThan			{ boolean compare(WSLScriptContext cx, IWSLValue arg1, IWSLValue arg2) { return arg1.toDouble(cx) < arg2.toDouble(cx); } },
		LessThanEqualTo		{ boolean compare(WSLScriptContext cx, IWSLValue arg1, IWSLValue arg2) { return arg1.toDouble(cx) <= arg2.toDouble(cx); } },
		Contains			{ boolean compare(WSLScriptContext cx, IWSLValue arg1, IWSLValue arg2) { return arg1.toString(cx).contains(arg2.toString(cx)); } },
		ContainsRe			{ boolean compare(WSLScriptContext cx, IWSLValue arg1, IWSLValue arg2) {
			String regex = arg2.toString(cx);
			String text = arg1.toString(cx);
			Matcher m = Pattern.compile(regex).matcher(text);
			if(m.find(0)) {
				for(int i = 0; i <= m.groupCount(); i++) {
					cx.setLocalVariable(String.valueOf(i), m.group(i));
				}
				return true;
			}
			return false;
		} };
	
		abstract boolean compare(WSLScriptContext cx, IWSLValue arg1, IWSLValue arg2);
	}
	
	private List<IWSLValue> args;
	private List<RelationalOperator> ops;
	
	public WSLRelationalCondition(List<IWSLValue> args, List<RelationalOperator> ops) {
		this.args = args;
		this.ops = ops;
	}
	
	@Override
	public boolean toBoolean(WSLScriptContext cx) {
		IWSLValue value = args.get(0);
		for(int i = 0; i < ops.size(); i++) {
			IWSLValue nextValue = args.get(i + 1);
			try {
				if(!ops.get(i).compare(cx, value, nextValue))
					return false;
			} catch(PatternSyntaxException e) {
				cx.scriptWarning("Pattern syntax error at pattern index ("
						+ e.getIndex() + "): " + e.getDescription());
				return false;
			} catch(Exception e) {
				cx.scriptWarning(e.getMessage());
				return false;
			}
			value = nextValue;
		}
		return true;
	}
}
