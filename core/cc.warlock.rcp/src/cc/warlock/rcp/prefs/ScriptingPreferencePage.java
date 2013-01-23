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

import java.io.File;
import java.util.Collection;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyPage;

import cc.warlock.core.client.settings.internal.DirectorySetting;
import cc.warlock.core.script.configuration.ScriptConfiguration;

public class ScriptingPreferencePage extends PropertyPage implements
		IWorkbenchPropertyPage {

	//protected Text scriptPrefix;
	protected TreeViewer scriptDirectories;
	protected Button addScriptDir, removeScriptDir /*, moveUp, moveDown*/;
	
	// protected ArrayList<String> directories = new ArrayList<String>();
	
	public ScriptingPreferencePage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(3, false));
//		main.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		new Label(main, SWT.NONE);
		
		Group dirGroup = new Group(main, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.horizontalSpan = 3;
		dirGroup.setText("Script Directories");
		dirGroup.setLayout(new GridLayout(2, false));
		dirGroup.setLayoutData(data);
		
		scriptDirectories = new TreeViewer(dirGroup, SWT.BORDER | SWT.SINGLE | SWT.H_SCROLL);
		scriptDirectories.getTree().setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, true, true));
		scriptDirectories.setContentProvider(new ITreeContentProvider() {
			public void dispose() {	}
			public Object[] getChildren(Object parentElement) {
				return new Object[0];
			}
			public Object[] getElements(Object inputElement) {
				return ((Collection<DirectorySetting>)inputElement).toArray();
			}
			public Object getParent(Object element) {return null;}
			public boolean hasChildren(Object element) { return false; }
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
		});
		
		scriptDirectories.setLabelProvider(new ILabelProvider() {
			public void addListener(ILabelProviderListener listener) {}
			public void dispose() {}
			public Image getImage(Object element) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
			}
			public String getText(Object element) {
				return ((DirectorySetting)element).getDirectory().getAbsolutePath();
			}
			public boolean isLabelProperty(Object element, String property) { return true; }
			public void removeListener(ILabelProviderListener listener) {}
		});
		scriptDirectories.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				boolean empty = scriptDirectories.getSelection().isEmpty();
				
				removeScriptDir.setEnabled(!empty);
				/*moveUp.setEnabled(!empty);
				moveDown.setEnabled(!empty);*/
			}
		});
		scriptDirectories.getTree().setLinesVisible(false);
		scriptDirectories.getTree().setSize(300, scriptDirectories.getTree().getSize().y);
		
		Composite dirButtonsComposite = new Composite(dirGroup, SWT.NONE);
		dirButtonsComposite.setLayout(new FillLayout(SWT.VERTICAL));
		dirButtonsComposite.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
		
		addScriptDir = new Button(dirButtonsComposite, SWT.PUSH);
		addScriptDir.setText("Add Directory..");
//		addScriptDir.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, false));
		addScriptDir.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addDirectoryClicked();
			}
		});
		
		removeScriptDir = new Button(dirButtonsComposite, SWT.PUSH);
		removeScriptDir.setText("Remove Directory");
		removeScriptDir.setEnabled(false);
//		removeScriptDir.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, false));
		removeScriptDir.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeDirectoryClicked();
			}
		});
		
		/*
		moveUp = new Button(dirButtonsComposite, SWT.PUSH);
		moveUp.setText("Move Up");
		moveUp.setEnabled(false);
//		moveUp.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));
		moveUp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				moveUpClicked();
			}
		});
		
		
		moveDown = new Button(dirButtonsComposite, SWT.PUSH);
		moveDown.setText("Move Down");
		moveDown.setEnabled(false);
//		moveDown.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, false));
		moveDown.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				moveDownClicked();
			}
		});
		*/
		//scriptPrefix.setText(ScriptConfiguration.instance().getScriptPrefix());
		refreshView();
		
		return main;
	}
	
	protected void refreshView() {
		scriptDirectories.setInput(ScriptConfiguration.instance().getScriptDirectories());
	}
	
	protected void addDirectoryClicked ()
	{
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		String directory = dialog.open();
		
		if (directory != null)
		{
			File dir = new File(directory);
			DirectorySetting setting = ScriptConfiguration.instance().getScriptDirectoryConfiguration().createSetting();
			setting.setDirectory(dir.getAbsolutePath());
			refreshView();
		}
	}
	
	protected void removeDirectoryClicked ()
	{
		IStructuredSelection selection = (IStructuredSelection) scriptDirectories.getSelection();
		DirectorySetting directory = (DirectorySetting) selection.getFirstElement();
		
		ScriptConfiguration.instance().getScriptDirectoryConfiguration().removeSetting(directory);
		scriptDirectories.remove(directory);
	}
	
	/*
	protected void moveUpClicked ()
	{
		IStructuredSelection selection = (IStructuredSelection) scriptDirectories.getSelection();
		DirectorySetting directory = (DirectorySetting) selection.getFirstElement();
		
		int index = directories.indexOf(directory);
		if (index > 0)
		{
			int oldIndex = index;
			index--;
			
			directories.set(oldIndex, directories.get(index));
			directories.set(index, directory);
			
			scriptDirectories.setInput(directories);
			scriptDirectories.refresh();
		}
	}
	
	protected void moveDownClicked ()
	{
		IStructuredSelection selection = (IStructuredSelection) scriptDirectories.getSelection();
		String directory = (String) selection.getFirstElement();
		
		int index = directories.indexOf(directory);
		if (index < directories.size() - 1)
		{
			int oldIndex = index;
			index++;
			
			directories.set(oldIndex, directories.get(index));
			directories.set(index, directory);
			
			scriptDirectories.setInput(directories);
			scriptDirectories.refresh();
		}
	}
	*/
	
	@Override
	public boolean performOk() {
		//ScriptConfiguration.instance().setScriptPrefix(scriptPrefix.getText());

		/*
		ScriptDirectoryConfiguration dirConf = ScriptConfiguration.instance().getScriptDirectoryConfiguration();
		dirConf.clear();
		for(String dir : directories) {
			DirectorySetting setting = dirConf.createSetting();
			setting.setDirectory(dir);
		}
		*/
		return true;
	}
}
