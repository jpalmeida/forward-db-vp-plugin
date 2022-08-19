package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import br.ufes.inf.nemo.ontoumltodb.util.MissingConstraint;

public class MissingConstraintData {

	private Node sourceNode;
	private GraphAssociation sourceAssociation;
//	private NodeProperty propertyToFilter; // take from TraceTable
//	private String filterValue;
	private MissingConstraint missingConstraint;
	
	public MissingConstraintData(Node sourceNode, GraphAssociation sourceAssociation, MissingConstraint missingConstraint) {
		this.sourceNode = sourceNode;
		this.sourceAssociation = sourceAssociation;
		this.missingConstraint = missingConstraint;
//		this.propertyToFilter = null;
//		this.filterValue = null;
	}
	
//	public ConstraintData(Node sourceNode, GraphAssociation sourceAssociation, NodeProperty propertyToFilter, String filterValue, MissingConstraint missingConstraint) {
//		this.sourceNode = sourceNode;
//		this.sourceAssociation = sourceAssociation;
//		this.missingConstraint = missingConstraint;
//		this.propertyToFilter = propertyToFilter;
//		this.filterValue = filterValue;
//	}
	
	
	public Node getSourceNode() {
		return this.sourceNode;
	}
	
	public GraphAssociation getSourceAssociation() {
		return this.sourceAssociation;
	}
	
	public MissingConstraint getMissingConstraint() {
		return this.missingConstraint;
	}
	
//	public NodeProperty getPropertyToFilter() {
//		return this.propertyToFilter;
//	}
//	
//	public String getFilterValue() {
//		return this.filterValue;
//	}
	
	public MissingConstraintData clone() {
		return new MissingConstraintData(
				this.sourceNode,
				this.sourceAssociation,
//				this.propertyToFilter,
//				this.filterValue,
				this.missingConstraint);
	}
	
	public String toString() {
		
		return "[" + this.missingConstraint.toString() + ": sourceNode: "+ sourceNode.getName() + " | " + sourceAssociation.toString() ;
	}
	
}
