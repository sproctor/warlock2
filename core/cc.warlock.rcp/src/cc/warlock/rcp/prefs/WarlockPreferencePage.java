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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbenchPropertyPage;

import cc.warlock.core.script.configuration.ScriptConfiguration;
import cc.warlock.rcp.configuration.GameViewConfiguration;

public class WarlockPreferencePage extends PreferencePageUtils implements IWorkbenchPropertyPage {
	private Button promptButton, suppressButton;
	private Spinner minCommandWidget;
	
	protected Control createContents(Composite parent) {
		//this.noDefaultAndApplyButton();
		createProfileDropDown(parent);
		
		Composite main = new Composite (parent, SWT.NONE);
		main.setLayout(new GridLayout(2, false));
		
		promptButton = new Button(main, SWT.CHECK);
		promptButton.setText("Supress prompts");
		promptButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		
		suppressButton = new Button(main, SWT.CHECK);
		suppressButton.setText("Suppress Script Exceptions");
		suppressButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		
		Label label = new Label(main, SWT.HORIZONTAL);
		label.setText("Minimum command size to record");
		minCommandWidget = new Spinner(main, SWT.NONE);
		minCommandWidget.setIncrement(1);
		minCommandWidget.setMinimum(0);
		minCommandWidget.setMaximum(100);
		
		updateData();
		
		return main;
	}
	
	protected void updateData () {
		promptButton.setSelection(GameViewConfiguration.getProvider(getSettings()).getSuppressPrompt());
		suppressButton.setSelection(ScriptConfiguration.instance().getSupressExceptions().get());
		minCommandWidget.setSelection(getSettings().getMinCommandSize());
	}
	
	@Override
	public boolean performOk() {
		GameViewConfiguration.getProvider(getSettings()).setSuppressPrompt(promptButton.getSelection());
		ScriptConfiguration.instance().getSupressExceptions().set(suppressButton.getSelection());
		getSettings().setMinCommandSize(minCommandWidget.getSelection());
		return true;
	}
}
