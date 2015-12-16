package cc.warlock.core.script.wsl.internal;

import cc.warlock.core.script.wsl.WSLScriptContext;

public interface IWSLCommandDefinition {
	public void execute(WSLScriptContext context, String arguments) throws InterruptedException;
}
