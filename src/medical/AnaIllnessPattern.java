package medical;

import java.util.ArrayList;

import tools.Helpers;

public class AnaIllnessPattern {

	/*
	I have a headache. - prp vbp det nn
	I just took my pills. - prp rb vbd prp$ nns
	Took my meds. - vbd prp$ nns
	*/
	private static String[] phrases = {"not feeling well", "feeling poorly", "feeling hot", "sick", "feeling ill", "feel ill"};
	private static String[] symptoms = {"uncomfortable", "headache", "cramped", "sweating", "irritated", "bloated", "pain", "agonizing", "sore", "dry", "sick"};
	private static String[] patterns = {"prp vbp det nn", "vbd prp$ nns"};
	
	// {uncomfortable, headache, well, cramped, hot, sweating, irritated, bloated, pain, agonizing, sore, dry, ... } + self pronoun
	public static boolean match ( String line, ArrayList<String> pos ) {
		
		boolean output = false;
		boolean hasPhrase = false;
		boolean hasSymptom = false;
		boolean hasPattern = false;
		
		String posString = Helpers.join(pos, " ");
		
		for (String symptom: symptoms) {
			if (line.toLowerCase().contains(symptom.toLowerCase()))
				hasSymptom = true;
		}
		
		for(String phrase: phrases) {
			if (line.toLowerCase().contains(phrase)) 
				hasPhrase = true;
		}
		
		for (String pattern: patterns) {
			if (posString.toLowerCase().contains(pattern.toLowerCase()))
				hasPattern = true;
		}
		
		if (hasSymptom || hasPhrase)
			output = true;
		
		return output;
	}
}
