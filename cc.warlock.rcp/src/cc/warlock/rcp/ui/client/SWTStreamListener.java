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

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.WarlockString;

public class SWTStreamListener implements IStreamListener {
	
	private IStreamListener listener;
	
	public SWTStreamListener (IStreamListener listener)
	{
		this.listener = listener;
	}
	
	private class ClearedWrapper implements Runnable
	{
		private IStream stream;
		
		public ClearedWrapper(IStream stream) {
			this.stream = stream;
		}
		
		public void run() {
			listener.streamCleared(stream);
		}
	}
	
	private class ReceivedTextWrapper implements Runnable
	{
		private IStream stream;
		private WarlockString text;
		
		public ReceivedTextWrapper(IStream stream, WarlockString text) {
			this.stream = stream;
			this.text = text;
		}
		
		public void run() {
			listener.streamReceivedText(stream, text);
		}
	}
	
	private class TitleChangedWrapper implements Runnable
	{
		private IStream stream;
		private String title;
		
		public TitleChangedWrapper(IStream stream, String title) {
			this.stream = stream;
			this.title = title;
		}
		
		public void run() {
			listener.streamTitleChanged(stream, title);
		}
	}
	
	private class CommandWrapper implements Runnable
	{
		private IStream stream;
		private ICommand command;
		
		public CommandWrapper(IStream stream, ICommand command) {
			this.stream = stream;
			this.command = command;
		}
		
		public void run() {
			listener.streamReceivedCommand(stream, command);
		}
	}
	
	private class PromptedWrapper implements Runnable
	{
		private IStream stream;
		private String text;
		
		public PromptedWrapper(IStream stream, String text) {
			this.stream = stream;
			this.text = text;
		}
		
		public void run() {
			listener.streamPrompted(stream, text);
		}
	}
	
	private class ComponentUpdatedWrapper implements Runnable
	{
		private IStream stream;
		private String id;
		private WarlockString text;
		
		public ComponentUpdatedWrapper(IStream stream, String id, WarlockString text) {
			this.stream = stream;
			this.id = id;
			this.text = text;
		}
		
		public void run() {
			listener.componentUpdated(stream, id, text);
		}
	}
	
	protected void run(Runnable runnable)
	{
		Display.getDefault().asyncExec(new CatchingRunnable(runnable));
	}
	
	@Override
	public void streamCleared(IStream stream) {
		run(new ClearedWrapper(stream));
	}
	
	@Override
	public void streamReceivedText(IStream stream, WarlockString text) {
		run(new ReceivedTextWrapper(stream, text));
	}
	
	@Override
	public void streamTitleChanged(IStream stream, String title) {
		run(new TitleChangedWrapper(stream, title));
	}
	
	@Override
	public void streamPrompted(IStream stream, String prompt) {
		run(new PromptedWrapper(stream, prompt));
	}
	
	@Override
	public void streamReceivedCommand (IStream stream, ICommand command) {
		run(new CommandWrapper(stream, command));
	}
	
	@Override
	public void componentUpdated(IStream stream, String id, WarlockString text) {
		run(new ComponentUpdatedWrapper(stream, id, text));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SWTStreamListener) {
			return this.listener.equals(((SWTStreamListener)obj).listener);
		} else {
			return this.listener.equals(obj);
		}
	}
}
