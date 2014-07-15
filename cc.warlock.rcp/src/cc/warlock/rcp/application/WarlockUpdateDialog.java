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
package cc.warlock.rcp.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.Update;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import cc.warlock.rcp.ui.WarlockSharedImages;

public class WarlockUpdateDialog extends Dialog {

	private TableViewer updateTable;
	private HashMap<Update, Boolean> updates = new HashMap<Update, Boolean>();
	
	public WarlockUpdateDialog (Shell parent, List<Update> updates) {
		super(parent);
		
		for (Update update : updates) {
			this.updates.put(update, true);
		}
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText("Warlock Updates");
		newShell.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_UPDATES));
		newShell.setSize(400, 450);
		newShell.setActive();
	}
		
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite main = (Composite) super.createDialogArea(parent);
		
		Label heading = new Label(main, SWT.NONE);
		heading.setFont(JFaceResources.getBannerFont());
		heading.setText("Warlock Automatic Updates");
		
		Label description = new Label(main, SWT.WRAP);
		description.setText("Warlock has found new updates to download.\n\nSelect the updates below that you would like to install, or press Cancel to ignore this update.");
		description.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		updateTable = new TableViewer(main, SWT.CHECK | SWT.BORDER);
		
		TableColumn check = new TableColumn(updateTable.getTable(), SWT.NONE, 0);
		check.setText(" ");
		check.setWidth(20);
		
		TableColumn name = new TableColumn(updateTable.getTable(), SWT.NONE, 1);
		name.setText("Warlock Feature");
		name.setWidth(150);
		
		TableColumn currentVersion = new TableColumn(updateTable.getTable(), SWT.NONE, 2);
		currentVersion.setText("Current Version");
		currentVersion.setWidth(100);
		
		TableColumn newVersion = new TableColumn(updateTable.getTable(), SWT.NONE, 3);
		newVersion.setText("New Version");
		newVersion.setWidth(100);
		
		updateTable.getTable().setHeaderVisible(true);
		
		updateTable.setContentProvider(ArrayContentProvider.getInstance());
		updateTable.setLabelProvider(new ITableLabelProvider() {
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				if (columnIndex == 1) return WarlockSharedImages.getImage(WarlockSharedImages.IMG_FEATURE);
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				Update update = (Update)element;
				
				if (columnIndex == 1) { //name
					return update.replacement.getProperty(IInstallableUnit.PROP_NAME);
				} else if (columnIndex == 2) { //old version
					return update.toUpdate.getVersion().toString();
					
				} else if (columnIndex == 3) { //new version
					update.replacement.getVersion().toString();
				}
				return "";
			}

			@Override
			public void addListener(ILabelProviderListener listener) {}
			@Override
			public void dispose() {}
			@Override
			public boolean isLabelProperty(Object element, String property) {
				return true;
			}
			@Override
			public void removeListener(ILabelProviderListener listener) {}
			
		});
		updateTable.getTable().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					TableItem item = (TableItem) event.item;
					Update update = (Update)item.getData();
					
					updates.put(update, !updates.get(update));
					System.out.println(update + "=" + updates.get(update));
				}
			}
		});
		updateTable.setInput(updates.keySet());
		updateTable.getTable().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		for (TableItem item : updateTable.getTable().getItems())
		{
			item.setChecked(true);
		}
		
		Composite buttonsComposite = new Composite(main, SWT.NONE);
		buttonsComposite.setLayoutData(new GridData(GridData.END, GridData.FILL, true, true));
		buttonsComposite.setLayout(new GridLayout(2, false));
		
		Button selectAll = new Button(buttonsComposite, SWT.PUSH);
		selectAll.setText("Select All");
		selectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (TableItem item : updateTable.getTable().getItems())
				{
					item.setChecked(true);
				}
			}
		});
		
		return main;
	}
	
	public List<Update> getSelectedFeatures()
	{
		ArrayList<Update> selected = new ArrayList<Update>();
		
		for (Update update : updates.keySet()) {
			if (updates.get(update)) {
				selected.add(update);
			}
		}
		return selected;
	}
}
