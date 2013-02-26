package cc.warlock.core.stormfront.script.wsl.internal;

import cc.warlock.core.stormfront.script.wsl.WSLScript;

public interface IWSLCommandDefinition {
	public void execute(WSLScript script, String arguments) throws InterruptedException;
}
