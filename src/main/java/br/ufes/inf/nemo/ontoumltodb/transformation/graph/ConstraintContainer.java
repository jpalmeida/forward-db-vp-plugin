package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import java.util.ArrayList;

public class ConstraintContainer {
	
//	private boolean necessaryGenerateMC3_4Constraint;
	
	private ArrayList<ConstraintData> mc1_2ConstraintData;
	private ArrayList<ConstraintData> mc6ConstraintData;

	
	public ConstraintContainer() {
//		this.necessaryGenerateMC3_4Constraint = false;
		mc1_2ConstraintData = new ArrayList<ConstraintData>();
		mc6ConstraintData = new ArrayList<ConstraintData>();
	}
	
	public boolean isNecessaryGenerateMC1_2Constraint() {
		if(this.mc1_2ConstraintData.isEmpty())
			return false;
		else return true;
	}
	
//	public void setNecessaryGenerateMC3_4Constraint(boolean flag) {
//		this.necessaryGenerateMC3_4Constraint = flag;
//	}
//	
//	public boolean isNecessaryGenerateMC3_4Constraint() {
//		return this.necessaryGenerateMC3_4Constraint;
//	}
	
	public boolean isNecessaryGenerateMC6Constraint() {
		if(this.mc6ConstraintData.isEmpty())
			return false;
		else return true;
	}
	
	public void addSourceNodeRelatedToMC1_2Constraint(Node sourceNode, GraphAssociation association) {
		if(!isSameOriginalAssociation(association, mc1_2ConstraintData ))
			this.mc1_2ConstraintData.add(new ConstraintData(sourceNode, association));
	}
	
	public void addSourceNodeRelatedToMC1_2Constraint(ConstraintData constraintData) {
		this.mc1_2ConstraintData.add(constraintData);
	}
	
	public ArrayList<ConstraintData> getMC1_2ConstraintData() {
		return this.mc1_2ConstraintData;
	}
	
	public void addSourceNodeRelatedToMC6Constraint(Node sourceNode, GraphAssociation association) {
		if(!isSameOriginalAssociation(association, mc6ConstraintData ))
			this.mc6ConstraintData.add(new ConstraintData(sourceNode, association));
	}
	
	public void addSourceNodeRelatedToMC6Constraint(ConstraintData constraintData) {
		this.mc6ConstraintData.add(constraintData);
	}
	
	public ArrayList<ConstraintData> getMC6ConstraintData() {
		return this.mc6ConstraintData;
	}
	
	private boolean isSameOriginalAssociation(GraphAssociation newAssociation, ArrayList<ConstraintData> constraints) {
		GraphAssociation association;
		for(ConstraintData data : constraints) {
			if(newAssociation.getOriginalAssociation() != null) {
				association = data.getAssociation();
				if(association.isMyId(newAssociation.getOriginalAssociation().getID()))
					return true;
			}
		}
		
		return false;
	}
	
	public ConstraintContainer clone() {
		ConstraintContainer container = new ConstraintContainer();

//		container.setNecessaryGenerateMC1_2Constraint(this.isNecessaryGenerateMC1_2Constraint());
//		container.setNecessaryGenerateMC3_4Constraint(this.isNecessaryGenerateMC3_4Constraint());
//		container.setNecessaryGenerateMC6Constraint(this.isNecessaryGenerateMC6Constraint());
		
		for(ConstraintData constraintData : this.mc1_2ConstraintData) {
			container.addSourceNodeRelatedToMC1_2Constraint(constraintData);
		}
		
		for(ConstraintData constraintData : this.mc6ConstraintData) {
			container.addSourceNodeRelatedToMC6Constraint(constraintData);
		}

		return container;
	}
	
	public String toString() {
		String text = "\n";
		
		text += "[MC1 = ";
		for(ConstraintData data : this.mc1_2ConstraintData) {
			text += data.getSourceNode().getName();
		}
		
		text += "]";
		text += "[MC6 = ";
		for(ConstraintData data : this.mc6ConstraintData) {
			text += data.getSourceNode().getName();
		}
		
		text += "]";
				
		return text;
	}
	
}
