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
package cc.warlock.core.script.internal;

import java.io.InputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cc.warlock.core.client.Command;
import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.IWarlockClientViewerListener;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.WarlockStyle;
import cc.warlock.core.client.settings.ClientSettings;
import cc.warlock.core.client.settings.VariableConfigurationProvider;
import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.settings.IVariable;

public class ScriptCommands implements IScriptCommands, IStreamListener, IRoomListener {

	private IWarlockClientViewer viewer;
	private Collection<LinkedBlockingQueue<String>> textWaiters =
		Collections.synchronizedCollection(new ArrayList<LinkedBlockingQueue<String>>());
	private StringBuffer receiveBuffer = new StringBuffer();
	
	private boolean suspended = false;
	/**
	 * Used to count room changes. In order to enable waitForRoom to reliably
	 * detect when we've entered a new room, we need a persistent state change
	 * of some sort.
	 * 
	 * @see #waitNextRoom()
	 */
	private int room = 0;
	private int prompt = 0;
	private boolean atPrompt = true;
	
	private final Lock lock = new ReentrantLock();
	private final Condition gotResume = lock.newCondition();
	private final Condition nextRoom = lock.newCondition();
	private final Condition promptCond = lock.newCondition();
	
	private List<Thread> scriptThreads = Collections.synchronizedList(new ArrayList<Thread>());

	private IScript script;
	
	private int typeAhead = 0;
	
	/**
	 * Storage for active actions. Users must acquire the monitor first!
	 */
	private ArrayList<Map.Entry<IMatch, Runnable>> actions = new ArrayList<Map.Entry<IMatch, Runnable>>();
	private Thread actionThread = null;
	
	private class ScriptActionThread extends Thread {
		public void run() {
			addThread(this);
			LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
			synchronized(textWaiters) {
				textWaiters.add(queue);
			}

			try {
				while(true) {
					assert !script.isRunning();
					String text = null;
					try {
						text = queue.take();
					} catch(InterruptedException e) {
						synchronized(actions) {
							/* We must clear actionThread in the same critical
							 * section as the decision to terminate the thread
							 */
							if(actions.size() == 0) {
								actionThread = null;
								return;
							}
							continue;
						}
					}
	
					synchronized(actions) {
						for(Map.Entry<IMatch, Runnable> action : actions) {
							if(action.getKey().matches(text))
								action.getValue().run();
						}
					}
				}
			} finally {
				synchronized(textWaiters) {
					textWaiters.remove(queue);
				}
				removeThread(this);
				synchronized(actions) {
					// FIXME What to do for a runtime exception?
					if (actionThread == this)
						actionThread = null;
				}
			}
		}
	}
	
	public ScriptCommands(IWarlockClientViewer viewer, IScript script) {
		this.viewer = viewer;
		this.script = script;

		IWarlockClient client = getClient();
		if(client != null) {
			setClient(client);
		} else {
			viewer.addClientViewerListener(new IWarlockClientViewerListener() {
				@Override
				public void clientChanged(IWarlockClient client) {
					setClient(client);
					ScriptCommands.this.viewer.removeClientViewerListener(this);
				}
			});
		}
	}
	
	private void setClient(IWarlockClient client) {
		client.addStreamListener(IWarlockClient.MAIN_STREAM_NAME, this);
		client.addRoomListener(this);
	}
	
	@Override
	public void echo(String text) throws InterruptedException {
		getClient().echo("[" + script.getName() + "]: " + text + "\n");
		waitIfNotPrompting();
	}
	
	@Override
	public void debug(String text) {
		getClient().echo("[" + script.getName() + "]: " + text + "\n", WarlockStyle.debugStyle);
	}
	
	@Override
	public BlockingQueue<String> createLineQueue() {
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		synchronized(textWaiters) {
			textWaiters.add(queue);
		}
		return queue;
	}
	
	@Override
	public boolean removeLineQueue(BlockingQueue<String> queue) {
		synchronized(textWaiters) {
			return textWaiters.remove(queue);
		}
	}
	
