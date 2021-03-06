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
package cc.warlock.rcp.plugin;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import cc.warlock.rcp.ui.client.WarlockClientAdaptable;
import cc.warlock.rcp.ui.client.WarlockClientAdapterFactory;
import cc.warlock.rcp.ui.macros.MacroRegistry;

/**
 * The main plugin class to be used in the desktop.
 */
public class Warlock2Plugin extends AbstractUIPlugin {
	//The shared instance.
	private static Warlock2Plugin plugin;
	
	public static final String PLUGIN_ID = "cc.warlock.rcp";
	
	/**
	 * The constructor.
	 */
	public Warlock2Plugin() {
		super();
		
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		// force load of the Macro registry
		MacroRegistry.instance();
		
		Platform.getAdapterManager().registerAdapters(new WarlockClientAdapterFactory(), WarlockClientAdaptable.class);
	}
	
	public IExtension[] getExtensions (String extensionPoint)
	{
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint(extensionPoint);
		IExtension[] extensions = ep.getExtensions();
		
		return extensions;
	}

	/**
	 * Returns the shared instance.
	 */
	public static Warlock2Plugin getDefault() {
		return plugin;
	}
}
