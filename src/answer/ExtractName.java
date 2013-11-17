package answer;

import java.io.IOException;
import java.util.ArrayList;

import kb.KnowledgeBase;
import kb.Person;

import relations.Entity;

public class ExtractName implements Extract {

	@Override
	public boolean execute( int oid, String object, String attr, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos  ) throws IOException {

		String name = "";
		boolean found = false;
		
		// get person from userResponse
		for(Entity e: ent) {
			if (e.getType().equals("PER"))
				name = e.getName();
		}
		
		if (!name.equals("")) {
			
			kb.update(oid, object, attr, name);
			if (kb.getSpeaker() == null) {
				kb.setSpeaker(oid);
			}
		}
		
		if (!name.equals(""))
			found = true;
		
		// delete buffer
		kb.delBuffer();
		
		return found;
	}

}
