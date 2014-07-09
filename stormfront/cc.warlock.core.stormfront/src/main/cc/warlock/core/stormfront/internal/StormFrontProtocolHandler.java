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
 * Created on Feb 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.stormfront.internal;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.WarlockStringMarker;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.IStormFrontTagHandler;
import cc.warlock.core.stormfront.tags.ATagHandler;
import cc.warlock.core.stormfront.tags.AppTagHandler;
import cc.warlock.core.stormfront.tags.BTagHandler;
import cc.warlock.core.stormfront.tags.CasttimeTagHandler;
import cc.warlock.core.stormfront.tags.ClearContainerTagHandler;
import cc.warlock.core.stormfront.tags.ClearStreamTagHandler;
import cc.warlock.core.stormfront.tags.CmdlistTagHandler;
import cc.warlock.core.stormfront.tags.CmdtimestampTagHandler;
import cc.warlock.core.stormfront.tags.CompDefTagHandler;
import cc.warlock.core.stormfront.tags.CompassTagHandler;
import cc.warlock.core.stormfront.tags.ComponentTagHandler;
import cc.warlock.core.stormfront.tags.DTagHandler;
import cc.warlock.core.stormfront.tags.DialogDataTagHandler;
import cc.warlock.core.stormfront.tags.IndicatorTagHandler;
import cc.warlock.core.stormfront.tags.InvTagHandler;
import cc.warlock.core.stormfront.tags.LaunchURLTagHandler;
import cc.warlock.core.stormfront.tags.LeftTagHandler;
import cc.warlock.core.stormfront.tags.MenuTagHandler;
import cc.warlock.core.stormfront.tags.ModeTagHandler;
import cc.warlock.core.stormfront.tags.NavTagHandler;
import cc.warlock.core.stormfront.tags.OutputTagHandler;
import cc.warlock.core.stormfront.tags.PlayerIDTagHandler;
import cc.warlock.core.stormfront.tags.PopBoldTagHandler;
import cc.warlock.core.stormfront.tags.PopStreamTagHandler;
import cc.warlock.core.stormfront.tags.PresetTagHandler;
import cc.warlock.core.stormfront.tags.PromptTagHandler;
import cc.warlock.core.stormfront.tags.PushBoldTagHandler;
import cc.warlock.core.stormfront.tags.PushStreamTagHandler;
import cc.warlock.core.stormfront.tags.ResourceTagHandler;
import cc.warlock.core.stormfront.tags.RightTagHandler;
import cc.warlock.core.stormfront.tags.RoundtimeTagHandler;
import cc.warlock.core.stormfront.tags.SentSettingsTagHandler;
import cc.warlock.core.stormfront.tags.SettingsInfoTagHandler;
import cc.warlock.core.stormfront.tags.SettingsTagHandler;
import cc.warlock.core.stormfront.tags.SpellTagHandler;
import cc.warlock.core.stormfront.tags.StreamTagHandler;
import cc.warlock.core.stormfront.tags.StreamWindowTagHandler;
import cc.warlock.core.stormfront.tags.StyleTagHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


/**
 * @author sproctor
 */
public class StormFrontProtocolHandler implements IStormFrontProtocolHandler {
	
	protected IWarlockClient client;
	protected HashMap<String, IStormFrontTagHandler> defaultTagHandlers = new HashMap<String, IStormFrontTagHandler>();
	protected Stack<String> tagStack = new Stack<String>();
	private Stack<String> streamStack = new Stack<String>();
	protected Stack<WarlockStringMarker> styleStack = new Stack<WarlockStringMarker>();
	private IWarlockStyle outputStyle = null;
	private WarlockString buffer = new WarlockString();
	protected int currentSpacing = 0;
	protected int monsterCount = 0;
	private boolean lineHasTag = false;
	private boolean lineHasContent = false;
	private HashMap<String, String> menuData = new HashMap<String, String>();
	
