package cc.warlock.core.client.internal;

import cc.warlock.core.client.settings.macro.CommandMacroHandler;

public class DefaultMacro extends WarlockMacro {
	private String command;
	
	public DefaultMacro(String command, String keyString) {
		super(keyString, new CommandMacroHandler(command));
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}
}
