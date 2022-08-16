package testClasses;

import java.util.ArrayList;

public class CheckTransformation {
	private String script;
	private ArrayList<String> texts;
	
	public CheckTransformation(String script) {
		this.script = script;
		texts = new ArrayList<String>();
	}

	public void addCommand(String command) {
		texts.add(command);
	}
	
	public String run() {
		script = removeSpaces(script).toLowerCase();
		String textAux;
		
		for (String text : texts) {
			textAux = removeSpaces(text).toLowerCase();
			
			if(script.contains(textAux)) {
				remove(textAux);
			}
			else {
				return "Unable to find the fallowing command in the script:\n" + text;
			}
		}
		
		if(script.trim().length() > 0) {
			return "There are unidentified commands in the script:\n"+ script;
		}
		
		return null;
	}
	
	private void remove(String text) {
		int ini = script.indexOf(text);
		int end = script.indexOf(text) + text.length() ;

		script = script.substring(0, ini) + script.substring(end);
	}
	
	private String removeSpaces(String name) {
		StringBuilder aux = new StringBuilder();;
		
		int index = 0;
		while(index < name.length()) {
			if(name.charAt(index) != ' ' && name.charAt(index) != '\n' && name.charAt(index) != '\r' && name.charAt(index) != '\t') {
				aux.append(name.charAt(index));
			}
			index++;
		}
		
		return aux.toString();
	}

}
