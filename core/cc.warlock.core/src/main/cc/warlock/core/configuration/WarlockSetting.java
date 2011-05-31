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
package cc.warlock.core.configuration;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;


/**
 * The base implementation class for all client settings
 * @author marshall
 */
public class WarlockSetting implements IWarlockSetting {

	private Preferences parentNode;
	private Preferences node;
	private boolean newSetting;
	
	public WarlockSetting (Preferences parentNode, String path)
	{
		this.parentNode = parentNode;
		try {
			newSetting = !parentNode.nodeExists(path);
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
		node = parentNode.node(path);
	}
	
	public Preferences getNode() {
		return node;
	}
	
	protected void changePath(String path) {
		try {
			node.removeNode();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
		node = parentNode.node(path);
	}
	
	public boolean isNewSetting() {
		return newSetting;
	}
	
	public void flush() {
		try {
			getNode().flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
