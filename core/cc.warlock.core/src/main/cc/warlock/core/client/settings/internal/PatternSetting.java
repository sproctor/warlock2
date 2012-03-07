package cc.warlock.core.client.settings.internal;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.internal.WarlockPattern;

public class PatternSetting extends AbstractPatternSetting<WarlockPattern> {
	public PatternSetting (Preferences parentNode, String path) {
		super(parentNode, path);
	}
	
	protected WarlockPattern createPattern(String text, boolean literal, boolean caseSensitive, boolean fullWord) {
		return new WarlockPattern(text, literal, caseSensitive, fullWord);
	}
}
