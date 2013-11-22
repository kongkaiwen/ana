package kb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class Request {

	private int id;
	private HashMap<String, String> attributes;
	
	public Request (int id ) {
		this.id = id;
		
		attributes = new HashMap<String, String>();
		attributes.put("name", "");
		attributes.put("created", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
	}
	
	public int getId() {
		return id;
	}
	
	public String get( String attr ) {
		return attributes.get(attr);
	}
	
	public void update( String attr, String value ) {
		attributes.put(attr, value);
	}
	
	public JSONObject toTableJSON() throws JSONException {
		JSONObject med = new JSONObject();
		med.put("id", getId());
		med.put("name", get("name"));
		med.put("created", get("created"));
		return med;
	}
}
