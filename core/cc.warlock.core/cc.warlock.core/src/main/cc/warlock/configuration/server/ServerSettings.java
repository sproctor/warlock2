package cc.warlock.configuration.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cc.warlock.client.ICommand;
import cc.warlock.client.IWarlockClientViewer;
import cc.warlock.client.internal.Command;
import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.client.stormfront.IStormFrontClientViewer;
import cc.warlock.client.stormfront.WarlockColor;
import cc.warlock.configuration.WarlockConfiguration;
import cc.warlock.configuration.skin.DefaultSkin;
import cc.warlock.configuration.skin.IWarlockSkin.ColorType;
import cc.warlock.configuration.skin.IWarlockSkin.FontFaceType;
import cc.warlock.configuration.skin.IWarlockSkin.FontSizeType;


public class ServerSettings implements Comparable<ServerSettings>
{

	public static final String PRESET_OR_STRING_SETTING_UPDATE_PREFIX = "<stgupd>";
	public static final String IGNORES_TEXT = "<<m><ignores disable=\"n\"></ignores><ignores disable=\"n\"></ignores></<m>";
	
	private IStormFrontClient client;
	private String playerId, clientVersion;
	private int majorVersion;
	private Document document;
	protected Palette palette;
	protected Hashtable<String, Preset> presets = new Hashtable<String,Preset>();
	protected Hashtable<String, HighlightString> highlightStrings = new Hashtable<String, HighlightString>();
	protected Hashtable<String, String> variables = new Hashtable<String, String>();
	protected ArrayList<ArrayList<MacroKey>> macroSets = new ArrayList<ArrayList<MacroKey>>();
	protected Hashtable<String, ServerScript> scripts = new Hashtable<String, ServerScript>();
	protected DefaultSkin defaultSkin = new DefaultSkin(this);
	
	protected ArrayList<HighlightString> deletedHighlightStrings = new ArrayList<HighlightString>();
	protected ArrayList<String> deletedVariables = new ArrayList<String>();
	protected ArrayList<IServerSettingsListener> listeners = new ArrayList<IServerSettingsListener>();
	
	private Element mainWindowElement, mainWindowFontElement,
		mainWindowColumnFontElement, commandLineElement, paletteElement, presetsElement, stringsElement, namesElement;
	
	public static int getPixelSizeInPoints (int pixelSize)
	{
		// we'll assume 96 dpi for now
		double points = pixelSize * (72.0/96.0);
		return (int) Math.round(points);
	}
	
	public ServerSettings (IStormFrontClient client)
	{
		this.client = client;
	}
	
	public ServerSettings (IStormFrontClient client, String playerId)
	{
		this.client = client;
		
		load(playerId);
	}
	
