package cc.warlock.rcp.stormfront.ui.views;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.settings.WarlockPreferencesScope;
import cc.warlock.rcp.ui.WarlockEntry;
import cc.warlock.rcp.ui.client.SWTPropertyListener;
import cc.warlock.rcp.util.ColorUtil;

public class StormFrontEntry extends WarlockEntry {
	private IWarlockClient client;
	private int rt;
	private int ct;
	
	private SWTPropertyListener<Integer> rtListener;
	private SWTPropertyListener<Integer> ctListener;
	
	private Color rtColor;
	private Color ctColor;
	
	public StormFrontEntry (Composite parent, IWarlockClientViewer viewer) {
		super(parent, viewer);
		
		getWidget().addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if(rt == 0 && ct == 0)
					return;
				Rectangle clientArea = getWidget().getClientArea();
				
				if(rtColor == null)
					rtColor = new Color(e.gc.getDevice(), 139, 0, 0);
				// Draw RT blocks
				e.gc.setBackground(rtColor);
				int height = clientArea.height - 4;
				if (ct > 0)
					height = (clientArea.height - 4) / 2 - 1;
				for (int i = 0; i < rt; i++) {
					e.gc.fillRectangle(2 + i * 20, 2, 16, height);
				}
				
				if(ctColor == null)
					ctColor = new Color(e.gc.getDevice(), 0, 0, 139);
				// Draw CT blocks
				e.gc.setBackground(ctColor);
				height = clientArea.height - 4;
				int y = 2;
				if (rt > 0) {
					height = (clientArea.height - 4) / 2 - 1;
					y = height + 3; // one pixel over the middle
				}
				for (int i = 0; i < ct; i++) {
					e.gc.fillRectangle(2 + i * 20, y, 16, height);
				}
				
				// Redraw the text
				Point pos = getWidget().getLocationAtOffset(0);
				e.gc.drawText(getWidget().getText(), pos.x, pos.y, true);
			}
		});
	}
	
	@Override
	public void setClient(IWarlockClient client) {
		if (this.client != null) {
			this.client.getTimer("roundtime").getProperty().removeListener(rtListener);
			this.client.getTimer("casttime").getProperty().removeListener(ctListener);
		}
		this.client = client;
		if (client != null) {
			if(rtListener == null)
				rtListener = new SWTPropertyListener<Integer>(new IPropertyListener<Integer>() {
					public void propertyChanged(Integer value) {
						rt = value;
						getWidget().redraw();
					}
				});
			if(ctListener == null)
				ctListener = new SWTPropertyListener<Integer>(new IPropertyListener<Integer>() {
					public void propertyChanged(Integer value) {
						ct = value;
						getWidget().redraw();
					}
				});
			client.getTimer("roundtime").getProperty().addListener(rtListener);
			client.getTimer("casttime").getProperty().addListener(ctListener);
		}
	}
	
	@Override
	public void loadSettings(final IClientSettings settings) {
		if(rtColor != null)
			rtColor.dispose();
		rtColor = ColorUtil.warlockColorToColor(settings.getRtColor());
		if(ctColor != null)
			ctColor.dispose();
		ctColor = ColorUtil.warlockColorToColor(settings.getCtColor());
		WarlockPreferencesScope.addPreferenceChangeListener(settings.getNode(), new IPreferenceChangeListener() {
			@Override
			public void preferenceChange(PreferenceChangeEvent event) {
				if (event.getKey().equals("rt-color")) {
				if(rtColor != null)
					rtColor.dispose();
				rtColor = ColorUtil.warlockColorToColor(settings.getRtColor());
				}
				if (event.getKey().equals("ct-color")) {
				if(ctColor != null)
					ctColor.dispose();
				ctColor = ColorUtil.warlockColorToColor(settings.getCtColor());
				}
			}
		});
		super.loadSettings(settings);
	}
}