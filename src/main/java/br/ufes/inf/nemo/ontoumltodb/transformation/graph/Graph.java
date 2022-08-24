package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.ElementType;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class Graph {
	private ArrayList<Node> nodes;
	private ArrayList<GraphAssociation> associations;
	private ArrayList<GraphGeneralizationSet> generalizationSets;

	public Graph() {
		this.nodes = new ArrayList<Node>();
		this.associations = new ArrayList<GraphAssociation>();
		this.generalizationSets = new ArrayList<GraphGeneralizationSet>();
	}

	/**
	 * Adds a new node (class) on the graph.
	 *
	 * @param Node. Node to be added in the graph.
	 */
	public void addNode(Node newNode) {
		this.nodes.add(newNode);
	}

	/**
	 * Returns the node instance. If not find, returns null.
	 *
	 * @param id. The class identifier of the original model.
	 */
	public Node getNodeById(String id) {
		for (Node node : nodes) {
			if (node.getID().equals(id))
				return node;
		}
		return null;
	}
	
	/**
	 * Returns the node instance. If not find, returns null.
	 *
	 * @param name. The class name of the original model.
	 */
	public Node getNodeByName(String name) {
		for (Node node : nodes) {
			if (node.getName().equalsIgnoreCase((name) ))
				return node;
		}
		return null;
	}

	/**
	 * Returns all nodes of the graph. The vector returned is not the same one maintained by Graph.
	 *
	 * @return An array with all nodes.
	 */
	public ArrayList<Node> getNodes() {
		ArrayList<Node> result = new ArrayList<Node>();
		for (Node node : this.nodes) {
			result.add(node);
		}
		return result;
	}

	/**
	 * Adds a new association to the graph.
	 *
	 * @param association. The association to be stored.
	 */
	public void addAssociation(GraphAssociation association) {
		if (this.getAssociationByID(association.getID()) == null) {
			this.associations.add(association);
		}
	}

	/**
	 * Adds a new generalization to the graph.
	 *
	 * @param generalization. The generalization to be stored.
	 */
	public void addGeneralization(GraphGeneralization generalization) {
		if (this.getAssociationByID(generalization.getID()) == null)
			this.associations.add(generalization);
	}

	/**
	 * Adds a new generalization set to the graph.
	 *
	 * @param generalizationSet. The generalization set to be stored.
	 * 
	 * Deprecated
	 */
	public void addGeneralizationSet(GraphGeneralizationSet generalizationSet) {
		if (this.getGeneralizationSetByID(generalizationSet.getID()) == null) {
			this.generalizationSets.add(generalizationSet);
		}
	}

	/**
	 * Returns the association of the respective ID.
	 *
	 * @param id. Association ID.
	 */
	public GraphAssociation getAssociationByID(String id) {
		for (GraphAssociation graphAssociation : associations) {
			if (graphAssociation.getID().equals(id))
				return graphAssociation;
		}
		return null;
	}

	/**
	 * Returns the Generalization Set of the respective ID.
	 *
	 * @param generalizationSet. The generalization set to be stored.
	 */
	public GraphGeneralizationSet getGeneralizationSetByID(String id) {
		for (GraphGeneralizationSet generalizationSet : this.generalizationSets) {
			if (generalizationSet.getID().equals(id))
				return generalizationSet;
		}
		return null;
	}
	
	/**
	 * Returns all generalization sets from the graph. The vector returned is not the same one maintained by Graph.
	 * @return
	 */
	public ArrayList<GraphGeneralizationSet> getGeneralizationSets() {
		ArrayList<GraphGeneralizationSet> result = new ArrayList<GraphGeneralizationSet>();
		for (GraphGeneralizationSet generalizationSet : this.generalizationSets) {
			result.add(generalizationSet);
		}
		return result;
	}

	/**
	 * Returns all associations of the graph. The vector returned is not the same one maintained by Graph.
	 *
	 * @return An array with all associations.
	 */
	public ArrayList<GraphAssociation> getAssociations() {
		ArrayList<GraphAssociation> result = new ArrayList<GraphAssociation>();
		for (GraphAssociation graphAssociation : this.associations) {
			result.add(graphAssociation);
		}
		return result;
	}
	
	public boolean existsA(GraphAssociation association) {
		for(Node node : this.nodes) {
			if(node.existsAssociation(association)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns all associations of one specific type from the graph.
	 *
	 * @return An array with all associations.
	 */
	public ArrayList<GraphAssociation> getAssociations(ElementType elementType) {
		ArrayList<GraphAssociation> result = new ArrayList<GraphAssociation>();
		for (GraphAssociation element : associations) {
			if(element.getElementType() == elementType) {
				result.add(element);
			}
		}
		return result;
	}

	/**
	 * Removes an node and its associations of the graph.
	 *
	 * @param node. Node to be removed.
	 */
	public void removeNodes(ArrayList<Node> nodes) {
		for (Node node : nodes) {
			if(existsNodeName(node.getName()))
				removeNode(node);
		}
	}

	/**
	 * Removes set of nodes and its associations of the graph.
	 *
	 * @param node. Node to be removed.
	 */
	public void removeNode(Node node) {
		int index = this.nodes.indexOf(node);		
		this.nodes.remove(index);

		GraphAssociation association;
		ArrayList<GraphAssociation> removeAssociations= node.getAssociations();
		while (!removeAssociations.isEmpty()) {
			association = removeAssociations.remove(0);
			index = this.associations.indexOf(association);
			this.associations.remove(index);
			association.disassociate();
		}

		GraphGeneralization generalization;
		ArrayList<GraphGeneralization> removeGeneralizations = node.getGeneralizations();
		while (!removeGeneralizations.isEmpty()) {
			generalization = removeGeneralizations.remove(0);
			index = this.associations.indexOf(generalization);
			this.associations.remove(index);
			generalization.disassociate();
		}
	}

	/**
	 * Removes the associations of the graph. The associations will be removed from
	 * the nodes.
	 *
	 * @param associations. Associations to be removed.
	 */
	public void removeAssociations(ArrayList<GraphAssociation> associations) {
		for (GraphAssociation association : associations) {
			this.removeAssociation(association);
		}
	}

	/**
	 * Removes the association of the graph. The association will be removed from
	 * the nodes.
	 *
	 * @param association. Association to be removed.
	 */
	public void removeAssociation(GraphAssociation association) {
		if (association instanceof GraphAssociation) {
			association.disassociate();
		}
		int index = this.associations.indexOf(association);
		if (index != -1) {
			this.associations.remove(index);
		}
	}
	
	/**
	 * Removes the generalization association of the graph (not remove the nodes). 
	 *
	 * @param generalization. Generalization to be removed.
	 */
	public void removeGeneralization(GraphGeneralization generalization) {
		if (generalization instanceof GraphGeneralization) {
			generalization.disassociate();
		}
		
		if(generalization.isBelongGeneralizationSet()) {
			generalization.getGeneralizationSet().removeGeneralzation(generalization);
		}
		
		int index = this.associations.indexOf(generalization);
		if (index != -1) {
			this.associations.remove(index);
		}
	}
	
	public void removeGeneralizationSet(GraphGeneralizationSet gs) {
		for(GraphGeneralization generalization: gs.getGeneralizations()) {
			generalization.setGeneralizationSet(null);
		}
		this.generalizationSets.remove(gs);
	}
	
	public boolean belongAnotherGS(Node node) {
		if(node.getGeneralizationSets().size() > 1) 
			return true;
		else return false;
	}
	
	public void removeEmptyGeneralizationSet() {
		int index = 0;
		GraphGeneralizationSet gs;
		
		while(index < this.generalizationSets.size()) {
			gs = this.generalizationSets.get(index);
			
			if(gs.isEmpty()) {
				this.generalizationSets.remove(index);
			}
			else {
				index++;
			}
		}
	}
	
	/**
	 * Clone the graph and his elements. All the elements are the same, including the IDs.
	 */
	public Graph clone() {
		Graph newGraph = new Graph();
		Node newSourceNode, newTargetNode;
		ArrayList<GraphGeneralization> newGeneralizations;
		GraphGeneralization generalizationAux;
		
		for (Node node : this.nodes) {
			newGraph.addNode(node.clone());
		}
		
		for(GraphAssociation association : this.associations) {
			newSourceNode = newGraph.getNodeById(association.getSourceNode().getID());
			newTargetNode = newGraph.getNodeById(association.getTargetNode().getID());
			if(association.getElementType() == ElementType.ASSOCIATION)
				newGraph.addAssociation(association.clone(null, newSourceNode, newTargetNode));
			else if(association.getElementType() == ElementType.GENERALIZATION) {
				newGraph.addAssociation(((GraphGeneralization)association).clone(newSourceNode, newTargetNode));
			}
		}
		
		for(GraphGeneralizationSet gs : this.generalizationSets) {
			newGeneralizations = new ArrayList<GraphGeneralization>();
			for(GraphGeneralization generalization : gs.getGeneralizations()) {
				generalizationAux = (GraphGeneralization) newGraph.getAssociationByID(generalization.getID());
				newGeneralizations.add( generalizationAux );
			}
			newGraph.addGeneralizationSet(gs.clone(newGeneralizations));
		}
		
		return newGraph;
	}
	
	public Node getNodeEndOf(GraphAssociation association, Node node) {
		if(association.getTargetNode().isMyId(node.getID()))
			return association.getSourceNode();
		else return association.getTargetNode();
	}

	/**
	 * Returns the Super Nodes of the current node;
	 * @param currentNode
	 * @return
	 */
	public ArrayList<Node> getSuperNodes(Node currentNode){
		ArrayList<Node> superNodes = new ArrayList<Node>();
		putSuperNodes(currentNode, superNodes);
		return superNodes;
	}
	
	private void putSuperNodes(Node currentNode, ArrayList<Node> superNodes) {
		Node superNode;
		for(GraphGeneralization generalization : currentNode.getGeneralizations()) {
			superNode = generalization.getGeneral();
			// The generalization association also exists in the superNode. So, 
			// if the currentNode is NOT the superNode, then add the node as superNode.
			if(!currentNode.getID().equals(superNode.getID()))
				superNodes.add( superNode);
		}
	}
	
	public boolean isSubNodeOf(Node currentNode, Node subNode) {
		Node evaluatedNode;
		boolean result = false;
		
		for(GraphGeneralization generalization : currentNode.getGeneralizations()) {
			evaluatedNode = generalization.getSpecific();
			//The current node can be a subNode of some generalization
			if(!currentNode.isMyId(evaluatedNode.getID())) {
				if(evaluatedNode.isMyId(subNode.getID()))
					return true;
				result = isSubNodeOf(evaluatedNode, subNode);
				if(result)
					return true;
			}
		}
		return false;
	}
	
	public boolean isSuperNodeOf(Node currentNode, Node superNode) {
		if(currentNode.getName().equals(superNode.getName())) {
			return true;
		}
		else {
			for(Node node : getSuperNodes(currentNode)) {
				return isSuperNodeOf(node, superNode);
			}
			return false;
		}
	}
	
	/**
	 * Returns the Root Nodes of the current node ID;
	 * @param currentNode
	 * @return
	 */
	public ArrayList<Node> getRootNodes(String nodeId){
		ArrayList<Node> rootNodes = new ArrayList<Node>();
		Node currentNode = getNodeById(nodeId);
		if(currentNode.getGeneralizations().size() > 0)
			getSupperRootNodes(currentNode, rootNodes);
		else rootNodes.add(currentNode);
		return rootNodes;
	}
	
	private void getSupperRootNodes(Node currentNode, ArrayList<Node> rootNodes ){
		
		if(isAllGeneralNodesAreTheSame(currentNode)) {
			rootNodes.add(currentNode);
		}
		else{
			for(GraphGeneralization generalization : currentNode.getGeneralizations()) {
				Node superNode = generalization.getGeneral();
				// The generalization association exists in the superNode and subNode.
				if(!currentNode.getID().equals(superNode.getID()))
					getSupperRootNodes(superNode, rootNodes);
			}
		}
	}
	
	private boolean isAllGeneralNodesAreTheSame(Node currentNode) {
		for(GraphGeneralization generalization : currentNode.getGeneralizations()) {
			if( ! generalization.getGeneral().isMyId(currentNode.getID()) )
				return false;
		}
		return true;
	}
	
	public boolean existsNodeName(String name) {
		for(Node node : this.nodes) {
			if(node.getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	
	/**
	 * Returns all Root Nodes of the graph;
	 * @param currentNode
	 * @return
	 */
	public ArrayList<Node> getRootNodes(){
		ArrayList<Node> rootNodes = new ArrayList<Node>();
		
		for(Node node : this.nodes) {
			if(node.getGeneralizations().size() > 0) {
				if(!existsSuperNodeFor(node)) {
					rootNodes.add(node);
				}
			}
		}
		return rootNodes;
	}
	
	public void transformEnumAssociationsInProperties() {
		for(Node node : this.nodes) {
			if(node.getStereotype() == Stereotype.ENUMERATION) {
				putAsEnumeration(node);
			}
		}
	}
	
	public void transform1To1AssociationIn1To01() {
		for(GraphAssociation association : this.associations) {
			if(association.getSourceCardinality() == Cardinality.C1 && association.getTargetCardinality() == Cardinality.C1)
				association.setTargetCardinality(Cardinality.C0_1);
		}
	}
	
	public void transformDatatypeAssociationInProperties() {
		for(Node node : this.nodes) {
			if(node.getStereotype() == Stereotype.DATATYPE) {
				putAsProperty(node);
			}
		}
	}
	
	private void putAsProperty(Node node) {
		Node relatedNode;
		NodeProperty newProperty;
		String name;
		boolean acceptNull, isMultivalued;
		
		for(GraphAssociation association : node.getAssociations()) {
			relatedNode = association.getNodeEndOf(node);
			
			name = association.getName();
			name = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
			
			if(		association.getCardinalityBeginOf(node) == Cardinality.C0_N ||
					association.getCardinalityBeginOf(node) == Cardinality.C1_N) 
				isMultivalued = true;
			else isMultivalued = false;
			
			if(		association.getCardinalityEndOf(node) == Cardinality.C0_1 ||
					association.getCardinalityEndOf(node) == Cardinality.C0_N)
				acceptNull = true;
			else acceptNull = false;
			
			newProperty = new NodeProperty(
					relatedNode,
					Increment.getNextS(),
					name, 
					node.getName(),
					acceptNull,
					isMultivalued
					);
			
			relatedNode.addProperty(newProperty);
			removeAssociation(association);
		}
	}
	
	private void putAsEnumeration(Node node) {
		ArrayList<String> literals = node.getLiterals();
		Node nodeAffected;
		NodePropertyEnumeration enumProperty;
		
		for(GraphAssociation association : node.getAssociations()) {
			nodeAffected = association.getNodeEndOf(node);
			enumProperty = new NodePropertyEnumeration(
					nodeAffected, 
					Increment.getNextS(), 
					node.getName(), 
					"enum", 
					association.isNulableNode(node), 
					association.isMultivaluedNode(nodeAffected));
			for(String literal : literals) {
				enumProperty.addValue(literal);
			}
			nodeAffected.addProperty(enumProperty);
			removeAssociation(association);
		}
	}
	
	private boolean existsSuperNodeFor(Node node) {
		for(GraphGeneralization generalization : node.getGeneralizations()) {
			if( ! generalization.getGeneral().isMyId(node.getID()) )
				return true;
		}
		return false;
	}
	
	public String getNodesName() {
		ArrayList<Node> result = new ArrayList<Node>();
		for (Node node : this.nodes) {
			result.add(node);
		}

		Collections.sort(result, new Comparator<Node>() {
			public int compare(Node node1, Node node2) {
				return node1.getName().compareTo(node2.getName());
			}
		});
		
		String msg = "";

		for (Node node : result) {
			msg += "\n" + node.getName();
		}
		
		return msg;
	}
	
	public void showNode(String name) {
		for (Node node : this.nodes) {
			if(name.equals(node.getName()))
				System.out.println(node.toString());
		}
	}

	public String toString() {
		ArrayList<Node> result = new ArrayList<Node>();
		for (Node node : this.nodes) {
			result.add(node);
		}

		Collections.sort(result, new Comparator<Node>() {
			public int compare(Node node1, Node node2) {
				return node1.getName().compareTo(node2.getName());
			}
		});
		
		String msg = " * Nodes *";

		for (Node node : result) {
			msg += "\n" + node.toString();
		}
		
		if(generalizationSets.size() > 0) {
			msg += "\n * Generalization Set *";
			for (GraphGeneralizationSet generalizationSet : generalizationSets) {
				msg += "\n" + generalizationSet.toString();
			}
		}
		
		return msg;
	}

}

