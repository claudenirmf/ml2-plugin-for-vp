package br.ufes.inf.nemo.ml2.vp;

import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;
import com.vp.plugin.VPPlugin;
import com.vp.plugin.VPPluginInfo;

import br.ufes.inf.nemo.ml2.ML2StandaloneSetup;

public class ML2PluginForVP implements VPPlugin {

	public static final String PLUGIN_ID = "br.ufes.inf.nemo.ml2.vp";
	private static Injector injector;
	private static XtextResourceSet resourceSet;
	
	@Override
	public void loaded(VPPluginInfo arg0) {
		System.out.println(PLUGIN_ID+" loaded.");
	}
	
	public static XtextResourceSet getResourceSet(){
		// obtain a resourceset from the injector
		if(resourceSet==null)
			resourceSet = getInjector().getInstance(XtextResourceSet.class);
		return resourceSet;
	}
	
	public static XtextResourceSet getResourceSetFromInjector(){
		// obtain a resourceset from the injector
		if(injector==null)	return null;
		return injector.getInstance(XtextResourceSet.class);
	}
	
	public static Injector getInjector(){
		// obtain a resourceset from the injector
		if(injector==null)
			injector = new ML2StandaloneSetup().createInjectorAndDoEMFRegistration();
		return injector;
	}

	@Override
	public void unloaded() {
		System.out.println(PLUGIN_ID+" unloaded.");
	}

}
