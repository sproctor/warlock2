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
package cc.warlock.core.client.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.prefs.Preferences;

import cc.warlock.core.client.Command;
import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.ICommandHistory;
import cc.warlock.core.client.ICommandHistoryListener;


/**
 * @author Marshall
 * 
 * A Command History implementation
 */
public class CommandHistory implements ICommandHistory {

	private int position = -1;
	private LinkedList<ICommand> commands = new LinkedList<ICommand>();
	private ArrayList<ICommandHistoryListener> listeners = new ArrayList<ICommandHistoryListener>();
	static private Preferences prefs = Preferences.userNodeForPackage(Command.class);
	
	public CommandHistory () {
		// load saved history
		byte[] array = prefs.getByteArray("command", null);
		if(array != null) {
			ByteArrayInputStream bytes = new ByteArrayInputStream(array);
			try {
				ObjectInputStream stream = new ObjectInputStream(bytes);
				commands = (LinkedList<ICommand>)stream.readObject();
				stream.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public ICommand getLastCommand() {
		try {
			return commands.getFirst();
		} catch(NoSuchElementException e) {
			return null;
		}
	}

	public ICommand next() {
		if (position >= 0)
			position--;

		ICommand command = current();
		for (ICommandHistoryListener listener : listeners) listener.historyNext(command);
		return command;
	}
	
	public ICommand prev() {
		if(position < size() - 1)
			position++;

		ICommand command = current();
		for (ICommandHistoryListener listener : listeners) listener.historyPrevious(command);

		return command;
	}
	
	private ICommand searchFromPos(String text, int pos) {
		if(pos < 0)
			pos = 0;
		while(pos < size()) {
			if(getCommandAt(pos).getCommand().contains(text)) {
				position = pos;
				return current();
			}
			pos++;
		}
		return null;
	}
	
	public ICommand search(String text) {
		return searchFromPos(text, position);
	}
	
	public ICommand searchBefore(String text) {
		return searchFromPos(text, position + 1);
	}

	public ICommand current () {
		return getCommandAt(position);
	}
	
	public int size() {
		return commands.size();
	}
	
	public ICommand getCommandAt(int pos) {
		try {
			ICommand command;
			if(pos == -1) command = null;
			else command = commands.get(pos);
			return command;
		}
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void resetPosition() {
		position = -1;
		
		for (ICommandHistoryListener listener : listeners) listener.historyReset(current());
	}
	
	public void addCommand(ICommand command) {
		
		if(commands.size() > 0 && command.getCommand().equals(commands.getFirst().getCommand()))
		{
			for (ICommandHistoryListener listener : listeners) {
				listener.commandRepeated(commands.getFirst());
			}
			return;
		}
		
		// command.setBoundForHistory(false);
		commands.addFirst(command);
		
		resetPosition();
		for (ICommandHistoryListener listener : listeners) listener.commandAdded(command);
	}
	
	public void save () {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			ObjectOutputStream stream = new ObjectOutputStream(bytes);
			stream.writeObject(commands);
			stream.flush();
			stream.close();
			prefs.putByteArray("command", bytes.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addCommandHistoryListener(ICommandHistoryListener listener) {
		listeners.add(listener);
	}
	
	public void removeCommandHistoryListener(ICommandHistoryListener listener) {
		listeners.remove(listener);
	}
}
