package br.ufes.inf.nemo.ontoumltodb.transformation.tracer;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;

public class Filter {

	private NodeProperty filterProperty;
	private Object value;
	//private ArrayList<NodeProperty> mandatoryProperties; Process restructured from version V0.2. 
	
	public Filter(NodeProperty filterProperty, Object value) {
		//this.mandatoryProperties = new ArrayList<NodeProperty>();  Process restructured from version V0.2.
		this.filterProperty = filterProperty;
		this.value = value;
	}
	
	public NodeProperty getFilterProperty() {
		return this.filterProperty;
	}
	
	public void setFilterProperty(NodeProperty newProperty) {
		this.filterProperty = newProperty;
	}
	
	public Object getValue() {
		return this.value;
	}
	
//	Process restructured from version V0.2.
//	public void addMandatoryProperty(NodeProperty property) {
//		this.mandatoryProperties.add(property);
//	}
//	
//	public ArrayList<NodeProperty> getMandatoryProperties(){
//		return this.mandatoryProperties;
//	}
//	
//	public boolean hasMandatoryProperties() {
//		if(this.mandatoryProperties.size() > 0)
//			return true;
//		else return false;
//	}
	
	public boolean isSame(Filter filter) {
		if(	filterProperty.getName().equals(filter.getFilterProperty().getName()) &&
			value.toString().equals(filter.getValue().toString())) {
			return true;
		}
		else return false;
	}
	
//	Process restructured from version V0.2.
//	public void updateMandatoryPrperties(Node to) {
//		for(int i = 0; i < mandatoryProperties.size(); i++) {
//			mandatoryProperties.set(i, getMigratedProperty(to, mandatoryProperties.get(i)));
//		}
//	}
//	
//	private NodeProperty getMigratedProperty(Node to, NodeProperty discriminatorProperty) {
//		for(NodeProperty nodeProperty : to.getProperties()) {
//			if(nodeProperty.getOriginalId().equals(discriminatorProperty.getOriginalId())  )
//				return nodeProperty;
//		}
//		return null;
//	}
//	
//	public ArrayList<NodeProperty> getMandatoryProperteOf(NodeProperty discriminatorProperty){
//		ArrayList<NodeProperty> mandatoryProperties = new ArrayList<NodeProperty>();
//		if(filterProperty.getName().equals(discriminatorProperty.getName())) {
//			for(NodeProperty property : this.mandatoryProperties) {
//				mandatoryProperties.add(property);
//			}
//		}
//		return mandatoryProperties;
//	}
	
	public String toString() {
		String text = "";
//		Process restructured from version V0.2.
//		for(NodeProperty property : this.mandatoryProperties) {
//			text += property.getName() + " | ";
//		}
		if(!text.equals("")) {
			text  = " (MANDATORY: " + text.substring(0, text.length() - 3) + ")";
		}
		return filterProperty.getName() + " = " + value.toString() + text;
		
//		String text = "";
//		for(NodeProperty property : this.mandatoryProperties) {
//			text += property.getName() + "["+property.getID() + "]" + " | ";
//		}
//		if(!text.equals("")) {
//			text  = " (MANDATORY: " + text.substring(0, text.length() - 3) + ")";
//		}
//		return filterProperty.getName() + " = " + value.toString() + "["+filterProperty.getID() + "]" + text;
	}
}
