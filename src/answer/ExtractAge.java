package answer;

import java.io.IOException;
import java.util.ArrayList;

import kb.KnowledgeBase;
import relations.Entity;

public class ExtractAge implements Extract {

	@Override
	public boolean execute( int oid, String object, String attr, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos, String val ) throws IOException {

		String num = "";
		String time = "";
		
		boolean found = false;
		boolean hasNum = false;
		
		for(String p: pos) {
			if (p.toLowerCase().equals("cd")) {
				num = tkns.get(pos.indexOf(p));
				hasNum = true;
			}
		}

		for(Entity e: ent) {
			if (e.getType().equals("TIME"))
				time = e.getName();
			
			if (e.getType().equals("NUM"))
				time = e.getName();
			
//			if (e.getType().equals("DUR"))
//				time = e.getName();
		}
		
		// update number
		if (!num.equals(""))
			kb.update(oid, object, attr, num);
		
		// if i found a time for the object -> update
		if (!time.equals(""))
			kb.update(oid, object, attr, time);
		
		if (!num.equals(""))
			found = true;
		
		if (!time.equals(""))
			found = true;
		
		// delete buffer
		kb.delBuffer();
		
		return found;
	}

}