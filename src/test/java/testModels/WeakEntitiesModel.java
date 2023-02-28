package testModels;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphGeneralization;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class WeakEntitiesModel {

	public static Graph getSimpleWeakEntityModel() {
		Graph graph = new Graph();
		
		Node name = new Node(Increment.getNextS(), "Name", Stereotype.QUALITY);
		
		Node namedEntity = new Node(Increment.getNextS(), "NamedEntity", Stereotype.CATEGORY);
		
		Node person = new Node(Increment.getNextS(), "Person", Stereotype.KIND);
		
		GraphGeneralization generaization1 = new GraphGeneralization(
				Increment.getNextS(), 
				namedEntity, 
				person);
		
		GraphAssociation association1 = new GraphAssociation(
				Increment.getNextS(), 
				"has", 
				name, 
				Cardinality.C1, 
				namedEntity, 
				Cardinality.C1);
		
		namedEntity.addAssociation(association1);
		name.addAssociation(association1);
		
		graph.addNode(name);
		graph.addNode(namedEntity);
		graph.addNode(person);
		
		graph.addAssociation(association1);
		graph.addGeneralization(generaization1);
		
		return graph;
	}
}
