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

import java.io.InputStream;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;

import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IWarlockClient;

public interface IScriptCommands extends IRoomListener {

	public void put (String text, int lineNum) throws InterruptedException;
	
	public void echo (String text) throws InterruptedException;
	public void debug(String text);
	
	public void move (String direction, int lineNum) throws InterruptedException;
	
	public void waitFor (IMatch match) throws InterruptedException;
	
	public void waitNextRoom () throws InterruptedException;
	
	public BlockingQueue<String> createLineQueue();
	public boolean removeLineQueue(BlockingQueue<String> queue);
	
	public IMatch matchWait (Collection<IMatch> matches, BlockingQueue<String> matchQueue, double timeout) throws InterruptedException;
	
	public void pause (double seconds) throws InterruptedException;
	
	public void waitForPrompt () throws InterruptedException;
	
	public IWarlockClient getClient();
	
	public void stop();
	
	public void addThread(Thread thread);
	public void removeThread(Thread thread);
	public void interrupt();
	
	public void suspend();
	public void resume();
	public boolean isSuspended();
	public void waitForResume() throws InterruptedException;
	
	public void playSound (InputStream stream);
	
	public void openWindow(String name);
	public void printToWindow(String name, String text);
	public void clearWindow(String name);
	
	public String getStoredVariable(String id);
	
	public void setStoredVariable(String id, String value);
	
	public void removeStoredVariable(String id);
	
	public void waitForRoundtime() throws InterruptedException;
	
	public void addAction(Runnable action, IMatch match);
	
	public void clearActions();
	
	public void removeAction(IMatch action);
	
	public void removeAction(String text);
	
	public void error(int line, String message, String command);
	public void warning(int line, String message, String command);
	public void debug (int level, int line, String message);
}
