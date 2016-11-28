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
package cc.warlock.rcp.ui.script;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.configuration.ScriptConfiguration;
import cc.warlock.core.settings.DirectorySetting;
import cc.warlock.rcp.ui.ComboField;
import cc.warlock.rcp.ui.TextField;
import cc.warlock.rcp.ui.WarlockSharedImages;

public class NewScriptWizardPage extends WizardPage {

	private TextField scriptNameText;
	private ComboField scriptExtCombo;
	private ComboField scriptDirCombo;
	
	public NewScriptWizardPage ()
	{
		super("Create a new script", "Create a new script",
			WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_NEWFILE_WIZBAN));
		
		setDescription("Create a Warlock script in one of your script directories.");
	}
	
	public void createControl(Composite parent) {

		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		main.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		new Label(main, SWT.NONE).setText("Choose a script directory to save this script to:");
		
		scriptDirCombo = new ComboField(main, SWT.BORDER | SWT.DROP_DOWN);
		boolean selected = false;
		for (DirectorySetting dir : ScriptConfiguration.instance().getScriptDirectories())
		{
			scriptDirCombo.getCombo().add(dir.getDirectory().getAbsolutePath());

			if (!selected) {
				scriptDirCombo.getCombo().select(0);
				selected = true;
			}
		}
		scriptDirCombo.getCombo().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label scriptName = new Label(main, SWT.NONE);
		scriptName.setText("Script name:");
		GridData data = new GridData();
		data.verticalIndent = 10;
		scriptName.setLayoutData(data);
		
		Composite scriptNameComposite = new Composite(main, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		scriptNameComposite.setLayout(layout);
		scriptNameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		scriptNameText = new TextField(scriptNameComposite, SWT.BORDER);
		scriptExtCombo = new ComboField(scriptNameComposite, SWT.DROP_DOWN);
		selected = false;
		for (IScriptEngine engine : ScriptEngineRegistry.getScriptEngines())
		{
			for (String extension : engine.getSupportedExtensions())
			{
				scriptExtCombo.getCombo().add("."+extension);
				if (!selected) {
					scriptExtCombo.getCombo().select(0);
					selected = true;
				}
			}
		}
		
		scriptNameText.getTextControl().setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		scriptExtCombo.getCombo().setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT, false, false));
				
		setControl(main);
	}

	public String getScriptDir() {
		return scriptDirCombo.getText();
	}

	public String getScriptName() {
		return scriptNameText.getText();
	}

	public String getScriptExt() {
		return scriptExtCombo.getText();
	}
}
