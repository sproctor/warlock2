package cc.warlock.core.client.internal;

import cc.warlock.core.client.settings.macro.CommandMacroHandler;

public class DefaultMacro extends WarlockMacro {
	private String command;
	
	public DefaultMacro(String command, int keycode, int modifiers) {
		super(keycode, modifiers, new CommandMacroHandler(command));
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}
}
