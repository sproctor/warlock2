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
package cc.warlock.core.script;

import java.util.ArrayList;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;

public abstract class AbstractScript implements IScript {

	private ArrayList<IScriptListener> listeners;
	private boolean stopped = true;
	private IScriptInfo info;
	private IWarlockClientViewer viewer;
	private int debugLevel = 1;
	
	public AbstractScript (IScriptInfo info, IWarlockClientViewer viewer)
	{
		this.listeners = new ArrayList<IScriptListener>();
		
		this.info = info;
		this.viewer = viewer;
	}
	
	public void start () {
		stopped = false;
		echo("[script started: " + getName() + "]");
		
		for (IScriptListener listener : listeners) listener.scriptStarted(this);
	}
	
	public boolean isRunning() {
		return !stopped;
	}
	
	public void stop() {
		stopped = true;
		echo("[script stopped: " + getName() + "]");
		getCommands().stop();
		
		for (IScriptListener listener : listeners) listener.scriptStopped(this, true);
		
		listeners.clear();
	}

	public boolean isSuspended() {
		return getCommands().isSuspended();
	}
	
	public void suspend() {
		if(!getCommands().isSuspended()) {
			echo("[script paused: " + getName() + "]");
			getCommands().suspend();

			for (IScriptListener listener : listeners) {
				listener.scriptPaused(this);
			}
		}
	}
	
	public void resume() {
		if(getCommands().isSuspended()) {
			echo("[script resumed: " + getName() + "]");
			getCommands().resume();
			
			for (IScriptListener listener : listeners) {
				listener.scriptResumed(this);
			}
		}
	}
	
	public void addScriptListener(IScriptListener listener) {
		listeners.add(listener);
	}
	
	public void removeScriptListener(IScriptListener listener) {
		listeners.remove(listener);
	}
	
	public String getName() {
		return info.getScriptName();
	}
	
	public IScriptInfo getScriptInfo() {
		return info;
	}
	
	protected void echo(String message) {
		getClient().echo(message + "\n");
	}
	
	public IWarlockClient getClient() {
		return viewer.getClient();
	}
	
	public IWarlockClientViewer getViewer() {
		return viewer;
	}
	
	abstract public IScriptCommands getCommands();
	
	/*
	 * Debug levels:
	 *   0: No output from script engine
	 *   1: Normal user output from script engine
	 *   2: Helpful script debugging output
	 *   3: Verbose script debugging output
	 *   4: For Warlock developers, engine debugging output
	 */
	public void setDebugLevel(int level) {
		this.debugLevel = level;
	}
	
	public int getDebugLevel() {
		return this.debugLevel;
	}
}
