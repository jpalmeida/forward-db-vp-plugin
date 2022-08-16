package br.ufes.inf.nemo.ontoumltodb.transformation.convert2er;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphGeneralization;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;

public class SolvesGeneralization {

	public static void run(Graph graph) {
		GraphGeneralization generalization;
		GraphAssociation newAssociation;
		String id;
		ArrayList<GraphGeneralization> toDestroy = new ArrayList<GraphGeneralization>();

		for (GraphAssociation association : graph.getAssociations()) {
			if (association instanceof GraphGeneralization) {
				generalization = (GraphGeneralization) association;

				id = Integer.toString(Increment.getNext());

				newAssociation = new GraphAssociation(id, // ID
						id, // association name
						generalization.getGeneral(), // sourceNode
						Cardinality.C1, // sourceCardinality
						generalization.getSpecific(), // targetNode
						Cardinality.C0_1 // targetCardinality
				);
				newAssociation.setOriginalAssociation(association);
				newAssociation.setDerivedFromGeneralization(true);

				generalization.getGeneral().addAssociation(newAssociation);
				generalization.getSpecific().addAssociation(newAssociation);
				graph.addAssociation(newAssociation);

				toDestroy.add(generalization);
			}
		}

		for (GraphGeneralization gen : toDestroy) {
			graph.removeAssociation(gen);
		}
	}
}
