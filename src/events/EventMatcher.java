package events;

import java.util.ArrayList;

import relations.Entity;

import kb.Event;

import edu.stanford.nlp.semgraph.SemanticGraph;

public class EventMatcher {

	public static boolean check(Event event, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos, SemanticGraph dep) {

		EventWhere.match(event, tkns, ent, pos);
		
		EventWhen.match(event, tkns, ent, pos, "start");
		
		EventWho.match(event, tkns, ent, pos);
		
		return false;
	}
}
