package attributes;

import java.util.ArrayList;
import java.util.HashMap;

import kb.Event;
import kb.Person;

import edu.stanford.nlp.semgraph.SemanticGraph;
import entities.AnaEntity;

public class PersonMatcher {

	public static ArrayList<String> check(String line, ArrayList<Person> people, ArrayList<AnaEntity> entities, ArrayList<String> pos, ArrayList<String> tkns, SemanticGraph dep) {
		HashMap<String, Integer> ei = new HashMap<String, Integer>();
		ArrayList<String> triples = new ArrayList<String>();
		
		// create an entity index
		for(AnaEntity ae: entities)
			ei.put(ae.getName(), line.indexOf(ae.getName()));
		
		// age pattern ( or date of birth )
		String ageMatch = AnaAgePattern.match(line, people, entities, pos, tkns);
		if (ageMatch != null)
			triples.add(ageMatch);
		
		// profession pattern ( PER works at ORG )
		String professionMatch = AnaProfessionPattern.match(line, tkns, entities, pos, dep, ei);
		if (professionMatch != null)
			triples.add(professionMatch);
		
		// education pattern ( PER goes to ORG )
		String schoolMatch = AnaSchoolPattern.match(line, entities, pos, dep, ei);
		if (schoolMatch != null)
			triples.add(schoolMatch);		
		
		// likes pattern
		String likesMatch = AnaLikesPattern.match(line, entities, pos, dep, ei);
		if (likesMatch != null)
			triples.add(likesMatch);
		
		// dislikes pattern
		String dislikesMatch = AnaDislikesPattern.match(line, entities, pos, dep, ei);
		if (dislikesMatch != null)
			triples.add(dislikesMatch);
		
		return triples;
	}
}
