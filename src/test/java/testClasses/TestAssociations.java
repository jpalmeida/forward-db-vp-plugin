package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.AssociationsModel;

public class TestAssociations {

	@Test
	public void testFor1to1() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( AssociationsModel.getGraph1to1() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         person_id               INTEGER        NOT NULL PRIMARY KEY \n"+
		    		",        name                    VARCHAR(20)    NOT NULL \n" + 
		    		");");
		    check.addCommand("CREATE TABLE Node1To1 (  \n" + 
		    		"        Node1To1_id            INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       Person_id              INTEGER       NULL \n" + 
		    		",       test                   INTEGER       NOT NULL \n" + 
		    		");");
		    
		    check.addCommand("ALTER TABLE node1to1   ADD FOREIGN KEY ( person_id ) REFERENCES Person ( person_id );");
		    
		    check.addCommand("CREATE UNIQUE INDEX ix_Node1To1_1 ON Node1To1 ( Person_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [For1to1] has problems: " + result);
		    	fail("testFor1to1");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testFor1to1");
		}
	}
	
	@Test
	public void testFor1to01() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( AssociationsModel.getGraph1to01() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               INTEGER        NOT NULL PRIMARY KEY \n"+
		    		",        name                    VARCHAR(20)    NOT NULL \n" + 
		    		");");
		    check.addCommand("CREATE TABLE Node1To01 (  \n" + 
		    		"        Node1To01_id           INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       Person_id              INTEGER       NULL \n" + 
		    		",       test                   INTEGER       NOT NULL \n" + 
		    		");");
		    
		    check.addCommand("ALTER TABLE node1to01 ADD FOREIGN KEY ( Person_id ) REFERENCES Person ( Person_id );");
		    
		    check.addCommand("CREATE UNIQUE INDEX ix_Node1To01_1 ON Node1To01 ( Person_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [For1to01] has problems: " + result);
		    	fail("testFor1to01");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testFor1to01");
		}
	}
	
	@Test
	public void testFor01to01() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( AssociationsModel.getGraph01to01() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               INTEGER        NOT NULL PRIMARY KEY \n"+
		    		",        node01to01_id           INTEGER        NULL \n" + 
		    		",        name                    VARCHAR(20)    NOT NULL \n" + 
		    		");");
		    check.addCommand("CREATE TABLE node01to01 ( \n" + 
		    		"         node01to01_id           INTEGER        NOT NULL PRIMARY KEY \n" + 
		    		",        Person_id               INTEGER        NULL \n" + 
		    		",        test                    INTEGER        NOT NULL \n" + 
		    		");");
		    
		    check.addCommand("ALTER TABLE Person ADD FOREIGN KEY ( node01to01_id ) REFERENCES Node01To01 ( node01to01_id );");
		    check.addCommand("ALTER TABLE node01to01 ADD FOREIGN KEY ( Person_id ) REFERENCES Person ( Person_id );");
		    
		    check.addCommand("CREATE UNIQUE INDEX ix_Person_1 ON Person ( Node01To01_id );");
		    check.addCommand("CREATE UNIQUE INDEX ix_Node01To01_2 ON Node01To01 ( Person_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [For01to01] has problems: " + result);
		    	fail("testFor01to01");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testFor01to01");
		}
	}
	
	@Test
	public void testFor1tN() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( AssociationsModel.getGraph1toN() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               INTEGER        NOT NULL PRIMARY KEY \n"+
		    		",        name                    VARCHAR(20)    NOT NULL \n" + 
		    		");");
		    check.addCommand("CREATE TABLE node1toN ( \n" + 
		    		"         node1toN_id             INTEGER        NOT NULL PRIMARY KEY \n" + 
		    		",        Person_id               INTEGER        NOT NULL \n" + 
		    		",        test                    INTEGER        NOT NULL \n" + 
		    		");");
		    
		    check.addCommand("ALTER TABLE node1toN ADD FOREIGN KEY ( Person_id ) REFERENCES Person ( Person_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [For1tN] has problems: " + result);
		    	fail("testFor1tN");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testFor1tN");
		}
	}
	
	@Test
	public void testFor01tN() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( AssociationsModel.getGraph01toN() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               INTEGER        NOT NULL PRIMARY KEY \n"+
		    		",        name                    VARCHAR(20)    NOT NULL \n" + 
		    		");");
		    check.addCommand("CREATE TABLE node01toN ( \n" + 
		    		"         node01toN_id            INTEGER        NOT NULL PRIMARY KEY \n" + 
		    		",        Person_id               INTEGER        NULL \n" + 
		    		",        test                    INTEGER        NOT NULL \n" + 
		    		");");
		    
		    check.addCommand("ALTER TABLE node01toN ADD FOREIGN KEY ( Person_id ) REFERENCES Person ( Person_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [For01tN] has problems: " + result);
		    	fail("testFor01tN");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testFor01tN");
		}
	}
	
	@Test
	public void testForNtN() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( AssociationsModel.getGraphNtoN() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               INTEGER        NOT NULL PRIMARY KEY \n"+
		    		",        name                    VARCHAR(20)    NOT NULL \n" + 
		    		");");
		    
		    check.addCommand("CREATE TABLE nodeNtoN ( \n" + 
		    		"         nodeNtoN_id             INTEGER        NOT NULL PRIMARY KEY \n" + 
		    		",        test                    INTEGER        NOT NULL \n" + 
		    		");");
		    
		    check.addCommand("CREATE TABLE PersonNodeNToN ( \n" + 
		    		"         PersonNodeNToN_id       INTEGER        NOT NULL PRIMARY KEY \n" + 
		    		",        NodeNToN_id             INTEGER        NOT NULL \n" + 
		    		",        Person_id               INTEGER        NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE PersonNodeNToN ADD FOREIGN KEY ( NodeNToN_id ) REFERENCES NodeNToN ( NodeNToN_id );");
		    check.addCommand("ALTER TABLE PersonNodeNToN ADD FOREIGN KEY ( Person_id ) REFERENCES Person ( Person_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [ForNtN] has problems: " + result);
		    	fail("testForNtN");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testForNtN");
		}
	}

	
