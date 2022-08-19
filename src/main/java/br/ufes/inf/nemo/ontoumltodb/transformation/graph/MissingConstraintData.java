package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import br.ufes.inf.nemo.ontoumltodb.util.MissingConstraint;

public class MissingConstraintData {

	private Node sourceNode;
	private GraphAssociation sourceAssociation;
	private MissingConstraint missingConstraint;
	
	public MissingConstraintData(Node sourceNode, GraphAssociation sourceAssociation, MissingConstraint missingConstraint) {
		this.sourceNode = sourceNode;
		this.sourceAssociation = sourceAssociation;
		this.missingConstraint = missingConstraint;
	}
	
	public Node getSourceNode() {
		return this.sourceNode;
	}
	
	public GraphAssociation getSourceAssociation() {
		return this.sourceAssociation;
	}
	
	public MissingConstraint getMissingConstraint() {
		return this.missingConstraint;
	}
	
	public MissingConstraintData clone() {
		return new MissingConstraintData(
				this.sourceNode,
				this.sourceAssociation,
				this.missingConstraint);
	}
	
	public String toString() {
		
		return "[" + this.missingConstraint.toString() + ": sourceNode: "+ sourceNode.getName() + " | " + sourceAssociation.toString() ;
	}
	
}
