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
package cc.warlock.core.client.logging;

import java.io.File;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.configuration.WarlockSetting;


public class LoggingConfiguration extends WarlockSetting
{
	public static final String ID = "logs";
	public static final String LOG_FORMAT_TEXT = "text";
	public static final String LOG_FORMAT_HTML = "html";
	
	protected String logFormat;
	protected boolean enableLogging;
	protected File logDirectory;
	
	public LoggingConfiguration (Preferences parentNode) {
		super(parentNode, ID);
		logFormat = getNode().get("format", LOG_FORMAT_TEXT);
		enableLogging = getNode().getBoolean("enabled", true);
		String dirName = getNode().get("dir", null);
		if(dirName == null)
			logDirectory = ConfigurationUtil.getConfigurationDirectory("logs", true);
		else
			logDirectory =  new File(dirName.trim());
	}

	public String getLogFormat() {
		return logFormat;
	}

	public void setLogFormat(String logFormat) {
		getNode().put("format", logFormat);
		this.logFormat = logFormat;
	}

	public boolean isLoggingEnabled() {
		return enableLogging;
	}

	public void setLoggingEnabled(boolean enableLogging) {
		getNode().putBoolean("enabled", enableLogging);
		this.enableLogging = enableLogging;
	}

	public File getLogDirectory() {
		return logDirectory;
	}

	public void setLogDirectory(File logDirectory) {
		getNode().put("dir", logDirectory.getAbsolutePath());
		this.logDirectory = logDirectory;
	}

	public static LoggingConfiguration getProvider(IClientSettings clientSettings) {
		return (LoggingConfiguration)clientSettings.getProvider(ID);
	}
}
