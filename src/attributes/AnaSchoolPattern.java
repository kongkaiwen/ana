package attributes;

import java.util.ArrayList;
import java.util.HashMap;

import edu.stanford.nlp.semgraph.SemanticGraph;
import entities.AnaEntity;
import graph.AnaParseGraph;


public class AnaSchoolPattern {
	
	private static String keyWords[] = {"school", "university", "institute", "college"};
	private static String verbs[] = {"attend", "go to", "enrolled", "studies"};

	/*
	In order to match there must be a person and an organization.  There must also be a keyword.
	*/
	public static String match( String line, ArrayList<AnaEntity> entities, ArrayList<String> pos, 
			SemanticGraph dep, HashMap<String, Integer> ei  ) {
		boolean flag = false;
		boolean has_per = false;
		boolean has_org = false;
		boolean has_key = false;
		
		AnaEntity org = null;
		AnaEntity per = null;
		
		for (AnaEntity ae: entities) {

			if (ae.getType().equals("PER") ) {
				has_per = true;
				per = ae;
			}
			
			if (ae.getType().equals("ORG") ) {
				has_org = true;
				org = ae;
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
		
		// check dependency links for matches
		String match = checkDependencies(line, dep, ei);
		
		// any simple matches?
		flag = has_per && has_org && has_key;
		
		if (flag) {
			return per.getId() + "#education#" + org.getName();
		}
		
		if (match != null) {
			return per.getId() + "#education#" + match; 
		}
		
		return null;
	}
	
	/*
  	Kevin is a student at the University of Alberta. NNP-nsubj-NN-PREP-NNP ( PER, TITLE, ORG )
  	*/
	private static String checkDependencies( String line, SemanticGraph dependencies, HashMap<String, Integer> entityIndex ) {
  		
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
