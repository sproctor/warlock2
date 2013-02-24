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
 * Created on Sep 17, 2004
 */
package cc.warlock.rcp.views;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.PageBook;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IProfile;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.IWarlockClientViewerListener;
import cc.warlock.core.client.PropertyListener;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.configuration.ScriptConfiguration;
import cc.warlock.rcp.ui.StreamText;
import cc.warlock.rcp.ui.WarlockEntry;
import cc.warlock.rcp.ui.WarlockPopupAction;
import cc.warlock.rcp.ui.client.SWTWarlockClientViewer;
import cc.warlock.rcp.util.SoundPlayer;

/**
 * @author marshall
 */
public abstract class GameView extends WarlockView implements IWarlockClientViewer {
	public static final String VIEW_ID = "cc.warlock.rcp.ui.views.GameView";
	
	private static ArrayList<GameView> openViews = new ArrayList<GameView>();
	private static ArrayList<IGameViewFocusListener> focusListeners = new ArrayList<IGameViewFocusListener>();
	private static GameView gameInFocus;
	private static final Random generator = new Random(new Date().getTime());
	
	protected PageBook popupPageBook;
	protected Label emptyPopup;
	protected StreamText streamText;
	protected WarlockEntry entry;
	protected SWTWarlockClientViewer wrapper;
	private IWarlockClient client;
	protected Composite mainComposite;
	private ArrayList<IWarlockClientViewerListener> listeners = new ArrayList<IWarlockClientViewerListener>();
	
	private IProfile profile;
	
	private HashMap<String, StreamText> customStreams = new HashMap<String, StreamText>();
	
	public GameView () {
		super();
		
		wrapper = new SWTWarlockClientViewer(this);
	}
	
	public static void addGameViewFocusListener (IGameViewFocusListener listener) {
		focusListeners.add(listener);
	}
	
	public static void removeGameViewFocusListener (IGameViewFocusListener listener) {
		focusListeners.remove(listener);
	}
	
	public static List<GameView> getOpenGameViews ()
	{
		return openViews;
	}
	
	public static GameView getGameViewInFocus () {
		return gameInFocus;
	}
	
	public static void initializeGameView (GameView gameView) {
		gameInFocus = gameView;
		
		if (ConnectionView.closeAfterConnect) {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IViewPart part = page.findView(ConnectionView.VIEW_ID);
			if (part != null)
				page.hideView(part);
		}
	}
	
	public static GameView createNext (String viewId) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			IViewPart part = page.showView(viewId, Integer.toString(generator.nextInt()), IWorkbenchPage.VIEW_ACTIVATE);
			// if there's an error in creating the view, we want to know about it.. don't cast unless we know it's not an errorviewpart
			if (part instanceof GameView) {
				GameView nextInstance = (GameView) part;
				initializeGameView(nextInstance);
				return nextInstance;
			}
			
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static GameView getGameViewForClient(IWarlockClient client) {
		for (GameView view: openViews) {
			if (view.client == client) {
				return view;
			}
		}
		// TODO: Make a GameView and return it. (Null is likely to cause problems in the long run)
		return null;
	}
	
