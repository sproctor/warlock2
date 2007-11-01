package cc.warlock.rcp.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.PropertyListener;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.rcp.configuration.GameViewConfiguration;
import cc.warlock.rcp.ui.IStyleProvider;
import cc.warlock.rcp.ui.WarlockText;
import cc.warlock.rcp.ui.client.SWTPropertyListener;
import cc.warlock.rcp.ui.client.SWTStreamListener;
import cc.warlock.rcp.ui.style.DefaultStyleProvider;
import cc.warlock.rcp.ui.style.StyleProviders;
import cc.warlock.rcp.util.ColorUtil;

public class StreamView extends ViewPart implements IStreamListener, IGameViewFocusListener {
	
	public static final String STREAM_VIEW_PREFIX = "cc.warlock.rcp.views.stream.";
	
	public static final String RIGHT_STREAM_PREFIX = "rightStream.";
	public static final String TOP_STREAM_PREFIX = "topStream.";
	
	protected static ArrayList<StreamView> openViews = new ArrayList<StreamView>();
	
	protected IStream mainStream;
	protected ArrayList<IStream> streams;
	protected IWarlockClient client;
	protected Composite mainComposite;
	protected PageBook book;
	protected Hashtable<IWarlockClient, WarlockText> clientStreams = new Hashtable<IWarlockClient, WarlockText>();
	
	// This name is the 'suffix' part of the stream... so we will install listeners for each client
	protected String mainStreamName;
	protected SWTStreamListener streamListenerWrapper;
	protected SWTPropertyListener<String> propertyListenerWrapper;
	protected boolean appendNewlines = false;
	protected boolean isPrompting = false;
	protected boolean multiClient = false;
	protected boolean buffering = false;
	protected WarlockString echoBuffer;
	protected ArrayList<IWarlockStyle> styles = new ArrayList<IWarlockStyle>();
	
	protected WarlockString bufferedText;
	
	public StreamView() {
		openViews.add(this);
		streamListenerWrapper = new SWTStreamListener(this);
		streams = new ArrayList<IStream>();
		
		if (!(this instanceof GameView))
		{
			GameView.addGameViewFocusListener(this);
			this.multiClient = true;
		}
	}
	
	protected void setMultiClient (boolean multiClient)
	{
		this.multiClient = multiClient;
	}

	public static StreamView getViewForStream (String prefix, String streamName) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		for (StreamView view : openViews)
		{
			if (view.getStreamName().equals(streamName))
			{
				page.activate(view);
				return view;
			}
		}
		
