package answer;

import java.io.IOException;
import java.util.ArrayList;

import kb.KnowledgeBase;
import relations.Entity;
import tools.Helpers;

public class ExtractWho implements Extract {
	
	@Override
	public boolean execute( int oid, String object, String attr, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos  ) throws IOException {

		boolean flag = false;
		boolean has_wrd = false;
		boolean has_title = false; 
		boolean has_per = false;
		boolean poss_nn = false; // possesive noun, my daughter
		
		String words[] = {"with", "together", "take"};
		
		Entity per = null;
		
		for (Entity ae: ent) {
			if (ae.getType().equals("PER") ) {
				has_per = true;
				per = ae;
			}
		}
		
		if ( Helpers.hasFamilyTitles(tkns, pos) ) {
			has_title = true;
		}
		
		for (String tkn: tkns) {
			for (String wrd: words) {
				if (tkn.toLowerCase().equals(wrd)) {
					has_wrd = true;
				}
			}
		}
		
		if (Helpers.join(pos, " ").toLowerCase().contains("prp$ nn")) {
			poss_nn = true;
		}
		
		//System.out.println(has_wrd+":"+has_title+":"+has_per+":"+poss_nn);
		
		if ( (has_wrd && has_title) || (has_wrd && has_per) ) {
			String name =  has_title ? Helpers.getFamilyTitle(tkns, pos) : per.getName();
			kb.getEvent(oid).update("who", name);
			flag = true;
		} else {
			if (poss_nn && has_title) {
				kb.getEvent(oid).update("who", Helpers.getFamilyTitle(tkns, pos));
				flag = true;
			}
		}
		
		return flag;
	}
}