	@Override
	public IMatch matchWait(Collection<IMatch> matches, BlockingQueue<String> matchQueue, double timeout) throws InterruptedException {
		try {
			boolean haveTimeout = timeout > 0.0;
			long timeoutEnd = 0L;
			if(haveTimeout)
				timeoutEnd = System.currentTimeMillis() + (long)(timeout * 1000.0);
			
			// run until we get a match or are told to stop
			while(true) {
				String text = null;
				// wait for some text
				if(haveTimeout) {
					long now = System.currentTimeMillis();
					if(timeoutEnd >= now)
						text = matchQueue.poll(timeoutEnd - now, TimeUnit.MILLISECONDS);
					if(text == null)
						return null;
				} else {
					text = matchQueue.take();
				}
				// try all of our matches
				for(IMatch match : matches) {
					if(match.matches(text)) {
						return match;
					}
				}
			}
		} finally {
			synchronized(textWaiters) {
				textWaiters.remove(matchQueue);
			}
		}
	}
	
	@Override
	public void move(String direction, int lineNum) throws InterruptedException {
		put(direction, lineNum);
		waitNextRoom();
	}

	@Override
	public void waitNextRoom() throws InterruptedException {
		lock.lock();
		try {
			int curRoom = room;
			while (room == curRoom)
				nextRoom.await();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void pause(double seconds) throws InterruptedException {
		long now = System.currentTimeMillis();
		long pauseEnd = now + (long)(seconds * 1000.0);
		
		while(pauseEnd > now) {
			Thread.sleep(pauseEnd - now);
			now = System.currentTimeMillis();
		}
	}
	
	@Override
	public void put(String text, int lineNum) throws InterruptedException {
		waitIfNotPrompting();
		// this is just to help keep us from sending too many lines at once
		if(typeAhead >= 2)
			this.waitForPrompt();
		synchronized(this) {
			typeAhead++;
		}
		Command command = new Command(text);
		command.setPrefix("[" + script.getName() + ":" + lineNum + "]: ");
		getClient().send(command);
	}

	@Override
	public void waitFor(IMatch match) throws InterruptedException {
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

		synchronized(textWaiters) {
			textWaiters.add(queue);
		}
		try {
			while(true) {
				String text = queue.take();
				if(match.matches(text)) {
					break;
				}
			}
		} finally {
			synchronized(textWaiters) {
				textWaiters.remove(queue);
			}
		}
	}

	@Override
	public void waitForPrompt() throws InterruptedException {
		int oldPrompt = prompt;
		lock.lock();
		try {
			while(oldPrompt == prompt)
				promptCond.await();
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public IWarlockClient getClient() {
		return viewer.getClient();
	}
	
	@Override
	public void streamCleared(IStream stream) {}
	
	@Override
	public void streamFlush(IStream stream) {}
	
	@Override
	public void streamPrompted(IStream stream, String prompt) {
		synchronized(this) {
			if(typeAhead > 0)
				typeAhead--;
		}
		lock.lock();
		try {
			atPrompt = true;
			this.prompt++;
			promptCond.signalAll();
		} finally {
			lock.unlock();
		}
		receiveLine(prompt);
	}
	
	@Override
	public void streamReceivedCommand(IStream stream, ICommand command) {
		receiveText(command.getCommand());
	}
	
	@Override
	public void streamReceivedText(IStream stream, WarlockString text) {
		if(text.hasStyleNamed(WarlockStyle.DEBUG))
			return;
		
		if(!text.hasStyleNamed(WarlockStyle.ECHO)) {
			lock.lock();
			atPrompt = false;
			lock.unlock();
		}
		receiveText(text.toString());
	}
	
	@Override
	public void componentUpdated(IStream stream, String id, WarlockString text) { }
	
	protected void receiveText(String text) {
		int end;
		receiveBuffer.append(text);
		while ((end = receiveBuffer.indexOf("\n")) != -1) {
			receiveLine(receiveBuffer.substring(0, end + 1));
			receiveBuffer.delete(0, end + 1);
		}
	}
	
	protected void receiveLine(String line) {
		synchronized(textWaiters) {
			for(LinkedBlockingQueue<String>  queue : textWaiters) {
				try {
					queue.put(line);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void nextRoom() {
		lock.lock();
		atPrompt = false;
		try {
			room++;
			nextRoom.signalAll();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void stop() {
		interrupt();

		getClient().removeStreamListener(IWarlockClient.MAIN_STREAM_NAME, this);
		getClient().removeRoomListener(this);
		clearActions();
	}
	
	@Override
	public void interrupt() {
		lock.lock();
		try {
			synchronized(scriptThreads) {
				for(Thread scriptThread : scriptThreads)
					scriptThread.interrupt();
			}
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public void resume() {
		lock.lock();
		try {
			suspended = false;
			gotResume.signalAll();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void suspend() {
		this.suspended = true;
	}
	
	@Override
	public boolean isSuspended() {
		return suspended;
	}
	
	@Override
	public void waitForResume() throws InterruptedException {
		// Don't grab the lock if we don't need to
		if(!suspended)
			return;
		
		lock.lock();
		try {
			while(suspended)
				gotResume.await();
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public void addThread(Thread thread) {
		scriptThreads.add(thread);
	}
	
	@Override
	public void removeThread(Thread thread) {
		scriptThreads.remove(thread);
	}
	
	@Override
	public void playSound(InputStream stream) {
		getClient().playSound(stream);
	}

	@Override
	public void streamTitleChanged(IStream stream, String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void streamCreated(IStream stream) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void openWindow(String name) {
		getClient().getViewer().openCustomStream("script" + name);
	}
	
	@Override
	public void printToWindow(String name, String text) {
		getClient().getViewer().printToCustomStream("script" + name, new WarlockString(text));
	}
	
	@Override
	public void clearWindow(String name) {
		getClient().getViewer().clearCustomStream("script" + name);
	}

	private IClientSettings getSettings() {
		IWarlockClient client = getClient();
		IClientSettings settings = null;
		if(client != null)
			settings = client.getClientSettings();
		if(settings == null)
			settings = ClientSettings.getGlobalClientSettings();
		return settings;
	}
	
	@Override
	public String getStoredVariable(String id) {
		IVariable var = VariableConfigurationProvider.getProvider(getSettings()).getVariable(id);
		if(var == null)
			return null;
		return var.getValue();
	}

	@Override
	public void setStoredVariable(String id, String value) {
		VariableConfigurationProvider.getProvider(getSettings()).addVariable(id, value);
	}

	@Override
	public void removeStoredVariable(String id) {
		VariableConfigurationProvider.getProvider(getSettings()).removeVariable(id);
	}
	
	@Override
	public void waitForRoundtime() throws InterruptedException {
		getClient().getTimer("roundtime").waitForEnd();
		
	}
	
	private void waitIfNotPrompting() throws InterruptedException {
		// the following prevents us from getting echos/commands interleaved in our text
		//     waits for a prompt if we aren't already at one.
		while(!atPrompt) {
			try {
				lock.lock();
				if(!atPrompt)
					promptCond.await();
			} finally {
				lock.unlock();
			}
		}
	}
	
	@Override
	public void addAction(Runnable action, IMatch match) {
		synchronized(actions) {
			actions.add(new SimpleEntry<IMatch, Runnable>(match, action));
			
			if(actionThread == null) {	
				actionThread = new ScriptActionThread();
				actionThread.start();
			}
		}
	}
	
	@Override
	public void clearActions() {
		synchronized(actions) {
			actions.clear();
			if(actionThread != null)
				actionThread.interrupt();
		}
	}
	
	@Override
	public void removeAction(IMatch action) {
		synchronized(actions) {
			actions.remove(action);
			if(actionThread != null)
				actionThread.interrupt();
		}
	}
	
	@Override
	public void removeAction(String text) {
		synchronized(actions) {
			boolean changed = false;
			for(Iterator<Map.Entry<IMatch, Runnable>> iter = actions.iterator(); iter.hasNext(); ) {
				IMatch match = iter.next().getKey();
				// remove the element with the same name as text
				if(match.getText().equals(text)) {
					iter.remove();
					changed = true;
				}
			}
			if(changed && actionThread != null)
				actionThread.interrupt();
		}
	}
	
	@Override
	public void error(int line, String message, String command) {
		debug("ERROR on line " + line + ": \"" + message + "\" command: (" + command + ")");
		script.stop();
	}
	
	@Override
	public void warning(int line, String message, String command) {
		debug("WARNING on line " + line + ": \"" + message + "\" line: (" + command + ")");
	}
	
	@Override
	public void debug(int level, int line, String message) {
		if (level <= script.getDebugLevel()) {
			debug("DEBUG on line " + line + ": " + message);
		}
	}
}
