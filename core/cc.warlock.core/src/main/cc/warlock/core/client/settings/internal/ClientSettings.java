/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.core.client.settings.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.logging.LoggingConfiguration;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.configuration.IWarlockSetting;
import cc.warlock.core.configuration.IWarlockSettingFactory;
import cc.warlock.core.configuration.WarlockPreferences;
import cc.warlock.core.configuration.WarlockSetting;


/**
 * This is the default Client Settings implementation, based on our XML Configuration backend.
 * This class includes a single default implementations for each {@link IConfigurationProvider}
 * @author marshall
 */
public class ClientSettings extends WarlockSetting implements IClientSettings
{
	private static HashMap<String, ClientSettings> clients = new HashMap<String, ClientSettings>();
	private static Preferences topNode = WarlockPreferences.getInstance().getNode().node("clients");
	private static HashMap<String, IWarlockSettingFactory> providerFactories = new HashMap<String, IWarlockSettingFactory>();
	
	private HashMap<String, IWarlockSetting> providers = new HashMap<String, IWarlockSetting>();
	
	private String name;
	private String clientId;
	
	static {
		
		try {
			for (String clientId : topNode.childrenNames()) {
				clients.put(clientId, new ClientSettings(clientId));
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		
		registerProviderFactory("highlights", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new HighlightConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("ignores", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new IgnoreConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("triggers", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new TriggerConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("variables", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new VariableConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("macros", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new MacroConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("windows", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new WindowConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("presets", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new PresetStyleConfigurationProvider(parentNode);
			}
		});
		
		registerProviderFactory("logs", new IWarlockSettingFactory() {
			public IWarlockSetting createSetting(Preferences parentNode) {
				return new LoggingConfiguration(parentNode);
			}
		});
	}
	
	private ClientSettings (String clientId) {
		super(topNode, clientId);
		
		this.clientId = clientId;
		
		name = getNode().get("name", null);
	}
	
	public IWarlockSetting getProvider (String providerId) {
		IWarlockSetting provider = providers.get(providerId);
		if (provider == null) {
			IWarlockSettingFactory factory = providerFactories.get(providerId);
			if (factory == null)
				return null;
			provider = factory.createSetting(getNode());
		}
		return provider;
	}
	
	public static void registerProviderFactory (String providerId, IWarlockSettingFactory factory) {
		providerFactories.put(providerId, factory);
	}
	
	public static ClientSettings getClientSettings (String clientId) {
		ClientSettings client = clients.get(clientId);
		if (client == null) {
			client = new ClientSettings (clientId);
			clients.put(clientId, client);
		}
		return client;
	}
	
	public static Collection<ClientSettings> getAllClientSettings () {
		return Collections.unmodifiableCollection(clients.values());
	}

	public String getName () {
		return name;
	}
	
	public void setName (String name) {
		getNode().put("name", name);
		
		this.name = name;
	}
	
	public String getCliendId () {
		return clientId;
	}
	
	public void flush() {
		try {
			getNode().flush();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
