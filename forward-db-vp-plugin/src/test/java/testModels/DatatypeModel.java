package testModels;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class DatatypeModel {

	public static Graph getGraph() {
		Graph graph = new Graph();
		
		Node person = new Node(Integer.toString(Increment.getNext()), "Person", null);
		
		Node date = new Node(Integer.toString(Increment.getNext()), "Date", Stereotype.DATATYPE);
		
		Node coordinate = new Node(Integer.toString(Increment.getNext()), "Coordinate", Stereotype.DATATYPE);
		
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"name", 
				date.getName(),
				false, //acceptNull
				false //multivalued
				));
		
		
		coordinate.addProperty(new NodeProperty(
				coordinate,
				Integer.toString(Increment.getNext()),
				"latitude", 
				null,
				false, //acceptNull
				false //multivalued
				));
		
		coordinate.addProperty(new NodeProperty(
				coordinate,
				Integer.toString(Increment.getNext()),
				"longitude", 
				null,
				false, //acceptNull
				false //multivalued
				));
		
		GraphAssociation association = new GraphAssociation(
				Integer.toString(Increment.getNext()), 
				"has", 
				person, 
				Cardinality.C0_N, 
				coordinate, 
				Cardinality.C0_1);
		
		person.addAssociation(association);
		coordinate.addAssociation(association);
		
		graph.addNode(person);
		graph.addNode(coordinate);
		graph.addAssociation(association);
		
		return graph;
	}
}
