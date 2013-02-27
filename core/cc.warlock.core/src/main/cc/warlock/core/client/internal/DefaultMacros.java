package cc.warlock.core.client.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import cc.warlock.core.client.IMacro;

public class DefaultMacros {
	private static DefaultMacros instance = new DefaultMacros();
	
	private ArrayList<WarlockMacro> macros = new ArrayList<WarlockMacro>();
	
	private DefaultMacros() {
		createDefaultMacro("\\xxml toggle containers\\r", "c", IMacro.ALT);
		createDefaultMacro("\\xxml toggle dialogs\\r", "d", IMacro.ALT);
		createDefaultMacro("{ExportDialog}", "e", IMacro.CTRL | IMacro.ALT);
		createDefaultMacro("{HighlightsDialog}", "h", IMacro.CTRL | IMacro.ALT);
		createDefaultMacro("{ImportDialog}", "i", IMacro.CTRL | IMacro.ALT);
		createDefaultMacro("{ToggleLinks}", "l", IMacro.ALT);
		createDefaultMacro("{ToggleMusic}", "m", IMacro.ALT);
		createDefaultMacro("{ToggleImages}", "i", IMacro.ALT);
		createDefaultMacro("{ToggleSounds}", "s", IMacro.ALT);
		createDefaultMacro("{MacrosDialog}", "m", IMacro.CTRL | IMacro.ALT);
		createDefaultMacro("{ChooseSkin}", "s", IMacro.CTRL | IMacro.ALT);
		createDefaultMacro("{VariablesDialog}", "v", IMacro.ALT);
		createDefaultMacro("{MacroSet}0", "0", IMacro.ALT);
		createDefaultMacro("{MacroSet}1", "1", IMacro.ALT);
		createDefaultMacro("{MacroSet}2", "2", IMacro.ALT);
		createDefaultMacro("{MacroSet}3", "3", IMacro.ALT);
		createDefaultMacro("{MacroSet}4", "4", IMacro.ALT);
		createDefaultMacro("{MacroSet}5", "5", IMacro.ALT);
		createDefaultMacro("{MacroSet}6", "6", IMacro.ALT);
		createDefaultMacro("{MacroSet}7", "7", IMacro.ALT);
		createDefaultMacro("{MacroSet}8", "8", IMacro.ALT);
		createDefaultMacro("{MacroSet}9", "9", IMacro.ALT);
		createDefaultMacro("{Restart}", "F5", IMacro.CTRL);
		createDefaultMacro("\\xretreat\\r", "r", IMacro.CTRL);
		createDefaultMacro("\\xlook\\r", "Keypad +", 0);
		createDefaultMacro("\\xhealth\\r", "Keypad /", 0);
		createDefaultMacro("\\xnotoriety\\r", "Keypad *", 0);
		createDefaultMacro("\\xmana\\r", "Keypad -", 0);
		createDefaultMacro("\\xup\\r", "Keypad .",0);
		createDefaultMacro("\\xdown\\r", "Keypad 0",0);
		createDefaultMacro("\\xsw\\r", "Keypad 1",0);
		createDefaultMacro("\\xs\\r", "Keypad 2",0);
		createDefaultMacro("\\xse\\r", "Keypad 3",0);
		createDefaultMacro("\\xw\\r", "Keypad 4",0);
		createDefaultMacro("\\xout\\r", "Keypad 5",0);
		createDefaultMacro("\\xe\\r", "Keypad 6",0);
		createDefaultMacro("\\xnw\\r", "Keypad 7",0);
		createDefaultMacro("\\xn\\r", "Keypad 8",0);
		createDefaultMacro("\\xne\\r", "Keypad 9",0);
		createDefaultMacro("{PageUp}", "Page Up",0);
		createDefaultMacro("{PageDown}", "Page Down",0);
		createDefaultMacro("{LineUp}", "Page Up", IMacro.SHIFT);
		createDefaultMacro("{LineDown}", "Page Down", IMacro.SHIFT);
		createDefaultMacro("{HistoryPrev}", "Up", 0);
		createDefaultMacro("{HistoryNext}", "Down", 0);
		createDefaultMacro("{RepeatLast}", "Enter", IMacro.CTRL);
		createDefaultMacro("{RepeatSecondToLast}", "Enter", IMacro.ALT);
		createDefaultMacro("{ReturnOrRepeatLast}", "Keypad Enter", 0);
		createDefaultMacro("{RepeatLast}", "Keypad Enter", IMacro.CTRL);
		createDefaultMacro("{RepeatSecondToLast}", "Keypad Enter", IMacro.ALT);
		createDefaultMacro("{CycleWindows}", "Tab", 0);
		createDefaultMacro("{BufferTop}", "Page Up", IMacro.CTRL);
		createDefaultMacro("{BufferBottom}","Page Down", IMacro.CTRL);
		createDefaultMacro("{BufferTop}", "Home", IMacro.CTRL);
		createDefaultMacro("{BufferBottom}","End", IMacro.CTRL);
		createDefaultMacro("{CycleWindowsReverse}", "Tab", IMacro.SHIFT);
		createDefaultMacro("\\xassess\\r", "a", IMacro.CTRL);
		createDefaultMacro("{copy}", "c", IMacro.CTRL);
		createDefaultMacro("{cut}", "x", IMacro.CTRL);
		createDefaultMacro("{paste}", "v", IMacro.CTRL);
		createDefaultMacro("{PauseScript}", "Esc", IMacro.SHIFT);
		createDefaultMacro("peer down\\r", "Keypad 0", IMacro.CTRL);
		createDefaultMacro("\\xpath focus damage\\r", "Keypad 0", IMacro.SHIFT);
		createDefaultMacro("\\xaft to port\\r", "Keypad 0", IMacro.ALT);
		createDefaultMacro("peer sw\\r", "Keypad 0", IMacro.CTRL);
		createDefaultMacro("\\xpath focus quick\\r", "Keypad 0", IMacro.SHIFT);
		createDefaultMacro("\\xaft\\r", "Keypad 0", IMacro.ALT);
		createDefaultMacro("peer s\\r", "Keypad 2", IMacro.CTRL);
		createDefaultMacro("\\xpath focus ease\\r", "Keypad 3", IMacro.SHIFT);
		createDefaultMacro("\\xaft to starboard\\r", "Keypad 3", IMacro.ALT);
		createDefaultMacro("peer se\\r", "Keypad 3", IMacro.CTRL);
		createDefaultMacro("\\xport\\r", "Keypad 4", IMacro.ALT);
		createDefaultMacro("peer w\\r", "Keypad 4", IMacro.CTRL);
		createDefaultMacro("peer out\\r", "Keypad 5", IMacro.CTRL);
		createDefaultMacro("\\xstarboard\\r", "Keypad 6", IMacro.ALT);
		createDefaultMacro("peer e\\r", "Keypad 6", IMacro.CTRL);
		createDefaultMacro("\\xforward to port\\r", "Keypad 7", IMacro.ALT);
		createDefaultMacro("peer nw\\r", "Keypad 7", IMacro.CTRL);
		createDefaultMacro("\\xforward\\r", "Keypad 8", IMacro.ALT);
		createDefaultMacro("peer n\\r", "Keypad 8", IMacro.CTRL);
		createDefaultMacro("\\xforward to starboard\\r", "Keypad 9", IMacro.ALT);
		createDefaultMacro("peer ne\\r", "Keypad 9", IMacro.CTRL);
		createDefaultMacro("peer up\\r", "Keypad .", IMacro.CTRL);
		createDefaultMacro("\\xpath sense\\r", "Keypad .", IMacro.SHIFT);
		createDefaultMacro("\\xpath check\\r", "Keypad *", IMacro.SHIFT);
		createDefaultMacro("\\xdemeanor neutral\\r", "e", IMacro.ALT);
		createDefaultMacro("\\xdemeanor reserved\\r", "w", IMacro.ALT);
		createDefaultMacro("\\xdemeanor cold\\r", "q", IMacro.ALT);
		//createDefaultMacro("\\xget \\?\\rput \\? in my %container\\r",116, IMacro.CTRL);
		//createDefaultMacro("\\xremove my %helmet\\rput my %helmet in my %container\\r\\premove my %gloves\\rput my %gloves in my %container\\r",16777235,0);
		//createDefaultMacro("\\xopen my \\?\\ropen my %gpouch\\r\\pfill my %gpouch with my \\?\\r\\pclose my %gpouch\\rclose my \\?\\r",16777228,0);
		//createDefaultMacro("\\xopen my %gpouch\\rappr my %gpouch\\r\\p\\p\\p\\p\\p\\p\\p\\p\\p\\xclose my %gpouch\\r",16777230,0);
		//createDefaultMacro("\\xopen my \\?\\rl in my \\?\\r\\pclose my \\?\\r",16777231,0);
		//createDefaultMacro("\\xget my %helmet\\rwear my %helmet\\r\\pget my %gloves\\rwear my %gloves\\r",16777234,0);
		createDefaultMacro("\\xdemeanor friendly\\r", "r", IMacro.ALT);
		createDefaultMacro("\\xdemeanor warm\\r", "t", IMacro.ALT);
		createDefaultMacro("\\xpath focus power\\r", "Keypad 4", IMacro.SHIFT);
		//createDefaultMacro("\\xremove my %shield\\rstow my %shield\\r",16777235, IMacro.CTRL);
		//createDefaultMacro("\\xget my %shield\\rwear my %shield\\r",16777234, IMacro.CTRL);
	}
	
	public static DefaultMacros instance ()
	{	
		return instance;
	}
	
	private void createDefaultMacro(String command, String key, int modifier) {
		macros.add(new WarlockMacro(command, key, modifier));
	}
	
	public Collection<WarlockMacro> getCollection() {
		return Collections.unmodifiableCollection(macros);
	}
	
	public WarlockMacro getMacro(String keyString) {
		for(WarlockMacro macro : macros) {
			if(macro.getKeyString().equals(keyString))
				return macro;
		}
		return null;
	}
}
