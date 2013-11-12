package graph;

public class AnaEdge {

	private String rel;
	private int gov, dep;
	
	public AnaEdge ( int gov, String rel, int dep ) {
		this.gov = gov;
		this.dep = dep;
		this.rel = rel;
	}
	
	public int getGov() {
		return this.gov;
	}
	
	public int getDep() {
		return this.dep;
	}
	
	public String getRel() {
		return this.rel;
	}
}
