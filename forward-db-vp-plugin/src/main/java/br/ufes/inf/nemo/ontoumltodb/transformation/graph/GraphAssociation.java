/**
 * Class responsible for storing association data between the class.
 *
 * Author:
 */

package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.ElementType;

public class GraphAssociation extends Element {

	private Node sourceNode;
	private Node targetNode;
	private Cardinality sourceCardinality;
	private Cardinality targetCardinality;
	private GraphAssociation originalAssociation;
	
	private String nodeNameRemoved; // this is important when there is a name collision in the FK name propagation
	// process.
	private boolean derivedFromGeneralization;

	public GraphAssociation(String id, String name, Node sourceNode, Cardinality sourceCardinality, Node targetNode,
			Cardinality targetCardinality) {
		super(id, id, name, ElementType.ASSOCIATION);
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.sourceCardinality = sourceCardinality;
		this.targetCardinality = targetCardinality;
		this.nodeNameRemoved = null;
		this.derivedFromGeneralization = false;
		this.sourceNode.addAssociation(this);
		this.targetNode.addAssociation(this);
		this.originalAssociation = this;
	}
	
	public GraphAssociation(String id, String name, ElementType elementType, Node sourceNode, Cardinality sourceCardinality, 
			Node targetNode, Cardinality targetCardinality) {
		super(id, id, name, elementType);
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.sourceCardinality = sourceCardinality;
		this.targetCardinality = targetCardinality;
		this.nodeNameRemoved = null;
		this.derivedFromGeneralization = false;
		this.sourceNode.addAssociation(this);
		this.targetNode.addAssociation(this);
		this.originalAssociation = this;
	}
	
	public GraphAssociation(String id, String originalId, String name, Node sourceNode, Cardinality sourceCardinality, Node targetNode,
			Cardinality targetCardinality) {
		super(id, originalId, name, ElementType.ASSOCIATION);
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.sourceCardinality = sourceCardinality;
		this.targetCardinality = targetCardinality;
		this.nodeNameRemoved = null;
		this.derivedFromGeneralization = false;
		this.sourceNode.addAssociation(this);
		this.targetNode.addAssociation(this);
		this.originalAssociation = this;
	}
	
	public GraphAssociation(String id, String originalId, String name, ElementType elementType, Node sourceNode, Cardinality sourceCardinality, 
			Node targetNode, Cardinality targetCardinality) {
		super(id, originalId, name, elementType);
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.sourceCardinality = sourceCardinality;
		this.targetCardinality = targetCardinality;
		this.nodeNameRemoved = null;
		this.derivedFromGeneralization = false;
		this.sourceNode.addAssociation(this);
		this.targetNode.addAssociation(this);
		this.originalAssociation = null;
	}

	/**
	 * Returns the source node.
	 *
	 * @return Node. The source node.
	 */
	public Node getSourceNode() {
		return this.sourceNode;
	}

	/**
	 * Indicates the source node.
	 *
	 * @param sourceNode. Association source node.
	 */
	public void setSourceNode(Node sourceNode) {
		this.sourceNode.removeAssociation(this);
		this.sourceNode = sourceNode;
		this.sourceNode.addAssociation(this);
	}

	/**
	 * Returns the target node.
	 *
	 * @return Association target node.
	 */
	public Node getTargetNode() {
		return this.targetNode;
	}

	/**
	 * Indicates the target node.
	 *
	 * @param targetNode. Association target node.
	 */
	public void setTargetNode(Node targetNode) {
		this.targetNode.removeAssociation(this);
		this.targetNode = targetNode;
		this.targetNode.addAssociation(this);
	}

	/**
	 * Returns the source cardinality.
	 *
	 * @return A Cardinality type with the source cardinality.
	 */
	public Cardinality getSourceCardinality() {
		return this.sourceCardinality;
	}

	/**
	 * Indicates the source cardinality.
	 *
	 * @param sourceCardinality. The source cardinality of the association.
	 */
	public void setSourceCardinality(Cardinality sourceCardinality) {
		this.sourceCardinality = sourceCardinality;
	}

	/**
	 * Returns the target cardinality.
	 *
	 * @return A Cardinality type with the target cardinality.
	 */
	public Cardinality getTargetCardinality() {
		return this.targetCardinality;
	}

	/**
	 * Indicates the target cardinality.
	 *
	 * @param targetCardinality. The target cardinality of the association.
	 */
	public void setTargetCardinality(Cardinality targetCardinality) {
		this.targetCardinality = targetCardinality;
	}

	/**
	 * Clone the association by referencing the current nodes.
	 */
	public GraphAssociation clone(String newId, Node newSourceNode, Node newTargetNode) {
		GraphAssociation newAssociation;
		newAssociation = new GraphAssociation(
						newId != null ? newId : getID(), 
						getOriginalId(),
						getName(),
						newSourceNode != null ? newSourceNode : sourceNode,
						this.sourceCardinality, 
						newTargetNode != null ? newTargetNode : targetNode, 
						this.targetCardinality);
		newAssociation.originalAssociation = this.originalAssociation;
		newAssociation.setNodeNameRemoved(getNodeNameRemoved());
		return newAssociation;
	}

