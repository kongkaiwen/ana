package medical;

import java.util.ArrayList;

public class AnaForgotPattern {

	private static String[] verbs = {"forgotten", "misplaced", "remember"};
	private static String[] references = {"meds", "pills", "medication", "drugs", "lozenge", "medicine", "dosage", "antibiotics", "antivirals"};
	private static String[] phrases = {"have i taken", "did i take", "not sure if", "not sure"};
	
	// forgotten to take medication
	public static boolean match ( String line, ArrayList<String> pos ) {
		
		boolean output = false;
		boolean hasVerb = false;
		boolean hasReference = false;
		boolean hasPhrase = false;
		
		for (String verb: verbs) {
			if (line.toLowerCase().contains(verb.toLowerCase()))
				hasVerb = true;
		}
		
		for (String reference: references) {
			if (line.toLowerCase().contains(reference.toLowerCase()))
				hasReference = true;
		}
		
		for (String phrase: phrases) {
			if (line.toLowerCase().contains(phrase.toLowerCase()))
				hasPhrase = true;
		}
		
		if ( (hasPhrase && hasReference) || (hasVerb && hasReference) )
			output = true;
		return output;
	}
}
