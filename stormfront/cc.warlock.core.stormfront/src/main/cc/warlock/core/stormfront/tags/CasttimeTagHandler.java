/**
 * 
 */
package cc.warlock.core.stormfront.tags;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

/**
 * @author kassah
 *
 */
public class CasttimeTagHandler extends DefaultTagHandler {

	public CasttimeTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.stormfront.internal.BaseTagHandler#getTagNames()
	 */
	@Override
	public String getTagName() {
		return "castTime";
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		handler.getClient().getTimer("casttime").setup(new Long(attributes.getValue("value")));
	}
}
