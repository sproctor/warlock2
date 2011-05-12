package cc.warlock.core.client;

public interface IWarlockFont {
	public String getFamilyName();
	public int getSize();
	
	public void setFamilyName(String familyName);
	public void setSize(int size);
	
	public boolean isDefaultFont();
}
