package br.ufes.inf.nemo.ontoumltodb.vp;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.model.IAssociation;
import com.vp.plugin.model.IAssociationEnd;
import com.vp.plugin.model.IAttribute;
import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IEnumerationLiteral;
import com.vp.plugin.model.IGeneralization;
import com.vp.plugin.model.IGeneralizationSet;
import com.vp.plugin.model.IModelElement;
import com.vp.plugin.model.IProject;
import com.vp.plugin.model.IStereotype;
import com.vp.plugin.model.factory.IModelElementFactory;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphGeneralization;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphGeneralizationSet;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodePropertyEnumeration;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.NodeOrigin;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class LoadGraph {

	private static Graph graph;
	private static IProject project;
	
	
	private static IDiagramElement elements[];

	public static Graph load() {
		graph = new Graph();
		project = ApplicationManager.instance().getProjectManager().getProject();
		
		elements = ApplicationManager.instance().getDiagramManager().getActiveDiagram().toDiagramElementArray();

		putClasses();
		
		putAssociations();

		putGeneralizations();

		putGeneralizationSets();
		
		return graph;
	}

	
	private static IDiagramElement getDiagramElement(IModelElement elementToFind) {
		for(IDiagramElement element : elements) {
			if(element.getModelElement() != null) {
				if(element.getModelElement().getId().equals(elementToFind.getId())) {
					return element;
				}
			}
		}
		return null;
	}
	
	/********************************************************************
	 ** puts the classes
	 *********************************************************************/
	private static void putClasses() {
		IModelElement[] sourceContents = project.toAllLevelModelElementArray(IModelElementFactory.MODEL_TYPE_CLASS);
		for (IModelElement element : sourceContents) {
			putClass(element);
		}
	}

	private static void putClass(IModelElement element) {
		String nodeName;
		Node newNode = null;
		Stereotype stereotype;
		
		nodeName = Util.removeSpecialChar(element.getName());
		
		newNode = graph.getNodeByName(nodeName);
		
		if(newNode == null) {
			stereotype = getFirstStereotype(element);
			
			if(stereotype == null && ((IClass) element).isAbstract()) {
				stereotype = Stereotype.ABSTRACT;
			}
			
			newNode = new Node(element.getId(), element.getName(), stereotype, NodeOrigin.FROM_MODEL);
			
			if(stereotype == Stereotype.ENUMERATION) {
				IEnumerationLiteral[] literals = ((IClass) element).toEnumerationLiteralArray();
				
				for(IEnumerationLiteral literal : literals) {
					newNode.addLiteral(literal.getName());
				}
			}
			
			IDiagramElement diagramElement = getDiagramElement(element);
			if(diagramElement != null) {
				newNode.setPostion(
						diagramElement.getX(), 
						diagramElement.getY(), 
						diagramElement.getWidth(),
						diagramElement.getHeight()
						);
			}
			
			graph.addNode(newNode);
		}
		putAttributes(element, newNode);
	}

	private static void putAttributes(IModelElement element, Node newNode) {
		NodeProperty property;
		IAttribute[] iAttributes = ((IClass) element).toAttributeArray();
		IModelElement enumType;
		
		
		for (IAttribute iAttribute : iAttributes) {
			enumType = getEnumerationType(iAttribute); 
			if(enumType != null) {
				property = new NodePropertyEnumeration(
						newNode,
						iAttribute.getId(), 
						iAttribute.getName(),
						iAttribute.getTypeAsElement().getName(), 
						acceptNull(iAttribute), 
						isMultivalued(iAttribute));
				
				IEnumerationLiteral[] literals = ((IClass) enumType).toEnumerationLiteralArray();
				
				for(IEnumerationLiteral attr : literals) {
					((NodePropertyEnumeration)property).addValue(attr.getName());
				}
			}
			else{
				property = new NodeProperty(
						newNode,
						iAttribute.getId(), 
						iAttribute.getName(),
						iAttribute.getTypeAsElement() != null ? iAttribute.getTypeAsElement().getName() : "int", 
						acceptNull(iAttribute), 
						isMultivalued(iAttribute));
				
			}
			newNode.addProperty(property);
		}
		
	}
	
	private static IModelElement getEnumerationType(IAttribute attr) {
		Stereotype result;
		
		if(attr.getTypeAsElement() == null)
			return null;
		
		String attrName = attr.getTypeAsElement().getName();
		
		IModelElement[] sourceContents = project.toAllLevelModelElementArray(IModelElementFactory.MODEL_TYPE_CLASS);
		for (IModelElement element : sourceContents) {
			result = getFirstStereotype(element);
			if(result != null) {
				if(result == Stereotype.ENUMERATION && element.getName().equals(attrName))
					return element;
			}
		}
		return null;
	}

	private static Stereotype getFirstStereotype(IModelElement element) {
		IStereotype[] stereotypes = element.toStereotypeModelArray();
		String stereotypeName = "";
		if (stereotypes != null && stereotypes.length > 0) {
			stereotypeName = stereotypes[0].getName();
			return Stereotype.getUfoStereotype(stereotypeName);
		}
		return null;
	}

	private static boolean acceptNull(IAttribute iAttribute) {
		String multiplicity = iAttribute.getMultiplicity();

		if (multiplicity == null)
			return true;

		String[] multiplicities = multiplicity.split("\\.\\.");

		if("Unspecified".equals(multiplicities[0])) {
			return true;
		}
		
		if (multiplicities.length > 1) {
			int min = multiplicities[0].equals("*") ? 99999 : Integer.parseInt(multiplicities[0]);
			int max = multiplicities[1].equals("*") ? 99999 : Integer.parseInt(multiplicities[1]);
			if (min == 0 || max == 0)
				return true;
		} else {
			int min = multiplicities[0].equals("*") ? 99999 : Integer.parseInt(multiplicities[0]);
			if (min == 0)
				return true;
		}
		return false;
	}

	private static boolean isMultivalued(IAttribute iAttribute) {
		String multiplicity = iAttribute.getMultiplicity();

		if (multiplicity == null)
			return false;

		String[] multiplicities = multiplicity.split("\\.\\.");
		
		if("Unspecified".equals(multiplicities[0])) {
			return false;
		}

		if (multiplicities.length > 1) {
			int min = multiplicities[0].equals("*") ? 99999 : Integer.parseInt(multiplicities[0]);
			int max = multiplicities[1].equals("*") ? 99999 : Integer.parseInt(multiplicities[1]);
			if (min > 1 || max > 1)
				return true;
		} else {
			int min = multiplicities[0].equals("*") ? 99999 : Integer.parseInt(multiplicities[0]);
			if (min > 1)
				return true;
		}
		return false;
	}

	/********************************************************************
	 ** puts the associations
	 *********************************************************************/
	public static void putAssociations() {
		IModelElement[] sourceContents = project.toAllLevelModelElementArray(IModelElementFactory.MODEL_TYPE_ASSOCIATION);
		for (IModelElement element : sourceContents) {
			putAssociation(element);
		}
	}

	private static void putAssociation(IModelElement element) {
		String sourceNodeName, targetNodeName;
		
		IAssociation association = (IAssociation) element;

		IModelElement from = association.getFrom();
		IModelElement to = association.getTo();

		sourceNodeName = Util.removeSpecialChar(from.getName());
		targetNodeName = Util.removeSpecialChar(to.getName());
		
		Node sourceNode = graph.getNodeByName(sourceNodeName);
		Node targetNode = graph.getNodeByName(targetNodeName);
		
		GraphAssociation newAssociation = new GraphAssociation(
				association.getId(), 
				association.getName(), 
				sourceNode,
				getCardinality(((IAssociationEnd) association.getFromEnd()).getMultiplicity()), 
				targetNode,
				getCardinality(((IAssociationEnd) association.getToEnd()).getMultiplicity()));

		sourceNode.addAssociation(newAssociation);
		targetNode.addAssociation(newAssociation);

		graph.addAssociation(newAssociation);
	}

	private static Cardinality getCardinality(String cardinality) {
		if (cardinality == null)
			return Cardinality.C0_1;

		int lowerCardinality = getLowerBoundCardinality(cardinality);
		int upperCardinality = getUpperBoundCardinality(cardinality);

		if (lowerCardinality == 0) {
			if (upperCardinality == 1)
				return Cardinality.C0_1;
			else
				return Cardinality.C0_N; // 0..2, 0..3, ..., 0..*
		} else {
			if (lowerCardinality == 1) {
				if (upperCardinality == 1)
					return Cardinality.C1;
				else
					return Cardinality.C1_N; // 1..2, 1..3, ..., 1..*
			}
			else {
				return Cardinality.C0_N; // *
			}
			
//			if (upperCardinality == 1)
//				return Cardinality.C1;
//			else
//				return Cardinality.C1_N; // 1..2, 1..3, ..., 1..*
		}
	}

	private static int getLowerBoundCardinality(String cardinality) {
		String[] cardinalities = cardinality.split("\\.\\.");
		String lowerBound = cardinalities[0];

		return lowerBound.equals("*") ? 99999 : Integer.parseInt(lowerBound);
	}

	private static int getUpperBoundCardinality(String cardinality) {
		String[] cardinalities = cardinality.split("\\.\\.");
		String upperBound;

		if (cardinalities.length > 1)
			upperBound = cardinalities[1];
		else
			upperBound = cardinalities[0];

		return upperBound.equals("*") ? 99999 : Integer.parseInt(upperBound);
	}

	/********************************************************************
	 ** puts the generalizations
	 *********************************************************************/
	public static void putGeneralizations() {
		IModelElement[] sourceContents = project.toAllLevelModelElementArray(IModelElementFactory.MODEL_TYPE_GENERALIZATION);
		for (IModelElement element : sourceContents) {
			putGeneralization(element);
		}
	}

	private static void putGeneralization(IModelElement element) {
		IGeneralization generalization = (IGeneralization) element;
		Node generalizationNode;
		Node specializationNode;
		String generalizationNodeName, specilizationNodeName;

		IModelElement from = generalization.getFrom();
		IModelElement to = generalization.getTo();
		
		generalizationNodeName = Util.removeSpecialChar(from.getName());
		specilizationNodeName = Util.removeSpecialChar(to.getName());

		generalizationNode = graph.getNodeByName(generalizationNodeName);
		specializationNode = graph.getNodeByName(specilizationNodeName);
		
		GraphGeneralization newGeneralization = new GraphGeneralization(
				generalization.getId(), 
				generalizationNode,
				specializationNode);

		graph.addGeneralization(newGeneralization);
	}

	/********************************************************************
	 ** puts the generalization sets
	 *********************************************************************/
	public static void putGeneralizationSets() {
		IModelElement[] sourceContents = project.toAllLevelModelElementArray(IModelElementFactory.MODEL_TYPE_GENERALIZATION_SET);
		for (IModelElement element : sourceContents) {
			putGeneralizationSet(element);
		}
	}
	
	private static void putGeneralizationSet(IModelElement element) {
		IGeneralizationSet generalizationSet = (IGeneralizationSet) element;
		GraphGeneralizationSet newGeneralizationSet;
		GraphGeneralization graphGeneralization;

		newGeneralizationSet = new GraphGeneralizationSet(generalizationSet.getId(), generalizationSet.getName(), generalizationSet.isDisjoint(), generalizationSet.isCovering());
				
		IGeneralization[] generalizations = generalizationSet.toGeneralizationArray();
		
		if(generalizations != null) {
			for (IGeneralization generalization : generalizations) {
				graphGeneralization = (GraphGeneralization)graph.getAssociationByID(generalization.getId());
				newGeneralizationSet.addGeneralization(graphGeneralization);
			}
			graph.addGeneralizationSet(newGeneralizationSet);
		}
	}
}
