package cc.warlock.rcp.menu;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import cc.warlock.rcp.views.DebugView;

public class DebugOpen extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			// Hide the view if we have one
			for (IViewReference view : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences())
			{
				if (DebugView.VIEW_ID.equals(view.getId())) {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(view);
					return null;
				}
			}
			// Otherwise show it
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DebugView.VIEW_ID, null, IWorkbenchPage.VIEW_VISIBLE);
		} catch(PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}
}
