package answer;

import java.io.IOException;
import java.util.ArrayList;

import kb.KnowledgeBase;
import kb.Person;

import relations.Entity;

public class ExtractPlace implements Extract {

	@Override
	public boolean execute( int oid, String object, String attr, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos  ) throws IOException {

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
			kb.update(oid, object, attr, nnp);
		
		if (!name.equals(""))
			kb.update(oid, object, attr, name);
		
		if (!nnp.equals(""))
			found = true;
		
		if (!name.equals(""))
			found = true;
		
		// delete buffer
		kb.delBuffer();
		
		return found;
	}

}
