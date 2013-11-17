package events;

import java.util.ArrayList;

import entities.AnaEntity;

public class EventWhere {

	/*
		I went for lunch at Olive Garden.
		We will go to Tim Hortons later.
		We went to Harvey's for lunch.
		We will have a potluck at our place next week.
		I will be going to Phil's home for Christmas.
	*/
	public static String match( String line, ArrayList<String> tkns, ArrayList<AnaEntity> ent, ArrayList<String> pos ) {
		
		boolean flag = false;
		boolean has_wrd = false;
		boolean has_ttl = false; 
		boolean has_per = false;
		boolean poss_nn = false;
		
		String prep[] = {"in", "at"};
		String verb[] = {"went", "go"};
		
		String nnp = "";
		for(String prt: pos) {
			int index = pos.indexOf(prt);
			if (prt.toLowerCase().equals("nnp")) {
				if (index+1 < pos.size()) {
					if (pos.get(index+1).toLowerCase().equals("nnp")) {
						return pos.get(index) + " " + pos.get(index+1);
					}
					nnp = prt;
				}
			}
		}
		
		return nnp;
	}
}
