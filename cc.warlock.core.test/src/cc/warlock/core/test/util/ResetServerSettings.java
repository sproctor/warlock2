package cc.warlock.core.test.util;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientConnectListener;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.internal.WarlockClient;
import cc.warlock.core.settings.AccountProvider;
import cc.warlock.core.settings.ProfileSetting;
import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.core.stormfront.network.StormFrontConnection;
import cc.warlock.core.stormfront.tags.SettingsInfoTagHandler;
import cc.warlock.core.test.TestUtil;

public class ResetServerSettings {

	protected static void usage ()
	{
		System.err.println("Usage: ");
		System.err.println("   resetServerSettings <profileName>");
//		System.err.println(" OR ");
//		System.err.println("   resetServerSettings <accountName> <password> <characterName>");
	}
	
	public static void main (String args[])
	{
		if (args.length == 0)
		{
			usage();
			return;
		}
		
		ProfileSetting profile = null;
		
		if (args.length == 1)
		{
			profile = AccountProvider.getInstance().getProfileByCharacterName(args[0]);
		}
				
		if (profile != null)
		{
			Map<String,String> loginProperties = TestUtil.autoLogin(profile, null);
			WarlockClient client = new WarlockClient(loginProperties.get("GAMECODE"));
			int port = Integer.parseInt(loginProperties.get(SGEConnection.PROPERTY_GAMEPORT));			
			
			try {
				WarlockClientRegistry.addWarlockClientListener(new IWarlockClientConnectListener() {
					public void clientConnected(IWarlockClient client) {
						StormFrontConnection connection = (StormFrontConnection) client.getConnection();
						
						//SettingsInfoTagHandler handler = 
							//(SettingsInfoTagHandler) connection.getProtocolHandler().getTagHandler(SettingsInfoTagHandler.class);
						
						//handler.setNewSettings(true);
					}
					
					public void clientCreated(IWarlockClient client) {}
					public void clientDisconnected(IWarlockClient client) {}
					public void clientSettingsLoaded(IWarlockClient client) {}
				});
				
				client.connect(loginProperties.get(SGEConnection.PROPERTY_GAMEHOST), port, loginProperties.get(SGEConnection.PROPERTY_KEY));
				
				System.out.println(">> Sleeping for 10 seconds while listeners and the connection events happen (probably a better way to poll for this) <<");
				try {
					Thread.sleep((long)1000*10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				Assert.fail(e.getMessage());
			}
		}
		else {
			System.err.println("Couldn't find profile: " + args[0]);
			return;
		}
		
		System.exit(0);
	}
}
