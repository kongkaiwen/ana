package answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import kb.KnowledgeBase;
import relations.Entity;

public class ExtractRelation implements Extract {

	public static String familyTitles[] = {"daughter", "son", "father", "mother", "grandfather", "grandmother", "neice", "nephew", "cousin", "uncle", "aunt", "wife", "husband", "grandson", "granddaughter", "friend", "brother", "sister"};
	
	@Override
	public boolean execute( int oid, String object, String attr, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos  ) throws IOException {

		String name = "";
		String title = "";
		boolean found = false;
		
		name = kb.getPerson(oid).get("name");
		
		// find relationships
		for(String tkn: tkns) {
			for(String tle: familyTitles) {
				if (tkn.toLowerCase().equals(tle)) {
					title = tkn;
				}
			}
		}
		
		if (!name.equals("") && !title.equals("")) {
			kb.addRelation(title, kb.getSpeaker().getId(), oid);
		}
		
		if (!name.equals("") && !title.equals("")) {
			found = true;
		}
		
		// delete buffer
		kb.delBuffer();
		
		return found;
	}
}
