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
 * Created on Jan 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.script.javascript;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;
import org.mozilla.javascript.WrappedException;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.IScriptFileInfo;
import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.script.ScriptCommandsFactory;
import cc.warlock.core.script.javascript.JavascriptScript.StopException;


/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JavascriptEngine implements IScriptEngine {

	public static final String ENGINE_ID = "cc.warlock.script.javascript.JavascriptEngine";
	
	public static final String includeFunction = "function include (src) { script.include(src); }";
	public static final String includeFunctionName = "<warlock js:include>";
	
	private ArrayList<IJavascriptVariableProvider> varProviders = new ArrayList<IJavascriptVariableProvider>();
	private ArrayList<JavascriptScript> runningScripts = new ArrayList<JavascriptScript>();
	
	public Scriptable scope;
	
	private ArrayList<String> supportedExtensions = new ArrayList<String>();
	
	private class WarlockContextFactory extends ContextFactory {
		
		protected Context makeContext() {
			Context cx = super.makeContext();
			// Can't optimize if we want the instruction counter
			cx.setOptimizationLevel(-1);
			// Allow the user to break infinite loops or waits that don't use
			// our APIs (if such things exist).
			cx.setInstructionObserverThreshold(500);
			
			// Expose our API as JS variables rather than Java primitives
			WrapFactory wf = new WrapFactory();
			wf.setJavaPrimitiveWrap(false);
			cx.setWrapFactory(wf);
			
			return cx;
		}
		
		protected void observeInstructionCount(Context cx, int instructionCount) {
			JavascriptScript script = null;
			// find our script by context
			for(JavascriptScript cur : runningScripts) {
				if(cur.getContext().equals(cx)) {
					script = cur;
					break;
				}
			}
			
			if(script == null) {
				System.out.println("Couldn't find context.");
				throw new Error();
			}
			
			if (!script.isRunning())
				throw script.new StopException();
		}
	}
	
	public JavascriptEngine() {
		supportedExtensions.add("js");
		ContextFactory.initGlobal(new WarlockContextFactory());
	}
	
	public String getScriptEngineId() {
		return ENGINE_ID;
	}
	
	public String getScriptEngineName() {
		return "Standard Javascript Engine (c) 2002-2007 Warlock Team";
	}
	
	
	public void addVariableProvider (IJavascriptVariableProvider provider)
	{
		varProviders.add(provider);
	}
	
	public void removeVariableProvider (IJavascriptVariableProvider provider)
	{
		varProviders.remove(provider);
	}
	
	public boolean supports(IScriptInfo scriptInfo) {
		if (scriptInfo instanceof IScriptFileInfo)
		{
			IScriptFileInfo info = (IScriptFileInfo) scriptInfo;
			if (info.getExtension() != null && supportedExtensions.contains(info.getExtension().toLowerCase())) {
					return true;
			}
		}
		
		return false;
	}

	public IScript startScript(IScriptInfo info, final IWarlockClientViewer viewer, final String[] arguments) {
		
		final JavascriptScript script = new JavascriptScript(this, info, viewer);
		IScriptCommands commands = ScriptCommandsFactory.getFactory().createScriptCommands(viewer, script);
		script.setCommands(commands);
		
		script.start();
		runningScripts.add(script);
		
		new Thread(new Runnable() {
			@Override
			public void run () {
				script.getCommands().addThread(Thread.currentThread());
				Context context = Context.enter();
				try {
					script.setContext(context);
					
					scope = context.initStandardObjects();
					scope.put("script", scope, script.getCommands());
					scope.put("arguments", scope, arguments);

					Function function = context.compileFunction(scope, includeFunction, includeFunctionName, 0, null);
					scope.put("include", scope, function);
					
					for (IJavascriptVariableProvider provider : varProviders) {
						provider.loadVariables(script, scope);
					}
					ScriptableObject.defineClass(scope, MatchList.class);
					
					Reader reader = script.getScriptInfo().openReader();
					Object result = context.evaluateReader(scope, reader, script.getName(), 1, null);
					System.err.println("script result: " + Context.toString(result));
					reader.close();
				}
				catch (WrappedException e) {
					if (!(e.getCause() instanceof Error))
					{
						e.printStackTrace();
						viewer.getClient().echo("[JS " + e.details() + "  Script: " + script.getName() + "  Line: " + e.lineNumber() + "]\n");
					}
				}
				catch (RhinoException e) {
					e.printStackTrace();
					viewer.getClient().echo("[JS " + e.details() + "  Script: " + script.getName() + "  Line: " + e.lineNumber() + "]\n");
				}
				catch(StopException e) {
					// normal exit, do nothing
				}
				catch (Exception e) {
					e.printStackTrace();
					viewer.getClient().echo("[unhandled exception in script: " + script.getName() + "]\n");
				}
				finally {
					script.getCommands().removeThread(Thread.currentThread());
					if(script.isRunning()) {
						script.stop();
					}
					Context.exit();
					runningScripts.remove(script);
				}
			}
		}).start();
		return script;
	}

	public static Object loadString (String content)
	{
		Context context = Context.enter();
		try {
			
			Scriptable scope = context.initStandardObjects();
			Object result = context.evaluateString(scope, content, "<cmd>", 1, null);
			
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			Context.exit();
		}
	}
	
	public static void main (String args[])
	{
		loadString("function helloWorld () { return 'Hello World'; } helloWorld(); ");
	}
	
	public Collection<? extends IScript> getRunningScripts() {
		return runningScripts;
	}
	
	public Collection<String> getSupportedExtensions() {
		return supportedExtensions;
	}
}
