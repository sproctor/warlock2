package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.ICommandHistory;
import cc.warlock.core.client.IWarlockEntry;

public class SWTWarlockEntry implements IWarlockEntry {

	private IWarlockEntry entry;
	
	public SWTWarlockEntry(IWarlockEntry entry) {
		this.entry = entry;
	}
	
	private class SetCurrentCommandWrapper implements Runnable {
		
		private String command;
		
		public SetCurrentCommandWrapper(String command) {
			this.command = command;
		}
		
		public void run () {
			entry.setCurrentCommand(command);
		}
	}
	
	private class AppendWrapper implements Runnable {
		
		private char c;
		
		public AppendWrapper(char c) {
			this.c = c;
		}
		
		public void run () {
			entry.append(c);
		}
	}
	
	protected void run(Runnable runnable) {
		Display.getDefault().asyncExec(new CatchingRunnable(runnable));
	}
	
	@Override
	public String getText() {
		return entry.getText();
	}

	@Override
	public ICommandHistory getCommandHistory() {
		return entry.getCommandHistory();
	}
	
	@Override
	public void setCurrentCommand(String command) {
		run(new SetCurrentCommandWrapper(command));
	}

	@Override
	public void append(char c) {
		run(new AppendWrapper(c));
	}

	@Override
	public void submit() {
		run(new Runnable() {
			public void run() {
				entry.submit();
			}
		});
	}

	@Override
	public void prevCommand() {
		run(new Runnable() {
			public void run() {
				entry.prevCommand();
			}
		});
	}

	@Override
	public void nextCommand() {
		run(new Runnable() {
			public void run() {
				entry.nextCommand();
			}
		});
	}

	@Override
	public void searchHistory() {
		run(new Runnable() {
			public void run() {
				entry.searchHistory();
			}
		});
	}

	@Override
	public void paste() {
		run(new Runnable() {
			public void run() {
				entry.paste();
			}
		});
	}

	@Override
	public void repeatLastCommand() {
		run(new Runnable() {
			public void run() {
				entry.repeatLastCommand();
			}
		});
	}

	@Override
	public void repeatSecondToLastCommand() {
		run(new Runnable() {
			public void run() {
				entry.repeatSecondToLastCommand();
			}
		});
	}

}
