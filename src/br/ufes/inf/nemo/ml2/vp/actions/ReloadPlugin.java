package br.ufes.inf.nemo.ml2.vp.actions;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;

import br.ufes.inf.nemo.ml2.vp.ML2ModelLoader;
import br.ufes.inf.nemo.ml2.vp.ML2PluginForVP;
import br.ufes.inf.nemo.ml2.vp.access.VPModelAccess;

public class ReloadPlugin implements VPActionController {

	@Override
	public void performAction(VPAction arg0) {
		VPModelAccess.clear();
		ML2ModelLoader.clear();
		ApplicationManager.instance().reloadPluginClasses(ML2PluginForVP.PLUGIN_ID);
		System.out.println("Plugin reloaded.");
	}

	@Override
	public void update(VPAction arg0) {}

}
