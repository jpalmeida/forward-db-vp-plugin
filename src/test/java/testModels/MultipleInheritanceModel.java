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

public class MultipleInheritanceModel {

	public static Graph getMultipleHierarchiesSimple() {
		Graph graph = new Graph();
		
		Node superClass1 = new Node(Increment.getNextS(), "SuperClass1", null);
		
		Node superClass2 = new Node(Increment.getNextS(), "SuperClass2", null);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", null);
		
		Node associatedClass = new Node(Increment.getNextS(), "AssociatedClass", Stereotype.RELATOR);
		
		
		subClass1.addProperty(new NodeProperty(
				subClass1,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
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
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass1, 
				subClass1);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass2, 
				subClass1);
		
		
		graph.addNode(superClass1);
		graph.addNode(superClass2);
		graph.addNode(subClass1);
		graph.addNode(associatedClass);
		
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		graph.addAssociation(association1);
		
		return graph;
	}
	
	public static Graph getMultipleHierarchies() {
		Graph graph = new Graph();
		
		Node superClass1 = new Node(Increment.getNextS(), "SuperClass1", null);
		
		Node superClass2 = new Node(Increment.getNextS(), "SuperClass2", null);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", null);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", null);
		
		Node subClass3 = new Node(Increment.getNextS(), "SubClass3", null);
		
		Node associatedClass = new Node(Increment.getNextS(), "AssociatedClass", null);
		
		
		subClass1.addProperty(new NodeProperty(
				subClass1,
				Integer.toString(Increment.getNext()),
				"age", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		subClass1.addProperty(new NodeProperty(
				subClass1,
				Integer.toString(Increment.getNext()),
				"test", 
				"int",
				true, //acceptNull
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
		
		subClass3.addProperty(new NodeProperty(
				subClass2,
				Integer.toString(Increment.getNext()),
				"height", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass1, 
				subClass1);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass2, 
				subClass1);
		
		GraphGeneralization generalization3 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass2, 
				subClass2);
		
		GraphGeneralization generalization4 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass2, 
				subClass3);
		
		GraphAssociation association1 = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				subClass1, 
				Cardinality.C1, 
				associatedClass, 
				Cardinality.C0_N);
		
		GraphGeneralizationSet gs = new GraphGeneralizationSet(
				Increment.getNextS(), 
				"GsText", 
				true, 
				true);
		
		gs.addGeneralization(generalization2);
		gs.addGeneralization(generalization3);
		gs.addGeneralization(generalization4);
				
		graph.addNode(superClass1);
		graph.addNode(superClass2);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(subClass3);
		graph.addNode(associatedClass);
		
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		graph.addAssociation(generalization3);
		graph.addAssociation(generalization4);
		graph.addAssociation(association1);
		
		graph.addGeneralizationSet(gs);
		
		return graph;
	}
	
	public static Graph getMultipleHierarchiesGeneralizationSet() {
		Graph graph = new Graph();
		
		Node superClass1 = new Node(Increment.getNextS(), "SuperClass1", null);
		
		Node superClass2 = new Node(Increment.getNextS(), "SuperClass2", null);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", null);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", null);
		
		Node subClass3 = new Node(Increment.getNextS(), "SubClass3", null);
		
		Node associatedClass = new Node(Increment.getNextS(), "AssociatedClass", null);
		
		
		subClass2.addProperty(new NodeProperty(
				subClass2,
				Integer.toString(Increment.getNext()),
				"ci", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
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
				subClass2);
		
		GraphGeneralization generalization4 = new GraphGeneralization(
				Increment.getNextS(), 
				superClass2, 
				subClass3);
		
		GraphAssociation association1 = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				subClass2, 
				Cardinality.C1, 
				associatedClass, 
				Cardinality.C0_N);
		
		GraphGeneralizationSet gs1 = new GraphGeneralizationSet(
				Increment.getNextS(), 
				"GsText1", 
				true, 
				true);
		
		GraphGeneralizationSet gs2 = new GraphGeneralizationSet(
				Increment.getNextS(), 
				"GsText2", 
				true, 
				true);
		
		gs1.addGeneralization(generalization1);
		gs1.addGeneralization(generalization2);
		
		gs2.addGeneralization(generalization3);
		gs2.addGeneralization(generalization4);
				
		graph.addNode(superClass1);
		graph.addNode(superClass2);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(subClass3);
		graph.addNode(associatedClass);
		
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		graph.addAssociation(generalization3);
		graph.addAssociation(generalization4);
		graph.addAssociation(association1);
		
		graph.addGeneralizationSet(gs1);
		graph.addGeneralizationSet(gs2);
		
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


}
