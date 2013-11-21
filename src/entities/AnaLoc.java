package entities;

import java.util.HashMap;

import org.json.JSONObject;

public class AnaLoc  implements AnaEntity {
	
	private HashMap<String,String> attributes;
	
	public AnaLoc (HashMap<String,String> attributes) {
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
		return "LOC";
	}
	
	public void updateAtr(String atr, String val) {
		String value = val;
		if (atr.equals("likes")) {
			String likes = this.attributes.get("likes");
			value = likes + "," + val;
		}
		this.attributes.put(atr, value);
	}

	@Override
	public JSONObject toJSON() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
