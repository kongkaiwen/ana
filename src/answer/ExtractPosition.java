package answer;

import java.io.IOException;
import java.util.ArrayList;

import kb.KnowledgeBase;
import relations.Entity;
import tools.Helpers;

public class ExtractPosition implements Extract {

	public static String positions[] = {"doctor", "engineer", "analyst", "student", "scientist"};
	
	@Override
	public boolean execute( int oid, String object, String attr, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos, String val  ) throws IOException {

		String nnp = "";
		String name = "";
		boolean found = false;
		String line = Helpers.join(tkns, " ");
		
		for (String d: positions) {
			if (line.contains(d))
				name = d;
		}
		
		if (line.toLowerCase().contains("not") || line.toLowerCase().contains("doesn 't") || line.toLowerCase().contains("none")) {
			kb.update(oid, object, attr, "N/A");
			return true;
		}
		
		// if no degree, extract the NNP
		if (name.equals("")) {
			for(String prt: pos) {
				int index = pos.indexOf(prt);
				if (prt.toLowerCase().contains("nn")) {
					if (index+1 < pos.size()) {
						if (pos.get(index+1).toLowerCase().contains("nn")) {
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
		
		if (!nnp.equals(""))
			found = true;
		
		if (!name.equals(""))
			kb.update(oid, object, attr, nnp);
		
		if (!name.equals(""))
			found = true;
		
		// delete buffer
		kb.delBuffer();
		
		return found;
	}
	
}
