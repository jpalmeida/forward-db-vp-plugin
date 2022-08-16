package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.RunningExampleModel;

public class TestObda {
		
	@Test
	public void testRunningExampleObda() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( RunningExampleModel.getGraph() );
			
			toDb.setProjectName("RunExample");
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.setBaseIri("https://example.com");
		    toDb.runTransformation();
		    String script = toDb.getObdaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("[PrefixDeclaration] \n" + 
		    		":       https://example.com# \n" + 
		    		"gufo:   http://purl.org/nemo/gufo# \n" + 
		    		"rdf:    http://www.w3.org/1999/02/22-rdf-syntax-ns# \n" + 
		    		"rdfs:   http://www.w3.org/2000/01/rdf-schema# \n" + 
		    		"owl:    http://www.w3.org/2002/07/owl# \n" + 
		    		"xsd:    http://www.w3.org/2001/XMLSchema#");
		    
		    
		    check.addCommand("[MappingDeclaration] @collection [[");
		    
		    // ADULT
		    check.addCommand("mappingId    RunExample-Adult \n" + 
				    		 "target       :RunExample/person/{person_id} a :Adult . \n" + 
				    		 "source       SELECT person.person_id  \n" + 
				    		 "             FROM person  \n" + 
				    		 "             WHERE life_phase_enum = 'ADULT' ");
		    
		    // BRAZILIAN CITIZEN
		    check.addCommand("mappingId    RunExample-BrazilianCitizen \n" + 
				    		 "target       :RunExample/person/{person_id} a :BrazilianCitizen ; :rg {rg}^^xsd:string . \n" + 
				    		 "source       SELECT person.person_id, person.rg  \n" + 
				    		 "             FROM person  \n" + 
				    		 "             LEFT JOIN nationality \n" + 
				    		 "                    ON person.person_id = nationality.person_id \n" + 
				    		 "                    AND nationality.nationality_enum = 'BRAZILIANCITIZEN'   ");
		    
		    // CHILD
		    check.addCommand("mappingId    RunExample-Child \n" + 
				    		 "target       :RunExample/person/{person_id} a :Child . \n" + 
				    		 "source       SELECT person.person_id  \n" + 
				    		 "             FROM person  \n" + 
				    		 "             WHERE life_phase_enum = 'CHILD' ");
		    
		    // CONTRACTOR
		    check.addCommand("mappingId    RunExample-Contractor \n" + 
				    		 "target       :RunExample/organization/{organization_id} a :Contractor . \n" + 
				    		 "source       SELECT organization.organization_id  \n" + 
				    		 "             FROM organization  \n" + 
				    		 "             WHERE is_contractor = TRUE ");
		    
		    // CORPORATE CUSTOMER
		    check.addCommand("mappingId    RunExample-CorporateCustomer \n" + 
				    		 "target       :RunExample/organization/{organization_id} a :CorporateCustomer ; :creditLimit {credit_limit}^^xsd:int . \n" + 
				    		 "source       SELECT organization.organization_id, organization.credit_limit  \n" + 
				    		 "             FROM organization  \n" + 
				    		 "             WHERE is_corporate_customer = TRUE  ");
		    
		    // CUSTOMER
		    check.addCommand("mappingId    RunExample-Customer \n" + 
				    		 "target       :RunExample/person/{person_id} a :Customer ; :creditRating {credit_rating}^^xsd:decimal . \n" + 
				    		 "source       SELECT person.person_id, person.credit_rating  \n" + 
				    		 "             FROM person  \n" + 
				    		 "             WHERE is_personal_customer = TRUE  \n" + 
				    		 "             AND   life_phase_enum = 'ADULT'  ");
		    
		    // CUSTOMER
		    check.addCommand("mappingId    RunExample-Customer106 \n" + 
				    		 "target       :RunExample/organization/{organization_id} a :Customer ; :creditRating {credit_rating}^^xsd:decimal . \n" + 
				    		 "source       SELECT organization.organization_id, organization.credit_rating  \n" + 
				    		 "             FROM organization  \n" + 
				    		 "             WHERE is_corporate_customer = TRUE  ");
		    
		    // EMPLOYEE
		    check.addCommand("mappingId    RunExample-Employee \n" + 
				    		 "target       :RunExample/person/{person_id} a :Employee . \n" + 
				    		 "source       SELECT person.person_id  \n" + 
				    		 "             FROM person  \n" + 
				    		 "             WHERE is_employee = TRUE  \n" + 
				    		 "             AND   life_phase_enum = 'ADULT' ");
		    
		    // EMPLOYMENT
		    check.addCommand("mappingId    RunExample-Employment \n" + 
				    		 "target       :RunExample/employment/{employment_id} a :Employment ; :salary {salary}^^xsd:decimal ; :hasEmployee :RunExample/person/{person_id}  ; :hasOrganization :RunExample/organization/{organization_id}  . \n" + 
				    		 "source       SELECT employment.employment_id, employment.salary, employment.person_id, employment.organization_id  \n" + 
				    		 "             FROM employment  ");
		    
