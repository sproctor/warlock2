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
package cc.warlock.core.script.wsl;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.script.AbstractScript;
import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.script.internal.ScriptCommands;
import cc.warlock.core.script.wsl.internal.ANTLRNoCaseReaderStream;
import cc.warlock.core.script.wsl.internal.IWSLValue;
import cc.warlock.core.script.wsl.internal.WSLAbstractCommand;
import cc.warlock.core.script.wsl.internal.WSLAbstractNumber;
import cc.warlock.core.script.wsl.internal.WSLAbstractString;
import cc.warlock.core.script.wsl.internal.WSLLexer;
import cc.warlock.core.script.wsl.internal.WSLParser;
import cc.warlock.core.script.wsl.internal.WSLString;

public class WSLScript extends AbstractScript {
	
	private HashMap<String, Integer> labels = new HashMap<String, Integer>();
	private HashMap<String, IWSLValue> specialVariables = new HashMap<String, IWSLValue>();
	private ArrayList<IWarlockHighlight> highlights = null;
	private ArrayList<WSLAbstractCommand> lines = new ArrayList<WSLAbstractCommand>();
	private WSLEngine engine;
	private IScriptCommands scriptCommands;
	private ArrayList<WSLScriptContext> contexts = new ArrayList<WSLScriptContext>();
	
	public WSLScript (WSLEngine engine, IScriptInfo info, IWarlockClientViewer viewer) {
		super(info, viewer);
		this.engine = engine;
		
		scriptCommands = new ScriptCommands(viewer, this);
		
		IWSLValue rt = new WSLRoundTime();
		setSpecialVariable("rt", rt);
		setSpecialVariable("roundtime", rt);
		setSpecialVariable("monstercount", new WSLMonsterCount());
		setSpecialVariable("lhand", new WSLLeftHand());
		setSpecialVariable("rhand", new WSLRightHand());
		setSpecialVariable("spell", new WSLSpell());
		setSpecialVariable("roomdesc", new WSLComponent("room desc"));
		setSpecialVariable("roomexits", new WSLComponent("room exits"));
		setSpecialVariable("roomplayers", new WSLComponent("room players"));
		setSpecialVariable("roomobjects", new WSLComponent("room objs"));
		setSpecialVariable("roomtitle", new WSLRoomTitle());
		setSpecialVariable("lastcommand", new WSLLastCommand());
		setSpecialVariable("character", new WSLCharacter());
	}

	public IWSLValue getVariable(String name) {
		// these values are maintained by the script
		IWSLValue val = specialVariables.get(name);
		if (val != null)
			return val;
		
		String var = scriptCommands.getStoredVariable(name);
		if (var != null)
			return new WSLString(var);
		
		return null;
	}
	
	public boolean variableExists(String name) {
		return specialVariables.containsKey(name) || scriptCommands.getStoredVariable(name) != null;
	}
	
	private class WSLRoundTime extends WSLAbstractNumber {
		public double toDouble(WSLScriptContext cx) {
			return getClient().getTimer("roundtime").getValue();
		}
	}
	
	private class WSLMonsterCount extends WSLAbstractNumber {
		public double toDouble(WSLScriptContext cx) {
			return Double.parseDouble((getClient().getProperty("monstercount").get()));
		}
	}
	
	private class WSLLeftHand extends WSLAbstractString {
		public String toString(WSLScriptContext cx) {
			return getClient().getProperty("left").get();
		}
	}
	
	private class WSLRightHand extends WSLAbstractString {
		public String toString(WSLScriptContext cx) {
			return getClient().getProperty("right").get();
		}
	}
	
	private class WSLSpell extends WSLAbstractString {
		public String toString(WSLScriptContext cx) {
			return getClient().getProperty("spell").get();
		}
	}
	
	private class WSLRoomTitle extends WSLAbstractString {
		public String toString(WSLScriptContext cx) {
			return getClient().getStreamTitle("room");
		}
	}
	
	private class WSLComponent extends WSLAbstractString {
		protected String componentName;
		public WSLComponent(String componentName) {
			this.componentName = componentName;
		}
		
		public String toString (WSLScriptContext cx) {
			return getClient().getComponent(componentName);
		}
	}
	