	public static String getCRC (String playerId)
	{
		try {
			FileInputStream stream = new FileInputStream(WarlockConfiguration.getConfigurationFile("serverSettings_" + playerId + ".xml"));
			SAXReader reader = new SAXReader();
			Document document = reader.read(stream);
			
			String crc = ((Element)document.selectSingleNode("/settings")).attributeValue("crc");
			
			stream.close();
			return crc;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void addServerSettingsListener (IServerSettingsListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeServerSettingsListener (IServerSettingsListener listener)
	{
		if (listeners.contains(listener))
			listeners.remove(listener);
	}
	
	public void load (String playerId)
	{
		this.playerId = playerId;
		
		try {
			FileInputStream stream = new FileInputStream(WarlockConfiguration.getConfigurationFile("serverSettings_" + playerId + ".xml"));
			SAXReader reader = new SAXReader();
			document = reader.read(stream);
			
			mainWindowElement = (Element) document.selectSingleNode("/settings/stream/w[@id=\"smain\"]");
			if (mainWindowElement != null) {
				mainWindowFontElement = (Element) mainWindowElement.selectSingleNode("font");
				mainWindowColumnFontElement = (Element) mainWindowElement.selectSingleNode("columnFont");
			}
			
			commandLineElement = (Element) document.selectSingleNode("/settings/cmdline");
			
			loadPalette();
			loadPresets();
			loadHighlightStrings();
			loadVariables();
			loadMacros();
			loadScripts();
			
			// initalize before we call the viewers
			defaultSkin.loadDefaultPresets(this, presets);
			
			for (IWarlockClientViewer v : client.getViewers())
			{
				IStormFrontClientViewer viewer = (IStormFrontClientViewer) v;
				viewer.loadServerSettings(this);
			}
			
			stream.close();
			incrementMajorVersion();
			
			for (IServerSettingsListener listener : listeners) {
				try {
					listener.serverSettingsLoaded(this);
				} catch (Throwable t) { }
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadPalette ()
	{
		paletteElement = (Element) document.selectSingleNode("/settings/palette");
		palette = new Palette(this, paletteElement);
	}
	
	private void loadPresets ()
	{
		presetsElement = (Element) document.selectSingleNode("/settings/presets");
		if (presetsElement != null)
		{
			for (Object o : presetsElement.elements())
			{
				Element pElement = (Element) o;
				String presetId = pElement.attributeValue("id");
				
				presets.put(presetId, new Preset(this, pElement, palette));
			}
		}
	}
	
	private void loadHighlightStrings()
	{
		stringsElement = (Element) document.selectSingleNode("/settings/strings");
		if (stringsElement != null)
		{
			for (Object o : stringsElement.elements())
			{
				Element hElement = (Element) o;
				String text = hElement.attributeValue("text");
				
				highlightStrings.put(text, new HighlightString(this, hElement, palette));
			}
		}
		
		namesElement = (Element) document.selectSingleNode("/settings/names");
		if (namesElement != null)
		{
			for (Object o : namesElement.elements())
			{
				Element hElement = (Element) o;
				String text = hElement.attributeValue("text");
				
				highlightStrings.put(text, new HighlightString(this, hElement, palette));
				highlightStrings.get(text).setIsName(true);
			}
		}
	}
	
	private void loadVariables()
	{
		Element varsElement = (Element) document.selectSingleNode("/settings/vars");
		if (varsElement != null)
		{
			for (Object o : varsElement.elements())
			{
				Element varElement = (Element) o;
				variables.put(varElement.attributeValue("name"), varElement.attributeValue("value"));
			}
		}
	}
	
	private void loadMacros ()
	{
		Element macrosElement = (Element) document.selectSingleNode("/settings/macros");
		if (macrosElement != null)
		{
			for (Object o : macrosElement.elements())
			{
				Element keysElement = (Element) o;
				ArrayList<MacroKey> keys = new ArrayList<MacroKey>();
				macroSets.add(keys);
				
				for (Object k : keysElement.elements())
				{
					Element kElement = (Element) k;
					
					keys.add(new MacroKey(this, kElement.attributeValue("key"), kElement.attributeValue("action")));
				}
			}
		}
	}
	
	private void loadScripts ()
	{
		Element scriptsElement = (Element) document.selectSingleNode("/settings/scripts");
		if (scriptsElement != null)
		{
			for (Object s : scriptsElement.elements())
			{
				Element sElement = (Element) s;
				if(sElement != null) {
					String name = sElement.attributeValue("name");
					if(name != null) {
						scripts.put(name, new ServerScript(this, sElement));
					} else {
						System.out.println("Couldn't find name");
					}
				} else {
					System.out.println("didn't get element in script");
				}
			}
		}
	}
	
	protected void incrementMajorVersion ()
	{
		// Needed so our settings are validated by other Stormfront clients
		
		ICommand command = new Command(
			PRESET_OR_STRING_SETTING_UPDATE_PREFIX +
			ServerSetting.UPDATE_PREFIX +
			"<settings client=\"" + clientVersion + "\" major=\"" + majorVersion + "\"></settings>" +
			"<settings client=\"" + clientVersion + "\" major=\"" + (++majorVersion) + "\"></settings>" +
			ServerSetting.UPDATE_SUFFIX, new Date());
		command.setInHistory(true);
		
		client.send(command);
	}
	
	public String getFontFaceSetting (FontFaceType settingType)
	{
		Element fontElement = null;
		
		switch (settingType)
		{
			case MainWindow_FontFace: fontElement = mainWindowFontElement; break;
			case MainWindow_MonoFontFace: fontElement = mainWindowColumnFontElement; break;
			case CommandLine_FontFace: fontElement = commandLineElement; break;
		}
		
		if (fontElement != null)
		{
			return fontElement.attributeValue("face");
		}
		
		return null;
	}
	
	public WarlockColor getColorSetting (ColorType settingType)
	{
		return getColorSetting(settingType, true);
	}
	
	public WarlockColor getColorSetting (ColorType settingType, boolean skinFallback)
	{
		String color = null;
		
		switch (settingType)
		{
			case MainWindow_Background: color = mainWindowElement == null ? null : mainWindowElement.attributeValue("bgcolor"); break;
			case MainWindow_Foreground: color = mainWindowElement == null ? null : mainWindowElement.attributeValue("fgcolor"); break;
			case CommandLine_Background: color = commandLineElement.attributeValue("bgcolor"); break;
			case CommandLine_Foreground: color = commandLineElement.attributeValue("fgcolor"); break;
			case CommandLine_BarColor: color = commandLineElement.attributeValue("barcolor"); break;
		}
		
		if (color == null) color = "skin";
		
		if (color.charAt(0) == '@')
		{
			WarlockColor paletteColor = palette.getPaletteColor(color.substring(1));
			paletteColor.addPaletteReference(this);
			return paletteColor;
		}
		else if ("skin".equals(color) && skinFallback)
		{
			return defaultSkin.getColor(settingType);
		}
		else if (color.charAt(0) == '#') {
			return new WarlockColor(color);
		}
		
		else return WarlockColor.DEFAULT_COLOR;
	}
	
	
	public int getFontSizeSetting (FontSizeType settingType)
	{
		Element fontElement = null;
		
		switch (settingType)
		{
			case MainWindow_FontSize: fontElement = mainWindowFontElement;
			case MainWindow_MonoFontSize: fontElement = mainWindowColumnFontElement;
		}
		
		if (fontElement != null)
		{
			return getPixelSizeInPoints(Integer.parseInt(fontElement.attributeValue("size")));
		}
		
		return defaultSkin.getFontSize(settingType);
	}
	
	public Palette getPalette ()
	{
		return palette;
	}
	
	public Preset getPreset (String presetId)
	{
		return presets.get(presetId);
	}
		
	public Collection<HighlightString> getHighlightStrings ()
	{	
		return highlightStrings == null ? null : highlightStrings.values();
	}
	
	public void clearHighlightStrings ()
	{
		highlightStrings.clear();
	}
	
	public void updateHighlightString (HighlightString string)
	{
		if (!highlightStrings.containsKey(string.getText()))
		{
			string.setNew(true);
		}
		highlightStrings.put(string.getText(), string);
	}
	
	public void deleteHighlightString (HighlightString string)
	{
		if (highlightStrings.containsKey(string.getText()))
		{
			deletedHighlightStrings.add(string);
			highlightStrings.remove(string.getText());
		}
	}
	
	protected void saveHighlights(boolean saveNames)
	{
		StringBuffer stringsAddMarkup = new StringBuffer();
		StringBuffer stringsUpdateMarkup = new StringBuffer();
		StringBuffer stringsDeleteMarkup = new StringBuffer();
		
		String paletteMarkup = "";
		if (palette.needsUpdate())
		{
			paletteMarkup = palette.toStormfrontMarkup();
		}
		
		for (HighlightString string : highlightStrings.values())
		{
			if (string.isName() == saveNames) {
				if (string.needsUpdate())
				{
					if (!string.isNew())
					{
						stringsUpdateMarkup.append(saveNames ?
								HighlightString.NAMES_PREFIX :
									HighlightString.STRINGS_PREFIX);
						stringsUpdateMarkup.append(ServerSetting.UPDATE_PREFIX);
						
						if (string.getOriginalHighlightString() != null)
							stringsUpdateMarkup.append(string.getOriginalHighlightString().toStormfrontMarkup());
						
						stringsUpdateMarkup.append(string.toStormfrontMarkup());
						stringsUpdateMarkup.append(ServerSetting.UPDATE_SUFFIX);
						stringsUpdateMarkup.append(saveNames ?
								HighlightString.NAMES_SUFFIX :
									HighlightString.STRINGS_SUFFIX);
						
						string.saveToDOM();
						string.setNeedsUpdate(false);
					} else {
						stringsAddMarkup.append(string.toStormfrontAddMarkup());
					}
				}
			}
		}
		
		for (HighlightString string : deletedHighlightStrings)
		{
			// don't send the delete command if it was re-added after it was marked for deletion
			if (highlightStrings.containsKey(string.getText())) continue;
			
			if (saveNames == string.isName()) {
				stringsDeleteMarkup.append(saveNames ?
						HighlightString.NAMES_PREFIX :
							HighlightString.STRINGS_PREFIX);
				stringsDeleteMarkup.append(ServerSetting.DELETE_PREFIX);
				stringsDeleteMarkup.append(string.toStormfrontMarkup());
				stringsDeleteMarkup.append(ServerSetting.DELETE_SUFFIX);
				stringsDeleteMarkup.append(saveNames ?
						HighlightString.NAMES_SUFFIX :
							HighlightString.STRINGS_SUFFIX);
				
				string.deleteFromDOM();
			}
		}
		
		if (stringsDeleteMarkup.length() > 0)
		{
			sendSettingsUpdate(PRESET_OR_STRING_SETTING_UPDATE_PREFIX, stringsDeleteMarkup, IGNORES_TEXT + paletteMarkup);
			
			deletedHighlightStrings.clear();
		}
		
		if (stringsAddMarkup.length() > 0)
		{
			sendSettingsUpdate(PRESET_OR_STRING_SETTING_UPDATE_PREFIX, stringsAddMarkup, IGNORES_TEXT + paletteMarkup);
		}
		
		if (stringsUpdateMarkup.length() > 0)
		{
			sendSettingsUpdate(PRESET_OR_STRING_SETTING_UPDATE_PREFIX, stringsUpdateMarkup, IGNORES_TEXT + paletteMarkup);
		}
		
		saveLocalXml();
	}
	
	public void saveHighlightStrings ()
	{
		saveHighlights(false);
	}
	
	public void saveHighlightNames ()
	{
		saveHighlights(true);
	}
	
	public void savePresets ()
	{
		StringBuffer presetUpdateMarkup = new StringBuffer();
		
		for (Preset preset : presets.values())
		{
			if (preset.needsUpdate())
			{
				presetUpdateMarkup.append(preset.getOriginalPreset().toStormfrontMarkup());
				presetUpdateMarkup.append(preset.toStormfrontMarkup());
				preset.saveToDOM();
				preset.setNeedsUpdate(false);
			}
		}
				
		if (presetUpdateMarkup.length() > 0)
		{
			sendSettingsUpdate(
				PRESET_OR_STRING_SETTING_UPDATE_PREFIX +
				Preset.STORMFRONT_MARKUP_PREFIX +
				ServerSetting.UPDATE_PREFIX,
				presetUpdateMarkup,
				ServerSetting.UPDATE_SUFFIX +
				Preset.STORMFRONT_MARKUP_SUFFIX);
		}
		
		saveLocalXml();
	}
	
	protected void saveLocalXml ()
	{
		try {
			FileWriter writer = new FileWriter(WarlockConfiguration.getConfigurationFile("serverSettings_" + playerId + ".xml"));
			document.write(writer);
			writer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private void sendSettingsUpdate (String prefix, StringBuffer markup, String suffix)
	{
		if (markup.length() > 0)
		{
			System.out.println("[test-settings-update]\n\n" + prefix + markup.toString() + suffix);
			
			try {
				client.getConnection().send(prefix + markup.toString() + suffix + "\n");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Preset createPreset ()
	{
		return Preset.createPresetFromParent(this, presetsElement);
	}
	
	public HighlightString createHighlightString (boolean isName)
	{
		HighlightString string = null;
		if (isName)
			string = HighlightString.createHighlightStringFromParent(this, namesElement);
		else
			string = HighlightString.createHighlightStringFromParent(this, stringsElement);
		
		string.setIsName(isName);
		return string;
	}
	
	public int compareTo(ServerSettings o) {
		if (this == o) return 0;
		return -1;
	}
	
	public DefaultSkin getDefaultSkin ()
	{
		return defaultSkin;
	}
	
	public boolean containsVariable (String name)
	{
		return variables.containsKey(name);
	}
	
	public String getVariable (String name)
	{
		return variables.get(name);
	}
	
	public Collection<String> getVariableNames ()
	{
		return variables.keySet();
	}
	
	public List<MacroKey> getMacroSet (int set)
	{
		if (macroSets.size() > set) {
			return macroSets.get(set);
		} else {
			List<MacroKey> macros = Collections.emptyList();
			return macros;
		}
	}
	
	public boolean containsServerScript (String scriptName)
	{
		return scripts.containsKey(scriptName);
	}
	
	public ServerScript getServerScript (String scriptName)
	{
		return scripts.get(scriptName);
	}
	
	public Collection<ServerScript> getAllServerScripts ()
	{
		return scripts.values();
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}
}