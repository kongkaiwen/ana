package answer;

import java.io.IOException;
import java.util.ArrayList;

import kb.KnowledgeBase;
import relations.Entity;

public class ExtractDate implements Extract {

	@Override
	public boolean execute( int oid, String object, String attr, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos, String val )throws IOException {

		String num = "";
		String date = "";
		String time = "";
		boolean found = false;
		
		// get person from userResponse
		for(Entity e: ent) {
			if (e.getType().equals("DATE"))
				date = e.getName();
			if (e.getType().equals("TIME"))
				time = e.getName();
		}
		
		if (time.equals("") && date.equals("")) {
			// check for cd
			
			for(String p: pos) {
				if (p.toLowerCase().equals("cd")) {
					num = tkns.get(pos.indexOf(p));
				}
			}
			
		}
		
		if (!num.equals(""))
			kb.update(oid, object, attr, num);
		
		if (!time.equals(""))
			kb.update(oid, object, attr, time);
		
		if (!date.equals(""))
			kb.update(oid, object, attr, date);
		
		if (!num.equals("")) 
			found = true;
		
		if (!time.equals(""))
			found = true;
		
		if (!date.equals(""))
			found = true;
		
		// delete buffer
		kb.delBuffer();
		
		return found;
	}

}
