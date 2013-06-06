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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.Update;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import cc.warlock.rcp.plugin.Warlock2Plugin;

public class WarlockUpdates {

	public static final String UPDATE_SITE = "warlock.updates.url";
	public static final String AUTO_UPDATE = "warlock.updates.autoupdate";
	
	protected static Properties updateProperties;
	
	static {
		updateProperties = new Properties();
		try {
			InputStream stream = FileLocator.openStream(Warlock2Plugin.getDefault().getBundle(), new Path("warlock-updates.properties"), false);
			updateProperties.load(stream);
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<Update> promptUpgrade (List<Update> updates) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		WarlockUpdateDialog dialog = new WarlockUpdateDialog(shell, updates);
		int response = dialog.open();
		
		if (response == Window.OK)
		{
			return dialog.getSelectedFeatures();
		}
		
		return Collections.emptyList();
	}
	
	public static boolean autoUpdate () {
		boolean autoUpdate = false;
		
		if (updateProperties.containsKey(AUTO_UPDATE)) {
			autoUpdate = Boolean.parseBoolean(updateProperties.getProperty(AUTO_UPDATE));
		}
		return autoUpdate;
	}
	
	public static void checkForUpdates (final Shell parent) {
		
		String repository_loc = updateProperties.getProperty(UPDATE_SITE);
		
		BundleContext context = FrameworkUtil.getBundle(WarlockUpdates.class).getBundleContext();
		ServiceReference<?> reference = context.getServiceReference(IProvisioningAgent.SERVICE_NAME);
		if(reference == null)
			return;
		IProvisioningAgent agent = (IProvisioningAgent) context.getService(reference);
		ProvisioningSession session = new ProvisioningSession(agent);
		UpdateOperation operation = new UpdateOperation(session);
		
		// Create uri
        URI uri = null;
        try {
          uri = new URI(repository_loc);
        } catch (URISyntaxException e) {
              System.out.println( "URI invalid: " + e.getMessage());
        }
        
        operation.getProvisioningContext().setArtifactRepositories(new URI[] { uri });
        operation.getProvisioningContext().setMetadataRepositories(new URI[] { uri });
        
		//Update[] updates = operation.getPossibleUpdates();
		IProgressMonitor monitor = new NullProgressMonitor();
		IStatus status = operation.resolveModal(monitor);
		
		// Failed to find updates (inform user and exit)
        if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
        	MessageDialog.openWarning(parent, "No update",
        			"No updates for the current installation have been found");
          return /*Status.CANCEL_STATUS*/;
        }
        
        // found updates, ask user if to install?
		if(status.isOK() && status.getSeverity() != IStatus.ERROR) {
			ProvisioningJob job = operation.getProvisioningJob(monitor);
			if(job == null) {
				MessageDialog.openInformation(parent, "Resultion", operation.getResolutionDetails()
						+ "\nLikely cause is from running inside Eclipse.");
			} else {
				job.schedule();
			}
		} else {
			MessageDialog.openError(parent, "Error updating", operation.getResolutionDetails());
		}
		context.ungetService(reference);
	}
}
