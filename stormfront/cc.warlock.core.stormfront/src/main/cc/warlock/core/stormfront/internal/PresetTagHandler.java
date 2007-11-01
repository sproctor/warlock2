package cc.warlock.core.stormfront.internal;

import java.util.Stack;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class PresetTagHandler extends DefaultTagHandler {

	private Stack<IWarlockStyle> styles = new Stack<IWarlockStyle>();
	
	public PresetTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "preset" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes) {
		String id = attributes.getValue("id");
		IWarlockStyle style = WarlockStyle.createCustomStyle(id);
		
		styles.push(style);
		handler.getCurrentStream().addStyle(style);
	}
	
	@Override
	public void handleEnd() {
		IWarlockStyle style = styles.pop();
		handler.getCurrentStream().removeStyle(style);
	}

}
