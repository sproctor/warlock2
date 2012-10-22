package cc.warlock.rcp.stormfront.ui.menu;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.internal.ScriptRegistry;
import cc.warlock.rcp.menu.SubMenuContributionItem;

public class ScriptMenuContributionItem extends CompoundContributionItem {
	
	// A class to retrieve the running scripts from a client.
	protected class CharacterContributionItem extends SubMenuContributionItem {
	
		private IWarlockClientViewer viewer;
		
		public CharacterContributionItem(IWarlockClientViewer viewer) {
			super(viewer.getClient().getCharacterName());
			this.viewer = viewer;
		}
		
		@Override
		protected IContributionItem[] getContributionItems() {
			// Add Menu Items
			ArrayList<IContributionItem> items = new ArrayList<IContributionItem>();

			
			for(IScript script : ScriptRegistry.getRegistry(viewer).getRunningScripts()) 
			{
				IContributionItem newItem = new ScriptContributionItem(script);
				items.add(newItem);
			}
			
			return items.toArray(new IContributionItem[items.size()]); 
		}		
	}
	
	public class CharacterNameAction extends Action {
		
		public CharacterNameAction (String name) {
			super(name);
		}
	}
	
	@Override
	protected IContributionItem[] getContributionItems() {
		// Add Menu Items
		ArrayList<IContributionItem> items = new ArrayList<IContributionItem>();
		
		java.util.List<IWarlockClient> clients = WarlockClientRegistry.getActiveClients();
		for(IWarlockClient client : clients) {
			CharacterContributionItem newCharItem = new CharacterContributionItem(client.getViewer());
			items.add(newCharItem);
		}

		return items.toArray(new IContributionItem[items.size()]); 
	}

}
