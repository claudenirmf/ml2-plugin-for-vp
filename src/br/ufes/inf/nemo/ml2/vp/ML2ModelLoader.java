package br.ufes.inf.nemo.ml2.vp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;
import com.vp.plugin.ApplicationManager;
import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IDependency;
import com.vp.plugin.model.IModelElement;
import com.vp.plugin.model.IPackage;
import com.vp.plugin.model.IStereotype;
import com.vp.plugin.model.ITaggedValue;
import com.vp.plugin.model.ITaggedValueContainer;
import com.vp.plugin.model.ITaggedValueDefinition;
import com.vp.plugin.model.ITaggedValueDefinitionContainer;
import com.vp.plugin.model.factory.IModelElementFactory;

import br.ufes.inf.nemo.ml2.ML2StandaloneSetup;
import br.ufes.inf.nemo.ml2.meta.Attribute;
import br.ufes.inf.nemo.ml2.meta.EntityDeclaration;
import br.ufes.inf.nemo.ml2.meta.FOClass;
import br.ufes.inf.nemo.ml2.meta.HOClass;
import br.ufes.inf.nemo.ml2.meta.Individual;
import br.ufes.inf.nemo.ml2.meta.ML2Class;
import br.ufes.inf.nemo.ml2.meta.ML2Model;
import br.ufes.inf.nemo.ml2.meta.MetaPackage;
import br.ufes.inf.nemo.ml2.meta.ModelElement;
import br.ufes.inf.nemo.ml2.meta.OrderlessClass;
import br.ufes.inf.nemo.ml2.meta.Reference;
import br.ufes.inf.nemo.ml2.vp.access.VPAssociation;
import br.ufes.inf.nemo.ml2.vp.access.VPAttribute;
import br.ufes.inf.nemo.ml2.vp.access.VPClass;
import br.ufes.inf.nemo.ml2.vp.access.VPDependency;
import br.ufes.inf.nemo.ml2.vp.access.VPDependencyType;
import br.ufes.inf.nemo.ml2.vp.access.VPModelAccess;
import br.ufes.inf.nemo.ml2.vp.access.VPModelElement;
import br.ufes.inf.nemo.ml2.vp.access.VPPackage;

public class ML2ModelLoader {
	
	public static final String ML2_FILE_EXTENSION = "ml2";

	public static final String STR_INDIVIDUAL = "individual";
	public static final String STR_FIRST_ORDER_CLASS = "firstorder";
	public static final String STR_HIGH_ORDER_CLASS = "highorder";
	public static final String STR_ORDERLESS_CLASS = "orderless";
	public static final String TG_VALLUE_ORDER = "order";
	
	public static final String STR_INSTANTIATION =  "instantiation";
	public static final String STR_SUBORDINATION =  "subordination";
	public static final String STR_POWER_TYPE =  "powertype";
	public static final String STR_CATEGORIZATION =  "categorization";
	public static final String STR_COMPL_CATEGORZATION =  "completecategorization";
	public static final String STR_DISJ_CATEGORIZATION =  "disjointcategorization";
	public static final String STR_PARTITIONS =  "partitions";
	
	public static final String BASE_TYPE_CLASS = IModelElementFactory.MODEL_TYPE_CLASS;
	public static final String BASE_TYPE_DEPENDENCY = IModelElementFactory.MODEL_TYPE_DEPENDENCY;
	
	private static final String DEFAULT_ORDER = "2";
	
//	private static ResourceSet rs;
	private static Set<IStereotype> stereotypes;
	
//	public static synchronized List<Resource> getResources(){
//		return getResourceSet().getResources();
//	}
//	
//	public static synchronized boolean isResourceSetLoaded(){
//		return rs!=null;
//	}
	
