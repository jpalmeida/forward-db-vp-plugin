/**
 * Class responsible for handling a generalization.
 *
 * Author:
 */

package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import br.ufes.inf.nemo.ontoumltodb.util.ElementType;

public class GraphGeneralization extends GraphAssociation {
	private GraphGeneralizationSet belongToGS;

	public GraphGeneralization(String id, Node generalizationNode, Node specializationNode) {
		super(id, id, "unnamed", ElementType.GENERALIZATION, generalizationNode, null, specializationNode, null);
		this.belongToGS = null;
	}
	
	public GraphGeneralization(String id, String originalId, Node generalizationNode, Node specializationNode) {
		super(id, originalId, "unnamed", ElementType.GENERALIZATION, generalizationNode, null, specializationNode, null);
		this.belongToGS = null;
	}

	/**
	 * Returns the generalization node (superclass).
	 *
	 * @return Generalization.
	 */
	public Node getGeneral() {
		return getSourceNode();// this.generalizationNode;
	}

	/**
	 * Returns the specialization node (subclass).
	 *
	 * @return Specialization node.
	 */
	public Node getSpecific() {
		return getTargetNode();// this.specializationNode;
	}

	/**
	 * Tells which set of generalizations the generalization belongs to.
	 *
	 * @param gs Generalization set to be associated with generalization.
	 */
	public void setBelongGeneralizationSet(GraphGeneralizationSet gs) {
		this.belongToGS = gs;
	}

	/**
	 * Returns which set of generalizations the generalization belongs to.
	 */
	public GraphGeneralizationSet getBelongGeneralizationSet() {
		return this.belongToGS;
	}

	/**
	 * Tells whether the generalization belongs to a generalization set.
	 */
	public boolean isBelongGeneralizationSet() {
		if (this.belongToGS == null)
			return false;
		else
			return true;
	}
	
	public GraphGeneralization clone(Node newGeneralizationNode, Node newSpecializationNode) {
		return new GraphGeneralization(getID(), getOriginalId(), newGeneralizationNode, newSpecializationNode);
	}

	public String toString() {
			return "GENERALIZATION: " + getSourceNode().getName() + " <- " + getTargetNode().getName();// + "  id: "+ getAssociationID();
	}
}