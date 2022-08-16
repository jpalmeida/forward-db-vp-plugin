package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.HierarchyModel;
import testModels.RunningExampleModel;
import testModels.RunningExampleSlim;

public class TestIndex {

	@Test
	public void testIndexOverlapping() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyOverlappingComplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String script = toDb.getIndexesScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE INDEX ix_associated_class_1 ON associated_class ( super_class_id, associated_class_id );");
		  
		    check.addCommand("CREATE INDEX ix_gs_test_1 ON gs_test ( super_class_id, gs_test_id );");
		    
		    check.addCommand("CREATE UNIQUE INDEX ix_gs_test_2 ON gs_test ( gs_test_enum, super_class_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [IndexOverlapping] has problems: " + result);
		    	fail("testIndexOverlapping");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testIndexOverlapping");
		}
	}
	
	@Test
	public void testRunningExampleIndexex() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( RunningExampleModel.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getIndexesScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE INDEX ix_organization_1 ON organization ( is_contractor, organization_id );");
		    check.addCommand("CREATE INDEX ix_organization_2 ON organization ( is_corporate_customer, organization_id );");
		    check.addCommand("CREATE INDEX ix_employment_1 ON employment ( person_id, employment_id );");
		    check.addCommand("CREATE INDEX ix_employment_2 ON employment ( organization_id, employment_id );");
		    check.addCommand("CREATE INDEX ix_enrollment_1 ON enrollment ( organization_id, enrollment_id );");
		    check.addCommand("CREATE INDEX ix_enrollment_2 ON enrollment ( person_id, enrollment_id );");
		    check.addCommand("CREATE INDEX ix_supply_contract_1 ON supply_contract ( organization_customer_id, supply_contract_id );");
		    check.addCommand("CREATE INDEX ix_supply_contract_2 ON supply_contract ( organization_contractor_id, supply_contract_id );");
		    check.addCommand("CREATE INDEX ix_supply_contract_3 ON supply_contract ( person_id, supply_contract_id );");
		    check.addCommand("CREATE INDEX ix_nationality_1 ON nationality ( person_id, nationality_id );");
		    check.addCommand("CREATE UNIQUE INDEX ix_nationality_2 ON nationality ( nationality_enum, person_id );");
		    
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
	public void testRunningExampleSlimIndex() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( RunningExampleSlim.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getIndexesScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE INDEX ix_organization_1 ON organization ( is_contractor, organization_id );");
		    check.addCommand("CREATE INDEX ix_organization_2 ON organization ( is_corporate_customer, organization_id );");
		    check.addCommand("CREATE INDEX ix_supply_contract_1 ON supply_contract ( organization_customer_id, supply_contract_id );");
		    check.addCommand("CREATE INDEX ix_supply_contract_2 ON supply_contract ( organization_contractor_id, supply_contract_id );");
		    check.addCommand("CREATE INDEX ix_supply_contract_3 ON supply_contract ( person_id, supply_contract_id);");
		   
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
}
