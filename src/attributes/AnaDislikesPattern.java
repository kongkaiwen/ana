package attributes;

import java.util.ArrayList;
import java.util.HashMap;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.semgraph.*;
import entities.AnaEntity;
import graph.AnaEdge;
import graph.AnaNode;
import graph.AnaParseGraph;

public class AnaDislikesPattern {
	
	private static String keyWords[] = {"hates", "dislikes", "loathes", "enjoys"};

	/*
	In order to match there must be a person and an organization.  There must also be a keyword.
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
		
		if (match != null) {
			return per.getId() + "#dislikes#" + match; 
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
