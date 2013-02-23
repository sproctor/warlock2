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
		createDefaultMacro("\\xlook\\r", "Numpad_Add", 0);
		createDefaultMacro("\\xhealth\\r", "Numpad_Divide", 0);
		createDefaultMacro("\\xnotoriety\\r", "Numpad_Multiply", 0);
		createDefaultMacro("\\xmana\\r", "Numpad_Subtract", 0);
		createDefaultMacro("\\xup\\r", "Numpad_Decimal",0);
		createDefaultMacro("\\xdown\\r", "Numpad_0",0);
		createDefaultMacro("\\xsw\\r", "Numpad_1",0);
		createDefaultMacro("\\xs\\r", "Numpad_2",0);
		createDefaultMacro("\\xse\\r", "Numpad_3",0);
		createDefaultMacro("\\xw\\r", "Numpad_4",0);
		createDefaultMacro("\\xout\\r", "Numpad_5",0);
		createDefaultMacro("\\xe\\r", "Numpad_6",0);
		createDefaultMacro("\\xnw\\r", "Numpad_7",0);
		createDefaultMacro("\\xn\\r", "Numpad_8",0);
		createDefaultMacro("\\xne\\r", "Numpad_9",0);
		createDefaultMacro("{PageUp}", "PageUp",0);
		createDefaultMacro("{PageDown}", "PageDown",0);
		createDefaultMacro("{LineUp}", "PageUp", IMacro.SHIFT);
		createDefaultMacro("{LineDown}", "PageDown", IMacro.SHIFT);
		createDefaultMacro("{HistoryPrev}", "Up", 0);
		createDefaultMacro("{HistoryNext}", "Down", 0);
		createDefaultMacro("{RepeatLast}", "Enter", IMacro.CTRL);
		createDefaultMacro("{RepeatSecondToLast}", "Enter", IMacro.ALT);
		createDefaultMacro("{ReturnOrRepeatLast}", "Numpad_Enter", 0);
		createDefaultMacro("{RepeatLast}", "Numpad_Enter", IMacro.CTRL);
		createDefaultMacro("{RepeatSecondToLast}", "Numpad_Enter", IMacro.ALT);
		createDefaultMacro("{CycleWindows}", "Tab", 0);
		createDefaultMacro("{BufferTop}", "PageUp", IMacro.CTRL);
		createDefaultMacro("{BufferBottom}","PageDown", IMacro.CTRL);
		createDefaultMacro("{BufferTop}", "Home", IMacro.CTRL);
		createDefaultMacro("{BufferBottom}","End", IMacro.CTRL);
		createDefaultMacro("{CycleWindowsReverse}", "Tab", IMacro.SHIFT);
		createDefaultMacro("\\xassess\\r", "a", IMacro.CTRL);
		createDefaultMacro("{copy}", "c", IMacro.CTRL);
		createDefaultMacro("{cut}", "x", IMacro.CTRL);
		createDefaultMacro("{paste}", "v", IMacro.CTRL);
		createDefaultMacro("{PauseScript}", "Esc", IMacro.SHIFT);
		createDefaultMacro("peer down\\r", "Numpad_0", IMacro.CTRL);
		createDefaultMacro("\\xpath focus damage\\r", "Numpad_0", IMacro.SHIFT);
		createDefaultMacro("\\xaft to port\\r", "Numpad_0", IMacro.ALT);
		createDefaultMacro("peer sw\\r", "Numpad_0", IMacro.CTRL);
		createDefaultMacro("\\xpath focus quick\\r", "Numpad_0", IMacro.SHIFT);
		createDefaultMacro("\\xaft\\r", "Numpad_0", IMacro.ALT);
		createDefaultMacro("peer s\\r", "Numpad_2", IMacro.CTRL);
		createDefaultMacro("\\xpath focus ease\\r", "Numpad_3", IMacro.SHIFT);
		createDefaultMacro("\\xaft to starboard\\r", "Numpad_3", IMacro.ALT);
		createDefaultMacro("peer se\\r", "Numpad_3", IMacro.CTRL);
		createDefaultMacro("\\xport\\r", "Numpad_4", IMacro.ALT);
		createDefaultMacro("peer w\\r", "Numpad_4", IMacro.CTRL);
		createDefaultMacro("peer out\\r", "Numpad_5", IMacro.CTRL);
		createDefaultMacro("\\xstarboard\\r", "Numpad_6", IMacro.ALT);
		createDefaultMacro("peer e\\r", "Numpad_6", IMacro.CTRL);
		createDefaultMacro("\\xforward to port\\r", "Numpad_7", IMacro.ALT);
		createDefaultMacro("peer nw\\r", "Numpad_7", IMacro.CTRL);
		createDefaultMacro("\\xforward\\r", "Numpad_8", IMacro.ALT);
		createDefaultMacro("peer n\\r", "Numpad_8", IMacro.CTRL);
		createDefaultMacro("\\xforward to starboard\\r", "Numpad_9", IMacro.ALT);
		createDefaultMacro("peer ne\\r", "Numpad_9", IMacro.CTRL);
		createDefaultMacro("peer up\\r", "Numpad_Decimal", IMacro.CTRL);
		createDefaultMacro("\\xpath sense\\r", "Numpad_Decimal", IMacro.SHIFT);
		createDefaultMacro("\\xpath check\\r", "Numpad_Multiply", IMacro.SHIFT);
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
		createDefaultMacro("\\xpath focus power\\r", "Numpad_4", IMacro.SHIFT);
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
