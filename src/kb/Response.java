package kb;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import tools.Helpers;

public class Response implements Comparable {

	private int id;
	// response text // response type // entity // dialogue number // # of questions asked
	private HashMap<String, String> attributes;
	private Date date;
	
	public Response( int id ) {
		this.id = id;
		this.date = new Date();
		
		attributes = new HashMap<String, String>();
		attributes.put("text", "");
		attributes.put("type", "");
		attributes.put("entity", "");
		attributes.put("created", new SimpleDateFormat("yyyy/MM/dd-hh:mm:ss").format(new Date()));
	}
	
	public Response( int id, String text, String type, String entity ) {
		this.id = id;
		this.date = new Date();
		
		attributes = new HashMap<String, String>();
		attributes.put("text", text);
		attributes.put("type", type);
		attributes.put("entity", entity);
		attributes.put("created", new SimpleDateFormat("yyyy/MM/dd-hh:mm:ss").format(new Date()));
	}
	
	public String get( String attr ) {
		return attributes.get(attr);
	}
	
	public void update( String attr, String value ) throws IOException {
		attributes.put(attr, value);
	}
	
	public int getId() {
		return id;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public class CustomComparator implements Comparator<Response> {
		@Override
		public int compare(Response arg0, Response arg1) {
			// TODO Auto-generated method stub
			return arg0.getDate().compareTo(arg1.getDate());
		}
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		if(arg0 instanceof Response ) {
			Response other = (Response) arg0;
			return this.date.compareTo(other.getDate());
		}
		return -1;
	}
}
