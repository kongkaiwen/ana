package events;

import java.util.HashMap;

import org.json.JSONObject;

public class AnaEvent {
private HashMap<String,String> attributes;
	
	public AnaEvent (HashMap<String,String> attributes) {
		this.attributes = attributes;
	}
	
	public String getId() {
		return this.attributes.get("id");
	}
	
	public String toString() {
		return this.attributes.get("name");
	}
	
	public String getName() {
		return this.attributes.get("name");
	}
	
	public String genQ() {
		for(String a: this.attributes.keySet()) {
			if ( !this.attributes.get(a).equals("") )
				continue;
			if ( a.equals("start") ) 
				return "When does " + this.attributes.get("name") + " start?";
			if ( a.equals("end")  ) 
				return "When will you return from " + this.attributes.get("name") + "?";
			if ( a.equals("location") ) 
				return "Where is the " + this.attributes.get("name") + "?";
			if ( a.equals("people") ) 
				return "Who will go to " + this.attributes.get("name") + "?";
		}
		return null;
	}
	
	public JSONObject toJSON() throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", this.attributes.get("name"));
		jsonObj.put("start", this.attributes.get("age"));
		jsonObj.put("end", this.attributes.get("education"));
		jsonObj.put("location", this.attributes.get("likes"));
		jsonObj.put("people", this.attributes.get("dislikes"));
		return jsonObj;
	}
}
