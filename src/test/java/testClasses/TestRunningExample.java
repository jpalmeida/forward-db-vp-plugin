package testClasses;


import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.RunningExampleModel;

public class TestRunningExample {
	
	@Test
	public void testRunningExample() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( RunningExampleModel.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE person ( \n" + 
		    		"         person_id               INTEGER        NOT NULL  PRIMARY KEY\n" + 
		    		",        name                    VARCHAR(20)    NOT NULL\n" + 
		    		",        birth_date              DATE           NOT NULL\n" + 
		    		",        rg                      VARCHAR(20)   NULL\r\n" + 
		    		",        ci                      VARCHAR(20)   NULL\r\n" + 
		    		",        life_phase_enum         ENUM('CHILD','ADULT')  NOT NULL \n" + 
		    		",        is_employee             BOOLEAN        NOT NULL DEFAULT FALSE\n" + 
		    		",        is_personal_customer    BOOLEAN        NOT NULL DEFAULT FALSE\n" + 
		    		",        credit_rating           DOUBLE         NULL\n" + 
		    		",        credit_card             VARCHAR(20)    NULL\n" + 
		    		"); ");
		    check.addCommand("CREATE TABLE organization (  \n" + 
		    		"         organization_id         INTEGER        NOT NULL  PRIMARY KEY\n" + 
		    		",        name                    VARCHAR(20)    NOT NULL\n" + 
		    		",        address                 VARCHAR(20)    NOT NULL\n" + 
		    		",        organization_type_enum  ENUM('PRIMARYSCHOOL','HOSPITAL')  NULL\n" + 
		    		",        playground_size         INTEGER        NULL\n" + 
		    		",        capacity                INTEGER        NULL \n" + 
		    		",        is_contractor           BOOLEAN        NOT NULL DEFAULT FALSE\n" + 
		    		",        is_corporate_customer   BOOLEAN        NOT NULL DEFAULT FALSE\n" + 
		    		",        credit_rating           DOUBLE         NULL\n" + 
		    		",        credit_limit            INTEGER        NULL\n" + 
		    		"); ");
		    check.addCommand("CREATE TABLE employment ( \n" + 
		    		"         employment_id           INTEGER        NOT NULL  PRIMARY KEY\n" + 
		    		",        person_id               INTEGER        NOT NULL\n" + 
		    		",        organization_id         INTEGER        NOT NULL\n" + 
		    		",        salary                  DOUBLE         NOT NULL\n" + 
		    		"); ");
		    check.addCommand("CREATE TABLE enrollment ( \n" + 
		    		"         enrollment_id           INTEGER        NOT NULL   PRIMARY KEY\n" + 
		    		",        organization_id         INTEGER        NOT NULL\n" + 
		    		",        person_id               INTEGER        NOT NULL\n" + 
		    		",        grade                   INTEGER        NOT NULL\n" + 
		    		"); ");
		    check.addCommand("CREATE TABLE supply_contract ( \n" + 
		    		"         supply_contract_id      INTEGER        NOT NULL  PRIMARY KEY\n" + 
		    		",        organization_customer_id    INTEGER        NULL\n" + 
		    		",        organization_contractor_id  INTEGER        NOT NULL\n" + 
		    		",        person_id               INTEGER        NULL\n" + 
		    		",        contract_value          DOUBLE         NOT NULL\n" + 
		    		"); ");
		    check.addCommand("CREATE TABLE nationality ( \r\n" + 
		    		"        nationality_id    INTEGER       NOT NULL  PRIMARY KEY\r\n" + 
		    		",       person_id              INTEGER       NOT NULL\r\n" + 
		    		",       nationality_enum       ENUM('BRAZILIANCITIZEN','ITALIANCITIZEN')  NOT NULL\r\n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE employment ADD FOREIGN KEY ( organization_id ) REFERENCES organization ( organization_id );");
		    check.addCommand("ALTER TABLE employment ADD FOREIGN KEY ( person_id ) REFERENCES person ( person_id );");
		    check.addCommand("ALTER TABLE enrollment ADD FOREIGN KEY ( organization_id ) REFERENCES organization ( organization_id );");
		    check.addCommand("ALTER TABLE enrollment ADD FOREIGN KEY ( person_id ) REFERENCES person ( person_id );");
		    check.addCommand("ALTER TABLE supply_contract ADD FOREIGN KEY ( organization_customer_id ) REFERENCES organization ( organization_id );");
		    check.addCommand("ALTER TABLE supply_contract ADD FOREIGN KEY ( organization_contractor_id ) REFERENCES organization ( organization_id );");
		    check.addCommand("ALTER TABLE supply_contract ADD FOREIGN KEY ( person_id ) REFERENCES person ( person_id );");
		    check.addCommand("ALTER TABLE nationality ADD FOREIGN KEY ( person_id ) REFERENCES person ( person_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [RunningExample] has problems: " + result);
		    	fail("testRunningExample");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testRunningExample");
		}
	}
	
	@Test
	public void testRunningExampleSchemaOFTpLC() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( RunningExampleModel.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_LEAF_CLASS);
		    toDb.setDbms(DbmsSupported.POSTGRE);
		    toDb.setStandardizeNames(true);
		    
		    toDb.runTransformation();
		    
		    String script = toDb.getRelationalSchemaScript();
		    
		    System.out.println(script);
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("");
		    
		    //String result = check.run();
		    /*
		     1 - the key of subclasse need be the same
		     2 - add one attribute to identify the existence of only the superclass instance
		    
		    if(result != null) {
		    	System.out.println("Test [RunningExample] has problems: \n" + result);
		    	fail("testRunningExample");
		    }
		    */
		} catch (Exception e) {
			e.printStackTrace();
			fail("testRunningExample");
		}
	}
}
