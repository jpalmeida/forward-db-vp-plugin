/**
 * This class is responsible for storing the attribute data.
 *
 * Author: 
 */

package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import br.ufes.inf.nemo.ontoumltodb.util.ElementType;
import br.ufes.inf.nemo.ontoumltodb.util.IndexType;
import br.ufes.inf.nemo.ontoumltodb.util.Origin;

public class NodeProperty extends Element{
	private Node ownerNode;
	// attributes intended for the class
	private String dataType;
	private boolean acceptNull;
	private boolean acceptedNull;
	private boolean multivalued;

	// attributes intended for the construction of the table
	private boolean isPK;
	private boolean isPKAutoIncrement;
	private boolean isFK;
	private String foreignNodeID;
	private GraphAssociation associationRelated;
	private NodeProperty primaryKeyRelated; 
	private Object defaultValue;
	private IndexType indexType;
	
	// attributes intended for the trace
	private boolean identifyOtherClass;
	private boolean generatedFromTransformationProcess;
	private Origin origin;
	
	// properties intended for constraint generation
	private NodeProperty belongsDiscriminatoryProperty;
	private boolean mandatoryFillingWhenMandatoryPropertyIsFilled;
	private String discriminatoryValue;

	public NodeProperty(Node ownerNode, String id, String name, String dataType, boolean acceptNull, boolean multivalued) {
		super(id, id, name, ElementType.PROPERTY);

		initialize(ownerNode, dataType, acceptNull, multivalued);
	}
	
	public NodeProperty(Node ownerNode, String id, String originalId, String name, String dataType, boolean acceptNull, boolean multivalued) {
		super(id, originalId, name, ElementType.PROPERTY);
		
		initialize(ownerNode, dataType, acceptNull, multivalued);
	}
	
	private void initialize(Node ownerNode, String dataType, boolean acceptNull, boolean multivalued) {
		this.ownerNode = ownerNode;
		this.dataType = dataType;
		this.acceptNull = acceptNull;
		this.acceptedNull = acceptNull;
		this.multivalued = multivalued;
		
		this.isPK = false;
		this.isPKAutoIncrement = false;
		this.isFK = false;
		this.defaultValue = null;
		this.associationRelated = null;
		this.primaryKeyRelated = null;
		this.indexType = IndexType.NOINDEX;
		this.identifyOtherClass = false;
		this.generatedFromTransformationProcess = false;
		this.origin = Origin.CREATION;
		this.belongsDiscriminatoryProperty = null;
		this.mandatoryFillingWhenMandatoryPropertyIsFilled = false;
		this.discriminatoryValue = null;
	}
	
	public Node getOwnerNode() {
		return ownerNode;
	}
	
	public void setOwnerNode(Node node) {
		this.ownerNode = node;
	}

	/**
	 * Informs the property data type.
	 *
	 * @param dataType. Name of the property type.
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * Returns the property data type.
	 *
	 * @return string
	 */
	public String getDataType() {
		return this.dataType;
	}

	/**
	 * Informs that the property is a primary key.
	 *
	 * @param flag. If true, the property will be marked as a primary key.
	 */
	public void setPrimaryKey(boolean flag) {
		this.isPK = flag;
		this.isPKAutoIncrement = flag;
		if (flag) {
			this.acceptNull = false;
		}
	}

	/**
	 * Returns whether the property is marked as primary key.
	 *
	 * @return boolean
	 */
	public boolean isPrimaryKey() {
		return this.isPK;
	}

	/**
	 * Informs whether the Primary Key is auto-incrementing or not. By default, the
	 * primary key is auto-incremented.
	 * 
	 * @param flag
	 */
	public void setPKAutoIncrement(boolean flag) {
		this.isPKAutoIncrement = flag;
	}

	/**
	 * Returns whether the primary key is auto-increment.
	 * 
	 * @return boolean
	 */
	public boolean isPrimaryKeyAutoIncrement() {
		return this.isPKAutoIncrement;
	}

	/**
	 * Informs which node the property (marked as a foreign key) refers to. This is
	 * necessary because the foreign key name may be different from the primary key
	 * name of the referenced table. This method marks the property as foreign key.
	 *
	 * @param foreignNode. Node to be referenced.
	 */
	public void setForeignNodeID(String foreignNodeID, GraphAssociation associationRelated, NodeProperty primaryKeyRelated) {
		if(foreignNodeID != null && associationRelated != null  && primaryKeyRelated != null) {
			this.isFK = true;
			this.foreignNodeID = foreignNodeID;
			this.associationRelated = associationRelated;
			this.primaryKeyRelated = primaryKeyRelated;
		}
	}

	/**
	 * Returns the ID of the node referenced by the foreign key.
	 *
	 * @return string
	 */
	public String getForeignKeyNodeID() {
		return this.foreignNodeID;
	}

	/**
	 * Returns if the property is marked as a foreign key.
	 *
	 * @return boolean
	 */
	public boolean isForeignKey() {
		return this.isFK;
	}

	/**
	 * Returns the association related of Foreign Key.
	 * 
	 * @return GraphAssociation
	 */
	public GraphAssociation getAssociationRelatedOfFK() {
		return this.associationRelated;
	}
	
	public NodeProperty getPrimaryKeyRelated() {
		return this.primaryKeyRelated;
	}

	/**
	 * Informs whether the property accepts null.
	 *
	 * @param flag. If true, the property accepts null.
	 */
	public void setNullable(boolean flag) {
		this.acceptNull = flag;
	}

	/**
	 * Returns whether the property accepts null.
	 *
	 * @return boolean
	 */
	public boolean isNullable() {
		return this.acceptNull;
	}
	
