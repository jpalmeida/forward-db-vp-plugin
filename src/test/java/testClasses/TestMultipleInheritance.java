package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.TriggerResult;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.MultipleInheritanceModel;

public class TestMultipleInheritance {

	@Test
	public void testMultipleHierarchiesSimple() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( MultipleInheritanceModel.getMultipleHierarchiesSimple() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE super_class1 (  \n" + 
		    		"        super_class1_id        INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       is_sub_class1          BOOLEAN       NOT NULL DEFAULT FALSE \n" + 
		    		",       age                    INTEGER       NULL \n" + 
		    		");  ");
		    
		    check.addCommand("CREATE TABLE super_class2 (  \n" + 
		    		"        super_class2_id        INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       is_sub_class1          BOOLEAN       NOT NULL DEFAULT FALSE \n" + 
		    		",       age                    INTEGER       NULL \n" + 
		    		");  ");
		    
		    check.addCommand("CREATE TABLE associated_class (  \n" + 
		    		"        associated_class_id    INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       super_class2_id        INTEGER       NOT NULL \n" + 
		    		",       super_class1_id        INTEGER       NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( super_class2_id ) REFERENCES super_class2 ( super_class2_id );");
		    
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( super_class1_id ) REFERENCES super_class1 ( super_class1_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [MultipleHierarchiesSimple] has problems: " + result);
		    	fail("MultipleHierarchiesSimple");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("MultipleHierarchiesSimple");
		}
	}
	
