/**
 * 
 */
package cc.warlock.rcp.userstreams.ui.views;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.WarlockString.WarlockStringStyleRange;
import cc.warlock.rcp.ui.client.SWTWarlockClientListener;
import cc.warlock.rcp.userstreams.IStreamFilter;
import cc.warlock.rcp.userstreams.internal.StreamFilter;
import cc.warlock.rcp.views.StreamView;

/**
 * @author Will Robertson
 * UserStreams
 * ViewPart/Stream View Class that shows user configurable content filtered from the main window.
 */
public class UserStream extends StreamView implements IWarlockClientListener {
	public static final String VIEW_ID = "cc.warlock.rcp.userstreams.rightView.userStream";
	protected static ArrayList<UserStream> openStreams = new ArrayList<UserStream>();
	private IStreamFilter[] filters = null;
	private String name = "Stream";
	private ArrayList<String> styles;
	
	public void clientActivated(IWarlockClient client) {
		// TODO Auto-generated method stub
	}
	
	public void setFilters(IStreamFilter[] filters) {
		this.filters = filters;
	}
	
	@Override
	protected void appendText (WarlockString string)
	{
		// Process filters on the complete lines
		WarlockString ret = new WarlockString();
		bufferLoop: for (WarlockString buffer : string.split("\\r?\\n")) {
			if(styles != null)
				for(WarlockStringStyleRange style : buffer.getStyles()) {
					String name = style.style.getName();
					if(name != null && styles.contains(name)) {
						ret.append(buffer);
						ret.append("\n");
						continue bufferLoop;
					}
				}
			for (IStreamFilter filter : this.filters) {
				if (filter == null) continue;
				if (filter.match(buffer)) {
					// If a filter matches, we go ahead and display the chunk
					ret.append(buffer);
					ret.append("\n");
					continue bufferLoop;
				}
			}
		}
		if (ret.length() > 0) {
			super.appendText(ret);
		}
	}
	
	public void clientConnected(IWarlockClient client) {	
		setClient(client);
	}

	public void clientDisconnected(IWarlockClient client) {
		// TODO Auto-generated method stub
	}
	
	public void clientRemoved(IWarlockClient client) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		String streamName = getViewSite().getSecondaryId().substring(getViewSite().getSecondaryId().lastIndexOf('.')+1);
		setName(streamName);
		setStreamName(IWarlockClient.DEFAULT_STREAM_NAME);
		setMultiClient(true);
		scanClients();
		if (streamName.equals("Events")) {
			this.filters = getEventsFilters();
		} else if (streamName.equals("Conversations")) {
			this.filters = getConversationsFilters();
			this.styles = new ArrayList<String>();
			styles.add("speech");
		} else {
			System.out.println("Not a stream name we recognize! ("+streamName+")");
		}
	}
	
	protected IStreamFilter[] getEventsFilters ()
	{
		ArrayList<IStreamFilter> filters = new ArrayList<IStreamFilter>();
		filters.add(new StreamFilter("^You've gained a new rank in .+\\.", IStreamFilter.type.regex));
		filters.add(new StreamFilter("^Announcement: .+$", IStreamFilter.type.regex));
		filters.add(new StreamFilter("^System Announcement: .+$", IStreamFilter.type.regex));
		filters.add(new StreamFilter("^(Xibar|Katamba|Yavash) slowly rises above the horizon\\.", IStreamFilter.type.regex));
		filters.add(new StreamFilter("^(Xibar|Katamba|Yavash) sets, slowly dropping below the horizon\\.", IStreamFilter.type.regex));
		
		return filters.toArray(new IStreamFilter[filters.size()]);
	}
	
	protected IStreamFilter[] getConversationsFilters ()
	{
		ArrayList<IStreamFilter> filters = new ArrayList<IStreamFilter>();
		filters.add(new StreamFilter("\\bthoughts in your head\\b", IStreamFilter.type.regex));
		filters.add(new StreamFilter("^\\w+ (\\bnod|\\blean|\\bstretch|\\bsmile|\\byawn|\\bchuckle|\\bchortle|\\bbeam|\\bhug|\\bapplaud|\\bbabble|\\bblink|\\bbow|\\bcackle|\\bcringe|\\bcower|\\bweep|\\bmumble|\\bwave|\\bponder|\\bpeers quizzically|\\bsnort|\\bsnuggle|\\bcuddle|\\bsmirk|\\blaugh|\\bjumps back from|\\bwhistles? a merry tune|\\bgrumble)", IStreamFilter.type.regex));
		filters.add(new StreamFilter("\\baccusatory\\b", IStreamFilter.type.regex));
		filters.add(new StreamFilter("\\bflush\\b", IStreamFilter.type.regex));
		filters.add(new StreamFilter("\"Boo\"", IStreamFilter.type.string));
		// filters.add(new StreamFilter("dance", IStreamFilter.type.string));
		filters.add(new StreamFilter("^\\((?!You ).+\\)$", IStreamFilter.type.regex));	//act
		filters.add(new StreamFilter("^(You tickle |As you reach out to tickle )", IStreamFilter.type.regex));	// tickle: 1st person
		filters.add(new StreamFilter("^\\w+ just tickled ", IStreamFilter.type.regex));					// tickle: 2nd + 3rd person
		filters.add(new StreamFilter("(^You hug | to avoid your hug|^You try to give \\w+ a hug, but)", IStreamFilter.type.regex));	// hug: 1st person
		filters.add(new StreamFilter("^\\w+ (just hugged|hugs)", IStreamFilter.type.regex));								// hug: 2nd + 3rd person
		filters.add(new StreamFilter("^You\\b (?:.(?!\\bat\\b))*?\\bgrin\\b(?:.(?!\\bat\\b))*?", IStreamFilter.type.regex));													// grin: 1st person, no target
		filters.add(new StreamFilter("^(?!You )\\w+ (?!\\bgives\\b)(?:.(?!\\bat\\b))*?\\bgrin(?:.(?!\\bat\\b))*?", IStreamFilter.type.regex));									// grin: 3rd person, no target
		// grin: 2nd person
		filters.add(new StreamFilter("^.+(\\bgrin.*\\byou\\b|\\byou\\b.*grin).*$", IStreamFilter.type.regex));																	
		// grin: 1st and 3rd person
		filters.add(new StreamFilter("(\\bgrin .*\\bat |\\bat .*\\bgrin|\\bgives .*\\bgrin)", IStreamFilter.type.regex));
		
		return filters.toArray(new IStreamFilter[filters.size()]);
	}

	public static UserStream getViewForUserStream (String streamName) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		for (UserStream view : openStreams)
		{
			if (view.getName().equals(streamName))
			{
				page.activate(view);
				return view;
			}
		}
		
		// none of the already created views match, create a new one
		try {
			return (UserStream) page.showView(VIEW_ID , "rightFolder."+ streamName, IWorkbenchPage.VIEW_ACTIVATE);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setName(String name) {
		this.name = name;
		this.setViewTitle(name);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void scanClients() {
		for (IWarlockClient client : WarlockClientRegistry.getActiveClients()) {
			if (client.getConnection() == null) continue;
			if (client.getConnection().isConnected())
				clientConnected(client);
		}
	}

	public UserStream() {
		super();
		WarlockClientRegistry.addWarlockClientListener(new SWTWarlockClientListener(this));
	}
}
