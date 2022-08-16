package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.HierarchyModel;
import testModels.RunningExampleModel;

public class TestViews {
	
	@Test
	public void testSimpleLiftingView() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSimpleHierarchyForLifting());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE VIEW vw_AssociatedClass AS \n" + 
		    		"SELECT   associated_class.associated_class_id AS AssociatedClassId \n" + 
		    		",        associated_class.address AS address \n" + 
		    		",        associated_class.super_class_id  AS SuperClassId \n" + 
		    		"FROM     associated_class;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.age AS age \n" + 
		    		"FROM     super_class \n" + 
		    		"WHERE    super_class.is_sub_class = TRUE ;");
		    
		    check.addCommand("CREATE VIEW vw_SuperClass AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.name AS name \n" + 
		    		"FROM     super_class;");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [SimpleLiftingView] has problems: " + result);
		    	fail("testSimpleLiftingView");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testSimpleLiftingView");
		}
	}
	
	@Test
	public void testLiftingDisjointCompleteViewInheritance() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyDisjointComplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.setPutInheritedAttributes(true);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE VIEW vw_AssociatedClass AS  \n" + 
		    		"SELECT   associated_class.associated_class_id AS AssociatedClassId \n" + 
		    		",        associated_class.address AS address \n" + 
		    		",        associated_class.super_class_id AS SuperClassId \n" + 
		    		"FROM     associated_class ;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass1 AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.name AS name \n"+
		    		",        super_class.age AS age \n" + 
		    		"FROM     super_class \n" + 
		    		"WHERE    super_class.gs_test_enum = 'SUBCLASS1' ;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass2 AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.name AS name \n"+
		    		",        super_class.height AS height \n" + 
		    		"FROM     super_class \n" + 
		    		"WHERE    super_class.gs_test_enum = 'SUBCLASS2' ;");
		    
		    check.addCommand("CREATE VIEW vw_SuperClass AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.name AS name \n" + 
		    		"FROM     super_class ;");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [LiftingDisjointCompleteViewInheritance] has problems: " + result);
		    	fail("testLiftingDisjointCompleteViewInheritance");
		    }
		} catch (Exception e) {
			e.printStackTrace();
			fail("testLiftingDisjointCompleteViewInheritance");
		}
	}
	
	@Test
	public void testSimpleLiftingViewInheritance() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSimpleHierarchyForLifting());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.setPutInheritedAttributes(true);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE VIEW vw_AssociatedClass AS \n" + 
		    		"SELECT   associated_class.associated_class_id AS AssociatedClassId \n" + 
		    		",        associated_class.address AS address \n" + 
		    		",        associated_class.super_class_id  AS SuperClassId \n" + 
		    		"FROM     associated_class;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.name AS name \n" + 
		    		",        super_class.age AS age \n"+
		    		"FROM     super_class \n" + 
		    		"WHERE    super_class.is_sub_class = TRUE ;");
		    
		    check.addCommand("CREATE VIEW vw_SuperClass AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.name AS name \n" + 
		    		"FROM     super_class;");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [SimpleLiftingViewInheritance] has problems: " + result);
		    	fail("testSimpleLiftingViewInheritance");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testSimpleLiftingViewInheritance");
		}
	}
	
	@Test
	public void testLiftingDisjointCompleteView() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyDisjointComplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE VIEW vw_AssociatedClass AS  \n" + 
		    		"SELECT   associated_class.associated_class_id AS AssociatedClassId \n" + 
		    		",        associated_class.address AS address \n" + 
		    		",        associated_class.super_class_id AS SuperClassId \n" + 
		    		"FROM     associated_class ;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass1 AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.age AS age \n" + 
		    		"FROM     super_class \n" + 
		    		"WHERE    super_class.gs_test_enum = 'SUBCLASS1' ;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass2 AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.height AS height \n" + 
		    		"FROM     super_class \n" + 
		    		"WHERE    super_class.gs_test_enum = 'SUBCLASS2' ;");
		    
		    check.addCommand("CREATE VIEW vw_SuperClass AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.name AS name \n" + 
		    		"FROM     super_class ;");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [LiftingDisjointCompleteView] has problems: " + result);
		    	fail("testLiftingDisjointCompleteView");
		    }
		} catch (Exception e) {
			e.printStackTrace();
			fail("testLiftingDisjointCompleteView");
		}
	}
	
	@Test
	public void testLiftingDisjointIncompleteView() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyDisjointIncomplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();
		    
		    CheckTransformation check = new CheckTransformation( script );

		    check.addCommand("CREATE VIEW vw_AssociatedClass AS  \n" + 
		    		"SELECT   associated_class.associated_class_id AS AssociatedClassId \n" + 
		    		",        associated_class.address AS address \n" + 
		    		",        associated_class.super_class_id AS SuperClassId \n" + 
		    		"FROM     associated_class ;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass1 AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.age AS age \n" + 
		    		"FROM     super_class \n" + 
		    		"WHERE    super_class.gs_test_enum = 'SUBCLASS1'  ;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass2 AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.height AS height \n" + 
		    		"FROM     super_class \n" + 
		    		"WHERE    super_class.gs_test_enum = 'SUBCLASS2' ;");
		    
		    check.addCommand("CREATE VIEW vw_SuperClass AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.name AS name \n" + 
		    		"FROM     super_class ;");
		     
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [LiftingDisjointIncompleteView] has problems: " + result);
		    	fail("testLiftingDisjointIncompleteView");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testLiftingDisjointIncompleteView");
		}
	}
	
	@Test
	public void testLiftingOverlappingCompleteView() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyOverlappingComplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();
		    
		    CheckTransformation check = new CheckTransformation( script );

		    check.addCommand("CREATE VIEW vw_AssociatedClass AS   \n" + 
		    		"SELECT   associated_class.associated_class_id AS AssociatedClassId \n" + 
		    		",        associated_class.address AS address \n" + 
		    		",        associated_class.super_class_id AS SuperClassId \n" + 
		    		"FROM     associated_class ;");

		    check.addCommand("CREATE VIEW vw_SubClass1 AS   \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.age AS age \n" + 
		    		"FROM     super_class  \n" + 
		    		"INNER JOIN  gs_test \n" + 
		    		"        ON  super_class.super_class_id = gs_test.super_class_id \n" + 
		    		"        AND gs_test.gs_test_enum = 'SUBCLASS1' ;");

		    check.addCommand("CREATE VIEW vw_SubClass2 AS \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.height AS height \n" + 
		    		"FROM     super_class  \n" + 
		    		"INNER JOIN  gs_test \n" + 
		    		"        ON  super_class.super_class_id = gs_test.super_class_id \n" + 
		    		"        AND gs_test.gs_test_enum = 'SUBCLASS2' ;");
		    
		    check.addCommand("CREATE VIEW vw_SuperClass AS   \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.name AS name \n" + 
		    		"FROM     super_class ;");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [LiftingOverlappingCompleteView] has problems: " + result);
		    	fail("testLiftingOverlappingCompleteView");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testLiftingOverlappingCompleteView");
		}
	}
	
	@Test
	public void testLiftingOverlappingInompleteView() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyOverlappingInomplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();
		    
		    CheckTransformation check = new CheckTransformation( script );

		    check.addCommand("CREATE VIEW vw_AssociatedClass AS  \n" + 
		    		"SELECT   associated_class.associated_class_id AS AssociatedClassId \n" + 
		    		",        associated_class.address AS address \n" + 
		    		",        associated_class.super_class_id AS SuperClassId \n" + 
		    		"FROM     associated_class ;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass1 AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.age AS age \n" + 
		    		"FROM     super_class \n" + 
		    		"INNER JOIN  gs_test \n" + 
		    		"        ON  super_class.super_class_id = gs_test.super_class_id \n" + 
		    		"        AND gs_test.gs_test_enum = 'SUBCLASS1' ;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass2 AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.height AS height \n" + 
		    		"FROM     super_class \n" + 
		    		"INNER JOIN  gs_test \n" + 
		    		"        ON  super_class.super_class_id = gs_test.super_class_id \n" + 
		    		"        AND gs_test.gs_test_enum = 'SUBCLASS2' ;");
		    
		    check.addCommand("CREATE VIEW vw_SuperClass AS  \n" + 
		    		"SELECT   super_class.super_class_id AS SuperClassId \n" + 
		    		",        super_class.name AS name \n" + 
		    		"FROM     super_class ;");
		    		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [LiftingOverlappingInompleteView] has problems: " + result);
		    	fail("testLiftingOverlappingInompleteView");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testLiftingOverlappingInompleteView");
		}
	}
	
	@Test
	public void testSimpleFlatteningView() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSimpleHierarchyForFlattening());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.setPutInheritedAttributes(false);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE VIEW vw_AssociatedClass AS  \n" + 
		    		"SELECT   associated_class.associated_class_id AS AssociatedClassId \n" + 
		    		",        associated_class.address AS address \n" + 
		    		",        associated_class.sub_class_id AS SuperClassId \n" + 
		    		"FROM     associated_class ;" );
		    
		    check.addCommand("CREATE VIEW vw_SubClass AS  \n" + 
		    		"SELECT   sub_class.sub_class_id AS SuperClassId \n" + 
		    		",        sub_class.name AS name\n" + 
		    		",        sub_class.age AS age \n" + 
		    		"FROM     sub_class ;");
		    
		    check.addCommand("CREATE VIEW vw_SuperClass AS  \n" + 
		    		"SELECT   sub_class.sub_class_id AS SuperClassId \n" + 
		    		",        sub_class.name AS name \n" + 
		    		"FROM     sub_class ;");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [SimpleFlatteningView] has problems: " + result);
		    	fail("testSimpleFlatteningView");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testSimpleFlatteningView");
		}
	}
	
	@Test
	public void testCommonFlatteningView() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getCommonHierarchyForFlattening());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();

		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE VIEW vw_AssociatedClass AS  \n" + 
		    		"SELECT   associated_class.associated_class_id AS AssociatedClassId \n" + 
		    		",        associated_class.address AS address \n" + 
		    		",        IFNULL(associated_class.sub_class2_id, \n" + 
		    		"                IFNULL(associated_class.sub_class1_id, NULL)  \n" + 
		    		"               )  AS SuperClassId \n" + 
		    		"FROM     associated_class ;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass1 AS  \n" + 
		    		"SELECT   sub_class1.sub_class1_id AS SuperClassId \n" + 
		    		",        sub_class1.age AS age \n" + 
		    		"FROM     sub_class1 ;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass2 AS  \n" + 
		    		"SELECT   sub_class2.sub_class2_id AS SuperClassId \n" + 
		    		",        sub_class2.height AS height \n" + 
		    		"FROM     sub_class2 ;");
		    
		    check.addCommand("CREATE VIEW vw_SuperClass AS  \n" + 
		    		"SELECT   sub_class1.sub_class1_id AS SuperClassId \n" + 
		    		",        sub_class1.name AS name \n" + 
		    		"FROM     sub_class1 \n" + 
		    		" \n" + 
		    		"UNION  \n" + 
		    		" \n" + 
		    		"SELECT   sub_class2.sub_class2_id AS SuperClassId \n" + 
		    		",        sub_class2.name AS name \n" + 
		    		"FROM     sub_class2 ;");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [CommonFlatteningView] has problems: " + result);
		    	fail("testCommonFlatteningView");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testCommonFlatteningView");
		}
	}
	
	@Test
	public void testRunningExampleView() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( RunningExampleModel.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setPutInheritedAttributes(false);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE VIEW vw_Adult AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		"FROM     person \n" + 
		    		"WHERE    person.life_phase_enum = 'ADULT' ;");
		    
		    check.addCommand("CREATE VIEW vw_BrazilianCitizen AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		",        person.rg AS rg \n" + 
		    		"FROM     person \n" + 
		    		"INNER JOIN  nationality \n" + 
		    		"        ON  person.person_id = nationality.person_id \n" + 
		    		"        AND nationality.nationality_enum = 'BRAZILIANCITIZEN' ;");
		    
		    check.addCommand("CREATE VIEW vw_Child AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		"FROM     person \n" + 
		    		"WHERE    person.life_phase_enum = 'CHILD' ;");
		    

		    check.addCommand("CREATE VIEW vw_Contractor AS  \n" + 
		    		"SELECT   organization.organization_id AS NamedEntityId \n" + 
		    		"FROM     organization \n" + 
		    		"WHERE    organization.is_contractor = TRUE ;");

		    check.addCommand("CREATE VIEW vw_CorporateCustomer AS  \n" + 
		    		"SELECT   organization.organization_id AS NamedEntityId \n" + 
		    		",        organization.organization_id AS CustomerId \n"+
		    		",        organization.credit_limit AS creditLimit \n" + 
		    		"FROM     organization \n" + 
		    		"WHERE    organization.is_corporate_customer = TRUE ;");
		    
		    check.addCommand("CREATE VIEW vw_Customer AS  \n" + 
		    		"SELECT   person.person_id AS CustomerId \n" + 
		    		",        person.credit_rating AS creditRating \n" + 
		    		"FROM     person \n" + 
		    		"WHERE    person.is_personal_customer = TRUE  \n" +
		    		"AND      person.life_phase_enum = 'ADULT'   \n" +
		    		" " + 
		    		"UNION  \n" + 
		    		" " + 
		    		"SELECT   organization.organization_id AS CustomerId \n" + 
		    		",        organization.credit_rating AS creditRating \n" + 
		    		"FROM     organization \n" + 
		    		"WHERE    organization.is_corporate_customer = TRUE ;");;

		    check.addCommand("CREATE VIEW vw_Employee AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		"FROM     person \n" + 
		    		"WHERE    person.is_employee = TRUE \n" + 
		    		"AND      person.life_phase_enum = 'ADULT' \n" + 
		    		";");

		    check.addCommand("CREATE VIEW vw_Employment AS  \n" + 
		    		"SELECT   employment.employment_id AS EmploymentId \n" + 
		    		",        employment.salary AS salary \n" +  
		    		",        employment.person_id AS  NamedEntityEmployeeId \n" + 
		    		",        employment.organization_id AS NamedEntityOrganizationId \n" +
		    		"FROM     employment ;");

		    check.addCommand("CREATE VIEW vw_Enrollment AS  \n" + 
		    		"SELECT   enrollment.enrollment_id AS EnrollmentId \n" + 
		    		",        enrollment.grade AS grade \n" + 
		    		",        enrollment.organization_id AS NamedEntityPrimarySchoolId \n" + 
		    		",        enrollment.person_id AS NamedEntityChildId \n" + 
		    		"FROM     enrollment ;");

		    check.addCommand("CREATE VIEW vw_Hospital AS  \n" + 
		    		"SELECT   organization.organization_id AS NamedEntityId \n" + 
		    		",        organization.capacity AS capacity \n" + 
		    		"FROM     organization \n" + 
		    		"WHERE    organization.organization_type_enum = 'HOSPITAL' ;");

		    check.addCommand("CREATE VIEW vw_ItalianCitizen AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		",        person.ci AS ci \n" + 
		    		"FROM     person \n" + 
		    		"INNER JOIN  nationality \n" + 
		    		"        ON  person.person_id = nationality.person_id \n" + 
		    		"        AND nationality.nationality_enum = 'ITALIANCITIZEN'  ;");
		    
		    check.addCommand("CREATE VIEW vw_NamedEntity AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		",        person.name AS name \n" + 
		    		"FROM     person \n" + 
		    		" \n" + 
		    		"UNION  \n" + 
		    		" \n" + 
		    		"SELECT   organization.organization_id AS NamedEntityId \n" + 
		    		",        organization.name AS name \n" + 
		    		"FROM     organization ;");
		    
		    check.addCommand("CREATE VIEW vw_Organization AS  \n" + 
		    		"SELECT   organization.organization_id AS NamedEntityId \n" + 
		    		",        organization.address AS address \n" + 
		    		"FROM     organization ;");
		    
		    check.addCommand("CREATE VIEW vw_Person AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		",        person.birth_date AS birthDate \n" + 
		    		"FROM     person ;");
		    
		    check.addCommand("CREATE VIEW vw_PersonalCustomer AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		",        person.person_id AS CustomerId \n" +
		    		",        person.credit_card AS creditCard \n" + 
		    		"FROM     person \n" + 
		    		"WHERE    person.is_personal_customer = TRUE  \n" + 
		    		"AND      person.life_phase_enum = 'ADULT' ;");
		    
		    check.addCommand("CREATE VIEW vw_PrimarySchool AS  \n" + 
		    		"SELECT   organization.organization_id AS NamedEntityId \n" + 
		    		",        organization.playground_size AS playgroundSize \n" + 
		    		"FROM     organization \n" + 
		    		"WHERE    organization.organization_type_enum = 'PRIMARYSCHOOL'  ;");
		    
		    check.addCommand("CREATE VIEW vw_SupplyContract AS  \n" + 
		    		"SELECT   supply_contract.supply_contract_id AS SupplyContractId \n" + 
		    		",        supply_contract.contract_value AS contractValue \n" + 
		    		",        IFNULL(supply_contract.organization_customer_id, \n" + 
		    		"                IFNULL(supply_contract.person_id, NULL)  \n" + 
		    		"               )  AS CustomerId \n" + 
		    		",        supply_contract.organization_contractor_id AS NamedEntityId \n" + 
		    		"FROM     supply_contract ;");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [RunningExampleView] has problems: " + result);
		    	fail("testRunningExampleView");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testRunningExampleView");
		}
	}
	
	@Test
	public void testRunningExampleViewInheritance() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( RunningExampleModel.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.setPutInheritedAttributes(true);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE VIEW vw_Adult AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n"+
		    		",        person.name AS name \n" + 
		    		",        person.birth_date AS birthDate \n" + 
		    		"FROM     person \n" + 
		    		"WHERE    person.life_phase_enum = 'ADULT' ;");
		    
		    check.addCommand("CREATE VIEW vw_BrazilianCitizen AS  \n" + 
		    		"SELECT  person.person_id AS NamedEntityId \n" + 
		    		",       person.name AS name \n" + 
		    		",       person.birth_date AS birthDate \n" + 
		    		",       person.rg AS rg \n" + 
		    		"FROM    person \n" + 
		    		"INNER JOIN  nationality \n" + 
		    		"        ON  person.person_id = nationality.person_id \n" + 
		    		"        AND nationality.nationality_enum = 'BRAZILIANCITIZEN'  ;");

		    check.addCommand("CREATE VIEW vw_Child AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		",        person.name AS name \n" + 
		    		",        person.birth_date AS birthDate \n" + 
		    		"FROM     person \n" + 
		    		"WHERE    person.life_phase_enum = 'CHILD' ;");

		    check.addCommand("CREATE VIEW vw_Contractor AS \n" + 
		    		"SELECT   organization.organization_id AS NamedEntityId \n" +
		    		",        organization.name AS name \n" + 
		    		",        organization.address AS address \n" + 
		    		"FROM     organization \n" + 
		    		"WHERE    organization.is_contractor = TRUE ;");

		    check.addCommand("CREATE VIEW vw_CorporateCustomer AS  \n" + 
		    		"SELECT   organization.organization_id AS NamedEntityId \n" + 
		    		",        organization.name AS name \n" + 
		    		",        organization.address AS address \n" + 
		    		",       organization.organization_id AS CustomerId \n" +
		    		",        organization.credit_rating AS creditRating \n" + 
		    		",        organization.credit_limit AS creditLimit \n" + 
		    		"FROM     organization \n" + 
		    		"WHERE    organization.is_corporate_customer = TRUE ;");
		    
		    check.addCommand("CREATE VIEW vw_Customer AS  \n" + 
		    		"SELECT   person.person_id AS CustomerId \n" + 
		    		",        person.credit_rating AS creditRating \n" + 
		    		"FROM     person \n" + 
		    		"WHERE    person.is_personal_customer = TRUE  \n" + 
		    		"AND      person.life_phase_enum = 'ADULT'  \n" + 
		    		" \n" + 
		    		"UNION  \n" + 
		    		" \n" + 
		    		"SELECT   organization.organization_id AS CustomerId \n" + 
		    		",        organization.credit_rating AS creditRating \n" + 
		    		"FROM     organization \n" + 
		    		"WHERE    organization.is_corporate_customer = TRUE ;");;

		    check.addCommand("CREATE VIEW vw_Employee AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		",        person.name AS name \n" + 
		    		",        person.birth_date AS birthDate \n" + 
		    		"FROM     person \n" + 
		    		"WHERE    person.life_phase_enum = 'ADULT' \n" + 
		    		"AND      person.is_employee = TRUE ;");

		    check.addCommand("CREATE VIEW vw_Employment AS  \n" + 
		    		"SELECT   employment.employment_id AS EmploymentId \n" + 
		    		",        employment.salary AS salary \n" + 
		    		",        employment.person_id AS  NamedEntityEmployeeId \n" + 
		    		",        employment.organization_id AS NamedEntityOrganizationId \n" + 
		    		"FROM     employment ;");

		    check.addCommand("CREATE VIEW vw_Enrollment AS  \n" + 
		    		"SELECT   enrollment.enrollment_id AS EnrollmentId \n" + 
		    		",        enrollment.grade AS grade \n" + 
		    		",        enrollment.organization_id AS NamedEntityPrimarySchoolId \n" + 
		    		",        enrollment.person_id AS NamedEntityChildId \n" + 
		    		"FROM     enrollment ;");

		    check.addCommand("CREATE VIEW vw_Hospital AS  \n" + 
		    		"SELECT   organization.organization_id AS NamedEntityId \n" + 
		    		",        organization.name AS name \n" + 
		    		",        organization.address AS address \n" + 
		    		",        organization.capacity AS capacity \n" + 
		    		"FROM     organization \n" + 
		    		"WHERE   organization.organization_type_enum = 'HOSPITAL' ;");

		    check.addCommand("CREATE VIEW vw_ItalianCitizen AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		",        person.name AS name \n" + 
		    		",        person.birth_date AS birthDate \n" + 
		    		",        person.ci AS ci \n" + 
		    		"FROM     person \n" + 
		    		"INNER JOIN  nationality \n" + 
		    		"        ON  person.person_id = nationality.person_id \n" + 
		    		"        AND nationality.nationality_enum = 'ITALIANCITIZEN' ;");
		    
		    check.addCommand("CREATE VIEW vw_NamedEntity AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		",        person.name AS name \n" + 
		    		"FROM     person \n" + 
		    		" \n" + 
		    		"UNION  \n" + 
		    		" \n" + 
		    		"SELECT   organization.organization_id AS NamedEntityId \n" + 
		    		",        organization.name AS name \n" + 
		    		"FROM     organization ;");
		    
		    check.addCommand("CREATE VIEW vw_Organization AS  \n" + 
		    		"SELECT   organization.organization_id AS NamedEntityId \n" + 
		    		",        organization.name AS name \n" + 
		    		",        organization.address AS address \n" + 
		    		"FROM     organization ;");
		    
		    check.addCommand("CREATE VIEW vw_Person AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		",        person.name AS name \n" + 
		    		",        person.birth_date AS birthDate \n" + 
		    		"FROM     person ;");
		    
		    check.addCommand("CREATE VIEW vw_PersonalCustomer AS  \n" + 
		    		"SELECT   person.person_id AS NamedEntityId \n" + 
		    		",        person.name AS name \n" + 
		    		",        person.birth_date AS birthDate \n" + 
		    		",        person.person_id AS CustomerId \n" +
		    		",        person.credit_rating AS creditRating \n" + 
		    		",        person.credit_card AS creditCard \n" + 
		    		"FROM     person \n" + 
		    		"WHERE    person.life_phase_enum = 'ADULT'  \n" + 
		    		"AND      person.is_personal_customer = TRUE ;");
		    
		    check.addCommand("CREATE VIEW vw_PrimarySchool AS  \n" + 
		    		"SELECT   organization.organization_id AS NamedEntityId \n" + 
		    		",        organization.name AS name \n" + 
		    		",        organization.address AS address \n" + 
		    		",        organization.playground_size AS playgroundSize \n" + 
		    		"FROM     organization \n" + 
		    		"WHERE    organization.organization_type_enum = 'PRIMARYSCHOOL' ;");
		    
		    check.addCommand("CREATE VIEW vw_SupplyContract AS  \n" + 
		    		"SELECT   supply_contract.supply_contract_id AS SupplyContractId \n" + 
		    		",        supply_contract.contract_value AS contractValue \n" + 
		    		",        IFNULL(supply_contract.organization_customer_id, \n" + 
		    		"                IFNULL(supply_contract.person_id, NULL)  \n" + 
		    		"               )  AS CustomerId \n" + 
		    		",        supply_contract.organization_contractor_id AS NamedEntityId \n" + 
		    		"FROM     supply_contract ;");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [RunningExampleViewInheritance] has problems: " + result);
		    	fail("testRunningExampleViewInheritance");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testRunningExampleViewInheritance");
		}
	}
	
	@Test
	public void testRunningExampleViewInheritanceOTpCC() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( RunningExampleModel.getGraph() );
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_CONCRETE_CLASS);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.setPutInheritedAttributes(true);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE VIEW vw_Adult AS  \n" + 
		    		"SELECT person.person_id AS namedEntityId \n" + 
		    		",      person.name AS name \n" + 
		    		",      person.birth_date AS birthDate \n" + 
		    		"FROM   person \n" + 
		    		"INNER JOIN adult \n" + 
		    		"        ON  person.person_id = adult.person_id \n" + 
		    		";");
		    
		    check.addCommand("CREATE VIEW vw_BrazilianCitizen AS  \n" + 
		    		"SELECT person.person_id AS namedEntityId \n" + 
		    		",      person.name AS name \n" + 
		    		",      person.birth_date AS birthDate \n" + 
		    		",      brazilian_citizen.rg AS rg \n" + 
		    		"FROM   person \n" + 
		    		"INNER JOIN brazilian_citizen \n" + 
		    		"        ON  person.person_id = brazilian_citizen.person_id \n" + 
		    		";");

		    check.addCommand("CREATE VIEW vw_Child AS  \n" + 
		    		"SELECT person.person_id AS namedEntityId \n" + 
		    		",      person.name AS name \n" + 
		    		",      person.birth_date AS birthDate \n" + 
		    		"FROM   person \n" + 
		    		"INNER JOIN child \n" + 
		    		"        ON  person.person_id = child.person_id \n" + 
		    		";");

		    check.addCommand("CREATE VIEW vw_Contractor AS  \n" + 
		    		"SELECT organization.organization_id AS namedEntityId \n" + 
		    		",      organization.name AS name \n" + 
		    		",      organization.address AS address \n" + 
		    		"FROM   organization \n" + 
		    		"INNER JOIN contractor \n" + 
		    		"        ON  organization.organization_id = contractor.organization_id \n" + 
		    		";");

		    check.addCommand("CREATE VIEW vw_CorporateCustomer AS  \n" + 
		    		"SELECT organization.organization_id AS namedEntityId \n" + 
		    		",      organization.name AS name \n" + 
		    		",      organization.address AS address \n" + 
		    		",      corporate_customer.organization_id AS customerId \n" + 
		    		",      corporate_customer.credit_rating AS creditRating \n" + 
		    		",      corporate_customer.credit_limit AS creditLimit \n" + 
		    		"FROM   organization \n" + 
		    		"INNER JOIN corporate_customer \n" + 
		    		"        ON  organization.organization_id = corporate_customer.organization_id \n" + 
		    		";");
		    
		    check.addCommand("CREATE VIEW vw_Customer AS  \n" + 
		    		"SELECT personal_customer.person_id AS customerId \n" + 
		    		",      personal_customer.credit_rating AS creditRating \n" + 
		    		"FROM   personal_customer \n" + 
		    		" \n" + 
		    		"UNION  \n" + 
		    		" \n" + 
		    		"SELECT corporate_customer.organization_id AS customerId \n" + 
		    		",      corporate_customer.credit_rating AS creditRating \n" + 
		    		"FROM   corporate_customer \n" + 
		    		";");

		    check.addCommand("CREATE VIEW vw_Employee AS  \n" + 
		    		"SELECT person.person_id AS namedEntityId \n" + 
		    		",      person.name AS name \n" + 
		    		",      person.birth_date AS birthDate \n" + 
		    		"FROM   person \n" + 
		    		"INNER JOIN adult \n" + 
		    		"        ON  person.person_id = adult.person_id \n" + 
		    		"INNER JOIN employee \n" + 
		    		"        ON  adult.person_id = employee.person_id \n" + 
		    		";");

		    check.addCommand("CREATE VIEW vw_Employment AS  \n" + 
		    		"SELECT employment.employment_id AS employmentId \n" + 
		    		",      employment.salary AS salary \n" + 
		    		",      employment.organization_id AS namedEntityOrganizationId \n" + 
		    		",      employment.person_id AS namedEntityEmployeeId \n" + 
		    		"FROM   employment \n" + 
		    		";");

		    check.addCommand("CREATE VIEW vw_Enrollment AS  \n" + 
		    		"SELECT enrollment.enrollment_id AS enrollmentId \n" + 
		    		",      enrollment.grade AS grade \n" + 
		    		",      enrollment.organization_id AS namedEntityPrimarySchoolId \n" + 
		    		",      enrollment.person_id AS namedEntityChildId \n" + 
		    		"FROM   enrollment \n" + 
		    		";");

		    check.addCommand("CREATE VIEW vw_Hospital AS  \n" + 
		    		"SELECT organization.organization_id AS namedEntityId \n" + 
		    		",      organization.name AS name \n" + 
		    		",      organization.address AS address \n" + 
		    		",      hospital.capacity AS capacity \n" + 
		    		"FROM   organization \n" + 
		    		"INNER JOIN hospital \n" + 
		    		"        ON  organization.organization_id = hospital.organization_id \n" + 
		    		";");

		    check.addCommand("CREATE VIEW vw_ItalianCitizen AS  \n" + 
		    		"SELECT person.person_id AS namedEntityId \n" + 
		    		",      person.name AS name \n" + 
		    		",      person.birth_date AS birthDate \n" + 
		    		",      italian_citizen.ci AS ci \n" + 
		    		"FROM   person \n" + 
		    		"INNER JOIN italian_citizen \n" + 
		    		"        ON  person.person_id = italian_citizen.person_id \n" + 
		    		";");
		    
		    check.addCommand("CREATE VIEW vw_NamedEntity AS  \n" + 
		    		"SELECT person.person_id AS namedEntityId \n" + 
		    		",      person.name AS name \n" + 
		    		"FROM   person \n" + 
		    		" \n" + 
		    		"UNION  \n" + 
		    		" \n" + 
		    		"SELECT organization.organization_id AS namedEntityId \n" + 
		    		",      organization.name AS name \n" + 
		    		"FROM   organization \n" + 
		    		";");
		    
		    check.addCommand("CREATE VIEW vw_Organization AS  \n" + 
		    		"SELECT organization.organization_id AS namedEntityId \n" + 
		    		",      organization.name AS name \n" + 
		    		",      organization.address AS address \n" + 
		    		"FROM   organization \n" + 
		    		";");
		    
		    check.addCommand("CREATE VIEW vw_Person AS  \n" + 
		    		"SELECT person.person_id AS namedEntityId \n" + 
		    		",      person.name AS name \n" + 
		    		",      person.birth_date AS birthDate \n" + 
		    		"FROM   person \n" + 
		    		";");
		    
		    check.addCommand("CREATE VIEW vw_PersonalCustomer AS  \n" + 
		    		"SELECT person.person_id AS namedEntityId \n" + 
		    		",      person.name AS name \n" + 
		    		",      person.birth_date AS birthDate \n" + 
		    		",      personal_customer.person_id AS customerId \n" + 
		    		",      personal_customer.credit_rating AS creditRating \n" + 
		    		",      personal_customer.credit_card AS creditCard \n" + 
		    		"FROM   person \n" + 
		    		"INNER JOIN adult \n" + 
		    		"        ON  person.person_id = adult.person_id \n" + 
		    		"INNER JOIN personal_customer \n" + 
		    		"        ON  adult.person_id = personal_customer.person_id \n" + 
		    		";");
		    
		    check.addCommand("CREATE VIEW vw_PrimarySchool AS  \n" + 
		    		"SELECT organization.organization_id AS namedEntityId \n" + 
		    		",      organization.name AS name \n" + 
		    		",      organization.address AS address \n" + 
		    		",      primary_school.playground_size AS playgroundSize \n" + 
		    		"FROM   organization \n" + 
		    		"INNER JOIN primary_school \n" + 
		    		"        ON  organization.organization_id = primary_school.organization_id \n" + 
		    		";");
		    
		    check.addCommand("CREATE VIEW vw_SupplyContract AS  \n" + 
		    		"SELECT supply_contract.supply_contract_id AS supplyContractId \n" + 
		    		",      supply_contract.contract_value AS contractValue \n" + 
		    		",      IFNULL(supply_contract.organization_customer_id, \n" + 
		    		"                IFNULL(supply_contract.person_id, NULL)  \n" + 
		    		"               )  AS customerId \n" + 
		    		",      supply_contract.organization_contractor_id AS namedEntityId \n" + 
		    		"FROM   supply_contract \n" + 
		    		";");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [RunningExampleViewInheritance] has problems: " + result);
		    	fail("testRunningExampleViewInheritance");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testRunningExampleViewInheritance");
		}
	}
	
	@Test
	public void testSimpleFlatteningViewInheritance() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSimpleHierarchyForFlattening());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.setPutInheritedAttributes(true);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("CREATE VIEW vw_AssociatedClass AS  \n" + 
		    		"SELECT   associated_class.associated_class_id AS AssociatedClassId \n" + 
		    		",        associated_class.address AS address \n" + 
		    		",        associated_class.sub_class_id AS SuperClassId \n" + 
		    		"FROM     associated_class ;" );
		    
		    check.addCommand("CREATE VIEW vw_SubClass AS  \n" + 
		    		"SELECT   sub_class.sub_class_id AS SuperClassId \n" + 
		    		",        sub_class.name AS name \n"+
		    		",        sub_class.age AS age \n" + 
		    		"FROM     sub_class ;");
		    
		    check.addCommand("CREATE VIEW vw_SuperClass AS  \n" + 
		    		"SELECT   sub_class.sub_class_id AS SuperClassId \n" + 
		    		",        sub_class.name AS name \n" + 
		    		"FROM     sub_class ;");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [SimpleFlatteningView] has problems: " + result);
		    	fail("testSimpleFlatteningViewInhritance");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testSimpleFlatteningViewInheritance");
		}
	}
	
	@Test
	public void testCommonFlatteningViewInheritance() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getCommonHierarchyForFlattening());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.setPutInheritedAttributes(true);
		    toDb.runTransformation();
		    String script = toDb.getViewsScript();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    check.addCommand("CREATE VIEW vw_AssociatedClass AS  \n" + 
		    		"SELECT   associated_class.associated_class_id AS AssociatedClassId \n" + 
		    		",        associated_class.address AS address \n" + 
		    		",        IFNULL(associated_class.sub_class2_id, \n" + 
		    		"                IFNULL(associated_class.sub_class1_id, NULL)  \n" + 
		    		"               )  AS SuperClassId \n" + 
		    		"FROM     associated_class ;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass1 AS  \n" + 
		    		"SELECT   sub_class1.sub_class1_id AS SuperClassId \n" +
		    		",        sub_class1.name AS name \n" + 
		    		",        sub_class1.age AS age \n" + 
		    		"FROM     sub_class1 ;");
		    
		    check.addCommand("CREATE VIEW vw_SubClass2 AS  \n" + 
		    		"SELECT   sub_class2.sub_class2_id AS SuperClassId \n" +
		    		",        sub_class2.name AS name \n" + 
		    		",        sub_class2.height AS height \n" + 
		    		"FROM     sub_class2 ;");
		    
		    check.addCommand("CREATE VIEW vw_SuperClass AS  \n" + 
		    		"SELECT   sub_class1.sub_class1_id AS SuperClassId \n" + 
		    		",        sub_class1.name AS name \n" + 
		    		"FROM     sub_class1 \n" + 
		    		" \n" + 
		    		"UNION  \n" + 
		    		" \n" + 
		    		"SELECT   sub_class2.sub_class2_id AS SuperClassId \n" + 
		    		",        sub_class2.name AS name \n" + 
		    		"FROM     sub_class2 ;");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [CommonFlatteningViewInheritance] has problems: " + result);
		    	fail("testCommonFlatteningViewInheritance");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testCommonFlatteningViewInheritance");
		}
	}
	
}
