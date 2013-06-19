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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.IWarlockFont;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.WarlockStringMarker;
import cc.warlock.core.client.settings.ClientSettings;
import cc.warlock.core.client.settings.PresetStyleConfigurationProvider;
import cc.warlock.core.client.settings.WindowConfigurationProvider;
import cc.warlock.core.settings.IWarlockSetting;
import cc.warlock.core.settings.IWarlockSettingListener;
import cc.warlock.rcp.configuration.GameViewConfiguration;
import cc.warlock.rcp.ui.client.SWTWarlockClientListener;
import cc.warlock.rcp.ui.client.SWTWarlockSettingListener;
import cc.warlock.rcp.util.ColorUtil;

/**
 * This is a replacement of the StyledText widget which adds the features we
 *   need to implement
 * @author Sean Proctor
 */
public class WarlockText {
	
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
	
	private IWarlockClient client;
	private Browser textWidget;
	private boolean browserLoaded = false;
	private boolean ignoreEmptyLines = true;
	//private Font monoFont = null;
	private LinkedList<WarlockStringMarker> markers = new LinkedList<WarlockStringMarker>();
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
	private WindowSettingsListener settingListener;
	final private String streamName;
	
	public WarlockText(Composite parent, IWarlockClientViewer viewer, String streamName) {
		this.streamName = streamName;
		
		textWidget = new Browser(parent, SWT.MOZILLA);
		textWidget.addProgressListener(new ProgressListener() {
			@Override
			public void changed(ProgressEvent event) {}
			@Override
			public void completed(ProgressEvent event) {
				browserLoaded = true;
			}
		});

		textWidget.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		textWidget.addStatusTextListener(new StatusTextListener() {
			@Override
			public void changed(StatusTextEvent event) {
				//if(event.text.startsWith("command:"))
					//MessageDialog.openInformation(textWidget.getShell(), "status message", event.text);
				System.out.println("status: " + event.text);
			}
		});

		try {
			Bundle bundle = Platform.getBundle("cc.warlock.rcp");
			URL url = FileLocator.find(bundle, new Path("text.html"), null);
			String urlString = FileLocator.resolve(url).toExternalForm();
			textWidget.setUrl(urlString);
			
			// Wait for the browser to finish loading
			while(!browserLoaded) {
				Display.getDefault().readAndDispatch();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		//RCPUtil.addTextContextMenu(textWidget, viewer, streamName);
	}
	
	public void selectAll() {
		//textWidget.selectAll();
	}
	
	public void copy() {
		//textWidget.copy();
	}
	
	public void pageUp() {
		if (isAtBottom()) {
			//textWidget.setCaretOffset(textWidget.getCharCount());
		}
		//textWidget.invokeAction(ST.PAGE_UP);
	}
	
	public void pageDown() {
		//textWidget.invokeAction(ST.PAGE_DOWN);
	}
	
	public void clearText() {
		textWidget.setText("");
		markers.clear();
	}
	
	public void setLineLimit(int limit) {
		//lineLimit = limit;
	}
	
	public void appendRaw(String string) {
		String text = StringEscapeUtils.escapeHtml(string).replaceAll("(\r\n|\n)", "<br/>");
		if(!textWidget.execute("append(\"" + text + "\")"))
			System.err.println("Problem appending: " + text);
	}
	
	private Pattern newlinePattern = Pattern.compile("\r?\n");
	private void removeEmptyLines(int offset) {
		/*int line = textWidget.getLineAtOffset(offset);
		int start = textWidget.getOffsetAtLine(line);
		int end = textWidget.getCharCount();
		if(start >= end)
			return;
		String str = textWidget.getTextRange(start, end - start);
		Matcher m = newlinePattern.matcher(str);
		
		int lineStart = 0;
		while(m.find(lineStart)) {
			if(lineStart == m.start()) {
				int matchPos = start + m.start();
				int matchLen = m.end() - m.start();
				// Add the newline marker. We give it an initial length of 1
				//   so it gets added correctly into the tree of markers
				WarlockStringMarker marker = new WarlockStringMarker(
						new WarlockStyle("newline"), matchPos, matchPos + matchLen);
				this.addInternalMarker(marker, markers);
				
				// then remove the newline from the text
				textWidget.replaceTextRange(matchPos, matchLen, "");
				// and shrink down the newline marker because the actual newline is no longer there.
				marker.setEnd(matchPos);
				WarlockStringMarker.updateMarkers(-matchLen, marker, markers);
				// Recursive call. if this could be a tail call, that would be awesome.
				removeEmptyLines(start);
				break;
			} else {
				lineStart = m.end();
			}
		}*/
	}
	
	private void restoreNewlines(int offset, Collection<WarlockStringMarker> markerList) {
		for(Iterator<WarlockStringMarker> iter = markerList.iterator();
		iter.hasNext(); )
		{
			WarlockStringMarker marker = iter.next();
		
			Collection<WarlockStringMarker> subList = marker.getSubMarkers();
			if(subList != null)
				restoreNewlines(offset, subList);
			
			// check to make sure we're a newline in the appropriate area
			if(marker.getStart() < offset)
				continue;
			String name = marker.getName();
			if(name == null || !name.equals("newline"))
				continue;
			
			// check if we're an empty line
			//if(marker.getStart() == 0 || textWidget.getTextRange(marker.getStart() - 1, 1).equals("\n"))
				//continue;
			
			// we're not an empty line, put us back into action
			//textWidget.replaceTextRange(marker.getStart(), 0, "\n");
			// TODO: this should actually just affect markers after us... oh well.
			WarlockStringMarker.updateMarkers(1, marker, markers);
			iter.remove();
		}
	}
	
	private Collection<StyleRange> mergeStyleRangeLists(Collection<StyleRange> list1, Collection<StyleRange> list2) {
		LinkedList<StyleRange> resultList = new LinkedList<StyleRange>();
		for(StyleRange style : list1) {
			resultList.add(style);
		}
		
		mergeLoop: for(StyleRange mergingStyle : list2) {
			if(mergingStyle == null)
				continue;
			for(ListIterator<StyleRange> iter = resultList.listIterator();
			iter.hasNext(); )
			{
				StyleRange style = iter.next();
				if(style == null)
					continue;
				
				// if the mergeStyle came before the current style, add the
				//   mergeStyle before it and go to the next mergeStyle
				if(style.start >= mergingStyle.start + mergingStyle.length) {
					iter.previous();
					iter.add(mergingStyle);
					continue mergeLoop;
				}
				
				// If the mergeStyle came after the current style, continue on
				if(mergingStyle.start >= style.start + style.length)
					continue;
				
				iter.remove();
				
				int subStart;
				
				if(style.start < mergingStyle.start) {
					// Add the style before they overlap
					StyleRange newStyle = (StyleRange)style.clone();
					newStyle.length = mergingStyle.start - style.start;
					iter.add(newStyle);
					
					subStart = mergingStyle.start;
				} else if(mergingStyle.start < style.start) {
					// Add the style before they overlap
					StyleRange newStyle = (StyleRange)mergingStyle.clone();
					newStyle.length = style.start - mergingStyle.start;
					iter.add(newStyle);
					
					subStart = style.start;
				} else {
					subStart = style.start;
				}
				
				int subEnd;
				if(style.start + style.length < mergingStyle.start + mergingStyle.length) {
					subEnd = style.start + style.length;
				} else {
					subEnd = mergingStyle.start + mergingStyle.length;
				}
				
				StyleRange newStyle = this.mergeStyleRanges(style, mergingStyle);
				newStyle.start = subStart;
				newStyle.length = subEnd - subStart;
				iter.add(newStyle);
				
				if(style.start + style.length < mergingStyle.start + mergingStyle.length) {
					int length = mergingStyle.start + mergingStyle.length - subEnd;
					mergingStyle.start = subEnd;
					mergingStyle.length = length;
					continue;
				} else if(mergingStyle.start + mergingStyle.length < style.start + style.length) {
					StyleRange endStyle = (StyleRange)style.clone();
					endStyle.start = subEnd;
					endStyle.length = style.start + style.length - subEnd;
					iter.add(endStyle);
				}
				// else both styles end at the same time
				
				// We matched a style and inserted the new style, so we're done
				continue mergeLoop;
			}
			
			resultList.add(mergingStyle);
		}
		
		return resultList;
	}
	
	/*private Collection<StyleRange> getHighlights(int start, int end) {
		ArrayList<StyleRange> highlightList = new ArrayList<StyleRange>();
		if(client == null)
			return highlightList;
		
		String text = textWidget.getTextRange(start, end - start);
		
		
		for (Iterator<? extends IWarlockHighlight> iter = client.getHighlightsIterator();
				iter.hasNext(); ) {
			IWarlockHighlight highlight = iter.next();
			if(highlight == null)
				continue;
			Pattern p;
			try {
				p = highlight.getPattern();
			} catch(PatternSyntaxException e) {
				continue;
			}
			if(p == null)
				continue;
			Matcher matcher = p.matcher(text);
			
			while (matcher.find())
			{
				MatchResult result = matcher.toMatchResult();
				
				IWarlockStyle style = highlight.getStyle();
				int highlightStart = result.start() + start;
				int highlightLength = result.end() - result.start();
				if(style.isFullLine()) {
					int lineNum = textWidget.getLineAtOffset(highlightStart);
					highlightStart = textWidget.getOffsetAtLine(lineNum);
					if(lineNum + 1 >= textWidget.getLineCount())
						highlightLength = end - highlightStart;
					else
						highlightLength = textWidget.getOffsetAtLine(lineNum + 1) - highlightStart;
				}
				
				StyleRangeWithData styleRange = warlockStyleToStyleRange(style,
						highlightStart, highlightLength);
				if(styleRange == null)
					continue;
				highlightList.add(styleRange);
				
				try{
					if (style.getSound() != null && !style.getSound().equals("")){
						SoundPlayer.play(style.getSound());
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return highlightList;
	}*/
	
	private void getMarkerStyles(WarlockStringMarker marker,
			StyleRange baseStyle, Collection<StyleRange> resultStyles) {
		int pos = marker.getStart();
		for(WarlockStringMarker subMarker : marker.getSubMarkers()) {
			
			int nextPos = subMarker.getStart();
			
			StyleRange styleRange = mergeStyleRanges(baseStyle,
					warlockStyleToStyleRange(marker.getStyle(), pos, nextPos - pos));
			
			if(nextPos > pos)
				resultStyles.add(styleRange);
			
			getMarkerStyles(subMarker, styleRange, resultStyles);

			pos = subMarker.getEnd();
		}
		
		if(marker.getEnd() > pos) {
			StyleRange styleRange = mergeStyleRanges(baseStyle,
					warlockStyleToStyleRange(marker.getStyle(), pos, marker.getEnd() - pos));
			resultStyles.add(styleRange);
		}
	}
	
	public String markupString(WarlockString wstring) {
		int offset = 0;
		String text = "";
		for(WarlockStringMarker marker: wstring.getStyles()) {
			String openTag = "";
			String closeTag = "";
			String command = marker.getStyle().getCommand();
			
			ArrayList<String> tagClasses = new ArrayList<String>();
			String classString = "";
			String name = marker.getStyle().getName();
			if(name != null)
				tagClasses.add(name);
			if("roomName".equals(name))
				System.out.println("roomname");
			if(marker.getStyle().isFullLine()) {
				tagClasses.add("full-line");
				System.out.println("added full-line");
			}
			if(name != null) {
				classString = " class=\\\""+StringUtils.join(tagClasses, ' ')+"\\\"";
			}
			if(command != null) {
				openTag = "<a href=\\\"javascript:\\\""+classString+" onclick=\\\"window.status='command:"+command+"'\\\">";
				closeTag = "</a>";
			} else if(name != null) {
				openTag = "<span" + classString + ">";
				closeTag = "</span>";
			}
			
			String before = StringEscapeUtils.escapeHtml(wstring.toString().substring(offset, marker.getStart()));
			offset = marker.getEnd();
			
			String substring = markupString(wstring.getMarkerContents(marker));
			
			text += before + openTag + substring + closeTag;
		}
		return text + StringEscapeUtils.escapeHtml(wstring.toString().substring(offset));
	}
	
	public void appendLine(WarlockString wstring) {
		String text = markupString(wstring);
		String script = "appendLine(\"" + text.replaceAll("(\r\n|\n)", "<br/>") + "\");";
		
		textWidget.execute(script);
	}
	
	public void append(WarlockString wstring) {
		String text = markupString(wstring);
		String script = "append(\"" + text.replaceAll("(\r\n|\n)", "<br/>") + "\");";
		
		textWidget.execute(script);
	}
	
	private void addComponentMarker(WarlockStringMarker marker, WarlockStringMarker topLevel) {
		if(marker.getComponentName() != null) {
			this.addMarker(marker);
			IWarlockStyle baseStyle = topLevel.getBaseStyle(marker);
			if(baseStyle != null)
				marker.setStyle(baseStyle);
		} else {
			for(WarlockStringMarker subMarker : marker.getSubMarkers()) {
				addComponentMarker(subMarker, topLevel);
			}
		}
	}
	
	private StyleRangeWithData warlockStyleToStyleRange(IWarlockStyle style, int start, int length) {
		StyleRangeWithData styleRange = new StyleRangeWithData();
		
		WarlockColor foreground = style.getForegroundColor();
		WarlockColor background = style.getBackgroundColor();
		boolean underline = style.isUnderline();
		boolean fullLine = style.isFullLine();
		
		IClientSettings settings = null;
		if(client != null)
			settings = client.getClientSettings();
		if(settings == null)
			settings = ClientSettings.getGlobalClientSettings();
		IWarlockStyle presetStyle = PresetStyleConfigurationProvider.getProvider(settings).getStyle(style.getName());
		if(presetStyle != null) {
			if(background == null || background.isDefault())
				background = presetStyle.getBackgroundColor();
			if(foreground == null || foreground.isDefault())
				foreground = presetStyle.getForegroundColor();
			underline |= presetStyle.isUnderline();
			fullLine |= presetStyle.isFullLine();
		}
		
		/*
		if(monoFont != null && style.isMonospace()) {
			styleRange.font = monoFont;
		}*/
		
		
		styleRange.fontStyle = SWT.NORMAL;
		if (style.isBold())
			styleRange.fontStyle |= SWT.BOLD;
		if (style.isItalic())
			styleRange.fontStyle |= SWT.ITALIC;
		styleRange.underline = underline;
		
		if (foreground != null && !foreground.isDefault())
			styleRange.foreground = ColorUtil.warlockColorToColor(foreground);
		if (background != null && !background.isDefault())
			styleRange.background = ColorUtil.warlockColorToColor(background);

		styleRange.start = start;
		styleRange.length = length;
		
		//if(fullLine)
			//textWidget.setLineBackground(textWidget.getLineAtOffset(styleRange.start), 1, styleRange.background);
		//if(style.getCommand() != null)
			//styleRange.action = style.getCommand();
		if(style.getName() != null)
			styleRange.data.put("name", style.getName());
		
		return styleRange;
	}
	
	private StyleRange mergeStyleRanges(StyleRange style1, StyleRange style2) {
		if(style1 == null)
			return style2;
		if(style2 == null)
			return style1;
		
		StyleRange newStyle;
		// start with a cloned style1, unless style2 has data, but style1 doesn't
		if(style2 instanceof StyleRangeWithData && !(style1 instanceof StyleRangeWithData))
			newStyle = new StyleRangeWithData(style1);
		else
			newStyle = (StyleRange)style1.clone();
		
		newStyle.start = style2.start;
		newStyle.length = style2.length;
		if(style2.font != null)
			newStyle.font = style2.font;
		if(style2.background != null)
			newStyle.background = style2.background;
		if(style2.foreground != null)
			newStyle.foreground = style2.foreground;
		if(style2.fontStyle != SWT.NORMAL)
			newStyle.fontStyle = style2.fontStyle;
		if(style2.strikeout) newStyle.strikeout = true;
		if(style2.underline) newStyle.underline = true;
		
		if(style2 instanceof StyleRangeWithData) {
			StyleRangeWithData _newStyle = (StyleRangeWithData)newStyle;
			StyleRangeWithData _style2 = (StyleRangeWithData)style2;
			
			_newStyle.data.putAll(_style2.data);
			if(_style2.action != null)
				_newStyle.action = _style2.action;
			if(_style2.tooltip != null)
				_newStyle.tooltip = _style2.tooltip;
		}
		
		return newStyle;
	}
	
	public boolean isAtBottom() {
		return true;
		//return textWidget.getLinePixel(textWidget.getLineCount()) <= textWidget.getClientArea().height;
	}
	
	private void postTextChange(boolean atBottom, int offset) {
		
		if(ignoreEmptyLines) {
			removeEmptyLines(offset);
			restoreNewlines(offset, markers);
		}
		
		constrainLineLimit(atBottom);
	}
	
	// this function removes the first "delta" amount of characters
	private void updateMarkers(int delta) {
		for(Iterator<WarlockStringMarker> iter = markers.iterator();
		iter.hasNext(); )
		{
			WarlockStringMarker marker = iter.next();
			
			// If the marker is moved off the beginning, remove it
			if(marker.getEnd() + delta < 0) {
				iter.remove();
				continue;
			}
			// move us accordingly
			marker.move(delta);
		}
	}
	
	public void addInternalMarker(WarlockStringMarker marker,
			LinkedList<WarlockStringMarker> markerList) {
		ListIterator<WarlockStringMarker> iter = markerList.listIterator();
		try {
			while(true) {
				if(!iter.hasNext()) {
					iter.add(marker);
					break;
				}
				WarlockStringMarker cur = iter.next();
				if(cur.getEnd() > marker.getStart()) {
					if(marker.getEnd() > cur.getStart()) {
						addInternalMarker(marker, cur.getSubMarkers());
						return;
					}

					iter.previous();
					iter.add(marker);
					break;
				}
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void addMarker(WarlockStringMarker marker) {
		ListIterator<WarlockStringMarker> iter = markers.listIterator();
		try {
			while(true) {
				if(!iter.hasNext()) {
					iter.add(marker);
					break;
				}
				WarlockStringMarker cur = iter.next();
				if(cur.getEnd() > marker.getStart()) {
					if(marker.getEnd() > cur.getStart()) {
						throw new Exception("Bad marker!");
					}

					iter.previous();
					iter.add(marker);
					break;
				}
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void replaceMarker(String name, WarlockString text) {
		WarlockStringMarker marker = null;
		IWarlockStyle baseStyle = null;
		for(WarlockStringMarker subMarker : markers) {
			marker = getMarkerByComponent(name, subMarker);
			if(marker != null) {
				baseStyle = subMarker.getBaseStyle(marker);
				break;
			}
		}
		if(marker == null)
			return;
		
		int start = marker.getStart();
		int length = marker.getEnd() - start;
		boolean atBottom = isAtBottom();
		//textWidget.replaceTextRange(start, length, text.toString());
		marker.clear();
		int newLength = text.length();
		marker.setEnd(start + newLength);
		WarlockStringMarker.updateMarkers(newLength - length, marker, markers);
		
		// Add the new styles to the existing marker
		for(WarlockStringMarker newMarker : text.getStyles()) {
			marker.addMarker(newMarker.copy(start));
		}
		
		/* Break up the ranges and merge overlapping styles because SWT only
		 * allows 1 style per section
		 */
		WarlockStringMarker markerWithStyle = marker.clone();
		markerWithStyle.setStyle(baseStyle);
		LinkedList<StyleRange> newStyles = new LinkedList<StyleRange>();
		getMarkerStyles(markerWithStyle, new StyleRangeWithData(), newStyles);
		//showStyles(newStyles, marker.getStart(), marker.getEnd());
		
		postTextChange(atBottom, start);
	}
	
	private WarlockStringMarker getMarkerByComponent(String componentName,
			WarlockStringMarker marker) {
		String myName = marker.getComponentName();
		if(myName != null && myName.equals(componentName))
			return marker;
		
		for(WarlockStringMarker subMarker : marker.getSubMarkers()) {
			WarlockStringMarker result = getMarkerByComponent(componentName, subMarker);
			if(result != null)
				return result;
		}
		return null;
	}
	
	private void constrainLineLimit(boolean atBottom) {
		// 'status' is a pointer that allows us to change the object in our parent..
		// in this method... it is intentional.
		/*if (lineLimit > 0) {
			int lines = textWidget.getLineCount();
			if (lines > lineLimit) {
				int linesToRemove = lines - lineLimit;
				int charsToRemove = textWidget.getOffsetAtLine(linesToRemove);
				if(atBottom) {
					textWidget.replaceTextRange(0, charsToRemove, "");
					updateMarkers(-charsToRemove);
				} else {
					int pixelsToRemove = textWidget.getLinePixel(linesToRemove);
					textWidget.replaceTextRange(0, charsToRemove, "");
					updateMarkers(-charsToRemove);
					if(pixelsToRemove < 0)
						textWidget.setTopPixel(-pixelsToRemove);
				}
			}
		}*/
	}
	
	public void setIgnoreEmptyLines(boolean ignoreLines) {
		this.ignoreEmptyLines = ignoreLines;
	}
	
	public Browser getTextWidget() {
		return textWidget;
	}
	
	public void setClient(IWarlockClient client) {
		if(this.client != null)
			WarlockClientRegistry.removeWarlockClientListener(clientListener);
		if(settingListener != null)
			settingListener.remove();
		
		this.client = client;
		
		// if client already has settings, we'll unintentially load them twice
		loadSettings();
		
		if(client != null)
			WarlockClientRegistry.addWarlockClientListener(clientListener);
	}
	
	protected IWarlockClient getClient() {
		return client;
	}
	
	public void setForeground(String color) {
		String text = "$('body').css('color', '"+color+"')";
		if(!textWidget.execute(text))
			System.err.println("Error setting \""+streamName+"\" fg color: \""+text+"\"");
	}
	
	public void setBackground(String color) {
		String text = "$('body').css('background-color', '"+color+"')";
		if(!textWidget.execute(text))
			System.err.println("Error setting \""+streamName+"\" bg color: \""+text+"\"");
	}
	
	public void setFont(String name, int size) {
		String text = "$('body').css('font', '"+(size > 0 ? size : "")+" "+name+"')";
		if(!textWidget.execute(text))
			System.err.println("Error setting \""+streamName+"\" bg color: \""+text+"\"");
	}
	
	public void setForeground(String name, String color) {
		String text = "setStyle('"+name+"', 'color', '"+color+"')";
		if(!textWidget.execute(text))
			System.err.println("Error setting \""+streamName+"\" fg color: \""+text+"\"");
		System.out.println("Setting \""+streamName+"\" fg: " + text);
	}
	
	public void setBackground(String name, String color) {
		String text = "setStyle('"+name+"', 'background-color', '"+color+"')";
		if(!textWidget.execute(text))
			System.err.println("Error setting \""+streamName+"\" bg color: \""+text+"\"");
		System.out.println("Setting \""+streamName+"\" bg: " + text);
	}
	
	public void setFont(String name, String fontName, int size) {
		String value = (size > 0 ? size  +"px" : "inherit") + "  " + fontName;
		String text = "setStyle('"+name+"', 'font', '"+value+"')";
		if(!textWidget.execute(text))
			System.err.println("Error setting \""+streamName+"\" font: \""+text+"\"");
		System.out.println("Setting \""+streamName+"\" font: " + text);
	}
	
	private void loadSettings() {
		if(settingListener != null)
			settingListener.remove();
		
		if(textWidget.isDisposed())
			return;
		
		IClientSettings settings = null;
		if(client != null)
			settings = client.getClientSettings();
		
		if(settings == null)
			settings = ClientSettings.getGlobalClientSettings();
		
		WindowConfigurationProvider wprovider = WindowConfigurationProvider.getProvider(settings);
		settingListener = new WindowSettingsListener(wprovider);
		
		// Set to defaults first, then try window settings later
		String background = wprovider.getWindowBackground(streamName).toString();
		String foreground = wprovider.getWindowForeground(streamName).toString();
		IWarlockFont font = wprovider.getWindowFont(streamName);
		
		setBackground(background);
		setForeground(foreground);

		if (font.isDefaultFont()) {
			String defaultFontFace = GameViewConfiguration.getProvider(settings).getDefaultFontFace();
			int defaultFontSize = GameViewConfiguration.getProvider(settings).getDefaultFontSize();
			setFont(defaultFontFace, defaultFontSize);
		} else {
			setFont(font.getFamilyName(), font.getSize());
		}
		
		IWarlockFont columnFont = wprovider.getWindowMonoFont(streamName);
		if(columnFont == null || columnFont.isDefaultFont()) {
			setFont("mono", "monospace", GameViewConfiguration.getProvider(settings).getDefaultFontSize());
		} else {
			String fontFace = columnFont.getFamilyName();
			int fontSize = columnFont.getSize();
			setFont("mono", fontFace, fontSize);
		}
		
		PresetStyleConfigurationProvider sprovider = PresetStyleConfigurationProvider.getProvider(settings);
		for(Entry<String, IWarlockStyle>dstyle: PresetStyleConfigurationProvider.getDefaultStyles().entrySet()) {
			IWarlockStyle style = sprovider.getStyle(dstyle.getKey());
			if(style == null)
				style = dstyle.getValue();
			WarlockColor fg = style.getForegroundColor();
			if(fg != null && !fg.isDefault())
				setForeground(style.getName(), fg.toString());
			WarlockColor bg = style.getBackgroundColor();
			if(bg != null && !bg.isDefault())
				setBackground(style.getName(), bg.toString());
		}
	}
	
	public String getName() {
		return streamName;
	}
}