package kb;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import answer.Extract;
import answer.ExtractAge;
import answer.ExtractDate;
import answer.ExtractDegree;
import answer.ExtractLikes;
import answer.ExtractName;
import answer.ExtractPlace;
import answer.ExtractPosition;
import answer.ExtractTime;

import tools.Helpers;

public class Person implements Entity {

	private int id;
	private HashMap<String, String> attributes;
	
	public Person ( int id ) {
		this.id = id;
		
		attributes = new HashMap<String, String>();
		attributes.put("sex", "");
		attributes.put("age", "");
		attributes.put("name", "");
		attributes.put("education_institute", "");
		attributes.put("education_degree", "");
		attributes.put("date_of_birth", "");
		attributes.put("date_of_death", "");
		attributes.put("profession_institute", "");
		attributes.put("profession_position", "");
		attributes.put("residence", "");
		attributes.put("ethnicity", "");
		attributes.put("religion", "");
		attributes.put("height", "");
		attributes.put("weight", "");
		attributes.put("languages", "");
		attributes.put("born_in", "");
		attributes.put("likes", "");
		attributes.put("dislikes", "");
	}
	
	public String get( String attr ) {
		return attributes.get(attr);
	}
	
	public void update( String attr, String value ) throws IOException {
		if (attr.equals("likes") || attr.equals("dislikes")) {
			if (attributes.get(attr).equals("")) {
				attributes.put(attr, value);
			} else {
				String list = attributes.get(attr);
				list = list + ", " + value;
				attributes.put(attr, list);
			}
		} else if (attr.equals("name")) {
			attributes.put(attr, value);
			attributes.put("sex", Helpers.predictGender(value));
		} else {
			attributes.put(attr, value);
		}
	}
	
	public void clear( String attr ) {
		if (attr.equals("likes")) {
			String likes = attributes.get("likes");
			if (likes.contains(", temp")) {
				likes = likes.replace(", temp", "");
			} else if (likes.contains("temp")) {
				likes = likes.replace("temp", "");
			}
			attributes.put(attr, likes);
		} else {
			attributes.put(attr, "");
		}
	}
	
