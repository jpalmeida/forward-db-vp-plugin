package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.WeakEntitiesModel;

public class TestWeakEntity {
	
	@Test
	public void testSimpleWeakEntityModel() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( WeakEntitiesModel.getSimpleWeakEntityModel());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE TABLE person ( \r\n" + 
		    		"        person_id              INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       name                   VARCHAR(20)   NOT NULL \n" + 
		    		"); ");		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [SimpleWeakEntityModel] has problems: " + result);
		    	fail("SimpleWeakEntityModel");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("SimpleWeakEntityModel");
		}
	}
}
