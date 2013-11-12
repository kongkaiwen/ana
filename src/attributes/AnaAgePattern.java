package attributes;

import java.util.ArrayList;

import entities.AnaEntity;


public class AnaAgePattern {
	private static String keyWords[] = {"years", "old", "age"};

	/*
	Kevin is 24 years old.
	My daughter is 24 years old. PRP$ NN is DUR NUMBER DUR .
	
	*/
	public static String match( String line, ArrayList<AnaEntity> entities, ArrayList<String> pos ) {
		boolean flag = false;
		boolean has_dur = false; 
		boolean has_num = false; 
		boolean has_per = false;
		boolean poss_nn = false; // possesive noun, my daughter
		
		AnaEntity per = null;
		AnaEntity num = null;
		AnaEntity dur = null;
		
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
		
		if (pos.toString().equals("PRP$ NN"))
			poss_nn = true;
		
		flag = has_per && has_num && has_dur;
		
		if (flag) {
			// find CD to get the age value
			int index = 0;
			for (String tag: pos) {
				if (tag.equals("CD"))
					break;
				index++;
			}
			
			return per.getId() + "#age#" + line.split(" ")[index];
		}
		
		return null;
	}
}
