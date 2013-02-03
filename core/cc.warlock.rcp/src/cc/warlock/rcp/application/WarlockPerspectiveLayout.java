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
package cc.warlock.rcp.application;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.settings.IWarlockSetting;
import cc.warlock.core.settings.WarlockPreferencesScope;
import cc.warlock.core.settings.WarlockSetting;

public class WarlockPerspectiveLayout extends WarlockSetting implements IWarlockSetting {
	protected Rectangle bounds = new Rectangle(25, 25, 1024, 768);
	private static WarlockPerspectiveLayout instance = new WarlockPerspectiveLayout();
	
	public static WarlockPerspectiveLayout instance() {
		return instance;
	}
	
	protected WarlockPerspectiveLayout() {
		super(null, "window-layout");
		
		bounds.x = getNode().getInt("x", 25);
		bounds.y = getNode().getInt("y", 25);
		bounds.width = getNode().getInt("width", 1024);
		bounds.height = getNode().getInt("height", 768);
	}
	
	@Override
	protected Preferences getParentNode() {
		return WarlockPreferencesScope.getInstance().getNode();
	}
	
	public void saveLayout() {
		bounds = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getBounds();
		getNode().putInt("x", bounds.x);
		getNode().putInt("y", bounds.y);
		getNode().putInt("width", bounds.width);
		getNode().putInt("height", bounds.height);
	}
	
	public void loadBounds ()
	{
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setBounds(bounds);
	}
}
