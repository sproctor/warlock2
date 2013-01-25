package cc.warlock.core.client.settings.internal;

import cc.warlock.core.client.internal.WarlockPattern;
import cc.warlock.core.configuration.IWarlockSetting;

public class PatternSetting extends AbstractPatternSetting<WarlockPattern> {
	public PatternSetting (IWarlockSetting parent, String path) {
		super(parent, path);
	}
	
	protected WarlockPattern createPattern(String text, boolean literal, boolean caseSensitive, boolean fullWord) {
		return new WarlockPattern(text, literal, caseSensitive, fullWord);
	}
}
