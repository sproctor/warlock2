package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewerListener;

public class SWTWarlockClientViewerListener implements
		IWarlockClientViewerListener {

	private IWarlockClientViewerListener listener;
	
	public SWTWarlockClientViewerListener(IWarlockClientViewerListener listener) {
		this.listener = listener;
	}
	
	private class ClientChangedWrapper implements Runnable {
		private IWarlockClient client;
		
		public ClientChangedWrapper(IWarlockClient client) {
			this.client = client;
		}
		
		public void run () {
			listener.clientChanged(client);
		}
	}
	
	protected void run(Runnable runnable) {
		Display.getDefault().asyncExec(new CatchingRunnable(runnable));
	}
	
	@Override
	public void clientChanged(IWarlockClient client) {
		run(new ClientChangedWrapper(client));
	}

}
