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
package cc.warlock.core.client;

public class WarlockStyle implements IWarlockStyle {

	public static final String BOLD = "bold";
	public static final String ROOM_NAME = "roomName";
	public static final String SPEECH = "speech";
	public static final String WHISPER = "whisper";
	public static final String THOUGHT = "thought";
	public static final String WATCHING = "watching";
	public static final String LINK = "link";
	public static final String SELECTED_LINK = "selectedLink";
	public static final String COMMAND = "command";
	public static final String ECHO = "echo";
	public static final String DEBUG = "debug";
	
	public static final WarlockStyle echoStyle = new WarlockStyle(ECHO);
	public static final WarlockStyle boldStyle = new WarlockStyle(BOLD);
	public static final WarlockStyle commandStyle = new WarlockStyle(COMMAND);
	public static final WarlockStyle debugStyle = new WarlockStyle(DEBUG);
	
	private WarlockColor foregroundColor = new WarlockColor(WarlockColor.DEFAULT_COLOR);
	private WarlockColor backgroundColor = new WarlockColor(WarlockColor.DEFAULT_COLOR);
	private boolean fullLine;
	private String name;
	private String componentName;
	private Runnable action;
	private String sound = new String();
	private boolean bold;
	private boolean italic;
	private boolean underline;
	private boolean monospace;
	
	public WarlockStyle(String name) {
		this.name = name;
	}
	
	public WarlockStyle () {
	}
	
	public WarlockStyle (IWarlockStyle other)
	{
		
		this.backgroundColor = new WarlockColor(other.getBackgroundColor());
		this.foregroundColor = new WarlockColor(other.getForegroundColor());
		this.name = other.getName();
		this.componentName = other.getComponentName();
		this.action = other.getAction();
		this.bold = other.isBold();
		this.italic = other.isItalic();
		this.underline = other.isUnderline();
		
		this.fullLine = other.isFullLine();
		this.setSound(other.getSound());
	}
	
	public Runnable getAction() {
		return action;
	}
	
	public boolean isBold() {
		return bold;
	}
	
	public boolean isItalic() {
		return italic;
	}
	
	public boolean isUnderline() {
		return underline;
	}
	
	public boolean isMonospace() {
		return monospace;
	}
	
	public boolean isFullLine() {
		return fullLine;
	}
	
	public void setFullLine(boolean fullLine) {
		this.fullLine = fullLine;
	}
	
	public String getName() {
		return name;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	
	public String getComponentName() {
		return componentName;
	}
	
	public WarlockColor getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(WarlockColor foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public WarlockColor getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(WarlockColor backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public String getSound() {
		return sound;
	}
	
	public void setAction(Runnable action) {
		this.action = action;
	}
	
	public void setSound(String sound){
		this.sound = sound;
	}
	
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	
	public void setItalic(boolean italic) {
		this.italic = italic;
	}
	
	public void setUnderline(boolean underline) {
		this.underline = underline;
	}
	
	public void setMonospace(boolean monospace) {
		this.monospace = monospace;
	}
	
	public void mergeWith(IWarlockStyle style) {
		WarlockColor fg = style.getForegroundColor();
		if(fg != null && !fg.equals(WarlockColor.DEFAULT_COLOR))
			setForegroundColor(fg);
		WarlockColor bg = style.getForegroundColor();
		if(bg != null && !bg.equals(WarlockColor.DEFAULT_COLOR))
			setBackgroundColor(bg);
		if(style.isBold())
			setBold(true);
		if(style.isItalic())
			setItalic(true);
		if(style.isUnderline())
			setUnderline(true);
		if(style.isMonospace())
			setMonospace(true);
	}
}
