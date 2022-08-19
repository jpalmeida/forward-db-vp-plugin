package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.util.MissingConstraint;

public class ConstraintContainer {
	
	private ArrayList<MissingConstraintData> missingConstraints;
	
	public ConstraintContainer() {
		missingConstraints = new ArrayList<MissingConstraintData>();
	}
	
	public boolean existsMissingConstraint(MissingConstraint missingConstraint) {
		for(MissingConstraintData constraintData : this.missingConstraints) {
			if(constraintData.getMissingConstraint() == missingConstraint)
				return true;
		}
		return false;
	}
	
	public void addMissingConstraint(Node sourceNode, GraphAssociation association, MissingConstraint missingConstraint) {
//		if(!isSameOriginalAssociation(association, missingConstraint ))
			this.missingConstraints.add(new MissingConstraintData(sourceNode, association, missingConstraint));
	}
	
	
	public ArrayList<MissingConstraintData> getMissingConstraint(MissingConstraint missingConstraint){
		ArrayList<MissingConstraintData> result = new ArrayList<MissingConstraintData>();
		for(MissingConstraintData constraintData : this.missingConstraints ) {
			if(constraintData.getMissingConstraint() == missingConstraint)
				result.add(constraintData);
		}
		
		return result;
	}
	
	public ArrayList<MissingConstraintData> getAllMissingConstraint(){
		ArrayList<MissingConstraintData> result = new ArrayList<MissingConstraintData>();
		for(MissingConstraintData constraintData : this.missingConstraints ) {
				result.add(constraintData.clone());
		}
		return result;
	}
	
//	private boolean isSameOriginalAssociation(GraphAssociation newAssociation, MissingConstraint missingConstraint) {
//		GraphAssociation association;
//		for(MissingConstraintData data : this.missingConstraints) {
//			if(newAssociation.getOriginalAssociation() != null) {
//				association = data.getSourceAssociation();
//				association = association.getOriginalAssociation();
//				if(association.isMyId(newAssociation.getOriginalAssociation().getID())) {
//					if(data.getMissingConstraint() == missingConstraint) {
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}
	
	public boolean existsMissingConstraintForAssociation(GraphAssociation association) {
		for(MissingConstraintData constraint : this.missingConstraints) {
			if(constraint.getSourceAssociation().isMyId(association.getOriginalAssociation().getID()))
				return true;
		}
		
		return false;
	}
	
	public void removeMissingConstraint(MissingConstraintData mc) {
		this.missingConstraints.remove(mc);
	}
	
	public ConstraintContainer clone() {
		ConstraintContainer container = new ConstraintContainer();
		for(MissingConstraintData constraintData : this.missingConstraints) {
			container.addMissingConstraint(constraintData.clone());
		}
		return container;
	}
	
	private void addMissingConstraint(MissingConstraintData constraintData) {
			this.missingConstraints.add(constraintData);
	}
	
	public String toString() {
		String text = "";
		String mc1 = "";
		String mc1Inverse = "";
		String mc3 = "";
		String mc6 = "";
		String mc6Inverse = "";
		
		
		for(MissingConstraintData data : this.missingConstraints) {
			if(data.getMissingConstraint() == MissingConstraint.MC1_2)
				mc1 += data.getSourceNode().getName() + " | ";
			
			if(data.getMissingConstraint() == MissingConstraint.MC1_2_Inverse)
				mc1Inverse += data.getSourceNode().getName() + " | ";
			
			if(data.getMissingConstraint() == MissingConstraint.MC3_4)
				mc3 += data.getSourceNode().getName() + " | ";
			
			if(data.getMissingConstraint() == MissingConstraint.MC6)
				mc6 += data.getSourceNode().getName() + " | ";
			
			if(data.getMissingConstraint() == MissingConstraint.MC6_Inverse)
				mc6Inverse += data.getSourceNode().getName() + " | ";
		}
		
		if( mc1.length() > 1)
			text += "MC1: {" + mc1 + "} ";
		
		if( mc1Inverse.length() > 1)
			text += "MC1 Inverse: {" + mc1Inverse+ "} ";
		
		if( mc3.length() > 1)
			text += "MC3: {" + mc3+ "} ";
		
		if( mc6.length() > 1)
			text += "MC6: {" + mc6+ "}";
		
		if( mc6Inverse.length() > 1)
			text += "MC6 Inverse: {" + mc6Inverse+ "} ";
		
		if(text.length() > 1)
			text = "[" + text + "]";
		
				
		return text;
	}
	
}
