package events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import relations.Entity;
import tools.Helpers;

import kb.Event;

import attributes.AnaAgePattern;
import attributes.AnaDislikesPattern;
import attributes.AnaLikesPattern;
import attributes.AnaProfessionPattern;
import attributes.AnaSchoolPattern;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import entities.AnaEntity;

public class EventMatcher {

	public static boolean check(Event event, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos, SemanticGraph dep) {

		EventWhere.match(event, tkns, ent, pos);
		
		EventWhen.match(event, tkns, ent, pos, "start");
		
		EventWho.match(event, tkns, ent, pos);
		
		return false;
	}
}
