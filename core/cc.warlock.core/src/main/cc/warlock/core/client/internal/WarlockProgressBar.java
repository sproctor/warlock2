package cc.warlock.core.client.internal;

import cc.warlock.core.client.IWarlockDialog;


public class WarlockProgressBar implements IWarlockDialog {
	private String id;
	private String text;
	private String value;
	private String left;
	private String top;
	private String width;
	private String height;
	
	public WarlockProgressBar(String id, String text, String value, String left, String top,
			String width, String height) {
		this.id = id;
		this.text = text;
		this.value = value;
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
	}
	
	public int getValue() {
		return Integer.parseInt(value);
	}
	
	public int getLeft() {
		return percentageToInt(left);
	}
	
	public int getWidth() {
		return percentageToInt(width);
	}
	
	public int getTop() {
		return percentageToInt(top);
	}
	
	public int getHeight() {
		return percentageToInt(height);
	}
	
	public String getId() {
		return id;
	}
	
	public String getText() {
		return text;
	}
	
	private int percentageToInt(String number) {
		int p = number.indexOf("%");
		return Integer.parseInt(number.substring(0, p));
	}
}
