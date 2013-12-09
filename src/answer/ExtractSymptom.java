package answer;

import java.io.IOException;
import java.util.ArrayList;

import kb.KnowledgeBase;
import relations.Entity;
import tools.Helpers;

public class ExtractSymptom implements Extract {

	@Override
	public boolean execute( int oid, String object, String attr, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos, String val ) throws IOException {
		
		boolean found = false;
		ArrayList<String> symptoms = Helpers.loadSymptoms();
		ArrayList<String> problems = new ArrayList<String>();
		
		String raw = Helpers.join(tkns, " ");
		for (String sym: symptoms) {
			if (raw.toLowerCase().contains(sym)) {
				found = true;
				problems.add(sym);
			}
		}
		
		// update 
		kb.update(oid, object, attr, Helpers.join(problems, ", "));
		
		// delete buffer
		kb.delBuffer();
		
		return found;
	}

}
