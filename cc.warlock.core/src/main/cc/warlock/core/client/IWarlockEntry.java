package cc.warlock.core.client;

public interface IWarlockEntry {
	public String getText ();
	public void setCurrentCommand (String command);
	public ICommandHistory getCommandHistory();
	public void append(char c);
	public void submit();
	public void prevCommand();
	public void nextCommand();
	public void searchHistory();
	public void paste();
	public void repeatLastCommand();
	public void repeatSecondToLastCommand();
}
