/**
 * Class responsible for storing the generalization set data.
 *
 * Author:
 */

package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.util.ElementType;

public class GraphGeneralizationSet extends Element {
	private ArrayList<GraphGeneralization> generalizations;
	
	private boolean disjoint;
	private boolean complete;
	private boolean isResolved;

	public GraphGeneralizationSet(String id,String name, boolean disjoint, boolean complete) {
		super(id, id, name, ElementType.GENERALIZATION_SET);
		generalizations = new ArrayList<GraphGeneralization>();
		this.disjoint = disjoint;
		this.complete = complete;
		this.isResolved = false;
	}
	
	public GraphGeneralizationSet(String id, String originalId, String name, boolean disjoint, boolean complete) {
		super(id, originalId, name, ElementType.GENERALIZATION_SET);
		generalizations = new ArrayList<GraphGeneralization>();
		this.disjoint = disjoint;
		this.complete = complete;
		this.isResolved = false;
	}
	
	/**
	 * Adds a new generalization in the generalization set.
	 * 
	 * @param generalization
	 */
	public void addGeneralization(GraphGeneralization generalization){
		if(generalizations.isEmpty()) {
			generalizations.add(generalization);
			generalization.setBelongGeneralizationSet(this);
		}
		else{
			if(!existsGeneralization(generalization.getID()) && hasSameSuperNode(generalization)) {
				generalizations.add(generalization);
				generalization.setBelongGeneralizationSet(this);
			}
		}
	}
	
	public ArrayList<GraphGeneralization> getGeneralizations(){
		ArrayList<GraphGeneralization> result = new ArrayList<GraphGeneralization>();
		
		for (GraphGeneralization generalization : generalizations) {
			result.add(generalization);
		}
		
		return result;
	}
	
	/**
	 * Informs if exists the generalization ID.
	 * 
	 * @param id
	 * @return
	 */
	public boolean existsGeneralization(String id) {
		for (GraphGeneralization graphGeneralization : generalizations) {
			if(graphGeneralization.getID().equals(id))
				return true;
		}
		return false;
	}
	
	private boolean hasSameSuperNode(GraphGeneralization generalization) {
		if(generalizations.isEmpty())
				return false;
		
		if(generalizations.get(0).getGeneral().getID().equals( generalization.getGeneral().getID()))
			return true;
		else return false;
	}

	/**
	 * Returns the generalization set supernode.
	 *
	 * @return Node with the supernode.
	 */
	public Node getGeneral() {
		if(generalizations.isEmpty())
			return null;
		return this.generalizations.get(0).getGeneral();
	}

	/**
	 * Returns the specialization nodes linked to the generalization set.
	 *
	 * @return An ArrayList with all the specialization nodes.
	 */
	public ArrayList<Node> getSpecializationNodes() {
		ArrayList<Node> result = new ArrayList<Node>();
		for (GraphGeneralization generalization : generalizations) {
			result.add(generalization.getSpecific());
		}
		return result;
	}

	/**
	 * Informs if the generalization set is disjoint(true). Otherwise is overlapping(false)
	 * @param flag
	 */
	public void setDisjoint(boolean flag) {
		this.disjoint = flag;
	}
		
	/**
	 * Checks whether the generalization set is disjoint.
	 *
	 * @return True if the generalization set is disjoint and false if it is
	 *         overlapping.
	 */
	public boolean isDisjoint() {
		return this.disjoint;
	}

	/**
	 * Informs if the generalization set is complete (true). Otherwise is incomplete (false).
	 * @param flag
	 */
	public void setComplete(boolean flag) {
		this.complete = flag;
	}
	
	/**
	 * Checks whether the generalization set is classified as incomplete.
	 *
	 * @return True if the generalization set is classified as complete and false if
	 *         it is incomplete.
	 */
	public boolean isComplete() {
		return this.complete;
	}
	
	/**
	 * Tells if the generalization set has already been resolved
	 * 
	 * @return
	 */
	public boolean isResolved() {
		return isResolved;
	}
	
	/**
	 * Sets the set of generalizations as resolved.
	 */
	public void setResolved(boolean isResolved) {
		this.isResolved = isResolved;
	}
	
	/**
	 * Clone the current generalization set with new GraphGeneralization. If the newGeneralizations is null, 
	 * clone with the current GraphGeneralizations
	 */
	public GraphGeneralizationSet clone(ArrayList<GraphGeneralization> newGeneralizations) {
		GraphGeneralizationSet newGs = new GraphGeneralizationSet(getID(), getOriginalId(), getName(), this.disjoint, this.complete);
		
		ArrayList<GraphGeneralization> cloneGeneralization;
		
		if(newGeneralizations != null) 
			cloneGeneralization = newGeneralizations;
		else cloneGeneralization = this.generalizations;
		
		
		for (GraphGeneralization generalization : cloneGeneralization) {
			newGs.addGeneralization(generalization);
		}
		return newGs;
	}
	

	/**
	 * Returns the generalization set formatted as string;
	 */
	public String toString() {
		String msg = "";

		msg += "GENERALIZATION SET: " + getGeneral().getName() + " <-GS-(" + getName() + ")- [";

		for (Node node : getSpecializationNodes()) {
			msg += node.getName() + " | ";
		}
		msg += "]";
		
		return msg;
	}

}
