package cc.warlock.core.stormfront.settings;

import cc.warlock.core.settings.IWarlockSetting;
import cc.warlock.core.settings.WarlockSetting;

public class CliSetting extends WarlockSetting {
	private String coord;
	private String command;
	private String menu;
	private String category;
	
	public CliSetting(IWarlockSetting parent, String path) {
		super(parent, path);
		
		this.coord = getNode().get("coord", null);
		this.command = getNode().get("command", null);
		this.menu = getNode().get("menu", null);
		this.category = getNode().get("category", null);
	}

	public void setCood(String coord) {
		getNode().put("coord", coord);
		
		this.coord = coord;
	}
	
	public void setCommand(String command) {
		getNode().put("command", command);
		
		this.command = command;
	}
	
	public void setMenu(String menu) {
		getNode().put("menu", menu);
		
		this.menu = menu;
	}
	
	public void setCategory(String category) {
		getNode().put("category", category);
		
		this.category = category;
	}
	
	public String getCoord() {
		return coord;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getMenu() {
		return menu;
	}
	
	public String getCategory() {
		return category;
	}
}
