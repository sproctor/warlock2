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
/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.script.javascript;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.script.AbstractScript;
import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.IScriptInfo;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JavascriptScript extends AbstractScript {

	private JavascriptEngine engine;
	private Context context;
	private IScriptCommands commands;
	
	public class StopException extends Error {
		private static final long serialVersionUID = 5687999229834097094L;
	}
	
	public JavascriptScript (JavascriptEngine engine, IScriptInfo info, IWarlockClientViewer viewer) {
		super(info, viewer);
		
		this.engine = engine;
	}

	public void setCommands(IScriptCommands commands) {
		this.commands = commands;
	}
	
	public IScriptCommands getCommands() {
		return commands;
	}
	
	public Scriptable getScope() {
		return engine.scope;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	public IScriptEngine getScriptEngine() {
		return engine;
	}
	
	public void execute(String command) { }
	
	public void checkStop() {
		if(commands.isSuspended()) {
			try {
				commands.waitForResume();
			} catch(InterruptedException e) {
				// Nothing to do
			}
		}
		if(!isRunning())
			throw new StopException();
	}
}