	public void createPartControl(Composite parent) {
		// Create main composite
		mainComposite = new Composite (parent, SWT.NONE);
		GridLayout mainLayout = new GridLayout(1, false);
		mainLayout.marginHeight = 0;
		mainLayout.marginWidth = 0;
		mainLayout.horizontalSpacing = 0;
		mainLayout.verticalSpacing = 0;
		mainComposite.setLayout(mainLayout);
		
		// create a pagebook for the popups
		popupPageBook = new PageBook(mainComposite, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.exclude = true;
		popupPageBook.setLayoutData(data);
		
		emptyPopup = new Label(popupPageBook, SWT.NONE);
		
		popupPageBook.showPage(emptyPopup);
		popupPageBook.setVisible(false);
		
		streamText = new StreamText(mainComposite, IWarlockClient.MAIN_STREAM_NAME);
		streamText.getTextWidget().setLayout(new GridLayout(1, false));
		streamText.setIgnoreEmptyLines(false);
		
		openViews.add(this);
		this.setFocus();

		mainComposite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (client != null) {
					// All done with client, dispose of it so it can do whatever it needs to to be done.
					client.dispose();
				}
				openViews.remove(GameView.this);
				if (gameInFocus == GameView.this) {
					gameInFocus = null;
					if(!openViews.isEmpty()) {
						GameView firstView = openViews.get(0);
						firstView.setFocus();
					}
				}
				streamText.dispose();
				streamText = null;
			}
		});
	}
	
	@Override
	public void setFocus() {
		super.setFocus();
		gameInFocus = this;
		for (IGameViewFocusListener listener : focusListeners) {
			listener.gameViewFocused(this);
		}
	}
	
	public void playSound(InputStream soundStream) {
		SoundPlayer.play(soundStream);
	}
		
	public void copy() {
		streamText.copy();
	}
	
	public IWarlockClient getClient() {
		return client;
	}
	
	public WarlockEntry getEntry() {
		return entry;
	}
	
	public WarlockPopupAction createPopup ()
	{
		WarlockPopupAction popup = new WarlockPopupAction(popupPageBook, SWT.NONE);
//		popup.moveAbove(book);
//		popup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//		popup.setVisible(false);
		
		return popup;
	}
	
	public void showPopup (WarlockPopupAction popup)
	{
		boolean atBottom = streamText.isAtBottom();
		popupPageBook.showPage(popup);
		popupPageBook.setVisible(true);
		((GridData)popupPageBook.getLayoutData()).exclude = false;
		
		mainComposite.layout();
		
		if(atBottom)
			streamText.scrollToEnd();
	}
	
	public void hidePopup (WarlockPopupAction popup)
	{
		boolean atBottom = streamText.isAtBottom();
		popupPageBook.showPage(emptyPopup);		
		popupPageBook.setVisible(false);
		((GridData)popupPageBook.getLayoutData()).exclude = true;
		
		mainComposite.layout();
		
		if(atBottom)
			streamText.scrollToEnd();
	}
	
	public void pageDown() {
		streamText.pageDown();
	}
	
	public void pageUp() {
		streamText.pageUp();
	}
	
	public synchronized void setClient(IWarlockClient client) {
		if (this.client != null) {
			// All done with client, dispose of it so it can do whatever it needs to to be done.
			this.client.dispose();
		}
		this.client = client;
		client.setViewer(wrapper);
		
		streamText.getTitle().addListener(new PropertyListener<String>() {
			public void propertyChanged(String value) {
				setViewTitle(value);
			}
		});
		streamText.setClient(client);
		
		for(IWarlockClientViewerListener listener : listeners) {
			listener.clientChanged(client);
		}
	}
	
	protected abstract void setViewTitle(String title);
	
	public boolean isStreamOpen(String streamName) {
		if(streamName == IWarlockClient.MAIN_STREAM_NAME)
			return true;
		
		for(StreamView streamView : StreamView.getOpenViews()) {
			if(streamView.getStreamName().equals(streamName))
				return true;
		}
		return false;
	}
	
	public IProfile getProfile() {
		return profile;
	}
	
	public void setProfile(IProfile profile) {
		this.profile = profile;
	}
	
	public void openCustomStream(String name) {
		StreamView view = StreamView.getViewForStream(StreamView.RIGHT_STREAM_PREFIX, name);
		view.setClient(client);
		customStreams.put(name, view.getStreamTextForClient(client));
	}
	
	public void printToCustomStream(String name, WarlockString text) {
		StreamText stream = customStreams.get(name);
		if(stream == null)
			return;
		stream.append(text);
	}
	
	public void clearCustomStream(String name) {
		StreamText stream = customStreams.get(name);
		if(stream == null)
			return;
		stream.clearText();
	}
	
	public void send(ICommand command) {
		String scriptPrefix = ScriptConfiguration.instance().getScriptPrefix();
		
		if (command.getCommand().startsWith(scriptPrefix)){
			ScriptEngineRegistry.startScript(this, command.getCommand().substring(scriptPrefix.length()));
		} else {
			if(client != null)
				client.send(command);
			else
				streamText.streamReceivedText(null, new WarlockString("No connection, command not sent", WarlockStyle.echoStyle));
		}
	}
	
	public void setEntry(WarlockEntry entry) {
		this.entry = entry;
	}
	
	@Override
	public synchronized void addClientViewerListener(IWarlockClientViewerListener listener) {
		listeners.add(listener);
	}

	@Override
	public synchronized void removeClientViewerListener(IWarlockClientViewerListener listener) {
		listeners.remove(listener);
	}
	
	public String getViewId() {
		return getViewSite().getId() + ":" + getViewSite().getSecondaryId();
	}
}
