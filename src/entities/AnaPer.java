package entities;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class AnaPer implements AnaEntity {
	
	private HashMap<String,String> attributes;
	
	public AnaPer (HashMap<String,String> attributes) {
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
	
	public String getType() {
		return "PER";
	}
	
	public String genQ() {
		for(String a: this.attributes.keySet()) {
			if ( this.attributes.get(a).equals("") ) {
				return "What is " + this.attributes.get("name") + "'s " + a + "?";
			}
		}
		return null;
	}
	
	public JSONObject toJSON() throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("type", "PER");
		jsonObj.put("name", this.attributes.get("name"));
		jsonObj.put("age", this.attributes.get("age"));
		jsonObj.put("education", this.attributes.get("education"));
		jsonObj.put("likes", this.attributes.get("likes"));
		jsonObj.put("dislikes", this.attributes.get("dislikes"));
		return jsonObj;
	}
	
	public void updateAtr(String atr, String val) {
		String value = val;
		if (atr.equals("likes")) {
			String likes = this.attributes.get("likes");
			value = likes + "," + val;
		}
		this.attributes.put(atr, value);
	}
}
