package kb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import answer.Extract;
import answer.ExtractBinary;
import answer.ExtractCondition;
import answer.ExtractTime;

public class Daily {

	private int id;
	private HashMap<String, String> attributes;
	
	public Daily( int id ) {
		// wake up, go to bed, took medication, ate meals, mood or condition
		this.id = id;
		
		attributes = new HashMap<String, String>();
		attributes.put("condition", "");
		attributes.put("woken", "");
		attributes.put("slept", "");
		attributes.put("tookmeds", "");
		attributes.put("atebreakfast", "");
		attributes.put("atelunch", "");
		attributes.put("atedinner", "");
		attributes.put("created", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
	}
	
	public String get( String attr ) {
		return attributes.get(attr);
	}
	
	public void update( String attr, String value ) {
		attributes.put(attr, value);
	}
	
	public int getId() {
		return id;
	}
	
	public String getEmptyAttr() {
		/*
			1. Name
			2. Age
			3. Education_insititute
			4. Profession_institute
		*/
		
		if (attributes.get("woken").equals("")) {
			return "woken";
		} else if (attributes.get("condition").equals("")) {
			return "condition";
		} else if (attributes.get("tookmeds").equals("")) {
			return "tookmeds";
		} else if (attributes.get("atebreakfast").equals("")) {
			return "atebreakfast";
		}
		
		return null;
	}
	
	public Extract getCallback( String attr ) {
		
		if ( attr.equals("condition") ) {
			return new ExtractCondition();
		} else if ( attr.equals("woken") ) {
			return new ExtractTime();
		} else if ( attr.equals("slept") ) {
			return new ExtractTime();
		} else if ( attr.equals("tookmeds") ) {
			return new ExtractBinary();
		} else if ( attr.equals("atebreakfast") ) {
			return new ExtractBinary();
		} else if ( attr.equals("atelunch") ) {
			return new ExtractBinary();
		} else if ( attr.equals("atedinner") ) {
			return new ExtractBinary();
		}
		
		return null;
	}
	
	public JSONObject toTableJSON() throws JSONException {
		JSONObject dai = new JSONObject();
		dai.put("condition", get("condition"));
		dai.put("woken", get("woken"));
		dai.put("slept", get("slept"));
		dai.put("tookmeds", get("tookmeds"));
		dai.put("breakfast", get("atebreakfast"));
		dai.put("lunch", get("atelunch"));
		dai.put("dinner", get("atedinner"));
		return dai;
	}
}
