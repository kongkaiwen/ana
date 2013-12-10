package attributes;

import edu.stanford.nlp.semgraph.SemanticGraph;
import entities.AnaEntity;
import graph.AnaParseGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import tools.Helpers;

import kb.KnowledgeBase;
import kb.Person;

public class DislikesPattern {
	
	private static String keyWords[] = {"hates", "dislikes", "loathes"};
	
	public static boolean match( ArrayList<String> tkns, ArrayList<String> pos, ArrayList<String> entities, ArrayList<Person> people, SemanticGraph dep, KnowledgeBase kb, boolean add ) throws IOException {
		boolean flag = false;
		boolean has_per = false;
		boolean has_key = false;

		Person per = null;
		String line = Helpers.join(tkns, " ").toLowerCase();
		
		if (people.size() > 0) {
			per = people.get(0);
			has_per = true;
		}
		
		for (String s: keyWords) {
			if (line.contains(s)) {
				has_key = true;
			}
		}
		
		// check dependency links for matches
		String match = checkDependencies(line, dep);
		
		if (match != null && has_key) {
			kb.update(per.getId(), "person", "dislikes", match);
			flag = true;
		}
		
		return flag;
	}

	/*
  	Kevin likes pizza. NNP-nsubj-VBZ-dobj-NN
  	Kevin likes to play games. NNP-nsubj-VBZ-xcomp-VB-dobj-NNS	
  	Kevin likes to eat pizza. 	NNP-nsubj-VBZ-xcomp-VB-dobj-NN
  	His favorite type of pizza is pepperoni. 
  	*/
  	private static String checkDependencies( String line, SemanticGraph dependencies ) {
  		
  		String verb = "";
  		String patt = "";
  		String output = "";
  		boolean flag = false;
  		
  		AnaParseGraph apg = new AnaParseGraph( dependencies );
  		
  		// Kevin dislikes pizza.
  		if (apg.contain("VBZ-dobj>-NN")) {
  			patt = "VBZ-dobj>-NN";
  			ArrayList<String> tokens = apg.extract(patt);
  			
  			verb = tokens.get(0).split("#")[0];
  			output = tokens.get(1).split("#")[0];
  			
  			for(String s : keyWords){
  		        if(s.compareToIgnoreCase(verb) == 0){
  		        	flag = true;
  		        }
  		    }	
  		}
  		
  		if ( flag )
  			return output;
		return null;
  	}
}
