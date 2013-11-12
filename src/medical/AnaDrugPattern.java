package medical;

import java.util.ArrayList;

public class AnaDrugPattern {
	
	private static String[] verbs = {"took", "swallowed", "ate", "ingested", "drank", "administer", "taking"};
	private static String[] references = {"meds", "pills", "medication", "drugs", "lozenge", "medicine", "dosage", "antibiotics", "antivirals"};
	
	// {took, swallowed, ate, ingested, drank, ...  } + drug_name
	public static boolean match ( String line, ArrayList<String> pos, ArrayList<String> drugs ) {
		
		boolean output = false;
		boolean hasVerb = false;
		boolean hasReference = false;
		boolean hasDrugName = false;
		
		for (String verb: verbs) {
			if (line.toLowerCase().contains(verb.toLowerCase()))
				hasVerb = true;
		}
		
		for (String reference: references) {
			if (line.toLowerCase().contains(reference.toLowerCase()))
				hasReference = true;
		}
		
		for (String drug: drugs) {
			if (line.toLowerCase().contains(drug.toLowerCase()))
				hasDrugName = true;
		}
		
		if ( (hasVerb && hasDrugName) || (hasVerb && hasReference) )
			output = true;
		return output;
	}
}
