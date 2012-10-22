package cc.warlock.core.script.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptListener;
import cc.warlock.core.script.ScriptEngineRegistry;

import com.martiansoftware.jsap.CommandLineTokenizer;

public class ScriptRegistry implements IScriptListener {
	static private HashMap<IWarlockClientViewer, ScriptRegistry> registries =
			new HashMap<IWarlockClientViewer, ScriptRegistry>();
	private IWarlockClientViewer viewer;
	private ArrayList<IScript> runningScripts = new ArrayList<IScript>();
	private ArrayList<IScriptListener> scriptListeners = new ArrayList<IScriptListener>();
	
	static public ScriptRegistry getRegistry(IWarlockClientViewer viewer) {
		return registries.get(viewer);
	}
	
	public ScriptRegistry(IWarlockClientViewer viewer) {
		this.viewer = viewer;
		registries.put(viewer, this);
	}
	
	public void runScript(String command) {
		command = command.replaceAll("[\\r\\n]", "");
		
		int firstSpace = command.indexOf(" ");
		String scriptName = command.substring(0, (firstSpace < 0 ? command.length() : firstSpace));
		String[] arguments = new String[0];
		
		if (firstSpace > 0)
		{
			String args = command.substring(firstSpace+1);
			arguments = CommandLineTokenizer.tokenize(args);
		}
		
		IScript script = ScriptEngineRegistry.startScript(scriptName, viewer, arguments);
		if (script != null)
		{
			script.addScriptListener(this);
			for (IScriptListener listener : scriptListeners) listener.scriptStarted(script);
			runningScripts.add(script);
		}
	}
	
	public void scriptPaused(IScript script) {
		for (IScriptListener listener : scriptListeners) listener.scriptPaused(script);
	}
	
	public void scriptResumed(IScript script) {
		for (IScriptListener listener : scriptListeners) listener.scriptResumed(script);
	}
	
	public void scriptStarted(IScript script) {
		for (IScriptListener listener : scriptListeners) listener.scriptStarted(script);
	}
	
	public void scriptStopped(IScript script, boolean userStopped) {
		runningScripts.remove(script);
		
		for (IScriptListener listener : scriptListeners) listener.scriptStopped(script, userStopped);
	}
	
	public Collection<IScript> getRunningScripts() {
		return runningScripts;
	}
	
	public void addScriptListener(IScriptListener listener)
	{
		scriptListeners.add(listener);
	}
	
	public void removeScriptListener (IScriptListener listener)
	{
		scriptListeners.remove(listener);
	}
}
