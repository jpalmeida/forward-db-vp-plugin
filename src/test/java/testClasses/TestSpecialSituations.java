package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.AttributesModel;
import testModels.HierarchyModel;

public class TestSpecialSituations {

	@Test
	public void testSpecialCharacteres() {
		try {
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
	
	@Test
	public void testMultipleInheritance() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( HierarchyModel.getMultipleInheritanceWithoutStereotypes() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE sub_class1 (  \n" + 
		    		"        sub_class1_id          INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       relator_id             INTEGER       NULL \n" + 
		    		",       age                    INTEGER       NOT NULL \n" + 
		    		",       is_bottom_class        BOOLEAN       NOT NULL DEFAULT FALSE \n" + 
		    		",       address                VARCHAR(20)   NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE sub_class2 (  \n" + 
		    		"        sub_class2_id          INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       relator_id             INTEGER       NULL \n" + 
		    		",       height                 INTEGER       NOT NULL \n" + 
		    		",       is_bottom_class        BOOLEAN       NOT NULL DEFAULT FALSE \n" + 
		    		",       address                VARCHAR(20)   NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE relator (  \n" + 
		    		"        relator_id             INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		");  \n" + 
		    		"");
		    
		    check.addCommand("ALTER TABLE sub_class1 ADD FOREIGN KEY ( relator_id ) REFERENCES relator ( relator_id );");
		    
		    check.addCommand("ALTER TABLE sub_class2 ADD FOREIGN KEY ( relator_id ) REFERENCES relator ( relator_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [MultipleInheritance] has problems: " + result);
		    	fail("MultipleInheritance");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("MultipleInheritance");
		}
	}
	
	@Test
	public void testDimondHierarchyWithoutStereotypes() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( HierarchyModel.getDimondHierarchyWithoutStereotypes() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE TABLE super_class (  \n" + 
		    		"        super_class_id         INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       relator_id             INTEGER       NULL \n" + 
		    		",       name                   VARCHAR(20)   NOT NULL \n" + 
		    		",       is_sub_class1          BOOLEAN       NOT NULL DEFAULT FALSE \n" + 
		    		",       age                    INTEGER       NULL \n" + 
		    		",       is_bottom_class        BOOLEAN       NOT NULL DEFAULT FALSE \n" + 
		    		",       address                VARCHAR(20)   NULL \n" + 
		    		",       is_sub_class2          BOOLEAN       NOT NULL DEFAULT FALSE \n" + 
		    		",       height                 INTEGER       NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE relator (  \n" + 
		    		"        relator_id             INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		");  \n" + 
		    		"");
		    
		    check.addCommand("ALTER TABLE super_class ADD FOREIGN KEY ( relator_id ) REFERENCES relator ( relator_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [DimondHierarchyWithoutStereotypes] has problems: " + result);
		    	fail("DimondHierarchyWithoutStereotypes");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("DimondHierarchyWithoutStereotypes");
		}
	}
	
}