	/**
	 * Remove the association from the source and target node.
	 *
	 * @param node Node to be checked for its existence in the association.
	 */
	public void disassociate() {
		this.sourceNode.removeAssociation(this);
		this.targetNode.removeAssociation(this);
	}

	/**
	 * Checks if the cardinality with node is low (1; 0..1).
	 *
	 * @param node. Side of the relationship with the node to be evaluated.
	 */
	public boolean isLowCardinalityOfNode(Node node) {
		if (this.sourceNode == node
				&& (this.sourceCardinality == Cardinality.C0_1 || this.sourceCardinality == Cardinality.C1)) {
			return true;
		}

		if (this.targetNode == node
				&& (this.targetCardinality == Cardinality.C0_1 || this.targetCardinality == Cardinality.C1)) {
			return true;
		}
		return false;
	}
	
	public boolean isNulableNode(Node node) {
		if (this.sourceNode == node
				&& (this.sourceCardinality == Cardinality.C0_1 || this.sourceCardinality == Cardinality.C0_N)) {
			return true;
		}

		if (this.targetNode == node
				&& (this.targetCardinality == Cardinality.C0_1 || this.targetCardinality == Cardinality.C0_N)) {
			return true;
		}
		return false;
	}
	
	public boolean isMultivaluedNode(Node node) {
		if (this.sourceNode == node
				&& (this.sourceCardinality == Cardinality.C0_N || this.sourceCardinality == Cardinality.C1_N)) {
			return true;
		}

		if (this.targetNode == node
				&& (this.targetCardinality == Cardinality.C0_N || this.targetCardinality == Cardinality.C1_N)) {
			return true;
		}
		return false;
	}

	/**
	 * Informs the name of the Node that was removed.
	 * 
	 * @param nodeName
	 */
	public void setNodeNameRemoved(String nodeName) {
		this.nodeNameRemoved = nodeName;
	}

	/**
	 * Return the name of the Node removed.
	 */
	public String getNodeNameRemoved() {
		return this.nodeNameRemoved;
	}

	/**
	 * Informs if the association was created from a generalization.
	 * 
	 * @param falg
	 */
	public void setDerivedFromGeneralization(boolean falg) {
		this.derivedFromGeneralization = falg;
	}

	/**
	 * Returns if the association was created from a generalization.
	 * 
	 * @returns
	 */
	public boolean isDerivedFromGeneralization() {
		return this.derivedFromGeneralization;
	}
	
	/**
	 * Informs the original association of the source conceptual model (original graph).
	 *  
	 * @param originalAssociation
	 */
	public void setOriginalAssociation(GraphAssociation originalAssociation) {
		if(originalAssociation == null)
			return;
		
		if(originalAssociation.getOriginalAssociation() == null)
			this.originalAssociation = originalAssociation;
		else this.originalAssociation = originalAssociation.getOriginalAssociation();
	}
	
	/**
	 * Returns the original associations of the input model;
	 * @return
	 */
	public GraphAssociation getOriginalAssociation() {
		return this.originalAssociation;
	}
	
	/**
	 * Returns the node on the other side of the association.
	 * @param currentNode
	 * @return
	 */
	public Node getNodeEndOf(Node currentNode) {
		if(sourceNode.isMyId(currentNode.getID()))
			return targetNode;
		else return sourceNode;
	}
	
	public Cardinality getCardinalityEndOf(Node node) {
		if(sourceNode.isMyId(node.getID()))
			return targetCardinality;
		else return sourceCardinality;
	}
	
	public Cardinality getCardinalityBeginOf(Node node) {
		if(sourceNode.isMyId(node.getID()))
			return sourceCardinality;
		else return targetCardinality;
	}
	

	/**
	 * Returns the association formatted as string;
	 */
	public String toString() {
		String text = "";
		
		text = getElementType().toString().toUpperCase()+": " +this.sourceNode.getName() + " (" + this.sourceCardinality + " - " + this.targetCardinality + ") " +	this.targetNode.getName();
		
//		if(originalAssociation != null) {
//			text += "\n\t\tORIGINAL: " + originalAssociation.getElementType().toString().toUpperCase()+": " + originalAssociation.sourceNode.getName() + " (" + originalAssociation.sourceCardinality + " - " + originalAssociation.targetCardinality + ") " +	originalAssociation.targetNode.getName();
//		}
		
		return text;
		//return getElementType().toString().toUpperCase()+": " +this.sourceNode.getName() + " (" + this.sourceCardinality + " - " + this.targetCardinality + ") " +	this.targetNode.getName();// +"  ID: "+ this.getAssociationID();
	}
}
