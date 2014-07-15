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

import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


/**
 * @author Rob
 * 
 * An XPath listener that handles compDef, which 
 * puts forth information about the room you just entered 
 * and its exits.
 * THis is only activated if you ENTER the room. Not 
 * looking, or peering.
 */
public class CompDefTagHandler extends DefaultTagHandler {
	
	private String id;
	private StringBuffer buffer;
	private WarlockStyle style;
	
	public CompDefTagHandler (IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String getTagName() {
		return "compDef";
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		if(style != null) {
			getHandler().removeStyle(style);
			style = null;
		}
		buffer = new StringBuffer();
		
		id = attributes.getValue("id");
		if(id == null)
			return;
		if(id.equals("room objs"))
			getHandler().resetMonsterCount();
		
		style = new WarlockStyle();
		style.setComponentName(id);
		getHandler().addStyle(style);
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		if (id != null) {
			buffer.append(characters);
		}
		// let the stream have the text, we just want to store the value in a property
		return false;
	}
	
	@Override
	public void handleEnd(String rawXML) {
		if(style != null) {
			getHandler().removeStyle(style);
			style = null;
		}
		
		if(id == null)
			return;
		
		if(id.equals("room objs")) {
			int count = getHandler().getMonsterCount();
			getHandler().getClient().setProperty("monstercount", String.valueOf(count));
		}
		
		getHandler().getClient().setComponent(id, buffer.toString(), getHandler().getCurrentStream());
	}
}
