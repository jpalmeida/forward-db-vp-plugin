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

public class AssociationsModel {
	
	public static Graph getGraph1to1() {
		Graph graph = new Graph();
		
		Node person = new Node(Increment.getNextS(), "Person", Stereotype.KIND);
		
		Node node1To1 = new Node(Increment.getNextS(), "Node1To1", Stereotype.KIND);
		
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		node1To1.addProperty(new NodeProperty(
				node1To1,
				Integer.toString(Increment.getNext()),
				"test", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association1to1 = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				person, 
				Cardinality.C1, 
				node1To1, 
				Cardinality.C1);
		
		person.addAssociation(association1to1);
		node1To1.addAssociation(association1to1);
		
		graph.addNode(person);
		graph.addNode(node1To1);
		
		graph.addAssociation(association1to1);
		return graph;
	}
	
	public static Graph getGraph1to01() {
		Graph graph = new Graph();
		
		Node person = new Node(Increment.getNextS(), "Person", Stereotype.KIND);
		
		Node node1To01 = new Node(Increment.getNextS(), "Node1To01", Stereotype.KIND);
		
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		node1To01.addProperty(new NodeProperty(
				node1To01,
				Integer.toString(Increment.getNext()),
				"test", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association1to01 = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				person, 
				Cardinality.C1, 
				node1To01, 
				Cardinality.C0_1);
		
		person.addAssociation(association1to01);
		node1To01.addAssociation(association1to01);
		
		graph.addNode(person);
		graph.addNode(node1To01);
		
		graph.addAssociation(association1to01);
		
		return graph;
	}
	
	public static Graph getGraph01to01() {
		Graph graph = new Graph();
		
		Node person = new Node(Increment.getNextS(), "Person", Stereotype.KIND);
		
		Node node01To01 = new Node(Increment.getNextS(), "Node01To01", Stereotype.KIND);
		
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		node01To01.addProperty(new NodeProperty(
				node01To01,
				Integer.toString(Increment.getNext()),
				"test", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association1toN = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				person, 
				Cardinality.C0_1, 
				node01To01, 
				Cardinality.C0_1);
		
		person.addAssociation(association1toN);
		node01To01.addAssociation(association1toN);
		
		graph.addNode(person);
		graph.addNode(node01To01);
		
		graph.addAssociation(association1toN);
		
		return graph;
	}
	
	public static Graph getGraph1toN() {
		Graph graph = new Graph();
		
		Node person = new Node(Increment.getNextS(), "Person", Stereotype.KIND);
		
		Node node1ToN = new Node(Increment.getNextS(), "Node1ToN", Stereotype.KIND);
		
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		node1ToN.addProperty(new NodeProperty(
				node1ToN,
				Integer.toString(Increment.getNext()),
				"test", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association1to0N = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				person, 
				Cardinality.C1, 
				node1ToN, 
				Cardinality.C0_N);
		
		person.addAssociation(association1to0N);
		node1ToN.addAssociation(association1to0N);
		
		graph.addNode(person);
		graph.addNode(node1ToN);
		
		graph.addAssociation(association1to0N);
		
		return graph;
	}

	public static Graph getGraph01toN() {
		Graph graph = new Graph();
		
		Node person = new Node(Increment.getNextS(), "Person", Stereotype.KIND);
		
		Node node01ToN = new Node(Increment.getNextS(), "Node01ToN", Stereotype.KIND);
		
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		node01ToN.addProperty(new NodeProperty(
				node01ToN,
				Integer.toString(Increment.getNext()),
				"test", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association01toN = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				person, 
				Cardinality.C0_1, 
				node01ToN, 
				Cardinality.C0_N);
		
		person.addAssociation(association01toN);
		node01ToN.addAssociation(association01toN);
		
		graph.addNode(person);
		graph.addNode(node01ToN);
		
		graph.addAssociation(association01toN);
		
		return graph;
	}
	
	public static Graph getGraphNtoN() {
		Graph graph = new Graph();
		
		Node person = new Node(Increment.getNextS(), "Person", Stereotype.KIND);
		
		Node nodeNToN = new Node(Increment.getNextS(), "NodeNToN", Stereotype.KIND);
		
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		nodeNToN.addProperty(new NodeProperty(
				nodeNToN,
				Integer.toString(Increment.getNext()),
				"test", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation associationNtoN = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				person, 
				Cardinality.C0_N, 
				nodeNToN, 
				Cardinality.C1_N);
		
		person.addAssociation(associationNtoN);
		nodeNToN.addAssociation(associationNtoN);
		
		graph.addNode(person);
		graph.addNode(nodeNToN);
		
		graph.addAssociation(associationNtoN);
		
		return graph;
	}
	
	public static Graph getGraphToSelfRelationship() {
		Graph graph = new Graph();
		
		Node person = new Node(Increment.getNextS(), "Person", Stereotype.KIND);
		
		Node man = new Node(Increment.getNextS(), "Man", Stereotype.SUBKIND);
		
		Node woman = new Node(Increment.getNextS(), "Woman", Stereotype.SUBKIND);
		
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		GraphGeneralization generalizationMan = new GraphGeneralization(
				Increment.getNextS(), 
				person, 
				man);
		
		GraphGeneralization generalizationWoman = new GraphGeneralization(
				Increment.getNextS(), 
				person, 
				woman);
		
		GraphAssociation association01toN = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				man, 
				Cardinality.C0_1, 
				woman, 
				Cardinality.C0_N);
		
		man.addAssociation(association01toN);
		woman.addAssociation(association01toN);
		
		graph.addNode(person);
		graph.addNode(man);
		graph.addNode(woman);
		
		graph.addAssociation(association01toN);
		graph.addAssociation(generalizationMan);
		graph.addAssociation(generalizationWoman);
		
		return graph;
	}
	
	public static Graph getGraphDuplicateAssociation() {
		Graph graph = new Graph();
		
		Node person = new Node(Increment.getNextS(), "Person", Stereotype.KIND);
		
		Node car = new Node(Increment.getNextS(), "Car", Stereotype.KIND);
		
		
		GraphAssociation association01toN = new GraphAssociation(
				Increment.getNextS(), 
				"hasRent", 
				person, 
				Cardinality.C0_1, 
				car, 
				Cardinality.C0_N);
		
		GraphAssociation association1to1N = new GraphAssociation(
				Increment.getNextS(), 
				"hasOwner", 
				person, 
				Cardinality.C1, 
				car, 
				Cardinality.C1_N);
		
		person.addAssociation(association01toN);
		person.addAssociation(association1to1N);
		
		car.addAssociation(association01toN);
		car.addAssociation(association1to1N);
		
		graph.addNode(person);
		graph.addNode(car);
		
		graph.addAssociation(association01toN);
		graph.addAssociation(association1to1N);
		
		return graph;
	}
	
	public static Graph getGraphAssociationNNLifting() {
		Graph graph = new Graph();
		
		Node veicle = new Node(Increment.getNextS(), "Veicle", Stereotype.KIND);
		
		Node car = new Node(Increment.getNextS(), "Car", Stereotype.SUBKIND);
		
		Node person = new Node(Increment.getNextS(), "Person", Stereotype.KIND);
		
		Node driver = new Node(Increment.getNextS(), "Driver", Stereotype.SUBKIND);
		
		Node relation = new Node(Increment.getNextS(), "Relation", Stereotype.RELATOR);
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				veicle, 
				car);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				person, 
				driver);
		
		
		GraphAssociation associationNtoN = new GraphAssociation(
				Increment.getNextS(), 
				"hasRent", 
				car, 
				Cardinality.C0_N, 
				driver, 
				Cardinality.C1_N);
		
		GraphAssociation association1to1N = new GraphAssociation(
				Increment.getNextS(), 
				"hasOwner", 
				car, 
				Cardinality.C1, 
				relation, 
				Cardinality.C0_N);
		
		graph.addNode(person);
		graph.addNode(veicle);
		graph.addNode(car);
		graph.addNode(driver);
		graph.addNode(relation);
		
		graph.addAssociation(associationNtoN);
		graph.addAssociation(association1to1N);
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
				
		return graph;
	}
	
	public static Graph getGraphToSelfAssociationWithGS() {
		Graph graph = new Graph();
		
		Node module = new Node(Increment.getNextS(), "Module", Stereotype.KIND);
		
		Node physical = new Node(Increment.getNextS(), "Physical", Stereotype.SUBKIND);
		
		Node logical = new Node(Increment.getNextS(), "Logical", Stereotype.SUBKIND);
		
		Node myClass = new Node(Increment.getNextS(), "MyClass", Stereotype.KIND);
		
		
		GraphGeneralization generalization1 = new GraphGeneralization(
				Increment.getNextS(), 
				module, 
				physical);
		
		GraphGeneralization generalization2 = new GraphGeneralization(
				Increment.getNextS(), 
				module, 
				logical);
		
		GraphAssociation association1 = new GraphAssociation(
				Increment.getNextS(), 
				"hasPhysical", 
				myClass, 
				Cardinality.C1_N, 
				physical, 
				Cardinality.C1);
		
		GraphAssociation association2 = new GraphAssociation(
				Increment.getNextS(), 
				"hasLogical", 
				myClass, 
				Cardinality.C0_N, 
				logical, 
				Cardinality.C1);
		
		GraphAssociation association3 = new GraphAssociation(
				Increment.getNextS(), 
				"hasMySelf", 
				module, 
				Cardinality.C0_N, 
				module, 
				Cardinality.C0_1);
		
		GraphGeneralizationSet gs = new GraphGeneralizationSet(
				Increment.getNextS(), 
				"Composed", 
				true, 
				false);
		
		gs.addGeneralization(generalization1);
		gs.addGeneralization(generalization2);
		
		module.addAssociation(association3);
		
		physical.addAssociation(association1);
		myClass.addAssociation(association1);
		
		logical.addAssociation(association2);
		myClass.addAssociation(association3);
		
		graph.addNode(module);
		graph.addNode(physical);
		graph.addNode(logical);
		graph.addNode(myClass);
		
		graph.addAssociation(association1);
		graph.addAssociation(association2);
		graph.addAssociation(association3);
		
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		
		graph.addGeneralizationSet(gs);
		
		return graph;
	}
	
	
	public static Graph getGraphNtoNWithAnAbstractClass() {
		Graph graph = new Graph();
		
		Node associatedClass = new Node(Increment.getNextS(), "AssociatedClass", Stereotype.KIND);
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", Stereotype.CATEGORY);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", Stereotype.KIND);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", Stereotype.KIND);
		
		
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
				"has", 
				superClass, 
				Cardinality.C0_N, 
				associatedClass, 
				Cardinality.C1_N);
		
		GraphGeneralizationSet gs = new GraphGeneralizationSet(
				Increment.getNextS(), 
				"Composed", 
				true, 
				false);
		
		gs.addGeneralization(generalization1);
		gs.addGeneralization(generalization2);
		
		superClass.addAssociation(association1);
		associatedClass.addAssociation(association1);
		
		graph.addNode(superClass);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(associatedClass);
		
		graph.addAssociation(association1);
		
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		
		graph.addGeneralizationSet(gs);
		
		return graph;
	}
}
