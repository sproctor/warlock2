package cc.warlock.core.settings;

import cc.warlock.core.client.internal.WarlockPattern;

public class PatternSetting extends AbstractPatternSetting<WarlockPattern> {
	public PatternSetting (IWarlockSetting parent, String path) {
		super(parent, path);
	}
	
	protected WarlockPattern createPattern(String text, boolean literal, boolean caseSensitive, boolean fullWord) {
		return new WarlockPattern(text, literal, caseSensitive, fullWord);
	}
}
