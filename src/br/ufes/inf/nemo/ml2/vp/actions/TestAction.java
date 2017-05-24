package br.ufes.inf.nemo.ml2.vp.actions;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FilenameUtils;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;
import com.vp.plugin.model.IAssociation;
import com.vp.plugin.model.IAssociationEnd;
import com.vp.plugin.model.IAttribute;
import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IPackage;
import com.vp.plugin.model.factory.IModelElementFactory;

import br.ufes.inf.nemo.ml2.vp.ML2PluginForVP;

public class TestAction implements VPActionController {
	
//	private Set<IModelElement>	testElements = new HashSet<IModelElement>();

	@Override
	public void performAction(VPAction arg0) {
		//testAssociation();
		//testAttribute();
		//testThread();
		//testFileLoad();
//		testGetResourceSet();
		testKnowingAttributes();
	}

	private void testKnowingAttributes() {
		Iterator iter = ApplicationManager.instance().getProjectManager()
				.getProject().allLevelModelElementIterator();
		while(iter.hasNext()){
			Object obj = iter.next();
			if(obj instanceof IClass){
				IClass c = (IClass) obj;
				System.out.print(c.getName()+" ");
				Iterator attIter = c.attributeIterator();
				while(attIter.hasNext()){
					IAttribute att = (IAttribute) attIter.next();
					System.out.print("\t["+att.getName());
					System.out.print(" "+att.getMultiplicity());
					System.out.print(" "+att.getTypeAsString());
					System.out.println("]");
				}
			}
		}
	}

	private void testGetResourceSet() {
		System.out.println(ML2PluginForVP.getResourceSet());
		System.out.println(ML2PluginForVP.getResourceSetFromInjector());
	}

	private void testFileLoad() {
		final String folderPath = "C:\\Users\\claud\\git\\ml2-bicycle-challenge\\Bicycle Challenge";
		final File folder = new File(folderPath);
		if(folder.isDirectory())	listFilesForFolder(folder);
	}

	private void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				if(FilenameUtils.getExtension(fileEntry.getPath()).equals("ml2")) {
					System.out.print("\tML2 File: ");
				}
				System.out.println(fileEntry.getName());
			}
		}
	}
	
	@Override
	public void update(VPAction arg0) {
//		for (IModelElement elem : testElements)	elem.delete();
//		testElements.clear();
	}
	
	private void testThread() {
		ViewManager vm = ApplicationManager.instance().getViewManager(); 
		vm.clearMessages(ML2PluginForVP.PLUGIN_ID);
		new Thread() {
			@Override public void run() {
				int life = 4;
				while(life>=0){
					vm.showMessage("A's LIFE \t"+life--,ML2PluginForVP.PLUGIN_ID);
					try { sleep(1000); } catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		new Thread() {
			@Override public void run() {
				int life = 6;
				while(life>=0){
					vm.showMessage("B's LIFE \t"+life--,ML2PluginForVP.PLUGIN_ID);
					try { sleep(1000); } catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	private void testAttribute() {
		IModelElementFactory factory = IModelElementFactory.instance();
		IPackage container = factory.createPackage();	container.setName("Test");
		IClass ca = factory.createClass();	ca.setName("A");	container.addChild(ca);
		IClass cb = factory.createClass();	cb.setName("B");	container.addChild(cb);
		
		IAttribute att = factory.createAttribute();
		ca.addAttribute(att);
		att.setName("att");
		att.setMultiplicity(IAttribute.MULTIPLICITY_ZERO_TO_MANY);
		att.setType(cb);
		
		IAttribute att2 = factory.createAttribute();
		ca.addAttribute(att2);
		att2.setName("att2");
		att2.setMultiplicity(IAttribute.MULTIPLICITY_ZERO_TO_ONE);
		att2.setType(att.getTypeAsElement());
	}
	
	private void testAssociation() {
		IModelElementFactory factory = IModelElementFactory.instance();
		
		IPackage container = factory.createPackage();
		IClass ca = factory.createClass();	ca.setName("A");	container.addChild(ca);
		IClass cb = factory.createClass();	cb.setName("B");	container.addChild(cb);
		IAssociation atob = factory.createAssociation();	container.addChild(atob);
		
		atob.setName("A to B");	atob.setFrom(ca);	atob.setTo(cb);
		
		IAssociationEnd toEnd = (IAssociationEnd) atob.getToEnd();
		IAssociationEnd fromEnd = (IAssociationEnd) atob.getFromEnd();
		
		toEnd.setMultiplicity(IAssociationEnd.MULTIPLICITY_ONE_TO_MANY);
		fromEnd.setMultiplicity(IAssociationEnd.MULTIPLICITY_ZERO_TO_ONE);
		
		toEnd.setName("{someconstraint}");
		
		toEnd.setNavigable(IAssociationEnd.NAVIGABLE_NAVIGABLE);
		fromEnd.setNavigable(IAssociationEnd.NAVIGABLE_UNSPECIFIED);
		
//		testElements.add(container);
//		testElements.add(ca);
//		testElements.add(cb);
//		testElements.add(atob);
	}

}
