package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.TriggerResult;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.RunningExampleSlim;

public class TestPostgreTrigger {

	
	@Test
	public void testRunningExampleSlimPostgreTrigger() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( RunningExampleSlim.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.POSTGRE);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String result = "";
		    
		    for(TriggerResult triggerResult : toDb.getTriggersScripts()) {
		    	result += triggerResult.getScript() + "\n";
		    }
		    
		    CheckTransformation check = new CheckTransformation( result );
		    check.addCommand("CREATE OR REPLACE FUNCTION check_person_i\n" + 
		    		"  RETURNS TRIGGER\n" + 
		    		"  LANGUAGE PLPGSQL\n" + 
		    		"AS \n" + 
		    		"$$ \n" + 
		    		"BEGIN \n" + 
		    		"    if( \n" + 
		    		"        ( new.life_phase_enum = 'ADULT' and  ( new.is_personal_customer is null )  )  or \n" + 
		    		"        ( new.life_phase_enum <> 'ADULT' and  (  ifnull(new.is_personal_customer, false) = true )  ) \n" + 
		    		"    ) \n" + 
		    		"    then \n" + 
		    		"        RAISE EXCEPTION  'ERROR 3: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        ( new.is_personal_customer = TRUE and  ( new.credit_rating is null or new.credit_card is null )  )  or \n" + 
		    		"        ( new.is_personal_customer <> TRUE and  ( new.credit_rating is not null or new.credit_card is not null )  ) \n" + 
		    		"    ) \n" + 
		    		"    then \n" + 
		    		"        RAISE EXCEPTION  'ERROR 3: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"END; \n" + 
		    		"$$; \n" + 
		    		"\n" + 
		    		"CREATE TRIGGER tg_person_i\n" + 
		    		"AFTER INSERT ON person\n" + 
		    		"FOR EACH ROW \n" + 
		    		"EXECUTE PROCEDURE check_person_i; ");
		    
		    check.addCommand("CREATE OR REPLACE FUNCTION check_person_u\n" + 
		    		"  RETURNS TRIGGER\n" + 
		    		"  LANGUAGE PLPGSQL\n" + 
		    		"AS \n" + 
		    		"$$ \n" + 
		    		"BEGIN \n" + 
		    		"    if( \n" + 
		    		"        ( new.life_phase_enum = 'ADULT' and  ( new.is_personal_customer is null )  )  or \n" + 
		    		"        ( new.life_phase_enum <> 'ADULT' and  (  ifnull(new.is_personal_customer, false) = true )  ) \n" + 
		    		"    ) \n" + 
		    		"    then \n" + 
		    		"        RAISE EXCEPTION  'ERROR 3: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( \n" + 
		    		"        ( new.is_personal_customer = TRUE and  ( new.credit_rating is null or new.credit_card is null )  )  or \n" + 
		    		"        ( new.is_personal_customer <> TRUE and  ( new.credit_rating is not null or new.credit_card is not null )  ) \n" + 
		    		"    ) \n" + 
		    		"    then \n" + 
		    		"        RAISE EXCEPTION  'ERROR 3: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"END; \n" + 
		    		"$$; \n" + 
		    		"\n" + 
		    		"CREATE TRIGGER tg_person_u\n" + 
		    		"AFTER UPDATE ON person\n" + 
		    		"FOR EACH ROW \n" + 
		    		"EXECUTE PROCEDURE check_person_u; ");
		    
		    check.addCommand("CREATE OR REPLACE FUNCTION check_organization_i\n" + 
		    		"  RETURNS TRIGGER\n" + 
		    		"  LANGUAGE PLPGSQL\n" + 
		    		"AS \n" + 
		    		"$$ \n" + 
		    		"BEGIN \n" + 
		    		"    if( \n" + 
		    		"        ( new.is_corporate_customer = TRUE and  ( new.credit_rating is null or new.credit_limit is null )  )  or \n" + 
		    		"        ( new.is_corporate_customer <> TRUE and  ( new.credit_rating is not null or new.credit_limit is not null )  ) \n" + 
		    		"    ) \n" + 
		    		"    then \n" + 
		    		"        RAISE EXCEPTION  'ERROR 3: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"END; \n" + 
		    		"$$; \n" + 
		    		"\n" + 
		    		"CREATE TRIGGER tg_organization_i\n" + 
		    		"AFTER INSERT ON organization\n" + 
		    		"FOR EACH ROW \n" + 
		    		"EXECUTE PROCEDURE check_organization_i; ");
		    
		    check.addCommand("CREATE OR REPLACE FUNCTION check_organization_u\n" + 
		    		"  RETURNS TRIGGER\n" + 
		    		"  LANGUAGE PLPGSQL\n" + 
		    		"AS \n" + 
		    		"$$ \n" + 
		    		"BEGIN \n" + 
		    		"    if( \n" + 
		    		"        ( new.is_corporate_customer = TRUE and  ( new.credit_rating is null or new.credit_limit is null )  )  or \n" + 
		    		"        ( new.is_corporate_customer <> TRUE and  ( new.credit_rating is not null or new.credit_limit is not null )  ) \n" + 
		    		"    ) \n" + 
		    		"    then \n" + 
		    		"        RAISE EXCEPTION  'ERROR 3: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"END; \n" + 
		    		"$$; \n" + 
		    		"\n" + 
		    		"CREATE TRIGGER tg_organization_u\n" + 
		    		"AFTER UPDATE ON organization\n" + 
		    		"FOR EACH ROW \n" + 
		    		"EXECUTE PROCEDURE check_organization_u;");
		    
