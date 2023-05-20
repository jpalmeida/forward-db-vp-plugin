package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.HierarchyModel;

public class TestLifting {

	@Test
	public void testSimpleLifting() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSimpleHierarchyForLifting());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE super_class ( \n" + 
		    		"         super_class_id          INTEGER        NOT NULL PRIMARY KEY \n" +
		    		",        name                    VARCHAR(20)    NOT NULL \n" +
		    		",        is_sub_class            BIT            NOT NULL DEFAULT FALSE \n"+
		    		",        age                     INTEGER        NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE associated_class ( \n" + 
		    		"         associated_class_id     INTEGER        NOT NULL PRIMARY KEY \n" + 
		    		",        super_class_id          INTEGER        NOT NULL \n" + 
		    		",        address                 VARCHAR(20)    NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( super_class_id ) REFERENCES super_class ( super_class_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [SimpleLifting] has problems: " + result);
		    	fail("testSimpleLifting");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testSimpleLifting");
		}
	}
	
	@Test
	public void testLiftingDisjointComplete() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyDisjointComplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE super_class (  \n" + 
		    		"        super_class_id         INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       name                   VARCHAR(20)   NOT NULL \n" + 
		    		",       gs_test_enum           ENUM('SUBCLASS1','SUBCLASS2')  NOT NULL \n" + 
		    		",       age                    INTEGER       NULL \n" + 
		    		",       height                 INTEGER       NULL \n" + 
		    		");  ");
		    
		    check.addCommand("CREATE TABLE associated_class (  \n" + 
		    		"        associated_class_id    INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       super_class_id         INTEGER       NOT NULL \n" + 
		    		",       address                VARCHAR(20)   NOT NULL \n" + 
		    		");  ");
		    		    
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( super_class_id ) REFERENCES super_class ( super_class_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [LiftingDisjointComplete] has problems: " + result);
		    	fail("testLiftingDisjointComplete");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testLiftingDisjointComplete");
		}
	}
	
	@Test
	public void testLiftingDisjointIncomplete() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyDisjointIncomplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE super_class (  \n" + 
		    		"        super_class_id         INTEGER       NOT NULL  PRIMARY KEY \n" + 
		    		",       name                   VARCHAR(20)   NOT NULL \n" + 
		    		",       gs_test_enum           ENUM('SUBCLASS1','SUBCLASS2')  NULL \n" + 
		    		",       age                    INTEGER       NULL \n" + 
		    		",       height                 INTEGER       NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE associated_class (  \n" + 
		    		"        associated_class_id    INTEGER       NOT NULL  PRIMARY KEY \n" + 
		    		",       super_class_id         INTEGER       NOT NULL \n" + 
		    		",       address                VARCHAR(20)   NOT NULL \n" + 
		    		");  ");
		    
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( super_class_id ) REFERENCES super_class ( super_class_id );");
		   		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [LiftingDisjointIncomplete] has problems: " + result);
		    	fail("testLiftingDisjointIncomplete");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testLiftingDisjointIncomplete");
		}
	}
	
	@Test
	public void testLiftingOverlappingComplete() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyOverlappingComplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE super_class (  \n" + 
		    		"        super_class_id         INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       name                   VARCHAR(20)   NOT NULL \n" + 
		    		",       age                    INTEGER       NULL \n" + 
		    		",       height                 INTEGER       NULL \n" + 
		    		");");
		    
		    check.addCommand("CREATE TABLE associated_class (  \n" + 
		    		"        associated_class_id    INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       super_class_id         INTEGER       NOT NULL \n" + 
		    		",       address                VARCHAR(20)   NOT NULL \n" + 
		    		");");
		    
		    check.addCommand("CREATE TABLE gs_test (  \n" + 
		    		"        gs_test_id        INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       super_class_id         INTEGER       NOT NULL \n" + 
		    		",       gs_test_enum           ENUM('SUBCLASS1','SUBCLASS2')  NOT NULL \n" +
		    		"); ");
		    
		    
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( super_class_id ) REFERENCES super_class ( super_class_id );");
		  
		    check.addCommand("ALTER TABLE gs_test ADD FOREIGN KEY ( super_class_id ) REFERENCES super_class ( super_class_id );");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [LiftingOverlappingComplete] has problems: " + result);
		    	fail("testLiftingOverlappingComplete");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testLiftingOverlappingComplete");
		}
	}
	
	@Test
	public void testLiftingOverlappingInomplete() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyOverlappingInomplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE super_class (  \n" + 
		    		"        super_class_id         INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       name                   VARCHAR(20)   NOT NULL \n" + 
		    		",       age                    INTEGER       NULL \n" + 
		    		",       height                 INTEGER       NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE associated_class (  \n" + 
		    		"        associated_class_id    INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       super_class_id         INTEGER       NOT NULL \n" + 
		    		",       address                VARCHAR(20)   NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE gs_test (  \n" + 
		    		"        gs_test_id        INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       super_class_id         INTEGER       NOT NULL \n" + 
		    		",       gs_test_enum           ENUM('SUBCLASS1','SUBCLASS2')  NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( super_class_id ) REFERENCES super_class ( super_class_id );");
		    
		    check.addCommand("ALTER TABLE gs_test ADD FOREIGN KEY ( super_class_id ) REFERENCES super_class ( super_class_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [LiftingOverlappingInomplete] has problems: " + result);
		    	fail("testLiftingOverlappingInomplete");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testLiftingOverlappingInomplete");
		}
	}
	
	@Test
	public void testSelfAssociationInSubnode() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSelfAssociationInSubnode());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE super_class (  \n" + 
		    		"        super_class_id         INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       super_class_class2_go_to_class3_id INTEGER       NULL \n" + 
		    		",       super_class_class2_go_to_class1_id INTEGER       NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE type (  \n" + 
		    		"        type_id                INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       super_class_id         INTEGER       NOT NULL \n" + 
		    		",       typeenum               ENUM('SUBCLASS1','SUBCLASS2','SUBCLASS3')  NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE sub_class1_sub_class1 (  \n" + 
		    		"        sub_class1_sub_class1_id INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       super_class_sub_class1_sub_class132_id INTEGER       NOT NULL \n" + 
		    		",       super_class_sub_class1_sub_class133_id INTEGER       NOT NULL \n" + 
		    		");");
		    
		    check.addCommand("ALTER TABLE super_class ADD FOREIGN KEY ( super_class_class2_go_to_class3_id ) REFERENCES super_class ( super_class_id );");
		    
		    check.addCommand("ALTER TABLE super_class ADD FOREIGN KEY ( super_class_class2_go_to_class1_id ) REFERENCES super_class ( super_class_id );");
		    
		    check.addCommand("ALTER TABLE type ADD FOREIGN KEY ( super_class_id ) REFERENCES super_class ( super_class_id );");
		    
		    check.addCommand("ALTER TABLE sub_class1_sub_class1 ADD FOREIGN KEY ( super_class_sub_class1_sub_class132_id ) REFERENCES super_class ( super_class_id );");
		    
		    check.addCommand("ALTER TABLE sub_class1_sub_class1 ADD FOREIGN KEY ( super_class_sub_class1_sub_class133_id ) REFERENCES super_class ( super_class_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [SelfAssociationInSubnode] has problems: " + result);
		    	fail("SelfAssociationInSubnode");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("SelfAssociationInSubnode");
		}
	}
	
	@Test
	public void testGSwithoutName() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getGraphGSwithoutName());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.POSTGRE);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE TYPE super_class_type_enum_enum_type AS ENUM ('SUBCLASS1', 'SUBCLASS2'); ");
		    
		    check.addCommand("CREATE TABLE super_class ( \n" + 
		    		"        super_class_id           SERIAL        NOT NULL PRIMARY KEY \n" + 
		    		",       super_class_type_enum    super_class_type_enum_enum_type  NULL \n" + 
		    		");");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [SelfAssociationInSubnode] has problems: " + result);
		    	fail("SelfAssociationInSubnode");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("SelfAssociationInSubnode");
		}
	}
	
	
	
}