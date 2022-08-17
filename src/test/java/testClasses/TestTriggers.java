package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.TriggerResult;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.AssociationsModel;
import testModels.HierarchyModel;
import testModels.RunningExampleModel;

public class TestTriggers {

	@Test
	public void testTriggerSimpleLifting() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSimpleHierarchyForLifting());
		    
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
		    		"CREATE TRIGGER tg_super_class_i BEFORE INSERT ON super_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if( (new.is_sub_class = true AND ( new.age is null ) ) OR \n" +
		    		"        (new.is_sub_class <> true AND ( new.age is not null ) ) \n" +
		    		"      )\n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_super_class_i].'; \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_super_class_u BEFORE UPDATE ON super_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if( (new.is_sub_class = true AND ( new.age is null ) ) OR \n" +
		    		"        (new.is_sub_class <> true AND ( new.age is not null ) ) \n" +
		    		"      )\n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_super_class_u].'; \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //    \n" + 
		    		"CREATE TRIGGER tg_associated_class_i BEFORE INSERT ON associated_class    \n" + 
		    		"FOR EACH ROW    \n" + 
		    		"BEGIN   \n" + 
		    		"   \n" + 
		    		"    declare msg varchar(128);   \n" + 
		    		"   \n" + 
		    		"        if not exists (    \n" + 
		    		"                        select 1   \n" + 
		    		"                        from super_class    \n" + 
		    		"                        where is_sub_class = true    \n" + 
		    		"                        AND   super_class.super_class_id = new.super_class_id   \n" + 
		    		"                       )    \n" + 
		    		"        then    \n" + 
		    		"                set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_i].';   \n" + 
		    		"                signal sqlstate '45000' set message_text = msg;   \n" + 
		    		"        end if;    \n" + 
		    		"   \n" + 
		    		"   \n" + 
		    		"END; //    \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //    \n" + 
		    		"CREATE TRIGGER tg_associated_class_u BEFORE UPDATE ON associated_class    \n" + 
		    		"FOR EACH ROW    \n" + 
		    		"BEGIN   \n" + 
		    		"   \n" + 
		    		"    declare msg varchar(128);   \n" + 
		    		"   \n" + 
		    		"        if not exists (    \n" + 
		    		"                        select 1   \n" + 
		    		"                        from super_class    \n" + 
		    		"                        where is_sub_class = true    \n" + 
		    		"                        AND   super_class.super_class_id = new.super_class_id   \n" + 
		    		"                       )    \n" + 
		    		"        then    \n" + 
		    		"                set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_u].';   \n" + 
		    		"                signal sqlstate '45000' set message_text = msg;   \n" + 
		    		"        end if;    \n" + 
		    		"   \n" + 
		    		"   \n" + 
		    		"END; //    \n" + 
		    		"delimiter ;");
		    
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testTriggerSimpleLifting] has problems: \n" + result);
		    	fail("testTriggerSimpleLifting");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTriggerSimpleLifting");
		}
	}
	
	@Test
	public void testTriggerLiftingGsDisjointComplete() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyDisjointComplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.setEnumFieldToLookupTable(false);
		    toDb.runTransformation();
		    
		    String result = "";
		    for(TriggerResult triggerResult : toDb.getTriggersScripts()) {
		    	result += triggerResult.getScript() + "\n";
		    }
		    
		    CheckTransformation check = new CheckTransformation( result );
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_super_class_i BEFORE INSERT ON super_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if( ( new.gs_test_enum = 'SUBCLASS1' AND ( new.age is null ) )  OR  \n" + 
		    		"        ( new.gs_test_enum <> 'SUBCLASS1' AND ( new.age is not null ) )  \n" + 
		    		"       )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_super_class_i].'; \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if( ( new.gs_test_enum = 'SUBCLASS2' AND ( new.height is null ) )  OR  \n" + 
		    		"        ( new.gs_test_enum <> 'SUBCLASS2' AND ( new.height is not null ) )  \n" + 
		    		"       )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_super_class_i].'; \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_super_class_u BEFORE UPDATE ON super_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if( ( new.gs_test_enum = 'SUBCLASS1' AND ( new.age is null ) )  OR  \n" + 
		    		"        ( new.gs_test_enum <> 'SUBCLASS1' AND ( new.age is not null ) )  \n" + 
		    		"       )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_super_class_u].'; \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if( ( new.gs_test_enum = 'SUBCLASS2' AND ( new.height is null ) )  OR  \n" + 
		    		"        ( new.gs_test_enum <> 'SUBCLASS2' AND ( new.height is not null ) )  \n" + 
		    		"       )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_super_class_u].'; \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_associated_class_i BEFORE INSERT ON associated_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" +  
		    		"                    where gs_test_enum = 'SUBCLASS1' \n" +
		    		"                    AND   super_class.super_class_id = new.super_class_id   \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_i].'; \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_associated_class_u BEFORE UPDATE ON associated_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" +  
		    		"                    where gs_test_enum = 'SUBCLASS1' \n" +
		    		"                    AND   super_class.super_class_id = new.super_class_id   \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_u].'; \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testTriggerLiftingGsDisjointComplete] has problems: \n" + result);
		    	fail("testTriggerLiftingGsDisjointComplete");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTriggerLiftingGsDisjointComplete");
		}
	}
	
	@Test
	public void testTriggerLiftingGsOverlapingComplete() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyOverlappingComplete());
		    
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
		    		"CREATE TRIGGER tg_associated_class_i  BEFORE INSERT ON associated_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" + 
		    		"                    INNER JOIN gs_test \n" + 
		    		"                            ON  super_class.super_class_id = gs_test.super_class_id \n" + 
		    		"                                AND gs_test.gs_test_enum = 'SUBCLASS1'  \n" + 
		    		"                    where super_class.super_class_id = new.super_class_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_associated_class_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
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
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" + 
		    		"                    INNER JOIN gs_test \n" + 
		    		"                            ON  super_class.super_class_id = gs_test.super_class_id \n" + 
		    		"                                AND gs_test.gs_test_enum = 'SUBCLASS1'  \n" + 
		    		"                    where super_class.super_class_id = new.super_class_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_associated_class_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testTriggerLiftingGsOverlapingComplete] has problems: \n" + result);
		    	fail("testTriggerLiftingGsOverlapingComplete");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTriggerLiftingGsOverlapingComplete");
		}
	}
	
	@Test
	public void testTriggerLiftingMultilevelHierarchy() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getMultilevelSimpleHierarchy());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String result = "";
		    for(TriggerResult triggerResult : toDb.getTriggersScripts()) {
		    	result += triggerResult.getScript() + "\n";
		    }
		    
		    CheckTransformation check = new CheckTransformation( result );
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_super_class_i BEFORE INSERT ON super_class \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.is_sub_class = true AND ( new.age is null  )  ) OR \n" + 
		    		"        (  new.is_sub_class <> true AND ( new.age is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_super_class_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.is_sub_sub_class = true AND ( new.ci is null  )  ) OR \n" + 
		    		"        (  new.is_sub_sub_class <> true AND ( new.ci is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_super_class_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_super_class_u BEFORE UPDATE ON super_class \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.is_sub_class = true AND ( new.age is null  )  ) OR \n" + 
		    		"        (  new.is_sub_class <> true AND ( new.age is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_super_class_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.is_sub_sub_class = true AND ( new.ci is null  )  ) OR \n" + 
		    		"        (  new.is_sub_sub_class <> true AND ( new.ci is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_super_class_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_associated_class_i BEFORE INSERT ON associated_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                        select 1 \n" + 
		    		"                        from super_class  \n" + 
		    		"                        where is_sub_sub_class = true  \n" + 
		    		"                        AND   is_sub_class = true  \n" + 
		    		"                        AND   super_class.super_class_id = new.super_class_id \n" + 
		    		"                       )  \n" + 
		    		"        then  \n" + 
		    		"                set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_i].'; \n" + 
		    		"                signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_associated_class_u BEFORE UPDATE ON associated_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                        select 1 \n" + 
		    		"                        from super_class  \n" + 
		    		"                        where is_sub_sub_class = true  \n" + 
		    		"                        AND   is_sub_class = true  \n" + 
		    		"                        AND   super_class.super_class_id = new.super_class_id \n" + 
		    		"                       )  \n" + 
		    		"        then  \n" + 
		    		"                set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_u].'; \n" + 
		    		"                signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testTriggerLiftingMultilevelHierarchy] has problems: \n" + result);
		    	fail("testTriggerLiftingMultilevelHierarchy");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTriggerLiftingMultilevelHierarchy");
		}
	}
	
	@Test
	public void testTriggerSimpleFlattening() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSimpleHierarchyForFlattening());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String result = "";
		    
		    for(TriggerResult triggerResult : toDb.getTriggersScripts()) {
		    	result += triggerResult.getScript() + "\n";
		    }
		    
		    if( ! result.trim().equals("")) {
		    	System.out.println("Test [testTriggerSimpleFlattening] has problems: \n" + result);
		    	fail("testTriggerSimpleFlattening");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTriggerSimpleFlattening");
		}
	}
	
	@Test
	public void testTriggerCommonMandatoryFlattening() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getCommonHierarchyMandatoryForFlattening());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String result = "";
		    for(TriggerResult triggerResult : toDb.getTriggersScripts()) {
		    	result += triggerResult.getScript() + "\n";
		    }
		    
		    CheckTransformation check = new CheckTransformation( result );
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_associated_class_i BEFORE INSERT ON associated_class \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        select  case when new.sub_class1_id is null then 0 else 1 end + \n" + 
		    		"                case when new.sub_class2_id is null then 0 else 1 end \n" + 
		    		"      ) <> 1 \n" + 
		    		"    then \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_i].'; \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_associated_class_u BEFORE UPDATE ON associated_class \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        select  case when new.sub_class1_id is null then 0 else 1 end + \n" + 
		    		"                case when new.sub_class2_id is null then 0 else 1 end \n" + 
		    		"      ) <> 1 \n" + 
		    		"    then \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_u].'; \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testTriggerCommonFlattening] has problems: \n" + result);
		    	fail("testTriggerCommonFlattening");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTriggerCommonFlattening");
		}
	}
	
	@Test
	public void testTriggerCommonOptionalFlattening() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getCommonHierarchyForFlattening());
		    
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
		    		"CREATE TRIGGER tg_associated_class_i  BEFORE INSERT ON associated_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        select  case when new.sub_class1_id is null then 0 else 1 end +  \n" + 
		    		"                case when new.sub_class2_id is null then 0 else 1 end  \n" + 
		    		"      ) > 1  \n" + 
		    		"    then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules[tg_associated_class_i].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
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
		    		"    if(  \n" + 
		    		"        select  case when new.sub_class1_id is null then 0 else 1 end +  \n" + 
		    		"                case when new.sub_class2_id is null then 0 else 1 end  \n" + 
		    		"      ) > 1  \n" + 
		    		"    then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules[tg_associated_class_u].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testTriggerCommonOptionalFlattening] has problems: \n" + result);
		    	fail("testTriggerCommonOptionalFlattening");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTriggerCommonOptionalFlattening");
		}
	}
	
	@Test
	public void testTriggerCommonOptionalLifting() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSimpleHierarchyForLiftingOptionalProperty());
		    
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
		    		"CREATE TRIGGER tg_super_class_i  BEFORE INSERT ON super_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.is_sub_class <> TRUE AND  ( new.age is not null )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_super_class_u  BEFORE UPDATE ON super_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.is_sub_class <> TRUE AND  ( new.age is not null )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_associated_class_i BEFORE INSERT ON associated_class \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from super_class \n" + 
		    		"                    where is_sub_class = true \n" + 
		    		"                    AND   super_class.super_class_id = new.super_class_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ; ");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_associated_class_u BEFORE UPDATE ON associated_class \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from super_class \n" + 
		    		"                    where is_sub_class = true \n" + 
		    		"                    AND   super_class.super_class_id = new.super_class_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ; ");
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testTriggerCommonOptionalLifting] has problems: \n" + result);
		    	fail("testTriggerCommonOptionalLifting");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTriggerCommonOptionalLifting");
		}
	}
	
	@Test
	public void testTriggerComplexFlattening() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getComplexHierarchyForFlattening());
		    
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
		    		"CREATE TRIGGER tg_associated_class_i BEFORE INSERT ON associated_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(      \n" + 
		    		"        select  case when new.sub_class1_id is null then 0 else 1 end +  \n" + 
		    		"                case when new.sub_class2_id is null then 0 else 1 end  \n" + 
		    		"      ) <> 1  \n" + 
		    		"    then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_i].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if(      \n" + 
		    		"        select  case when new.sub_class3_id is null then 0 else 1 end +  \n" + 
		    		"                case when new.sub_class4_id is null then 0 else 1 end  \n" + 
		    		"      ) <> 1  \n" + 
		    		"    then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_i].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_associated_class_u BEFORE UPDATE ON associated_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(      \n" + 
		    		"        select  case when new.sub_class1_id is null then 0 else 1 end +  \n" + 
		    		"                case when new.sub_class2_id is null then 0 else 1 end  \n" + 
		    		"      ) <> 1  \n" + 
		    		"    then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_u].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if(      \n" + 
		    		"        select  case when new.sub_class3_id is null then 0 else 1 end +  \n" + 
		    		"                case when new.sub_class4_id is null then 0 else 1 end  \n" + 
		    		"      ) <> 1  \n" + 
		    		"    then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_associated_class_u].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testTriggerCommonFlattening] has problems: \n" + result);
		    	fail("testTriggerCommonFlattening");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTriggerCommonFlattening");
		}
	}
	
	@Test
	public void testTriggerRunningExample() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(RunningExampleModel.getGraph());
		    
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
		    		"CREATE TRIGGER tg_person_i  BEFORE INSERT ON person  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        (  new.is_personal_customer = true AND ( new.credit_rating is null  OR new.credit_card is null  )  ) OR  \n" + 
		    		"        (  new.is_personal_customer <> true AND ( new.credit_rating is not null  OR new.credit_card is not null  )  )  \n" + 
		    		"      )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_person_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.life_phase_enum <> 'ADULT' AND  (  (new.is_employee is not null  and  new.is_employee = true)  OR  (new.is_personal_customer is not null  and  new.is_personal_customer = true)  )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_person_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if; "+
		    		"\n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_person_u BEFORE UPDATE ON person \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.is_personal_customer = true AND ( new.credit_rating is null  OR new.credit_card is null  )  ) OR \n" + 
		    		"        (  new.is_personal_customer <> true AND ( new.credit_rating is not null  OR new.credit_card is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_person_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.life_phase_enum <> 'ADULT' AND  (  (new.is_employee is not null  and  new.is_employee = true)  OR  (new.is_personal_customer is not null  and  new.is_personal_customer = true)  )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_person_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if; "+
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_organization_i BEFORE INSERT ON organization \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.is_corporate_customer = true AND (new.credit_rating is null  OR new.credit_limit is null  )  ) OR \n" + 
		    		"        (  new.is_corporate_customer <> true AND ( new.credit_rating is not null  OR new.credit_limit is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_organization_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.organization_type_enum = 'HOSPITAL' AND ( new.capacity is null  )  ) OR \n" + 
		    		"        (  new.organization_type_enum <> 'HOSPITAL' AND ( new.capacity is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_organization_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.organization_type_enum = 'PRIMARYSCHOOL' AND ( new.playground_size is null  )  ) OR \n" + 
		    		"        (  new.organization_type_enum <> 'PRIMARYSCHOOL' AND ( new.playground_size is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_organization_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_organization_u BEFORE UPDATE ON organization \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.is_corporate_customer = true AND ( new.credit_rating is null  OR new.credit_limit is null )  ) OR \n" + 
		    		"        (  new.is_corporate_customer <> true AND ( new.credit_rating is not null  OR new.credit_limit is not null )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_organization_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.organization_type_enum = 'HOSPITAL' AND ( new.capacity is null  )  ) OR \n" + 
		    		"        (  new.organization_type_enum <> 'HOSPITAL' AND ( new.capacity is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_organization_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.organization_type_enum = 'PRIMARYSCHOOL' AND ( new.playground_size is null  )  ) OR \n" + 
		    		"        (  new.organization_type_enum <> 'PRIMARYSCHOOL' AND ( new.playground_size is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_organization_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_employment_i BEFORE INSERT ON employment \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from person \n" + 
		    		"                    where is_employee = true \n" + 
		    		"                    AND   life_phase_enum = 'ADULT' \n" + 
		    		"                    AND   person.person_id = new.person_id \n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_employment_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_employment_u BEFORE UPDATE ON employment \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from person \n" + 
		    		"                    where is_employee = true \n" + 
		    		"                    AND   life_phase_enum = 'ADULT' \n" + 
		    		"                    AND   person.person_id = new.person_id \n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_employment_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_enrollment_i BEFORE INSERT ON enrollment \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from organization \n" + 
		    		"                    where organization_type_enum = 'PRIMARYSCHOOL' \n" + 
		    		"                    AND   organization.organization_id = new.organization_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_enrollment_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from person \n" + 
		    		"                    where life_phase_enum = 'CHILD' \n" + 
		    		"                    AND   person.person_id = new.person_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_enrollment_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_enrollment_u BEFORE UPDATE ON enrollment \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from organization \n" + 
		    		"                    where organization_type_enum = 'PRIMARYSCHOOL' \n" + 
		    		"                    AND   organization.organization_id = new.organization_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_enrollment_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from person \n" + 
		    		"                    where life_phase_enum = 'CHILD' \n" + 
		    		"                    AND   person.person_id = new.person_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_enrollment_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_supply_contract_i BEFORE INSERT ON supply_contract \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        select  case when new.person_id is null then 0 else 1 end + \n" + 
		    		"                case when new.organization_customer_id is null then 0 else 1 end \n" + 
		    		"      ) <> 1 \n" + 
		    		"    then \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_supply_contract_i].'; \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( new.organization_customer_id is not null ) \n" + 
		    		"    then \n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from organization \n" + 
		    		"                    where is_corporate_customer = true \n" + 
		    		"                    AND   organization.organization_id = new.organization_customer_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_supply_contract_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from organization \n" + 
		    		"                    where is_contractor = true \n" + 
		    		"                    AND   organization.organization_id = new.organization_contractor_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_supply_contract_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"    if( new.person_id is not null ) \n" + 
		    		"    then \n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from person \n" + 
		    		"                    where is_personal_customer = true \n" + 
		    		"                    AND   life_phase_enum = 'ADULT' \n" + 
		    		"                    AND   person.person_id = new.person_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_supply_contract_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_supply_contract_u BEFORE UPDATE ON supply_contract \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        select  case when new.person_id is null then 0 else 1 end + \n" + 
		    		"                case when new.organization_customer_id is null then 0 else 1 end \n" + 
		    		"      ) <> 1 \n" + 
		    		"    then \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_supply_contract_u].'; \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( new.organization_customer_id is not null ) \n" + 
		    		"    then \n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from organization \n" + 
		    		"                    where is_corporate_customer = true \n" + 
		    		"                    AND   organization.organization_id = new.organization_customer_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_supply_contract_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from organization \n" + 
		    		"                    where is_contractor = true \n" + 
		    		"                    AND   organization.organization_id = new.organization_contractor_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_supply_contract_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"    if( new.person_id is not null ) \n" + 
		    		"    then \n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from person \n" + 
		    		"                    where is_personal_customer = true \n" + 
		    		"                    AND   life_phase_enum = 'ADULT' \n" + 
		    		"                    AND   person.person_id = new.person_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_supply_contract_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testTriggerCommonFlattening] has problems: \n" + result);
		    	fail("testTriggerCommonFlattening");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTriggerCommonFlattening");
		}
	}
	
	@Test
	public void testLiftingPaper() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getLiftingPaper());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setEnumFieldToLookupTable(false);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String result = "";
		    
		    for(TriggerResult triggerResult : toDb.getTriggersScripts()) {
		    	result += triggerResult.getScript() + "\n";
		    }
		    
		    CheckTransformation check = new CheckTransformation( result );
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_person_i BEFORE INSERT ON person \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.nationality_enum = 'BRAZILIAN' AND ( new.age is null  )  ) OR \n" + 
		    		"        (  new.nationality_enum <> 'BRAZILIAN' AND ( new.age is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_person_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.nationality_enum = 'ITALIAN' AND ( new.height is null  )  ) OR \n" + 
		    		"        (  new.nationality_enum <> 'ITALIAN' AND ( new.height is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_person_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_person_u BEFORE UPDATE ON person \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.nationality_enum = 'BRAZILIAN' AND ( new.age is null  )  ) OR \n" + 
		    		"        (  new.nationality_enum <> 'BRAZILIAN' AND ( new.age is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_person_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        (  new.nationality_enum = 'ITALIAN' AND ( new.height is null  )  ) OR \n" + 
		    		"        (  new.nationality_enum <> 'ITALIAN' AND ( new.height is not null  )  ) \n" + 
		    		"      ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_person_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_supply_contract_i BEFORE INSERT ON supply_contract \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from person \n" + 
		    		"                    where nationality_enum = 'ITALIAN' \n" + 
		    		"                    AND   person.person_id = new.person_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_supply_contract_i].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_supply_contract_u BEFORE UPDATE ON supply_contract \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from person \n" + 
		    		"                    where nationality_enum = 'ITALIAN' \n" + 
		    		"                    AND   person.person_id = new.person_id\n" + 
		    		"                   ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_supply_contract_u].';\n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testTriggerLiftingGsDisjointCompleteEnum] has problems: \n" + result);
		    	fail("testTriggerLiftingGsDisjointCompleteEnum");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTriggerLiftingGsDisjointCompleteEnum");
		}
	}
	
	
	@Test
	public void testTriggerComplexDimond() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( HierarchyModel.getComplexDimond());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setEnumFieldToLookupTable(false);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String result = "";
		    
		    for(TriggerResult triggerResult : toDb.getTriggersScripts()) {
		    	result += triggerResult.getScript() + "\n";
		    }
		    
		    CheckTransformation check = new CheckTransformation( result );
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_super_class_i  BEFORE INSERT ON super_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.is_sub_class1 <> TRUE AND  (  (new.is_bottom_class is not null  and  new.is_bottom_class = true)  OR  (new.is_sub_class3 is not null  and  new.is_sub_class3 = true)  )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_super_class_u  BEFORE UPDATE ON super_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.is_sub_class1 <> TRUE AND  (  (new.is_bottom_class is not null  and  new.is_bottom_class = true)  OR  (new.is_sub_class3 is not null  and  new.is_sub_class3 = true)  )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_relator_i  BEFORE INSERT ON relator  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" +  
		    		"                    where is_bottom_class = true  \n" + 
		    		"                    AND   is_sub_class1 = true  \n" + 
		    		"                    AND   super_class.super_class_id = new.super_class_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_relator_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" +  
		    		"                    where is_bottom_class = true  \n" + 
		    		"                    AND   is_sub_class2 = true  \n" + 
		    		"                    AND   super_class.super_class_id = new.super_class_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_relator_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ; \n" + 
		    		"");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_relator_u  BEFORE UPDATE ON relator  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" +  
		    		"                    where is_bottom_class = true  \n" + 
		    		"                    AND   is_sub_class1 = true  \n" + 
		    		"                    AND   super_class.super_class_id = new.super_class_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_relator_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" +  
		    		"                    where is_bottom_class = true  \n" + 
		    		"                    AND   is_sub_class2 = true  \n" + 
		    		"                    AND   super_class.super_class_id = new.super_class_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_relator_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_sub_class3_super_class_i  BEFORE INSERT ON sub_class3_super_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" +  
		    		"                    where is_sub_class3 = true  \n" + 
		    		"                    AND   is_sub_class1 = true  \n" + 
		    		"                    AND   super_class.super_class_id = new.super_class_has_sub_class343_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_sub_class3_super_class_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_sub_class3_super_class_u  BEFORE UPDATE ON sub_class3_super_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" +  
		    		"                    where is_sub_class3 = true  \n" + 
		    		"                    AND   is_sub_class1 = true  \n" + 
		    		"                    AND   super_class.super_class_id = new.super_class_has_sub_class343_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_sub_class3_super_class_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [TriggerComplexDimond] has problems: \n" + result);
		    	fail("TriggerComplexDimond");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("TriggerComplexDimond");
		}
	}
	
	@Test
	public void testTriggerSelfAssociationInSubnode() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSelfAssociationInSubnode());
		    
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
		    		"CREATE TRIGGER tg_super_class_i  BEFORE INSERT ON super_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if( new.super_class_has_sub_class3_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" + 
		    		"                    INNER JOIN type \n" + 
		    		"                            ON  super_class.super_class_id = type.super_class_id \n" + 
		    		"                                AND type.typeenum = 'SUBCLASS3'  \n" + 
		    		"                    where super_class.super_class_id = new.super_class_has_sub_class3_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if( new.super_class_has_sub_class1_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" +  
		    		"                    INNER JOIN type \n" + 
		    		"                            ON  super_class.super_class_id = type.super_class_id \n" + 
		    		"                                AND type.typeenum = 'SUBCLASS1'  \n" + 
		    		"                    where super_class.super_class_id = new.super_class_has_sub_class1_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");

		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_super_class_u  BEFORE UPDATE ON super_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if( new.super_class_has_sub_class3_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" + 
		    		"                    INNER JOIN type \n" + 
		    		"                            ON  super_class.super_class_id = type.super_class_id \n" + 
		    		"                                AND type.typeenum = 'SUBCLASS3'  \n" + 
		    		"                    where super_class.super_class_id = new.super_class_has_sub_class3_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if( new.super_class_has_sub_class1_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" +  
		    		"                    INNER JOIN type \n" + 
		    		"                            ON  super_class.super_class_id = type.super_class_id \n" + 
		    		"                                AND type.typeenum = 'SUBCLASS1'  \n" + 
		    		"                    where super_class.super_class_id = new.super_class_has_sub_class1_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_sub_class1_sub_class1_i  BEFORE INSERT ON sub_class1_sub_class1  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" +  
		    		"                    INNER JOIN type \n" + 
		    		"                            ON  super_class.super_class_id = type.super_class_id \n" + 
		    		"                                AND type.typeenum = 'SUBCLASS1'  \n" + 
		    		"                    where super_class.super_class_id = new.super_class_has_sub_class132_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_sub_class1_sub_class1_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_sub_class1_sub_class1_u  BEFORE UPDATE ON sub_class1_sub_class1  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" +  
		    		"                    INNER JOIN type \n" + 
		    		"                            ON  super_class.super_class_id = type.super_class_id \n" + 
		    		"                                AND type.typeenum = 'SUBCLASS1'  \n" + 
		    		"                    where super_class.super_class_id = new.super_class_has_sub_class132_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_sub_class1_sub_class1_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [TriggerSelfAssociationInSubnode] has problems: " + result);
		    	fail("TriggerSelfAssociationInSubnode");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("TriggerSelfAssociationInSubnode");
		}
	}
	
	@Test
	public void testTriggerAssociationNNLifting() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(AssociationsModel.getGraphAssociationNNLifting());
		    
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
		    		"CREATE TRIGGER tg_relation_i  BEFORE INSERT ON relation  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from veicle  \n" + 
		    		"                    where is_car = true  \n" + 
		    		"                    AND   veicle.veicle_id = new.veicle_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_relation_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_relation_u  BEFORE UPDATE ON relation  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from veicle  \n" + 
		    		"                    where is_car = true  \n" + 
		    		"                    AND   veicle.veicle_id = new.veicle_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_relation_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");	
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_veicle_person_i  BEFORE INSERT ON veicle_person  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from veicle  \n" + 
		    		"                    where is_car = true  \n" + 
		    		"                    AND   veicle.veicle_id = new.veicle_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_veicle_person_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");	
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_veicle_person_u  BEFORE UPDATE ON veicle_person  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from veicle  \n" + 
		    		"                    where is_car = true  \n" + 
		    		"                    AND   veicle.veicle_id = new.veicle_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_veicle_person_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");	
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [TriggerAssociationNNLifting] has problems: " + result);
		    	fail("TriggerAssociationNNLifting");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("TriggerAssociationNNLifting");
		}
	}
	
	@Test
	public void testInverseRestrictionMC1() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getInverseRestrictionMC1());
		    
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
		    		"CREATE TRIGGER tg_sub_class1_i  BEFORE INSERT ON sub_class1  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if( new.related_class_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if(  \n" + 
		    		"            select  case when exists( select 1 from sub_class2 where related_class_id = new.related_class_id) then 1 else 0 end  +  \n" + 
		    		"                    case when exists( select 1 from super_class2 where related_class_id = new.related_class_id) then 1 else 0 end  \n" + 
		    		"        ) <> 0  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules[tg_sub_class1_i].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_sub_class1_u  BEFORE UPDATE ON sub_class1  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if( new.related_class_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if(  \n" + 
		    		"            select  case when exists( select 1 from sub_class2 where related_class_id = new.related_class_id) then 1 else 0 end  +  \n" + 
		    		"                    case when exists( select 1 from super_class2 where related_class_id = new.related_class_id) then 1 else 0 end  \n" + 
		    		"        ) <> 0  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules[tg_sub_class1_u].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");	
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_sub_class2_i  BEFORE INSERT ON sub_class2  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if( new.related_class_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if(  \n" + 
		    		"            select  case when exists( select 1 from sub_class1 where related_class_id = new.related_class_id) then 1 else 0 end  +  \n" + 
		    		"                    case when exists( select 1 from super_class2 where related_class_id = new.related_class_id) then 1 else 0 end  \n" + 
		    		"        ) <> 0  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules[tg_sub_class2_i].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");	
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_sub_class2_u  BEFORE UPDATE ON sub_class2  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if( new.related_class_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if(  \n" + 
		    		"            select  case when exists( select 1 from sub_class1 where related_class_id = new.related_class_id) then 1 else 0 end  +  \n" + 
		    		"                    case when exists( select 1 from super_class2 where related_class_id = new.related_class_id) then 1 else 0 end  \n" + 
		    		"        ) <> 0  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules[tg_sub_class2_u].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
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
		    		"    if( new.related_class_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if(  \n" + 
		    		"            select  case when exists( select 1 from sub_class1 where related_class_id = new.related_class_id) then 1 else 0 end  +  \n" + 
		    		"                    case when exists( select 1 from sub_class2 where related_class_id = new.related_class_id) then 1 else 0 end  \n" + 
		    		"        ) <> 0  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules[tg_super_class2_i].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
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
		    		"    if( new.related_class_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if(  \n" + 
		    		"            select  case when exists( select 1 from sub_class1 where related_class_id = new.related_class_id) then 1 else 0 end  +  \n" + 
		    		"                    case when exists( select 1 from sub_class2 where related_class_id = new.related_class_id) then 1 else 0 end  \n" + 
		    		"        ) <> 0  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules[tg_super_class2_u].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");	
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [testInverseRestrictionMC1] has problems: " + result);
		    	fail("testInverseRestrictionMC1");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testInverseRestrictionMC1");
		}
	}
	
	@Test
	public void testInverseRestrictionMC1ChangedAssociation() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getInverseRestrictionMC1ChangedAssociation());
		    
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
		    		"CREATE TRIGGER tg_related_class_i  BEFORE INSERT ON related_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        select  case when new.sub_class1_id is null then 0 else 1 end +  \n" + 
		    		"                case when new.sub_class2_id is null then 0 else 1 end  \n" + 
		    		"      ) <> 1  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_related_class_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_related_class_u  BEFORE UPDATE ON related_class  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        select  case when new.sub_class1_id is null then 0 else 1 end +  \n" + 
		    		"                case when new.sub_class2_id is null then 0 else 1 end  \n" + 
		    		"      ) <> 1  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_related_class_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");	
		    
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [InverseRestrictionMC1ChangedAssociation] has problems: " + result);
		    	fail("InverseRestrictionMC1ChangedAssociation");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("InverseRestrictionMC1ChangedAssociation");
		}
	}
}
