package answer;

import java.io.IOException;
import java.util.ArrayList;

import kb.KnowledgeBase;
import relations.Entity;

public class ExtractBinary implements Extract {

	@Override
	public boolean execute( int oid, String object, String attr, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos )throws IOException {

		boolean found = false;
		boolean binary = false;
		
		// get person from userResponse
		for(String tkn: tkns) {
			if (tkn.toLowerCase().matches("(yes|yea)")) {
				found = true;
				binary = true;
			}
			
			if (tkn.toLowerCase().matches("(no|nope)")) {
				found = true;
				binary = false;
			}
		}

		if (found)
			kb.update(oid, object, attr, String.valueOf(binary));
		
		// delete buffer
		kb.delBuffer();
		
		return found;
	}

}
