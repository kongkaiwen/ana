package attributes;

import java.io.IOException;
import java.util.ArrayList;

import kb.KnowledgeBase;
import kb.Person;
import edu.stanford.nlp.semgraph.SemanticGraph;
import entities.AnaEntity;

public class AgePattern {
	
	private static String keyWords[] = {"years", "old", "age", "turning"};

	/*
	Kevin is 24 years old.
	My daughter is 24 years old. PRP$ NN is DUR NUMBER DUR .
	Jacob is turning 24.
	My grandson is 12.
	*/
	public static boolean match( ArrayList<String> tkns, ArrayList<String> pos, ArrayList<String> entities, ArrayList<Person> people, SemanticGraph dep, KnowledgeBase kb, boolean add ) throws IOException {
		boolean flag = false;
		boolean has_dur = false; 
		boolean has_num = false; 
		boolean has_per = false;
		boolean poss_nn = false; // possesive noun, my daughter
		boolean poss_cd = false;
		
		Person per = null;
		String num = null;
		String dur = null;
		String cd = null;
		
		if (people.size() > 0) {
			per = people.get(0);
			has_per = true;
		}
		
		for (String ent: entities) {
			if (ent.equals("NUMBER") ) {
				has_num = true;
				num = tkns.get(entities.indexOf(ent));
			}
		}
		
		for(String prt: pos) {
			if (prt.toLowerCase().equals("cd")) {
				has_num = true;
				cd = tkns.get(pos.indexOf(prt));
				
				if (cd.contains("am") || cd.contains("pm")) {
					cd = null;
				}
			}
		}
		
		if (pos.toString().equals("PRP$ NN"))
			poss_nn = true;
		
		if (pos.toString().equals("VBZ CD ."))
			poss_cd = true;
		
		flag = has_per && (cd != null);
		
		// My daughter is 12.
		if (poss_nn && poss_cd)
			flag = true;
		
		if ( flag ) {
			kb.update(per.getId(), "person", "age", cd);
			flag = true;
		}
		
		return flag;
	}
}
