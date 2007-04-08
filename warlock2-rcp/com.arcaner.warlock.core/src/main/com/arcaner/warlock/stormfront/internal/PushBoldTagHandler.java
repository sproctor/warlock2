package com.arcaner.warlock.stormfront.internal;

import com.arcaner.warlock.client.stormfront.internal.StormFrontStyle;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class PushBoldTagHandler extends DefaultTagHandler {

	public PushBoldTagHandler (IStormFrontProtocolHandler handler)
	{
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "pushBold" };
	}
	
	public void handleEnd() {
		handler.setCurrentStyle(StormFrontStyle.BOLD_STYLE);
//		handler.pushBuffer();
	}
}
