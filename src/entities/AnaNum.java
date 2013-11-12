package entities;

import java.util.HashMap;

import org.json.JSONObject;

public class AnaNum implements AnaEntity {
	private HashMap<String,String> attributes;
	
	public AnaNum (HashMap<String,String> attributes) {
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
		return "NUM";
	}
	
	public String genQ() {
		for(String a: this.attributes.keySet()) {
			if ( this.attributes.get(a) == "" ) {
				return "What is " + this.attributes.get("name") + "'s " + a + "?";
			}
		}
		return null;
	}
	
	public JSONObject toJSON() throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("type", "DUR");
		jsonObj.put("name", this.attributes.get("name"));
		return jsonObj;
	}
	
	public void updateAtr(String atr, String val) {
		this.attributes.put(atr, val);
	}
}
