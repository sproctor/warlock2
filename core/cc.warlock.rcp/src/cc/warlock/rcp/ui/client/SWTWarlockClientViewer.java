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
package cc.warlock.rcp.ui.client;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IMacroCommand;
import cc.warlock.core.client.IMacroVariable;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.IWarlockEntry;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.internal.WarlockMacro;

/**
 * @author Marshall
 *
 * A convenience super class for viewers who need SWT thread access
 */
public class SWTWarlockClientViewer implements IWarlockClientViewer {
	
	private IWarlockClientViewer viewer;
	
	public SWTWarlockClientViewer (IWarlockClientViewer viewer) {
		this.viewer = viewer;
	}
	
	private class CopyWrapper implements Runnable {
		public void run () {
			viewer.copy();
		}
	}
	
	private class PlaySoundWrapper implements Runnable {
		private InputStream soundStream;
		
		public PlaySoundWrapper(InputStream soundStream) {
			this.soundStream = soundStream;
		}
		
		public void run () {
			viewer.playSound(soundStream);
		}
	}
	
	
	private class OpenCustomStreamWrapper implements Runnable {
		private String name;
		
		public OpenCustomStreamWrapper(String name) {
			this.name = name;
		}
		
		public void run () {
			viewer.openCustomStream(name);
		}
	}
	
	private class PrintToCustomStreamWrapper implements Runnable {
		private String name;
		private WarlockString text;
		
		public PrintToCustomStreamWrapper(String name, WarlockString text) {
			this.name = name;
			this.text = text;
		}
		
		public void run () {
			viewer.printToCustomStream(name, text);
		}
	}
	
	private class ClearCustomStreamWrapper implements Runnable {
		private String name;
		
		public ClearCustomStreamWrapper(String name) {
			this.name = name;
		}
		
		public void run () {
			viewer.clearCustomStream(name);
		}
	}
	
	private class LaunchUrlWrapper implements Runnable {
		private URL url;

		LaunchUrlWrapper(URL url) {
			this.url = url;
		}
		
		public void run() {
			viewer.launchURL(url);
		}
	}
	
	private class AppendImageWrapper implements Runnable {
		private URL url;

		public AppendImageWrapper(URL url) {
			this.url = url;
		}
		
		public void run() {
			viewer.appendImage(url);
		}
	}
	
	protected void run(Runnable runnable) {
		Display.getDefault().asyncExec(new CatchingRunnable(runnable));
	}
	
	public IWarlockClient getClient() {
		return viewer.getClient();
	}
	
	public void copy() {
		run(new CopyWrapper());
	}
	
	public void playSound(InputStream file) {
		run(new PlaySoundWrapper(file));
	}
	
	public boolean isStreamOpen(String streamName) {
		// This method is not allowed to use any SWT methods
		return viewer.isStreamOpen(streamName);
	}

	public Collection<IMacroVariable> getMacroVariables() {
		return viewer.getMacroVariables();
	}

	public IMacroCommand getMacroCommand(String id) {
		return viewer.getMacroCommand(id);
	}

	public Collection<WarlockMacro> getDefaultMacros() {
		return viewer.getDefaultMacros();
	}
	
	public void openCustomStream(String name) {
		run(new OpenCustomStreamWrapper(name));
	}
	
	public void printToCustomStream(String name, WarlockString text) {
		run(new PrintToCustomStreamWrapper(name, text));
	}
	
	public void clearCustomStream(String name) {
		run(new ClearCustomStreamWrapper(name));
	}
	
	public void send(ICommand command) {
		viewer.send(command);
	}
	
	public void launchURL(URL url) {
		run(new LaunchUrlWrapper(url));
	}
	
	public void appendImage(URL imageURL) {
		run(new AppendImageWrapper(imageURL));
	}
	
	public IWarlockEntry getEntry () {
		return new SWTWarlockEntry(viewer.getEntry());
	}
}