 	public StormFrontProtocolHandler(IWarlockClient client) {
		
		this.client = client;
		
		// server settings handlers
		new PlayerIDTagHandler(this);
		new ModeTagHandler(this);
		new SettingsTagHandler(this);
		new SettingsInfoTagHandler(this);
		new CmdlistTagHandler(this);
		new CmdtimestampTagHandler(this);
		new SentSettingsTagHandler(this);
		
		// Register the handlers
		new AppTagHandler(this);
		new DialogDataTagHandler(this);
		new PromptTagHandler(this);
		new RoundtimeTagHandler(this);
		new CasttimeTagHandler(this);
		new CompDefTagHandler(this); // for the room stream
		new NavTagHandler(this); // for nextRoom notification
		new CompassTagHandler(this);
		new MenuTagHandler(this);
		
		// stream handlers
		new PushStreamTagHandler(this);
		new PopStreamTagHandler(this);
		new ClearStreamTagHandler(this);
		new StreamTagHandler(this);
		new StreamWindowTagHandler(this);
		
		// Container handlers
		new ClearContainerTagHandler(this);
		new InvTagHandler(this);
		
		new SpellTagHandler(this);
		new LeftTagHandler(this);
		new RightTagHandler(this);
		new ComponentTagHandler(this);
		new StyleTagHandler(this);
		new OutputTagHandler(this);
		
		new PushBoldTagHandler(this);
		new PopBoldTagHandler(this);
		new PresetTagHandler(this);
		new IndicatorTagHandler(this);
		new LaunchURLTagHandler(this);
		new ResourceTagHandler(this);
		new ATagHandler(this);
		new BTagHandler(this);
		new DTagHandler(this);
		
		//new StubTagHandler(this); // handles known tags that don't have an implementation.
	}
	
	/*
	 * This function is to register handlers for xml tags
	 */
	public void registerHandler(IStormFrontTagHandler tagHandler) {
		defaultTagHandlers.put(tagHandler.getTagName(), tagHandler);
	}
	
	/*
	 * The purpose of this function is painfully obvious.
	 */
	public IWarlockClient getClient() {
		return client;
	}
	
	/*
	 *  push a stream onto the stack
	 */
	public void pushStream(String streamId) {
		clearStyles();
		flushBuffer();
		//IStream stream = client.getStream(streamId);
		streamStack.push(streamId);
		lineHasContent = false;
	}
	
	public void popStream() {
		clearStyles();
		flushBuffer();
		try {
			streamStack.pop();
		} catch(EmptyStackException e) {
			e.printStackTrace();
		}
		lineHasContent = false;
	}

