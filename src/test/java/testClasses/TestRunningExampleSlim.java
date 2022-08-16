package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.TriggerResult;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.RunningExampleSlim;

public class TestRunningExampleSlim {

	@Test
	public void testRunningExampleSlimSchema() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( RunningExampleSlim.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE person (  \n" + 
		    		"         person_id               INTEGER        NOT NULL  PRIMARY KEY \n" + 
		    		",        name                    VARCHAR(20)    NOT NULL  \n" + 
		    		",        birth_date              DATE           NOT NULL  \n" +
		    		",        life_phase_enum         ENUM('CHILD', 'ADULT')   NOT NULL  \n" +
		    		",        is_personal_customer    BOOLEAN        NOT NULL DEFAULT FALSE \n" +
		    		",        credit_rating           DOUBLE         NULL  \n" + 
		    		",        credit_card             VARCHAR(20)    NULL  \n" + 
		    		"); ");
		    check.addCommand("CREATE TABLE organization (   \n" + 
		    		"         organization_id         INTEGER        NOT NULL  PRIMARY KEY \n" + 
		    		",        name                    VARCHAR(20)    NOT NULL \n" + 
		    		",        address                 VARCHAR(20)    NOT NULL \n" + 
		    		",        is_contractor           BOOLEAN        NOT NULL DEFAULT FALSE \n" + 
		    		",        is_corporate_customer   BOOLEAN        NOT NULL DEFAULT FALSE \n" + 
		    		",        credit_rating           DOUBLE         NULL \n" + 
		    		",        credit_limit            INTEGER        NULL \n" + 
		    		"); ");
		    check.addCommand("CREATE TABLE supply_contract (  \n" + 
		    		"         supply_contract_id      INTEGER        NOT NULL  PRIMARY KEY \n" + 
		    		",        organization_customer_id    INTEGER    NULL \n" + 
		    		",        organization_contractor_id  INTEGER    NOT NULL \n" + 
		    		",        person_id               INTEGER        NULL \n" + 
		    		",        contract_value          DOUBLE         NOT NULL \n" + 
		    		"); ");


		    check.addCommand("ALTER TABLE supply_contract ADD FOREIGN KEY ( organization_customer_id ) REFERENCES organization ( organization_id );");
		    check.addCommand("ALTER TABLE supply_contract ADD FOREIGN KEY ( organization_contractor_id ) REFERENCES organization ( organization_id );");
		    check.addCommand("ALTER TABLE supply_contract ADD FOREIGN KEY ( person_id ) REFERENCES person ( person_id );");
		   
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [RunningExample] has problems: \n" + result);
		    	fail("testRunningExample");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testRunningExample");
		}
	}
	
	@Test
	public void testRunningExampleSlimTrigger() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( RunningExampleSlim.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String result = "";
		    
		    for(TriggerResult triggerResult : toDb.getTriggersScripts()) {
		    	result += triggerResult.getScript() + "\n";
		    }
		    
		    System.out.println(result);
		    
		    CheckTransformation check = new CheckTransformation( result );
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_person_i  BEFORE INSERT ON person \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"         ( NEW.life_phase_enum <> 'ADULT' AND ( (NEW.is_personal_customer is not null  AND NEW.is_personal_customer = TRUE ) OR NEW.credit_rating is not null  OR NEW.credit_card is not null  )  ) \n" + 
		    		"    ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_person_i].'; \n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"         ( NEW.is_personal_customer = TRUE AND ( NEW.credit_rating is null  OR NEW.credit_card is null  )  )  OR \n" + 
		    		"         ( NEW.is_personal_customer <> TRUE AND ( NEW.credit_rating is not null  OR NEW.credit_card is not null  )  ) \n" + 
		    		"    ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_person_i].'; \n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // \n" + 
		    		"CREATE TRIGGER tg_person_u  BEFORE UPDATE ON person \n" + 
		    		"FOR EACH ROW \n" + 
		    		"BEGIN\n" + 
		    		"\n" + 
		    		"    declare msg varchar(128);\n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"         ( NEW.life_phase_enum <> 'ADULT' AND ( (NEW.is_personal_customer is not null  AND NEW.is_personal_customer = TRUE ) OR NEW.credit_rating is not null  OR NEW.credit_card is not null  )  ) \n" + 
		    		"    ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_person_u].'; \n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"         ( NEW.is_personal_customer = TRUE AND ( NEW.credit_rating is null  OR NEW.credit_card is null  )  )  OR \n" + 
		    		"         ( NEW.is_personal_customer <> TRUE AND ( NEW.credit_rating is not null  OR NEW.credit_card is not null  )  ) \n" + 
		    		"    ) \n" + 
		    		"    then \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_person_u].'; \n" + 
		    		"        signal sqlstate '45000' set message_text = msg;\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"\n" + 
		    		"END; // \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // "+
		    		"CREATE TRIGGER tg_organization_i BEFORE INSERT ON organization  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        (  NEW.is_corporate_customer = TRUE AND ( NEW.credit_rating is null  OR NEW.credit_limit is null  )  ) OR  \n" + 
		    		"        (  NEW.is_corporate_customer <> TRUE AND ( NEW.credit_rating is not null  OR NEW.credit_limit is not null  )  )  \n" + 
		    		"      )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_organization_i].'; \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter // "+
		    		"CREATE TRIGGER tg_organization_u BEFORE UPDATE ON organization  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        (  NEW.is_corporate_customer = TRUE AND ( NEW.credit_rating is null  OR NEW.credit_limit is null  )  ) OR  \n" + 
		    		"        (  NEW.is_corporate_customer <> TRUE AND ( NEW.credit_rating is not null  OR NEW.credit_limit is not null  )  )  \n" + 
		    		"      )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules [tg_organization_u].'; \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_supply_contract_i  BEFORE INSERT ON supply_contract  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        SELECT  case when NEW.organization_customer_id is null then 0 else 1 end +  \n" + 
		    		"                case when NEW.person_id is null then 0 else 1 end  \n" + 
		    		"      ) <> 1  \n" + 
		    		"    then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules[tg_supply_contract_i].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if( NEW.organization_customer_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    SELECT 1 \n" + 
		    		"                    FROM organization  \n" + 
		    		"                    WHERE is_corporate_customer = TRUE  \n" + 
		    		"                    AND   organization.organization_id = NEW.organization_customer_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_supply_contract_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    SELECT 1 \n" + 
		    		"                    FROM organization  \n" + 
		    		"                    WHERE is_contractor = TRUE  \n" + 
		    		"                    AND   organization.organization_id = NEW.organization_contractor_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_supply_contract_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    if( NEW.person_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    SELECT 1 \n" + 
		    		"                    FROM person  \n" + 
		    		"                    WHERE is_personal_customer = TRUE  \n" + 
		    		"                    AND   life_phase_enum = 'ADULT'  \n" + 
		    		"                    AND   person.person_id = NEW.person_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_supply_contract_i].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");
		    
		    check.addCommand("delimiter //  \n" + 
		    		"CREATE TRIGGER tg_supply_contract_u  BEFORE UPDATE ON supply_contract  \n" + 
		    		"FOR EACH ROW  \n" + 
		    		"BEGIN \n" + 
		    		" \n" + 
		    		"    declare msg varchar(128); \n" + 
		    		" \n" + 
		    		"    if(  \n" + 
		    		"        SELECT  case when NEW.organization_customer_id is null then 0 else 1 end +  \n" + 
		    		"                case when NEW.person_id is null then 0 else 1 end  \n" + 
		    		"      ) <> 1  \n" + 
		    		"    then  \n" + 
		    		"            set msg = 'ERROR: Violating conceptual model rules[tg_supply_contract_u].';  \n" + 
		    		"            signal sqlstate '45000' set message_text = msg;  \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"    if( NEW.organization_customer_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    SELECT 1 \n" + 
		    		"                    FROM organization  \n" + 
		    		"                    WHERE is_corporate_customer = TRUE  \n" + 
		    		"                    AND   organization.organization_id = NEW.organization_customer_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_supply_contract_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		"        if not exists (  \n" + 
		    		"                    SELECT 1 \n" + 
		    		"                    FROM organization  \n" + 
		    		"                    WHERE is_contractor = TRUE  \n" + 
		    		"                    AND   organization.organization_id = NEW.organization_contractor_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_supply_contract_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    if( NEW.person_id is not null )  \n" + 
		    		"    then  \n" + 
		    		"        if not exists (  \n" + 
		    		"                    SELECT 1 \n" + 
		    		"                    FROM person  \n" + 
		    		"                    WHERE is_personal_customer = TRUE  \n" + 
		    		"                    AND   life_phase_enum = 'ADULT'  \n" + 
		    		"                    AND   person.person_id = NEW.person_id \n" + 
		    		"                   )  \n" + 
		    		"    then  \n" + 
		    		"        set msg = 'ERROR: Violating conceptual model rules[tg_supply_contract_u].';  \n" + 
		    		"        signal sqlstate '45000' set message_text = msg; \n" + 
		    		"        end if;  \n" + 
		    		" \n" + 
		    		"    end if;  \n" + 
		    		" \n" + 
		    		" \n" + 
		    		"END; //  \n" + 
		    		"delimiter ;");

		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [RunningExample] has problems: \n" + result);
		    	fail("testRunningExample");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testRunningExample");
		}
	}
	
	
	@Test
	public void testRunningExampleSlimSchemaPTpCC() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( RunningExampleSlim.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_CONCRETE_CLASS);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getRelationalSchemaScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE TABLE child (  \n" + 
		    		"        person_id              INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE personal_customer (  \n" + 
		    		"        person_id              INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       credit_rating          DOUBLE        NOT NULL \n" + 
		    		",       credit_card            VARCHAR(20)   NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE corporate_customer (  \n" + 
		    		"        organization_id        INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		",       credit_rating          DOUBLE        NOT NULL \n" + 
		    		",       credit_limit           INTEGER       NOT NULL \n" + 
		    		");  ");

		    check.addCommand("CREATE TABLE adult (  \n" + 
		    		"        person_id              INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE contractor (  \n" + 
		    		"        organization_id        INTEGER       NOT NULL PRIMARY KEY \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE person (  \n" + 
		    		"        person_id              INTEGER       NOT NULL  PRIMARY KEY \n" + 
		    		",       name                   VARCHAR(20)   NOT NULL \n" + 
		    		",       birth_date             DATE          NOT NULL \n" + 
		    		");");
		    
		    check.addCommand("CREATE TABLE organization (  \n" + 
		    		"        organization_id        INTEGER       NOT NULL  PRIMARY KEY \n" + 
		    		",       name                   VARCHAR(20)   NOT NULL \n" + 
		    		",       address                VARCHAR(20)   NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("CREATE TABLE supply_contract (  \n" + 
		    		"        supply_contract_id         INTEGER       NOT NULL  PRIMARY KEY \n" + 
		    		",       organization_customer_id   INTEGER       NULL \n" + 
		    		",       person_id                  INTEGER       NULL \n" + 
		    		",       organization_contractor_id INTEGER       NOT NULL \n" + 
		    		",       contract_value             DOUBLE        NOT NULL \n" + 
		    		"); ");
		    
		    check.addCommand("ALTER TABLE child ADD FOREIGN KEY ( person_id ) REFERENCES person ( person_id );");
		    
		    check.addCommand("ALTER TABLE personal_customer ADD FOREIGN KEY ( person_id ) REFERENCES adult ( person_id );");
		    
		    check.addCommand("ALTER TABLE corporate_customer ADD FOREIGN KEY ( organization_id ) REFERENCES organization ( organization_id );");
		    
		    check.addCommand("ALTER TABLE adult ADD FOREIGN KEY ( person_id ) REFERENCES person ( person_id );");
		    
		    check.addCommand("ALTER TABLE contractor ADD FOREIGN KEY ( organization_id ) REFERENCES organization ( organization_id );");
		    
		    check.addCommand("ALTER TABLE supply_contract ADD FOREIGN KEY ( organization_customer_id ) REFERENCES corporate_customer ( organization_id );");
		    
		    check.addCommand("ALTER TABLE supply_contract ADD FOREIGN KEY ( person_id ) REFERENCES personal_customer ( person_id );");
		    
		    check.addCommand("ALTER TABLE supply_contract ADD FOREIGN KEY ( organization_contractor_id ) REFERENCES contractor ( organization_id );");
		   
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [RunningExample] has problems: \n" + result);
		    	fail("testRunningExample");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testRunningExample");
		}
	}
	
//	@Test
//	public void testTracebalityRunningExampleSlim() {
//		try {
//			OntoUmlToDb toDb = new OntoUmlToDb(RunningExampleSlim.getGraph());
//		    
//			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
//		    toDb.setDbms(DbmsSupported.MYSQL);
//		    toDb.setStandardizeNames(true);
//		    toDb.runTransformation();
//		    String script = toDb.getStringTrace();
//		    
//		    CheckTransformation check = new CheckTransformation( script );
//		    
//		    System.out.println(script);
//		    
//		    check.addCommand("");
//		    
//		    String result = check.run();
//		    
//		    if(result != null) {
//		    	System.out.println("Test [TracebalityRunningExampleSlim] has problems: " + result);
//		    	fail("TracebalityRunningExampleSlim");
//		    }
//		    
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("TracebalityRunningExampleSlim");
//		}
//	}
}
