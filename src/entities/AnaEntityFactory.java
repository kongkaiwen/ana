package entities;

import java.util.HashMap;

public class AnaEntityFactory {
	
    public static AnaEntity createEntity (String type, HashMap<String,String> attributes) {
	   
	    if (type.equalsIgnoreCase ("PER")) 
	        return new AnaPer(attributes);
	    if (type.equalsIgnoreCase ("ORG")) 
	        return new AnaOrg(attributes);
	    if (type.equalsIgnoreCase ("LOC")) 
	    	return new AnaPer(attributes);
	    if (type.equalsIgnoreCase ("NUM")) 
	    	return new AnaNum(attributes);
	    if (type.equalsIgnoreCase ("DUR")) 
	    	return new AnaDur(attributes);
	    if (type.equalsIgnoreCase ("TIME")) 
	    	return new AnaTime(attributes);
	    if (type.equalsIgnoreCase ("DATE")) 
	    	return new AnaDate(attributes);
	    throw new IllegalArgumentException("No such type!");
    }
}
