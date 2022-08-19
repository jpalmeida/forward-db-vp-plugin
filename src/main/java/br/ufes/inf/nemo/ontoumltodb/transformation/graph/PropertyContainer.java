/**
 * Class responsible for storing all properties (attributes) of the class.
 *
 * Author: 
 */

package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import java.util.ArrayList;

public class PropertyContainer {

	private ArrayList<NodeProperty> properties;

	public PropertyContainer() {
		this.properties = new ArrayList<NodeProperty>();
	}

	/**
	 * Adds a new property in the container.
	 *
	 * @param property. Property to be added.
	 */
	public void addProperty(NodeProperty property) {
		if (!this.existsPropertyName(property.getName())) {
			this.properties.add(property);
		}
	}

	/**
	 * Adds a set of property in the container.
	 *
	 * @param properties. An ArrayList with the properties to be added.
	 */
	public void addProperties(ArrayList<NodeProperty> newProperties) {
		for (NodeProperty newProperty : newProperties) {
			addProperty(newProperty);
		}
	}

	/**
	 * Adds a new property in the container in a specific position.
	 *
	 * @param index.    Position in which the property will be added.
	 * @param property. Property to be added.
	 */	
	public void addPropertyAt(int index, NodeProperty property) {
		//it is necessary to allow duplicate attributes because is possible to have 
		//two or more associations with the same class/table
		this.properties.add(index, property);
	}

	/**
	 * Adds a set of property in the container from a specific position
	 *
	 * @param index.      Initial position to be added to the properties.
	 * @param properties. Properties to be added.
	 */
	public void addPropertiesAt(int index, ArrayList<NodeProperty> properties) {
		for (NodeProperty nodeProperty : properties) {
			addPropertyAt(index, nodeProperty);
			index++;
		}
	}

	/**
	 * Returns the property with the name.
	 * @param id
	 * @return
	 */
	public NodeProperty getPropertyByName(String name) {
		for (NodeProperty nodeProperty : this.properties) {
			if (nodeProperty.getName().equals(name))
				return nodeProperty;
		}
		return null;
	}

	/**
	 * Returns all properties of the container.
	 *
	 * @return An ArrayList with all properties.
	 */
	public ArrayList<NodeProperty> getProperties() {
		ArrayList<NodeProperty> result = new ArrayList<NodeProperty>();
		for (NodeProperty nodeProperty : properties) {
			result.add(nodeProperty);
		}
		return result;
	}

	/**
	 * Removes a specific property of the container.
	 *
	 * @param property. Property to be removed.
	 */
	public void removeProperty(String id) {
		int index = 0;
		for (NodeProperty nodeProperty : properties) {
			if (nodeProperty.getID().equals(id)) {
				this.properties.remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Returns the property marked as the primary key.
	 *
	 * @return The primary key property
	 */
	public NodeProperty getPrimaryKey() {
		for (NodeProperty nodeProperty : properties) {
			if (nodeProperty.isPrimaryKey())
				return nodeProperty;
		}
		return null;
	}
	
	public ArrayList<NodeProperty> getForeignKeys(){
		ArrayList<NodeProperty> result = new ArrayList<NodeProperty>();
		for (NodeProperty nodeProperty : properties) {
			if(nodeProperty.isForeignKey())
				result.add(nodeProperty);
		}
		return result;
	}

	/**
	 * Finds the property marked as primary key and returns its name.
	 *
	 * @return A string with the primary key name.
	 */
	public String getPKName() {
		for (NodeProperty nodeProperty : properties) {
			if (nodeProperty.isPrimaryKey()) {
				return nodeProperty.getName();
			}
		}
		return "[Did not find the pk name]";
	}

	/**
	 * Checks if there is any property with the given name.
	 *
	 * @param propertyName. Property name to be searched.
	 * @return True if the property name exists in the container, otherwise false.
	 */
	public boolean existsPropertyName(String propertyName) {
		for (NodeProperty nodeProperty : properties) {
			if (nodeProperty.getName().equals(propertyName))
				return true;
		}
		return false;
	}

	/**
	 * Checks if the property exists in the container. The verification is done by
	 * the ID property.
	 *
	 * @param property Property to be searched.
	 */
	public boolean existsProperty(NodeProperty property) {
		for (NodeProperty nodeProperty : properties) {
			if (nodeProperty.getOriginalId().equals(property.getID()))
				return true;
		}
		return false;
	}

	/**
	 * Clone the container and indicate which node it belongs to.
	 *
	 * @param sourceNode. Node to which the container belongs.
	 * @return A new container with new properties.
	 */
	public PropertyContainer clone(Node newNode) {
		PropertyContainer container = new PropertyContainer();

		for (NodeProperty nodeProperty : properties) {
			container.addProperty(nodeProperty.clone(newNode, null));
		}

		return container;
	}

	/**
	 * Returns the FK related to the node ID.
	 *
	 * @param node
	 */
	public NodeProperty getFKRelatedOfNodeID(String id) {
		for (NodeProperty property : this.properties) {
			if(property.getForeignKeyNodeID() != null) {
				if (property.getForeignKeyNodeID().equals(id)) {
					return property;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the property related to the ID.
	 * @param id
	 * @return
	 */
	public NodeProperty getPropertyById(String id) {
		for (NodeProperty property : this.properties) {
			if (property.getID().equals(id)) {
				return property;
			}
		}
		return null;
	}
	
	/**
	 * Returns the property related to the original ID.
	 * @param id
	 * @return
	 */
	public NodeProperty getPropertyByOriginalId(String originalId) {
		for (NodeProperty property : this.properties) {
			if (property.getOriginalId().equals(originalId)) {
				return property;
			}
		}
		return null;
	}
	
	public NodeProperty getPropertyByOriginalName(String name) {
		for (NodeProperty property : this.properties) {
			if (property.getOriginalName().equals(name)) {
				return property;
			}
		}
		return null;
	}
	
	public boolean isNecessaryGenerateMC3_4Constraint() {
		for (NodeProperty property : this.properties) {
			if(property.hasMandatoryProperty())
				return true;
		}
		return false;
	}
	
	public void atualizeMandatoryProperties() {
		NodeProperty mandatoryProperty;
		for (NodeProperty property : this.properties) {
			if(property.hasMandatoryProperty()) {
				mandatoryProperty = property.getMandatoryProperty(); // old property
				mandatoryProperty = getPropertyByName(mandatoryProperty.getName()); // get new property
				property.setMandatoryProperty(mandatoryProperty); // atualize new property
			}
		}
	}

	/**
	 * Returns all properties formatted as a string.
	 */
	public String toString() {
		String msg = "";
		for (NodeProperty nodeProperty : properties) {
			if(nodeProperty instanceof NodePropertyEnumeration)
				msg += "\n\t"+((NodePropertyEnumeration)nodeProperty).toString();
			else msg += "\n\t"+nodeProperty.toString();
		}
		return msg;
	}

}
