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
/*
 * Created on Mar 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.ui.macros;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.swt.SWT;

import cc.warlock.core.client.IMacro;
import cc.warlock.core.client.IMacroCommand;
import cc.warlock.core.client.IMacroVariable;
import cc.warlock.core.client.internal.DefaultMacro;
import cc.warlock.core.client.internal.WarlockMacro;
import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.ui.macros.internal.EscapeMacroHandler;
import cc.warlock.rcp.ui.macros.internal.ReturnMacroHandler;


/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MacroRegistry {
	private static MacroRegistry instance = new MacroRegistry();
	
	private ArrayList<IMacro> macros = new ArrayList<IMacro>();
	
	// TODO - determine if variables and commands should be synchronized
	private HashMap<String, IMacroVariable> variables = new HashMap<String, IMacroVariable>();
	private HashMap<String, IMacroCommand> commands = new HashMap<String, IMacroCommand>();
	private ArrayList<DefaultMacro> defaultMacros = new ArrayList<DefaultMacro>();
	
	MacroRegistry () {
		loadMacros();
		loadVariables();
		loadCommands();
		loadDefaultMacros();
	}
	
	public static MacroRegistry instance ()
	{	
		return instance;
	}
	
	public Collection<IMacro> getMacros ()
	{
		return macros;
	}
	
	public Collection<IMacroVariable> getMacroVariables ()
	{
		return variables.values();
	}
	
	public Collection<IMacroCommand> getMacroCommands() {
		return commands.values();
	}
	
	public void addMacro (IMacro macro)
	{
		macros.add(macro);
	}
	
	public void removeMacro(IMacro macro) {
		macros.remove(macro);
	}
	
	public void replaceMacro(IMacro originalMacro, IMacro newMacro) {
		int index = macros.indexOf(originalMacro);
		if (index > -1) {
			macros.set(index, newMacro);
		}
	}
	
	private void loadMacros () {
		macros.add(new WarlockMacro(SWT.CR, new ReturnMacroHandler()));
		macros.add(new WarlockMacro(SWT.ESC, new EscapeMacroHandler()));
	}
	
	private void loadDefaultMacros() {
		createDefaultMacro("\\xxml toggle containers\\r",99,65536);
		createDefaultMacro("\\xxml toggle dialogs\\r",100,65536);
		createDefaultMacro("{ExportDialog}",101,327680);
		createDefaultMacro("{HighlightsDialog}",104,327680);
		createDefaultMacro("{ImportDialog}",105,327680);
		createDefaultMacro("{ToggleLinks}",108,65536);
		createDefaultMacro("{ToggleMusic}",109,65536);
		createDefaultMacro("{ToggleImages}",105,65536);
		createDefaultMacro("{ToggleSounds}",115,65536);
		createDefaultMacro("{MacrosDialog}",109,327680);
		createDefaultMacro("{ChooseSkin}",115,327680);
		createDefaultMacro("{VariablesDialog}",118,65536);
		createDefaultMacro("{MacroSet}0",48,65536);
		createDefaultMacro("{MacroSet}1",49,65536);
		createDefaultMacro("{MacroSet}2",50,65536);
		createDefaultMacro("{MacroSet}3",51,65536);
		createDefaultMacro("{MacroSet}4",52,65536);
		createDefaultMacro("{MacroSet}5",53,65536);
		createDefaultMacro("{MacroSet}6",54,65536);
		createDefaultMacro("{MacroSet}7",55,65536);
		createDefaultMacro("{MacroSet}8",56,65536);
		createDefaultMacro("{MacroSet}9",57,65536);
		createDefaultMacro("{Restart}",16777230,262144);
		createDefaultMacro("\\xretreat\\r",114,262144);
		createDefaultMacro("\\xlook\\r",16777259,0);
		createDefaultMacro("\\xhealth\\r",16777263,0);
		createDefaultMacro("\\xnotoriety\\r",16777258,0);
		createDefaultMacro("\\xmana\\r",16777261,0);
		createDefaultMacro("\\xup\\r",16777262,0);
		createDefaultMacro("\\xdown\\r",16777264,0);
		createDefaultMacro("\\xsw\\r",16777265,0);
		createDefaultMacro("\\xs\\r",16777266,0);
		createDefaultMacro("\\xse\\r",16777267,0);
		createDefaultMacro("\\xw\\r",16777268,0);
		createDefaultMacro("\\xout\\r",16777269,0);
		createDefaultMacro("\\xe\\r",16777270,0);
		createDefaultMacro("\\xnw\\r",16777271,0);
		createDefaultMacro("\\xn\\r",16777272,0);
		createDefaultMacro("\\xne\\r",16777273,0);
		createDefaultMacro("{PageUp}",16777221,0);
		createDefaultMacro("{PageDown}",16777222,0);
		createDefaultMacro("{LineUp}",16777221,131072);
		createDefaultMacro("{LineDown}",16777222,131072);
		createDefaultMacro("{HistoryPrev}",16777217,0);
		createDefaultMacro("{HistoryNext}",16777218,0);
		createDefaultMacro("{RepeatLast}",13,262144);
		createDefaultMacro("{RepeatSecondToLast}",13,65536);
		createDefaultMacro("{ReturnOrRepeatLast}",16777296,0);
		createDefaultMacro("{RepeatLast}",16777296,262144);
		createDefaultMacro("{RepeatSecondToLast}",16777296,65536);
		createDefaultMacro("{CycleWindows}",9,0);
		createDefaultMacro("{BufferTop}",16777221,262144);
		createDefaultMacro("{BufferBottom}",16777222,262144);
		createDefaultMacro("{BufferTop}",16777223,262144);
		createDefaultMacro("{BufferBottom}",16777224,262144);
		createDefaultMacro("{CycleWindowsReverse}",9,131072);
		createDefaultMacro("\\xassess\\r",97,262144);
		createDefaultMacro("{copy}",99,262144);
		createDefaultMacro("{cut}",120,262144);
		createDefaultMacro("{paste}",118,262144);
		createDefaultMacro("{PauseScript}",27,131072);
		createDefaultMacro("peer down\\r",16777264,262144);
		createDefaultMacro("\\xpath focus damage\\r",16777265,131072);
		createDefaultMacro("\\xaft to port\\r",16777265,65536);
		createDefaultMacro("peer sw\\r",16777265,262144);
		createDefaultMacro("\\xpath focus quick\\r",16777266,131072);
		createDefaultMacro("\\xaft\\r",16777266,65536);
		createDefaultMacro("peer s\\r",16777266,262144);
		createDefaultMacro("\\xpath focus ease\\r",16777267,131072);
		createDefaultMacro("\\xaft to starboard\\r",16777267,65536);
		createDefaultMacro("peer se\\r",16777267,262144);
		createDefaultMacro("\\xport\\r",16777268,65536);
		createDefaultMacro("peer w\\r",16777268,262144);
		createDefaultMacro("peer out\\r",16777269,262144);
		createDefaultMacro("\\xstarboard\\r",16777270,65536);
		createDefaultMacro("peer e\\r",16777270,262144);
		createDefaultMacro("\\xforward to port\\r",16777271,65536);
		createDefaultMacro("peer nw\\r",16777271,262144);
		createDefaultMacro("\\xforward\\r",16777272,65536);
		createDefaultMacro("peer n\\r",16777272,262144);
		createDefaultMacro("\\xforward to starboard\\r",16777273,65536);
		createDefaultMacro("peer ne\\r",16777273,262144);
		createDefaultMacro("peer up\\r",16777262,262144);
		createDefaultMacro("\\xpath sense\\r",16777263,131072);
		createDefaultMacro("\\xpath check\\r",16777258,131072);
		createDefaultMacro("\\xdemeanor neutral\\r",101,65536);
		createDefaultMacro("\\xdemeanor reserved\\r",119,65536);
		createDefaultMacro("\\xdemeanor cold\\r",113,65536);
		createDefaultMacro("\\xget \\?\\rput \\? in my %container\\r",116,262144);
		createDefaultMacro("\\xremove my %helmet\\rput my %helmet in my %container\\r\\premove my %gloves\\rput my %gloves in my %container\\r",16777235,0);
		createDefaultMacro("\\xopen my \\?\\ropen my %gpouch\\r\\pfill my %gpouch with my \\?\\r\\pclose my %gpouch\\rclose my \\?\\r",16777228,0);
		createDefaultMacro("\\xopen my %gpouch\\rappr my %gpouch\\r\\p\\p\\p\\p\\p\\p\\p\\p\\p\\xclose my %gpouch\\r",16777230,0);
		createDefaultMacro("\\xopen my \\?\\rl in my \\?\\r\\pclose my \\?\\r",16777231,0);
		createDefaultMacro("\\xget my %helmet\\rwear my %helmet\\r\\pget my %gloves\\rwear my %gloves\\r",16777234,0);
		createDefaultMacro("\\xdemeanor friendly\\r",114,65536);
		createDefaultMacro("\\xdemeanor warm\\r",116,65536);
		createDefaultMacro("\\xpath focus power\\r",16777268,131072);
		createDefaultMacro("\\xremove my %shield\\rstow my %shield\\r",16777235,262144);
		createDefaultMacro("\\xget my %shield\\rwear my %shield\\r",16777234,262144);
	}
	
	private DefaultMacro createDefaultMacro(String command, int keycode, int modifier) {
		DefaultMacro macro = new DefaultMacro(command, keycode, modifier);
		defaultMacros.add(macro);
		return macro;
	}
	
	public Collection<DefaultMacro> getDefaultMacros() {
		return defaultMacros;
	}
	
	public IMacro getMacro(int keycode, int modifiers) {
		for (IMacro macro : macros)
		{
			if (macro.getKeyCode() == keycode && macro.getModifiers() == modifiers) {
				return macro;
			}
		}
		return null;
	}
	
	public void setMacroVariable(String id, IMacroVariable variable) {
		variables.put(id, variable);
	}
	
	public IMacroVariable getMacroVariable(String id) {
		return variables.get(id);
	}
	
	public void removeMacroVariable(IMacroVariable variable) {
		variables.remove(variable.getIdentifier());
	}
	
	public void addMacroCommand(IMacroCommand command) {
		commands.put(command.getIdentifier(), command);
	}
	
	public IMacroCommand getMacroCommand(String id) {
		return commands.get(id);
	}
	
	public void removeMacroCommand(IMacroCommand command) {
		commands.remove(command.getIdentifier());
	}
	
	private void loadCommands () {
		try {
			IExtension[] extensions = Warlock2Plugin.getDefault().getExtensions("cc.warlock.rcp.macroCommands");
			for (int i = 0; i < extensions.length; i++) {
				IExtension ext = extensions[i];
				IConfigurationElement[] ce = ext.getConfigurationElements();
				
				for (int j = 0; j < ce.length; j++) {
					Object obj = ce[j].createExecutableExtension("classname");
					
					if (obj instanceof IMacroCommand)
					{
						IMacroCommand command = (IMacroCommand) obj;
						commands.put(command.getIdentifier(), command);
					}
				}
			}
		} catch (InvalidRegistryObjectException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private void loadVariables () {
		try {
			IExtension[] extensions = Warlock2Plugin.getDefault().getExtensions("cc.warlock.rcp.macroVariables");
			for (int i = 0; i < extensions.length; i++) {
				IExtension ext = extensions[i];
				IConfigurationElement[] ce = ext.getConfigurationElements();
				
				for (int j = 0; j < ce.length; j++) {
					Object obj = ce[j].createExecutableExtension("classname");
					
					if (obj instanceof IMacroVariable)
					{
						IMacroVariable var = (IMacroVariable) obj;
						variables.put(var.getIdentifier(), var);
					}
				}
			}
		} catch (InvalidRegistryObjectException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	
}
