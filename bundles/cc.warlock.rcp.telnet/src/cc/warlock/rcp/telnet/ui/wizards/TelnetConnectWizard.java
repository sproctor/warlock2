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
package cc.warlock.rcp.telnet.ui.wizards;

import java.util.HashMap;
import java.util.Map;

import cc.warlock.rcp.telnet.util.LoginUtil;
import cc.warlock.rcp.wizards.WizardWithNotification;

/**
 * @author kassah
 *
 */
public class TelnetConnectWizard extends WizardWithNotification {
	private ConnectWizardPage page1;
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		System.out.println("Hostname: " + page1.host() + " Port: " + page1.port());
		LoginUtil.connectAndOpenGameView (page1.host(), page1.port(), page1.host());
		return true;
	}
	
	public boolean canFinish() {
		return page1.isPageComplete();
	}
	
	public void addPages() {
		page1 = new ConnectWizardPage();
		
		addPage(page1);
	}

}
