package testClasses;

//import static org.junit.Assert.fail;
//
//import org.junit.jupiter.api.Test;
//
//import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
//import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
//import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
//import testModels.EnumerationsModel;

public class TestLookupTables {
/*
	@Test
	public void testeLoockupTableForEnum() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( EnumerationsModel.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    System.out.println(script);
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               INTEGER        NOT NULL PRIMARY KEY\n" + 
		    		",        phase_id                INTEGER        NOT NULL\n"+
		    		",        name                    VARCHAR(20)    NOT NULL\n" + 
		    		");");
		    check.addCommand("CREATE TABLE phase ( \n" + 
		    		"         phase_id                INTEGER        NOT NULL PRIMARY KEY\n" +
		    		",        phase_enum              VARCHAR(20)    NOT NULL\n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE person ADD FOREIGN KEY ( phase_id ) REFERENCES phase ( phase_id );");
		    
		    check.addCommand("INSERT INTO Phase(Phase_id, Phase_enum ) VALUES(1, 'ADULT');");
		    check.addCommand("INSERT INTO Phase(Phase_id, Phase_enum ) VALUES(2, 'CHILD');");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [LookupTableforEnumeration] has problems: " + result);
		    	fail("testeLoockupTableForEnum");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testeLoockupTableForEnum");
		}
	}
	
	@Test
	public void testLoockupTableEnumForMySql() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb( EnumerationsModel.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setEnumFieldToLookupTable(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE Person ( \n" + 
		    		"         Person_id               INTEGER        NOT NULL  PRIMARY KEY\n" + 
		    		",        phase_id                INTEGER        NOT NULL\n"+
		    		",        name                    VARCHAR(20)    NOT NULL\n" + 
		    		");");
		    check.addCommand("CREATE TABLE phase ( \n" + 
		    		"         phase_id                INTEGER        NOT NULL  PRIMARY KEY\n" +
		    		",        phase_enum              VARCHAR(20)    NOT NULL\n" + 
		    		"); ");
		    
		    
		    check.addCommand("ALTER TABLE person ADD FOREIGN KEY ( phase_id ) REFERENCES phase ( phase_id );");
		    
		    check.addCommand("INSERT INTO Phase(Phase_enum) VALUES('ADULT');");
		    check.addCommand("INSERT INTO Phase(Phase_enum) VALUES('CHILD');");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [EnumForMySqlWithLookupTable] has problems: " + result + "\n");
		    	fail("testLoockupTableEnumForMySql");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testLoockupTableEnumForMySql");
		}
	}
	*/
}
