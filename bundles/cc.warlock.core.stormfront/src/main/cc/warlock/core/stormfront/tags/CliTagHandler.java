package cc.warlock.core.stormfront.tags;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.settings.CliSetting;
import cc.warlock.core.stormfront.settings.CmdlistSettings;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class CliTagHandler extends BaseTagHandler {
	private IStormFrontProtocolHandler handler;
	
	public CliTagHandler(IStormFrontProtocolHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public String getTagName() {
		return "cli";
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		String coord = attributes.getValue("coord");
		String menu = attributes.getValue("menu");
		String command = attributes.getValue("command");
		String menu_cat = attributes.getValue("menu_cat");
		
		CmdlistSettings provider = CmdlistSettings.getProvider(handler.getClient().getClientSettings());
		CliSetting setting = provider.getCli(coord);
		setting.setCommand(command);
		setting.setMenu(menu);
		setting.setCategory(menu_cat);
	}
}
