package kb;

import java.io.IOException;
import java.util.ArrayList;

import org.joda.time.DateTime;

import relations.Entity;

import answer.Extract;

public class ResponseBuffer {

	private int oid;
	private DateTime timeout;
	private String line, response, object, attr;
	private Extract callback;
	
	public ResponseBuffer( String line, String response, int oid, String object, String attr, DateTime timeout, Extract callback ) {
		this.oid = oid;
		this.line = line;
		this.response = response;
		this.object = object;
		this.attr = attr;
		this.timeout = timeout;
		this.callback = callback;
	}
	
	public DateTime getTimeout() {
		return timeout;
	}

	public String getLine() {
		return line;
	}

	public String getResponse() {
		return response;
	}

	public String getObject() {
		return object;
	}
	
	public String getAttr() {
		return attr;
	}
	
	public int getOID() {
		return oid;
	}
	
	public boolean executeCallback( String data, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos ) throws IOException {
		return callback.execute( oid, object, attr, kb, tkns, ent, pos );
	}
}