	public static XtextResourceSet clear(){
		XtextResourceSet resourceSet = ML2PluginForVP.getResourceSet();
		resourceSet.getResources().clear();
		return resourceSet;
//		if(getResourceSet()!=null)
//			getResourceSet().getResources().clear();
////		if(stereotypes!=null)
////			for (IStereotype str : stereotypes)
////				str.delete();
	}
	
//	public static synchronized void loadModel(String name, String path) {
//		// Initialize the model
//		@SuppressWarnings("unused")
//		MetaPackage mp = MetaPackage.eINSTANCE;
//		
//		// Register the XMI resource factory for the .xmi extension
//		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
//		Map<String, Object> map = reg.getExtensionToFactoryMap();
//		map.put("*", new XMIResourceFactoryImpl());
//		
//		// Obtain a new resource set
//		ResourceSet rs = getResourceSet();	clear();
//		
//		// Get the resource
//		Resource importedModel = rs.createResource(URI.createURI(path+"\\"+name));
//		
//        try {
//			FileInputStream file = new FileInputStream(path+"\\"+name);
//			importedModel.load(file, null);
//			file.close();
//			
//			for (EObject o : importedModel.getContents()) {
//				if(o instanceof ML2Model){
//					ML2Model m = (ML2Model) o;
//					System.out.println("Model "+m.getName()+" was loaded.");
//				}
//			}
//		} catch (FileNotFoundException e) {
//			System.out.println("File not found in '"+path+"\\"+name+"'.");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static synchronized void loadModel(String folderPath) {
		File folder = new File(folderPath);
		if(!folder.isDirectory())
			throw new InvalidPathException(folderPath, "The path must be of a valid directory.");
		
		// load a resource by URI, in this case from the file system
		XtextResourceSet resourceSet = clear();
		for (File fileEntry : folder.listFiles()) {
			if(FilenameUtils.getExtension(fileEntry.getPath()).equals(ML2_FILE_EXTENSION)){
				Resource res = resourceSet.getResource(URI.createFileURI(fileEntry.getPath()), true);
				System.out.println("Loading model "+((ML2Model) res.getContents().get(0)).getName());
			}
		}
		System.out.print("Loaded models: ");
		for (ML2Model m : getLoadedModels()) {
			System.out.print(m.getName()+"; ");
		}
		System.out.println();
	}

	public static void feed() {
		for (ModelElement element : getLoadedModelElements())
			feed(element);
	}
	
	public static void update() {
		Set<ModelElement> set = getLoadedModelElements();
		int count=1, size=set.size();
		for (ModelElement elem : set) {
			System.out.println("Updating ["+count+"/"+size+"] "); count++;
			
			if(elem instanceof ML2Class){
				ML2Class c = (ML2Class) elem;
				VPClass vpc = (VPClass) VPModelAccess.getModelElement(getFullyQualifiedName(c));
				setStereotype(c,vpc);
				updateSpecializations(c,vpc);
				updateInstantiations(c,vpc);
				updateSubordinations(c,vpc);
				updateCategorizations(c,vpc);
				updatePowertyping(c,vpc);
				updateReferences(c,vpc);
				updateAttributes(c,vpc);
				if(elem instanceof HOClass)
					vpc.setOrder((HOClass) elem);
			} else if(elem instanceof Individual) {
				Individual i = (Individual) elem;
				VPClass vpc = (VPClass) VPModelAccess.getModelElement(getFullyQualifiedName(i));
				setStereotype(i,vpc);
				updateInstantiations(i,vpc);
			}
		}
	}

	public static String getFullyQualifiedName(ModelElement element) {
		if(element instanceof EntityDeclaration){
			if(((EntityDeclaration) element).getName() == null)	
				System.out.println("The element name is null.");
			else if(element.eContainer() == null)	System.out.println("The container of "+((EntityDeclaration) element).getName()+" is null.");
			else if(((ML2Model) element.eContainer()).getName() == null)	System.out.println("The container name of"+((EntityDeclaration) element).getName()+" is null.");
			
			
			return ((ML2Model) element.eContainer()).getName() 
					+ '.' + ((EntityDeclaration) element).getName();
		} else {
			return "";
		}
	}

	public static IStereotype getStereotypeByNameAndBaseType(String strName, String baseType) {
		// Loads strList
		if (stereotypes == null || stereotypes.size() == 0) {
			stereotypes = new HashSet<IStereotype>();
			for (IModelElement e : ApplicationManager.instance().getProjectManager().getProject()
					.toAllLevelModelElementArray(IModelElementFactory.MODEL_TYPE_STEREOTYPE))
				stereotypes.add((IStereotype) e);
		}
		// Returns the stereotype in case it is found
		for(IModelElement m : stereotypes){
			final IStereotype s = (IStereotype) m;
			if(s.getName().equals(strName) && s.getBaseType().equals(baseType))
				return s;
		}
		// If the stereotype does not exist, its is created, recorded and returned
		final IStereotype s = IModelElementFactory.instance().createStereotype();
		s.setName(strName);
		s.setBaseType(baseType);
		if(strName.equals(STR_HIGH_ORDER_CLASS)){
			ITaggedValueDefinitionContainer container = IModelElementFactory.instance().createTaggedValueDefinitionContainer();
			s.setTaggedValueDefinitions(container);
			ITaggedValueDefinition order = container.createTaggedValueDefinition();
			order.setName(TG_VALLUE_ORDER);
			order.setType(ITaggedValueDefinition.TYPE_INTEGER);
			order.setDefaultValue(DEFAULT_ORDER);
		}
		stereotypes.add(s);
		return s;
	}

