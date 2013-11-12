package graph;

public class AnaNode {
	
	private int id;
	private String pos, word;
	
	public AnaNode ( int id, String pos, String word ) {
		this.id = id;
		this.pos = pos;
		this.word = word;
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
}
