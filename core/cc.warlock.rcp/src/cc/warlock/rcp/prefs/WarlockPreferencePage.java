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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;

import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.script.configuration.ScriptConfiguration;
import cc.warlock.rcp.configuration.GameViewConfiguration;

public class WarlockPreferencePage extends PreferencePageUtils implements IWorkbenchPropertyPage {
	protected Button promptButton, suppressScriptExceptionsButton;
	
	//private ClientSettings settings;
	
	protected Control createContents(Composite parent) {
		this.noDefaultAndApplyButton();
		createProfileDropDown(parent);
		
		Composite main = new Composite (parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		
		promptButton = new Button(main, SWT.CHECK);
		promptButton.setText("Supress prompts");
		
		
		suppressScriptExceptionsButton = new Button(main, SWT.CHECK);
		suppressScriptExceptionsButton.setText("Suppress Script Exceptions");
		
		if (settings == null)
			settings = getDefaultSettings();
		
		setData(settings);
		
		return main;
	}
	
	protected void setData (IClientSettings settings) {
		this.settings = settings;
		promptButton.setSelection(GameViewConfiguration.getProvider(settings).getSuppressPrompt());
		suppressScriptExceptionsButton.setSelection(ScriptConfiguration.instance().getSupressExceptions().get());
	}
	
	@Override
	public boolean performOk() {
		GameViewConfiguration.getProvider(settings).setSuppressPrompt(promptButton.getSelection());
		ScriptConfiguration.instance().getSupressExceptions().set(suppressScriptExceptionsButton.getSelection());
		return true;
	}
}
