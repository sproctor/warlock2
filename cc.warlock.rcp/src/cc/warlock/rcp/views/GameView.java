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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.PageBook;

import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IProfile;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.IWarlockClientViewerListener;
import cc.warlock.core.client.PropertyListener;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.WarlockStyle;
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
	
	private StreamText streamText;
	
	private PageBook popupPageBook;
	private Label emptyPopup;
	private WarlockEntry entry;
	private final SWTWarlockClientViewer wrapper;
	private IWarlockClient client;
	private ArrayList<IWarlockClientViewerListener> listeners = new ArrayList<IWarlockClientViewerListener>();
	
	private IProfile profile;
	
	private HashMap<String, StreamText> customStreams = new HashMap<String, StreamText>();
	
	private HashMap<String, Menu> menuMap = new HashMap<String, Menu>();
	
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
	
	@Override
	public void createPartControl(Composite parent) {
		streamText = new StreamText(parent, this, IWarlockClient.MAIN_STREAM_NAME);
		streamText.getTextWidget().setLayout(new FormLayout());
		streamText.setIgnoreEmptyLines(false);
		
		// create a pagebook for the popups
		popupPageBook = new PageBook(streamText.getTextWidget(), SWT.NONE);
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		popupPageBook.setLayoutData(data);
		
		emptyPopup = new Label(popupPageBook, SWT.NONE);
		
		popupPageBook.showPage(emptyPopup);
		popupPageBook.setVisible(false);
		
		openViews.add(this);
		this.setFocus();
		
		if(client != null) {
			loadClientSettings(client.getClientSettings());
		}

		parent.addDisposeListener(new DisposeListener() {
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
	
	abstract protected void loadClientSettings(IClientSettings settings);
	
	@Override
	public void setFocus() {
		super.setFocus();
		gameInFocus = this;
		for (IGameViewFocusListener listener : focusListeners) {
			listener.gameViewFocused(this);
		}
	}
	
	@Override
	public void playSound(InputStream soundStream) {
		SoundPlayer.play(soundStream);
	}
	
	@Override
	public void copy() {
		streamText.copy();
	}
	
	@Override
	public IWarlockClient getClient() {
		return client;
	}
	
	@Override
	public WarlockEntry getEntry() {
		return entry;
	}
	
	public WarlockPopupAction createPopup() {
		WarlockPopupAction popup = new WarlockPopupAction(popupPageBook, SWT.NONE);
		
		return popup;
	}
	
	public void showPopup (WarlockPopupAction popup)
	{
		boolean atBottom = streamText.isAtBottom();
		
		popupPageBook.showPage(popup);
		popupPageBook.setVisible(true);
		
		popupPageBook.getParent().layout();
		
		if(atBottom)
			streamText.scrollToEnd();
	}
	
	public void hidePopup (WarlockPopupAction popup)
	{
		boolean atBottom = streamText.isAtBottom();
		
		popupPageBook.showPage(emptyPopup);		
		popupPageBook.setVisible(false);
		
		popupPageBook.getParent().layout();
		
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
	
	@Override
	public void createMenu(String id) {
		Menu popupMenu = new Menu(this.streamText.getTextWidget());
		menuMap.put(id, popupMenu);
		this.streamText.getTextWidget().setMenu(popupMenu);
	}
	
	// TODO make this non-SF specific
	@Override
	public void addMenuItem(String id, String category, String text, final Runnable runner) {
		Menu menu = menuMap.get(id);
		if(menu == null) {
			System.err.println("No menu found");
			return;
		}
		MenuItem item;
		if(category != null && category.contains("_")) {
			Menu subMenu = menuMap.get(id + " " + category);
			if(subMenu == null) {
				Pattern catPat = Pattern.compile("[a-zA-Z]+$");
				Matcher m = catPat.matcher(category);
				if(!m.find()) {
					System.err.println("Bad category");
					return;
				}
				System.out.println("id: " + id + ", category: " + category + ", text: " + text);
				String title = m.group();
				Pattern parentPat = Pattern.compile("^(\\d+[_-].+)[_-]\\w+$");
				m = parentPat.matcher(category);
				Menu parentMenu;
				if(m.find()) {
					parentMenu = menuMap.get(id + " " + m.group(1));
					if(parentMenu == null) {
						System.err.println("Bad parent category: " + id + " " + m.group(1));
						return;
					}
				} else {
					parentMenu = menu;
				}
				subMenu = new Menu(parentMenu);
				menuMap.put(id + " " + category, subMenu);
				System.out.println("added menu: " + id + " " + category);
				
				MenuItem subItem = new MenuItem(parentMenu, SWT.CASCADE);
				subItem.setMenu(subMenu);
				subItem.setText(title);
			}
			item = new MenuItem(subMenu, SWT.NONE);
		} else {
			item = new MenuItem(menu, SWT.NONE);
		}
		item.setText(text);
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runner.run();
			}
		});
	}
	
	@Override
	public void displayMenu(String id) {
		Menu menu = menuMap.get(id);
		if(menu == null) {
			System.err.println("No menu found");
			return;
		}
		menu.setVisible(true);
	}
	
	protected StreamText getStreamText() {
		return streamText;
	}
}
