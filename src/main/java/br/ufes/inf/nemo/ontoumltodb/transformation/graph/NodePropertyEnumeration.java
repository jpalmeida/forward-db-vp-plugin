/**
 * The Enumeration is treated as a special type of Property. In addition to
 * having all the features of OntoProperty, it is also capable of adding
 * several values of the same type.
 *
 * Author: 
 */

package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import java.util.ArrayList;

public class NodePropertyEnumeration extends NodeProperty {
	private ArrayList<String> values;
	private boolean generateEnumColumn;
	
	private GraphGeneralizationSet originGeneralizationSet;

	public NodePropertyEnumeration(Node owner, String id, String name, String dataType, boolean isNull, boolean multiValues) {
		super(owner, id, id, name, dataType, isNull, multiValues);
		this.values = new ArrayList<String>();
		this.generateEnumColumn = true;
		this.originGeneralizationSet = null;
	}
	
	public NodePropertyEnumeration(Node owner, String id, String originalId, String name, String dataType, boolean isNull, boolean multiValues) {
		super(owner, id, originalId, name, dataType, isNull, multiValues);
		this.values = new ArrayList<String>();
		this.generateEnumColumn = true;
		this.originGeneralizationSet = null;
	}

	/**
	 * Adds a new value belonging to the Enumeration.
	 *
	 * @param value. Name to be added.
	 */
	public NodePropertyEnumeration addValue(String value) {
		this.values.add(value);
		return this;
	}

	/**
	 * Returns the names belonging to the Enumeration.
	 *
	 * @return The ArrayList with the names.
	 */
	public ArrayList<String> getValues() {
		return this.values;
	}
	
	/**
	 * Informs if an ENUMERATION type field will be created. If is not 
	 * the ENUMERATION type (false), inserts will be created for each 
	 * enumeration value for the respective table (LOOKUP table).
	 * 
	 * @param flag
	 */
	public void setGenerateEnumFiled(boolean flag) {
		this.generateEnumColumn = flag;
	}
	
	/**
	 * Returns whether a field of type ENUMERATION will be created. If 
	 * returns false, indicates that inserts will be created for each 
	 * enumeration value for the respective created table (LOOKUP type table).
	 * 
	 * @return
	 */
	public boolean isGenerateEnumColumn() {
		return this.generateEnumColumn;
	}
	
	public void setOriginGeneralizationSet(GraphGeneralizationSet gs) {
		this.originGeneralizationSet = gs;
	}
	
	public GraphGeneralizationSet getOriginGeneralizationSet() {
		return this.originGeneralizationSet;
	}
	
	public NodePropertyEnumeration clone(Node newOwner, String newKey) {
		NodePropertyEnumeration newProperty = new NodePropertyEnumeration(
				newOwner == null ? getOwnerNode() : newOwner,
				newKey == null ? getID() : newKey, 
				getOriginalId(),
				this.getName(), 
				this.getDataType(),
				this.isNullable(), 
				this.isMultivalued());

		newProperty.setPrimaryKey(this.isPrimaryKey());
		newProperty.setPKAutoIncrement(this.isPrimaryKey());
		newProperty.setForeignNodeID(this.getForeignKeyNodeID(), this.getAssociationRelatedOfFK(), this.getPrimaryKeyRelated());
		newProperty.setDefaultValue(this.getDefaultValue());
		newProperty.setOriginGeneralizationSet(this.originGeneralizationSet);
		if(isBelongsDiscriminatoryProperty())
			newProperty.setDiscriminatoryProperty(getDiscriminatoryProperty(),  isMandatoryFillingWhenMandatoryPropertyIsFilled(), getDiscriminatoryValue());
		
		
		for(String nodeValue : values) {
			newProperty.addValue(nodeValue);
		}

		return newProperty;
	}

	/**
	 * Returns the enumerations as string.
	 */
	public String toString(){
		String result = "[";
		String msgMandatory = "";

		 for (String name : this.values) {
			 result += name + " | ";
		 }
		 result += "]";
		 
		 if(isBelongsDiscriminatoryProperty()) {
				msgMandatory += "[MANDATORY PROPERTY: " + getDiscriminatoryProperty().getName() + "]";
			}
		
		return 	"PROPERTY: "+
				getName() + 
				" : " + getDataType() + 
				", " + 	(isNullable() == true ? "\tNULL" : "\tNOT NULL") + 
				" " + 
				(isMultivalued() == true ? "\tMULTIVALUED" : "")+
				"\t" + result +
				msgMandatory;// + "  ID: " + this.id;
	 }

}
