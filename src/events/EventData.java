package events;

import java.util.ArrayList;


public class EventData {

	private String mention, label, cluster;
	private ArrayList<String> pos, tkns, ner;

	public EventData ( String mention, String label ) {
		this.mention = mention;
		this.label = label;
	}
	
	public String getCluster() {
		return this.cluster;
	}
	
	public void setCluster(String cluster) {
		this.cluster = cluster;
	}
	
	public String getMention() {
		return mention;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public ArrayList<String> getPos() {
		return pos;
	}

	public void setPos(ArrayList<String> pos) {
		this.pos = pos;
	}

	public ArrayList<String> getTkns() {
		return tkns;
	}

	public void setTkns(ArrayList<String> tkns) {
		this.tkns = tkns;
	}

	public ArrayList<String> getNER() {
		return ner;
	}

	public void setNER(ArrayList<String> ner) {
		this.ner = ner;
	}
	
}
