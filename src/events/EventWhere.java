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
		
		if (!name.equals(""))
			event.update("where", name);
		
		if (!name.equals(""))
			found = true;
		
		if (found)
			System.out.println("EventWhere");
		
		return null;
	}
}
