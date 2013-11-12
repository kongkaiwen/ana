package kb;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import answer.Extract;
import answer.ExtractAge;
import answer.ExtractDate;
import answer.ExtractName;
import answer.ExtractOrganization;
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
	}
	
	public String get( String attr ) {
		return attributes.get(attr);
	}
	
	public void update( String attr, String value ) throws IOException {
		if (attr.equals("likes")) {
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
		//jso.put("sex", String.valueOf(attributes.get("sex")));
		//jso.put("age", String.valueOf(attributes.get("age")));
		//jso.put("name", String.valueOf(attributes.get("name")));
		
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
		}
		
		return null;
	}
	
	public Extract getCallback( String attr ) {
		
		if ( attr.equals("age") ) {
			return new ExtractAge();
		} else if ( attr.equals("name") ) {
			return new ExtractName();
		} else if ( attr.equals("education_institute") ) {
			return new ExtractOrganization();
		} else if ( attr.equals("education_degree") ) {
			
		} else if ( attr.equals("date_of_birth") ) {
			return new ExtractDate();
		} else if ( attr.equals("date_of_death") ) {
			
		} else if ( attr.equals("profession_institute") ) {
			return new ExtractOrganization();
		} else if ( attr.equals("profession_position") ) {
			
		} else if ( attr.equals("residence") ) {
			
		} else if ( attr.equals("ethnicity") ) {
			
		} else if ( attr.equals("religion") ) {
			
		} else if ( attr.equals("height") ) {
			
		} else if ( attr.equals("weight") ) {
			
		} else if ( attr.equals("languages") ) {
			
		} else if ( attr.equals("born_in") ) {
			
		} else if ( attr.equals("likes") ) {
			
		}
		
		return null;
	}
	
}
