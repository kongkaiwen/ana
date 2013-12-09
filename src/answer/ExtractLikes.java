package answer;

import edu.stanford.nlp.semgraph.SemanticGraph;
import entities.AnaEntity;
import graph.AnaParseGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import kb.KnowledgeBase;
import relations.Entity;
import tools.Helpers;

public class ExtractLikes implements Extract {
	
	private static String keyWords[] = {"likes", "favorite", "loves", "enjoys"};

	@Override
	public boolean execute( int oid, String object, String attr, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos, String val  ) throws IOException {

		boolean flag = false;
		boolean has_per = false;
		boolean has_key = false;

		Entity per = null;
		
		for (Entity ae: ent) {
			if (ae.getType().equals("PER") ) {
				has_per = true;
				per = ae;
			}
		}
		
		for (String s: keyWords) {
			if (tkns.contains(s)) {
				has_key = true;
			}
		}
		
		String ans = "";
		// start from last POS, return first nn
		for (int i=pos.size()-1;i>=0;i--) {
			String tag = pos.get(i);
			if (tag.toLowerCase().contains("nn")) {
				ans = tkns.get(i);
				flag = true;
				break;
			}
		}
		
		// update value;
		if (flag)
			kb.update(oid, object, attr, ans);
		
		// delete buffer
		kb.delBuffer();
		
		return flag;
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
