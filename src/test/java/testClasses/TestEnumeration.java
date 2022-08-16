package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.EnumerationsModel;

public class TestEnumeration {

	@Test
	public void testEnumerationsAsAssociation() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( EnumerationsModel.getEnumerationsAsAssociationGraph());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE TABLE person ( \r\n" + 
		    		"        person_id              INTEGER       NOT NULL PRIMARY KEY\r\n" + 
		    		",       nationality            ENUM('BRAZILIAN','ITALIAN')  NOT NULL\r\n" + 
		    		"); ");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [EnumerationsAsAssociation] has problems: " + result);
		    	fail("EnumerationsAsAssociation");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("EnumerationsAsAssociation");
		}
	}
}
