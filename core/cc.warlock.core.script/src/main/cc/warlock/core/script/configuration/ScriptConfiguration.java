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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.internal.Property;
import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.configuration.WarlockPreferences;
import cc.warlock.core.configuration.WarlockSetting;

public class ScriptConfiguration extends WarlockSetting {

	protected ScriptDirectoryConfiguration scriptDirectories;
	protected Property<Boolean> suppressExceptions;
	protected String scriptPrefix;
	
	protected HashMap<String, ArrayList<String>> engineExtensions = new HashMap<String, ArrayList<String>>();
	
	protected static ScriptConfiguration instance = new ScriptConfiguration();
	
	public static ScriptConfiguration instance() {
		return instance;
	}
	
	private ScriptConfiguration () {
		super(WarlockPreferences.getInstance().getNode(), "scripting");
		suppressExceptions = new Property<Boolean>(getNode().getBoolean("suppress-exceptions", true));
		scriptPrefix = getNode().get("prefix", ".");
		
		scriptDirectories = new ScriptDirectoryConfiguration(this.getNode());
		if(scriptDirectories.getSettings().isEmpty()) {
			scriptDirectories.add(ConfigurationUtil.getUserDirectory("scripts", false));
			scriptDirectories.add(ConfigurationUtil.getUserDirectory("warlock-scripts", false));
			scriptDirectories.add(ConfigurationUtil.getConfigurationDirectory("scripts", false));
		}
	}
	
	public IProperty<Boolean> getSupressExceptions() {
		return suppressExceptions;
	}
	
	public Set<File> getScriptDirectories ()
	{
		return scriptDirectories;
	}

	public String getScriptPrefix() {
		return scriptPrefix;
	}

	public void setScriptPrefix(String scriptPrefix) {
		this.scriptPrefix = scriptPrefix;
	}
}