		    // ENROLLMENT
		    check.addCommand("mappingId    RunExample-Enrollment \n" + 
				    		 "target       :RunExample/enrollment/{enrollment_id} a :Enrollment ; :grade {grade}^^xsd:int ; :hasSchool :RunExample/organization/{organization_id}  ; :hasChild :RunExample/person/{person_id}  . \n" + 
				    		 "source       SELECT enrollment.enrollment_id, enrollment.grade, enrollment.organization_id, enrollment.person_id  \n" + 
				    		 "             FROM enrollment  \n");
		    
		    // HOSPITAL
		    check.addCommand("mappingId    RunExample-Hospital \n" + 
				    		 "target       :RunExample/organization/{organization_id} a :Hospital ; :capacity {capacity}^^xsd:int . \n" + 
				    		 "source       SELECT organization.organization_id, organization.capacity  \n" + 
				    		 "             FROM organization  \n" + 
				    		 "             WHERE organization_type_enum = 'HOSPITAL'  ");
		    
		    // ITALIAN CITIZEN
		    check.addCommand("mappingId    RunExample-ItalianCitizen \n" + 
				    		 "target       :RunExample/person/{person_id} a :ItalianCitizen ; :ci {ci}^^xsd:string . \n" + 
				    		 "source       SELECT person.person_id, person.ci  \n" + 
				    		 "            FROM person  \n" + 
				    		 "            LEFT JOIN nationality \n" + 
				    		 "                   ON person.person_id = nationality.person_id \n" + 
				    		 "                   AND nationality.nationality_enum = 'ITALIANCITIZEN'  ");
		    
		    // NAMED ENTITY
		    check.addCommand("mappingId    RunExample-NamedEntity \n" + 
				    		 "target       :RunExample/person/{person_id} a :NamedEntity ; :name {name}^^xsd:string . \n" + 
				    		 "source       SELECT person.person_id, person.name  \n" + 
				    		 "             FROM person  ");
		    
		    // NAMED ENTITY
		    check.addCommand("mappingId    RunExample-NamedEntity107 \n" + 
				    		 "target       :RunExample/organization/{organization_id} a :NamedEntity ; :name {name}^^xsd:string . \n" + 
				    		 "source       SELECT organization.organization_id, organization.name  \n" + 
				    		 "             FROM organization ");
		    
		    // ORGANIZATION
		    check.addCommand("mappingId    RunExample-Organization \n" + 
				    		 "target       :RunExample/organization/{organization_id} a :Organization ; :address {address}^^xsd:string . \n" + 
				    		 "source       SELECT organization.organization_id, organization.address  \n" + 
				    		 "             FROM organization  ");
		    
		    // PERSON
		    check.addCommand("mappingId    RunExample-Person \n" + 
				    		 "target       :RunExample/person/{person_id} a :Person ; :birthDate {birth_date}^^xsd:dateTime . \n" + 
				    		 "source       SELECT person.person_id, person.birth_date  \n" + 
				    		 "             FROM person   ");
		    
		    // PERSONAL CUSTOMER
		    check.addCommand("mappingId    RunExample-PersonalCustomer \n" + 
				    		 "target       :RunExample/person/{person_id} a :PersonalCustomer ; :creditCard {credit_card}^^xsd:string . \n" + 
				    		 "source       SELECT person.person_id, person.credit_card  \n" + 
				    		 "             FROM person  \n" + 
				    		 "             WHERE is_personal_customer = TRUE  \n" + 
				    		 "             AND   life_phase_enum = 'ADULT'  ");

		    // PRIMARY SCHOOL
		    check.addCommand("mappingId    RunExample-PrimarySchool \n" + 
				    		 "target       :RunExample/organization/{organization_id} a :PrimarySchool ; :playgroundSize {playground_size}^^xsd:int . \n" + 
				    		 "source       SELECT organization.organization_id, organization.playground_size  \n" + 
				    		 "             FROM organization  \n" + 
				    		 "             WHERE organization_type_enum = 'PRIMARYSCHOOL' ");
		    
		    // SUPPLY CONTRACT
		    check.addCommand("mappingId    RunExample-SupplyContract \n" + 
				    		 "target       :RunExample/supply_contract/{supply_contract_id} a :SupplyContract ; :contractValue {contract_value}^^xsd:decimal ; :hasCustomer :RunExample/organization/{organization_customer_id}  ; :hasContractor :RunExample/organization/{organization_contractor_id}  ; :hasCustomer :RunExample/person/{person_id}  . \n" + 
				    		 "source       SELECT supply_contract.supply_contract_id, supply_contract.contract_value, supply_contract.organization_customer_id, supply_contract.organization_contractor_id, supply_contract.person_id  \n" + 
				    		 "             FROM supply_contract ");
		    
		    
		    check.addCommand("]]");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [RunningExampleObda] has problems: " + result);
		    	fail("testRunningExampleObda");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testRunningExampleObda");
		}
	}

}
