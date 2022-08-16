package testClasses;
import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;


import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.AttributesModel;
import testModels.EnumerationsModel;

public class TestSgbdTypes {
	
	@Test
	public void testMultivaluedAttribute() {
		try {
			
			OntoUmlToDb toDb = new OntoUmlToDb( AttributesModel.getMultivaluedAttributesGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation checkDb = new CheckTransformation( script );
		    checkDb.addCommand("CREATE TABLE Person (  \n" + 
		    		"        Person_id              INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		");");
		    
		    checkDb.addCommand("CREATE TABLE x11 (  \n" + 
		    		"        x11_id                 INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       Person_id              INTEGER       NOT NULL \n" + 
		    		",       x11                    VARCHAR(20)   NOT NULL \n" + 
		    		"); ");
		    
		    checkDb.addCommand("CREATE TABLE x12 (  \n" + 
		    		"        x12_id                 INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       Person_id              INTEGER       NOT NULL \n" + 
		    		",       x12                    VARCHAR(20)   NOT NULL \n" + 
		    		"); ");
		    
		    checkDb.addCommand("ALTER TABLE x11 ADD FOREIGN KEY ( Person_id ) REFERENCES Person ( Person_id );");
		    
		    checkDb.addCommand("ALTER TABLE x12 ADD FOREIGN KEY ( Person_id ) REFERENCES Person ( Person_id );");
		    		    		    
		    String result = checkDb.run();
		    
		    if(result != null) {
		    	System.out.println("Test [ForGenericSchema] has problems: " + result);
		    	fail("testForGenericSchema");
		    }		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testForGenericSchema");
		}
	}
	
	@Test
	public void testMultivaluedEnumAttribute() {
		try {
			
			OntoUmlToDb toDb = new OntoUmlToDb( AttributesModel.getMultivaluedEnumAttributeGraph());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation checkDb = new CheckTransformation( script );
		    checkDb.addCommand("CREATE TABLE Person (  \n" + 
		    		"        Person_id              INTEGER       NOT NULL  PRIMARY KEY \n" + 
		    		"); ");
		    
		    checkDb.addCommand("CREATE TABLE x1 (  \n" + 
		    		"        x1_id                  INTEGER       NOT NULL  PRIMARY KEY \n" + 
		    		",       Person_id              INTEGER       NOT NULL \n" + 
		    		",       x1                     ENUM('Test1','Test2')  NOT NULL \n" + 
		    		"); ");
		    
		    checkDb.addCommand("CREATE TABLE x2 (  \n" + 
		    		"        x2_id                  INTEGER       NOT NULL  PRIMARY KEY \n" + 
		    		",       Person_id              INTEGER       NOT NULL \n" + 
		    		",       x2                     ENUM('Test3','Test4')  NOT NULL \n" + 
		    		");" );
		    
		    checkDb.addCommand("ALTER TABLE x1 ADD FOREIGN KEY ( Person_id ) REFERENCES Person ( Person_id );");
		    
		    checkDb.addCommand("ALTER TABLE x2 ADD FOREIGN KEY ( Person_id ) REFERENCES Person ( Person_id );");
		    		    		    
		    String result = checkDb.run();
		    
		    if(result != null) {
		    	System.out.println("Test [ForGenericSchema] has problems: " + result);
		    	fail("testForGenericSchema");
		    }		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testForGenericSchema");
		}
	}
	
	@Test
	public void testForGenericSchema() {
		try {
			
			OntoUmlToDb toDb = new OntoUmlToDb( AttributesModel.getTypesAttributesGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation checkDb = new CheckTransformation( script );
		    checkDb.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               INTEGER        NOT NULL PRIMARY KEY \n"+
		    		",        x1                      VARCHAR(20)    NOT NULL \n" + 
		    		",        x2                      INTEGER        NOT NULL \n" + 
		    		",        x3                      DATE           NULL \n" + 
		    		",        x4                      BIT            NULL \n" + 
		    		",        x5                      BIT(8)         NULL \n" + 
		    		",        x6                      CHAR(3)        NULL \n" + 
		    		",        x7                      DOUBLE         NULL \n" + 
		    		",        x8                      FLOAT          NULL \n" + 
		    		",        x9                      BIGINT         NULL \n" + 
		    		",        x10                     SMALLINT       NULL \n" +
		    		");");
		    		    		    
		    String result = checkDb.run();
		    
		    if(result != null) {
		    	System.out.println("Test [ForGenericSchema] has problems: " + result);
		    	fail("testForGenericSchema");
		    }		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testForGenericSchema");
		}
	}
	
	@Test
	public void testForMySql() {
		try {
			
			OntoUmlToDb toDb = new OntoUmlToDb( AttributesModel.getTypesAttributesGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               INTEGER        NOT NULL  PRIMARY KEY \n"+
		    		",        x1                      VARCHAR(20)    NOT NULL \n" + 
		    		",        x2                      INTEGER        NOT NULL \n" + 
		    		",        x3                      DATE           NULL \n" + 
		    		",        x4                      BOOLEAN        NULL \n" + 
		    		",        x5                      BINARY(8)      NULL \n" + 
		    		",        x6                      CHAR(3)        NULL \n" + 
		    		",        x7                      DOUBLE         NULL \n" + 
		    		",        x8                      FLOAT          NULL \n" + 
		    		",        x9                      BIGINT         NULL \n" + 
		    		",        x10                     SMALLINT       NULL \n" +
		    		");");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [ForMySql] has problems: " + result);
		    	fail("testForMySql");
		    }
		} catch (Exception e) {
			e.printStackTrace();
			fail("testForMySql");
		}
	}
	

	
	@Test
	public void testEnumnForMySql() {
		try {			
			OntoUmlToDb toDb = new OntoUmlToDb( EnumerationsModel.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setEnumFieldToLookupTable(false);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               INTEGER        NOT NULL PRIMARY KEY\n" + 
		    		",        name                    VARCHAR(20)    NOT NULL\n" + 
		    		",        phase                   ENUM('ADULT','CHILD')  NOT NULL\n"+
		    		");");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [EnumnForMySql] has problems: " + result + "\n");
		    	fail("testEnumnForMySql");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testEnumnForMySql");
		}
	}
	
