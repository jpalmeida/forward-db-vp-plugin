package br.ufes.inf.nemo.ontoumltodb.vp.controllers;

import java.util.HashMap;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.DiagramManager;
import com.vp.plugin.diagram.IDiagramTypeConstants;
import com.vp.plugin.diagram.IDiagramUIModel;
import com.vp.plugin.diagram.connector.IAssociationUIModel;
import com.vp.plugin.diagram.shape.IClassUIModel;
import com.vp.plugin.model.IAssociation;
import com.vp.plugin.model.IAssociationEnd;
import com.vp.plugin.model.IAttribute;
import com.vp.plugin.model.IClass;
import com.vp.plugin.model.factory.IModelElementFactory;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.util.Shape;
import br.ufes.inf.nemo.ontoumltodb.util.Position;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class ClassDiagramBuilder {
	
	private Graph graph;
	private DiagramManager diagramManager;
	private IDiagramUIModel diagram;
	HashMap<String, IClass> classesModel = new HashMap<String, IClass>();
	HashMap<String, IClassUIModel> classesUIModel = new HashMap<String, IClassUIModel>();
	
	private ClassDiagramBuilder(Graph graph) {
		this.graph = graph;
	}
	
	public static ClassDiagramBuilder builder(Graph graph) {
		return new ClassDiagramBuilder(graph);
	}

	
	public void run() {
		this.diagramManager = ApplicationManager.instance().getDiagramManager();
		this.diagram = diagramManager.createDiagram(IDiagramTypeConstants.DIAGRAM_TYPE_CLASS_DIAGRAM);
		
		for(Node node : graph.getNodes()) {
			buildClass(node);
		}
		
		for(GraphAssociation association : graph.getAssociations()) {
			buildAssociation(association);
		}
	}
	
	private void buildClass(Node node) {
		Position position = new Position(node.getPositionX(), node.getPositionY(), 50, 20);
		Shape shape = position.getCurrentShape();
		
		IClass newClass = IModelElementFactory.instance().createClass();
		newClass.setName(node.getName());
		if(node.getStereotype() == Stereotype.ENUMERATION) {
			newClass.addStereotype(node.getStereotype().toString());
		}
		classesModel.put(node.getID(), newClass);
		
		IClassUIModel newClassUI = (IClassUIModel) diagramManager.createDiagramElement(diagram, newClass);
		classesUIModel.put(node.getID(), newClassUI);
		
		buildAttributes(newClass, node);
		
		if(shape.getX() == 0) {
			Node nodeAux;
			Node nodeX = null;
			int x = 999999;
			for(GraphAssociation association : node.getAssociations()) {
				nodeAux = getNodeEnd(association, node);
				if(nodeAux.getPositionX() < x && nodeAux.getPositionX() != 0) {
					x = nodeAux.getPositionX();
					nodeX = nodeAux;
				}
			}
			
			if(nodeX != null) {
				position = new Position(nodeX.getPositionX(), nodeX.getPositionY(), 50, 20);
				shape = position.getNextPosition();
				while( existsObject(shape.getX(), shape.getY()) ) {
					shape = position.getNextPosition();
				}
			}
		}
		
		node.setPostion(shape.getX(), shape.getY(), node.getWidth(), node.getHeight());
		
		newClassUI.setBounds(
				node.getPositionX(), 
				node.getPositionY(), 
				node.getHeight(), 
				node.getWidth());
		newClassUI.fitSize();
		newClassUI.setRequestResetCaption(true);
	}
	
	private boolean existsObject(int posX, int posY) {
		int width;
		int height;
		int currentX, currentY;
		
		for(Node node : graph.getNodes()) {
			currentX = node.getPositionX();
			currentY = node.getPositionY();
			width = node.getWidth() + 30;
			height = node.getHeight() + 30;
			
			if( 	(posX >= currentX && posX <= (currentX + width) ) || 
					( (posX + width) >= currentX && (posX + width) <= (currentX + width) )
				) {
				if(		(posY >= currentY && posY <= (currentY + height)) ||
						( (posY + height) >= currentY && (posY + height) <= (currentY + height))
				){
					return true;
				}
			}
		}
		return false;
	}
	
	private void buildAttributes(IClass newClass, Node node) {
		IAttribute attr;
		for(NodeProperty property : node.getProperties()) {
			attr = newClass.createAttribute();
			attr.setName(property.getName());
			attr.setType(property.getDataType());
			if(property.isNullable()) {
				if(property.isMultivalued()) 
					attr.setMultiplicity("*");
				else attr.setMultiplicity("0..1");
			}
			else {
				if(property.isMultivalued()) 
					attr.setMultiplicity("1..*");
				else attr.setMultiplicity("1");
			}
		}
	}
	
	private Node getNodeEnd(GraphAssociation association, Node node) {
		if(association.getTargetNode().isMyId(node.getID()))
			return association.getSourceNode();
		else return association.getTargetNode();
	}
	
	private void buildAssociation(GraphAssociation association) {
		Node sourceNode = association.getSourceNode();
		Node targetNode = association.getTargetNode();
		
		IClass sourceClass = classesModel.get(sourceNode.getID());
		IClass targetClass = classesModel.get(targetNode.getID());
		
		IClassUIModel sourceUIClass = classesUIModel.get(sourceNode.getID());
		IClassUIModel targetUIClass = classesUIModel.get(targetNode.getID());
		
		//create a normal association
		IAssociation newAassociation = IModelElementFactory.instance().createAssociation();
		newAassociation.setFrom(sourceClass);
		newAassociation.setTo(targetClass);
		newAassociation.setName(association.getName());
		
		//ser multiplicity
		IAssociationEnd associationFromEnd = (IAssociationEnd) newAassociation.getFromEnd();
		associationFromEnd.setMultiplicity(association.getSourceCardinality().toString());
		IAssociationEnd associationToEnd = (IAssociationEnd) newAassociation.getToEnd();
		associationToEnd.setMultiplicity(association.getTargetCardinality().toString());
		
		// create association connector on diagram
		IAssociationUIModel associationConnector = (IAssociationUIModel) diagramManager.createConnector(diagram, newAassociation, sourceUIClass, targetUIClass, null);
		// set to automatic calculate the initial caption position
		associationConnector.setRequestResetCaption(true);		
	}
}