//	private static ResourceSet getResourceSet(){
//		return (rs==null) ? rs=new ResourceSetImpl() : rs ;
//	}

	private static Set<ML2Model> getLoadedModels(){
		Set<ML2Model> models = new HashSet<ML2Model>();
		for (Resource resource : ML2PluginForVP.getResourceSet().getResources()){
			EObject obj = resource.getContents().get(0);
			if(obj instanceof ML2Model){
				models.add((ML2Model) obj);
			}
//			for (EObject eo : resource.getContents())
//				if(eo instanceof ML2Model)
//					models.add((ML2Model) eo);
		}
		return models;
	}

	private static Set<ModelElement> getLoadedModelElements(){
		Set<ModelElement> elements = new HashSet<ModelElement>();
		for (ML2Model m : getLoadedModels())
			elements.addAll(m.getElements());
		return elements;
	}

	private static IStereotype getStereotypeOf(EntityDeclaration element) {
		if(element instanceof FOClass){
			return getStereotypeByNameAndBaseType(STR_FIRST_ORDER_CLASS, BASE_TYPE_CLASS);
		} else if(element instanceof HOClass){
			return getStereotypeByNameAndBaseType(STR_HIGH_ORDER_CLASS, BASE_TYPE_CLASS);
		} else if(element instanceof OrderlessClass){
			return getStereotypeByNameAndBaseType(STR_ORDERLESS_CLASS, BASE_TYPE_CLASS);
		} else {
			return getStereotypeByNameAndBaseType(STR_INDIVIDUAL, BASE_TYPE_CLASS);
		}
	}

	private static void feed(ModelElement element) {
		if(element instanceof EntityDeclaration){
			String fqn = getFullyQualifiedName(element);
			VPModelElement vpc = VPModelAccess.getModelElement(fqn);
			if(vpc == null) {
				VPModelElement vpp = VPModelAccess.getModelElement(((ML2Model) element.eContainer()).getName());
				if(vpp == null)	vpp = create((ML2Model) element.eContainer());
				vpc = create((EntityDeclaration) element,(VPPackage) vpp);
			}
		}
	}
	
	private static VPPackage create(ML2Model model) {
		IPackage pkg = IModelElementFactory.instance().createPackage();
		pkg.setName(model.getName());
		VPPackage vpp = (VPPackage) VPModelElement.wrap(pkg);
		VPModelAccess.register(vpp);
		return vpp;
	}

	private static VPClass create(EntityDeclaration element, VPPackage vpp) {
		IClass c = IModelElementFactory.instance().createClass();
		c.setName(element.getName());
		vpp.getVPSource().addChild(c);
		VPClass vpc = (VPClass) VPModelElement.wrap(c);
		VPModelAccess.register(vpc);
		return vpc;
	}

	private static void updateSpecializations(ML2Class c, VPClass vpc) {
		Set<VPClass> vpsupers = vpc.getSuperClasses();
		Set<ML2Class> ontolsupers = new HashSet<ML2Class>();
		ontolsupers.addAll(c.getSuperClasses());
		
		Set<VPClass> dontDelete = new HashSet<VPClass>();
		Set<ML2Class> alreadySupers = new HashSet<ML2Class>();
		
		// Remove old supers
		for (VPClass oldsuper : vpsupers) {
			for (ML2Class ontoLSuper : ontolsupers){
				if(oldsuper.equals(ontoLSuper)) {
					dontDelete.add(oldsuper);
					alreadySupers.add(ontoLSuper);
					break;
				}
			}
		}
		// Add missing supers
		ontolsupers.removeAll(alreadySupers);
		for (ML2Class oc : ontolsupers) {
			vpc.addSuper(oc);
		}
		// Remove non-matching supers
		vpsupers.removeAll(dontDelete);
		for (VPClass notasuper : vpsupers) {
			try {
				vpc.removeSuper(notasuper);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		// Add new supers
//		for (OntoLClass ontoLSuper : ontolsupers) {
//			if(ontoLSuper.eIsProxy())	continue;
//			// If it is not a proxy, verifies if it already exists
//			String ol_fqn = getFullyQualifiedName(ontoLSuper);
//			boolean found = false;
//			for (VPClass vpClass : vpsupers)
//				if(ol_fqn .equals( vpClass.getFullyQualifiedName())){
//					found = true;
//					break;
//				}
//			if(!found){
//				vpc.addSuper(ontoLSuper);
//			}
//		}
	}

	private static void updateInstantiations(EntityDeclaration e, VPClass vpc) {
		Set<VPClass> vp_iofs = vpc.getInstantiatedClasses();
		List<ML2Class> ontol_iofs = e.getInstantiatedClasses();
		Set<VPClass> dontDelete = new HashSet<VPClass>();
		Set<ML2Class> dontCreate = new HashSet<ML2Class>();
		// Who is on first but not on second must leave
		for (VPClass vp_iof : vp_iofs) {
			for (ML2Class ontol_iof : ontol_iofs) {
				if(vp_iof.equals(ontol_iof)){
					dontDelete.add(vp_iof);
					dontCreate.add(ontol_iof);
					break;
				}
			}
		}
		vp_iofs.removeAll(dontDelete);
		for (VPClass todelete : vp_iofs) {
			vpc.removeInstantiationTo(todelete);
		}
		ontol_iofs.removeAll(dontCreate);
		for (ML2Class tocreate : ontol_iofs) {
			vpc.addInstantiationTo(tocreate);
		}
	}

	private static void updateSubordinations(ML2Class c, VPClass vpc) {
		Set<VPClass> firsts = vpc.getSubordinatorClasses();
		List<ML2Class> seconds = c.getSubordinators();
		// Who is on first but not on second must leave
		for (VPClass first : firsts) {
			String fqn1 = first.getFullyQualifiedName();
			boolean found = false;
			for (ML2Class second : seconds) {
				String fqn2 = getFullyQualifiedName(second);
				if(fqn1.equals(fqn2)){
					found = true;
					break;
				}
			}
			if(!found){
				firsts.remove(first);
				// TODO delete old subordination dependency relation
			}
		}
		// Who is on second but not on first must stay
		for (ML2Class second : seconds){
			String fqn2 = getFullyQualifiedName(second);
			boolean found = false;
			for (VPClass first : firsts)
				if(fqn2 .equals( first.getFullyQualifiedName())){
					found = true;
					break;
				}
			if(!found){
				vpc.addSubordinatorClass(second);
			}
		}
	}

	private static void updateCategorizations(ML2Class c, VPClass vpc) {
		VPDependency vpd = vpc.getDependencyToBasetype();
		ML2Class cat_base = c.getCategorizedClass();
		if(vpd==null && cat_base==null){			// No current basetype
			return ; 								//dont exist -> do nothing
		}
		else if(vpd!=null && cat_base==null){
			vpd.delete();							//existed -> delete
			return ; 
		}
		else if(vpd==null && cat_base!=null){
			vpc.addBasetype(cat_base,c.getCategorizationType());	//dont exist -> create
			return ;
		} 
		else if(vpd.categorizes(cat_base, c.getCategorizationType())){
			return ;								// if they are the same -> do nothing
		}
		else {
			vpd.delete();							// if they don't match -> delete and create
			vpc.addBasetype(cat_base,c.getCategorizationType());
		}
	}

	private static void updatePowertyping(ML2Class c, VPClass vpc) {
		VPDependency vpd = vpc.getDependencyToPowertypeOf();
		ML2Class pwt_base = c.getPowertypeOf();
		if(vpd==null && pwt_base==null){			// No current basetype
			return ; 								//dont exist -> do nothing
		}
		else if(vpd!=null && pwt_base==null){
			vpd.delete();							//existed -> delete
			return ; 
		}
		else if(vpd==null && pwt_base!=null){
			vpc.addPowertypeOf(pwt_base);	//dont exist -> create
			return ;
		} 
		else if(vpd.isPowertypeOf(pwt_base, c)){
			return ;								// if they are the same -> do nothing
		}
		else {
			vpd.delete();							// if they don't match -> delete and create
			vpc.addPowertypeOf(pwt_base);
		}
	}

	private static void updateReferences(ML2Class c, VPClass vpc) {
		Set<VPAssociation> associations = vpc.getToAssociations();
		List<Reference> references = c.getReferences();
		Set<VPAssociation> theseAreOk = new HashSet<VPAssociation>();
		Set<Reference> ignoreThese = new HashSet<Reference>();
		// Who is on first but not on second must leave
		for (VPAssociation association : associations) {
			for (Reference reference : references) {
				if(association.equals(reference)){
					association.update(reference);
					theseAreOk.add(association);
					ignoreThese.add(reference);
					break;
				}
			}
		}
		associations.removeAll(theseAreOk);
		references.removeAll(ignoreThese);
		for (VPAssociation association : associations) {
			association.getVPSource().delete();
		}
		// Who is on second but not on first must stay
		for (Reference reference : references) {
			vpc.addToAssociation(reference);
		}
	}

	private static void updateAttributes(ML2Class c, VPClass vpc) {
		Set<VPAttribute> vp_atts = vpc.getAttributes();
		List<Attribute> ontol_atts = c.getAttributes();
		Set<VPAttribute> theseAreOk = new HashSet<VPAttribute>();
		Set<Attribute> ignoreThese = new HashSet<Attribute>();
		// Who is on first but not on second must leave
		for (VPAttribute vp_att : vp_atts) {
			for (Attribute ontol_att : ontol_atts) {
				if(vp_att.equals(ontol_att)){
					vp_att.update(ontol_att);
					theseAreOk.add(vp_att);
					ignoreThese.add(ontol_att);
					break;
				}
			}
		}
		vp_atts.removeAll(theseAreOk);
		ontol_atts.removeAll(ignoreThese);
		for (VPAttribute vp_att : vp_atts) {
			vp_att.getVPSource().delete();
		}
		// Who is on second but not on first must stay
		for (Attribute ontol_att : ontol_atts) {
			vpc.addAttribute(ontol_att);
		}
	}

	private static void setStereotype(EntityDeclaration e, VPClass vpc) {
		IClass source = vpc.getVPSource();
		// Clear previous stereotypes
		IStereotype[] old = source.toStereotypeModelArray();
		if (old != null)
			for (IStereotype str : old)
				source.removeStereotype(str);
		// Add proper stereotype
		source.addStereotype(getStereotypeOf(e));
	}
	
}
