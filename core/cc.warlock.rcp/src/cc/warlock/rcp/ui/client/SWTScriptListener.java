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
package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptListener;

public class SWTScriptListener implements IScriptListener {

	private IScriptListener listener;
	
	public SWTScriptListener (IScriptListener listener)
	{
		this.listener = listener;
	}
	
	private class PausedListener implements Runnable
	{
		private IScript script;
		
		public PausedListener(IScript script) {
			this.script = script;
		}
		
		public void run() {
			listener.scriptPaused(script);
		}
	}
	
	private class ResumedListener implements Runnable
	{
		private IScript script;
		
		public ResumedListener(IScript script) {
			this.script = script;
		}
		
		public void run() {
			listener.scriptResumed(script);
		}
	}
	
	private class StartedListener implements Runnable
	{
		private IScript script;
		
		public StartedListener(IScript script) {
			this.script = script;
		}
		
		public void run() {
			listener.scriptStarted(script);
		}
	}
	
	private class StoppedListener implements Runnable
	{
		private IScript script;
		private boolean userStopped;
		
		public StoppedListener(IScript script, boolean userStopped) {
			this.script = script;
			this.userStopped = userStopped;
		}
		
		public void run() {
			listener.scriptStopped(script, userStopped);
		}
	}
	
	protected void run(Runnable runnable)
	{
		Display.getDefault().asyncExec(new CatchingRunnable(runnable));
	}
	
	public void scriptPaused(IScript script) {
		run(new PausedListener(script));
	}

	public void scriptResumed(IScript script) {
		run(new ResumedListener(script));
	}

	public void scriptStarted(IScript script) {
		run(new StartedListener(script));
	}

	public void scriptStopped(IScript script, boolean userStopped) {
		run(new StoppedListener(script, userStopped));
	}

}
