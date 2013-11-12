		package entities;

import java.util.HashMap;

import org.json.JSONObject;

public class AnaDate implements AnaEntity {
	private HashMap<String,String> attributes;
	
	public AnaDate (HashMap<String,String> attributes) {
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
		return "DATE";
	}
	
	public JSONObject toJSON() throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("type", "DATE");
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
