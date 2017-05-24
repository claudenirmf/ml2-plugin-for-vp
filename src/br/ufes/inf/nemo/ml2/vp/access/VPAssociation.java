package br.ufes.inf.nemo.ml2.vp.access;

import com.vp.plugin.model.IAssociation;
import com.vp.plugin.model.IAssociationEnd;

import br.ufes.inf.nemo.ml2.meta.ModelElement;
import br.ufes.inf.nemo.ml2.meta.Reference;
import br.ufes.inf.nemo.ml2.vp.ML2ModelLoader;

public class VPAssociation extends VPModelElement {

	VPAssociation(IAssociation source) {
		super(source);
	}
	
	@Override
	public IAssociation getVPSource() {
		return (IAssociation) super.getVPSource();
	}
	
	public VPClass getFrom() {
		return (VPClass) VPModelAccess.getModelElement(getFullyQualifiedName(getVPSource().getFrom()));
	}
	
	public VPClass getTo() {
		return (VPClass) VPModelAccess.getModelElement(getFullyQualifiedName(getVPSource().getTo()));
	}
	
	public IAssociationEnd getToEnd(){
		return (IAssociationEnd) getVPSource().getToEnd();
	}
	
	public IAssociationEnd getFromEnd(){
		return (IAssociationEnd) getVPSource().getFromEnd();
	}
	
	@Override
	public boolean equals(Object obj) {
		Reference reference;
		if (obj instanceof Reference)
			reference = (Reference) obj;
		else
			return super.equals(obj);
		
		// Compare target end name and reference name
		if(!reference.getName().equals(getVPSource().getToEnd().getName()))
			return false;
		// Compare reference container and association source
		String fqn1 = ML2ModelLoader.getFullyQualifiedName((ModelElement) reference.eContainer());
		String fqn2 = getFrom().getFullyQualifiedName();
		if(!fqn1.equals(fqn2))
			return false;
		// Compare reference target and association target
		String fqn3 = ML2ModelLoader.getFullyQualifiedName(reference.get_type());
		String fqn4 = getTo().getFullyQualifiedName();
		if(!fqn3.equals(fqn4))
			return false;
		
		return true;
	}

	public void update(Reference reference) {
		String upperBound = reference.getUpperBound()==-1 ? "*" : reference.getUpperBound()+"" ;
		String multiplicity = reference.getLowerBound()+".."+upperBound;
		getToEnd().setMultiplicity(multiplicity);
//		if(reference.getSubsetOf()!=null && !reference.getSubsetOf().isEmpty()){
//		}
		// TODO Add subsets and opposite
	}
	
	@Override
	public String toString() {
		String s = "[from " + getFromEnd().getName() + " to " + getToEnd().getName() + "]";
		return s;
	}

}
