package events;

import java.util.ArrayList;

import kb.Event;

import entities.AnaEntity;

public class EventWhen {

	public static String match( Event event, ArrayList<String> tkns, ArrayList<AnaEntity> ent, ArrayList<String> pos ) {
		
		boolean flag = false;
		boolean has_dte = false;
		boolean has_tme = false;
		
		AnaEntity dte = null;
		AnaEntity tme = null;
		
		String tense = event.get("tense");
		
		for (AnaEntity ae: ent) {

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
