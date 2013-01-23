/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.rcp.prefs;

import java.util.ArrayList;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.part.PageBook;

import cc.warlock.core.client.CommandMacroHandler;
import cc.warlock.core.client.IMacro;
import cc.warlock.core.client.IMacroCommand;
import cc.warlock.core.client.IMacroHandler;
import cc.warlock.core.client.internal.DefaultMacro;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.client.settings.internal.MacroConfigurationProvider;
import cc.warlock.core.client.settings.internal.MacroSetting;
import cc.warlock.rcp.ui.ContentAssistCellEditor;
import cc.warlock.rcp.ui.KeyStrokeCellEditor;
import cc.warlock.rcp.ui.KeyStrokeText;
import cc.warlock.rcp.ui.KeyStrokeText.KeyStrokeLockListener;
import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.ui.macros.DefaultMacros;
import cc.warlock.rcp.ui.macros.MacroRegistry;

/**
 *
 * @author Marshall Culpepper
 */
public class MacrosPreferencePage extends PreferencePageUtils implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.prefs.macros";
	
	protected static String COLUMN_COMMAND = "command";
	protected static String COLUMN_KEY = "key";
	
	protected TableViewer macroTableView;
	//protected ClientSettings settings;
	
	//protected ArrayList<MacroSetting> macros = new ArrayList<MacroSetting>();

	protected Button addMacroButton;
	protected Button removeMacroButton;
	protected Button clearMacrosButton;
	protected Button defaultMacrosButton;
	
	protected MacroSetting selectedMacro;
	protected PageBook filterBook;

	protected Text commandText;
	protected KeyStrokeText keyComboText;
	protected Menu filterMenu;
	protected boolean filterByCommand = true;
	
	@Override
	protected Control createContents(Composite parent) {
		this.noDefaultAndApplyButton();
		
		createProfileDropDown(parent);
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(2, false));
		
		Composite filterComposite = new Composite(main, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = layout.marginHeight = 0;
		filterComposite.setLayout(layout);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
//		data.horizontalSpan = 2;
		filterComposite.setLayoutData(data);
		
		new Label(filterComposite, SWT.NONE).setText("Filter: ");
	
		filterBook = new PageBook(filterComposite, SWT.NONE);
		filterBook.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		commandText = new Text(filterBook, SWT.BORDER);
		commandText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				macroTableView.refresh();
			}
		});
		
		keyComboText = new KeyStrokeText(filterBook, SWT.BORDER);
		keyComboText.addKeyStrokeLockListener(new KeyStrokeLockListener() {
			public void keyStrokeLocked() {
				macroTableView.refresh();
			}
			public void keyStrokeUnlocked() {
				macroTableView.refresh();
			}
		});
		
		filterBook.showPage(commandText);

		Button filterButton = new Button(filterComposite, SWT.ARROW | SWT.DOWN);
		filterMenu = new Menu(filterButton);
		filterButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				filterMenu.setVisible(true);
			}
		});
		
		MenuItem filterByCommand = new MenuItem(filterMenu, SWT.RADIO);
		filterByCommand.setText("Filter by command");
		filterByCommand.setSelection(true);
		filterByCommand.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MacrosPreferencePage.this.filterByCommand = true;
				filterBook.showPage(commandText);
				macroTableView.refresh();
			}
		});
		
		MenuItem filterByKeyCombo = new MenuItem(filterMenu, SWT.RADIO);
		filterByKeyCombo.setText("Filter by key combo");
		filterByKeyCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MacrosPreferencePage.this.filterByCommand = false;
				filterBook.showPage(keyComboText.getText());
				macroTableView.refresh();
			}
		});
		filterButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.FILL, false, false));
		
		new Label(main, SWT.NONE);
		macroTableView = new TableViewer(main, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		TableViewerColumn commandColumn = new TableViewerColumn(macroTableView, SWT.NONE, 0);
		commandColumn.getColumn().setText("Command");
		commandColumn.getColumn().setWidth(225);
		commandColumn.setEditingSupport(new CommandEditingSupport(macroTableView));
		
		TableViewerColumn keyColumn = new TableViewerColumn(macroTableView, SWT.NONE, 1);
		keyColumn.getColumn().setText("Key Combination");
		keyColumn.getColumn().setWidth(125);
		keyColumn.setEditingSupport(new KeyStrokeEditingSupport(macroTableView));
		
		macroTableView.setUseHashlookup(true);
		macroTableView.setColumnProperties(new String[] {COLUMN_COMMAND, COLUMN_KEY});
		macroTableView.setContentProvider(ArrayContentProvider.getInstance());
		macroTableView.setLabelProvider(new LabelProvider());
		macroTableView.addFilter(new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				IMacro macro = (IMacro) element;
				IMacroHandler handler = macro.getHandler();
				
				if (handler != null && handler instanceof CommandMacroHandler)
					return true;
				else
					return false;
			}	
		});
		macroTableView.addFilter(new MacroFilter());
		
		macroTableView.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (macroTableView.getSelection().isEmpty()) {
					removeMacroButton.setEnabled(false);
				} else {
					selectedMacro = (MacroSetting) ((IStructuredSelection)macroTableView.getSelection()).getFirstElement();
					removeMacroButton.setEnabled(true);
				}
			}
		});
		
		//macroTableView.setInput(settings.getMacros());
		macroTableView.getTable().setHeaderVisible(true);
		int listHeight = macroTableView.getTable().getItemHeight() * 8;
		Rectangle trim = macroTableView.getTable().computeTrim(0, 0, 0, listHeight);
		data = new GridData(GridData.FILL, GridData.FILL, true, true);
		data.heightHint = trim.height;
		
		macroTableView.getTable().setLayoutData(data);
		
		Composite macroButtons = new Composite(main, SWT.NONE);
		macroButtons.setLayout(new GridLayout(1, true));
		macroButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		addMacroButton = new Button(macroButtons, SWT.PUSH);
		addMacroButton.setText("Add Macro");
		addMacroButton.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_ADD));
		addMacroButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addMacroSelected();
			}
		});
		addMacroButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		removeMacroButton = new Button(macroButtons, SWT.PUSH);
		removeMacroButton.setText("Remove Macro");
		removeMacroButton.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_REMOVE));
		removeMacroButton.setEnabled(false);
		removeMacroButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeMacroSelected();
			}
		});
		removeMacroButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Label filler = new Label(macroButtons, SWT.NONE);
		filler.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));	
		
		clearMacrosButton = new Button(macroButtons, SWT.PUSH);
		clearMacrosButton.setText("Clear Macros");
		clearMacrosButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				clearMacros();
			}
		});
		clearMacrosButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		defaultMacrosButton = new Button(macroButtons, SWT.PUSH);
		defaultMacrosButton.setText("Reset to Defaults");
		defaultMacrosButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setupDefaultMacros();
			}
		});
		defaultMacrosButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		if (settings == null)
			settings = getDefaultSettings();
		
		setData(settings);
		
		return main;
	}
	
	protected void setData (IClientSettings settings) {
		this.settings = settings;
		
		macroTableView.setInput(MacroConfigurationProvider.getMacros(settings));
		macroTableView.refresh();
	}

	protected class MacroFilter extends ViewerFilter {
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			MacroSetting macro = (MacroSetting)element;
			
			if (filterByCommand) {
				String command = macro.getCommand();
				
				if (command.equals("")) {
					return true;
				}
				
				return command.toLowerCase().contains(commandText.getText().toLowerCase());
			} else {
				KeyStroke stroke = keyComboText.getKeyStroke();
				if (stroke != null && stroke.getNaturalKey() != KeyStroke.NO_KEY) {
					int modifiers = MacroRegistry.instance().getModifiers(macro.getKeyString());
					int keycode = MacroRegistry.instance().getKeycode(macro.getKeyString());
					return (stroke.getModifierKeys() == modifiers && stroke.getNaturalKey() == keycode);
				}
				return true;
			}
		}
	}
	
	private ArrayList<MacroSetting> addedMacros = new ArrayList<MacroSetting>();
	
	private void addMacroSelected ()
	{
		MacroSetting macro = MacroConfigurationProvider.getProvider(settings).createSetting();
		macro.setCommand("");
		macro.setKeyString("");
		
		addedMacros.add(macro);
		
		macroTableView.refresh();
		
		macroTableView.editElement(macro, 0);
	}
	
	private ArrayList<MacroSetting> removedMacros = new ArrayList<MacroSetting>();

	protected void removeMacroSelected ()
	{
		if (!addedMacros.remove(selectedMacro))
			removedMacros.add(selectedMacro);
		
		MacroConfigurationProvider.getProvider(settings).removeSetting(selectedMacro);
		macroTableView.setInput(MacroConfigurationProvider.getMacros(settings));
		//macroTableView.remove(selectedMacro);
		macroTableView.refresh();
	}
	
	protected void setupDefaultMacros() {
		// There probably is a better place to put this.
		clearMacros();
		for(DefaultMacro macro : DefaultMacros.instance().getCollection()) {
			MacroSetting smacro = MacroConfigurationProvider.getProvider(settings).createSetting();
			smacro.setCommand(macro.getCommand());
			smacro.setKeyString(macro.getKeyString());
			
			addedMacros.add(smacro);
			//macros.add(macro);
			macroTableView.add(smacro);
			macroTableView.refresh();
		}
	}
	
	protected void clearMacros() {
		// Clear out all exiting macros.
		/*Table macroTable = macroTableView.getTable();
		if(macroTable == null)
			macroTable.clearAll();*/
		
		// Save the macros to be able to restore them
		for (MacroSetting macro : MacroConfigurationProvider.getMacros(settings)) {
			if (!addedMacros.contains(macro))
				removedMacros.add(macro);
		}
		
		// We removed all of the added macros
		addedMacros.clear();
		
		MacroConfigurationProvider.getProvider(settings).clear();
	}
	
	protected class LabelProvider implements ITableLabelProvider
	{
		public void addListener(ILabelProviderListener listener) {}
		public void dispose() {}
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		public String getColumnText(Object element, int columnIndex) {
			MacroSetting macro = (MacroSetting) element;
			if (columnIndex == 0) {
				return macro.getCommand();
			} else {
				int keycode = MacroRegistry.instance().getKeycode(macro.getKeyString());
				int modifiers = MacroRegistry.instance().getModifiers(macro.getKeyString());
				return SWTKeySupport.getKeyFormatterForPlatform().format(KeyStroke.getInstance(modifiers, keycode));
			}
		}
		public boolean isLabelProperty(Object element, String property) {
			return true;
		}
		public void removeListener(ILabelProviderListener listener) {}
	}
	
	protected class CommandEditingSupport extends EditingSupport implements IContentProposalProvider
	{
		protected ContentAssistCellEditor editor;
		
		public CommandEditingSupport (TableViewer viewer) {
			super(viewer);
			
			editor = new ContentAssistCellEditor(macroTableView.getTable(), SWT.SINGLE, new char[] { '{', '$', '\\' }, this);
		}
		
		protected boolean canEdit(Object element) {
			return true;
		}
		
		protected CellEditor getCellEditor(Object element) { 
			return editor;
		}
		
		protected Object getValue(Object element) {
			return ((MacroSetting)element).getCommand();
		}
		
		protected void setValue(Object element, Object value) {
			((MacroSetting)element).setCommand((String)value);
			
			macroTableView.update(element, null);
		}
		
		protected class MacroCommandContentProposal implements IContentProposal
		{
			protected IMacroCommand command;
			protected String contents;
			protected int position;
		
			public MacroCommandContentProposal (IMacroCommand command, String contents, int position)
			{
				this.command = command;
				this.contents = contents;
				this.position = position;
			}
			
			public String getContent() {
				String content = "{" + this.command.getIdentifier() + "}";
				
				int leftBracketIndex = contents.substring(0, position).lastIndexOf('{');
				String text = contents.substring(0, leftBracketIndex) + content + contents.substring(position);
				
				return text;
			}
			
			public int getCursorPosition() {
				return getContent().length();
			}
			
			public String getDescription() {
				return command.getDescription();
			}
			
			public String getLabel() {
				return "{"+command.getIdentifier()+"}";
			}
		}
		
		public IContentProposal[] getProposals(String contents, int position) {
			ArrayList<IContentProposal> proposals = new ArrayList<IContentProposal>();
			
			int lastRightBracket = contents.substring(0, position).lastIndexOf('{');
			
			if (lastRightBracket >= 0) {
				String commandSubstr = contents.substring(lastRightBracket+1, position).toLowerCase();
				
				for (IMacroCommand command : MacroRegistry.instance().getMacroCommands()) {
					if (command.getIdentifier().toLowerCase().startsWith(commandSubstr)) {
						proposals.add(new MacroCommandContentProposal(
							command, contents.substring(0, contents.length()-1), position-1));
					}
				}
			}
			
			return proposals.toArray(new IContentProposal[proposals.size()]);
		}
	}
	
	protected class KeyStrokeEditingSupport extends EditingSupport
	{
		protected KeyStrokeCellEditor editor;
		
		public KeyStrokeEditingSupport (TableViewer viewer)
		{
			super(viewer);
			
			this.editor = new KeyStrokeCellEditor(macroTableView.getTable(), SWT.SINGLE);
		}
		
		protected boolean canEdit(Object element) {
			return true;
		}

		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		protected Object getValue(Object element) {
			MacroSetting macro = (MacroSetting) element;
			int modifiers = MacroRegistry.instance().getModifiers(macro.getKeyString());
			int keycode = MacroRegistry.instance().getKeycode(macro.getKeyString());
			return KeyStroke.getInstance(modifiers, keycode);
		}

		protected void setValue(Object element, Object value) {
			MacroSetting macro = (MacroSetting) element;
			KeyStroke stroke = (KeyStroke) value;
			
			macro.setKeyString(MacroRegistry.instance().getKeyString(stroke.getNaturalKey(), stroke.getModifierKeys()));
			
			macroTableView.update(macro, null);
		}	
	}
	
	@Override
	public boolean performCancel() {
		// Re-add the removed macros
		for (MacroSetting oldMacro : removedMacros) {
			MacroSetting macro = MacroConfigurationProvider.getProvider(settings).createSetting();
			macro.setCommand(oldMacro.getCommand());
			macro.setKeyString(oldMacro.getKeyString());
		}
		removedMacros.clear();
		
		// Remove the added macros
		for (MacroSetting newMacro : addedMacros) {
			MacroConfigurationProvider.getProvider(settings).removeSetting(newMacro);
		}
		addedMacros.clear();
		
		return true;
	}
	
	@Override
	public boolean performOk() {
		// Changes have already been made, clear out the history
		addedMacros.clear();
		removedMacros.clear();
		
		settings.flush();
		
		return true;
	}
}
