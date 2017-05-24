package br.ufes.inf.nemo.ml2.vp.access;

import com.vp.plugin.model.IAttribute;

import br.ufes.inf.nemo.ml2.meta.Attribute;
import br.ufes.inf.nemo.ml2.meta.ModelElement;
import br.ufes.inf.nemo.ml2.vp.ML2ModelLoader;

public class VPAttribute extends VPModelElement {

	VPAttribute(IAttribute att) {
		super(att);
	}
	
	@Override
	public IAttribute getVPSource() {
		return (IAttribute) super.getVPSource();
	}
	
	@Override
	public boolean equals(Object obj) {
		Attribute attribute;
		if (obj instanceof Attribute)	attribute = (Attribute) obj;
		else 							return super.equals(obj);
		
		// Compare VP's attribute name and attribute name
		if(!attribute.getName().equals(getVPSource().getName()))
			return false;
		// Compare parent class name and container ontol class name
		String fqn1 = ML2ModelLoader.getFullyQualifiedName((ModelElement) attribute.eContainer());
		String fqn2 = getParent().getFullyQualifiedName();
		if(!fqn1.equals(fqn2))
			return false;
		// Compare attribute type name and property type name
		// TODO This is going to be a problem
//		String fqn3 = ML2ModelLoader.getFullyQualifiedName((ModelElement) attribute.get_type());
//		String fqn4 = getType().getFullyQualifiedName();
//		if(!fqn3.equals(fqn4))
//			return false;
		String att_name = attribute.isPrimitive() ? 
				attribute.getPrimitiveType().toString() : attribute.get_type().getName() ;
		if(!getVPSource().getTypeAsString().equals(att_name))
			return false;

		return true;
	}

	public VPClass getType() {
		String fqn = getFullyQualifiedName(getVPSource().getTypeAsElement());
		VPClass vpc = (VPClass) VPModelAccess.getModelElement(fqn);
		return vpc;
	}

	public VPClass getParent() {
		String fqn = VPModelElement.getFullyQualifiedName(getVPSource().getParent());
		return (VPClass) VPModelAccess.getModelElement(fqn);
	}

	public void update(Attribute attribute) {
		String upperBound = attribute.getUpperBound()==-1 ? "*" : attribute.getUpperBound()+"" ;
		String multiplicity = attribute.getLowerBound()+".."+upperBound;
		getVPSource().setMultiplicity(multiplicity);
	}

}
