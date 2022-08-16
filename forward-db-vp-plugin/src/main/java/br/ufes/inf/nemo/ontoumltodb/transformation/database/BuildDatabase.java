package br.ufes.inf.nemo.ontoumltodb.transformation.database;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.TriggerResult;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;

public class BuildDatabase {

	private static IDbms getDbms(DbmsSupported dbms) {
		
		switch (dbms) {
		case H2: {
			return new H2();
		}
		case MYSQL: {
			return new MySql();
		}
		case ORACLE: {
			return new Oracle();
		}
		case POSTGRE: {
			return new Postgre();
		}
		case SQLSERVER: {
			return new SqlServer();
		}
		default:
			return new Generic();
		}
	}
	
	public static String getSchema(Graph graph, DbmsSupported dbms) {
		IDbms targetDBMS;
		
		targetDBMS = getDbms(dbms);
		
		return targetDBMS.getSchema(graph);
	}
	
	public static String getIndexes(Graph graph, DbmsSupported dbms) {
		IDbms targetDBMS;
		
		targetDBMS = getDbms(dbms);
		
		return targetDBMS.getIndexes(graph);
	}
	
	public static String getViews(TraceTable traceTable, boolean putInheritedAttributes, DbmsSupported dbms) {
		IDbms targetDBMS;

		targetDBMS = getDbms(dbms);
		
		return targetDBMS.generateViewForEachTrace(traceTable, putInheritedAttributes);//, targetDBMS);
	}
	
	public static ArrayList<TriggerResult> getTriggers(Graph graph, TraceTable traceTable, DbmsSupported dbms) {
		IDbms targetDBMS;

		targetDBMS = getDbms(dbms);
		
		return targetDBMS.generateTriggers(graph, traceTable);
	}
}