//	@Test
//	public void testDuplicatedFK() {
//		try {
//			OntoUmlToDb toDb = new OntoUmlToDb( AttributesModel.getDuplicateFK() );
//		    
//			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
//		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
//		    toDb.setStandardizeNames(true);
//		    toDb.runTransformation();
//		    
//		    String script = toDb.getRelationalSchemaScript();
//		    
//		    System.out.println(script);
//		    
//		    CheckTransformation check = new CheckTransformation( script );
//		    check.addCommand("CREATE TABLE super_class ( \n" + 
//		    		"        super_class_id         INTEGER       NOT NULL PRIMARY KEY \n" + 
//		    		",       name                   VARCHAR(20)   NOT NULL \n" + 
//		    		",       is_sub_class1          BIT           NOT NULL DEFAULT FALSE \n" + 
//		    		",       age1                   INTEGER       NULL \n" + 
//		    		",       is_sub_class2          BIT           NOT NULL DEFAULT FALSE \n" + 
//		    		",       age2                   INTEGER       NULL \n" + 
//		    		"); ");
//
//		    check.addCommand("CREATE TABLE associated_class (  \n" + 
//		    		"        associated_class_id    INTEGER       NOT NULL PRIMARY KEY \n" + 
//		    		",       super_class_sub_class2_id INTEGER       NOT NULL \n" + 
//		    		",       super_class_sub_class1_id INTEGER       NOT NULL \n" + 
//		    		",       address                VARCHAR(20)   NOT NULL \n" + 
//		    		"); ");
//		    
//		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( super_class_sub_class2_id ) REFERENCES super_class ( super_class_id );");
//		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( super_class_sub_class1_id ) REFERENCES super_class ( super_class_id );");
//		    
//		    
//		    String result = check.run();
//		    
//		    if(result != null) {
//		    	System.out.println("Test [DuplicatedFK] has problems: " + result);
//		    	fail("testDuplicatedFK");
//		    }
//		    
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("testDuplicatedFK");
//		}
//	}
	
	@Test
	public void testSelfRelationship() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( AssociationsModel.getGraphToSelfRelationship() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE person ( \n" + 
		    		"         person_id               INTEGER        NOT NULL  PRIMARY KEY \n"+
		    		",        person_man_id           INTEGER        NULL \n"+
		    		",        name                    VARCHAR(20)    NOT NULL \n" +
		    		",        is_man                  BOOLEAN        NOT NULL DEFAULT FALSE \n" + 
		    		",        is_woman                BOOLEAN        NOT NULL DEFAULT FALSE \n" + 
		    		");");
		    
		    check.addCommand("ALTER TABLE person ADD FOREIGN KEY ( person_man_id ) REFERENCES person ( person_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [SelfRelationship] has problems: " + result);
		    	fail("SelfRelationship");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("SelfRelationship");
		}
	}
	
	@Test
	public void testDuplicateAssociation() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( AssociationsModel.getGraphDuplicateAssociation() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE person ( \n" + 
		    		"         person_id               INTEGER        NOT NULL  PRIMARY KEY \n"+
		    		");");
		    
		    check.addCommand("CREATE TABLE car (  \n" + 
		    		"        car_id                 INTEGER       NOT NULL  PRIMARY KEY \n" + 
		    		",       person_has_owner_id    INTEGER       NOT NULL \n" + 
		    		",       person_has_rent_id     INTEGER       NULL \n" + 
		    		");  \n" + 
		    		"");
		    
		    check.addCommand("ALTER TABLE car ADD FOREIGN KEY ( person_has_owner_id ) REFERENCES person ( person_id );");
		    check.addCommand("ALTER TABLE car ADD FOREIGN KEY ( person_has_rent_id ) REFERENCES person ( person_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [SelfRelationship] has problems: " + result);
		    	fail("SelfRelationship");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("SelfRelationship");
		}
	}
	
	@Test
	public void testSelfAssociationWithGS() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( AssociationsModel.getGraphToSelfAssociationWithGS() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    String result = "";
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Module (  \n" + 
		    		"        Module_id                INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       ModuleModule_id          INTEGER       NULL \n" + 
		    		",       ComposedEnum             ENUM('Physical','Logical')  NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE MyClass (  \n" + 
		    		"        MyClass_id               INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       ModuleLogical_id         INTEGER       NOT NULL \n" + 
		    		",       ModulePhysical_id        INTEGER       NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE Module ADD FOREIGN KEY ( ModuleModule_id ) REFERENCES Module ( Module_id );");
		    
		    check.addCommand("ALTER TABLE MyClass ADD FOREIGN KEY ( ModuleLogical_id ) REFERENCES Module ( Module_id );");
		    
		    check.addCommand("ALTER TABLE MyClass ADD FOREIGN KEY ( ModulePhysical_id ) REFERENCES Module ( Module_id );");
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testSelfAssociationWithGS] has problems: " + result);
		    	fail("testSelfAssociationWithGS");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testSelfAssociationWithGS");
		}
	}
	
}
