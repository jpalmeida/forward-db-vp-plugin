package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.AssociationsModel;
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
		    	System.out.println("Test [SpecialCharacteres] has problems: " + result);
		    	fail("testSpecialCharacteres");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testSpecialCharacteres");
		}
	}
	
	
	@Test
	public void testResolveNtoNFirst() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( AssociationsModel.getGraphNtoNWithAnAbstractClass() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.POSTGRE);
		    toDb.setTransformaNtoNFirst(true);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE sub_class1 ( \r\n" + 
		    		"        sub_class1_id            SERIAL        NOT NULL PRIMARY KEY\r\n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE sub_class2 ( \r\n" + 
		    		"        sub_class2_id            SERIAL        NOT NULL PRIMARY KEY\r\n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE associated_class ( \r\n" + 
		    		"        associated_class_id      SERIAL        NOT NULL PRIMARY KEY\r\n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE super_class_associated_class ( \r\n" + 
		    		"        super_class_associated_class_id  SERIAL        NOT NULL PRIMARY KEY\r\n" + 
		    		",       sub_class2_id            INTEGER       NULL\r\n" + 
		    		",       sub_class1_id            INTEGER       NULL\r\n" + 
		    		",       associated_class_id      INTEGER       NOT NULL\r\n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE super_class_associated_class ADD FOREIGN KEY ( sub_class2_id ) REFERENCES sub_class2 ( sub_class2_id );");
		    
		    check.addCommand("ALTER TABLE super_class_associated_class ADD FOREIGN KEY ( sub_class1_id ) REFERENCES sub_class1 ( sub_class1_id );");
		    
		    check.addCommand("ALTER TABLE super_class_associated_class ADD FOREIGN KEY ( associated_class_id ) REFERENCES associated_class ( associated_class_id );");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [ResolveNtoNFirst] has problems: " + result);
		    	fail("testresolveNtoNFirst");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testresolveNtoNFirst");
		}
	}
	
}
