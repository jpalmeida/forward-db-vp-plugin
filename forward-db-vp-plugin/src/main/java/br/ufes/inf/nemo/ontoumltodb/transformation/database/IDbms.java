package br.ufes.inf.nemo.ontoumltodb.transformation.database;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.TriggerResult;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;

public interface IDbms {

	  /**
	   * Returns the relational schema script for the graph (ER).
	   *
	   * @param target Graph (ER) to be generated the script with the relational scheme.
	   */
	  public String getSchema(Graph graph);
	  
	  /**
	   * Returns the script of the scripts for the graph (ER).
	   *
	   * @param target Graph (ER) to be generated the script with the relational scheme.
	   */
	  public String getIndexes(Graph graph);
	  
	  /**
	   * Returns the script to create the view, without the query.
	   * @param node
	   * @return
	   */
	  public String getViewName(Node node);
	  
	  /**
	   * Returns the string value of an object
	   * @param value
	   * @return
	   */
	  public String getStringValue(Object value);
	  
	  /**
	   * Generate the view for each trace of TableTrace
	   * 
	   * @param traceTable
	   * @param putInheritedAttributes
	   * @param dbms
	   * @return
	   */
	  public String generateViewForEachTrace(TraceTable traceTable, boolean putInheritedAttributes);//, IDbms dbms);
	  
	  /**
	   * Generate the triggers for each constraint present in the conceptual model e lost in the relational schema. 
	   * @return
	   */
	  public ArrayList<TriggerResult> generateTriggers(Graph graph, TraceTable traceTable);
}
