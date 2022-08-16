package br.ufes.inf.nemo.ontoumltodb.vp.controllers;

import java.util.HashMap;
import java.util.Set;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.DiagramManager;
import com.vp.plugin.diagram.IDiagramTypeConstants;
import com.vp.plugin.diagram.IDiagramUIModel;
import com.vp.plugin.diagram.shape.IDBTableUIModel;
import com.vp.plugin.model.IDBColumn;
import com.vp.plugin.model.IDBForeignKey;
import com.vp.plugin.model.IDBForeignKeyConstraint;
import com.vp.plugin.model.IDBTable;
import com.vp.plugin.model.factory.IModelElementFactory;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.util.Position;
import br.ufes.inf.nemo.ontoumltodb.util.Shape;

public class ErDiagramBuilder {
	
	private Graph graph;
	private DiagramManager diagramManager;
	private IDiagramUIModel diagram;
	
	HashMap<String, IDBTable> tablesModel = new HashMap<String, IDBTable>();
	HashMap<String, IDBTableUIModel> tablesUIModel = new HashMap<String, IDBTableUIModel>();

	private ErDiagramBuilder(Graph graph) {
		this.graph = graph;
	}
	
	public static ErDiagramBuilder builder(Graph graph) {
		return new ErDiagramBuilder(graph);
	}
	
	public void run() {
		// ***********************************************************
		// https://knowhow.visual-paradigm.com/openapi/er-diagram/
		// ***********************************************************
		
		diagramManager = ApplicationManager.instance().getDiagramManager();
		diagram = diagramManager.createDiagram(IDiagramTypeConstants.DIAGRAM_TYPE_ER_DIAGRAM);
		
		for(Node node : graph.getNodes()) {
			buildTable(node);
		}
		
		for(Node node : graph.getNodes()) {
			buildFks(node);
		}
		
		resizeTables();		
	}
	
	private void buildTable(Node node) {
		
		//Create table for retail centers
		IDBTable newTable = IModelElementFactory.instance().createDBTable();
		newTable.setName(node.getName());
		
		//Create the table shape on diagram
		IDBTableUIModel newTableUI = (IDBTableUIModel) diagramManager.createDiagramElement(diagram, newTable);
		Shape shape = getPositionShape(node);
		newTableUI.setBounds(shape.getX(),shape.getY(),shape.getWidth(),shape.getHeight());
		node.setPostion(shape.getX(),shape.getY(),shape.getWidth(),shape.getHeight());
		
		buildAttributes(newTable, node);
		
		//Call to re-calculate caption position when render the diagram
		// NOT WORK!!!!
		newTableUI.resetCaption();
		newTableUI.resetCaptionSize();
		
		tablesModel.put(node.getID(), newTable);
		tablesUIModel.put(node.getID(), newTableUI);
	}
	
	private void buildAttributes(IDBTable newTable, Node node) {
		IDBColumn newColumn;
		for(NodeProperty property : node.getProperties()) {
			if(! property.isForeignKey()) {
				newColumn = newTable.createDBColumn();
				newColumn.setName(property.getName());
				
				if(property.isPrimaryKey()) {
					newColumn.setPrimaryKey(true);
				}
				
				newColumn.setNullable(property.isNullable());
				
				newColumn.setType(property.getDataType(),10,0);
			}
		}
	}
		
	private void buildFks(Node node) {
		IDBForeignKey foreignKey = null;
		IDBForeignKeyConstraint constraint;
		IDBTable tableFrom; // refers to table where column was created
		IDBTable tableTo; // references the table where the column was propagated
		IDBColumn primaryKey;
		
		tableTo = tablesModel.get(node.getID());
		for(NodeProperty property : node.getProperties()) {
			if(property.isForeignKey()) {
				tableFrom = tablesModel.get(property.getForeignKeyNodeID());
				
				primaryKey = tableTo.getDBColumnByName(property.getName());
				
				//Create a foreign key
				foreignKey = IModelElementFactory.instance().createDBForeignKey();
				
				//The foreign key is connecting from the resident table ...
				foreignKey.setFrom(tableFrom);
				
				//to the referenced table
				foreignKey.setTo(tableTo);
				
				//Create a constraint
				constraint = IModelElementFactory.instance().createDBForeignKeyConstraint();
				
				//The new foreign key will be referencing the ID column in the source table
				constraint.setRefColumn(primaryKey);
				constraint.setForeignKey(foreignKey);
				
				//Create the connector
				IDBTableUIModel tableUIFrom = tablesUIModel.get(property.getForeignKeyNodeID());
				IDBTableUIModel tableUITo = tablesUIModel.get(node.getID());
				diagramManager.createConnector(diagram, foreignKey, tableUIFrom, tableUITo, null);
				
				
				constraint.setName("fk_" + property.getName());
				constraint.getForeignKey().setName("FK");
			}
		}
	}

	private void resizeTables() {
		Node node;
		IDBTableUIModel table;
		Set<String> keys = tablesModel.keySet();
		int numberOfProperties;
		int max = 0;
		
        for(String key: keys){
            node = graph.getNodeById(key);
            numberOfProperties = node.getProperties().size();
            
            max = 0;
            for(NodeProperty property : node.getProperties()) {
            	if(property.getName().length() > max)
            		max = property.getName().length();
            }
            
            table = tablesUIModel.get(key);
            table.setSize( 90 + (max * 9) , 22 + (numberOfProperties * 19) );
        }
	}
	
	private Shape getPositionShape(Node node) {
		Position position = new Position(node.getPositionX(), node.getPositionY(), node.getWidth(), node.getHeight());
		Shape shape = position.getCurrentShape();
		
		if(shape.getX() == 0) {
			Node nodeAux;
			Node nodeX = null;
			int x = 999999;
			for(GraphAssociation association : node.getAssociations()) {
				nodeAux = graph.getNodeEndOf(association, node);
				if(nodeAux.getPositionX() < x && nodeAux.getPositionX() != 0) {
					x = nodeAux.getPositionX();
					nodeX = nodeAux;
				}
			}
			
			if(nodeX != null) {
				position = new Position(nodeX.getPositionX(), nodeX.getPositionY(), 50, 20);
				shape = position.getNextPosition();
				while( existsObject(shape.getX(), shape.getY()) ) {
					shape = position.getNextPosition();
				}
			}
		}
		
		return shape;
	}
	
	private boolean existsObject(int posX, int posY) {
		int width;
		int height;
		int currentX, currentY;
		
		for(Node node : graph.getNodes()) {
			currentX = node.getPositionX();
			currentY = node.getPositionY();
			width = node.getWidth() + 30;
			height = node.getHeight() + 30;
			
			if( 	(posX >= currentX && posX <= (currentX + width) ) || 
					( (posX + width) >= currentX && (posX + width) <= (currentX + width) )
				) {
				if(		(posY >= currentY && posY <= (currentY + height)) ||
						( (posY + height) >= currentY && (posY + height) <= (currentY + height))
				){
					return true;
				}
			}
		}
		return false;
	}
	
}
