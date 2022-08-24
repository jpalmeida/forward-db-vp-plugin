/**
 * An AssociationContainer is intended to group all the properties of a node. A
 * node is composed of a relationships set with other nodes. This interface
 * contains the necessary methods for handling node's associations.
 *
 * Author:
 */

package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.util.ElementType;

public class AssociationContainer {

	private Node parentNode; // Node to which the container belongs.
	private ArrayList<GraphAssociation> associations;

	public AssociationContainer(Node parentNode) {
		this.parentNode = parentNode;
		this.associations = new ArrayList<GraphAssociation>();
	}

	/**
	 * Adds a new association to the node. Each node has a list of its associations.
	 * In this way, a association is referenced by the origin and destination node,
	 * forming a bidirectional graph.
	 *
	 * @param association Association to be added.
	 */
	public void addAssociation(GraphAssociation association) {
		if(! existsAssociation(association))
			this.associations.add(association);
	}
	
	public boolean existsAssociation(GraphAssociation association) {
		for (GraphAssociation currentAssociation : associations) {
			if(currentAssociation.getID().equals(association.getID()))
				return true;
		}
		return false;
	}

	/**
	 * Returns the associations with the node.
	 *
	 * @return An list with all the associations that arrive and depart from the
	 *         respective node.
	 */
	public ArrayList<GraphAssociation> getAssociations() {
		ArrayList<GraphAssociation> result = new ArrayList<GraphAssociation>();
		for (GraphAssociation graphAssociation : associations) {
			if(graphAssociation.getElementType() == ElementType.ASSOCIATION)
				result.add(graphAssociation);
		}
		return result;
	}
	
	public GraphAssociation getAssociationWith(Node destinationNode) {
		for(GraphAssociation association : this.associations) {
			if(association.getNodeEndOf(this.parentNode).getName().equals(destinationNode.getName()))
				return association;
		}
		return null;
	}
	  
	/**
	 * Returns generalizations belonging to the node.
	 */
	public ArrayList<GraphGeneralization> getGeneralizations() {
		ArrayList<GraphGeneralization> result = new ArrayList<GraphGeneralization>();
		for (GraphAssociation graphAssociation : associations) {
			if(graphAssociation.getElementType() == ElementType.GENERALIZATION)
				result.add((GraphGeneralization)graphAssociation);
		}
		return result;
		//return this.generalizations;
	}

	/**
	 * Returns generalization sets belonging to the node, when the node is a
	 * generalization of some node.
	 */
	public ArrayList<GraphGeneralizationSet> getGeneralizationSets() {
		ArrayList<GraphGeneralizationSet> gSets = new ArrayList<GraphGeneralizationSet>();

	    for (GraphGeneralization generalization : getGeneralizations()) {
	    	if (generalization.isBelongGeneralizationSet()) {
		          gSets.add(generalization.getGeneralizationSet());
		      }
		} 
		return gSets;
	}
	
	/**
	 * Removes the association form the node. The association still exists in the
	 * graph.
	 * 
	 * @param association
	 */
	public void removeAssociation(GraphAssociation association) {
		int index = this.associations.indexOf(association);
		if (index != -1)
			this.associations.remove(index);
	}
	  
	/**
	 * Checks whether the current node is a specialist node of some generalization.
	 *
	 * @return True if the node is a specialization of another node, otherwise
	 *         false.
	 */
	public boolean isSpecialization() {
		for (GraphAssociation graphAssociation : associations) {
			if(graphAssociation.getElementType() == ElementType.GENERALIZATION) {
				if (((GraphGeneralization)graphAssociation).getSpecific().getID() == this.parentNode.getID())
					return true;
			}
		}
		return false;
	}
	
	public boolean hasMultipleInheritance() {
		int qtd = 0;
		for (GraphGeneralization generalization : getGeneralizations()) {
	    	if(generalization.getSpecific().getName().equals(parentNode.getName())) {
	    		qtd++;
	    	}
		}
		
		if(qtd > 1)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the current node has any specialization.
	 *
	 * @return True if the node has at last one specialization node, otherwise
	 *         false.
	 */
	public boolean hasSpecialization() {
		for (GraphAssociation graphAssociation : associations) {
			if(graphAssociation.getElementType() == ElementType.GENERALIZATION) {
				if (((GraphGeneralization)graphAssociation).getGeneral().getID() == this.parentNode.getID())
					return true;
			}
		}
		return false;
	}	
	
	/**
	 * Returns all generalizations of the node.
	 * 
	 * @return
	 */
	public ArrayList<Node> getGeneralizationNodes() {
		ArrayList<Node> generalizations = new ArrayList<Node>();

		for (GraphAssociation graphAssociation : associations) {
			if(graphAssociation.getElementType() == ElementType.GENERALIZATION) {
				if (((GraphGeneralization)graphAssociation).getGeneral().getID() != this.parentNode.getID())
					generalizations.add(((GraphGeneralization)graphAssociation).getGeneral());
			}
		}
		return generalizations;
	}
	
	/**
	 * 
	 * Returns all specializations of the node.
	 * @return
	 */
	public ArrayList<Node> getSpecializationNodes() {
		ArrayList<Node> specializations = new ArrayList<Node>();

		for (GraphAssociation graphAssociation : associations) {
			if(graphAssociation.getElementType() == ElementType.GENERALIZATION) {
				if (((GraphGeneralization)graphAssociation).getSpecific().getID() != this.parentNode.getID())
					specializations.add(((GraphGeneralization)graphAssociation).getSpecific());
			}
		}
		return specializations;
	}

	/**
	 * Returns all associations formatted as a string.
	 */
	public String toString() {
		String msg = "";

		for (GraphAssociation association : getAssociations()) {
			msg += "\n\t" + association.toString();
		}

		for (GraphGeneralization graphGeneralization : getGeneralizations()) {
			msg += "\n\t" + graphGeneralization.toString();
		}
		return msg;
	}

}
