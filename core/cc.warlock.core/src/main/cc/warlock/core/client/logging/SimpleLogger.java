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
package cc.warlock.core.client.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.internal.WarlockClient;

/**
 * A simple text-based logger that rotates daily
 * 
 * @author marshall
 *
 */
public class SimpleLogger implements IStreamListener {

	protected IWarlockClient client;
	protected int maxBufferSize = 2000;
	protected StringBuffer buffer = new StringBuffer(maxBufferSize);
	protected boolean nextlineStamp = false;
	
	protected static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	protected static final DateFormat timeFormat = new SimpleDateFormat("[H:m:s] ");
	
	public SimpleLogger (IWarlockClient client) {
		this.client = client;
	}
	
	private boolean isEnabled(IStream stream) {
		IClientSettings settings = client.getClientSettings();
		return settings != null
				&& LoggingConfiguration.getProvider(settings).isLoggingEnabled()
				&& (stream.getName().equals(WarlockClient.MAIN_STREAM_NAME)
						|| stream.getClosedTarget().equals(WarlockClient.MAIN_STREAM_NAME));
	}
	@Override
	public void streamReceivedText(IStream stream, WarlockString text) {
		if(isEnabled(stream))
			appendBuffer(text.toString());
	}

	@Override
	public void streamPrompted(IStream stream, String prompt) {
		if(isEnabled(stream))
			appendBuffer(prompt + "\n");
	}

	@Override
	public void streamReceivedCommand(IStream stream, ICommand command) {
		if(isEnabled(stream))
			appendBuffer(command.getText() + "\n");
	}
	
	protected synchronized void appendBuffer(String str) {
		if (nextlineStamp) {
			nextlineStamp = false;
			str = timeFormat.format(Calendar.getInstance().getTime()) + str;
		}
		str = str.replaceAll("\n(.)", "\n" + timeFormat.format(Calendar.getInstance().getTime()) + "$1");
		if (str.endsWith("\n")) {
			nextlineStamp = true;
		}
		buffer.append(str);
		if (buffer.length() >= maxBufferSize) {
			dumpBuffer();
		}
	}

	protected File getLogFile ()
	{
		String characterName = client.getCharacterName();
		
		return new File(LoggingConfiguration.getProvider(client.getClientSettings()).getLogDirectory(),
			characterName + "-" + dateFormat.format(Calendar.getInstance().getTime()) + ".txt");
	}
	
	public synchronized void flush() {
		dumpBuffer();
	}
	
	protected void dumpBuffer ()
	{
		try {
			FileOutputStream stream = new FileOutputStream(getLogFile(), true);
			stream.write(buffer.toString().getBytes());
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		buffer.setLength(0);
	}

	@Override
	public void streamCreated(IStream stream) {
		// Don't care
	}
	
	@Override
	public void streamCleared(IStream stream) {
		// Don't care
	}

	@Override
	public void streamFlush(IStream stream) {
		// Don't care .. or maybe we should flush the buffer here?
	}

	@Override
	public void streamTitleChanged(IStream stream, String title) {
		// Don't care
	}

	@Override
	public void componentUpdated(IStream stream, String id, WarlockString value) {
		// and ... probably don't care
	}
}
