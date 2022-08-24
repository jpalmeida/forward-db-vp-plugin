package testModels;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class DatatypeModel {

	public static Graph getDatatypeAsAttribute() {
		Graph graph = new Graph();
		
		Node person = new Node(Integer.toString(Increment.getNext()), "Person", null);
		
		Node date = new Node(Integer.toString(Increment.getNext()), "Date", Stereotype.DATATYPE);
		
		Node coordinate = new Node(Integer.toString(Increment.getNext()), "Coordinate", Stereotype.DATATYPE);
		
		coordinate.addProperty(new NodeProperty(
				coordinate,
				Increment.getNextS(),
				"latitude", 
				null,
				false, //acceptNull
				false //multivalued
				));
		
		coordinate.addProperty(new NodeProperty(
				coordinate,
				Increment.getNextS(),
				"longitude", 
				null,
				false, //acceptNull
				false //multivalued
				));
		
		person.addProperty(new NodeProperty(
				person,
				Increment.getNextS(),
				"mydate", 
				date.getName(),
				false, //acceptNull
				false //multivalued
				));
		
		
		person.addProperty(new NodeProperty(
				person,
				Increment.getNextS(),
				"position",
				coordinate.getName(),
				false, //acceptNull
				false //multivalued
				));
		
		person.addProperty(new NodeProperty(
				person,
				Increment.getNextS(),
				"multiplePositions", 
				coordinate.getName(),
				false, //acceptNull
				true //multivalued
				));
		
		
		graph.addNode(person);
		graph.addNode(date);
		graph.addNode(coordinate);
		
		return graph;
	}
	
	public static Graph getDatatypeAsAssociation() {
		Graph graph = new Graph();
		
		Node person = new Node(Integer.toString(Increment.getNext()), "Person", null);
		
		Node date = new Node(Integer.toString(Increment.getNext()), "Date", Stereotype.DATATYPE);
		
		Node coordinate = new Node(Integer.toString(Increment.getNext()), "Coordinate", Stereotype.DATATYPE);
		
		coordinate.addProperty(new NodeProperty(
				coordinate,
				Increment.getNextS(),
				"latitude", 
				null,
				false, //acceptNull
				false //multivalued
				));
		
		coordinate.addProperty(new NodeProperty(
				coordinate,
				Increment.getNextS(),
				"longitude", 
				null,
				false, //acceptNull
				false //multivalued
				));
		
		person.addProperty(new NodeProperty(
				person,
				Increment.getNextS(),
				"name", 
				"String",
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association1 = new GraphAssociation(
				Integer.toString(Increment.getNext()), 
				"myDate", 
				person, 
				Cardinality.C1, 
				date, 
				Cardinality.C0_1);
		
		GraphAssociation association2 = new GraphAssociation(
				Integer.toString(Increment.getNext()), 
				"multiplePositions", 
				person, 
				Cardinality.C1, 
				coordinate, 
				Cardinality.C0_N);
		
		GraphAssociation association3 = new GraphAssociation(
				Integer.toString(Increment.getNext()), 
				"monovaluedPosition", 
				person, 
				Cardinality.C1, 
				coordinate, 
				Cardinality.C0_1);
		
		person.addAssociation(association1);
		coordinate.addAssociation(association2);
		
		person.addAssociation(association2);
		
		person.addAssociation(association3);
		coordinate.addAssociation(association3);
		
		graph.addNode(person);
		graph.addNode(date);
		graph.addNode(coordinate);
		
		graph.addAssociation(association1);
		graph.addAssociation(association2);
		graph.addAssociation(association3);
		
		return graph;
	}
}
