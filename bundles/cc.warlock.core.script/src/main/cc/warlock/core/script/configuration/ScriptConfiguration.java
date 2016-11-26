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
package cc.warlock.core.script.configuration;

import java.util.Collection;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.Property;
import cc.warlock.core.settings.DirectorySetting;
import cc.warlock.core.settings.WarlockPreferencesScope;
import cc.warlock.core.settings.WarlockSetting;

public class ScriptConfiguration extends WarlockSetting {

	private ScriptDirectoryConfiguration directoryConf;
	private Property<Boolean> suppressExceptions;
	//private String scriptPrefix;
	
	//private HashMap<String, ArrayList<String>> engineExtensions = new HashMap<String, ArrayList<String>>();
	
	private static ScriptConfiguration instance = new ScriptConfiguration();
	
	public static ScriptConfiguration instance() {
		return instance;
	}
	
	private ScriptConfiguration () {
		super(null, "scripting");
		suppressExceptions = new Property<Boolean>(getNode().getBoolean("suppress-exceptions", true));
		//scriptPrefix = getNode().get("prefix", ".");
		
		directoryConf = new ScriptDirectoryConfiguration(this);
		if(directoryConf.getSettings().isEmpty()) {
			DirectorySetting confScripts = directoryConf.createSetting();
			confScripts.setDirectory("scripts", "config");
		}
	}
	
	@Override
	protected Preferences getParentNode() {
		return WarlockPreferencesScope.getInstance().getNode();
	}
	
	public IProperty<Boolean> getSupressExceptions() {
		return suppressExceptions;
	}
	
	public Collection<DirectorySetting> getScriptDirectories ()
	{
		return directoryConf.getSettings();
	}

	public ScriptDirectoryConfiguration getScriptDirectoryConfiguration() {
		return directoryConf;
	}
	
	public String getScriptPrefix() {
		return ".";
	}
}
