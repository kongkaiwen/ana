package events;

import java.util.ArrayList;

import tools.Helpers;

import entities.AnaEntity;

public class EventWho {

	/*
	I went to lunch with my son.
	I went to lunch with Sarah.
	I need to take my girlfriend on a picnic soon.
	*/
	public static String match( String line, ArrayList<String> tkns, ArrayList<AnaEntity> ent, ArrayList<String> pos ) {
		
		boolean flag = false;
		boolean has_wrd = false;
		boolean has_ttl = false; 
		boolean has_per = false;
		boolean poss_nn = false; // possesive noun, my daughter
		
		String words[] = {"with", "together", "take"};
		
		AnaEntity per = null;
		
		for (AnaEntity ae: ent) {
			if (ae.getType().equals("PER") ) {
				has_per = true;
				per = ae;
			}
		}
		
		if ( Helpers.hasFamilyTitles(tkns, pos) ) {
			has_ttl = true;
		}
		
		for (String tkn: tkns) {
			for (String wrd: words) {
				if (tkn.toLowerCase().equals(wrd)) {
					has_wrd = true;
				}
			}
		}
		
		if (pos.toString().equals("PRP$ NN"))
			poss_nn = true;
		
		if ( (has_wrd && has_ttl) || (has_wrd && has_per) ) {
			return has_ttl ? Helpers.getFamilyTitle(tkns, pos) : per.getName();
		}
		
		// id#age#12
		return null;
	}
}
