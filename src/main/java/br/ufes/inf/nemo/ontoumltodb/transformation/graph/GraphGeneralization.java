/**
 * Class responsible for handling a generalization.
 *
 * Author:
 */

package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import br.ufes.inf.nemo.ontoumltodb.util.ElementType;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.IndexType;

public class GraphGeneralization extends GraphAssociation {
	private GraphGeneralizationSet generalizationSet;

	public GraphGeneralization(String id, Node generalizationNode, Node specializationNode) {
		super(id, id, "unnamed", ElementType.GENERALIZATION, generalizationNode, null, specializationNode, null);
		this.generalizationSet = null;
	}
	
	public GraphGeneralization(String id, String originalId, Node generalizationNode, Node specializationNode) {
		super(id, originalId, "unnamed", ElementType.GENERALIZATION, generalizationNode, null, specializationNode, null);
		this.generalizationSet = null;
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
	public void setGeneralizationSet(GraphGeneralizationSet gs) {
		this.generalizationSet = gs;
	}

	/**
	 * Returns which set of generalizations the generalization belongs to.
	 */
	public GraphGeneralizationSet getGeneralizationSet() {
		return this.generalizationSet;
	}

	/**
	 * Tells whether the generalization belongs to a generalization set.
	 */
	public boolean isBelongGeneralizationSet() {
		if (this.generalizationSet == null)
			return false;
		else
			return true;
	}
	
	public NodeProperty getPropertyRelated() {
		NodeProperty newProperty = new NodeProperty(
				getGeneral(), 
				Increment.getNextS(),
				"is" + getSpecific().getName(), 
				"boolean", 
				false, 
				false);
		newProperty.setIdentifyOtherClass(true);
		newProperty.setDefaultValue(false);
		newProperty.setIndexType(IndexType.INDEX);
		newProperty.setGeneratedFromTransformationProcess(true);
		
		return newProperty;
	}
	
	public Object getValueRelated() {
		if(!isBelongGeneralizationSet()) {
			return Boolean.TRUE;
		}
		else {
			return getSpecific().getName().toUpperCase();
		}
	}
	
	public GraphGeneralization clone(Node newGeneralizationNode, Node newSpecializationNode) {
		return new GraphGeneralization(getID(), getOriginalId(), newGeneralizationNode, newSpecializationNode);
	}

	public String toString() {
			return "GENERALIZATION: " + getSourceNode().getName() + " <- " + getTargetNode().getName();// + "  id: "+ getAssociationID();
	}
}