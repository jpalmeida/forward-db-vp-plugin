package br.ufes.inf.nemo.ontoumltodb;

import com.vp.plugin.VPPlugin;
import com.vp.plugin.VPPluginInfo;

public class OntoumlToDbPlugin implements VPPlugin{

	public static final String PLUGIN_VERSION_RELEASE = "0.1";
	
	/** 
	 * OntoUMLPlugin constructor. Declared to make explicit Open API requirements. 
	 */
	public OntoumlToDbPlugin() {
	    System.out.println("OntoUML to DB Plugin (v" + PLUGIN_VERSION_RELEASE + ") loaded successfully.");
	}

	/**
	 * Called by Visual Paradigm when the plugin is loaded.
	 *
	 * @param pluginInfo
	 */
	public void loaded(VPPluginInfo pluginInfo) {
	}

	/**
	 * Called by Visual Paradigm when the plugin is unloaded (i.e., Visual Paradigm
	 * will be exited). This method is not called when the plugin is reloaded.
	 */
	public void unloaded() {
	}
}
