package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

public class ConstraintData {

	private Node sourceNode;
	private GraphAssociation sourceAssociation;
	
	public ConstraintData(Node sourceNode, GraphAssociation sourceAssociation) {
		this.sourceNode = sourceNode;
		this.sourceAssociation = sourceAssociation;
	}
	
	public Node getSourceNode() {
		return this.sourceNode;
	}
	
	public GraphAssociation getAssociation() {
		return this.sourceAssociation;
	}
	
}
