package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.settings.IWarlockSetting;
import cc.warlock.core.settings.IWarlockSettingListener;

public class SWTWarlockSettingListener implements IWarlockSettingListener {
	private IWarlockSettingListener listener;
	
	public SWTWarlockSettingListener(IWarlockSettingListener listener) {
		this.listener = listener;
	}
	
	private class SettingChangedWrapper implements Runnable {
		private IWarlockSetting setting;
		public SettingChangedWrapper(IWarlockSetting setting) {
			this.setting = setting;
		}
		public void run() {
			listener.settingChanged(setting);
		}
	}
	
	@Override
	public void settingChanged(IWarlockSetting setting) {
		Display.getDefault().asyncExec(new CatchingRunnable(new SettingChangedWrapper(setting)));
	}

}
