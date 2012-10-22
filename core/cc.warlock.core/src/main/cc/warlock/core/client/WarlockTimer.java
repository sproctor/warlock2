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

import cc.warlock.core.client.internal.Property;

public class WarlockTimer {
	static private Timer timer = new Timer();
	
	private WarlockTimerTask task;
	private Property<Integer> value = new Property<Integer>();
	private int length;
	private long timeDelta;
	private Long end;
	
	private class WarlockTimerTask extends TimerTask {
		public void run() {
			update();
		}
	}
	
	private synchronized void update() {
		// Synchronize with external roundtime updates
		long now = System.currentTimeMillis();
		long useconds = 0;
		
		
		if (end != null)
			useconds = end * 1000L + timeDelta - now;

		if (useconds <= 0) {
			end = null;
			length = 0;
			value.set(0);
			task.cancel();
			task = null;
			this.notifyAll();
			return;
		}
		
		// Update the world with the new roundtime
		// Avoid flicker caused by redundant updates
		int newValue = (int)((useconds + 999) / 1000);
		if (value.get() != newValue)
			value.set(newValue);

		//if(task == null) {
			// Compute how long until next roundtime update
			long waitTime = useconds % 1000;
			if (waitTime == 0)
				waitTime = 1000;

			if(task != null)
				task.cancel();
			task = new WarlockTimerTask();
			timer.scheduleAtFixedRate(task, waitTime, 1000);
		//}
	}
	
	public void sync(long time) {
		if(end == null)
			return;
		
		long newTimeDelta = System.currentTimeMillis() - time * 1000L;
		
		if (length > 0) {
			// Don't decrease timeDelta while roundtimes are active.
			if (newTimeDelta > timeDelta)
				timeDelta = newTimeDelta;
			return;
		}
		
		timeDelta = newTimeDelta;
		
		if (end > time) {
			// We need to do this now due to scheduling delays in the thread
			length = (int)(end - time);
			value.set(length);
			update();
		} else {
			value.set(0);
			end = null;
			length = 0;
			this.notifyAll();
		}
	}
	
	public Property<Integer> getProperty() {
		return value;
	}
	
	public int getValue() {
		return value.get();
	}
	
	public int getLength() {
		return length;
	}
	
	
	public void waitForEnd() throws InterruptedException {
		while (end != null) {
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
