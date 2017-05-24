package br.ufes.inf.nemo.ml2.vp.actions;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;

import br.ufes.inf.nemo.ml2.vp.ML2ModelLoader;
import br.ufes.inf.nemo.ml2.vp.ML2PluginForVP;
import br.ufes.inf.nemo.ml2.vp.access.VPModelAccess;

public class OpenSynchronizationMenu implements VPActionController {

	@Override
	public void performAction(VPAction arg0) {
//		ViewManager vm = ApplicationManager.instance().getViewManager();
//		vm.clearMessages(ML2PluginForVP.PLUGIN_ID);
//		System.out.println("Open synchronization menu button pressed.");
//		
//		VPModelAccess.load();
//		ML2ModelLoader.clear();
//		String path = "C:\\Users\\claud\\git\\OntoL-Maven\\br.ufes.inf.nemo.ontol.parent\\br.ufes.inf.nemo.ontol.lib\\src-gen\\models\\";
//		String name = "ufo-a.xmi";
//		ML2ModelLoader.loadModel(name,path);
//		ML2ModelLoader.feed();
//		ML2ModelLoader.update();
//		
		System.out.println("Open synchronization action performed.");
	}

	@Override
	public void update(VPAction arg0) {}

}
