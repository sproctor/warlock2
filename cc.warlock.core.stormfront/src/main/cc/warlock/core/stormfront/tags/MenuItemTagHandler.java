package cc.warlock.core.stormfront.tags;

import cc.warlock.core.client.Command;
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
		String noun = attributes.getValue("noun");
		CliSetting cli = CmdlistSettings.getProvider(getHandler().getClient().getClientSettings()).getCli(coord);
		if(cli == null) {
			System.err.print("Bad coord in menu item.");
			return;
		}
		//System.out.println("menu item: category - " + cli.getCategory() + ", menu text - " + cli.getMenu()
		//		+ ", command - " + cli.getCommand());
		String menuText = cli.getMenu().replace("@", "");
		if(noun != null)
			menuText = menuText.replace("%", noun);
		String tempCommand = cli.getMenu().replace("@", getHandler().getMenuData(id));
		if(noun != null)
			tempCommand = tempCommand.replace("%", noun);
		final String command = tempCommand;
		getHandler().getClient().getViewer().addMenuItem(id, cli.getCategory(), menuText, new Runnable() {
			@Override
			public void run() {
				System.out.print("Running command: " + command);
				
				getHandler().getClient().send(new Command(command));
			}
		});
	}
}
