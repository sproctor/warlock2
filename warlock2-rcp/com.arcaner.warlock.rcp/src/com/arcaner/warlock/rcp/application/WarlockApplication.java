/*
 * Created on Dec 30, 2004
 */
package com.arcaner.warlock.rcp.application;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.arcaner.warlock.rcp.actions.WizardAction;
import com.arcaner.warlock.rcp.ui.WarlockSharedImages;
import com.arcaner.warlock.rcp.wizards.ConnectWizard;

/**
 * @author Marshall
 */
public class WarlockApplication extends WorkbenchAdvisor implements IPlatformRunnable {
	
	public String getInitialWindowPerspectiveId ()
	{
		return WarlockPerspectiveFactory.WARLOCK_PERSPECTIVE_ID;
	}
	
	public void preWindowOpen(IWorkbenchWindowConfigurer configurer)
	{
		configurer.setShowPerspectiveBar(false);
		configurer.setShowProgressIndicator(false);
		configurer.setShowFastViewBars(false);
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
	}
	
	public void fillActionBars(IWorkbenchWindow window, IActionBarConfigurer configurer, int flags)
	{
		if ((flags & FILL_MENU_BAR) == 0)
			return;
		
		IMenuManager mainMenu = configurer.getMenuManager();
		MenuManager connectionMenu = new MenuManager ("&Connection");
		mainMenu.add(connectionMenu);
		
		connectionMenu.add(new WizardAction("Connect...", ConnectWizard.class, WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_CONNECT)));
	}
	
    public Object run(Object args) throws Exception
    {
		Display display = PlatformUI.createDisplay();
		int ret = PlatformUI.createAndRunWorkbench(display, this);
		if (ret == PlatformUI.RETURN_RESTART)
			return EXIT_RESTART;
		
		return EXIT_OK;
	}
}
