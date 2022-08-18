package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import br.ufes.inf.nemo.ontoumltodb.util.MissingConstraint;

public class ConstraintData {

	private Node sourceNode;
	private GraphAssociation sourceAssociation;
	private NodeProperty propertyToFilter;
	private String filterValue;
	private MissingConstraint missingConstraint;
	
	public ConstraintData(Node sourceNode, GraphAssociation sourceAssociation, MissingConstraint missingConstraint) {
		this.sourceNode = sourceNode;
		this.sourceAssociation = sourceAssociation;
		this.missingConstraint = missingConstraint;
		this.propertyToFilter = null;
		this.filterValue = null;
	}
	
	public ConstraintData(Node sourceNode, GraphAssociation sourceAssociation, NodeProperty propertyToFilter, String filterValue, MissingConstraint missingConstraint) {
		this.sourceNode = sourceNode;
		this.sourceAssociation = sourceAssociation;
		this.missingConstraint = missingConstraint;
		this.propertyToFilter = propertyToFilter;
		this.filterValue = filterValue;
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
	
	public NodeProperty getPropertyToFilter() {
		return this.propertyToFilter;
	}
	
	public String getFilterValue() {
		return this.filterValue;
	}
	
	public ConstraintData clone() {
		return new ConstraintData(
				this.sourceNode,
				this.sourceAssociation,
				this.propertyToFilter,
				this.filterValue,
				this.missingConstraint);
	}
	
}
