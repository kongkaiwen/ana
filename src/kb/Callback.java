package kb;

import java.io.IOException;
import java.util.ArrayList;

import org.joda.time.DateTime;

import relations.Entity;

import answer.Extract;

public class Callback {
	
	private int oid;
	private String line, obj, atr;
	private DateTime time;
	private Extract function;

	public Callback(String line, String question, int oid, String obj, String atr, DateTime time, Extract function) {
		this.line = line;
		this.obj = obj;
		this.atr = atr;
		
		this.oid = oid;
		this.time = time;
		this.function = function;
	}
	
	public Extract getFunction() {
		return this.function;
	}
	
	public String getAtr() {
		return this.atr;
	}
	
	public String getLine() {
		return this.line;
	}
	
	public boolean executeCallback( String data, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos ) throws IOException {
		return function.execute( oid, obj, atr, kb, tkns, ent, pos );
	}
}
