package events;

import java.util.ArrayList;

import relations.Entity;

import kb.Event;

import entities.AnaEntity;

public class EventWhen {

	public static String match( Event event, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos, String attr ) {
		
		boolean flag = false;
		boolean has_dte = false;
		boolean has_tme = false;
		boolean has_num = false;
		
		String cd = null;
		Entity dte = null;
		Entity tme = null;
		
		String tense = event.get("tense");
		
		for (Entity ae: ent) {

			if (ae.getType().equals("DATE") ) {
				has_dte = true;
				dte = ae;
			}
			
			if (ae.getType().equals("TIME") ) {
				has_tme = true;
				tme = ae;
			}
		}
		
		for(String prt: pos) {
			if (prt.toLowerCase().equals("cd")) {
				has_num = true;
				cd = tkns.get(pos.indexOf(prt));
				
//				if (cd.contains("am") || cd.contains("pm")) {
//					cd = null;
//				}
			}
		}
		
		if (has_num) {
			event.update(attr, cd);
		}
		
		if (has_tme) {
			event.update(attr, tme.getName());
		}
		
		if (has_dte) {
			event.update(attr, dte.getName());
		}
		
		return null;
	}
}
