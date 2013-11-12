package kb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import answer.Extract;
import answer.ExtractName;
import answer.ExtractOrganization;
import answer.ExtractSymptom;
import answer.ExtractTime;

public class Medical {
	
	private int id;
	private HashMap<String, String> attributes;
	
	public Medical( int id ) {
		
		this.id = id;
		
		attributes = new HashMap<String, String>();
		attributes.put("issue", "");
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
	
	public Extract getCallback( String attr ) {
		
		if ( attr.equals("issue") ) {
			return new ExtractSymptom();
		} 
		
		return null;
	}
}