	@Test
	public void testMultipleInheritance() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( MultipleInheritanceModel.getMultipleInheritanceWithoutStereotypes() );
		    
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
	public void testMultipleHierarchies() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( MultipleInheritanceModel.getMultipleHierarchies() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String script = toDb.getRelationalSchemaScript();
		    
		    System.out.println(script);
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE super_class1 (  \n" + 
		    		"        super_class1_id        INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       is_sub_class1          BOOLEAN       NOT NULL DEFAULT FALSE \n" + 
		    		",       age                    INTEGER       NULL \n" + 
		    		",       test                   INTEGER       NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE super_class2 (  \n" + 
		    		"        super_class2_id        INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       gs_text_enum           ENUM('SUBCLASS1','SUBCLASS2','SUBCLASS3')  NOT NULL \n" + 
		    		",       age                    INTEGER       NULL \n" + 
		    		",       test                   INTEGER       NULL \n" + 
		    		",       ci                     INTEGER       NULL \n" + 
		    		",       height                 INTEGER       NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE associated_class (  \n" + 
		    		"        associated_class_id    INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       super_class2_id        INTEGER       NOT NULL \n" + 
		    		",       super_class1_id        INTEGER       NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( super_class2_id ) REFERENCES super_class2 ( super_class2_id );");
		    
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( super_class1_id ) REFERENCES super_class1 ( super_class1_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testMultipleHierarchies] has problems: " + result);
		    	fail("testMultipleHierarchies");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testMultipleHierarchies");
		}
	}
	
	@Test
	public void testMultipleHierarchiesGeneralizationSet() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( MultipleInheritanceModel.getMultipleHierarchiesGeneralizationSet() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE super_class1 (  \n" + 
		    		"        super_class1_id        INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       gs_text1_enum          ENUM('SUBCLASS1','SUBCLASS2')  NOT NULL \n" + 
		    		",       ci                     INTEGER       NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE super_class2 (  \n" + 
		    		"        super_class2_id        INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       gs_text2_enum          ENUM('SUBCLASS2','SUBCLASS3')  NOT NULL \n" + 
		    		",       ci                     INTEGER       NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE associated_class (  \n" + 
		    		"        associated_class_id    INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       super_class2_id        INTEGER       NOT NULL \n" + 
		    		",       super_class1_id        INTEGER       NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( super_class2_id ) REFERENCES super_class2 ( super_class2_id );");
		    
		    check.addCommand("ALTER TABLE associated_class ADD FOREIGN KEY ( super_class1_id ) REFERENCES super_class1 ( super_class1_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [MultipleHierarchiesGeneralizationSet] has problems: " + result);
		    	fail("MultipleHierarchiesGeneralizationSet");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("MultipleHierarchiesGeneralizationSet");
		}
	}
	
	@Test
	public void testMultipleHierarchiesTriggers() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( MultipleInheritanceModel.getMultipleHierarchies() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String result = "";
		    for(TriggerResult triggerResult : toDb.getTriggersScripts()) {
		    	result += triggerResult.getScript() + "\n";
		    }
		    
		    CheckTransformation check = new CheckTransformation( result );
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_super_class1_i  BEFORE INSERT ON super_class1  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.is_sub_class1 = TRUE and  ( new.age is null )  )  or  \n" + 
		    		"        ( new.is_sub_class1 <> TRUE and  ( new.age is not null or new.test is not null )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class1_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_super_class1_u  BEFORE UPDATE ON super_class1  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.is_sub_class1 = TRUE and  ( new.age is null )  )  or  \n" + 
		    		"        ( new.is_sub_class1 <> TRUE and  ( new.age is not null or new.test is not null )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class1_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_super_class2_i  BEFORE INSERT ON super_class2  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.gs_text_enum = 'SUBCLASS1' and  ( new.age is null )  )  or  \n" + 
		    		"        ( new.gs_text_enum <> 'SUBCLASS1' and  ( new.age is not null or new.test is not null )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class2_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.gs_text_enum = 'SUBCLASS2' and  ( new.ci is null )  )  or  \n" + 
		    		"        ( new.gs_text_enum <> 'SUBCLASS2' and  ( new.ci is not null )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class2_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.gs_text_enum = 'SUBCLASS3' and  ( new.height is null )  )  or  \n" + 
		    		"        ( new.gs_text_enum <> 'SUBCLASS3' and  ( new.height is not null )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class2_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_super_class2_u  BEFORE UPDATE ON super_class2  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.gs_text_enum = 'SUBCLASS1' and  ( new.age is null )  )  or  \n" + 
		    		"        ( new.gs_text_enum <> 'SUBCLASS1' and  ( new.age is not null or new.test is not null )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class2_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.gs_text_enum = 'SUBCLASS2' and  ( new.ci is null )  )  or  \n" + 
		    		"        ( new.gs_text_enum <> 'SUBCLASS2' and  ( new.ci is not null )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class2_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.gs_text_enum = 'SUBCLASS3' and  ( new.height is null )  )  or  \n" + 
		    		"        ( new.gs_text_enum <> 'SUBCLASS3' and  ( new.height is not null )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class2_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_associated_class_i  BEFORE INSERT ON associated_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if( new.super_class2_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class2  \n" + 
		    		"                    where gs_text_enum = 'SUBCLASS1'  \n" + 
		    		"                    and   super_class2.super_class2_id = new.super_class2_id \n" + 
		    		"        )  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_i].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if( new.super_class1_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class1  \n" + 
		    		"                    where is_sub_class1 = TRUE  \n" + 
		    		"                    and   super_class1.super_class1_id = new.super_class1_id \n" + 
		    		"        )  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_i].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_associated_class_u  BEFORE UPDATE ON associated_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if( new.super_class2_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class2  \n" + 
		    		"                    where gs_text_enum = 'SUBCLASS1'  \n" + 
		    		"                    and   super_class2.super_class2_id = new.super_class2_id \n" + 
		    		"        )  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_u].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if( new.super_class1_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class1  \n" + 
		    		"                    where is_sub_class1 = TRUE  \n" + 
		    		"                    and   super_class1.super_class1_id = new.super_class1_id \n" + 
		    		"        )  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_u].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testMultipleHierarchies] has problems: " + result);
		    	fail("testMultipleHierarchies");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testMultipleHierarchies");
		}
	}
	
	@Test
	public void testDimondHierarchyWithoutStereotypes() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( MultipleInheritanceModel.getDimondHierarchyWithoutStereotypes() );
		    
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
	
	@Test
	public void testComplexDimandoAssociation() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( MultipleInheritanceModel.getComplexDimond());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE super_class (  \n" + 
		    		"        super_class_id         INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       is_sub_class1          BOOLEAN       NOT NULL DEFAULT FALSE \n" + 
		    		",       is_bottom_class        BOOLEAN       NOT NULL DEFAULT FALSE \n" + 
		    		",       is_sub_class4          BOOLEAN       NOT NULL DEFAULT FALSE \n" + 
		    		",       is_sub_class3          BOOLEAN       NOT NULL DEFAULT FALSE \n" + 
		    		",       is_sub_class2          BOOLEAN       NOT NULL DEFAULT FALSE \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE relator (  \n" + 
		    		"        relator_id             INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       super_class_id         INTEGER       NOT NULL" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE sub_class3_super_class (  \n" + 
		    		"        sub_class3_super_class_id INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       super_class_has_sub_class343_id INTEGER       NOT NULL \n" + 
		    		",       super_class_has_sub_class344_id INTEGER       NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE relator ADD FOREIGN KEY ( super_class_id ) REFERENCES super_class ( super_class_id );");
		    
		    check.addCommand("ALTER TABLE sub_class3_super_class ADD FOREIGN KEY ( super_class_has_sub_class343_id ) REFERENCES super_class ( super_class_id );");
		   
		    check.addCommand("ALTER TABLE sub_class3_super_class ADD FOREIGN KEY ( super_class_has_sub_class344_id ) REFERENCES super_class ( super_class_id );");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [ComplexDimandoAssociation] has problems: " + result);
		    	fail("ComplexDimandoAssociation");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("ComplexDimandoAssociation");
		}
	}
}
