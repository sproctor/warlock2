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
package cc.warlock.rcp.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import cc.warlock.core.client.IClientSettings;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.IWarlockFont;
import cc.warlock.core.client.settings.ClientSettings;
import cc.warlock.core.client.settings.WindowConfigurationProvider;
import cc.warlock.core.settings.IWindowSettings;
import cc.warlock.rcp.ui.client.WarlockClientAdaptable;
import cc.warlock.rcp.views.GameView;

public class RCPUtil {
	
	public static void openURL (String url)
	{
		try {
			PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(url));
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void openPerspective (String perspectiveId)
	{
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IPerspectiveDescriptor perspective =
			PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(perspectiveId);
		
		page.setPerspective(perspective);
	}
	
	public static int openPreferences (String pageId)
	{
		GameView inFocus = GameView.getGameViewInFocus();
		
		IWarlockClient client = inFocus == null ? null : inFocus.getClient();
		WarlockClientAdaptable clientAdapter = new WarlockClientAdaptable(client);

		PreferenceDialog dialog = PreferencesUtil.createPropertyDialogOn(Display.getDefault().getActiveShell(),
					clientAdapter, pageId, null, null);

		if(dialog == null)
			return 0;
		
		dialog.getShell().setText("Warlock Preferences");
		dialog.getTreeViewer().expandToLevel(2);

		return dialog.open();
	}
	
	public static void addTextContextMenu(final StyledText text, final IWarlockClientViewer viewer, final String name) {
		ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
		
		Menu contextMenu = new Menu(text);
		MenuItem itemFont = new MenuItem(contextMenu, SWT.PUSH);
		itemFont.setText("Change normal font");
		itemFont.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				IWarlockClient client = viewer.getClient();
				FontDialog fontDialog = new FontDialog(text.getShell());
				fontDialog.setText("Choose normal font");
				IClientSettings settings = null;
				if(client != null)
					settings = client.getClientSettings();
				if(settings == null)
					settings = ClientSettings.getGlobalClientSettings();
				WindowConfigurationProvider provider = WindowConfigurationProvider.getProvider(settings);
				fontDialog.setFontList(getFontList(text.getShell(), provider.getWindowFont(name)));
				FontData font = fontDialog.open();
				if(font == null)
					return;
				IWindowSettings wsetting = provider.getOrCreateWindowSettings(name);
				IWarlockFont fontSetting = wsetting.getFont();
				FontUtil.setWarlockFontFromFontData(fontSetting, font);
			}
		});
		MenuItem itemMonoFont = new MenuItem(contextMenu, SWT.PUSH);
		itemMonoFont.setText("Change monospace font");
		itemMonoFont.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				IWarlockClient client = viewer.getClient();
				FontDialog fontDialog = new FontDialog(text.getShell());
				fontDialog.setText("Choose normal font");
				
				IClientSettings settings = null;
				if(client != null)
					settings = client.getClientSettings();
				if(settings == null)
					settings = ClientSettings.getGlobalClientSettings();
				WindowConfigurationProvider provider = WindowConfigurationProvider.getProvider(settings);
				fontDialog.setFontList(getFontList(text.getShell(), provider.getWindowMonoFont(name)));
				FontData font = fontDialog.open();
				if(font == null)
					return;
				IWindowSettings wsetting = provider.getOrCreateWindowSettings(name);
				IWarlockFont fontSetting = wsetting.getColumnFont();
				FontUtil.setWarlockFontFromFontData(fontSetting, font);
			}
		});
		MenuItem itemBgColor = new MenuItem(contextMenu, SWT.PUSH);
		itemBgColor.setText("Change background color");
		itemBgColor.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				IWarlockClient client = viewer.getClient();
				ColorDialog colorDialog = new ColorDialog(text.getShell());
				colorDialog.setText("Choose font color");
				IClientSettings settings = null;
				if(client != null)
					settings = client.getClientSettings();
				if(settings == null)
					settings = ClientSettings.getGlobalClientSettings();
				WindowConfigurationProvider provider = WindowConfigurationProvider.getProvider(settings);
				colorDialog.setRGB(ColorUtil.warlockColorToRGB(provider.getWindowBackground(name)));
				RGB color = colorDialog.open();
				if(color == null)
					return;
				IWindowSettings wsetting = provider.getOrCreateWindowSettings(name);
				wsetting.setBackgroundColor(ColorUtil.rgbToWarlockColor(color));
			}
		});
		MenuItem itemFgColor = new MenuItem(contextMenu, SWT.PUSH);
		itemFgColor.setText("Change font color");
		itemFgColor.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				IWarlockClient client = viewer.getClient();
				ColorDialog colorDialog = new ColorDialog(text.getShell());
				colorDialog.setText("Choose font color");
				IClientSettings settings = null;
				if(client != null)
					settings = client.getClientSettings();
				if(settings == null)
					settings = ClientSettings.getGlobalClientSettings();
				WindowConfigurationProvider provider = WindowConfigurationProvider.getProvider(settings);
				colorDialog.setRGB(ColorUtil.warlockColorToRGB(provider.getWindowForeground(name)));
				RGB color = colorDialog.open();
				if(color == null)
					return;
				IWindowSettings wsetting = provider.getOrCreateWindowSettings(name);
				wsetting.setForegroundColor(ColorUtil.rgbToWarlockColor(color));
			}
		});
		MenuItem itemCopy = new MenuItem(contextMenu, SWT.PUSH);
		itemCopy.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				text.copy();
			}
		});
		itemCopy.setText("Copy");
		itemCopy.setImage(images.getImage(ISharedImages.IMG_TOOL_COPY));
		MenuItem itemClear = new MenuItem(contextMenu, SWT.PUSH);
		itemClear.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				text.setText("");
			}
		});
		itemClear.setText("Clear");
		itemClear.setImage(images.getImage(ISharedImages.IMG_TOOL_DELETE));
		text.setMenu(contextMenu);
	}
	
	static public FontData[] getFontList(Shell shell, IWarlockFont font) {
		
		if (font.isDefaultFont()){
			return JFaceResources.getDefaultFont().getFontData();
		}

		FontData datas[] = new FontData[0];
		
		if (font.getFamilyName() != null)
			datas = shell.getDisplay().getFontList(font.getFamilyName(), true);
		
		FontData data = new FontData();
		if (datas.length == 0) {
			return JFaceResources.getDefaultFont().getFontData();
		} else {
			data.setName(font.getFamilyName());
			data.setHeight(font.getSize());
			
			return new FontData[] { data };
		}
	}
}
