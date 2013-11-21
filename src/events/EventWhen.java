package events;

import java.util.ArrayList;

import relations.Entity;

import kb.Event;

import entities.AnaEntity;

public class EventWhen {

	public static String match( Event event, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos ) {
		
		boolean flag = false;
		boolean has_dte = false;
		boolean has_tme = false;
		
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
		
		if (has_tme) {
			event.update("start", tme.getName());
		}
		
		if (has_dte) {
			event.update("start", dte.getName());
		}
		
		return null;
	}
}
