package attributes;

import java.util.ArrayList;

import kb.Person;

import entities.AnaEntity;


public class AnaAgePattern {
	private static String keyWords[] = {"years", "old", "age", "turning"};

	/*
	Kevin is 24 years old.
	My daughter is 24 years old. PRP$ NN is DUR NUMBER DUR .
	Jacob is turning 24.
	My grandson is 12.
	*/
	public static String match( String line, ArrayList<Person> people, ArrayList<AnaEntity> entities, ArrayList<String> pos, ArrayList<String> tkns ) {
		boolean flag = false;
		boolean has_dur = false; 
		boolean has_num = false; 
		boolean has_per = false;
		boolean poss_nn = false; // possesive noun, my daughter
		boolean poss_cd = false;
		
		AnaEntity per = null;
		AnaEntity num = null;
		AnaEntity dur = null;
		String cd = null;
		
		for (AnaEntity ae: entities) {
			//System.out.println("type: "+ ae.getType());
			if (ae.getType().equals("PER") ) {
				has_per = true;
				per = ae;
			}
			
			if (ae.getType().equals("NUM") ) {
				has_num = true;
				num = ae;
			}
			
			if (ae.getType().equals("DUR") ) {
				has_dur = true;
				dur = ae;
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
		
		//flag = has_per && has_num && has_dur;
		flag = has_per && (cd != null);
		
		// My daughter is 12.
		if (poss_nn && poss_cd)
			flag = true;
		
		// She is 24 years old.
		// He is turning 5.
		if (flag) {
			// find CD to get the age value
			int index = 0;
			for (String tag: pos) {
				if (tag.equals("CD"))
					break;
				index++;
			}
			
			//return per.getId() + "#age#" + line.split(" ")[index];
			return per.getId() + "#age#" + cd;
		}
		
		// I or My pronoun
		boolean poss_prn;
		if (pos.toString().equals("PRP VBP"))
			poss_nn = true;
		
		if (line.startsWith("I") && has_per == false) {
			
		}
		
		
		return null;
	}
}
