package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import br.ufes.inf.nemo.ontoumltodb.util.MissingConstraint;

public class ConstraintData {

	private Node sourceNode;
	private GraphAssociation sourceAssociation;
	private MissingConstraint missingConstraint;
	
	public ConstraintData(Node sourceNode, GraphAssociation sourceAssociation, MissingConstraint missingConstraint) {
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
	
	public ConstraintData clone() {
		return new ConstraintData(
				this.sourceNode,
				this.sourceAssociation,
				this.missingConstraint);
	}
	
}
