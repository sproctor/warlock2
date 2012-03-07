package cc.warlock.core.client.internal;

import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockStyle;

public class WarlockHighlight extends WarlockPattern implements IWarlockHighlight {
	protected IWarlockStyle style;

	public WarlockHighlight (String text) {
		super(text);
	}
	
	public WarlockHighlight (String text, boolean literal, boolean caseSensitive, boolean fullWord, IWarlockStyle style)
	{
		super(text, literal, caseSensitive, fullWord);
		this.style = style;
	}
	
	public IWarlockStyle getStyle() {
		return style;
	}
	
	public void setStyle(IWarlockStyle style) {
		this.style = style;
	}
}
