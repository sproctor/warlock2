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
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.osgi.service.prefs.BackingStoreException;

import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.settings.HighlightConfigurationProvider;
import cc.warlock.core.client.settings.WindowConfigurationProvider;
import cc.warlock.core.settings.IWindowSettings;
import cc.warlock.core.settings.PatternSetting;
import cc.warlock.core.settings.WarlockPreferencesScope;
import cc.warlock.rcp.configuration.GameViewConfiguration;
import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.util.ColorUtil;


public class HighlightStringsPreferencePage extends PreferencePageUtils implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.prefs.highlightStrings";
	
	protected TableViewer stringTable;
	protected Button fillLineButton, regexButton, fullWordMatchButton, caseSensitiveButton;
	protected ColorSelector customBGSelector, customFGSelector;
	protected Button defaultBG, customBG, defaultFG, customFG;
	protected Button addString, removeString, soundButton;
	protected Text filterText;
	protected Text soundText; 
	protected PatternSetting selectedString;
	protected ArrayList<PatternSetting> addedStrings = new ArrayList<PatternSetting>();
	protected ArrayList<PatternSetting> removedStrings = new ArrayList<PatternSetting>();
	
	@Override
	protected Control createContents(Composite parent) {
		createProfileDropDown(parent);
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		
		createStringsTable(main);
		createOptions(main);
		
		return main;
	}
	
	private void createStringsTable (Composite main)
	{
		Group stringsGroup = new Group(main, SWT.NONE);
		stringsGroup.setLayout(new GridLayout(2, false));
		stringsGroup.setText(getDisplayName());
		stringsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		Composite filterComposite = new Composite(stringsGroup, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = layout.marginHeight = 0;
		filterComposite.setLayout(layout);
		filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		new Label(filterComposite, SWT.NONE).setText("Filter: ");
		filterText = new Text(filterComposite, SWT.BORDER);
		filterText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				stringTable.refresh();
			}
		});
		filterText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		new Label(stringsGroup, SWT.NONE);
		
		try {
			WarlockPreferencesScope.getInstance().getNode().sync();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
		
		stringTable = new TableViewer(stringsGroup, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL );
		TableColumn column = new TableColumn(stringTable.getTable(), SWT.NONE, 0);
		column.setWidth(400);
		
		stringTable.setUseHashlookup(true);
		stringTable.setColumnProperties(new String[] { "highlightString" });
		
		CellEditor editors[] = new CellEditor[] { 
				new TextCellEditor(stringTable.getTable())
		};
		
		stringTable.setCellEditors(editors);
		stringTable.setCellModifier(new ICellModifier () {
			public boolean canModify(Object element, String property) {
				return true;
			}

			public Object getValue(Object element, String property) {
				return ((PatternSetting)element).getText();
			}

			public void modify(Object element, String property, Object value) {
				TableItem item = (TableItem)element;
				PatternSetting string = (PatternSetting)item.getData();
				String pattern = ((String)value).trim();
				
				try {
					string.setText(pattern);
				} catch(PatternSyntaxException e) {
					e.printStackTrace();
				}
				stringTable.refresh(string);
			}
		});
		
		stringTable.addFilter(new ViewerFilter () {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				
				PatternSetting string = (PatternSetting) element;
				String str = string.getText();
				
				if (str.equals("")) return true;
				
				return (str.toLowerCase().contains(filterText.getText().toLowerCase()));
			}
		});
		
		stringTable.setLabelProvider(new StringsLabelProvider());
		stringTable.setContentProvider(ArrayContentProvider.getInstance());
		
		int listHeight = stringTable.getTable().getItemHeight() * 8;
		Rectangle trim = stringTable.getTable().computeTrim(0, 0, 0, listHeight);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
		data.heightHint = trim.height;
		stringTable.getTable().setLayoutData(data);

		stringTable.addSelectionChangedListener(new ISelectionChangedListener () {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				PatternSetting string = (PatternSetting) selection.getFirstElement();
				
				if (string != selectedString)
				{
					stringTable.cancelEditing();
				}
				
				highlightStringSelected(string);
			}
		});
		
		Composite buttonsComposite = new Composite(stringsGroup, SWT.NONE);
		buttonsComposite.setLayout(new GridLayout(1, true));
		buttonsComposite.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, true, true));
		
		addString = createButton(buttonsComposite, "Add", SWT.LEFT | SWT.PUSH);
		addString.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_ADD));
		addString.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		removeString = createButton(buttonsComposite, "Remove", SWT.LEFT | SWT.PUSH);
		removeString.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_REMOVE));
		removeString.setEnabled(false);
		
		if (settings == null)
			settings = getDefaultSettings();
		
		setData (settings);
	}
	
	protected void setData (IClientSettings settings) {
		this.settings = settings;
		
		stringTable.setInput(HighlightConfigurationProvider.getHighlights(settings));
		stringTable.refresh();
	}

	private void createOptions (Composite main)
	{
		Group optionsGroup = new Group(main, SWT.NONE);
		optionsGroup.setLayout(new GridLayout(1, false));
		optionsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		optionsGroup.setText("Options");
		Composite mainGroup = new Composite(optionsGroup, SWT.NONE);
		
		mainGroup.setLayout(new GridLayout(2, false));
		mainGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		new Label(mainGroup, SWT.NONE).setText("Foreground Color: ");
		Composite fgColorComposite = new Composite(mainGroup, SWT.NONE);
		fgColorComposite.setLayout(new GridLayout(3, false));
		fgColorComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		defaultFG = createRadio(fgColorComposite, "Default");
		defaultFG.setEnabled(false);
		customFG = createRadio(fgColorComposite);
		customFG.setEnabled(false);
		customFGSelector = createColorSelector(fgColorComposite);
		customFGSelector.setEnabled(false);
		customFGSelector.setColorValue(new RGB(0, 0, 0));
		
		new Label(mainGroup, SWT.NONE).setText("Background Color: ");
		Composite bgColorComposite = new Composite(mainGroup, SWT.NONE);
		bgColorComposite.setLayout(new GridLayout(3, false));
		bgColorComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		defaultBG = createRadio(bgColorComposite, "Default");
		defaultBG.setEnabled(false);
		customBG = createRadio(bgColorComposite);
		customBG.setEnabled(false);
		customBGSelector = createColorSelector(bgColorComposite);
		customBGSelector.setEnabled(false);
		customBGSelector.setColorValue(new RGB(0, 0, 0));
		
		regexButton = createCheckbox(mainGroup, "Regular expression");
		regexButton.setEnabled(false);
		fillLineButton = createCheckbox(mainGroup, "Highlight Entire Line");
		fillLineButton.setEnabled(false);
		caseSensitiveButton = createCheckbox(mainGroup, "Case Sensitive");
		caseSensitiveButton.setEnabled(false);
		fullWordMatchButton = createCheckbox(mainGroup, "Match full word (word boundary)");
		fullWordMatchButton.setEnabled(false);
		
		Group soundGroup = new Group(optionsGroup, SWT.NONE);
		soundGroup.setLayout(new GridLayout(3, false));
		soundGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		new Label(soundGroup, SWT.NONE).setText("Sound:");
		soundText = new Text(soundGroup, SWT.BORDER | SWT.SINGLE);
		soundText.setEnabled(false);
		GridData soundTextData = new GridData(GridData.FILL_HORIZONTAL);
		soundText.setLayoutData(soundTextData);
		soundText.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				selectedString.getStyle().setSound(soundText.getText());
			}
		});
		
		soundButton = createButton(soundGroup, "Browse", SWT.PUSH);
		
		soundButton.setEnabled(false);
		addBtnSoundListener(main);
	}
	
	private void addBtnSoundListener(final Composite parent){
		soundButton.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e){
				browseForSound();
			}
			
			public void widgetDefaultSelected(SelectionEvent e){}
		});
	}
	
	protected String getDisplayName ()
	{
		return "Highlight Strings";
	}
	
	private void highlightStringSelected (PatternSetting string)
	{
		if (string == null) {
			// No string selected, disable all fields
			removeString.setEnabled(false);
			defaultFG.setEnabled(false);
			defaultFG.setSelection(true);
			customFG.setEnabled(false);
			customFG.setSelection(true);
			customFGSelector.setEnabled(false);
			customFGSelector.setColorValue(new RGB(0, 0, 0));
			defaultBG.setEnabled(false);
			customBG.setEnabled(false);
			customBG.setSelection(true);
			customBGSelector.setEnabled(false);
			customBGSelector.setColorValue(new RGB(0, 0, 0));
			regexButton.setEnabled(false);
			regexButton.setSelection(false);
			fillLineButton.setEnabled(false);
			fillLineButton.setSelection(false);
			caseSensitiveButton.setEnabled(false);
			caseSensitiveButton.setSelection(false);
			fullWordMatchButton.setEnabled(false);
			fullWordMatchButton.setSelection(false);
			soundText.setEnabled(false);
			soundText.setText("");
			soundButton.setEnabled(false);
			return;
		}
		selectedString = string;
		
		removeString.setEnabled(true);
		
		WarlockColor fgColor = string.getStyle().getForegroundColor();
		WarlockColor bgColor = string.getStyle().getBackgroundColor();

		boolean fgIsDefault = fgColor.isDefault();
		boolean bgIsDefault = bgColor.isDefault();
		
		defaultFG.setSelection(fgIsDefault);
		defaultFG.setEnabled(true);
		customFG.setSelection(!fgIsDefault);
		customFG.setEnabled(true);
		customFGSelector.setEnabled(!fgIsDefault);
		
		defaultBG.setSelection(bgIsDefault);
		defaultBG.setEnabled(true);
		customBG.setSelection(!bgIsDefault);
		customBG.setEnabled(true);
		customBGSelector.setEnabled(!bgIsDefault);
		
		IWindowSettings mainWindow = WindowConfigurationProvider.getProvider(settings).getMainWindowSettings();
		if (fgIsDefault) {
			fgColor = mainWindow.getForegroundColor();
			fgIsDefault = fgColor.isDefault();
		}
		if(fgIsDefault) {
			customFGSelector.setColorValue(ColorUtil.warlockColorToRGB(GameViewConfiguration.getProvider(settings).getDefaultForeground()));
		} else {
			customFGSelector.setColorValue(ColorUtil.warlockColorToRGB(fgColor));
		}
		
		if (bgIsDefault) {
			bgColor = mainWindow.getBackgroundColor();
			bgIsDefault = bgColor.isDefault();
		}
		if (bgIsDefault) {
			customBGSelector.setColorValue(ColorUtil.warlockColorToRGB(GameViewConfiguration.getProvider(settings).getDefaultBackground()));
		} else {
			customBGSelector.setColorValue(ColorUtil.warlockColorToRGB(bgColor));
		}
		
		regexButton.setSelection(!string.isLiteral());
		regexButton.setEnabled(true);
		
		fillLineButton.setSelection(string.getStyle().isFullLine());
		fillLineButton.setEnabled(true);
		
		caseSensitiveButton.setSelection(string.isCaseSensitive());
		caseSensitiveButton.setEnabled(true);
		
		fullWordMatchButton.setSelection(string.isFullWordMatch());
		fullWordMatchButton.setEnabled(true);
		
		try{
			if (string.getStyle().getSound() != null){
				soundText.setText(string.getStyle().getSound());
			}else{
				soundText.setText("");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		soundText.setEnabled(true);
		soundButton.setEnabled(true);

	}
	
	@Override
	protected void buttonPressed(Button button) {
		if (button == removeString) {
			removeStringSelected();
		} else if (button == addString) {
			addStringSelected();
		} else if (button == defaultBG) {
			defaultBackgroundSelected();
		} else if (button == defaultFG) {
			defaultForegroundSelected();
		} else if (button == customBG) {
			customBGSelector.setEnabled(true);
		} else if (button == customFG) {
			customFGSelector.setEnabled(true);
		} else if (button == fillLineButton) {
			fillLineSelected();
		} else if (button == fullWordMatchButton) {
			fullWordMatchSelected();
		} else if (button == caseSensitiveButton) {
			caseSensitiveSelected();
		} else if (button == regexButton) {
			regexSelected();
		}
	}
	
	protected void browseForSound(){
		FileDialog fd = new FileDialog(HighlightStringsPreferencePage.this.getShell(), SWT.OPEN);
		fd.setFilterNames(new String[]{"Wave File (*.wav)"});
		fd.setFilterExtensions(new String[]{"*.wav"});
		String filename = fd.open();
		if (filename != null){
			soundText.setText(filename);
			try {
				if (selectedString != null && selectedString.getStyle() != null) {
					selectedString.getStyle().setSound(filename);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	@Override
	protected void colorSelectorChanged(ColorSelector selector) {
		if (selector == customBGSelector) {
			customBackgroundChanged();
		} else if (selector == customFGSelector) {
			customForegroundChanged();
		}
	}
	
	private void defaultForegroundSelected ()
	{
		selectedString.getStyle().setForegroundColor(WindowConfigurationProvider.getProvider(settings).getMainWindowSettings().getForegroundColor());
		customFGSelector.setEnabled(false);
		WarlockColor color = selectedString.getStyle().getForegroundColor();
		if(color.isDefault())
			color = WindowConfigurationProvider.getProvider(settings).getDefaultForegroundColor();
		customFGSelector.setColorValue(ColorUtil.warlockColorToRGB(color));
		stringTable.update(selectedString, null);
		setValid(true);
	}
	
	private void customForegroundChanged ()
	{
		RGB selectedColor = customFGSelector.getColorValue();
		WarlockColor newColor = ColorUtil.rgbToWarlockColor(selectedColor);
		selectedString.getStyle().setForegroundColor(newColor);
		stringTable.update(selectedString, null);
		setValid(true);
	}
	
	private void defaultBackgroundSelected ()
	{
		// TODO this seems a little off
		selectedString.getStyle().setBackgroundColor(WindowConfigurationProvider.getProvider(settings).getMainWindowSettings().getBackgroundColor());
		customBGSelector.setEnabled(false);
		WarlockColor color = selectedString.getStyle().getBackgroundColor();
		if(color.isDefault())
			color = WindowConfigurationProvider.getProvider(settings).getDefaultBackgroundColor();
		customBGSelector.setColorValue(ColorUtil.warlockColorToRGB(color));
		stringTable.update(selectedString, null);	
		setValid(true);
	}
	
	private void customBackgroundChanged ()
	{
		selectedString.getStyle().setBackgroundColor(ColorUtil.rgbToWarlockColor(customBGSelector.getColorValue()));
		stringTable.update(selectedString, null);
		setValid(true);
	}
	
	private void removeStringSelected() {
		// Grab selected string.
		PatternSetting string = selectedString;
		
		// Select Next (or Previous if last) Highlight in line
		int index = stringTable.getTable().getSelectionIndex();
		if (stringTable.getElementAt(index + 1) != null) {
			stringTable.getTable().setSelection(index + 1);
			highlightStringSelected((PatternSetting) stringTable.getElementAt(index + 1));
		} else if (stringTable.getElementAt(index - 1) != null) {
			stringTable.getTable().setSelection(index - 1);
			highlightStringSelected((PatternSetting) stringTable.getElementAt(index - 1));
		}
		
		// Mark string removed in our changelog to commit to prefs
		// If it was added since the last commit, just remove it from the changelog
		if (!addedStrings.remove(string))
			removedStrings.add(string);
		
		// Remove string from our display list and notify the table
		//highlightStrings.remove(string);
		stringTable.remove(string);
	}

	private void addStringSelected() {
		PatternSetting newString = (PatternSetting)HighlightConfigurationProvider.getProvider(settings).createSetting();
		newString.setText("<Highlight Text>");
		
		addedStrings.add(newString);
		
		selectedString = newString;
		
		stringTable.add(newString);
		stringTable.editElement(newString, 0);
	}
	
	private void fillLineSelected () {
		selectedString.getStyle().setFullLine(fillLineButton.getSelection());
	}
	
	private void fullWordMatchSelected () {
		selectedString.setFullWordMatch(fullWordMatchButton.getSelection());
	}
	
	private void caseSensitiveSelected () {
		selectedString.setCaseSensitive(caseSensitiveButton.getSelection());
	}
	
	private void regexSelected () {
		selectedString.setLiteral(!regexButton.getSelection());
	}
	
	protected class StringsLabelProvider implements ITableLabelProvider, ITableColorProvider
	{
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			return ((PatternSetting)element).getText();
		}

		public void addListener(ILabelProviderListener listener) {	}

		public void dispose() {	}

		public boolean isLabelProperty(Object element, String property) {
			return true;
		}

		public void removeListener(ILabelProviderListener listener) {}

		public Color getBackground(Object element, int columnIndex) {
			PatternSetting string = (PatternSetting)element;
			Color c = new Color(HighlightStringsPreferencePage.this.getShell().getDisplay(),
					ColorUtil.warlockColorToRGB(string.getStyle().getBackgroundColor().isDefault() ?
							WindowConfigurationProvider.getProvider(settings).getDefaultBackgroundColor() : string.getStyle().getBackgroundColor()));
			
			return c;
		}

		public Color getForeground(Object element, int columnIndex) {
			PatternSetting string = (PatternSetting)element;
			Color c = new Color(HighlightStringsPreferencePage.this.getShell().getDisplay(), 
					ColorUtil.warlockColorToRGB(string.getStyle().getForegroundColor().isDefault() ?
							WindowConfigurationProvider.getProvider(settings).getDefaultForegroundColor() : string.getStyle().getForegroundColor()));
			
			return c;
		}
	}
	
	@Override
	protected void performDefaults() {
		//copyHighlightStrings();
		stringTable.refresh();
	}
	
	@Override
	public boolean performOk() {
		//HighlightConfigurationProvider highlightConfig = client.getClientSettings().getHighlightConfigurationProvider();
		//if(highlightConfig == null)
		//	return false;
		
		/* FIXME: we're updating before hitting ok
		 for (HighlightString string : highlightStrings) {
			WarlockStyle style = (WarlockStyle) string.getStyle();
			
			if (addedStrings.remove(string)) {
				highlightConfig.addHighlightString(string);
			} else if (string.needsUpdate() || (style != null && style.needsUpdate())) {
				highlightConfig.replaceHighlightString(string.getOriginalHighlightString(), string);
			}
		}*/
		
		for (PatternSetting string : removedStrings)
		{
			HighlightConfigurationProvider.getProvider(settings).removeSetting(string);
		}
		
		settings.flush();
		return true;
	}
}
