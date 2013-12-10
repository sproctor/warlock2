package cc.warlock.rcp.ui;

import org.eclipse.swt.widgets.Composite;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.internal.Property;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.rcp.configuration.GameViewConfiguration;
import cc.warlock.rcp.ui.client.SWTStreamListener;
import cc.warlock.rcp.views.GameView;

public class StreamText extends WarlockText implements IStreamListener {

	protected boolean isPrompting = false;
	protected String prompt = null;
	protected Property<String> title = new Property<String>();
	private IStreamListener streamListener = new SWTStreamListener(this);
	
	private WarlockString textBuffer;
	
	private static final WarlockString basicPrompt = new WarlockString(">");
	
	public StreamText(Composite parent, IWarlockClientViewer viewer, String streamName) {
		super(parent, viewer, streamName);
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
		if(!GameViewConfiguration.getProvider(getClient().getClientSettings()).getSuppressPrompt()) {
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
			showPrompt(prompt);
		
		// If we're suppressing prompts, prepend one to the command.
		if(GameViewConfiguration.getProvider(getClient().getClientSettings()).getSuppressPrompt()) {
			append(basicPrompt);
		}
		
		append(string);
		isPrompting = false;
	}

	public void streamReceivedText(IStream stream, WarlockString text) {
		if (isPrompting && text.length() > 0) {
			showPrompt("\n");
			isPrompting = false;
		}
		
		bufferText(text);
	}
	
	public void streamTitleChanged(IStream stream, String title) {
		this.title.set(stream.getFullTitle());
	}

	public void setClient(IWarlockClient client) {
		if (getClient() == client)
			return;
		
		if(getClient() != null) {
			getClient().removeStreamListener(this.getName(), streamListener);
		}
		
		super.setClient(client);
		
		if(client != null) {
			GameView game = GameView.getGameViewForClient(client);
			if (game == null || game.getEntry() == null) {
				System.out.println("Couldn't find a gameview for this client! This view won't be setup to send keys over.");
			} else {
				this.getTextWidget().addVerifyKeyListener(game.getEntry().getVerifyKeyListener());
			}
			
			client.addStreamListener(this.getName(), streamListener);
			
			title.set(client.getStreamTitle(this.getName()));
			WarlockString history = client.getStreamHistory(this.getName());
			if(history != null)
				bufferText(history);
		}
		
		this.flushBuffer();
	}
	
	public void dispose() {
		setClient(null);
	}
}
