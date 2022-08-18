package br.ufes.inf.nemo.ontoumltodb.transformation.convert2er;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;

public class SolvesForeignKey {

	public static void solves(Graph graph) {
		
		ArrayList<GraphAssociation> associations = graph.getAssociations();

		resolve1To1(associations);
		
		resolve1To01(associations);
		
		resolve01To01(associations);

		resolve1ToN(associations);
	}
	
	/**
	 * This method resolves (1:1) associations
	 * @param associations
	 */
	private static void resolve1To1(ArrayList<GraphAssociation> associations) {
		GraphAssociation association;
		int i = 0;
		while (i < associations.size()) {
			association = associations.get(i);

			if (is1To1(association)) {
				associations.remove(i);
				association.setTargetCardinality(Cardinality.C0_1);
				propagateForeignKey(association.getTargetNode(), association.getSourceCardinality(), association.getSourceNode(), association, true);
			} else {
				i++;
			}
		}
	}
	
	/**
	 * This method resolves (1:0..1) associations
	 * @param associations
	 */
	private static void resolve1To01(ArrayList<GraphAssociation> associations) {
		GraphAssociation association;
		int i = 0;
		while (i < associations.size()) {
			association = associations.get(i);

			if (is1To01(association)) {
				associations.remove(i);
				if (association.getSourceCardinality() == Cardinality.C1) {
					propagateForeignKey(association.getSourceNode(), association.getTargetCardinality(), association.getTargetNode(), association, true);
				}
				else {
					propagateForeignKey(association.getTargetNode(), association.getSourceCardinality(), association.getSourceNode(), association, true);
				}
			} else {
				i++;
			}
		}
	}
	
	/**
	 * This method resolves (0..1:0..1) associations
	 * @param associations
	 */
	private static void resolve01To01(ArrayList<GraphAssociation> associations) {
		GraphAssociation association;
		GraphAssociation originalAssociation;
		
		int i = 0;
		while (i < associations.size()) {
			association = associations.get(i);

			if (is01To01(association)) {
				associations.remove(i);
				originalAssociation = association.getOriginalAssociation();
				
				if(originalAssociation.getTargetCardinality() == Cardinality.C1) {
					propagateForeignKey(association.getTargetNode(), association.getSourceCardinality(), association.getSourceNode(), association, false);
				}
				else {
					if(originalAssociation.getSourceCardinality() == Cardinality.C1)
						propagateForeignKey(association.getSourceNode(), association.getTargetCardinality(), association.getTargetNode(), association, false);
					else {
						propagateForeignKey(association.getSourceNode(), association.getTargetCardinality(), association.getTargetNode(), association, false);
						propagateForeignKey(association.getTargetNode(), association.getSourceCardinality(), association.getSourceNode(), association, false);
					}
				}
			} else {
				i++;
			}
		}
	}
	
	/**
	 * This method resolves (1:N) associations. (1:N) is the same that (1:0..N) or (1:1..N) or (0..1:0..N) or (0..1:1..N)
	 * (1:0..N) or (1:1..N) the fk is propagated as obligatory.
	 * (0..1:0..N) or (0..1:1..N) the fk is propagated as optional.
	 * @param associations
	 */
	private static void resolve1ToN(ArrayList<GraphAssociation> associations) {
		GraphAssociation association;
		Node sourceNode, targetNode;
		Cardinality cardinalityFrom;
		int i = 0;
		while (i < associations.size()) {
			association = associations.get(i);

			if (is1ToN(association)) {
				associations.remove(i);
				sourceNode = getSourceNode1ToN(association);
				targetNode = getTargetNode1ToN(association);
				cardinalityFrom = getSourceCardinality1ToN(association);
				propagateForeignKey(sourceNode, cardinalityFrom , targetNode, association, false);
			} else {
				i++;
			}
		}
	}
	
	
	// **************************************************************************************************************
	
	/**
	 * Return if the association is (1:1).
	 * @param association
	 * @return
	 */
	private static boolean is1To1(GraphAssociation association) {
		if (association.getSourceCardinality() == Cardinality.C1   && association.getTargetCardinality() == Cardinality.C1)
			return true;
		else
			return false;
	}
	