	/**
	 * Returns whether the property accepted null in the source model.
	 *
	 * @return boolean
	 */
	public boolean isWasNullable() {
		return this.acceptedNull;
	}

	/**
	 * Informs whether the property is multivalued.
	 *
	 * @param flag. If true, the property is multivalued.
	 */
	public void setMultivalued(boolean flag) {
		this.multivalued = flag;
	}

	/**
	 * Returns whether the property is multivalued.
	 *
	 * @return boolean
	 */
	public boolean isMultivalued() {
		return this.multivalued;
	}

	/**
	 * Informs a default value for the property.
	 *
	 * @param value. The default value.
	 */
	public void setDefaultValue(Object value) {
		this.defaultValue = value;
	}

	/**
	 * Returns the default value of the property.
	 *
	 * @return any
	 */
	public Object getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * Informs if an index will be created for this field.
	 * 
	 * @param flag
	 */
	public void setIndexType(IndexType indexType) {
		this.indexType = indexType;
	}
	
	public IndexType getIndexType() {
		return this.indexType;
	}

	/**
	 * Returns whether it is necessary to create an index for this field.
	 * 
	 * @returns boolean
	 */
	public boolean isIndex() {
		if(this.indexType != IndexType.NOINDEX)
			return false;
		else return true;
	}
	
	public void setIdentifyOtherClass(boolean flag) {
		this.identifyOtherClass = flag;
	}
	
	public boolean isIdentifyOtherClass() {
		return this.identifyOtherClass;
	}
	
	public void setGeneratedFromTransformationProcess(boolean flag) {
		this.generatedFromTransformationProcess = flag;
	}
	
	public boolean isGeneratedFromTransformationProcess() {
		return this.generatedFromTransformationProcess;
	}
	
	public void setRecivedBy(Origin origin) {
		this.origin = origin;
	}
	
	public Origin getOrigin() {
		return this.origin;
	}
	
	public boolean isSelfAssociation() {
		if(associationRelated.getSourceNode().isMyId(associationRelated.getTargetNode().getID()))
			return true;	
		return false;
	}
	
	public boolean isBelongsDiscriminatoryProperty() {
		if(this.belongsDiscriminatoryProperty != null)
			return true;
		else return false;
	}
	
	public NodeProperty getDiscriminatoryProperty() {
		return this.belongsDiscriminatoryProperty;
	}
	
	public boolean isMandatoryFillingWhenMandatoryPropertyIsFilled() {
		return this.mandatoryFillingWhenMandatoryPropertyIsFilled;
	}
	
	public String getDiscriminatoryValue() {
		return this.discriminatoryValue;
	}
	
	public void setDiscriminatoryProperty(NodeProperty property, boolean mandatoryFillingWhenMandatoryPropertyIsFilled, String discriminatoryValue) {
		// lose the first mandatory property in the lifting process, necessary for correct link  
		if(this.belongsDiscriminatoryProperty == null) {
			this.belongsDiscriminatoryProperty = property;
			this.discriminatoryValue = discriminatoryValue;
			this.mandatoryFillingWhenMandatoryPropertyIsFilled = mandatoryFillingWhenMandatoryPropertyIsFilled;
		}
	}
	
	public void setDiscriminatoryProperty(NodeProperty property) {
		this.belongsDiscriminatoryProperty = property;
	}

	/**
	 * Returns a new property with the same values of the current property.
	 *
	 * @return IOntoProperty.
	 */
	public NodeProperty clone(Node newOwner, String newKey) {
		NodeProperty newProperty = new NodeProperty(
						newOwner == null ? ownerNode : newOwner,
						newKey == null ? getID() : newKey, 
						getOriginalId(),
						getName(), 
						this.dataType,
						this.acceptNull, 
						this.multivalued);
		
		newProperty.setPrimaryKey(this.isPK);
		newProperty.setPKAutoIncrement(this.isPKAutoIncrement);
		newProperty.setForeignNodeID(this.foreignNodeID, this.associationRelated, this.primaryKeyRelated);
		newProperty.setDefaultValue(this.defaultValue);
		newProperty.setIdentifyOtherClass(this.identifyOtherClass);
		newProperty.setGeneratedFromTransformationProcess(this.generatedFromTransformationProcess);
		newProperty.setRecivedBy(this.origin);
		if(this.belongsDiscriminatoryProperty != null)
			newProperty.setDiscriminatoryProperty(this.belongsDiscriminatoryProperty, this.mandatoryFillingWhenMandatoryPropertyIsFilled, this.discriminatoryValue);
				
		return newProperty;
	}

	public String toString() {
		String msg = "";
		String msgMandatory = "";
		
		if(this.isForeignKey()) {
			if(this.associationRelated == null) {
				msg = "[ ********** NOT EXISTS ASSOCIATION RELATED TO FK **********]";
			}
			else msg = this.associationRelated.toString();
		}
		
		if(belongsDiscriminatoryProperty != null) {
			msgMandatory += "[MANDATORY PROPERTY: " + belongsDiscriminatoryProperty.getName() + "]";
		}
		
		return 	"PROPERTY: "+
				getName() + 
//				" ["+
//				getID()+
//				"]"+
				" : " + this.dataType + 
				", " + 	(this.acceptNull == true ? "\tNULL" : "\tNOT NULL") + 
				" " +
				(this.isPrimaryKey() == true ? " PK ": "")+
				(this.isForeignKey() == true ? " FK " + msg: "")+
				(this.multivalued == true ? "\tMULTIVALUED" : "")+
				msgMandatory; 
	}

}
