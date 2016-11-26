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

package cc.warlock.core.client;

import java.util.Timer;
import java.util.TimerTask;

public class WarlockTimer {
	static private Timer timer = new Timer();
	
	private WarlockTimerTask task;
	private Property<Integer> value = new Property<Integer>(0);
	private long end = 0; // 0 for no roundtime or roundtime already setup
	
	private class WarlockTimerTask extends TimerTask {
		public void run() {
			update();
		}
	}
	
	private synchronized void update() {
		int newValue = value.get() - 1;

		if (newValue <= 0) {
			task.cancel();
			task = null;
			clear();
			return;
		}
		
		value.set(newValue);
	}
	
	/*
	 * Sync sets up the timer if we previously set the end.
	 */
	public synchronized void sync(long time) {
		if(end == 0) // no roundtime or it's already setup
			return;
		
		if(end > time)
			value.set((int)(end - time));
		else
			value.set(0);
		
		// Set end to 0 to signify that we setup the timer
		end = 0;
		
		if(task != null)
			task.cancel();
		task = new WarlockTimerTask();
		timer.scheduleAtFixedRate(task, 1000, 1000);
	}
	
	private void clear() {
		value.set(0);
		this.notifyAll();
	}
	
	public Property<Integer> getProperty() {
		return value;
	}
	
	public int getValue() {
		return value.get();
	}
	
	public synchronized void waitForEnd() throws InterruptedException {
		// while we have a roundtime or a roundtime being setup.
		while (value.get() > 0 || end > 0) {
			wait();
		}
	}
	
	/**
	 * Set up a roundtime to start with the next time sync.
	 * @param end The end of the roundtime as sent from the server.
	 */
	public void setup(long end) {
		this.end = end;
	}
}
