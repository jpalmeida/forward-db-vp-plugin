package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.HierarchyModel;

public class TestFlattening {

	@Test
	public void testSimpleFlattening() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSimpleHierarchyForFlattening());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE sub_class (  \n" + 
		    		"         sub_class_id            INTEGER        NOT NULL PRIMARY KEY \n" +
		    		",        name                    VARCHAR(20)    NOT NULL \n" +
		    		",        age                     INTEGER        NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE associated_class ( \n" + 
		    		"         associated_class_id     INTEGER        NOT NULL PRIMARY KEY \n" + 
		    		",        sub_class_id            INTEGER        NULL \n" + 
		    		",        address                 VARCHAR(20)    NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( sub_class_id ) REFERENCES sub_class ( sub_class_id );");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [SimpleFlattening] has problems: " + result);
		    	fail("testSimpleFlattening");
 		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testSimpleFlattening");
		}
	}
	
	@Test
	public void testCommonFlattening() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getCommonHierarchyForFlattening());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE sub_class1 (  \n" + 
		    		"         sub_class1_id            INTEGER       NOT NULL PRIMARY KEY \n" +
		    		",        name                    VARCHAR(20)    NOT NULL \n" +
		    		",        age                     INTEGER        NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE sub_class2 (  \n" + 
		    		"         sub_class2_id           INTEGER        NOT NULL PRIMARY KEY \n" +
		    		",        name                    VARCHAR(20)    NOT NULL \n" +
		    		",        height                  INTEGER        NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE associated_class ( \n" + 
		    		"         associated_class_id     INTEGER        NOT NULL PRIMARY KEY \n" + 
		    		",        sub_class2_id           INTEGER        NULL \n" +
		    		",        sub_class1_id           INTEGER        NULL \n" +
		    		",        address                 VARCHAR(20)    NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( sub_class2_id ) REFERENCES sub_class2 ( sub_class2_id );");
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( sub_class1_id ) REFERENCES sub_class1 ( sub_class1_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [CommonFlattening] has problems: " + result);
		    	fail("testCommonFlattening");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testCommonFlattening");
		}
	}
	
}
