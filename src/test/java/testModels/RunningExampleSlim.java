package testModels;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphGeneralization;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphGeneralizationSet;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class RunningExampleSlim {

	public static Graph getGraph() {
		Graph graph = new Graph();
		
		Node namedEntity = new Node(Increment.getNextS(), "NamedEntity", Stereotype.CATEGORY);
		Node customer = new Node(Increment.getNextS(), "Customer", Stereotype.ROLE_MIXIN);
		
		Node person = new Node(Increment.getNextS(), "Person", Stereotype.KIND);
		Node organization = new Node(Increment.getNextS(), "Organization", Stereotype.KIND);
		
		Node contractor = new Node(Increment.getNextS(), "Contractor", Stereotype.ROLE);
		Node personalCustomer = new Node(Increment.getNextS(), "PersonalCustomer", Stereotype.ROLE);
		Node corporateCustomer = new Node(Increment.getNextS(), "CorporateCustomer", Stereotype.ROLE);
		
		Node child = new Node(Increment.getNextS(), "Child", Stereotype.PHASE);
		Node adult = new Node(Increment.getNextS(), "Adult", Stereotype.PHASE);
		
		Node supplyContract = new Node(Increment.getNextS(), "SupplyContract", Stereotype.RELATOR);
				
		namedEntity.addProperty(new NodeProperty(namedEntity, Increment.getNextS(),"name","string",false, false ));
		
		customer.addProperty(new NodeProperty(customer, Integer.toString(Increment.getNext()),"creditRating","double",false,false));
		
		person.addProperty(new NodeProperty(person, Integer.toString(Increment.getNext()),"birthDate","Date",false,false));
		
		organization.addProperty(new NodeProperty(organization, Integer.toString(Increment.getNext()),"address","string",false,false));
	
		personalCustomer.addProperty(new NodeProperty(personalCustomer, Integer.toString(Increment.getNext()),"creditCard","string",false,false));
		
		corporateCustomer.addProperty(new NodeProperty(corporateCustomer, Integer.toString(Increment.getNext()),"creditLimit","int",false,false));
		
		supplyContract.addProperty(new NodeProperty(supplyContract, Integer.toString(Increment.getNext()),"contractValue","double",false,false));
		
		GraphAssociation aCustomerContract = new GraphAssociation(
				Increment.getNextS(), 
				"hasCustomer", 
				customer, 
				Cardinality.C1, 
				supplyContract, 
				Cardinality.C1_N);
		
		GraphAssociation aContractorContract = new GraphAssociation(
				Increment.getNextS(), 
				"hasContractor", 
				contractor, 
				Cardinality.C1, 
				supplyContract, 
				Cardinality.C1_N);
		
		
		GraphGeneralization gNamedEntityPerson = new GraphGeneralization(Increment.getNextS(),namedEntity,person);
		GraphGeneralization gNamedEntityOrganization = new GraphGeneralization(Increment.getNextS(),namedEntity,organization);
		GraphGeneralization gPersonChild = new GraphGeneralization(Increment.getNextS(),person,child);
		GraphGeneralization gPersonAdult = new GraphGeneralization(Increment.getNextS(),person,adult);
		GraphGeneralization gAdultPersonal = new GraphGeneralization(Increment.getNextS(),adult,personalCustomer);
		GraphGeneralization gOrganizationContractor = new GraphGeneralization(Increment.getNextS(),organization,contractor);
		GraphGeneralization gOrganizationCustomer = new GraphGeneralization(Increment.getNextS(),organization,corporateCustomer);
		GraphGeneralization gCustomerPersonal = new GraphGeneralization(Increment.getNextS(),customer,personalCustomer);
		GraphGeneralization gCustomerCorporate = new GraphGeneralization(Increment.getNextS(),customer,corporateCustomer);
		
		
		GraphGeneralizationSet gsNamedEntityType = new GraphGeneralizationSet(Increment.getNextS(), "NamedEntityType", true, false);
		GraphGeneralizationSet gsLifePhase = new GraphGeneralizationSet(Increment.getNextS(), "LifePhase", true, true);
		GraphGeneralizationSet gsCustomerType = new GraphGeneralizationSet(Increment.getNextS(), "CustomerType", true, false);
		
		gsNamedEntityType.addGeneralization(gNamedEntityPerson);
		gsNamedEntityType.addGeneralization(gNamedEntityOrganization);
		gsLifePhase.addGeneralization(gPersonChild);
		gsLifePhase.addGeneralization(gPersonAdult);
		gsCustomerType.addGeneralization(gCustomerPersonal);
		gsCustomerType.addGeneralization(gCustomerCorporate);
		
		
		graph.addNode(child);
		graph.addNode(personalCustomer);
		graph.addNode(corporateCustomer);
		graph.addNode(adult);
		graph.addNode(contractor);
		graph.addNode(namedEntity);
		graph.addNode(customer);
		graph.addNode(person);
		graph.addNode(organization);
		graph.addNode(supplyContract);
		
		graph.addAssociation(aCustomerContract);
		graph.addAssociation(aContractorContract);
		graph.addAssociation(gNamedEntityPerson);
		graph.addAssociation(gNamedEntityOrganization);
		graph.addAssociation(gPersonChild);
		graph.addAssociation(gPersonAdult);
		graph.addAssociation(gAdultPersonal);
		graph.addAssociation(gOrganizationContractor);
		graph.addAssociation(gOrganizationCustomer);
		graph.addAssociation(gCustomerPersonal);
		graph.addAssociation(gCustomerCorporate);
		
		graph.addGeneralizationSet(gsNamedEntityType);
		graph.addGeneralizationSet(gsLifePhase);
		graph.addGeneralizationSet(gsCustomerType);
		return graph;
	}

}
