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
package cc.warlock.rcp.ui;

import java.util.ArrayList;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.ICommandHistory;
import cc.warlock.core.client.IMacro;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.IWarlockClientViewerListener;
import cc.warlock.core.client.IWarlockEntry;
import cc.warlock.core.client.IWarlockFont;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.internal.Command;
import cc.warlock.core.client.internal.CommandHistory;
import cc.warlock.core.client.internal.DefaultMacros;
import cc.warlock.core.client.settings.MacroConfigurationProvider;
import cc.warlock.core.client.settings.WindowConfigurationProvider;
import cc.warlock.core.settings.IWarlockSetting;
import cc.warlock.core.settings.IWarlockSettingListener;
import cc.warlock.rcp.configuration.GameViewConfiguration;
import cc.warlock.rcp.ui.client.SWTWarlockClientListener;
import cc.warlock.rcp.ui.client.SWTWarlockClientViewerListener;
import cc.warlock.rcp.ui.client.SWTWarlockSettingListener;
import cc.warlock.rcp.ui.macros.MacroRegistry;
import cc.warlock.rcp.util.ColorUtil;
import cc.warlock.rcp.util.FontUtil;
import cc.warlock.rcp.util.RCPUtil;
import cc.warlock.rcp.views.GameView;

abstract public class WarlockEntry implements IWarlockEntry {
	
	protected StyledText widget;
	private IWarlockClientViewer viewer;
	private boolean searchMode = false;
	private StringBuffer searchText = new StringBuffer();
	private String searchCommand = "";
	private CommandHistory commandHistory = new CommandHistory();
	private int minCommandSize = 0;
	private VerifyKeyListener verifyKeyListener = new VerifyKeyListener() {
		public void verifyKey(VerifyEvent e) {
			if(!e.doit)
				return;
			if(processKey(e.keyCode, e.stateMask, e.character))
				e.doit = false;
		}
	};
	private KeyListener keyListener = new KeyListener() {
		public void keyPressed(KeyEvent e) {
			if(!e.doit || viewer.getClient() != GameView.getGameViewInFocus().getClient())
				return;
			if(processKey(e.keyCode, e.stateMask, e.character))
				e.doit = false;
		}
		
		public void keyReleased(KeyEvent e) {
			// Do nothing
		}
	};
	private IWarlockClientViewerListener viewerListener = new SWTWarlockClientViewerListener(new IWarlockClientViewerListener() {
		@Override
		public void clientChanged(IWarlockClient client) {
			setClient(client);
		}
	});
	private static final ArrayList<Character> entryCharacters = new ArrayList<Character>();
	static {
		char[] entryChars = new char[] {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'.', '/', '{', '}', '<', '>', ',', '?', '\'', '"', ':', ';', '[', ']', '|', '\\', '-', '_', '+', '=',
			'~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', ' '
		};
		
		for (char c : entryChars) {
			entryCharacters.add(c);
		}
	}
	
	public WarlockEntry(Composite parent, IWarlockClientViewer viewer) {
		this.viewer = viewer;
		
		widget = new StyledText(parent, SWT.SINGLE); 
		widget.setEditable(true);
		widget.setMargins(2, 2, 2, 2);
		
		widget.addVerifyKeyListener(verifyKeyListener);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().addKeyListener(keyListener);
		
		Color background = ColorUtil.warlockColorToColor(GameViewConfiguration.defaultDefaultBgColor);
		Color foreground = ColorUtil.warlockColorToColor(GameViewConfiguration.defaultDefaultFgColor);
		
		widget.setBackground(background);
		widget.setForeground(foreground);
		
		WarlockClientRegistry.addWarlockClientListener(new SWTWarlockClientListener(new IWarlockClientListener() {
			@Override
			public void clientCreated(IWarlockClient client) {}
			@Override
			public void clientConnected(IWarlockClient client) {}
			@Override
			public void clientDisconnected(IWarlockClient client) {}
			@Override
			public void clientSettingsLoaded(IWarlockClient client) {
				if (client == WarlockEntry.this.viewer.getClient()) {
					loadSettings();
					WindowConfigurationProvider.getProvider(client.getClientSettings()).addListener(new SWTWarlockSettingListener(new IWarlockSettingListener() {
						@Override
						public void settingChanged(IWarlockSetting setting) {
							loadSettings();
						}
					}));
				}
			}
		}));
		
		setClient(viewer.getClient());
		viewer.addClientViewerListener(viewerListener);
		
		RCPUtil.addTextContextMenu(widget, viewer, "entry");
	}
	
