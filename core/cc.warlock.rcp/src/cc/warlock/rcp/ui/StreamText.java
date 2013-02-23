package cc.warlock.rcp.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;
import cc.warlock.core.client.IWarlockFont;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.internal.Property;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.client.settings.WindowConfigurationProvider;
import cc.warlock.core.settings.IWarlockSetting;
import cc.warlock.core.settings.IWarlockSettingListener;
import cc.warlock.rcp.configuration.GameViewConfiguration;
import cc.warlock.rcp.ui.client.SWTStreamListener;
import cc.warlock.rcp.ui.client.SWTWarlockClientListener;
import cc.warlock.rcp.ui.client.SWTWarlockSettingListener;
import cc.warlock.rcp.util.ColorUtil;
import cc.warlock.rcp.util.FontUtil;
import cc.warlock.rcp.views.GameView;

public class StreamText extends WarlockText implements IStreamListener {

	protected String streamName;
	protected boolean isPrompting = false;
	protected String prompt = null;
	protected Property<String> title = new Property<String>();
	private IStreamListener streamListener = new SWTStreamListener(this);
	private WindowSettingsListener settingListener;
	private IWarlockClientListener clientListener = new SWTWarlockClientListener(new IWarlockClientListener() {
		@Override
		public void clientCreated(IWarlockClient client) {}
		@Override
		public void clientConnected(IWarlockClient client) {}
		@Override
		public void clientDisconnected(IWarlockClient client) {}
		@Override
		public void clientSettingsLoaded(IWarlockClient client) {
			loadSettings();
		}
	});
	private WarlockString textBuffer;
	
	public StreamText(Composite parent, String streamName) {
		super(parent);
		this.streamName = streamName;
	}
	
	public void setFocus() {
		// Nothing to do
	}
	
	protected void bufferText (WarlockString string)
	{
		if(textBuffer == null) {
			textBuffer = new WarlockString();
		}

		textBuffer.append(string);
	}
	
	protected void bufferText (String string)
	{
		if(textBuffer == null) {
			textBuffer = new WarlockString();
		}

		textBuffer.append(string);
	}
	
	public Property<String> getTitle() {
		return title;
	}
	
	public void streamCreated(IStream stream) {
		this.title.set(stream.getFullTitle());
	}
	
	public void componentUpdated(IStream stream, String id, WarlockString value) {
		flushBuffer();
		replaceMarker(id, value);
	}

	public void streamCleared(IStream stream) {
		flushBuffer();
		clearText();
	}

	public void streamFlush(IStream stream) {
		flushBuffer();
	}

	private void flushBuffer() {
		if(textBuffer != null) {
			append(textBuffer);
			textBuffer = null;
		}
	}
	
	private void showPrompt(String prompt) {
		if(!GameViewConfiguration.getProvider(client.getClientSettings()).getSuppressPrompt()) {
			append(new WarlockString(prompt));
		}
	}
	
	public void streamPrompted(IStream stream, String prompt) {
		flushBuffer();
		if(!isPrompting) {
			isPrompting = true;
			if(prompt != null)
				showPrompt(prompt);
		} else {
			// if the new prompt is the same as the old one, do nothing.
			// if the new prompt is null, just print the newline.
			if(prompt == null) {
				if(this.prompt != null)
					showPrompt("\n");
			} else if(this.prompt == null || !this.prompt.equals(prompt)) {
				showPrompt("\n" + prompt);
			}	
		}
		this.prompt = prompt;
	}

	public void streamReceivedCommand(IStream stream, ICommand command) {
		flushBuffer();
		
		WarlockString string = new WarlockString(command.getText(), WarlockStyle.commandStyle);
		
		if(!isPrompting && prompt != null)
			append(new WarlockString(prompt));
		
		append(string);
		isPrompting = false;
	}

	public void streamReceivedText(IStream stream, WarlockString text) {
		if (isPrompting && text.length() > 0) {
			bufferText("\n");
			isPrompting = false;
		}
		
		bufferText(text);
	}
	
	public void streamTitleChanged(IStream stream, String title) {
		this.title.set(stream.getFullTitle());
	}

	public void setClient(IWarlockClient client) {
		if (this.client == client)
			return;
		
		if(this.client != null) {
			this.client.removeStreamListener(streamName, streamListener);
			WarlockClientRegistry.removeWarlockClientListener(clientListener);
			if(settingListener != null)
				settingListener.remove();
		}
		
		this.client = client;
		
		if(client != null) {
			GameView game = GameView.getGameViewForClient(client);
			if (game == null || game.getEntry() == null) {
				System.out.println("Couldn't find a gameview for this client! This view won't be setup to send keys over.");
			} else {
				this.getTextWidget().addVerifyKeyListener(game.getEntry().getVerifyKeyListener());
			}
			
			client.addStreamListener(streamName, streamListener);
			WarlockClientRegistry.addWarlockClientListener(clientListener);
			// if client already has settings, we'll unintentially load them twice
			loadSettings();
			
			title.set(client.getStreamTitle(streamName));
			WarlockString history = client.getStreamHistory(streamName);
			if(history != null)
				bufferText(history);
		}
		
		this.flushBuffer();
	}
	
	private class WindowSettingsListener implements IWarlockSettingListener {
		WindowConfigurationProvider provider;
		IWarlockSettingListener listener = new SWTWarlockSettingListener(this);
		public WindowSettingsListener(WindowConfigurationProvider provider) {
			this.provider = provider;
			provider.addListener(listener);
		}
		public void settingChanged(IWarlockSetting setting) {
			loadSettings();
		}
		public void remove() {
			provider.removeListener(listener);
		}
	}
	
	private void loadSettings() {
		if(settingListener != null)
			settingListener.remove();
		
		IClientSettings settings = client.getClientSettings();
		
		if(settings == null)
			return;
		
		WindowConfigurationProvider provider = WindowConfigurationProvider.getProvider(settings);
		settingListener = new WindowSettingsListener(provider);
		
		// Set to defaults first, then try window settings later
		Color background = ColorUtil.warlockColorToColor(provider.getWindowBackground(streamName));
		Color foreground = ColorUtil.warlockColorToColor(provider.getWindowForeground(streamName));
		IWarlockFont font = provider.getWindowFont(streamName);
		
		this.setBackground(background);
		this.setForeground(foreground);

		String defaultFontFace = GameViewConfiguration.getProvider(settings).getDefaultFontFace();
		int defaultFontSize = GameViewConfiguration.getProvider(settings).getDefaultFontSize();

		if (font.isDefaultFont()) {
			this.setFont(new Font(Display.getDefault(), defaultFontFace, defaultFontSize, SWT.NORMAL));
		} else {
			this.setFont(FontUtil.warlockFontToFont(font));
		}
	}
	
	public void dispose() {
		setClient(null);
	}
}
