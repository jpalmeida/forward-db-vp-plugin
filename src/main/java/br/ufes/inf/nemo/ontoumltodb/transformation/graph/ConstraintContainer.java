package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.util.MissingConstraint;

public class ConstraintContainer {
	
	private ArrayList<ConstraintData> missingConstraints;
	
	public ConstraintContainer() {
		missingConstraints = new ArrayList<ConstraintData>();
	}
	
	public boolean existsMissingConstraint(MissingConstraint missingConstraint) {
		for(ConstraintData constraintData : this.missingConstraints) {
			if(constraintData.getMissingConstraint() == missingConstraint)
				return true;
		}
		return false;
	}
	
	public void addMissingConstraint(Node sourceNode, GraphAssociation association, MissingConstraint missingConstraint) {
		if(!isSameOriginalAssociation(association, missingConstraint ))
			this.missingConstraints.add(new ConstraintData(sourceNode, association, missingConstraint));
	}
	
	public void addMissingConstraint(Node sourceNode, GraphAssociation association, NodeProperty propertyToFilter, String filterValue, MissingConstraint missingConstraint) {
		if(!isSameOriginalAssociation(association, missingConstraint ))
			this.missingConstraints.add(new ConstraintData(sourceNode, association, propertyToFilter, filterValue, missingConstraint));
	}
	
	
	public ArrayList<ConstraintData> getMissingConstraint(MissingConstraint missingConstraint){
		ArrayList<ConstraintData> result = new ArrayList<ConstraintData>();
		for(ConstraintData constraintData : this.missingConstraints ) {
			if(constraintData.getMissingConstraint() == missingConstraint)
				result.add(constraintData.clone());
		}
		
		return result;
	}
	
	public ArrayList<ConstraintData> getAllMissingConstraint(){
		ArrayList<ConstraintData> result = new ArrayList<ConstraintData>();
		for(ConstraintData constraintData : this.missingConstraints ) {
				result.add(constraintData.clone());
		}
		return result;
	}
	
	private boolean isSameOriginalAssociation(GraphAssociation newAssociation, MissingConstraint missingConstraint) {
		GraphAssociation association;
		for(ConstraintData data : this.missingConstraints) {
			if(newAssociation.getOriginalAssociation() != null) {
				association = data.getSourceAssociation();
				if(association.isMyId(newAssociation.getOriginalAssociation().getID())) {
					if(data.getMissingConstraint() == missingConstraint) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean existsMissingConstraintForAssociation(GraphAssociation association) {
		for(ConstraintData constraint : this.missingConstraints) {
			if(constraint.getSourceAssociation().isMyId(association.getOriginalAssociation().getID()))
				return true;
		}
		
		return false;
	}
	
	public ConstraintContainer clone() {
		ConstraintContainer container = new ConstraintContainer();
		for(ConstraintData constraintData : this.missingConstraints) {
			container.addMissingConstraint(constraintData.clone());
		}
		return container;
	}
	
	private void addMissingConstraint(ConstraintData constraintData) {
			this.missingConstraints.add(constraintData);
	}
	
	public String toString() {
		String text = "\n";
		
		text += "\t[MC1 = ";
		for(ConstraintData data : this.missingConstraints) {
			if(data.getMissingConstraint() == MissingConstraint.MC1_2 || data.getMissingConstraint() == MissingConstraint.MC1_2_Inverse)
				text += data.getSourceNode().getName() + " | ";
		}
		
		text += "]";
		text += "[MC6 = ";
		for(ConstraintData data : this.missingConstraints) {
			if(data.getMissingConstraint() == MissingConstraint.MC6)
			text += data.getSourceNode().getName();
		}
		text += "]";
				
		return text;
	}
	
}
