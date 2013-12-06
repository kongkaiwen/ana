package tools;

import java.util.ArrayList;

public class Text {

	String line;
	ArrayList<String> tkn, pos, ent;

	public Text( String line ) {
		this.line = line;
	}
	
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public ArrayList<String> getTkn() {
		return tkn;
	}

	public void setTkn(ArrayList<String> tkn) {
		this.tkn = tkn;
	}

	public ArrayList<String> getPos() {
		return pos;
	}

	public void setPos(ArrayList<String> pos) {
		this.pos = pos;
	}

	public ArrayList<String> getEnt() {
		return ent;
	}

	public void setEnt(ArrayList<String> ent) {
		this.ent = ent;
	}
}
