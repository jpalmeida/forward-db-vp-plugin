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

public class RunningExampleModel {

	public static Graph getGraph() {
		Graph graph = new Graph();
		
		Node namedEntity = new Node(Increment.getNextS(), "NamedEntity", Stereotype.CATEGORY);
		Node customer = new Node(Increment.getNextS(), "Customer", Stereotype.ROLE_MIXIN);
		
		Node person = new Node(Increment.getNextS(), "Person", Stereotype.KIND);
		Node organization = new Node(Increment.getNextS(), "Organization", Stereotype.KIND);
		
		Node primarySchool = new Node(Increment.getNextS(), "PrimarySchool", Stereotype.SUBKIND);
		Node hospital = new Node(Increment.getNextS(), "Hospital", Stereotype.SUBKIND);
		
		Node brazilianCitizen = new Node(Increment.getNextS(), "BrazilianCitizen", Stereotype.ROLE);
		Node italianCitizen = new Node(Increment.getNextS(), "ItalianCitizen", Stereotype.ROLE);
		Node employee = new Node(Increment.getNextS(), "Employee", Stereotype.ROLE);
		Node contractor = new Node(Increment.getNextS(), "Contractor", Stereotype.ROLE);
		Node personalCustomer = new Node(Increment.getNextS(), "PersonalCustomer", Stereotype.ROLE);
		Node corporateCustomer = new Node(Increment.getNextS(), "CorporateCustomer", Stereotype.ROLE);
		
		Node child = new Node(Increment.getNextS(), "Child", Stereotype.PHASE);
		Node adult = new Node(Increment.getNextS(), "Adult", Stereotype.PHASE);
		
		Node employment = new Node(Increment.getNextS(), "Employment", Stereotype.RELATOR);
		Node enrollment = new Node(Increment.getNextS(), "Enrollment", Stereotype.RELATOR);
		Node supplyContract = new Node(Increment.getNextS(), "SupplyContract", Stereotype.RELATOR);
				
		namedEntity.addProperty(new NodeProperty(namedEntity, Increment.getNextS(),"name","string",false, false ));
		
		customer.addProperty(new NodeProperty(customer, Integer.toString(Increment.getNext()),"creditRating","double",false,false));
		
		person.addProperty(new NodeProperty(person, Integer.toString(Increment.getNext()),"birthDate","Date",false,false));
		
		organization.addProperty(new NodeProperty(organization, Integer.toString(Increment.getNext()),"address","string",false,false));
	
		primarySchool.addProperty(new NodeProperty(primarySchool, Integer.toString(Increment.getNext()),"playgroundSize","int",false,false));
		
		hospital.addProperty(new NodeProperty(hospital, Integer.toString(Increment.getNext()),"capacity","int",false,false));
		
		brazilianCitizen.addProperty(new NodeProperty(brazilianCitizen, Integer.toString(Increment.getNext()),"rg","string",false,false));
		
		italianCitizen.addProperty(new NodeProperty(italianCitizen, Integer.toString(Increment.getNext()),"ci","string",false,false));
		
		personalCustomer.addProperty(new NodeProperty(personalCustomer, Integer.toString(Increment.getNext()),"creditCard","string",false,false));
		
		corporateCustomer.addProperty(new NodeProperty(corporateCustomer, Integer.toString(Increment.getNext()),"creditLimit","int",false,false));
		
		employment.addProperty(new NodeProperty(employment, Integer.toString(Increment.getNext()),"salary","double",false,false));		
		
		enrollment.addProperty(new NodeProperty(enrollment, Integer.toString(Increment.getNext()),"grade","int",false,false));
		
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
		
		GraphAssociation aChildEnrollment = new GraphAssociation(
				Increment.getNextS(), 
				"hasChild", 
				child, 
				Cardinality.C1, 
				enrollment, 
				Cardinality.C0_N);
		
		GraphAssociation aSchoolEnrollment = new GraphAssociation(
				Increment.getNextS(), 
				"hasSchool", 
				primarySchool, 
				Cardinality.C1, 
				enrollment, 
				Cardinality.C0_N);
		
		GraphAssociation aEmployeeEmployment = new GraphAssociation(
				Increment.getNextS(), 
				"hasEmployee", 
				employee, 
				Cardinality.C1, 
				employment, 
				Cardinality.C1_N);
		
		GraphAssociation aOrganizationEmployment = new GraphAssociation(
				Increment.getNextS(), 
				"hasOrganization", 
				organization, 
				Cardinality.C1, 
				employment, 
				Cardinality.C0_N);
		
		GraphGeneralization gNamedEntityPerson = new GraphGeneralization(Increment.getNextS(),namedEntity,person);
		GraphGeneralization gNamedEntityOrganization = new GraphGeneralization(Increment.getNextS(),namedEntity,organization);
		GraphGeneralization gPersonBrazilian = new GraphGeneralization(Increment.getNextS(),person,brazilianCitizen);
		GraphGeneralization gPersonItalian = new GraphGeneralization(Increment.getNextS(),person,italianCitizen);
		GraphGeneralization gPersonChild = new GraphGeneralization(Increment.getNextS(),person,child);
		GraphGeneralization gPersonAdult = new GraphGeneralization(Increment.getNextS(),person,adult);
		GraphGeneralization gAdultEmployee = new GraphGeneralization(Increment.getNextS(),adult,employee);
		GraphGeneralization gAdultPersonal = new GraphGeneralization(Increment.getNextS(),adult,personalCustomer);
		GraphGeneralization gOrganizationSchool = new GraphGeneralization(Increment.getNextS(),organization,primarySchool);
		GraphGeneralization gOrganizationHospital = new GraphGeneralization(Increment.getNextS(),organization,hospital);
		GraphGeneralization gOrganizationContractor = new GraphGeneralization(Increment.getNextS(),organization,contractor);
		GraphGeneralization gOrganizationCustomer = new GraphGeneralization(Increment.getNextS(),organization,corporateCustomer);
		GraphGeneralization gCustomerPersonal = new GraphGeneralization(Increment.getNextS(),customer,personalCustomer);
		GraphGeneralization gCustomerCorporate = new GraphGeneralization(Increment.getNextS(),customer,corporateCustomer);
		
		
		GraphGeneralizationSet gsNamedEntityType = new GraphGeneralizationSet(Increment.getNextS(), "NamedEntityType", true, false);
		GraphGeneralizationSet gsNationality = new GraphGeneralizationSet(Increment.getNextS(), "Nationality", false, false);
		GraphGeneralizationSet gsLifePhase = new GraphGeneralizationSet(Increment.getNextS(), "LifePhase", true, true);
		GraphGeneralizationSet gsOrganizationType = new GraphGeneralizationSet(Increment.getNextS(), "OrganizationType", true, false);
		GraphGeneralizationSet gsCustomerType = new GraphGeneralizationSet(Increment.getNextS(), "CustomerType", true, false);
		
		gsNamedEntityType.addGeneralization(gNamedEntityPerson);
		gsNamedEntityType.addGeneralization(gNamedEntityOrganization);
		gsNationality.addGeneralization(gPersonBrazilian);
		gsNationality.addGeneralization(gPersonItalian);
		gsLifePhase.addGeneralization(gPersonChild);
		gsLifePhase.addGeneralization(gPersonAdult);
		gsOrganizationType.addGeneralization(gOrganizationSchool);
		gsOrganizationType.addGeneralization(gOrganizationHospital);
		gsCustomerType.addGeneralization(gCustomerPersonal);
		gsCustomerType.addGeneralization(gCustomerCorporate);
		
		
		graph.addNode(brazilianCitizen);
		graph.addNode(italianCitizen);
		graph.addNode(child);
		graph.addNode(personalCustomer);
		graph.addNode(corporateCustomer);
		graph.addNode(employee);
		graph.addNode(adult);
		graph.addNode(primarySchool);
		graph.addNode(hospital);
		graph.addNode(contractor);
		graph.addNode(namedEntity);
		graph.addNode(customer);
		graph.addNode(person);
		graph.addNode(organization);
		graph.addNode(employment);
		graph.addNode(enrollment);
		graph.addNode(supplyContract);

		
		
		
//		graph.addNode(namedEntity);
//		graph.addNode(customer);
//		graph.addNode(person);
//		graph.addNode(organization);
//		graph.addNode(primarySchool);
//		graph.addNode(hospital);
//		graph.addNode(brazilianCitizen);
//		graph.addNode(italianCitizen);
//		graph.addNode(employee);
//		graph.addNode(contractor);
//		graph.addNode(personalCustomer);
//		graph.addNode(corporateCustomer);
//		graph.addNode(child);
//		graph.addNode(adult);
//		graph.addNode(employment);
//		graph.addNode(enrollment);
//		graph.addNode(supplyContract);
		
		graph.addAssociation(aCustomerContract);
		graph.addAssociation(aContractorContract);
		graph.addAssociation(aChildEnrollment);
		graph.addAssociation(aSchoolEnrollment);
		graph.addAssociation(aEmployeeEmployment);
		graph.addAssociation(aOrganizationEmployment);
		graph.addAssociation(gNamedEntityPerson);
		graph.addAssociation(gNamedEntityOrganization);
		graph.addAssociation(gPersonBrazilian);
		graph.addAssociation(gPersonItalian);
		graph.addAssociation(gPersonChild);
		graph.addAssociation(gPersonAdult);
		graph.addAssociation(gAdultEmployee);
		graph.addAssociation(gAdultPersonal);
		graph.addAssociation(gOrganizationSchool);
		graph.addAssociation(gOrganizationHospital);
		graph.addAssociation(gOrganizationContractor);
		graph.addAssociation(gOrganizationCustomer);
		graph.addAssociation(gCustomerPersonal);
		graph.addAssociation(gCustomerCorporate);
		
		graph.addGeneralizationSet(gsNamedEntityType);
		graph.addGeneralizationSet(gsNationality);
		graph.addGeneralizationSet(gsLifePhase);
		graph.addGeneralizationSet(gsOrganizationType);
		graph.addGeneralizationSet(gsCustomerType);
		return graph;
	}
}


