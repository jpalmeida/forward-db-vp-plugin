package br.ufes.inf.nemo.ontoumltodb.transformation.obda;

import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.Trace;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceSet;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;

public class GenerateObda {

	public static String generateMapping(String projectName, TraceTable traceTable, String baseIri) {
		boolean first;
		String text = "";
		
		text += generatePrefix(baseIri);
		
		text += "[MappingDeclaration] @collection [[\n\n";

		for (TraceSet trace : traceTable.getTraces()) {
			first = true;
			for (Trace targetNode : trace.getTraces()) {
				text += GenerateObdaMappingId.generate(projectName, trace.getSourceNode(), first);

				text += GenerateObdaTarget.generate(projectName, trace.getSourceNode(), targetNode);// trace.getSourceNode(), projectName, tracedNode);

				text += GenerateObdaSource.generate(trace.getSourceNode(), targetNode);

				first = false;

				text += "\n\n";
			}
		}
		text += "]]\n";

		return text;
	}

	private static String generatePrefix(String baseIri) {
		return ("[PrefixDeclaration]\n" 
				+ ":       " + baseIri + "#\n" 
				+ "gufo:   http://purl.org/nemo/gufo#\n"
				+ "rdf:    http://www.w3.org/1999/02/22-rdf-syntax-ns#\n"
				+ "rdfs:   http://www.w3.org/2000/01/rdf-schema#\n" 
				+ "owl:    http://www.w3.org/2002/07/owl#\n"
				+ "xsd:    http://www.w3.org/2001/XMLSchema#\n" 
				+ "\n");
	}
}
