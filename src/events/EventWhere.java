package events;

import java.util.ArrayList;

import kb.Event;

import relations.Entity;

import entities.AnaEntity;

public class EventWhere {

	/*
		I went for lunch at Olive Garden.
		We will go to Tim Hortons later.
		We went to Harvey's for lunch.
		We will have a potluck at our place next week.
		I will be going to Phil's home for Christmas.
	*/
	public static String match( Event event, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos ) {
		
		String nnp = "";
		String name = "";
		boolean found = false;
		
		// get organization from userResponse
		for(Entity e: ent) {
			if (e.getType().equals("ORG"))
				name = e.getName();
		}
		
		// if no org, extract the NNP
		if (name.equals("")) {
			for(String prt: pos) {
				int index = pos.indexOf(prt);
				if (prt.toLowerCase().equals("nnp")) {
					if (index+1 < pos.size()) {
						if (pos.get(index+1).toLowerCase().equals("nnp")) {
							nnp = tkns.get(index) + " " + tkns.get(index+1);
							break;
						}
						nnp = tkns.get(index);
					}
				}
			}
		}
		
		if (!nnp.equals(""))
			event.update("where", nnp);
		
		if (!name.equals(""))
			event.update("where", name);
		
		if (!nnp.equals(""))
			found = true;
		
		if (!name.equals(""))
			found = true;
		
		return null;
	}
}
