package events;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.stanford.nlp.trees.semgraph.SemanticGraph;
import entities.AnaEntity;
import graph.AnaParseGraph;

public class AnaEventPattern {
	// time
	// nnp-nn-nsubj-vbz-prep_at-cd
	// Dance class is at 8pm.

	// date, pers
	// nn-nsubj-nn
	// Tomorrow is John's birthday party.

	// pers, date
	// nnp-pos-nn-nsubj-vbz-tmod-nn
	// Anna's graduation is next week.
	
	public static String keyWords[] = {"concert", "class", "party", "graduation", "game", "event", "potluck", "gathering",
		"klatch", "breakfast", "lunch", "dinner", "supper", "barbeque", "gala", "function", "seminar", "yoga", "lecture",
		"meeting", "date", "trip", "conference"	};
	
	// keyword + (DATE || TIME)
	public static String match1(String line, ArrayList<AnaEntity> entities, ArrayList<String> pos, SemanticGraph dep) {
		boolean has_pers = false;
		boolean has_date = false;
		boolean has_time = false;
		boolean has_keyw = false;
		
		String noun = null;
		AnaEntity per = null;
		AnaEntity dte = null;
		AnaEntity tme = null;
		
		for (AnaEntity ae: entities) {
			if (ae.getType().equals("PER") ) {
				has_pers = true;
				per = ae;
			}
			
			if (ae.getType().equals("DATE") ) {
				has_date = true;
				dte = ae;
			}
			
			if (ae.getType().equals("TIME") ) {
				has_time = true;
				tme = ae;
			}
		}
		
		for (String s: keyWords) {
			if (line.contains(s)) {
				has_keyw = true;
				noun = s;
			}
		}
		
		if ( dte != null && noun != null )
			return noun + "#" + dte.getName() + "#date";
		
		if ( tme != null && noun != null )
			return noun + "#" + tme.getName() + "#time";
		
		return null;
	}
	
	// tmod link + NN-VBZ-NN + (DATE || TIME)
	public static String match2(String line, ArrayList<AnaEntity> entities, ArrayList<String> pos, SemanticGraph dep) {
		String verb = "";
  		String patt = "";
  		String output = "";
  		boolean flag = false;
  		
  		AnaParseGraph apg = new AnaParseGraph( dep );
  		
  		// We are going to see a movie tonight.
  		if (apg.contain("NN-*>-VBZ-tmod>-NN")) {
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
  		
  		if ( flag )
  			return output;
		return null;
	}
	
	public static String match3(String line, ArrayList<AnaEntity> entities, ArrayList<String> pos, ArrayList<String> tkn,
			SemanticGraph dep) throws ParseException, IOException {
		
		ArrayList<String> new_entities = new ArrayList<String>();
		for(AnaEntity ae: entities)
			new_entities.add(ae.getName());
		
		double val = AnaEventModel.classify(line, pos, tkn, new_entities);
		HashMap<String, String> attr = new HashMap<String, String>();
		
		if (val > 0) {
			// found event
			AnaParseGraph apg = new AnaParseGraph( dep );
			ArrayList<String> event_name = apg.extract_nounphrase();
			if (event_name == null)
				return "N/A";
			
			String output = "";
			for(String tst: event_name) {
				if (tst.split("#")[1].equals("NN"))
					return tst.split("#")[0];
			}
			return event_name.toString();
		} else {
			return null;
		}
	}
}
