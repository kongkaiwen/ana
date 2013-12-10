package attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import tools.Helpers;

import kb.KnowledgeBase;
import kb.Person;
import edu.stanford.nlp.semgraph.SemanticGraph;
import entities.AnaEntity;
import graph.AnaParseGraph;

public class SchoolPattern {

	private static String keyWords[] = {"school", "university", "institute", "college"};
	private static String verbs[] = {"attend", "go to", "enrolled", "studies"};
	
	public static boolean match(ArrayList<String> tkns, ArrayList<String> pos, ArrayList<String> entities, ArrayList<Person> people, SemanticGraph dep, KnowledgeBase kb, boolean add) throws IOException {

		boolean flag = false;
		boolean has_per = false;
		boolean has_org = false;
		boolean has_key = false;
		
		String org = null;
		Person per = null;
		String line = Helpers.join(tkns, " ").toLowerCase();
		
		if (people.size() > 0) {
			per = people.get(0);
			has_per = true;
		}
		
		for (String ent: entities) {
			if (ent.equals("ORGANIZATION") ) {
				has_org = true;
				org = tkns.get(entities.indexOf(ent));;
			}
		}
		
		for (String s: keyWords) {
			if (line.contains(s)) {
				has_key = true;
			}
		}
		
		for (String v: verbs) {
			if (line.contains(v)) {
				has_key = true;
			}
		}

		// any simple matches?
		flag = has_per && has_org && has_key;
		
		if (flag) {
			kb.update(per.getId(), "person", "education_institute", org);
		}
		
		// check dependency links for matches
		String match = checkDependencies(line, dep);
		
		if (match != null) {
			kb.update(per.getId(), "person", "education_institute", match);
			flag = true;
		}
		
		return flag;
	}
	
	/*
  	Kevin is a student at the University of Alberta. NNP-nsubj-NN-PREP-NNP ( PER, TITLE, ORG )
  	*/
	private static String checkDependencies( String line, SemanticGraph dependencies ) {
  		
  		String patt = "";
  		String output = "";
  		String position = "";
  		String institute = "";
  		boolean flag = false;
  		
  		AnaParseGraph apg = new AnaParseGraph( dependencies );
  		
  		// Kevin is a student at the University of Alberta.
  		if (apg.contain("NN-prep_at>-NNP")) {
  			patt = "NN-prep_at>-NNP";
  			ArrayList<String> tokens = apg.extract(patt);
  			position = tokens.get(0).split("#")[0];
  			institute = tokens.get(1).split("#")[0];
  			
  			output = institute;
  			if (position.equals("student"))
  				flag = true;
  		}
  		
  		if ( flag )
  			return output;
		return null;
  	}
}
