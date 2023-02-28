package br.ufes.inf.nemo.ontoumltodb.transformation.obda;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.Trace;

public class GenerateObdaTarget {

	public static String generate(String projectName, Node sourceNode, Trace targetNode) {
		String text = "target       ";

		text += generateSubject(projectName, sourceNode, targetNode);

		text += generatePredicateAndObjects(sourceNode, targetNode);

		text += generateForeignKeyAssociations(projectName, targetNode);

		text += ".\n";

		return text;
	}

	private static String generateSubject(String projectName, Node sourceNode, Trace targetNode) {
		String text = "";
		text += ":";
		text += projectName;
		text += "/";
		text += targetNode.getMainNode().getName();
		text += "/";
		text += "{";
		text += targetNode.getMainNode().getPKName();
		text += "}";
		text += " a ";
		text += ":";
		text += sourceNode.getName();
		text += " ";
		return text;
	}

	private static String generatePredicateAndObjects(Node sourceNode, Trace targetNode) {
		String text = "";

		for (NodeProperty property : sourceNode.getProperties()) {
			if (!property.isPrimaryKey()) {
				text += "; ";
				text += generatePredicateFromProperty(property);
				text += " ";
				text += generateObject(property, targetNode);
			}
		}
		return text;
	}

	private static String generatePredicateFromProperty(NodeProperty property) {
		String text = ":";
		text += property.getName();
		return text;
	}

	private static String generateObject(NodeProperty property, Trace targetNode) {
		String text = "";
		String targetProperty = "";
		NodeProperty nodeProperty = targetNode.getPropertyByOriginalId(property.getID());

		if (nodeProperty != null) {
			targetProperty = nodeProperty.getName();
		}

		text += "{";
		text += targetProperty;
		text += "}";
		text += getType(property);
		text += " ";

		return text;
	}

	private static String generateForeignKeyAssociations(String project, Trace targetNode) {
		String text = "";
		GraphAssociation association;

		for (NodeProperty property : targetNode.getMainNode().getProperties()) {
			if (property.isForeignKey()) {
				association = property.getAssociationRelatedOfFK();
				if (!association.isOverridesGeneralization()) {
					text += "; ";
					text += generatePredicateFromAssociation(association);
					text += " :";
					text += project;
					text += "/";
					text += association.getSourceNode().getID() == property.getForeignKeyNodeID()
							? association.getSourceNode().getName()
							: association.getTargetNode().getName();
					text += "/";
					text += generateReferencedObject(property, targetNode);
					text += " ";
				}
			}
		}
		return text;
	}

	private static String generateReferencedObject(NodeProperty property, Trace targetNode) {
		String text = "";
		text += "{";
		text += targetNode.getPropertyById(property.getID()).getName();
		text += "}";
		text += " ";

		return text;
	}

	private static String generatePredicateFromAssociation(GraphAssociation association) {
		String text = ":";
		text += association.getName() != null ? association.getName() : "unnamed_association";
		return text;
	}

	private static String getType(NodeProperty property) {
		if (property.getDataType() == "Date") {
			return "^^xsd:dateTime";
		} else if (property.getDataType() == "DateTime") {
			return "^^xsd:dateTime";
		} else if (property.getDataType() == "float") {
			return "^^xsd:decimal";
		} else if (property.getDataType() == "double") {
			return "^^xsd:decimal";
		} else if (property.getDataType() == "long") {
			return "^^xsd:decimal";
		}

		return "^^xsd:" + property.getDataType();
	}  
}