	public HashMap<String, String> getAttr() {
		return attributes;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean equals( Person person ) {
		return false;
	}
	
	public JSONObject toJSON() throws JSONException {
		// { data: { id: "n1" } }
		
		JSONObject obj = new JSONObject();
		JSONObject jso = new JSONObject();
		
		jso.put("id", String.valueOf(getId()));
		
		for(String key: attributes.keySet()) {
			if (!attributes.get(key).equals("")) {
				jso.put(key, attributes.get(key));
			}
		}
		
		obj.put("data", jso);
		
		return obj;
	}
	
	public String getEmptyAttr() {
		/*
			1. Name
			2. Age
			3. Education_insititute
			4. Profession_institute
		*/
		
		if (attributes.get("name").equals("")) {
			return "name";
		} else if (attributes.get("age").equals("")) {
			return "age";
		} else if (attributes.get("education_institute").equals("")) {
			return "education_institute";
		} else if (attributes.get("profession_institute").equals("")) {
			return "profession_institute";
		} else if (attributes.get("education_degree").equals("")) {
			return "education_degree";
		} else if (attributes.get("profession_position").equals("")) {
			return "profession_position";
		} else if (attributes.get("residence").equals("")) {
			return "residence";
		} else if (attributes.get("likes").equals("")) {
			return "likes";
		}
		
		return null;
	}
	
	public Extract getCallback( String attr ) {
		
		if ( attr.equals("age") ) {
			return new ExtractAge();
		} else if ( attr.equals("name") ) {
			return new ExtractName();
		} else if ( attr.equals("education_institute") ) {
			return new ExtractPlace();
		} else if ( attr.equals("education_degree") ) {
			return new ExtractDegree();
		} else if ( attr.equals("date_of_birth") ) {
			return new ExtractDate();
		} else if ( attr.equals("date_of_death") ) {
			
		} else if ( attr.equals("profession_institute") ) {
			return new ExtractPlace();
		} else if ( attr.equals("profession_position") ) {
			return new ExtractPosition();
		} else if ( attr.equals("residence") ) {
			return new ExtractPlace();
		} else if ( attr.equals("ethnicity") ) {
			
		} else if ( attr.equals("religion") ) {
			
		} else if ( attr.equals("height") ) {
			
		} else if ( attr.equals("weight") ) {
			
		} else if ( attr.equals("languages") ) {
			
		} else if ( attr.equals("born_in") ) {
			
		} else if ( attr.equals("likes") ) {
			return new ExtractLikes();
		}
		
		return null;
	}
	
	public String formulateResponse( String attr, String val ) {
		
		if ( attr.equals("age") ) {
			return get("name") + " is " + val + "years old.";
		} else if ( attr.equals("name") ) {
			return "His name is " +val;
		} else if ( attr.equals("education_institute") ) {
			return get("name") + " went to " + get("education_institute");
		} else if ( attr.equals("education_degree") ) {
			return get("name") + " has a degree in " + get("education_degree");
		} else if ( attr.equals("date_of_birth") ) {
			return get("name") + " was born on " + get("date_of_birth");
		} else if ( attr.equals("date_of_death") ) {
			return get("name") + " died on " + get("date_of_death");
		} else if ( attr.equals("profession_institute") ) {
			return get("name") + " works at " + get("profession_institute");
		} else if ( attr.equals("profession_position") ) {
			return get("name") + " is a " + get("profession_position");
		} else if ( attr.equals("residence") ) {
			return get("residence");
		} else if ( attr.equals("ethnicity") ) {
			return get("ethnicity");
		} else if ( attr.equals("religion") ) {
			return get("religion");
		} else if ( attr.equals("height") ) {
			return get("height");
		} else if ( attr.equals("weight") ) {
			return get("weight");
		} else if ( attr.equals("languages") ) {
			return get("languages");
		} else if ( attr.equals("born_in") ) {
			return get("born_in");
		} else if ( attr.equals("likes") ) {
			return get("name") + " likes; " + get("likes") + ".";
		} else if ( attr.equals("dislikes") ) {
			return get("name") + " doesn't like; " + get("dislikes");
		}
		
		return get(attr);
	}
	
	public String frameResponse( String attr, String val ) {
		
		if ( attr.equals("age") ) {
			return get("name") + " is " + val + "years old.";
		} else if ( attr.equals("name") ) {
			return "His name is " +val+"?";
		} else if ( attr.equals("education_institute") ) {
			return "So.. " + get("name") + " went to " + get("education_institute") + "?";	
		} else if ( attr.equals("education_degree") ) {
			return "So.. " + get("name") + " has a degree in " + get("education_degree") + "?";
		} else if ( attr.equals("date_of_birth") ) {
			return "So.. " + get("name") + " was born on " + get("date_of_birth") + "?";
		} else if ( attr.equals("date_of_death") ) {
			return get("name") + " died on " + get("date_of_death");
		} else if ( attr.equals("profession_institute") ) {
			return "Oh ok, " + get("name") + " works at " + get("profession_institute") + "?";	
		} else if ( attr.equals("profession_position") ) {
			return "I see, " + get("name") + " is a " + get("profession_position") + ".";
		} else if ( attr.equals("residence") ) {
			return get("residence");
		} else if ( attr.equals("ethnicity") ) {
			return get("ethnicity");
		} else if ( attr.equals("religion") ) {
			return get("religion");
		} else if ( attr.equals("height") ) {
			return get("height");
		} else if ( attr.equals("weight") ) {
			return get("weight");
		} else if ( attr.equals("languages") ) {
			return get("languages");
		} else if ( attr.equals("born_in") ) {
			return get("born_in");
		} else if ( attr.equals("likes") ) {
			return "I see "+ get("name") + " likes " + val + ".";
		} else if ( attr.equals("dislikes") ) {
			return get("name") + " doesn't like; " + get("dislikes");
		}
		
		return get(attr);
	}
	
}
