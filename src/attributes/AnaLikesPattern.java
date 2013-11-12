package attributes;

import java.util.ArrayList;
import java.util.HashMap;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.semgraph.SemanticGraphEdge;
import entities.AnaEntity;
import graph.AnaEdge;
import graph.AnaNode;
import graph.AnaParseGraph;

public class AnaLikesPattern {
	
	private static String keyWords[] = {"likes", "favorite", "loves", "enjoys"};

	/*
	In order to match there must be a person and a likes.  There must also be a keyword.
	*/
	public static String match( String line, ArrayList<AnaEntity> ent, ArrayList<String> pos, 
			SemanticGraph dep, HashMap<String, Integer> ei ) {
		boolean flag = false;
		boolean has_per = false;
		boolean has_key = false;

		AnaEntity per = null;
		
		for (AnaEntity ae: ent) {
			if (ae.getType().equals("PER") ) {
				has_per = true;
				per = ae;
			}
		}
		
		for (String s: keyWords) {
			if (line.contains(s)) {
				has_key = true;
			}
		}
		
		// check dependency links for matches
		String match = checkDependencies(line, dep, ei);
		
		if (has_per && match != null) {
			return per.getId() + "#likes#" + match; 
		}
		
		return null;
	}
	
  	/*
  	Kevin likes pizza. NNP-nsubj-VBZ-dobj-NN
  	Kevin likes to play games. NNP-nsubj-VBZ-xcomp-VB-dobj-NNS	
  	Kevin likes to eat pizza. 	NNP-nsubj-VBZ-xcomp-VB-dobj-NN
  	His favorite type of pizza is pepperoni. 
  	*/
	private static String checkDependencies( String line, SemanticGraph dependencies, HashMap<String, Integer> entityIndex ) {
  		
		String verb = "";
  		String patt = "";
  		String output = "";
  		boolean flag = false;
  		
  		AnaParseGraph apg = new AnaParseGraph( dependencies );
  		
  		// Kevin likes pizza.
  		if (apg.contain("VBZ-dobj>-NN")) {
  			patt = "VBZ-dobj>-NN";
  			ArrayList<String> tokens = apg.extract(patt);
  			
  			verb = tokens.get(0).split("#")[0];
  			output = tokens.get(1).split("#")[0];
  			
  			for(String s : keyWords){
  		        if(s.compareToIgnoreCase(verb) == 0) {
  		        	flag = true;
  		        }
  		    }	
  		}
  		
  		// Kevin likes cookies.
  		if (apg.contain("VBZ-dobj>-NNS")) {
  			patt = "VBZ-dobj>-NNS";
  			ArrayList<String> tokens = apg.extract(patt);
  			
  			verb = tokens.get(0).split("#")[0];
  			output = tokens.get(1).split("#")[0];
  			
  			for(String s : keyWords){
  		        if(s.compareToIgnoreCase(verb) == 0) {
  		        	flag = true;
  		        }
  		    }	
  		}
  		
  		// Kevin likes to swim.
  		if (apg.contain("VBZ-xcomp>-VB")) {
  			patt = "VBZ-xcomp>-VB";
  			ArrayList<String> tokens = apg.extract(patt);

  			verb = tokens.get(0).split("#")[0];
  			output = tokens.get(1).split("#")[0];
  			
  			for(String s : keyWords){
  		        if(s.compareToIgnoreCase(verb) == 0) {
  		        	flag = true;
  		        }
  		    }
  		}
  		
  		// Kevin likes to eat pizza.
  		if (apg.contain("VBZ-xcomp>-VB-dobj>-NN")) {
  			patt = "VBZ-xcomp>-VB-dobj>-NN";
  			ArrayList<String> tokens = apg.extract(patt);

  			verb = tokens.get(0).split("#")[0];
  			output = tokens.get(2).split("#")[0];
  			
  			for(String s : keyWords){
  		        if(s.compareToIgnoreCase(verb) == 0) {
  		        	flag = true;
  		        }
  		    }
  		}
  		
  		if ( flag )
  			return output;
		return null;
  	}
}
