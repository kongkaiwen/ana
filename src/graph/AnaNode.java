package graph;

public class AnaNode {
	
	private int id;
	private String pos, word, ner;
	
	public AnaNode ( int id, String pos, String word, String ner ) {
		this.id = id;
		this.pos = pos;
		this.word = word;
		this.ner = ner;
	}
	
	public int getId() {
		return this.id;
	}

	public String getPos() {
		return pos;
	}
	
	public String getWord() {
		return word;
	}
	
	public String getNer() {
		return ner;
	}
}
