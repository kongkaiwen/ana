package answer;

import java.io.IOException;
import java.util.ArrayList;

import kb.KnowledgeBase;
import relations.Entity;

public class ExtractDate implements Extract {

	@Override
	public boolean execute( int oid, String object, String attr, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos )throws IOException {
		String date = "";
		String time = "";
		boolean found = false;
		
		// get person from userResponse
		for(Entity e: ent) {
			System.out.println(e.getType());
			if (e.getType().equals("DATE"))
				date = e.getName();
			if (e.getType().equals("TIME"))
				time = e.getName();
		}
		
		if (!time.equals(""))
			kb.update(oid, object, attr, time);
		
		if (!date.equals(""))
			kb.update(oid, object, attr, date);
		
		if (!time.equals(""))
			found = true;
		
		if (!date.equals(""))
			found = true;
		
		// delete buffer
		kb.delBuffer();
		
		return found;
	}

}
