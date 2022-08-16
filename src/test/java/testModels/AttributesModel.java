package testModels;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphGeneralization;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodePropertyEnumeration;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class AttributesModel {
	

	public static Graph getTypesAttributesGraph() {
		Graph graph = new Graph();
		
		Node person = new Node(Integer.toString(Increment.getNext()), "Person", Stereotype.KIND);
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"x1", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"x2", 
				"int",
				false, //acceptNull
				false //multivalued
				));
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"x3", 
				"Date",
				true, //acceptNull
				false //multivalued
				));
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"x4", 
				"boolean",
				true, //acceptNull
				false //multivalued
				));
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"x5", 
				"byte",
				true, //acceptNull
				false //multivalued
				));
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"x6", 
				"char",
				true, //acceptNull
				false //multivalued
				));
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"x7", 
				"double",
				true, //acceptNull
				false //multivalued
				));
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"x8", 
				"float",
				true, //acceptNull
				false //multivalued
				));
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"x9", 
				"long",
				true, //acceptNull
				false //multivalued
				));
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"x10", 
				"short",
				true, //acceptNull
				false //multivalued
				));
		
		graph.addNode(person);
		
		return graph;
	}
	
	public static Graph getMultivaluedAttributesGraph() {
		Graph graph = new Graph();
		
		Node person = new Node(Integer.toString(Increment.getNext()), "Person", Stereotype.KIND);
		
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"x11", 
				"string",
				true, //acceptNull
				true //multivalued
				));
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"x12", 
				"string",
				false, //acceptNull
				true //multivalued
				));
		
		graph.addNode(person);
		
		return graph;
	}
	
	public static Graph getMultivaluedEnumAttributeGraph() {
		Graph graph = new Graph();
		
		Node person = new Node(Integer.toString(Increment.getNext()), "Person", Stereotype.KIND);
		
		NodePropertyEnumeration newEnumeration1 = new NodePropertyEnumeration(
				person,
				Integer.toString(Increment.getNext()),
				"x1", 
				"string",
				true, //acceptNull
				true //multivalued
				);
		newEnumeration1.addValue("Test1");
		newEnumeration1.addValue("Test2");
		person.addProperty(newEnumeration1);
		
		NodePropertyEnumeration newEnumeration2 = new NodePropertyEnumeration(
				person,
				Integer.toString(Increment.getNext()),
				"x2", 
				"string",
				true, //acceptNull
				true //multivalued
				);
		newEnumeration2.addValue("Test3");
		newEnumeration2.addValue("Test4");
		person.addProperty(newEnumeration2);
		
		graph.addNode(person);
		
		return graph;
	}
	
	
	public static Graph getDuplicateFK() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", Stereotype.KIND);
		
		Node subClass1 = new Node(Increment.getNextS(), "SubClass1", Stereotype.ROLE);
		
		Node subClass2 = new Node(Increment.getNextS(), "SubClass2", Stereotype.ROLE);
		
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
				Cardinality.C1_N);
		
		GraphAssociation association2 = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				subClass2, 
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
		
		graph.addNode(superClass);
		graph.addNode(subClass1);
		graph.addNode(subClass2);
		graph.addNode(associatedClass);
		
		graph.addAssociation(association1);
		graph.addAssociation(association2);
		graph.addAssociation(generalization1);
		graph.addAssociation(generalization2);
		
		return graph;
	}
	
	public static Graph getSpecialCharacteresGraph() {
		Graph graph = new Graph();
		
		Node person = new Node(Integer.toString(Increment.getNext()), "Person x Human", Stereotype.KIND);

		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"x1 2+3-4/5*6\\x", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		graph.addNode(person);
		
		return graph;
	}
}
