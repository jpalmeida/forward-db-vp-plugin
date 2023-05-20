package br.ufes.inf.nemo.ontoumltodb.transformation.approaches;

import br.ufes.inf.nemo.ontoumltodb.transformation.approaches.process.Flatting;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;

public class OneTablePerLeafClass extends CommonTransformation implements IStrategy{

	@Override
	public void run(Graph graph, TraceTable traceTable) {
		if(this.isTransformaNtoNFirst)
			resolveNtoN(graph, traceTable);
		
	    runFlattening(graph, traceTable);
	}

	@Override
	public void setTransformaNtoNFirst(boolean flag) {
		this.isTransformaNtoNFirst = flag;
	}
	
	//*************************************
	//*** F L A T T E N I N G
	//*************************************
	private void runFlattening(Graph graph, TraceTable traceTable) {
		Node node = getTopLevelClass(graph);

		while (node != null) {
			Flatting.run(node, graph, traceTable);
			node = getTopLevelClass(graph);
		}	
	}
	
	private Node getTopLevelClass(Graph graph) {
		for (Node node : graph.getNodes()) {
			if (	!node.isSpecialization() && 
					node.hasSpecialization() 
			) {
				return node;
			}
		}
		return null;
	}

}
