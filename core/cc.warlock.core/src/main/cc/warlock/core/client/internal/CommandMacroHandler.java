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
package cc.warlock.core.client.internal;

import java.util.Collection;

import cc.warlock.core.client.IMacro;
import cc.warlock.core.client.IMacroCommand;
import cc.warlock.core.client.IMacroHandler;
import cc.warlock.core.client.IMacroVariable;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.IWarlockEntry;

/**
 * This is the default macro handler for a command being sent to the connection.
 * Embedded macro variables and special-commands are supported, to allow for additional functionality.
 * 
 * @author marshall
 */
public class CommandMacroHandler implements IMacroHandler {

	private String command;
	
	public CommandMacroHandler (String command)
	{
		this.command = command;
	}
	
	public boolean handleMacro(IMacro macro, IWarlockClientViewer viewer)
	{
		Collection<IMacroVariable> variables = viewer.getMacroVariables();
		IWarlockEntry entry = viewer.getEntry();
		
		String newCommand = new String(command);
		String savedCommand = null;
		
		for (IMacroVariable var : variables)
		{
			String id = var.getIdentifier();
			if (newCommand.contains(id))
			{
				String newVar = id.replaceAll("\\$", "\\\\\\$");
				String value = var.getValue(viewer);
				if (value != null)
				{
					newCommand = newCommand.replaceAll(newVar, value);
				}
			}
		}
		
		for (int pos = 0; pos < newCommand.length(); pos++) {
			char curChar = newCommand.charAt(pos);
			if(curChar == '\\' && newCommand.length() > pos + 1) {
				pos++;
				curChar = newCommand.charAt(pos);
				switch(curChar) {
				
				// submit current text in entry
				case 'n':
				case 'r':
					entry.submit();
					break;
					
				// pause 1 second
				case 'p':
					// not sure how to implement pause
					break;
					
				// clear the entry
				case 'x':
					entry.setCurrentCommand("");
					break;
					
				// display a dialog to get the value
				case '?':
					// unimplemented
					break;
					
				// save current text in entry
				case 'S':
					savedCommand = entry.getText();
					break;
					
				// restore saved command
				case 'R':
					if(savedCommand != null)
						entry.setCurrentCommand(savedCommand);
					break;
				default:
					entry.append(curChar);
				}
			} else if(curChar == '{') {
				int endPos = newCommand.indexOf('}', pos);
				if(endPos == -1) {
					entry.append(curChar);
				} else {
					String commandText = newCommand.substring(pos + 1, endPos);
					pos = endPos + 1;
					IMacroCommand macroCommand = viewer.getMacroCommand(commandText);
					if(macroCommand != null) {
						macroCommand.execute(viewer);
					}
				}
			} else if(curChar == '@') {
				// Stub... Move cursor to this char position
			} else {
				entry.append(curChar);
			}
		}
		return true;
	}
	
	public String getCommand () {
		return this.command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	@Override
	public String toString() {
		return "Command(" + command + ")";
	}
}
