/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.stormfront.tags;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


/**
 * @author Sean Proctor
 */
public class PromptTagHandler extends DefaultTagHandler {
	
	protected String prompt;
	
	public PromptTagHandler (IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "prompt" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		prompt = "";
		handler.flushBuffer();
		handler.clearStyles();
		handler.clearStreams();
		
		String time = attributes.getValue("time");
		if (time != null)
			handler.getClient().syncTime(new Long(time));
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		prompt += characters;
		return true;
	}
	
	@Override
	public void handleEnd(String rawXML) {
		handler.getClient().prompt(prompt);
	}
}
