package testModels;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphGeneralization;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphGeneralizationSet;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class HierarchyModel {

	public static Graph getSimpleHierarchyForFlattening() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", Stereotype.CATEGORY);
		
		Node subClass = new Node(Increment.getNextS(), "SubClass", Stereotype.KIND);
		
		Node associatedClass = new Node(Increment.getNextS(), "AssociatedClass", Stereotype.RELATOR);
				
		superClass.addProperty(new NodeProperty(
				superClass,
				Increment.getNextS(),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		subClass.addProperty(new NodeProperty(
				subClass,
				Integer.toString(Increment.getNext()),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		subClass.addProperty(new NodeProperty(
				subClass,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		associatedClass.addProperty(new NodeProperty(
				associatedClass,
				Increment.getNextS(),
				"address", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				superClass, 
				Cardinality.C1, 
				associatedClass, 
				Cardinality.C1_N);
		
		GraphGeneralization generalization = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass);
		
		graph.addNode(superClass);
		graph.addNode(subClass);
		graph.addNode(associatedClass);
		
		graph.addAssociation(association);
		graph.addAssociation(generalization);
		
		return graph;
	}
	
	public static Graph getCommonHierarchyForFlattening() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", Stereotype.CATEGORY);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", Stereotype.KIND);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", Stereotype.KIND);
		
		Node associatedClass = new Node(Increment.getNextS(), "AssociatedClass", Stereotype.RELATOR);
				
		superClass.addProperty(new NodeProperty(
				superClass,
				Increment.getNextS(),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		subClass1.addProperty(new NodeProperty(
				subClass1,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		subClass2.addProperty(new NodeProperty(
				subClass2,
				Integer.toString(Increment.getNext()),
				"height", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		associatedClass.addProperty(new NodeProperty(
				associatedClass,
				Increment.getNextS(),
				"address", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				superClass, 
				Cardinality.C0_1, 
				associatedClass, 
				Cardinality.C0_N);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass1);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass2);
		
		GraphGeneralizationSet newGeneralizationSet = new GraphGeneralizationSet(Increment.getNextS(), "GS_TEST", true, false);
		newGeneralizationSet.addGeneralization(generalization1);
		newGeneralizationSet.addGeneralization(generalization2);
		
		graph.addNode(superClass);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(associatedClass);
		
		graph.addAssociation(association);
		graph.addGeneralization(generalization1);
		graph.addGeneralization(generalization2);
		graph.addGeneralizationSet(newGeneralizationSet);
		
		return graph;
	}
	
	public static Graph getCommonHierarchyMandatoryForFlattening() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", Stereotype.CATEGORY);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", Stereotype.KIND);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", Stereotype.KIND);
		
		Node associatedClass = new Node(Increment.getNextS(), "AssociatedClass", Stereotype.RELATOR);
				
		superClass.addProperty(new NodeProperty(
				superClass,
				Increment.getNextS(),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		subClass1.addProperty(new NodeProperty(
				subClass1,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		subClass2.addProperty(new NodeProperty(
				subClass2,
				Integer.toString(Increment.getNext()),
				"height", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		associatedClass.addProperty(new NodeProperty(
				associatedClass,
				Increment.getNextS(),
				"address", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				superClass, 
				Cardinality.C1, 
				associatedClass, 
				Cardinality.C0_N);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass1);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass2);
		
		GraphGeneralizationSet newGeneralizationSet = new GraphGeneralizationSet(Increment.getNextS(), "GS_TEST", true, false);
		newGeneralizationSet.addGeneralization(generalization1);
		newGeneralizationSet.addGeneralization(generalization2);
		
		graph.addNode(superClass);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(associatedClass);
		
		graph.addAssociation(association);
		graph.addGeneralization(generalization1);
		graph.addGeneralization(generalization2);
		graph.addGeneralizationSet(newGeneralizationSet);
		
		return graph;
	}
	
	public static Graph getComplexHierarchyForFlattening() {
		Graph graph = new Graph();
		
		Node superClass1 = new Node(Increment.getNextS(), "SuperClass1", Stereotype.CATEGORY);
		
		Node superClass2 = new Node(Increment.getNextS(), "SuperClass2", Stereotype.CATEGORY);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", Stereotype.KIND);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", Stereotype.KIND);
		
		Node subClass3 = new Node(Increment.getNextS(), "SubClass3", Stereotype.KIND);
		
		Node subClass4 = new Node(Increment.getNextS(), "SubClass4", Stereotype.KIND);
		
		Node associatedClass = new Node(Increment.getNextS(), "AssociatedClass", Stereotype.RELATOR);
		
		superClass1.addProperty(new NodeProperty(
				superClass1,
				Increment.getNextS(),
				"name1", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		superClass2.addProperty(new NodeProperty(
				superClass2,
				Increment.getNextS(),
				"name2", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		subClass1.addProperty(new NodeProperty(
				subClass1,
				Integer.toString(Increment.getNext()),
				"age1", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		subClass2.addProperty(new NodeProperty(
				subClass2,
				Integer.toString(Increment.getNext()),
				"age2", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		subClass3.addProperty(new NodeProperty(
				subClass3,
				Integer.toString(Increment.getNext()),
				"age3", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		subClass4.addProperty(new NodeProperty(
				subClass4,
				Integer.toString(Increment.getNext()),
				"age4", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		associatedClass.addProperty(new NodeProperty(
				associatedClass,
				Increment.getNextS(),
				"address", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association1 = new GraphAssociation(
				Increment.getNextS(), 
				"has1", 
				superClass1, 
				Cardinality.C1, 
				associatedClass, 
				Cardinality.C0_N);
		
		GraphAssociation association2 = new GraphAssociation(
				Increment.getNextS(), 
				"has2", 
				superClass2, 
				Cardinality.C1, 
				associatedClass, 
				Cardinality.C0_N);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass1, 
				subClass1);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass1, 
				subClass2);
		
		GraphGeneralization generalization3 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass2, 
				subClass3);
		
		GraphGeneralization generalization4 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass2, 
				subClass4);
		
		GraphGeneralizationSet newGeneralizationSet1 = new GraphGeneralizationSet(Increment.getNextS(), "GS_TEST1", true, false);
		newGeneralizationSet1.addGeneralization(generalization1);
		newGeneralizationSet1.addGeneralization(generalization2);
		
		GraphGeneralizationSet newGeneralizationSet2 = new GraphGeneralizationSet(Increment.getNextS(), "GS_TEST2", true, false);
		newGeneralizationSet2.addGeneralization(generalization3);
		newGeneralizationSet2.addGeneralization(generalization4);
		
		graph.addNode(superClass1);
		graph.addNode(superClass2);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(subClass3);
		graph.addNode(subClass4);
		graph.addNode(associatedClass);
		
		graph.addAssociation(association1);
		graph.addAssociation(association2);
		graph.addGeneralization(generalization1);
		graph.addGeneralization(generalization2);
		graph.addGeneralization(generalization3);
		graph.addGeneralization(generalization4);
		graph.addGeneralizationSet(newGeneralizationSet1);
		graph.addGeneralizationSet(newGeneralizationSet2);
		
		return graph;
	}
	
	
	public static Graph getSimpleHierarchyForLifting() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", Stereotype.KIND);
		
		Node subClass = new Node(Increment.getNextS(), "SubClass", Stereotype.SUBKIND);
		
		Node associatedClass = new Node(Increment.getNextS(), "AssociatedClass", Stereotype.RELATOR);
				
		superClass.addProperty(new NodeProperty(
				superClass,
				Increment.getNextS(),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		subClass.addProperty(new NodeProperty(
				subClass,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
				false, //acceptNull
				false //multivalued
				));		
		
		associatedClass.addProperty(new NodeProperty(
				associatedClass,
				Increment.getNextS(),
				"address", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				subClass, 
				Cardinality.C1, 
				associatedClass, 
				Cardinality.C1_N);
		
		GraphGeneralization generalization = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass);
		
		graph.addNode(superClass);
		graph.addNode(subClass);
		graph.addNode(associatedClass);
		
		graph.addAssociation(association);
		graph.addAssociation(generalization);
		
		return graph;
	}
	
	public static Graph getSimpleHierarchyForLiftingOptionalProperty() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", Stereotype.KIND);
		
		Node subClass = new Node(Increment.getNextS(), "SubClass", Stereotype.SUBKIND);
		
		Node associatedClass = new Node(Increment.getNextS(), "AssociatedClass", Stereotype.RELATOR);
				
		superClass.addProperty(new NodeProperty(
				superClass,
				Increment.getNextS(),
				"name", 
				"string",
				true, //acceptNull
				false //multivalued
				));
		
		subClass.addProperty(new NodeProperty(
				subClass,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
				true, //acceptNull
				false //multivalued
				));
		
		associatedClass.addProperty(new NodeProperty(
				associatedClass,
				Increment.getNextS(),
				"address", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				subClass, 
				Cardinality.C1, 
				associatedClass, 
				Cardinality.C1_N);
		
		GraphGeneralization generalization = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass);
		
		graph.addNode(superClass);
		graph.addNode(subClass);
		graph.addNode(associatedClass);
		
		graph.addAssociation(association);
		graph.addAssociation(generalization);
		
		return graph;
	}
	
	public static Graph getHierarchyDisjointComplete() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", Stereotype.KIND);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", Stereotype.SUBKIND);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", Stereotype.SUBKIND);
		
		Node associatedClass = new Node(Increment.getNextS(), "AssociatedClass", Stereotype.RELATOR);
				
		superClass.addProperty(new NodeProperty(
				superClass,
				Increment.getNextS(),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		subClass1.addProperty(new NodeProperty(
				subClass1,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		subClass2.addProperty(new NodeProperty(
				subClass2,
				Integer.toString(Increment.getNext()),
				"height", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		associatedClass.addProperty(new NodeProperty(
				associatedClass,
				Increment.getNextS(),
				"address", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				subClass1, 
				Cardinality.C1, 
				associatedClass, 
				Cardinality.C1_N);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass1);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass2);
		
		GraphGeneralizationSet newGeneralizationSet = new GraphGeneralizationSet(Increment.getNextS(), "GsTest", true, true);
		newGeneralizationSet.addGeneralization(generalization1);
		newGeneralizationSet.addGeneralization(generalization2);
		
		graph.addNode(superClass);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(associatedClass);
		
		graph.addAssociation(association);
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		
		graph.addGeneralizationSet(newGeneralizationSet);
		
		return graph;
	}
	
	public static Graph getHierarchyDisjointIncomplete() {
		Graph graph = getHierarchyDisjointComplete();
		
		GraphGeneralizationSet gs =  graph.getGeneralizationSets().get(0);
		
		gs.setComplete(false);
		
		return graph;
	}
	
	public static Graph getHierarchyOverlappingComplete() {
		Graph graph = getHierarchyDisjointComplete();
		
		GraphGeneralizationSet gs =  graph.getGeneralizationSets().get(0);
		
		gs.setDisjoint(false);
		
		return graph;
	}
	
	public static Graph getHierarchyOverlappingInomplete() {
		Graph graph = getHierarchyDisjointComplete();
		
		GraphGeneralizationSet gs =  graph.getGeneralizationSets().get(0);
		
		gs.setDisjoint(false);
		gs.setComplete(false);
		
		return graph;
	}
	
	
	public static Graph getMultilevelSimpleHierarchy() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", Stereotype.KIND);
		
		Node subClass = new Node(Increment.getNextS(), "SubClass", Stereotype.SUBKIND);
		
		Node subSubClass = new Node(Increment.getNextS(), "SubSubClass", Stereotype.ROLE);
		
		Node associatedClass = new Node(Increment.getNextS(), "AssociatedClass", Stereotype.RELATOR);
		
				
		superClass.addProperty(new NodeProperty(
				superClass,
				Increment.getNextS(),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		subClass.addProperty(new NodeProperty(
				subClass,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		subSubClass.addProperty(new NodeProperty(
				subSubClass,
				Integer.toString(Increment.getNext()),
				"ci", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		associatedClass.addProperty(new NodeProperty(
				associatedClass,
				Increment.getNextS(),
				"address", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				subSubClass, 
				Cardinality.C1, 
				associatedClass, 
				Cardinality.C1_N);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				subClass, 
				subSubClass);
		
		graph.addNode(superClass);
		graph.addNode(subClass);
		graph.addNode(subSubClass);
		graph.addNode(associatedClass);
		
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		graph.addAssociation(association);
		
		return graph;
	}
	
	public static Graph getMultiSimpleHierarchies() {
		Graph graph = new Graph();
		
		Node superClass1 = new Node(Increment.getNextS(), "SuperClass1", Stereotype.KIND);
		
		Node superClass2 = new Node(Increment.getNextS(), "SuperClass2", Stereotype.KIND);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", Stereotype.SUBKIND);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", Stereotype.SUBKIND);
		
		Node associatedClass = new Node(Increment.getNextS(), "AssociatedClass", Stereotype.RELATOR);
		
				
		superClass1.addProperty(new NodeProperty(
				superClass1,
				Increment.getNextS(),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		superClass2.addProperty(new NodeProperty(
				superClass2,
				Increment.getNextS(),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		subClass1.addProperty(new NodeProperty(
				subClass1,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		subClass2.addProperty(new NodeProperty(
				subClass2,
				Integer.toString(Increment.getNext()),
				"ci", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		associatedClass.addProperty(new NodeProperty(
				associatedClass,
				Increment.getNextS(),
				"address", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association1 = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				subClass1, 
				Cardinality.C1, 
				associatedClass, 
				Cardinality.C0_N);
		
		GraphAssociation association2 = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				subClass2, 
				Cardinality.C1, 
				associatedClass, 
				Cardinality.C0_N);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass1, 
				subClass1);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass2, 
				subClass2);
		
		
		graph.addNode(superClass1);
		graph.addNode(superClass2);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(associatedClass);
		
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		graph.addAssociation(association1);
		graph.addAssociation(association2);
		
		return graph;
	}
	
	public static Graph getLiftingPaper() {
		Graph graph = new Graph();
		
		Node person = new Node(Increment.getNextS(), "Person", Stereotype.KIND);
		
		Node brazilian = new Node(Increment.getNextS(), "Brazilian", Stereotype.ROLE);
		
		Node italian = new Node(Increment.getNextS(), "Italian", Stereotype.ROLE);
		
		Node supplyContract = new Node(Increment.getNextS(), "SupplyContract", Stereotype.RELATOR);
				
		person.addProperty(new NodeProperty(
				person,
				Increment.getNextS(),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		brazilian.addProperty(new NodeProperty(
				brazilian,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		italian.addProperty(new NodeProperty(
				italian,
				Integer.toString(Increment.getNext()),
				"height", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		supplyContract.addProperty(new NodeProperty(
				supplyContract,
				Increment.getNextS(),
				"address", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				italian, 
				Cardinality.C1, 
				supplyContract, 
				Cardinality.C1_N);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				person, 
				brazilian);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				person, 
				italian);
		
		GraphGeneralizationSet newGeneralizationSet = new GraphGeneralizationSet(Increment.getNextS(), "Nationality", true, true);
		newGeneralizationSet.addGeneralization(generalization1);
		newGeneralizationSet.addGeneralization(generalization2);
		
		graph.addNode(person);
		graph.addNode(brazilian);
		graph.addNode(italian);
		graph.addNode(supplyContract);
		
		graph.addAssociation(association);
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		
		graph.addGeneralizationSet(newGeneralizationSet);
		
		return graph;
	}

	public static Graph getDiamondHierarchy() {
		Graph graph = new Graph();
		
		Node superCategory = new Node(Increment.getNextS(), "SuperCategory", Stereotype.CATEGORY);
		
		Node kind = new Node(Increment.getNextS(), "Kind", Stereotype.KIND);
		
		Node subCategory = new Node(Increment.getNextS(), "SubCategory", Stereotype.CATEGORY);
		
		Node subKind = new Node(Increment.getNextS(), "SubKind", Stereotype.SUBKIND);
		
		Node relator = new Node(Increment.getNextS(), "Relator", Stereotype.RELATOR);
				
		superCategory.addProperty(new NodeProperty(
				superCategory,
				Increment.getNextS(),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		kind.addProperty(new NodeProperty(
				kind,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		subCategory.addProperty(new NodeProperty(
				subCategory,
				Integer.toString(Increment.getNext()),
				"height", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		subKind.addProperty(new NodeProperty(
				subKind,
				Increment.getNextS(),
				"address", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				relator, 
				Cardinality.C1, 
				superCategory, 
				Cardinality.C1_N);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superCategory, 
				kind);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				superCategory, 
				subCategory);
		
		GraphGeneralization generalization3 = new GraphGeneralization(
				Increment.getNextS(), 
				subCategory, 
				subKind);
		
		GraphGeneralization generalization4 = new GraphGeneralization(
				Increment.getNextS(), 
				kind, 
				subKind);
				
		graph.addNode(superCategory);
		graph.addNode(kind);
		graph.addNode(subCategory);
		graph.addNode(subKind);
		graph.addNode(relator);
		
		graph.addAssociation(association);
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		graph.addAssociation(generalization3);
		graph.addAssociation(generalization4);
		
		return graph;
	}
	
	public static Graph getMultipleInheritanceWithoutStereotypes() {
		Graph graph = new Graph();
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", null);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", null);
		
		Node bottomClass = new Node(Increment.getNextS(), "BottomClass", null);
		
		Node relator = new Node(Increment.getNextS(), "Relator", null);
		
		subClass1.addProperty(new NodeProperty(
				subClass1,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		subClass2.addProperty(new NodeProperty(
				subClass2,
				Integer.toString(Increment.getNext()),
				"height", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		bottomClass.addProperty(new NodeProperty(
				bottomClass,
				Increment.getNextS(),
				"address", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				relator, 
				Cardinality.C1, 
				bottomClass, 
				Cardinality.C1_N);
		
		GraphGeneralization generalization3 = new GraphGeneralization(
				Increment.getNextS(), 
				subClass2, 
				bottomClass);
		
		GraphGeneralization generalization4 = new GraphGeneralization(
				Increment.getNextS(), 
				subClass1, 
				bottomClass);
				
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(bottomClass);
		graph.addNode(relator);
		
		graph.addAssociation(association);
		graph.addAssociation(generalization3);
		graph.addAssociation(generalization4);
		
		return graph;
	}
	
	public static Graph getDimondHierarchyWithoutStereotypes() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", null);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", null);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", null);
		
		Node bottomClass = new Node(Increment.getNextS(), "BottomClass", null);
		
		Node relator = new Node(Increment.getNextS(), "Relator", null);
				
		superClass.addProperty(new NodeProperty(
				superClass,
				Increment.getNextS(),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		subClass1.addProperty(new NodeProperty(
				subClass1,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		subClass2.addProperty(new NodeProperty(
				subClass2,
				Integer.toString(Increment.getNext()),
				"height", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		bottomClass.addProperty(new NodeProperty(
				bottomClass,
				Increment.getNextS(),
				"address", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				relator, 
				Cardinality.C1, 
				bottomClass, 
				Cardinality.C1_N);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass1);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass2);
		
		GraphGeneralization generalization3 = new GraphGeneralization(
				Increment.getNextS(), 
				subClass2, 
				bottomClass);
		
		GraphGeneralization generalization4 = new GraphGeneralization(
				Increment.getNextS(), 
				subClass1, 
				bottomClass);
				
		graph.addNode(superClass);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(bottomClass);
		graph.addNode(relator);
		
		graph.addAssociation(association);
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		graph.addAssociation(generalization3);
		graph.addAssociation(generalization4);
		
		return graph;
	}
	
	public static Graph getComplexDimond() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", null);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", null);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", null);
		
		Node subClass3 = new Node(Increment.getNextS(), "SubClass3", null);
		
		Node bottomClass = new Node(Increment.getNextS(), "BottomClass", null);
		
		Node subClass4 = new Node(Increment.getNextS(), "SubClass4", null);
		
		Node relator = new Node(Increment.getNextS(), "Relator", null);
		
		GraphAssociation association = new GraphAssociation(
				Increment.getNextS(), 
				"hasRelator", 
				relator, 
				Cardinality.C1_N, 
				bottomClass, 
				Cardinality.C1);
		
		GraphAssociation association2 = new GraphAssociation(
				Increment.getNextS(), 
				"hasSubClass3", 
				subClass3, 
				Cardinality.C1_N, 
				superClass, 
				Cardinality.C1_N);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass1);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass2);
		
		GraphGeneralization generalization3 = new GraphGeneralization(
				Increment.getNextS(), 
				subClass2, 
				bottomClass);
		
		GraphGeneralization generalization4 = new GraphGeneralization(
				Increment.getNextS(), 
				subClass1, 
				bottomClass);
		
		GraphGeneralization generalization5 = new GraphGeneralization(
				Increment.getNextS(), 
				subClass1, 
				subClass3);
		
		GraphGeneralization generalization6 = new GraphGeneralization(
				Increment.getNextS(), 
				bottomClass, 
				subClass4);
				
		graph.addNode(superClass);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(subClass3);
		graph.addNode(bottomClass);
		graph.addNode(subClass4);
		graph.addNode(relator);
		
		graph.addAssociation(association);
		graph.addAssociation(association2);
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		graph.addAssociation(generalization3);
		graph.addAssociation(generalization4);
		graph.addAssociation(generalization5);
		graph.addAssociation(generalization6);
		
		return graph;
	}
	
	public static Graph getSelfAssociationInSubnode() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", null);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", null);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", null);
		
		Node subClass3 = new Node(Increment.getNextS(), "SubClass3", null);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass1);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass2);
		
		GraphGeneralization generalization3 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass3);
		
		GraphAssociation association1 = new GraphAssociation(
				Increment.getNextS(), 
				"Class2GoToClass1", 
				subClass2, 
				Cardinality.C1, 
				subClass1, 
				Cardinality.C1_N);
		
		GraphAssociation association2 = new GraphAssociation(
				Increment.getNextS(), 
				"Class2GoToClass3", 
				subClass2, 
				Cardinality.C1, 
				subClass3, 
				Cardinality.C1_N);
		
		GraphAssociation association3 = new GraphAssociation(
				Increment.getNextS(), 
				"subClass1SubClass1", 
				subClass1, 
				Cardinality.C1_N, 
				subClass1, 
				Cardinality.C1_N);
		
		GraphGeneralizationSet newGeneralizationSet = new GraphGeneralizationSet(
				Increment.getNextS(), 
				"TYPE", 
				false, 
				false);
		
		newGeneralizationSet.addGeneralization(generalization1);
		newGeneralizationSet.addGeneralization(generalization2);
		newGeneralizationSet.addGeneralization(generalization3);
				
		graph.addNode(superClass);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(subClass3);
		
		graph.addAssociation(association1);
		graph.addAssociation(association2);
		graph.addAssociation(association3);
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		graph.addAssociation(generalization3);
		
		graph.addGeneralizationSet(newGeneralizationSet);
		
		return graph;
	}
	
	public static Graph getInverseRestrictionMC1() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", Stereotype.ABSTRACT);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", null);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", null);
		
		Node subClass3 = new Node(Increment.getNextS(), "SubClass3", null);
		
		Node superClass2 = new Node(Increment.getNextS(), "SuperClass2", null);
		
		Node relatedClass = new Node(Increment.getNextS(), "RelatedClass", null);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass1);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass2);
		
		GraphGeneralization generalization3 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass3);
		
		GraphGeneralization generalization4 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass2, 
				subClass3);
		
		GraphAssociation association1 = new GraphAssociation(
				Increment.getNextS(), 
				"hasSuperClass", 
				relatedClass, 
				Cardinality.C1, 
				superClass, 
				Cardinality.C0_1);
		
		GraphGeneralizationSet newGeneralizationSet = new GraphGeneralizationSet(
				Increment.getNextS(), 
				"TYPE", 
				false, 
				false);
		
		newGeneralizationSet.addGeneralization(generalization1);
		newGeneralizationSet.addGeneralization(generalization2);
		newGeneralizationSet.addGeneralization(generalization3);
		newGeneralizationSet.addGeneralization(generalization4);
				
		graph.addNode(superClass);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(subClass3);
		graph.addNode(superClass2);
		graph.addNode(relatedClass);
		
		graph.addAssociation(association1);
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		graph.addAssociation(generalization3);
		graph.addAssociation(generalization4);
		
		graph.addGeneralizationSet(newGeneralizationSet);
		
		return graph;
	}
	
	public static Graph getInverseRestrictionMC1ChangedAssociation() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", Stereotype.ABSTRACT);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", null);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", null);
		
		Node relatedClass = new Node(Increment.getNextS(), "RelatedClass", null);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass1);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass2);
		
		GraphAssociation association1 = new GraphAssociation(
				Increment.getNextS(), 
				"hasSuperClass", 
				relatedClass, 
				Cardinality.C1_N, 
				superClass, 
				Cardinality.C1);
		
		GraphGeneralizationSet newGeneralizationSet = new GraphGeneralizationSet(
				Increment.getNextS(), 
				"TYPE", 
				false, 
				false);
		
		newGeneralizationSet.addGeneralization(generalization1);
		newGeneralizationSet.addGeneralization(generalization2);
				
		graph.addNode(superClass);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(relatedClass);
		
		graph.addAssociation(association1);
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		
		graph.addGeneralizationSet(newGeneralizationSet);
		
		return graph;
	}
	
	public static Graph getComplexHierarchy() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", null);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", null);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", null);
		
		Node subClass3 = new Node(Increment.getNextS(), "SubClass3", null);
		
		Node subClass4 = new Node(Increment.getNextS(), "SubClass4", null);
		
		Node subClass5 = new Node(Increment.getNextS(), "SubClass5", null);
		
		Node subClass6 = new Node(Increment.getNextS(), "SubClass6", null);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass, 
				subClass1);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				subClass1, 
				subClass2);
		
		GraphGeneralization generalization3 = new GraphGeneralization(
				Increment.getNextS(), 
				subClass1, 
				subClass3);
		
		GraphGeneralization generalization4 = new GraphGeneralization(
				Increment.getNextS(), 
				subClass3, 
				subClass4);
		
		GraphGeneralization generalization5 = new GraphGeneralization(
				Increment.getNextS(), 
				subClass3, 
				subClass5);
		
		GraphGeneralization generalization6 = new GraphGeneralization(	
				Increment.getNextS(), 
				subClass3, 
				subClass6);
		
		
		GraphGeneralizationSet newGeneralizationSet1 = new GraphGeneralizationSet(
				Increment.getNextS(), 
				"LevelA", 
				true, 
				true);
		
		GraphGeneralizationSet newGeneralizationSet2 = new GraphGeneralizationSet(
				Increment.getNextS(), 
				"LevelB", 
				true, 
				true);
		
		newGeneralizationSet1.addGeneralization(generalization2);
		newGeneralizationSet1.addGeneralization(generalization3);
		
		newGeneralizationSet2.addGeneralization(generalization4);
		newGeneralizationSet2.addGeneralization(generalization5);
		newGeneralizationSet2.addGeneralization(generalization6);
				
		graph.addNode(superClass);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(subClass3);
		graph.addNode(subClass4);
		graph.addNode(subClass5);
		graph.addNode(subClass6);
		
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		graph.addAssociation(generalization3);
		graph.addAssociation(generalization4);
		graph.addAssociation(generalization5);
		graph.addAssociation(generalization6);
		
		graph.addGeneralizationSet(newGeneralizationSet1);
		graph.addGeneralizationSet(newGeneralizationSet2);
		
		return graph;
	}

}

