package cc.warlock.core.stormfront.script.wsl.internal;

import cc.warlock.core.stormfront.script.wsl.WSLScriptContext;

public interface IWSLCommandDefinition {
	public void execute(WSLScriptContext context, String arguments) throws InterruptedException;
}