		    check.addCommand("CREATE OR REPLACE FUNCTION check_supply_contract_i\n" + 
		    		"  RETURNS TRIGGER\n" + 
		    		"  LANGUAGE PLPGSQL\n" + 
		    		"AS \n" + 
		    		"$$ \n" + 
		    		"BEGIN \n" + 
		    		"    if( \n" + 
		    		"        select  case when new.person_id is null then 0 else 1 end + \n" + 
		    		"                case when new.organization_customer_id is null then 0 else 1 end \n" + 
		    		"      ) <> 1 \n" + 
		    		"    then \n" + 
		    		"        RAISE EXCEPTION 'ERROR 1: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( new.organization_customer_id is not null ) \n" + 
		    		"    then \n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from organization \n" + 
		    		"                    where is_corporate_customer = TRUE \n" + 
		    		"                    and   organization.organization_id = new.organization_customer_id\n" + 
		    		"        ) \n" + 
		    		"        then \n" + 
		    		"            RAISE EXCEPTION  'ERROR 4: Violating conceptual model rules [XX_TRIGGER_NAME_XX].'; \n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( new.organization_contractor_id is not null ) \n" + 
		    		"    then \n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from organization \n" + 
		    		"                    where is_contractor = TRUE \n" + 
		    		"                    and   organization.organization_id = new.organization_contractor_id\n" + 
		    		"        ) \n" + 
		    		"        then \n" + 
		    		"            RAISE EXCEPTION  'ERROR 4: Violating conceptual model rules [XX_TRIGGER_NAME_XX].'; \n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( new.person_id is not null ) \n" + 
		    		"    then \n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from person \n" + 
		    		"                    where is_personal_customer = TRUE \n" + 
		    		"                    and   life_phase_enum = 'ADULT' \n" + 
		    		"                    and   person.person_id = new.person_id\n" + 
		    		"        ) \n" + 
		    		"        then \n" + 
		    		"            RAISE EXCEPTION  'ERROR 4: Violating conceptual model rules [XX_TRIGGER_NAME_XX].'; \n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"END; \n" + 
		    		"$$; \n" + 
		    		"\n" + 
		    		"CREATE TRIGGER tg_supply_contract_i\n" + 
		    		"AFTER INSERT ON supply_contract\n" + 
		    		"FOR EACH ROW \n" + 
		    		"EXECUTE PROCEDURE check_supply_contract_i;");
		    
		    check.addCommand("CREATE OR REPLACE FUNCTION check_supply_contract_u\n" + 
		    		"  RETURNS TRIGGER\n" + 
		    		"  LANGUAGE PLPGSQL\n" + 
		    		"AS \n" + 
		    		"$$ \n" + 
		    		"BEGIN \n" + 
		    		"    if( \n" + 
		    		"        select  case when new.person_id is null then 0 else 1 end + \n" + 
		    		"                case when new.organization_customer_id is null then 0 else 1 end \n" + 
		    		"      ) <> 1 \n" + 
		    		"    then \n" + 
		    		"        RAISE EXCEPTION 'ERROR 1: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( new.organization_customer_id is not null ) \n" + 
		    		"    then \n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from organization \n" + 
		    		"                    where is_corporate_customer = TRUE \n" + 
		    		"                    and   organization.organization_id = new.organization_customer_id\n" + 
		    		"        ) \n" + 
		    		"        then \n" + 
		    		"            RAISE EXCEPTION  'ERROR 4: Violating conceptual model rules [XX_TRIGGER_NAME_XX].'; \n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( new.organization_contractor_id is not null ) \n" + 
		    		"    then \n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from organization \n" + 
		    		"                    where is_contractor = TRUE \n" + 
		    		"                    and   organization.organization_id = new.organization_contractor_id\n" + 
		    		"        ) \n" + 
		    		"        then \n" + 
		    		"            RAISE EXCEPTION  'ERROR 4: Violating conceptual model rules [XX_TRIGGER_NAME_XX].'; \n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"    if( new.person_id is not null ) \n" + 
		    		"    then \n" + 
		    		"        if not exists ( \n" + 
		    		"                    select 1\n" + 
		    		"                    from person \n" + 
		    		"                    where is_personal_customer = TRUE \n" + 
		    		"                    and   life_phase_enum = 'ADULT' \n" + 
		    		"                    and   person.person_id = new.person_id\n" + 
		    		"        ) \n" + 
		    		"        then \n" + 
		    		"            RAISE EXCEPTION  'ERROR 4: Violating conceptual model rules [XX_TRIGGER_NAME_XX].'; \n" + 
		    		"        end if; \n" + 
		    		"\n" + 
		    		"    end if; \n" + 
		    		"\n" + 
		    		"END; \n" + 
		    		"$$; \n" + 
		    		"\n" + 
		    		"CREATE TRIGGER tg_supply_contract_u\n" + 
		    		"AFTER UPDATE ON supply_contract\n" + 
		    		"FOR EACH ROW \n" + 
		    		"EXECUTE PROCEDURE check_supply_contract_u; \n" + 
		    		"");
		    
		    result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [RunningExample Postgre] has problems: \n" + result);
		    	fail("testRunningExampleSlimPostgreTrigger");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testRunningExampleSlimPostgreTrigger");
		}
	}

	
}
