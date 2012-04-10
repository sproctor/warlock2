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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.settings.internal.DirectorySetting;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.IScriptFileInfo;
import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.script.IScriptProvider;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.configuration.ScriptConfiguration;

public class FilesystemScriptProvider implements IScriptProvider {
	
	protected static FilesystemScriptProvider instance = new FilesystemScriptProvider();
	protected boolean forcedScan = false;
	protected Thread scanningThread;
	
	static {
		ScriptEngineRegistry.addScriptProvider(instance());
	}
	
	public static FilesystemScriptProvider instance()
	{
		return instance;
	}
	
	protected class ScriptFileInfo implements IScriptFileInfo, Comparable<ScriptFileInfo>
	{
		public String scriptName;
		public File scriptFile;
		public String contents;
		
		public File getScriptFile() {
			return scriptFile;
		}
		
		public String getScriptName() {
			return scriptName;
		}
		
		public int compareTo(ScriptFileInfo o) {
			return scriptName.compareTo(o.scriptName);
		}
		
		public String getExtension() {
			if (scriptFile.getName().indexOf(".") > 0)
			{
				return scriptFile.getName().substring(scriptFile.getName().lastIndexOf('.') + 1);
			}
			return null;
		}
		
		public Reader openReader() {
			try {
				return new FileReader(scriptFile);
			} catch (FileNotFoundException e) {
				return null;
			}
		}
	}
	
	private FilesystemScriptProvider () { }

	public IScript startScript(IScriptInfo scriptInfo, IWarlockClientViewer viewer, String[] arguments) {
		for (IScriptEngine engine : ScriptEngineRegistry.getScriptEngines())
		{
			if (engine.supports(scriptInfo))
			{
				return engine.startScript(scriptInfo, viewer, arguments);
			}
		}
		return null;
	}
	
	protected void scanDirectory (Collection<IScriptInfo> infos, File directory)
	{
		if (!directory.exists())
			return;
		
		for (File file : directory.listFiles())
		{
			if (file.isFile())
			{
				ScriptFileInfo info = new ScriptFileInfo();
				info.scriptFile = file;
				info.scriptName = file.getName();
				if (info.scriptName.indexOf('.') > 0)
				{
					info.scriptName = info.scriptName.substring(0, info.scriptName.indexOf('.'));
				}

				infos.add(info);
			}
		}
	}
	
	public Collection<IScriptInfo> getScriptInfos() {
		ArrayList<IScriptInfo> infos = new ArrayList<IScriptInfo>();
		for (DirectorySetting setting : ScriptConfiguration.instance().getScriptDirectories())
		{
			File dir = setting.getDirectory();
			if (dir.exists())
			{
				scanDirectory(infos, dir);
			}
		}
		return infos;
	}
}
