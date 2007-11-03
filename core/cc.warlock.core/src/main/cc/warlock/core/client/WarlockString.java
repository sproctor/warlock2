package cc.warlock.core.client;

import java.util.ArrayList;
import java.util.List;

public class WarlockString {

	private StringBuffer text = new StringBuffer();
	private ArrayList<WarlockStringStyleRange> styles = new ArrayList<WarlockStringStyleRange>();
	private IWarlockClient client;
	
	public class WarlockStringStyleRange {
		public int start;
		public int length;
		public IWarlockStyle style;
		
		public WarlockStringStyleRange(int start, int length, IWarlockStyle style) {
			this.start = start;
			this.length = length;
			this.style = style;
		}
	}
	
	public WarlockString(IWarlockClient client) {
		this.client = client;
	}
	
	public WarlockString(IWarlockClient client, CharSequence text) {
		this.client = client;
		this.text.append(text);
	}
	
	public WarlockString(IWarlockClient client, String text) {
		this.client = client;
		this.text.append(text);
	}
	
	public String toString() {
		return text.toString();
	}
	
	public void append(String text) {
		this.text.append(text);
	}
	
	public void append(WarlockString string) {
		int charCount = text.length();
		text.append(string.toString());
		for(WarlockStringStyleRange range : string.getStyles()) {
			addStyle(charCount + range.start, range.length, range.style);
		}
	}
	
	public void addStyle(int start, int length, IWarlockStyle style) {
		for(WarlockStringStyleRange curStyle : styles) {
			if(curStyle.style.equals(style)) {
				if(curStyle.start + curStyle.length == start) {
					curStyle.length += length;
					return;
				}
			}
		}
		styles.add(new WarlockStringStyleRange(start, length, style));
	}
	
	public List<WarlockStringStyleRange> getStyles() {
		return styles;
	}
	
	public int length() {
		return text.length();
	}
	
	public void clear() {
		text.setLength(0);
		styles.clear();
	}
	
	public IWarlockClient getClient() {
		return client;
	}
	
	public WarlockString substring(int start) {
		return substring(start, text.length());
	}
	
	public WarlockString substring(int start, int length) {
		WarlockString substring = new WarlockString(client, text.substring(start));
		int end = start + length;
		for(WarlockStringStyleRange style : styles) {
			if(style.start >= start) {
				int stylelength = Math.min(style.length, end - style.start);
				substring.addStyle(style.start - start, stylelength, style.style);
			}
		}
		return substring;
	}
}