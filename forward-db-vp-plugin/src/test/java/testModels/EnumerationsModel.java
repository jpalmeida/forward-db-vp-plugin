package testModels;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodePropertyEnumeration;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class EnumerationsModel {
	
	public static Graph getGraph() {
		Graph graph = new Graph();
		
		Node person = new Node(Integer.toString(Increment.getNext()), "Person", Stereotype.KIND);
		
		person.addProperty(new NodeProperty(
				person,
				Integer.toString(Increment.getNext()),
				"name", 
				"string",
				false, //acceptNull
				false //multivalued
				));
		
		person.addProperty(new NodePropertyEnumeration(
				person,
				Integer.toString(Increment.getNext()),
				"phase", 
				"enum",
				false, //acceptNull
				false //multivalued
				).addValue("ADULT").addValue("CHILD"));
		
		graph.addNode(person);
		
		return graph;
	}
	

	
	public static Graph getEnumerationsAsAssociationGraph() {
		Graph graph = new Graph();
		
		Node person = new Node(Integer.toString(Increment.getNext()), "Person", Stereotype.KIND);
		
		Node nationality = new Node(Integer.toString(Increment.getNext()), "Nationality", Stereotype.ENUMERATION);
		
		nationality.addLiteral("Brazilian");
		nationality.addLiteral("Italian");
		
		
		GraphAssociation association = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				person, 
				Cardinality.C0_1, 
				nationality, 
				Cardinality.C1_N);
		
		graph.addNode(person);
		graph.addNode(nationality);
		
		graph.addAssociation(association);
		
		return graph;
	}
}
