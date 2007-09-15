package cc.warlock.stormfront.internal;

import java.util.Map;

import cc.warlock.stormfront.IStormFrontProtocolHandler;


public class StreamTagHandler extends DefaultTagHandler {

	public StreamTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "stream" };
	}

	 @Override
	public void handleStart(Map<String,String> attributes) {
		 String id = attributes.get("id");
		 if (id != null)
		 {
			String streamId = new String(id);
			
			handler.pushStream(streamId);
		 }
	}
	 
	 @Override
	public void handleEnd() {
		handler.popStream();
	}
}