	protected void loadSettings() {
		IClientSettings settings = viewer.getClient().getClientSettings();
		WindowConfigurationProvider provider = WindowConfigurationProvider.getProvider(settings);
		WarlockColor bg = provider.getWindowBackground("entry");
		WarlockColor fg = provider.getWindowForeground("entry");
		IWarlockFont font = provider.getWindowFont("entry");
		if (font.isDefaultFont()) {
			String defaultFontFace = GameViewConfiguration.getProvider(settings).getDefaultFontFace();
			int defaultFontSize = GameViewConfiguration.getProvider(settings).getDefaultFontSize();
			widget.setFont(new Font(Display.getDefault(), defaultFontFace, defaultFontSize, SWT.NORMAL));
		} else {
			widget.setFont(FontUtil.warlockFontToFont(font));
		}
		
		widget.setBackground(ColorUtil.warlockColorToColor(bg));
		widget.setForeground(ColorUtil.warlockColorToColor(fg));
		minCommandSize = settings.getMinCommandSize();
		settings.getNode().addPreferenceChangeListener(new IPreferenceChangeListener() {
			@Override
			public void preferenceChange(PreferenceChangeEvent event) {
				if (event.getKey().equals("min-command-size"))
					minCommandSize = Integer.parseInt((String)event.getNewValue());
			}
		});
	}
	
	public String getText() {
		if(searchMode)
			return searchCommand;
		else
			return widget.getText();
	}
	
	public void setText(String text) {
		text = text.trim();
		widget.setText(text);
		widget.setCaretOffset(text.length());
	}
	
	public void addKeyListener(KeyListener listener) {
		widget.addKeyListener(listener);
	}

	public StyledText getWidget() {
		return widget;
	}
	
	// returns whether we processed the key or not.
	protected boolean processKey(int keyCode, int stateMask, char character) {
		//System.out.println("got char \"" + e.character + "\"");
		IWarlockClient client = viewer.getClient();
		String keyString = MacroRegistry.instance().getKeyString(keyCode, stateMask);
		IMacro macro;
		if(client == null || client.getClientSettings() == null)
			macro = DefaultMacros.instance().getMacro(keyString);
		else
			macro = MacroConfigurationProvider.getProvider(client.getClientSettings()).getMacro(keyString);

		// System macros
		if(macro == null)
			macro = MacroRegistry.instance().getMacro(keyCode, stateMask);
		
		if(macro != null) {
			leaveSearchMode();
			macro.execute(viewer);

			return true;
		}

		if (!widget.isFocusControl() || searchMode) {
			if(entryCharacters.contains(character))
			{
				append(character);
				if(!widget.isFocusControl())
					widget.setFocus();
				return true;
			} else if(character == '\b') {
				if(searchMode) {
					searchText.setLength(searchText.length() - 1);
					search();
				} else {
					widget.invokeAction(ST.DELETE_PREVIOUS);
				}
				if(!widget.isFocusControl())
					widget.setFocus();
				return true;
			}
		}
		return false;
	}
	
	private void leaveSearchMode() {
		if(searchMode) {
			 searchMode = false;
			 setText(searchCommand);
			 searchCommand = "";
			 searchText.setLength(0);
		 }
	}
	
	public void append(char ch) {
		if(searchMode) {
			searchText.append(ch);
			search();
		} else {
			int offset = widget.getCaretOffset();
			widget.insert(String.valueOf(ch));
			widget.setCaretOffset(offset + 1);
		}
	}
	
	private void search() {
		ICommand foundCommand = commandHistory.search(searchText.toString());
		if(foundCommand != null) {
			searchCommand = foundCommand.getCommand();
		}
		setSearchText();
	}
	
	public void prevCommand() {
		ICommand prevCommand = commandHistory.prev();
		
		if(prevCommand != null)
			setText(prevCommand.getCommand());
		else
			setText("");
	}
	
	public void nextCommand() {
		ICommand nextCommand = commandHistory.next();
		if(nextCommand != null) {
			setText(nextCommand.getCommand());
		} else {
			setText("");
		}
	}
	
	public void searchHistory() {
		if(searchMode) {
			ICommand foundCommand = commandHistory.searchBefore(searchText.toString());
			if(foundCommand != null) {
				searchCommand = foundCommand.getCommand();
			}
			setSearchText();
		} else {
			searchMode = true;
			setSearchText();
		}
	}
	
	private void setSearchText() {
		widget.setText("(reverse history search)'" + searchText.toString() + "': " + searchCommand);
	}
	
	public void submit() {
		String text;
		if(searchMode) {
			text = searchCommand;
			leaveSearchMode();
		} else {
			text = widget.getText();
		}
		ICommand command = new Command(text);
		viewer.send(command);
		if (command.getCommand().length() > minCommandSize)
			commandHistory.addCommand(command);
		commandHistory.resetPosition();
		setText("");
	}
	
	public void repeatLastCommand() {
		if (commandHistory.size() >= 1) {
			ICommand command = commandHistory.getLastCommand();
			viewer.send(command);
		}
	}
	
	public void repeatSecondToLastCommand() {
		if (commandHistory.size() >= 2) {
			ICommand command = commandHistory.getCommandAt(1);
			viewer.send(command);
		}
	}
	
	public void setCurrentCommand (String command) {
		if(command == null) {
			command = "";
		}
		setText(command);
		widget.setSelection(command.length());
	}
	
	public void paste() {
		widget.paste();
	}
	
	abstract public void setClient (IWarlockClient client);
	
	public VerifyKeyListener getVerifyKeyListener() {
		return verifyKeyListener;
	}
	
	public ICommandHistory getCommandHistory() {
		return commandHistory;
	}
}
