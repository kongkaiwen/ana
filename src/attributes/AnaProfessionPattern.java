package attributes;

import java.util.ArrayList;
import java.util.HashMap;

import tools.Helpers;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.semgraph.*;

import entities.AnaEntity;
import graph.AnaParseGraph;

public class AnaProfessionPattern {
	
	private static String keyWords[] = {"works", "work", "profession", "job", "occupation", "employed"};

	/*
	In order to match there must be a person and an organization.  There must also be a keyword.
	*/
	public static String match( String line, ArrayList<String> tkns, ArrayList<AnaEntity> ent, ArrayList<String> pos, 
			SemanticGraph dep, HashMap<String, Integer> ei ) {
		boolean flag = false;
		boolean has_per = false;
		boolean has_org = false;
		boolean has_key = false;
		
		AnaEntity org = null;
		AnaEntity per = null;
		
		for (AnaEntity ae: ent) {

			if (ae.getType().equals("PER") ) {
				has_per = true;
				per = ae;
			}
			
			if (ae.getType().equals("ORG") ) {
				has_org = true;
				org = ae;
			}
		}
		
		// jane works at safeway
		if (has_per && Helpers.join(pos, " ").equals("nn vbz in nn .")) {
			return per.getId() + "#profession#" + tkns.get(3);
		}
		
		for (String s: keyWords) {
			if (line.contains(s)) {
				has_key = true;
			}
		}
		
		// check dependency links for matches
		String match = checkDependencies(line, dep, ei);
		
		// any simple matches?
		flag = has_per && has_org && has_key;
		
		if (flag) {
			return per.getId() + "#profession#" + org.getName();
		}
		
		if (match != null) {
			return per.getId() + "#profession#" + match; 
		}
		
		// jane works at olive garden
		
		return null;
	}
	
  	/*
  	Kevin is a software developer at Google. NNP-nsubj-NN-PREP-NNP ( PER, TITLE, ORG )
  	*/
	private static String checkDependencies( String line, SemanticGraph dependencies, HashMap<String, Integer> entityIndex ) {
  		
  		String patt = "";
  		String output = "";
  		String position = "";
  		String institute = "";
  		boolean flag = false;
  		
  		AnaParseGraph apg = new AnaParseGraph( dependencies );
  		
  		// Kevin is a software developer at Google.
  		if (apg.contain("NN-prep_at>-NNP")) {
  			patt = "NN-prep_at>-NNP";
  			ArrayList<String> tokens = apg.extract(patt);
  			position = tokens.get(0).split("#")[0];
  			institute = tokens.get(1).split("#")[0];
  			
  			output = institute;
  			if (!position.equals("student"))
  				flag = true;
  		}
  		
  		// I am a research assistant.
  		if (apg.contain("NN-nsubj>-PRP")) {
  			patt = "NN-nsubj>-PRP";
  			ArrayList<String> tokens = apg.extract(patt);
  			institute = tokens.get(0).split("#")[0];
  			position = tokens.get(1).split("#")[0];
  			
  			output = institute;
  			if (!institute.equals("student"))
  				flag = true;
  		}
  		
  		if ( flag )
  			return output;
		return null;
  	}
}
