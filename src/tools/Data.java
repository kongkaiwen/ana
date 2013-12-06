package tools;

import java.util.ArrayList;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;

public class Data {

	private String mention, label;
	private ArrayList<String> pos, tkns;
	private SemanticGraph dependencies;
	private Tree constituent;

	public Data ( String mention, String label ) {
		this.mention = mention;
		this.label = label;
	}
	
	public void setConstituent( Tree tree ) {
		constituent = tree;
	}
	
	public Tree getConstituent() {
		return constituent;
	}
	
	public String getMention() {
		return mention;
	}
	
	public double getLabel() {
		return Double.parseDouble(label);
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
	
	public SemanticGraph getDependencies() {
		return dependencies;
	}

	public void setDependencies(SemanticGraph dependencies) {
		this.dependencies = dependencies;
	}
}
