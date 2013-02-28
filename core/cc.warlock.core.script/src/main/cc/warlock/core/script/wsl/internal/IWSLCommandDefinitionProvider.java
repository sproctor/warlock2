package cc.warlock.core.script.wsl.internal;

import java.util.Map;

public interface IWSLCommandDefinitionProvider {

	public Map<String, IWSLCommandDefinition> getCommands();
}
