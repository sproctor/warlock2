package cc.warlock.core.stormfront.settings;

import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.settings.ClientSettings;
import cc.warlock.core.settings.ArrayConfigurationProvider;
import cc.warlock.core.settings.IWarlockSetting;
import cc.warlock.core.settings.IWarlockSettingFactory;

public class CmdlistSettings extends ArrayConfigurationProvider<CliSetting> {
	public static final String ID = "cmdlist";
	
	private String timestamp;
	
	static {
		ClientSettings.registerProviderFactory("cmdlist", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(IWarlockSetting parent) {
				return new CmdlistSettings(parent);
			}
		});
	}
	
	// TODO: Make this a per game or account setting rather than per profile
	public CmdlistSettings(IWarlockSetting parent) {
		super(parent, ID);
		timestamp = getNode().get("timestamp", "1");
	}

	@Override
	protected CliSetting loadSetting(String id) {
		return new CliSetting(this, id);
	}

	public CliSetting getCli(String coord) {
		for(CliSetting cli : this.getSettings()) {
			if(coord.equals(cli.getCoord()))
				return cli;
		}
		// we might not want to do this for all cases.
		CliSetting setting = createSetting();
		setting.setCood(coord);
		return setting;
	}
	
	public void setTimestamp(String timestamp) {
		getNode().put("timestamp", timestamp);
		
		this.timestamp = timestamp;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public static CmdlistSettings getProvider(IClientSettings clientSettings) {
		return (CmdlistSettings)clientSettings.getProvider(ID);
	}
}
