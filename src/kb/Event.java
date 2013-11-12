package kb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import answer.Extract;
import answer.ExtractAge;
import answer.ExtractDate;
import answer.ExtractName;
import answer.ExtractOrganization;
import answer.ExtractTime;

public class Event implements Entity {

	private int id;
	private HashMap<String, String> attributes;
	
	public Event( int id ) {
		
		this.id = id;
		
		attributes = new HashMap<String, String>();
		attributes.put("start", "");
		attributes.put("end", "");
		attributes.put("where", "");
		attributes.put("who", "");
		attributes.put("tense", "");
		attributes.put("created", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
	}
	
	public String get( String attr ) {
		return attributes.get(attr);
	}
	
	public void update( String attr, String value ) {
		if (attr.equals("who")) {
			if (attributes.get(attr).equals("")) {
				attributes.put(attr, value);
			} else {
				String list = attributes.get(attr);
				list = list + ", " + value;
				attributes.put(attr, list);
			}
		} else {
			attributes.put(attr, value);
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getEmptyAttr() {
		/*
			1. Start
			2. Who
			3. Where
			4. End
		*/
		
		if (attributes.get("start").equals("")) {
			return "start";
		} else if (attributes.get("who").equals("")) {
			return "who";
		} else if (attributes.get("where").equals("")) {
			return "where";
		} else if (attributes.get("end").equals("")) {
			return "end";
		}
	
		return null;
	}
	
	public Extract getCallback( String attr ) {
		
		if ( attr.equals("start") ) {
			return new ExtractTime();
		} else if ( attr.equals("end") ) {
			return new ExtractTime();
		} else if ( attr.equals("where") ) {
			return new ExtractOrganization();
		} else if ( attr.equals("who") ) {
			return new ExtractName();
		}
		
		return null;
	}
	
	public JSONObject toTableJSON() throws JSONException {
		JSONObject evt = new JSONObject();
		evt.put("id", getId());
		evt.put("name", get("name"));
		evt.put("where", get("where"));
		evt.put("who", get("who"));
		evt.put("start", get("start"));
		evt.put("end", get("end"));
		evt.put("created", get("created"));
		return evt;
	}
}
