package cc.warlock.rcp.stormfront.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.WarlockEntry;
import cc.warlock.rcp.ui.client.SWTPropertyListener;

public class StormFrontEntry extends WarlockEntry {
	private IWarlockClient client;
	private int rt;
	private int ct;
	
	private SWTPropertyListener<Integer> rtListener = new SWTPropertyListener<Integer>(new IPropertyListener<Integer>() {
		public void propertyChanged(Integer value) {
			rt = value;
			widget.redraw();
		}
	});
	private SWTPropertyListener<Integer> ctListener = new SWTPropertyListener<Integer>(new IPropertyListener<Integer>() {
		public void propertyChanged(Integer value) {
			ct = value;
			widget.redraw();
		}
	});
	
	public StormFrontEntry (Composite parent, IWarlockClientViewer viewer) {
		super(parent, viewer);
		
		widget.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle clientArea = widget.getClientArea();
				
				// Draw RT blocks
				e.gc.setBackground(widget.getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
				int height = clientArea.height - 4;
				if (ct > 0)
					height = (clientArea.height - 4) / 2 - 1;
				for (int i = 0; i < rt; i++) {
					e.gc.fillRectangle(2 + i * 20, 2, 16, height);
				}
				
				// Draw CT blocks
				e.gc.setBackground(widget.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
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
				Point pos = widget.getLocationAtOffset(0);
				e.gc.drawText(widget.getText(), pos.x, pos.y, true);
			}
		});
	}
	
	public void setClient(IWarlockClient client) {
		if (this.client != null) {
			this.client.getTimer("roundtime").getProperty().removeListener(rtListener);
			this.client.getTimer("casttime").getProperty().removeListener(ctListener);
		}
		this.client = client;
		if (client != null) {
			client.getTimer("roundtime").getProperty().addListener(rtListener);
			client.getTimer("casttime").getProperty().addListener(ctListener);
		}
	}
}
