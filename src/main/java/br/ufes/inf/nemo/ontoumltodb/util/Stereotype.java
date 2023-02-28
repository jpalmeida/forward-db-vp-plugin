package br.ufes.inf.nemo.ontoumltodb.util;


public enum Stereotype {

	TYPE("type"), 
	HISTORICAL_ROLE("historicalRole"), 
	HISTORICAL_ROLE_MIXIN("historicalRoleMixin"), 
	EVENT("event"),
	SITUATION("situation"), 
	CATEGORY("category"), 
	MIXIN("mixin"), 
	ROLE_MIXIN("roleMixin"), 
	PHASE_MIXIN("phaseMixin"),
	KIND("kind"), 
	COLLECTIVE("collective"), 
	QUANTITY("quantity"), 
	RELATOR("relator"), 
	QUALITY("quality"), 
	MODE("mode"),
	SUBKIND("subkind"), 
	ROLE("role"), 
	PHASE("phase"),
	ENUMERATION("enumeration"), 
	DATATYPE("datatype"),
	ABSTRACT("abstract"),
	DISPOSITION("disposition");

	private final String display;

	private Stereotype(String s) {
		display = s;
	}

	@Override
	public String toString() {
		return display;
	}
	
	public static Stereotype getUfoStereotype(String name){
	    for(Stereotype stereotype : values()){
	        if( stereotype.display.equalsIgnoreCase(name)){
	            return stereotype;
	        }
	    }
	    return null;
	}

	public static boolean isNonSortal(Stereotype type) {
		if (	type == Stereotype.CATEGORY || 
				type == Stereotype.ROLE_MIXIN || 
				type == Stereotype.PHASE_MIXIN || 
				type == Stereotype.MIXIN ||
				type == Stereotype.ABSTRACT)
			return true;
		else
			return false;
	}
	
	
	public static boolean isSortalNonKind(Stereotype type) {
	    if (	type == Stereotype.ROLE || 
	    		type == Stereotype.PHASE || 
	    		type == Stereotype.SUBKIND) 
	    	return true;
	    else return false;
	}
	
	public static boolean isWeakStereotype(Stereotype type) {
	    if (	type == Stereotype.QUALITY || 
	    		type == Stereotype.MODE || 
	    		type == Stereotype.DISPOSITION) 
	    	return true;
	    else return false;
	}
}