	private static boolean is1To01(GraphAssociation association) {
		if (	(association.getSourceCardinality() == Cardinality.C1) && (association.getTargetCardinality() == Cardinality.C0_1)
				||
				(association.getSourceCardinality() == Cardinality.C0_1) && (association.getTargetCardinality() == Cardinality.C1))
			return true;
		else
			return false;
	}
	
	private static boolean is01To01(GraphAssociation association) {
		if ((association.getSourceCardinality() == Cardinality.C0_1) && (association.getTargetCardinality() == Cardinality.C0_1))
			return true;
		else
			return false;
	}
	
	private static boolean is1ToN(GraphAssociation association) {
		if (	(association.getSourceCardinality() == Cardinality.C1) && (association.getTargetCardinality() == Cardinality.C0_N) ||
				(association.getSourceCardinality() == Cardinality.C1) && (association.getTargetCardinality() == Cardinality.C1_N) ||
				(association.getSourceCardinality() == Cardinality.C0_1) && (association.getTargetCardinality() == Cardinality.C0_N) ||
				(association.getSourceCardinality() == Cardinality.C0_1) && (association.getTargetCardinality() == Cardinality.C1_N) ||
				
				(association.getSourceCardinality() == Cardinality.C0_N) && (association.getTargetCardinality() == Cardinality.C1) ||
				(association.getSourceCardinality() == Cardinality.C0_N) && (association.getTargetCardinality() == Cardinality.C0_1) ||
				(association.getSourceCardinality() == Cardinality.C1_N) && (association.getTargetCardinality() == Cardinality.C1) ||
				(association.getSourceCardinality() == Cardinality.C1_N) && (association.getTargetCardinality() == Cardinality.C0_1)
				)
			return true;
		else
			return false;
	}
	
	private static Node getSourceNode1ToN(GraphAssociation association) {
		if (	(association.getSourceCardinality() == Cardinality.C1) || (association.getSourceCardinality() == Cardinality.C0_1) )
			return association.getSourceNode();
		
		if (	(association.getTargetCardinality() == Cardinality.C1) || (association.getTargetCardinality() == Cardinality.C0_1) )
			return association.getTargetNode();
		
		return null;
	}
	
	private static Node getTargetNode1ToN(GraphAssociation association) {
		if (	(association.getTargetCardinality() == Cardinality.C0_N) || (association.getTargetCardinality() == Cardinality.C1_N) )
			return association.getTargetNode();
		
		if (	(association.getSourceCardinality() == Cardinality.C0_N) || (association.getSourceCardinality() == Cardinality.C1_N) )
			return association.getSourceNode();
		return null;
	}
	
	private static Cardinality getSourceCardinality1ToN(GraphAssociation association) {
		if (	(association.getSourceCardinality() == Cardinality.C0_1) || (association.getSourceCardinality() == Cardinality.C1) )
			return association.getSourceCardinality();
		
		if (	(association.getTargetCardinality() == Cardinality.C0_1) || (association.getTargetCardinality() == Cardinality.C1) )
			return association.getTargetCardinality();
		
		return null;
	}
	
	// **************************************************************************************************************
	
	private static void propagateForeignKey(Node from, Cardinality cardinalityFrom, Node to, GraphAssociation association, boolean asPk) {
		NodeProperty fk = from.getPrimaryKey().clone(to, from.getPrimaryKey().getName() + '_' + Increment.getNext());

		fk.setForeignNodeID(from.getID(), association, from.getPrimaryKey());

		if (cardinalityFrom == Cardinality.C0_1 || cardinalityFrom == Cardinality.C0_N) {
			fk.setNullable(true);
		} else {
			fk.setNullable(false);
		}
		
		if(asPk) {
			fk.setPrimaryKey(true);
			fk.setPKAutoIncrement(false);
			to.removeProperty(to.getPrimaryKey().getID());
			
			to.addPropertyAt(0, fk);
		}
		else{
			fk.setPrimaryKey(false);
			fk.setPKAutoIncrement(false);
			to.addPropertyAt(1, fk);
		}
	}
}