	private class WSLLastCommand extends WSLAbstractString {
		public String toString(WSLScriptContext cx) {
			return getClient().getLastCommand();
		}
	}
	
	private class WSLCharacter extends WSLAbstractString {
		public String toString(WSLScriptContext cx) {
			return getClient().getCharacterName();
		}
	}
	

	public void start (Collection<String> arguments)
	{
		super.start();
		
		StringBuffer totalArgs = new StringBuffer();
		int i = 1;
		for (String argument : arguments) {
			setSpecialVariable(Integer.toString(i), argument);
			if (i > 1)
				totalArgs.append(" ");
			totalArgs.append(argument);
			i++;
		}
		// populate the rest of the argument variable
		for(; i <= 9; i++) {
			setSpecialVariable(Integer.toString(i), "");
		}
		// set 0 to the entire list
		setSpecialVariable("0", totalArgs.toString());
		
		Thread scriptThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Reader scriptReader = info.openReader();
					
					CharStream input = new ANTLRNoCaseReaderStream(scriptReader);
					WSLLexer lex = new WSLLexer(input);
					CommonTokenStream tokens = new CommonTokenStream(lex);
					WSLParser parser = new WSLParser(tokens);

					parser.setScript(WSLScript.this);

					parser.script();
				} catch(IOException e) {
					e.printStackTrace();
					return;
				} catch (RecognitionException e) {
					e.printStackTrace();
					return;
				}
				WSLScriptContext context = new WSLScriptContext(WSLScript.this);
				contexts.add(context);
				context.run();
				if(isRunning())
					stop();
				
				if(highlights != null)
					getClient().removeHighlights(highlights);
			}
		});
		scriptThread.setName("Wizard Script: " + getName());
		getCommands().addThread(scriptThread);
		scriptThread.start();
	}
	
	public void addLabel(String label, int line) {
		labels.put(label.toLowerCase(), line - 1);
	}
	
	public int labelIndex(String label) {
		Integer line = labels.get(label.toLowerCase());
		if(line != null)
			return line;
		else
			return -1;
	}
	
	public void addLine(WSLAbstractCommand command) {
		lines.add(command);
	}
	
	public WSLAbstractCommand getLine(int lineNum) {
		return lines.get(lineNum);
	}
	
	public int numLines() {
		return lines.size();
	}
	
	protected void setGlobalVariable(String name, boolean value) {
		scriptCommands.setStoredVariable(name, value ? "true" : "false");
	}
	
	protected void setGlobalVariable(String name, double value) {
		String str = Math.floor(value) == value ?
				Long.toString((long)value) :
					Double.toString(value);
		setGlobalVariable(name, str);
	}
	
	protected void setGlobalVariable(String name, String value) {
		scriptCommands.setStoredVariable(name, value);
	}
	
	protected void setSpecialVariable(String name, String value) {
		setSpecialVariable(name, new WSLString(value));
	}
	
	protected void setSpecialVariable(String name, IWSLValue value) {
		deleteVariable(name);
		specialVariables.put(name, value);
	}
	
	protected boolean hasSpecialVariable(String name) {
		return specialVariables.containsKey(name);
	}
	
	protected void deleteVariable(String name) {
		scriptCommands.removeStoredVariable(name);
	}
	
	public IScriptEngine getScriptEngine() {
		return engine;
	}
	
	public IScriptCommands getCommands() {
		return scriptCommands;
	}
	
	public void addHighlight(IWarlockHighlight highlight) {
		if(highlights == null) {
			highlights = new ArrayList<IWarlockHighlight>();
		} else {
			// remove to reload highlights
			getClient().removeHighlights(highlights);
		}
		
		highlights.add(highlight);
		
		// add or re-add to reload client highlights
		getClient().addHighlights(highlights);
	}
	
	public boolean removeHighlight(String text) {
		if(highlights == null)
			return false;
		
		Iterator<IWarlockHighlight> iter = highlights.iterator();
		while(iter.hasNext()) {
			IWarlockHighlight highlight = iter.next();
			if(highlight.getText().equalsIgnoreCase(text)) {
				iter.remove();
				// remove and add to rebuild cache
				getClient().removeHighlights(highlights);
				getClient().addHighlights(highlights);
				return true;
			}
		}
		return false;
	}

}