		// none of the already created views match, create a new one
		try {
			StreamView nextInstance = (StreamView) page.showView(STREAM_VIEW_PREFIX + prefix + streamName);
			nextInstance.setStreamName(streamName);
			nextInstance.setMultiClient(true);
			
			return nextInstance;
		} catch (PartInitException e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		mainComposite = new Composite (parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		mainComposite.setLayout(layout);
		
		book = new PageBook(mainComposite, SWT.NONE);
		book.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
	}
	
	protected WarlockText getTextForClient (IWarlockClient client)
	{
		if (!clientStreams.containsKey(client))
		{
			WarlockText text = new WarlockText(book, SWT.V_SCROLL);
			GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
			text.setLayoutData(data);
			text.setEditable(false);
			text.setWordWrap(true);
			text.getTextWidget().setIndent(1);
			
			Color background = ColorUtil.warlockColorToColor(GameViewConfiguration.instance().getDefaultBackground());
			Color foreground = ColorUtil.warlockColorToColor(GameViewConfiguration.instance().getDefaultForeground());
			
			text.setBackground(background);
			text.setForeground(foreground);
			
			String fontFace = GameViewConfiguration.instance().getDefaultFontFace();
			int fontSize = GameViewConfiguration.instance().getDefaultFontSize();
			
			text.setFont(new Font(Display.getDefault(), fontFace, fontSize, SWT.NORMAL));
			text.setScrollDirection(SWT.DOWN);
			
			clientStreams.put(client, text);
			return text;
		}
		else return clientStreams.get(client);
	}

	public void gameViewFocused(GameView gameView) {
		if (multiClient)
		{
			setClient(gameView.getWarlockClient());
		}
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public IStream getMainStream() {
		return mainStream;
	}

	protected void appendStreamBuffer (IStream stream)
	{
		StringBuffer string = client.getStreamBuffer(stream);
		
		if (string != null && string.length() > 0)
		{
			streamReceivedText(stream, string.toString());
			for (IWarlockStyle style : client.getStreamBufferStyles(stream))
			{
				streamAddedStyle(stream, style);
			}
		}
	}
	
	public void setMainStream(IStream stream) {
		this.mainStream = stream;
		
		stream.addStreamListener(streamListenerWrapper);
		propertyListenerWrapper = new SWTPropertyListener<String>(new PropertyListener<String>() {
			@Override
			public void propertyChanged(IProperty<String> property, String oldValue) {
				if (property.getName().equals("streamTitle"))
				{
					setPartName(property.get());
				}
			}
		});
		stream.getTitle().addListener(propertyListenerWrapper);
		
		appendStreamBuffer(stream);
	}
	
	public void addStream (IStream stream) {
		stream.addStreamListener(streamListenerWrapper);
		streams.add(stream);
		
		appendStreamBuffer(stream);
	}
	
	public void streamCleared(IStream stream) {
		if (this.mainStream.equals(stream) || streams.contains(stream))
		{
			clientStreams.get(client).setText("");
		}
	}
	
	protected IStyleProvider getStyleProvider ()
	{
		return StyleProviders.getStyleProvider(client);
	}
	
	protected void appendText (WarlockString string)
	{
		
		if (buffering)
		{
			if(bufferedText == null)
				bufferedText = string;
			else
				bufferedText.append(string);
		}
		else
		{
			WarlockText text = getTextForClient(client);
			text.append(string);
		}
	}
	
	public void streamAddedStyle(IStream stream, IWarlockStyle style) {
		if (this.mainStream.equals(stream) || this.streams.contains(stream))
		{
			styles.add(style);
			
			/* TODO: old stuff, clean out
			WarlockText text = getTextForClient(client);
			int charCount = getCharCount(text);
			
			if (!style.isEndStyle())
			{
				StyleRangeWithData range = getStyleProvider().getStyleRange(style, charCount);
				if (style.getStyleTypes().contains(IWarlockStyle.StyleType.LINK))
				{
					range.data.put("link.url", style.getLinkAddress().toString());
				}
				unendedRanges.push(range);
			}
			else
			{
				if (unendedRanges.size() > 0)
				{
					StyleRangeWithData range = unendedRanges.pop();
					range.length = charCount - range.start;
					
					addStyleRange(text, range);
					if (unendedRanges.size() == 0 && innerRanges.size() > 0)
					{
						for (StyleRangeWithData innerRange : innerRanges) { addStyleRange(text, innerRange); }
						innerRanges.clear();
					}
				}
			}*/
		}
	}
	
	public void streamRemovedStyle(IStream stream, IWarlockStyle style) {
		if (this.mainStream.equals(stream) || this.streams.contains(stream))
		{
			styles.remove(style);
		}
	}
	
	public void streamReceivedText(IStream stream, String text) {
		if (this.mainStream.equals(stream) || this.streams.contains(stream))
		{
			WarlockString string = new WarlockString(client);
			
			if (isPrompting) {
				string.append("\n");
				isPrompting = false;
			}
			int styleStart = string.length();
			string.append(text);
			for(IWarlockStyle style : styles) {
				string.addStyle(styleStart, string.length(), style);
			}
			
			if (appendNewlines)
				string.append("\n");
			
			appendText(string);
		}
	}
	
	public void streamEchoed(IStream stream, String text) {
		if (this.mainStream.equals(stream) || this.streams.contains(stream))
		{
			WarlockString string = new WarlockString(client, text);
			string.addStyle(0, text.length(), WarlockStyle.createCustomStyle("command"));
			
			if (isPrompting)
			{
				WarlockText textWidget = getTextForClient(client);
				isPrompting = false;
				textWidget.append(string);
			}
			else
			{
				if(echoBuffer == null)
					echoBuffer = new WarlockString(client);
				echoBuffer.append(string);
			}
		}
	}
	
	public void streamPrompted(IStream stream, String prompt) {
		if (!isPrompting && (this.mainStream.equals(stream) || this.streams.contains(stream)))
		{
			WarlockText textWidget = getTextForClient(client);
			isPrompting = true;
			WarlockString text = new WarlockString(client);
			
			if (buffering && bufferedText != null)
			{
				text.append(bufferedText);
				bufferedText = null;
			}
			
			styles.clear();
			text.append(prompt);
			
			if (echoBuffer != null)
			{
				text.append(echoBuffer);

				echoBuffer = null;
				isPrompting = false;
			}
			
			textWidget.append(text);
		}
	}
	
	public void streamDonePrompting (IStream stream) {
		isPrompting = false;
	}

	public static Collection<StreamView> getOpenViews ()
	{
		return openViews;
	}
	
	public void setClient (IWarlockClient client)
	{
		this.client = client;
		book.showPage(getTextForClient(client).getTextWidget());
		
		if (mainStream == null)
			setMainStream(client.getStream(mainStreamName));

		if (StyleProviders.getStyleProvider(client) == null)
			StyleProviders.setStyleProvider(client, DefaultStyleProvider.instance());
	}
	
	@Override
	public void dispose() {
		if (mainStream != null) {
			mainStream.removeStreamListener(streamListenerWrapper);
			mainStream.getTitle().removeListener(propertyListenerWrapper);
		}
		
		for (IStream stream : streams)
		{
			stream.removeStreamListener(streamListenerWrapper);
		}
		
		clientStreams.clear();
		GameView.removeGameViewFocusListener(this);
		
		if (openViews.contains(this)) {
			openViews.remove(this);
		}
		
		super.dispose();
	}
	
	public String getStreamName() {
		return mainStreamName;
	}

	public void setStreamName(String streamName) {
		this.mainStreamName = streamName;
	}

	public void setAppendNewlines(boolean appendNewlines) {
		this.appendNewlines = appendNewlines;
	}
	
	public void setViewTitle (String title)
	{
		setPartName(title);
	}

	public boolean isBuffered() {
		return buffering;
	}

	public void setBuffered(boolean buffer) {
		this.buffering = buffer;
	}
}
