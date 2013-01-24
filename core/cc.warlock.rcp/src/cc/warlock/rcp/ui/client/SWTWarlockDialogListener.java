package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IWarlockDialogListener;

public class SWTWarlockDialogListener implements IWarlockDialogListener {
	private IWarlockDialogListener listener;
	
	public SWTWarlockDialogListener(IWarlockDialogListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void dialogChanged() {
		Display.getDefault().asyncExec(new CatchingRunnable(new Runnable() {
			public void run() {
				listener.dialogChanged();
			}
		}));
	}

}
