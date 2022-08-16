package testModels;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphGeneralization;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class TraceTableModel {

	public static Graph getSimpleHierarchy() {
		Graph graph = new Graph();
		
		Node superClass = new Node(Increment.getNextS(), "SuperClass", Stereotype.KIND);
		Node subClass = new Node(Increment.getNextS(), "SubClass", Stereotype.SUBKIND);
		Node subSubClass = new Node(Increment.getNextS(), "SubSubClass", Stereotype.SUBKIND);
				
		superClass.addProperty(new NodeProperty(superClass, Increment.getNextS(),"name","string",false, false ));
		
		subClass.addProperty(new NodeProperty(subClass, Integer.toString(Increment.getNext()),"age","int",false,false));
		
		subSubClass.addProperty(new NodeProperty(subSubClass, Integer.toString(Increment.getNext()),"hight","int",false,false));
		
		GraphGeneralization gSuperclassSubClass = new GraphGeneralization(Increment.getNextS(),superClass,subClass);
		GraphGeneralization gSubclasses = new GraphGeneralization(Increment.getNextS(),subClass,subSubClass);
		
		graph.addNode(superClass);
		graph.addNode(subClass);
		graph.addNode(subSubClass);
		
		graph.addAssociation(gSuperclassSubClass);
		graph.addAssociation(gSubclasses);
		
		return graph;
	}
	
	public static Graph getComplexHierarchy() {
		Graph graph = new Graph();
		
		Node superClassA = new Node(Increment.getNextS(), "superClassA", Stereotype.KIND);
		Node subClass = new Node(Increment.getNextS(), "SubClass", Stereotype.SUBKIND);
		Node subSubClass = new Node(Increment.getNextS(), "SubSubClass", Stereotype.SUBKIND);
		Node superClassB = new Node(Increment.getNextS(), "superClassB", Stereotype.ROLE_MIXIN);
				
		superClassB.addProperty(new NodeProperty(
				subSubClass,
				Integer.toString(Increment.getNext()),
				"test",
				"int",
				false,false
				)
		);
		
		GraphGeneralization gSuperclassSubClass = new GraphGeneralization(Increment.getNextS(),superClassA,subClass);
		GraphGeneralization gSubclasses = new GraphGeneralization(Increment.getNextS(),subClass,subSubClass);
		GraphGeneralization gSuperclass2SubClass = new GraphGeneralization(Increment.getNextS(),superClassB,subSubClass);
		
		graph.addNode(superClassA);
		graph.addNode(subClass);
		graph.addNode(subSubClass);
		graph.addNode(superClassB);
		
		graph.addAssociation(gSuperclassSubClass);
		graph.addAssociation(gSuperclass2SubClass);
		graph.addAssociation(gSubclasses);
		
		return graph;
	}

}

