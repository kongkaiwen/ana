package entities;

import java.util.HashMap;

import org.json.JSONObject;

public class AnaTime implements AnaEntity {
	private HashMap<String,String> attributes;
	
	public AnaTime (HashMap<String,String> attributes) {
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
		return "TIME";
	}
	
	public JSONObject toJSON() throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("type", "TIME");
		jsonObj.put("name", this.attributes.get("name"));
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
