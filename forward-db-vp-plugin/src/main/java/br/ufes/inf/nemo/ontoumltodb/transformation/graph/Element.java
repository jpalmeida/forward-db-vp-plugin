package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import br.ufes.inf.nemo.ontoumltodb.util.ElementType;
import br.ufes.inf.nemo.ontoumltodb.util.Origin;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class Element {
	private String id;
	private String originalId;
	private String name;
	private String originalName;
	private ElementType elementType;
	private boolean resolved;
	private Origin origin;
	
	private int posX;
	private int posY;
	private int height;
	private int width;
	
	public Element(String id, String originalId, String name, ElementType elementType) {
		this.elementType = elementType;
		this.id = id;
		this.originalId = (id == null ? id : originalId);
		this.name = formatName(name);
		this.originalName = formatName(name);
		this.resolved = false;
		this.posX = 0;
		this.posY = 0;
		this.height = 0;
		this.width = 0;
		this.origin = Origin.CREATION;
	}
	
	private String formatName(String name) {
		if(name == null) {
			return "unspecified";
		}else {
			if(name.trim().length() == 0) {
				return "unspecified";
			}
			else {
				return Util.removeSpecialChar(name);
			}
		}
	}

	/**
	 * Returns the element ID.
	 */
	public String getID() {
		return this.id;
	}
	
	/**
	 * Set the element ID.
	 */
	public void setID(String id) {
		this.id = id;
	}
	
	/**
	 * Returns the original element ID
	 * @return
	 */
	public String getOriginalId() {
		return this.originalId;
	}
	
	public String getOriginalName() {
		return this.originalName;
	}

	/**
	 * Returns the element name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Set the element name.
	 */
	public void setName(String name) {
		this.name = formatName(name);
	}

	/**
	 * Returns the element type
	 * @return
	 */
	public ElementType getElementType() {
		return elementType;
	}

	/**
	 * Informs if the graphic element has already been resolved by some process.
	 * 
	 * @param flag
	 */
	public void setResolved(boolean flag) {
		this.resolved = flag;
	}
	
	/**
	 * Returns if the graphic element has already been resolved by some process.
	 * @return
	 */
	public boolean isResolved() {
		return this.resolved;
	}
	
	/*
	 * Informs the X and Y position, as well the height an width 
	 */
	public void setPostion(int posX, int posY, int width, int height) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height= height;
	}
	
	/*
	 * Returns the x position
	 */
	public int getPositionX() {
		return this.posX;
	}
	
	/*
	 * Return the y position
	 */
	public int getPositionY() {
		return this.posY;
	}
	
	/*
	 * Returns the element Height.
	 */
	public int getHeight() {
		return this.height;
	}
	
	/*
	 * Returns the element width.
	 */
	public int getWidth() {
		return this.width;
	}
	
	public Origin getOrigin() {
		return this.origin;
	}
	
	public void setOrigin(Origin origin) {
		this.origin = origin;
	}
	
	/**
	 * Returns if the id passed as an argument is the same as the id of the 
	 * queried element.
	 * 
	 * @param id
	 * @return
	 */
	public boolean isMyId(String id) {
		return this.id.equals(id) ? true : false;
	}
}
