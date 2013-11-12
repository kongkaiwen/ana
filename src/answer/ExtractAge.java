package answer;

import java.io.IOException;
import java.util.ArrayList;

import kb.KnowledgeBase;
import relations.Entity;

public class ExtractAge implements Extract {

	@Override
	public boolean execute( int oid, String object, String attr, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos )throws IOException {

		String time = "";
		boolean found = false;
		boolean hasNum = false;
		
		for(String p: pos) {
			if (p.equals("cd")) {
				hasNum = true;
			}
		}
		
		for(Entity e: ent) {
			if (e.getType().equals("TIME"))
				time = e.getName();
			
			if (e.getType().equals("NUM"))
				time = e.getName();
		}
		
		// if i found a time for the object -> update
		if (!time.equals(""))
			kb.update(oid, object, attr, time);
		
		if (!time.equals(""))
			found = true;
		
		// delete buffer
		kb.delBuffer();
		
		return found;
	}

}