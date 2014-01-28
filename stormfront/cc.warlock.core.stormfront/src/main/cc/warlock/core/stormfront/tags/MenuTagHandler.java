package cc.warlock.core.stormfront.tags;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class MenuTagHandler extends DefaultTagHandler {
	private String id;
	
	public MenuTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		
		addTagHandler(new MenuItemTagHandler(handler, this));
	}
	
	@Override
	public String getTagName() {
		return "menu";
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		id = attributes.getValue("id");
	}
	
	public String getId() {
		return id;
	}
}