	public String getCurrentStream() {
		return streamStack.peek();
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(String characters) {
		// if there was no handler or it couldn't handle the characters,
		// take a default action
		if(!handleCharacters(characters)) {
			String str = characters;
			// Suppress newlines following tags when the line is empty
			if(lineHasTag && !lineHasContent) {
				if(str.startsWith("\n"))
					str = str.substring(1);
				else if(str.startsWith("\r\n"))
					str = str.substring(2);
			}
			
			// user the buffer
			buffer.append(str);
			if(styleStack.empty() && tagStack.empty())
				flushBuffer();
			
			if(characters.contains("\n"))
				lineHasTag = false;
			
			// I don't think we need to handle line endings with \n\r or \r
			lineHasContent = !characters.endsWith("\n");
		}
	}
	
	private boolean handleCharacters(String characters) {
		// Start looking for handlers at the highest level tag
		for(int pos = tagStack.size() - 1; pos >= 0; pos--) {
			String tagName = tagStack.get(pos);
		
			IStormFrontTagHandler tagHandler = getTagHandlerForElement(tagName);
			if(tagHandler != null) {
				//tagHandler.setCurrentTag(tagName);
				// if the handler handled the characters, we're done
				if(tagHandler.handleCharacters(characters))
					return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String name, String rawXML) {
		// Get the tag name off the stack
		if(tagStack.empty() || !name.equals(tagStack.peek())) {
			System.err.println("Unexpected close tag \"" + name + "\".");
		} else {
			tagStack.pop();
		}
		
		// call the method for the object
		IStormFrontTagHandler tagHandler = getTagHandlerForElement(name);
		if(tagHandler != null) {
			//tagHandler.setCurrentTag(name);
			tagHandler.handleEnd(rawXML);
		}
		
		lineHasTag = true;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String name, StormFrontAttributeList attributes, String rawXML) {
		
		// call the method for the object
		IStormFrontTagHandler tagHandler = getTagHandlerForElement(name);
		
		tagStack.push(name);
		
		if(tagHandler != null) {
			//tagHandler.setCurrentTag(name);
			tagHandler.handleStart(attributes, rawXML);
		} else {
			System.err.println("Unhandled tag \"" + name + "\". Probably an unsupported tag.");
		}
		
		lineHasTag = true;
	}
	
	private IStormFrontTagHandler getTagHandlerForElement(String name) {
		if(tagStack.empty())
			return defaultTagHandlers.get(name);
		
		IStormFrontTagHandler rv = getTagHandlerForElementHelper(name, null, 0, false);
		if(rv == null)
			rv = defaultTagHandlers.get(name);
		return rv;
	}
	
	private IStormFrontTagHandler getTagHandlerForElementHelper(String name,
			IStormFrontTagHandler parentHandler, int stackPosition, boolean origParent) {
		if(stackPosition < tagStack.size()) {
			// Walk down the stack until we hit the bottom, then search for our tag out the way back out
			String tagName = tagStack.get(stackPosition);
			
			// next handler is the child the parent
			IStormFrontTagHandler nextHandler;
			if(parentHandler != null)
				nextHandler = parentHandler.getTagHandler(tagName);
			else
				nextHandler = defaultTagHandlers.get(tagName);
			
			// If we just searched for the tag we're looking for, return the result.
			if(name.equals(tagName))
				return nextHandler;
			
			// If we found a handler, then it's original
			boolean nextIsOrig = nextHandler != null;
			
			// If there was no match, use the current parent (not original)
			if(nextHandler == null) {
				nextHandler = parentHandler;
			}
			
			// see if there is a valid handler further down the tree.
			IStormFrontTagHandler tagHandler = getTagHandlerForElementHelper(name,
					nextHandler, stackPosition + 1, nextIsOrig);
			if(tagHandler != null) {
				return tagHandler;
			} else {
				if(origParent)
					return parentHandler.getTagHandler(name);
				else
					return null;
			}
		} else {
			if(origParent)
				return parentHandler.getTagHandler(name);
			else
				return null;
		}
	}
	
	public void addStyle(IWarlockStyle style) {
		WarlockStringMarker marker = new WarlockStringMarker(style,
				buffer.length(), buffer.length());
		
		if(!styleStack.empty()) {
			WarlockStringMarker lastStyle = styleStack.peek();
			lastStyle.addMarker(marker);
		} else {
			buffer.addMarker(marker);
		}
		
		styleStack.push(marker);
		
		lineHasContent = true;
	}
	
	public void removeStyle(IWarlockStyle style) {
		if(styleStack.empty() || styleStack.peek().getStyle() != style)
			return;
		
		WarlockStringMarker marker = styleStack.pop();
		marker.setEnd(buffer.length());
		
		if(styleStack.empty() && tagStack.empty()) {
			flushBuffer();
		}
	}
	
	public void flushBuffer() {
		if(outputStyle != null)
			buffer.addStyle(outputStyle);
		if(streamStack.empty()) {
			client.put(buffer);
		} else {
			client.put(streamStack.peek(), buffer);
		}
		buffer = new WarlockString();
	}
	
	public void clearStyles() {
		while(!styleStack.empty()) {
			WarlockStringMarker marker = styleStack.pop();
			marker.setEnd(buffer.length());
		}
	}
	
	public void resetMonsterCount() {
		monsterCount = 0;
	}
	
	public void incrementMonsterCount() {
		monsterCount++;
	}
	
	public int getMonsterCount() {
		return monsterCount;
	}
	
	public WarlockString getBuffer() {
		return buffer;
	}
	
	public void clearBuffer() {
		buffer = new WarlockString();
		lineHasContent = false;
	}
	
	public void clearStreams() {
		streamStack.clear();
	}
	
	public void setOutputStyle(IWarlockStyle style) {
		flushBuffer();
		outputStyle = style;
	}
	
	public void setMenuData(String id, String data) {
		menuData.put(id, data);
	}
	
	public String getMenuData(String id) {
		return menuData.get(id);
	}
}
