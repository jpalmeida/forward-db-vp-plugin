package testClasses;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import testModels.HierarchyModel;
import testModels.MultipleInheritanceModel;
import testModels.TraceTableModel;

public class TestTraceability {
	
	@Test
	public void testTraceabilityForHierarchy() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(TraceTableModel.getSimpleHierarchy());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.setEnumFieldToLookupTable(false);
		    toDb.runTransformation();
		    String traces = toDb.getStringTrace();
		    
		    CheckTransformation check = new CheckTransformation( traces );
		    
		    check.addCommand("TRACE SET: SubClass \n" + 
		    		"	TRACE: super_class [is_sub_class = true];");
		    check.addCommand("TRACE SET: SubSubClass \n" + 
		    		"	TRACE: super_class [is_sub_sub_class = true | is_sub_class = true ];");
		    check.addCommand("TRACE SET: SuperClass \n" + 
		    		"	TRACE: super_class;");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [TraceabilitySimpleFlattening] has problems: " + result);
		    	fail("testTraceabilitySimpleFlattening");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTraceabilitySimpleFlattening");
		}
	}
	
	@Test
	public void testTraceabilityForComplexHierarchy() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(TraceTableModel.getComplexHierarchy());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.setEnumFieldToLookupTable(false);
		    toDb.runTransformation();
		    String traces = toDb.getStringTrace();
		    
		    CheckTransformation check = new CheckTransformation( traces );
		    
		    check.addCommand("TRACE SET: superClassA \n" + 
		    		"	TRACE: super_class_a;");
		    check.addCommand("TRACE SET: SubClass \n" + 
		    		"	TRACE: super_class_a [is_sub_class = true];");
		    check.addCommand("TRACE SET: SubSubClass \n" + 
		    		"	TRACE: super_class_a [is_sub_sub_class = true | is_sub_class = true];");
		    check.addCommand("TRACE SET: superClassB \n" + 
		    		"	TRACE: super_class_a [is_sub_sub_class = true | is_sub_class = true];");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [TraceabilitySimpleFlattening] has problems: " + result);
		    	fail("testTraceabilitySimpleFlattening");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTraceabilitySimpleFlattening");
		}
	}
	
	
	@Test
	public void testTraceabilitySimpleFlattening() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSimpleHierarchyForFlattening());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String traces = toDb.getStringTrace();
		    
		    CheckTransformation check = new CheckTransformation( traces );
		    
		    check.addCommand("TRACE SET: SuperClass \n" + 
		    		"	TRACE: sub_class;");
		    check.addCommand("TRACE SET: SubClass \n" + 
		    		"	TRACE: sub_class;");
		    check.addCommand("TRACE SET: AssociatedClass \n" + 
		    		"	TRACE: associated_class;");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [TraceabilitySimpleFlattening] has problems: " + result);
		    	fail("testTraceabilitySimpleFlattening");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTraceabilitySimpleFlattening");
		}
	}
	
	@Test
	public void testTraceabilityCommonFlattening() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getCommonHierarchyForFlattening());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String traces = toDb.getStringTrace();
		    
		    CheckTransformation check = new CheckTransformation( traces );
		    
		    check.addCommand("TRACE SET: SuperClass \n" + 
		    		"	TRACE: sub_class1 \n" + 
		    		"	TRACE: sub_class2;");
		    check.addCommand("TRACE SET: SubClass1 \n" + 
		    		"	TRACE: sub_class1;");
		    check.addCommand("TRACE SET: SubClass2 \n" + 
		    		"	TRACE: sub_class2;");
		    check.addCommand("TRACE SET: AssociatedClass \n" + 
		    		"	TRACE: associated_class;");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [TraceabilityCommonFlattening] has problems: " + result);
		    	fail("testTraceabilityCommonFlattening");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTraceabilityCommonFlattening");
		}
	}
	
	@Test
	public void testTraceabilitySimpleLifting() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSimpleHierarchyForLifting());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String traces = toDb.getStringTrace();
		    
		    CheckTransformation check = new CheckTransformation( traces );
		    
		    check.addCommand("TRACE SET: SuperClass \n" + 
		    		"	TRACE: super_class;");
		    check.addCommand("TRACE SET: SubClass \n" + 
		    		"	TRACE: super_class [is_sub_class = true ];");
		    check.addCommand("TRACE SET: AssociatedClass \n" + 
		    		"	TRACE: associated_class;");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [TraceabilitySimpleLifting] has problems: " + result);
		    	fail("testTraceabilitySimpleLifting");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTraceabilitySimpleLifting");
		}
	}
	
	@Test
	public void testTraceabilityLiftingDisjointComplete() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyDisjointComplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String traces = toDb.getStringTrace();
		    
		    CheckTransformation check = new CheckTransformation( traces );

		    check.addCommand("TRACE SET: SuperClass \n" + 
		    		"	TRACE: super_class;");
		    check.addCommand("TRACE SET: SubClass1 \n" + 
		    		"	TRACE: super_class [gs_test_enum = SubClass1 ];");
		    check.addCommand("TRACE SET: SubClass2 \n" + 
		    		"	TRACE: super_class [gs_test_enum = SubClass2 ];");
		    check.addCommand("TRACE SET: AssociatedClass \n" + 
		    		"	TRACE: associated_class;");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [TraceabilityLiftingDisjointComplete] has problems: " + result);
		    	fail("testTraceabilityLiftingDisjointComplete");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTraceabilityLiftingDisjointComplete");
		}
	}
	
	@Test
	public void testTraceabilityLiftingDisjointIncomplete() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyDisjointIncomplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String traces = toDb.getStringTrace();
		    
		    CheckTransformation check = new CheckTransformation( traces );
		    
		    check.addCommand("TRACE SET: SuperClass \n" + 
		    		"	TRACE: super_class;");
		    check.addCommand("TRACE SET: SubClass1 \n" + 
		    		"	TRACE: super_class [gs_test_enum = SubClass1];");
		    check.addCommand("TRACE SET: SubClass2 \n" + 
		    		"	TRACE: super_class [gs_test_enum = SubClass2];");
		    check.addCommand("TRACE SET: AssociatedClass \n" + 
		    		"	TRACE: associated_class;");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [TraceabilityLiftingDisjointIncomplete] has problems: " + result);
		    	fail("testTraceabilityLiftingDisjointIncomplete");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTraceabilityLiftingDisjointIncomplete");
		}
	}
	
	@Test
	public void testTraceabilityLiftingOverlappingInomplete() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getHierarchyOverlappingInomplete());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.GENERIC_SCHEMA);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    
		    String traces = toDb.getStringTrace();
		    
		    CheckTransformation check = new CheckTransformation( traces );
		    
		    check.addCommand("TRACE SET: AssociatedClass \n" + 
		    		"	TRACE: associated_class;");
		    check.addCommand("TRACE SET: SubClass1 \n" + 
		    		"	TRACE: super_class | gs_test [gs_test_enum = SubClass1];");
		    check.addCommand("TRACE SET: SubClass2 \n" + 
		    		"	TRACE: super_class | gs_test [gs_test_enum = SubClass2];");
		    check.addCommand("TRACE SET: SuperClass \n" + 
		    		"	TRACE: super_class ;");
		    
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [TraceabilityLiftingOverlappingInomplete] has problems: " + result);
		    	fail("testTraceabilityLiftingOverlappingInomplete");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("testTraceabilityLiftingOverlappingInomplete");
		}
	}
	
	@Test
	public void testTraceabilityComplexDimandoAssociation() {
		try {
			Increment.inicialzate();
			OntoUmlToDb toDb = new OntoUmlToDb( MultipleInheritanceModel.getComplexDimond());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String traces = toDb.getStringTrace();
		    
		    CheckTransformation check = new CheckTransformation( traces );
		    check.addCommand("TRACE SET: BottomClass \n" + 
		    		"	TRACE: super_class [is_bottom_class = true | is_sub_class1 = true] | sub_class3_super_class \n" + 
		    		"	TRACE: super_class [is_bottom_class = true | is_sub_class2 = true] | sub_class3_super_class;");
		    
		    check.addCommand("TRACE SET: Relator \n" + 
		    		"	TRACE: relator;");
		    
		    check.addCommand("TRACE SET: SubClass1 \n" + 
		    		"	TRACE: super_class [is_sub_class1 = true] | sub_class3_super_class;");
		    
		    check.addCommand("TRACE SET: SubClass2 \n" + 
		    		"	TRACE: super_class [is_sub_class2 = true] | sub_class3_super_class;");
		    
		    check.addCommand("TRACE SET: SubClass3 \n" + 
		    		"	TRACE: super_class [is_sub_class3 = true | is_sub_class1 = true] | sub_class3_super_class;");
		    
		    check.addCommand("TRACE SET: SubClass4 \n" + 
		    		"	TRACE: super_class [is_bottom_class = true | is_sub_class4 = true | is_sub_class1 = true] | sub_class3_super_class \n" + 
		    		"	TRACE: super_class [is_bottom_class = true | is_sub_class4 = true | is_sub_class2 = true] | sub_class3_super_class;");
		    
		    check.addCommand("TRACE SET: SuperClass \n" + 
		    		"	TRACE: super_class | sub_class3_super_class;");
		   
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [TraceabilityComplexDimandoAssociation] has problems: " + result);
		    	fail("TraceabilityComplexDimandoAssociation");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("TraceabilityComplexDimandoAssociation");
		}
	}
	
	@Test
	public void testTracebalitySelfAssociationInSubnode() {
		try {
			OntoUmlToDb toDb = new OntoUmlToDb(HierarchyModel.getSelfAssociationInSubnode());
		    
			toDb.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);
		    toDb.setDbms(DbmsSupported.MYSQL);
		    toDb.setStandardizeNames(true);
		    toDb.runTransformation();
		    String script = toDb.getStringTrace();
		    
		    CheckTransformation check = new CheckTransformation( script );
		    
		    check.addCommand("TRACE SET: SubClass1 \n" + 
		    		"	TRACE: super_class | sub_class1_sub_class1 | type [typeenum = SubClass1] ;");
		    
		    check.addCommand("TRACE SET: SubClass2 \n" + 
		    		"	TRACE: super_class | type [typeenum = SubClass2];");
		    
		    check.addCommand("TRACE SET: SubClass3 \n" + 
		    		"	TRACE: super_class | type [typeenum = SubClass3] ;");
		    
		    check.addCommand("TRACE SET: SuperClass \n" + 
		    		"	TRACE: super_class ;");
		    
		    String result = check.run();
		    
		    if(result != null) {
		    	System.out.println("Test [TracebalitySelfAssociationInSubnode] has problems: " + result);
		    	fail("TracebalitySelfAssociationInSubnode");
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			fail("TracebalitySelfAssociationInSubnode");
		}
	}
}
