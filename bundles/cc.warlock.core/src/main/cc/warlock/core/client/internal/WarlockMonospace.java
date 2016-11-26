package cc.warlock.core.client.internal;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;

public class WarlockMonospace implements IWarlockStyle {
	private static IWarlockStyle instance = new WarlockMonospace();
	
	private WarlockColor foregroundColor = new WarlockColor();
	private WarlockColor backgroundColor = new WarlockColor();
	
	public static IWarlockStyle getInstance() {
		return instance;
	}
	
	private WarlockMonospace() {
		
	}

	@Override
	public Runnable getAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WarlockColor getForegroundColor() {
		return foregroundColor;
	}

	@Override
	public WarlockColor getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public boolean isBold() {
		return false;
	}

	@Override
	public boolean isItalic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUnderline() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFullLine() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMonospace() {
		return true;
	}

	@Override
	public String getName() {
		return "monospace";
	}

	@Override
	public String getComponentName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSound() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAction(Runnable action) {
		
	}

	@Override
	public void setForegroundColor(WarlockColor color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBackgroundColor(WarlockColor color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFullLine(boolean fullLine) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBold(boolean bold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setItalic(boolean italic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUnderline(boolean underline) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMonospace(boolean monospace) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setComponentName(String componentName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSound(String sound) {
		// TODO Auto-generated method stub
		
	}
}
