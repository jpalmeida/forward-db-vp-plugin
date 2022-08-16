package br.ufes.inf.nemo.ontoumltodb.transformation.approaches;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;

public interface IStrategy {

	/**
	 * Method responsible for performing the transformation of the graph.
	 *
	 * @param graph Graph to be modified.
	 */
	public void run(Graph graph, TraceTable traceTable);
}
