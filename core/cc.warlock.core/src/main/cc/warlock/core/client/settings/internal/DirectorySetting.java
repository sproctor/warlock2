package cc.warlock.core.client.settings.internal;

import java.io.File;

import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.configuration.IWarlockSetting;
import cc.warlock.core.configuration.WarlockSetting;

public class DirectorySetting extends WarlockSetting {
	private String type;
	private String name;
	
	private File directory = null;
	
	public DirectorySetting (IWarlockSetting parent, String path) {
		super(parent, path);
		name = getNode().get("name", null);
		type = getNode().get("type", "absolute");
	}
	
	public File getDirectory() {
		if(directory == null)
			loadDirectory();
		
		return directory;
	}
	
	private void loadDirectory() {
		if(type.equals("user"))
			directory = ConfigurationUtil.getUserDirectory(name, false);
		
		if(type.equals("config"))
			directory = ConfigurationUtil.getConfigurationDirectory(name, false);
		
		directory = ConfigurationUtil.getAbsoluteDirectory(name, false);
	}
	
	public void setDirectory(String name, String type) {
		this.getNode().put("type", type);
		this.type = type;
		
		this.getNode().put("name", name);
		this.name = name;
		
		directory = null;
	}
	
	public void setDirectory(String name) {
		setDirectory(name, "absolute");
	}
}
