package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.DatatypeModel;

public class TestDatatype {

	@Test
	public void testDataType() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( DatatypeModel.getGraph());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE TABLE person (  \n" + 
		    		"        person_id              INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       coordinate_id          INTEGER       NULL \n" + 
		    		",       name                   DATE          NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE coordinate (  \n" + 
		    		"        coordinate_id          INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       latitude               int           NOT NULL \n" + 
		    		",       longitude              int           NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE person ADD FOREIGN KEY ( coordinate_id ) REFERENCES coordinate ( coordinate_id );");
		    
		    
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
