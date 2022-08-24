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
	public void testDataTypeAsAttribute() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( DatatypeModel.getDatatypeAsAttribute());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE TABLE person (  \n" + 
		    		"        person_id              INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       myDate                 DATE          NOT NULL \n" + 
		    		",       latitude               int           NOT NULL \n" + 
		    		",       longitude              int           NOT NULL \n" + 
		    		");");
		    
		    check.addCommand("CREATE TABLE multiple_positions (  \n" + 
		    		"        multiple_positions_id  INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       person_id              INTEGER       NOT NULL \n" + 
		    		",       latitude               int           NOT NULL \n" + 
		    		",       longitude              int           NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE multiple_positions ADD FOREIGN KEY ( person_id ) REFERENCES person ( person_id );");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [DataTypeAsAttribute] has problems: " + result);
		    	fail("DataTypeAsAttribute");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("DataTypeAsAttribute");
		}
	}
	
	@Test
	public void testDataTypeAsAssociation() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( DatatypeModel.getDatatypeAsAssociation());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE TABLE person ( \r\n" + 
		    		"        person_id              INTEGER       NOT NULL PRIMARY KEY\r\n" + 
		    		",       name                   STRING        NOT NULL\r\n" + 
		    		",       my_date                DATE          NOT NULL\r\n" + 
		    		",       latitude               int           NOT NULL\r\n" + 
		    		",       longitude              int           NOT NULL\r\n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE multiple_positions ( \r\n" + 
		    		"        multiple_positions_id  INTEGER       NOT NULL PRIMARY KEY\r\n" + 
		    		",       person_id              INTEGER       NOT NULL\r\n" + 
		    		",       latitude               int           NOT NULL\r\n" + 
		    		",       longitude              int           NOT NULL\r\n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE multiple_positions ADD FOREIGN KEY ( person_id ) REFERENCES person ( person_id );");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [DataTypeAsAssociation] has problems: " + result);
		    	fail("DataTypeAsAssociation");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("DataTypeAsAssociation");
		}
	}
}
