/**
 * A node represents the existence of a class in the OntoUML model, its properties and
 * associations. A node is an composition of properties and associations, therefore,
 * the node serves as an interface for manipulating properties and associations. The
 * node has the capacity to tell which nodes it has been transformed to.
 *
 * Author: 
 */

package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.util.ElementType;
import br.ufes.inf.nemo.ontoumltodb.util.MissingConstraint;
import br.ufes.inf.nemo.ontoumltodb.util.NodeOrigin;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class Node extends Element {

	private Stereotype stereotype;
	private PropertyContainer propertyContainer;
	private LiteralContainer literalContainer;
	private AssociationContainer associationContainer;
	private ConstraintContainer constraintContainer;
	private NodeOrigin nodeOrigin;

	public Node(String id, String name, Stereotype stereotype) {
		super(id, id, name, ElementType.CLASS);
		inicialize(stereotype, NodeOrigin.UNDEFINED);
		
	}
	
	public Node(String id, String name, Stereotype stereotype, NodeOrigin nodeOrigin) {
		super(id, id, name, ElementType.CLASS);
		inicialize(stereotype, nodeOrigin);
	}
	
//	public Node(String id, String originalId, String name, Stereotype stereotype) {
//		super(id, originalId, name, ElementType.CLASS);
//		inicialize(stereotype, NodeOrigin.UNDEFINED);
//	}
	
	public Node(String id, String originalId, String name, Stereotype stereotype, NodeOrigin nodeOrigin) {
		super(id, originalId, name, ElementType.CLASS);
		inicialize(stereotype, nodeOrigin);
	}
	
	private void inicialize(Stereotype stereotype, NodeOrigin nodeOrigin) {
		this.stereotype = stereotype;
		this.nodeOrigin = nodeOrigin;
		
		this.propertyContainer = new PropertyContainer();
		this.literalContainer = new LiteralContainer();
		this.associationContainer = new AssociationContainer(this);
		this.constraintContainer = new ConstraintContainer();
	}

	/**
	 * Returns the stereotype name.
	 *
	 * @return The name of the stereotype.
	 */
	public Stereotype getStereotype() {
		return this.stereotype;
	}
	
	public void setStereotype(Stereotype newStereotype) {
		this.stereotype = newStereotype;
	}

	/**
	 * Informs the node's properties container.
	 *
	 * @param container Container to be put on the node.
	 */
	private void setPropertyContainer(PropertyContainer container) {
		this.propertyContainer = container;
	}
	
	/**
	 * Informs if the node origin. 
	 * If FROM_MODE, the node is originate from the source model. 
	 * If FROM_ATTRIBUTE, the node is originate from an attribute (the transformation process create it). 
	 * if FROM_GENERALIZATION_SET, the node is originate from a generalization set (the transformation process create it); 
	 *  
	 * @return
	 */
	public NodeOrigin getNodeOrigin() {
		return this.nodeOrigin;
	}	

	/**
	 * Creates a new node with the same properties values.
	 *
	 * @return A new node identical to the current one.
	 */
	public Node clone() {
		Node newNode = new Node(getID(), getOriginalId(),getName(), this.stereotype, this.nodeOrigin);
		newNode.setPostion(getPositionX(), getPositionY(), getHeight(), getHeight());
		newNode.setPropertyContainer(this.propertyContainer.clone(newNode));
		newNode.setConstraintContainer(this.constraintContainer.clone());
		return newNode;
	}

	// ---------------------------------------------------------------------------------------
	// --- The methods below are intended to manipulate the attributes
	// (PropertyContainer)
	// ---------------------------------------------------------------------------------------

	public void addProperty(NodeProperty property) {
		this.propertyContainer.addProperty(property);
	}

	public void addProperties(ArrayList<NodeProperty> properties) {
		this.propertyContainer.addProperties(properties);
	}

	public void addPropertyAt(int index, NodeProperty property) {
		this.propertyContainer.addPropertyAt(index, property);
	}

	public void addPropertiesAt(int index, ArrayList<NodeProperty> properties) {
		this.propertyContainer.addPropertiesAt(index, properties);
	}

	public ArrayList<NodeProperty> getProperties() {
		return this.propertyContainer.getProperties();
	}

	public PropertyContainer clonePropertyContainer(Node newNode) {
		return this.propertyContainer.clone(newNode);
	}

	public void removeProperty(String id) {
		this.propertyContainer.removeProperty(id);
	}

	public NodeProperty getPrimaryKey() {
		return this.propertyContainer.getPrimaryKey();
	}

	public String getPKName() {
		return this.propertyContainer.getPKName();
	}

	public boolean existsPropertyName(String propertyName) {
		return this.propertyContainer.existsPropertyName(propertyName);
	}

	public boolean existsProperty(NodeProperty property) {
		return this.propertyContainer.existsProperty(property);
	}

	public NodeProperty getFKRelatedOfNodeID(String id) {
		return this.propertyContainer.getFKRelatedOfNodeID(id);
	}
	
	public NodeProperty getPropertyById(String id) {
		return this.propertyContainer.getPropertyById(id);
	}
	
	public NodeProperty getPropertyByName(String name) {
		return this.propertyContainer.getPropertyByName(name);
	}
	
	public NodeProperty getPropertyByOriginalId(String originalId) {
		return this.propertyContainer.getPropertyByOriginalId(originalId);
	}
	
	public NodeProperty getPropertyByOriginalName(String name) {
		return this.propertyContainer.getPropertyByOriginalName(name);
	}

	// ---------------------------------------------------------------------------------------
	// --- The methods below are intended to manipulate the associations
	// (AssociationContainer)
	// ---------------------------------------------------------------------------------------

	public ArrayList<GraphGeneralization> getGeneralizations() {
		return this.associationContainer.getGeneralizations();
	}

	public ArrayList<GraphGeneralizationSet> getGeneralizationSets() {
		return this.associationContainer.getGeneralizationSets();
	}

	public void addAssociation(GraphAssociation association) {
		this.associationContainer.addAssociation(association);
	}

	public ArrayList<GraphAssociation> getAssociations() {
		return this.associationContainer.getAssociations();
	}

	public boolean isSpecialization() {
		return this.associationContainer.isSpecialization();
	}

	public boolean hasSpecialization() {
		return this.associationContainer.hasSpecialization();
	}

	public void removeAssociation(GraphAssociation association) {
		this.associationContainer.removeAssociation(association);
	}

	public ArrayList<Node> getGeneralizationNodes() {
		return this.associationContainer.getGeneralizationNodes();
	}

	public ArrayList<Node> getSpecializationNodes() {
		return this.associationContainer.getSpecializationNodes();
	}
	
	public boolean hasMultipleInheritance() {
		return this.associationContainer.hasMultipleInheritance();
	}
	
	
	public boolean existsAssociation(GraphAssociation association) {
		return this.associationContainer.existsAssociation(association);
	}
	// ----------------------------------------------------
	
	public void addLiteral(String literal) {
		this.literalContainer.addLiteral(literal);
	}
	
	public ArrayList<String> getLiterals() {
		return this.literalContainer.getLiterals();
	}
	// ----------------------------------------------------
	

	// ---------------------------------------------------------------------------------------
	// --- The methods below are intended to manipulate the node constraint
	// ---------------------------------------------------------------------------------------
	
	public boolean existsMissingConstraint(MissingConstraint missingConstraint) {
		if(missingConstraint == MissingConstraint.MC3_4) {
			return this.propertyContainer.isNecessaryGenerateMC3_4Constraint();
		}else {
			return this.constraintContainer.existsMissingConstraint(missingConstraint);
		}
	}
	
	public void addMissingConstraint(Node sourceNode, GraphAssociation association, MissingConstraint missingConstraint) {
		this.constraintContainer.addMissingConstraint(sourceNode, association, missingConstraint);
	}
	
	public ArrayList<ConstraintData> getMissingConstraint(MissingConstraint missingConstraint){
		return this.constraintContainer.getMissingConstraint(missingConstraint);
	}
	
	public ArrayList<ConstraintData> getAllMissingConstraint(){
		return this.constraintContainer.getAllMissingConstraint();
	}
	
	private void setConstraintContainer(ConstraintContainer container) {
		this.constraintContainer = container;
	}
	
	public void atualizeMandatoryProperties() {
		this.propertyContainer.atualizeMandatoryProperties();
	}
	
	// ----------------------------------------------------
	

	public String toString(){
	 String msg = getName(); /*+ " <<" + this.stereotype + ">>";*/ // + "( ID: "+this.getID() + ")";
	 
	 msg += this.propertyContainer.toString();
	 msg += this.literalContainer.toString();
	 msg += this.associationContainer.toString();
	 msg += this.constraintContainer.toString();

	 return msg;
	}
}