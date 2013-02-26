package cc.warlock.core.stormfront.script.wsl.internal;

import java.util.Map;

public interface IWSLCommandDefinitionProvider {

	public Map<String, IWSLCommandDefinition> getCommands();
}
