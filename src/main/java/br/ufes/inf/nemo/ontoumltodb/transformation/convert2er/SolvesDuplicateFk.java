package br.ufes.inf.nemo.ontoumltodb.transformation.convert2er;

import java.util.ArrayList;


import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class SolvesDuplicateFk {

	public static void solves(Graph graph) {
		ArrayList <NodeProperty> duplicateFks;
		ArrayList <NodeProperty> resolveFkNames;
		NodeProperty serlfAssociation;
		
		//For duplicated foreign keys
		for(Node node : graph.getNodes()) {			
			duplicateFks = getDuplicateFksName(node);
			while( ! duplicateFks.isEmpty()) {
				resolveFkNames = extractFirstDuplicate(duplicateFks);
				resolveDuplicateFksName(node, resolveFkNames);
			}
		}
		
		//For self association
		for(Node node : graph.getNodes()) {			
			serlfAssociation = getSelfAssociation(node);
			while(serlfAssociation != null) {
				resolveSelfAssociationFor(serlfAssociation);
				serlfAssociation = getSelfAssociation(node);
			}
		}
	}
	//*****************************************
	//*** For duplicated foreign keys
	//*****************************************
	private static ArrayList <NodeProperty> getDuplicateFksName(Node node) {
		NodeProperty property1, property2;
		ArrayList <NodeProperty> duplicateProperties = new ArrayList<NodeProperty>();
		
		for(int index_1 = 0; index_1 < node.getProperties().size(); index_1++) {
			property1 = node.getProperties().get(index_1);
			if(property1.isForeignKey()) {
				for(int index_2 = 0; index_2 < node.getProperties().size(); index_2++) {
					property2 = node.getProperties().get(index_2);
					if(property2.isForeignKey()) {
						if( ! property1.isMyId(property2.getID()) )  {
							if(property1.getName().equals(property2.getName())) {
								addIfNotExists(duplicateProperties, property1);
								addIfNotExists(duplicateProperties, property2);
							}
						}
					}
				}
			}
		}
		return duplicateProperties;
	} 
	private static void addIfNotExists(ArrayList <NodeProperty> duplicateProperties, NodeProperty property) {
		boolean find = false;
		
		for(NodeProperty currentProperty : duplicateProperties) {
			if(currentProperty.isMyId(property.getID()))
				find = true;
		}
		
		if( ! find)
			duplicateProperties.add(property);
	}
	
	private static ArrayList<NodeProperty> extractFirstDuplicate(ArrayList<NodeProperty> duplicateFks){
		ArrayList<NodeProperty> result = new ArrayList<NodeProperty>();
		NodeProperty property;
		int index = 0;
		
		property = duplicateFks.remove(0);
		
		result.add(property);
		
		while( index < duplicateFks.size()) {
			if(duplicateFks.get(index).getName().equals(property.getName()) ) {
				result.add(duplicateFks.remove(index));
			}
			else {
				index++;
			}
		}
		
		return result;
	}
	
	private static void resolveDuplicateFksName(Node node, ArrayList<NodeProperty> resolveFkNames) {
		String newName, currentName, classNameEnd;
		
		String originalName = resolveFkNames.get(0).getName();
		
		for(NodeProperty property : resolveFkNames) {
			currentName = property.getName();
			classNameEnd = getClassNameEnd(property);
			
			newName = currentName.substring(0, currentName.length() - 3);//remove "_id"
			newName = newName + classNameEnd + "_id";	
			
			property.setName(newName);
			
			if(isPropertyNameDuplicated(node, newName)) {
				resolveDuplicateFksNameByAssociation(node, originalName, newName);
			}
		}
	}
	
	private static void resolveDuplicateFksNameByAssociation(Node node, String originalName, String currentName) {
		GraphAssociation association;
		String newName;
		
		for(NodeProperty property : node.getProperties()) {
			
			if(property.getName().equals(currentName) && property.isForeignKey()) {
				
				association = property.getAssociationRelatedOfFK().getOriginalAssociation();
				if(association == null)
					association = property.getAssociationRelatedOfFK();
			
				newName = originalName.substring(0, originalName.length() - 3);//remove "_id"
				if(association.getName() != null) {
					if(association.getName().length() > 1) {
						newName += association.getName().substring(0, 1).toUpperCase();
						newName += association.getName().substring(1, association.getName().length() );
					}
					else {
						newName += association.getName().toUpperCase();
					}
				}
				else newName += association.getID();
				
				newName += "_id";
				
				property.setName(newName);
				
				if(isPropertyNameDuplicated(node, newName)) {					
					resolveDuplicateFksNameById(node, originalName, newName);
				}
			}
		}
	}
	
	private static void resolveDuplicateFksNameById(Node node, String originalName, String currentName) {
		String newName;
		String increment;
		
		for(NodeProperty property : node.getProperties()) {
			if(property.getName().equals(currentName)) {
				
				newName = currentName.substring(0, currentName.length() - 3);//remove "_id"
				
				increment = Increment.getNextS();
				
				newName += increment +"_id";
				
				if( (newName.length() + 3) > Util.getMaxSizeNames()) {
					newName = currentName.substring(0, Util.getMaxSizeNames() - increment.length() - 3 -10);
					newName += increment +"_id";
				}				
				property.setName(newName);
			}
		}
	}
	
	private static String getClassNameEnd(NodeProperty property) {
		String className;
		GraphAssociation originalAssociation = property.getAssociationRelatedOfFK().getOriginalAssociation();
		if(originalAssociation == null) {
			originalAssociation = property.getAssociationRelatedOfFK();
		}
		Node currentNode = property.getOwnerNode();
		
		if(originalAssociation.getSourceNode().isMyId(currentNode.getID()))
			className = originalAssociation.getTargetNode().getName();
		else className = originalAssociation.getSourceNode().getName();
		
		return className;
	}
	
	private static boolean isPropertyNameDuplicated(Node node, String name) {
		int qtd = 0;
		for(NodeProperty property : node.getProperties()) {
			if(property.getName().equals(name)) {
				qtd++;
			}
		}
	
		if(qtd > 1)
			return true;
		return false;
	}

	//*****************************************
	//*** For self association
	//*****************************************

	private static NodeProperty getSelfAssociation(Node node) {
		String primaryKeyName = node.getPrimaryKey().getName();
		
		for(NodeProperty property : node.getProperties()) {
			if(property.isForeignKey() && ! property.isPrimaryKey()) {
				if(property.getName().equals(primaryKeyName)) {
					return property;
				}
			}
		}
		return null;
	}
	
	private static void resolveSelfAssociationFor(NodeProperty selfProperty) {
		String currentName, relatedName, newName;
		
		currentName = selfProperty.getName();
		
		relatedName = getClassNameLowCardinality(selfProperty);
		
		newName = currentName.substring(0, currentName.length() - 3);//remove "_id"
		
		newName = newName + relatedName + "_id";
		
		selfProperty.setName(newName);		
	}
	
	private static String getClassNameLowCardinality(NodeProperty property) {
		GraphAssociation association = property.getAssociationRelatedOfFK().getOriginalAssociation();
		if(association == null) {
			association = property.getAssociationRelatedOfFK();
		}
		
		if(association.getSourceCardinality() == Cardinality.C0_1 || association.getSourceCardinality() == Cardinality.C1)
			return association.getSourceNode().getName();
		else return association.getTargetNode().getName();
	}
}
