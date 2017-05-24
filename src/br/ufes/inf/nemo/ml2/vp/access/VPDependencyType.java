package br.ufes.inf.nemo.ml2.vp.access;

import br.ufes.inf.nemo.ml2.meta.CategorizationType;
import br.ufes.inf.nemo.ml2.vp.ML2ModelLoader;

public enum VPDependencyType {

	INSTANTIATION(ML2ModelLoader.STR_INSTANTIATION),
	SUBORDINATION(ML2ModelLoader.STR_SUBORDINATION),
	CATERGORIZATION(ML2ModelLoader.STR_CATEGORIZATION),
	COMPLETE_CATEGORIZATION(ML2ModelLoader.STR_COMPL_CATEGORZATION),
	DISJOINT_CATEGORIZATION(ML2ModelLoader.STR_DISJ_CATEGORIZATION),
	PARTITIONING(ML2ModelLoader.STR_PARTITIONS),
	POWERTYPING(ML2ModelLoader.STR_POWER_TYPE);
	
	private String stereotype_name;
	
	private VPDependencyType(String str_name) {
		this.stereotype_name = str_name;
	}
	
	public String str(){
		return this.stereotype_name;
	}
	
	public CategorizationType convert(){
		switch (this) {
		case CATERGORIZATION:
			return CategorizationType.CATEGORIZER;
		case COMPLETE_CATEGORIZATION:
			return CategorizationType.COMPLETE_CATEGORIZER;
		case DISJOINT_CATEGORIZATION:
			return CategorizationType.DISJOINT_CATEGORIZER;
		case PARTITIONING:
			return CategorizationType.PARTITIONER;
		default:
			return null;
		}
	}
	
	@Override
	public String toString() {
		return this.str();
	}

}
