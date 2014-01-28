package cc.warlock.core.stormfront.tags;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.settings.CliSetting;
import cc.warlock.core.stormfront.settings.CmdlistSettings;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class MenuItemTagHandler extends DefaultTagHandler {
	private MenuTagHandler menuHandler;
	
	public MenuItemTagHandler(IStormFrontProtocolHandler handler, MenuTagHandler menuHandler) {
		super(handler);
		
		this.menuHandler = menuHandler;
	}
	
	@Override
	public String getTagName() {
		return "mi";
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		String id = menuHandler.getId();
		String coord = attributes.getValue("coord");
		final CliSetting cli = CmdlistSettings.getProvider(handler.getClient().getClientSettings()).getCli(coord);
		if(cli == null) {
			System.err.print("Bad coord in menu item.");
			return;
		}
		handler.getClient().getViewer().addMenuItem(id, cli.getMenuCat() + " " + cli.getMenu(), new Runnable() {
			@Override
			public void run() {
				System.out.print("Running command: " + cli.getCommand());
				//cli.getCommand();
			}
		});
	}
}
