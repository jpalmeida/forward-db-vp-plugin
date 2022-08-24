package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.TriggerResult;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.MultipleInheritanceModel;

public class TestTriggersMultipleInheritance {
	
	@Test
	public void testTriggerComplexDimond() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( MultipleInheritanceModel.getComplexDimond());
		    
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
		    		"        ( new.is_sub_class1 = TRUE and  ( new.is_bottom_class is null or new.is_sub_class3 is null )  )  or  \n" + 
		    		"        ( new.is_sub_class1 <> TRUE and  (  ifnull(new.is_bottom_class, false) = true or  ifnull(new.is_sub_class3, false) = true )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.is_bottom_class = TRUE and  ( new.is_sub_class4 is null )  )  or  \n" + 
		    		"        ( new.is_bottom_class <> TRUE and  (  ifnull(new.is_sub_class4, false) = true )  )  \n" + 
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
		    		"        ( new.is_sub_class1 = TRUE and  ( new.is_bottom_class is null or new.is_sub_class3 is null )  )  or  \n" + 
		    		"        ( new.is_sub_class1 <> TRUE and  (  ifnull(new.is_bottom_class, false) = true or  ifnull(new.is_sub_class3, false) = true )  )  \n" + 
		    		"    )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_super_class_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        ( new.is_bottom_class = TRUE and  ( new.is_sub_class4 is null )  )  or  \n" + 
		    		"        ( new.is_bottom_class <> TRUE and  (  ifnull(new.is_sub_class4, false) = true )  )  \n" + 
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
		    		"    if( new.super_class_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" + 
		    		"                    where is_bottom_class = TRUE  \n" + 
		    		"                    and   is_sub_class1 = TRUE  \n" + 
		    		"                    and   super_class.super_class_id = new.super_class_id \n" + 
		    		"        )  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_relator_i].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if( new.super_class_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" + 
		    		"                    where is_bottom_class = TRUE  \n" + 
		    		"                    and   is_sub_class2 = TRUE  \n" + 
		    		"                    and   super_class.super_class_id = new.super_class_id \n" + 
		    		"        )  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_relator_i].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;" + 
		    		"");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_relator_u  BEFORE UPDATE ON relator  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if( new.super_class_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" + 
		    		"                    where is_bottom_class = TRUE  \n" + 
		    		"                    and   is_sub_class1 = TRUE  \n" + 
		    		"                    and   super_class.super_class_id = new.super_class_id \n" + 
		    		"        )  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_relator_u].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if( new.super_class_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" + 
		    		"                    where is_bottom_class = TRUE  \n" + 
		    		"                    and   is_sub_class2 = TRUE  \n" + 
		    		"                    and   super_class.super_class_id = new.super_class_id \n" + 
		    		"        )  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_relator_u].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
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
		    		"    if( new.super_class_has_sub_class343_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" + 
		    		"                    where is_sub_class3 = TRUE  \n" + 
		    		"                    and   is_sub_class1 = TRUE  \n" + 
		    		"                    and   super_class.super_class_id = new.super_class_has_sub_class343_id \n" + 
		    		"        )  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_sub_class3_super_class_i].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if( new.super_class_has_sub_class344_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" + 
		    		"                    where is_sub_class3 = TRUE  \n" + 
		    		"                    and   is_sub_class1 = TRUE  \n" + 
		    		"                    and   super_class.super_class_id = new.super_class_has_sub_class344_id \n" + 
		    		"        )  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_sub_class3_super_class_i].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
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
		    		"    if( new.super_class_has_sub_class343_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" + 
		    		"                    where is_sub_class3 = TRUE  \n" + 
		    		"                    and   is_sub_class1 = TRUE  \n" + 
		    		"                    and   super_class.super_class_id = new.super_class_has_sub_class343_id \n" + 
		    		"        )  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_sub_class3_super_class_u].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if( new.super_class_has_sub_class344_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    select 1 \n" + 
		    		"                    from super_class  \n" + 
		    		"                    where is_sub_class3 = TRUE  \n" + 
		    		"                    and   is_sub_class1 = TRUE  \n" + 
		    		"                    and   super_class.super_class_id = new.super_class_has_sub_class344_id \n" + 
		    		"        )  \n" + 
		    		"        then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules [tg_sub_class3_super_class_u].';  \n" + 
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
		    	System.out.println("Test [TriggerComplexDimond] has problems: \n" + result);
		    	fail("TriggerComplexDimond");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("TriggerComplexDimond");
		}
	}
	
}
