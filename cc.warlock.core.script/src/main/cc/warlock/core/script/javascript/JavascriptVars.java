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
package cc.warlock.core.script.javascript;

import org.mozilla.javascript.Scriptable;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.script.ScriptEngineRegistry;

public class JavascriptVars implements IJavascriptVariableProvider {

	//private HashMap<JavascriptScript, JavascriptCommands> scriptCommands = new HashMap<JavascriptScript, JavascriptCommands>();
	
	public JavascriptVars ()
	{
		JavascriptEngine engine = (JavascriptEngine)
			ScriptEngineRegistry.getScriptEngine(JavascriptEngine.ENGINE_ID);
		
		if (engine == null) {
			System.err.println("Couldn't find JavaScript engine.");
		} else {
			engine.addVariableProvider(this);
		}
	}
	
	public void loadVariables(JavascriptScript script, Scriptable scope) {
		IWarlockClient client = script.getClient();

		//overwrite the "script" variable with our big delegator
		//JavascriptCommands commands = new JavascriptCommands(script.getCommands(), script);
		//scriptCommands.put(script, script.);

		scope.put("script", scope, script.getCommands());
		scope.put("compass", scope, client.getCompass());
		scope.put("commandHistory", scope, script.getViewer().getEntry().getCommandHistory());

		scope.put("roundtime", scope, new JavascriptProperty<Integer>(scope, client.getTimer("roundtime").getProperty()));
		scope.put("casttime", scope, new JavascriptProperty<Integer>(scope, client.getTimer("casttime").getProperty()));
		scope.put("leftHand", scope, new JavascriptProperty<String>(scope, client.getProperty("left")));
		scope.put("rightHand", scope, new JavascriptProperty<String>(scope, client.getProperty("right")));
		scope.put("spell", scope, new JavascriptProperty<String>(scope, client.getProperty("spell")));
		scope.put("monstercount", scope, new JavascriptProperty<String>(scope, client.getProperty("monstercount")));
	}
	
}