	@Test
	public void testForH2() {
		try {
			
			OntoUmlToDb toDb = new OntoUmlToDb( AttributesModel.getTypesAttributesGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.H2);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               INTEGER        NOT NULL IDENTITY PRIMARY KEY \n"+
		    		",        x1                      VARCHAR(20)    NOT NULL \n" + 
		    		",        x2                      INTEGER        NOT NULL \n" + 
		    		",        x3                      DATE           NULL \n" + 
		    		",        x4                      BOOLEAN        NULL \n" + 
		    		",        x5                      BINARY(8)      NULL \n" + 
		    		",        x6                      CHAR(3)        NULL \n" + 
		    		",        x7                      DOUBLE         NULL \n" + 
		    		",        x8                      FLOAT          NULL \n" + 
		    		",        x9                      BIGINT         NULL \n" + 
		    		",        x10                     SMALLINT       NULL \n" +
		    		");");
		    		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [ForH2] has problems: " + result);
		    	fail("testForH2");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testForH2");
		}
	}
	
	@Test
	public void testForOracle() {
		try {
			
			OntoUmlToDb toDb = new OntoUmlToDb( AttributesModel.getTypesAttributesGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.ORACLE);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               NUMBER(10,0)   NOT NULL GENERATED ALWAYS AS IDENTITY \n" + 
		    		",        x1                      VARCHAR2(20)   NOT NULL \n" + 
		    		",        x2                      NUMBER(10,0)   NOT NULL \n" + 
		    		",        x3                      DATE           NULL \n" + 
		    		",        x4                      CHAR(1)        NULL \n" + 
		    		",        x5                      RAW(1)         NULL \n" + 
		    		",        x6                      CHAR(3)        NULL \n" + 
		    		",        x7                      NUMBER(20,4)   NULL \n" + 
		    		",        x8                      NUMBER(10,2)   NULL \n" + 
		    		",        x9                      NUMBER(20,0)   NULL \n" + 
		    		",        x10                     NUMBER(3,0)    NULL \n" + 
		    		",        CONSTRAINT pk_Person PRIMARY KEY( Person_id ) \n" + 
		    		");");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [ForOracle] has problems: " + result);
		    	fail("testForOracle");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testForOracle");
		}
	}
	
	@Test
	public void testForSqlSErver() {
		try {
			
			OntoUmlToDb toDb = new OntoUmlToDb( AttributesModel.getTypesAttributesGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.SQLSERVER);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               INTEGER        NOT NULL IDENTITY(1,1) PRIMARY KEY \n"+
		    		",        x1                      VARCHAR(20)    NOT NULL \n" + 
		    		",        x2                      INTEGER        NOT NULL \n" + 
		    		",        x3                      DATE           NULL \n" + 
		    		",        x4                      BIT            NULL \n" + 
		    		",        x5                      BINARY(8)      NULL \n" + 
		    		",        x6                      CHAR(3)        NULL \n" + 
		    		",        x7                      REAL           NULL \n" + 
		    		",        x8                      FLOAT(53)      NULL \n" + 
		    		",        x9                      BIGINT         NULL \n" + 
		    		",        x10                     SMALLINT       NULL \n" +
		    		");");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [ForSqlSErver] has problems: " + result);
		    	fail("testForSqlSErver");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testForSqlSErver");
		}
	}
	
	@Test
	public void testForPostgre() {
		try {
			
			OntoUmlToDb toDb = new OntoUmlToDb( AttributesModel.getTypesAttributesGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.POSTGRE);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               SERIAL         NOT NULL PRIMARY KEY \n"+
		    		",        x1                      VARCHAR(20)    NOT NULL \n" + 
		    		",        x2                      INTEGER        NOT NULL \n" + 
		    		",        x3                      DATE           NULL \n" + 
		    		",        x4                      BOOLEAN        NULL \n" + 
		    		",        x5                      BYTE(4)        NULL \n" + 
		    		",        x6                      CHAR(3)        NULL \n" + 
		    		",        x7                      FLOAT(8)       NULL \n" + 
		    		",        x8                      FLOAT(4)       NULL \n" + 
		    		",        x9                      BIGINT         NULL \n" + 
		    		",        x10                     SMALLINT       NULL \n" +
		    		");");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [ForPostgre] has problems: " + result);
		    	fail("testForPostgre");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testForPostgre");
		}
	}
	
	@Test
	public void testEnumnForPostgre() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( EnumerationsModel.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.POSTGRE);
		    toDb.setEnumFieldToLookupTable(false);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               SERIAL         NOT NULL PRIMARY KEY\n" + 
		    		",        name                    VARCHAR(20)    NOT NULL\n" + 
		    		",        phase                   phase_enum_type NOT NULL \n"+
		    		");");
		    
		    check.addCommand("CREATE TYPE phase_enum_type AS ENUM ('ADULT', 'CHILD');");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [EnumnForPostgre] has problems: " + result + "\n");
		    	fail("testEnumnForMySql");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testEnumnForPostgre");
		}
	}
}

