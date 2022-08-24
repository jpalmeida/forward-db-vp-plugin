package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.AttributesModel;

public class TestSpecialSituations {

	@Test
	public void testSpecialCharacteres() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( AttributesModel.getSpecialCharacteresGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE personx_human (  \n" + 
		    		"        personx_human_id       INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       x123456x               VARCHAR(20)   NOT NULL \n" + 
		    		"); ");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [DuplicateAssociation] has problems: " + result);
		    	fail("DuplicateAssociation");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("DuplicateAssociation");
		}
	}	
}
