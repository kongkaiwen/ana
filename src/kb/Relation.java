package kb;

import org.json.JSONException;
import org.json.JSONObject;

public class Relation {

	private int id, e1, e2;
	private String type;
	
	public Relation ( int id, int e1, int e2, String type ) {
		this.id = id;
		this.e1 = e1;
		this.e2 = e2;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	
	public int getE1() {
		return e1;
	}

	public int getE2() {
		return e2;
	}

	public String getType() {
		return type;
	}
	
	public JSONObject toJSON() throws JSONException {
		// { data: { id: "e1", source: "n1", target: "n2" } }
		JSONObject obj = new JSONObject();
		JSONObject jso = new JSONObject();
		
		//jso.put("id", String.valueOf(getId()));
		jso.put("source", String.valueOf(getE1()));
		jso.put("target", String.valueOf(getE2()));
		jso.put("relation", type);
		
		obj.put("data", jso);
		
		return obj;
	}
	
}
