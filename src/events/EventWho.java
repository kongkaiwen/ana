package events;

import java.util.ArrayList;

import kb.Event;

import relations.Entity;
import tools.Helpers;

import entities.AnaEntity;

public class EventWho {

	/*
	I went to lunch with my son.
	I went to lunch with Sarah.
	I need to take my girlfriend on a picnic soon.
	*/
	public static String match( Event event, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos ) {
		
		boolean flag = false;
		boolean has_wrd = false;
		boolean has_title = false; 
		boolean has_per = false;
		boolean poss_nn = false; // possesive noun, my daughter
		
		String words[] = {"with", "together", "take"};
		
		Entity per = null;
		
		for (Entity ae: ent) {
			if (ae.getType().equals("PER") ) {
				has_per = true;
				per = ae;
			}
		}
		
		if ( Helpers.hasFamilyTitles(tkns, pos) ) {
			has_title = true;
		}
		
//		for (String tkn: tkns) {
//			for (String wrd: words) {
//				if (tkn.toLowerCase().equals(wrd)) {
//					has_wrd = true;
//				}
//			}
//		}
		
		if (Helpers.join(pos, " ").toLowerCase().contains("prp$ nn")) {
			poss_nn = true;
		}
		
		if ( (has_wrd && has_title) || ( has_per) ) {
			String name =  has_title ? Helpers.getFamilyTitle(tkns, pos) : per.getName();
			event.update("who", name);
		} else {
			if (poss_nn && has_title) {
				event.update("who", Helpers.getFamilyTitle(tkns, pos));
			}
		}
		
		// id#age#12
		return null;
	}
}
