package br.ufes.inf.nemo.ontoumltodb.transformation.convert2er;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;

public class ToEntityRelationship {

	public static void run(Graph graph, TraceTable traceTable, boolean isStandardizeNames, boolean enumFiledToLookupTable) {

		graph.transform1To1AssociationIn1To01();
		
		SolvesWeakEntities.run(graph, traceTable);
		
		SolvesGeneralization.run(graph);
		
		SolvesMultivaluedProperty.run(graph, traceTable);

		SolvesEnumeration.solves(graph, traceTable, enumFiledToLookupTable);
		
		SolvesDatatype.solves(graph, traceTable);

		SolvesCardinalityNtoN.solves(graph, traceTable);
		
		SolvesPrimaryKey.solves(graph);

		SolvesForeignKey.solves(graph);
		
		SolvesDuplicateFk.solves(graph);
		
		SolvesName.solves(graph, isStandardizeNames);
	}
}
