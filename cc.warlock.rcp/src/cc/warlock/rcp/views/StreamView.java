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
package cc.warlock.rcp.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.PageBook;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;
import cc.warlock.core.client.PropertyListener;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.rcp.configuration.GameViewConfiguration;
import cc.warlock.rcp.ui.StreamText;
import cc.warlock.rcp.ui.client.SWTWarlockClientListener;
import cc.warlock.rcp.util.ColorUtil;

public class StreamView extends WarlockView implements IGameViewFocusListener
{	
	public static final String STREAM_VIEW_PREFIX = "cc.warlock.rcp.views.stream.";
	public static final String RIGHT_STREAM_PREFIX = "rightStream";
	public static final String LEFT_STREAM_PREFIX = "leftStream";
	public static final String TOP_STREAM_PREFIX = "topStream";
	
	//private static ArrayList<StreamView> openViews = new ArrayList<StreamView>();
	
	private String streamName;
	
	private StreamText activeStream;
	private IWarlockClient activeClient;
	private PageBook book;
	
	private HashMap<IWarlockClient, StreamText> streams =
		new HashMap<IWarlockClient, StreamText>();
	
	private StyledText nullTextWidget;
	private IWarlockClientListener clientListener = new SWTWarlockClientListener(new IWarlockClientListener() {
		@Override
		public void clientConnected(IWarlockClient client) {
			addClient(client);
		}
		@Override
		public void clientDisconnected(IWarlockClient client) {}
		@Override
		public void clientSettingsLoaded(IWarlockClient client) {}
	});
	
	@Override
	public void createPartControl(Composite parent) {
		// Create main composite
		Composite mainComposite = new Composite (parent, SWT.NONE);
		FillLayout layout = new FillLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.spacing = 0;
		mainComposite.setLayout(layout);
		
		// Create page book
		book = new PageBook(mainComposite, SWT.NONE);
		
		nullTextWidget = new StyledText(book, SWT.V_SCROLL);
		nullTextWidget.setLayout(new FillLayout());
		nullTextWidget.setEditable(false);
		nullTextWidget.setWordWrap(true);
		nullTextWidget.setIndent(1);
		Color background = ColorUtil.warlockColorToColor(GameViewConfiguration.defaultDefaultBgColor);
		Color foreground = ColorUtil.warlockColorToColor(GameViewConfiguration.defaultDefaultFgColor);
		
		nullTextWidget.setBackground(background);
		nullTextWidget.setForeground(foreground);
		book.showPage(nullTextWidget);
		
		// Hacked to allow UserStream to preset the streamName
		if(streamName == null)
			streamName = getViewSite().getSecondaryId();
		updateViewTitle();
		
		for (IWarlockClient client : WarlockClientRegistry.getActiveClients()) {
			addClient(client);
		}
		WarlockClientRegistry.addWarlockClientListener(clientListener);
		
		GameView.addGameViewFocusListener(this);
		GameView inFocus = GameView.getGameViewInFocus();
		if (inFocus != null) {
			setClient(inFocus.getClient());
		}
	}
	
	protected void addClient(IWarlockClient client) {
		if(book == null) {
			System.err.println("Adding client before intializing StreamView");
			return;
		}
		StreamText streamText = createStreamText(book, client);
		streamText.getTextWidget().setLayout(new FillLayout());
		streams.put(client, streamText);
		streamText.setClient(client);
		
		// TODO: Make sure this listener gets destroyed on dispose.
		streamText.getTitle().addListener(new NameListener(client));
		
		if(activeClient == null || activeClient == client)
			setClient(client);
	}
	
	// Hack to allow UserStream to inject its custom StreamText
	protected StreamText createStreamText(Composite container, IWarlockClient client) {
		return new StreamText(container, client.getViewer(), streamName);
	}
	
	private class NameListener extends PropertyListener<String> {
		private IWarlockClient client;
		
		public NameListener(IWarlockClient client) {
			this.client = client;
		}
		
		public void propertyChanged(String value) {
			if(activeClient == client)
				updateViewTitle();
		}
	}

	public void gameViewFocused(GameView gameView) {
		setClient(gameView.getClient());
	}
	
	public synchronized void setClient (IWarlockClient client)
	{
		activeClient = client;
		activeStream = streams.get(client);
		
		updateViewTitle();
		
		if(activeStream != null)
			book.showPage(activeStream.getTextWidget());
		else
			book.showPage(nullTextWidget);
	}
	
	// Allow this to be overridden for UserStreams
	protected void updateViewTitle() {
		String title = activeClient == null ? "" : activeClient.getStreamTitle(streamName);
		if(title.length() > 0)
			setViewTitle(title);
		else
			setViewTitle("(" + streamName + ")");
	}
	
	@Override
	public void dispose() {
		GameView.removeGameViewFocusListener(this);
		
		WarlockClientRegistry.removeWarlockClientListener(clientListener);
		
		for(StreamText stream : streams.values()) {
			stream.dispose();
		}
		activeClient = null;
		super.dispose();
	}
	
	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
	
	public void setViewTitle (String title)
	{
		setPartName(title);
	}
	
	@Override
	public void pageUp() {
		activeStream.pageUp();
	}
	
	@Override
	public void pageDown() {
		activeStream.pageDown();
	}
	
	public StreamText getStreamTextForClient(IWarlockClient client) {
		return streams.get(client);
	}
}
