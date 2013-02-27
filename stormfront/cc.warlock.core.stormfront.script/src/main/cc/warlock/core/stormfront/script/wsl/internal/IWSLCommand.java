package cc.warlock.core.stormfront.script.wsl.internal;

import cc.warlock.core.stormfront.script.wsl.WSLScriptContext;

public interface IWSLCommand {
	public int getLineNumber();
	public void setInstant(boolean i);
	public boolean isInstant();
	public void execute(WSLScriptContext cx) throws InterruptedException;
	public WSLAbstractCommand getNext();
}
