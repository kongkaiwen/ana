package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import tools.Helpers;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class AnaParseGraph {
	
	private HashMap<Integer, AnaEdge> edges;
	private HashMap<Integer, AnaNode> nodes;
	
	public static void main(String args[]) {
		System.out.println("AAA");
		Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
	    String line = "Jacob is my nephew.";
		SemanticGraph dependencies = Helpers.getDependencies(pipeline, line);
		AnaParseGraph apg = new AnaParseGraph( dependencies );
		apg.hasLink("nephew", "Jacob");
	}

	public AnaParseGraph( SemanticGraph dependencies ) {
		this.edges = new HashMap<Integer, AnaEdge>();
  		this.nodes = new HashMap<Integer, AnaNode>();
  		
  		for (IndexedWord iw: dependencies.vertexListSorted()) 
  			this.nodes.put(iw.index(), new AnaNode(iw.index(), iw.tag(), iw.word(), iw.ner()));
  		
  		for( edu.stanford.nlp.semgraph.SemanticGraphEdge e1: dependencies.edgeIterable() ) {
  			IndexedWord dep = e1.getDependent();
        	IndexedWord gov = e1.getGovernor();
        	GrammaticalRelation rel = e1.getRelation();
        	
        	AnaEdge ae = new AnaEdge(gov.index(), rel.toString(), dep.index());
        	this.edges.put(e1.hashCode(), ae);
        }
	}
	
	// nephew, Jacob
	public boolean hasLink( String node, String person ) {
		
		// find the correct node
		for(Integer k: edges.keySet()) {
			
			AnaEdge ae = edges.get(k);
			
			int di = ae.getDep();
			int gi = ae.getGov();
			String link = ae.getRel();
			
			AnaNode dep = nodes.get(di);
			AnaNode gov = nodes.get(gi);
			
			//System.out.println(dep.getWord() + "(" + dep.getNer() + ")" + " " + link + " " + "(" + gov.getNer() + ")" +  gov.getWord());
			
			if (dep.getWord().toLowerCase().equals(person.toLowerCase()) && gov.getWord().toLowerCase().equals(node.toLowerCase())) {
				//System.out.println("has link(" + node + ", " + person + "): true");
				return true;
			}
			
			if (dep.getWord().toLowerCase().equals(node.toLowerCase()) && gov.getWord().toLowerCase().equals(person.toLowerCase())) {
				//System.out.println("has link(" + node + ", " + person + "): true");
				return true;
			}
		}
		
		//System.out.println("has link(" + node + ", " + person + "): false");
		return false;
	}
	
	// VBZ-dobj>-NN
	// VBZ-xcomp>-VB-dobj>-NN
	// NNP-<nsubj-VBZ-dobj>-NN
	public boolean contain( String pattern ) {
		String rel, dir, dep, gov;

		String parts[] = pattern.split("-");
		ArrayList<Boolean> contains = new ArrayList<Boolean>();
		
		int prev = -1;
		int components = 0;
		for (int i=0;i<parts.length-1;i+=2) {
			components++;
			contains.add(false);
			
			rel = parts[i+1];
			dir = (rel.charAt(0) == '<') ? "left" : "right";
			rel = rel.replaceAll("<", "").replaceAll(">", "");
			
			if ( dir.equals("right") ) {
				gov = parts[i];
				dep = parts[i+2];
			} else {
				gov = parts[i+2];
				dep = parts[i];
			}
			
			//System.out.println(gov+" "+rel+" "+dep);
			for (AnaEdge ae: this.edges.values()) {
				AnaNode govN = this.nodes.get(ae.getGov());
				AnaNode depN = this.nodes.get(ae.getDep());
				
				if ( components > 1  ) {
					//System.out.println(govN.getPos()+" "+ae.getRel()+" "+depN.getPos()+" "+prev+" "+govN.getId());
					if ( dir.equals("left") ) {
						if (gov.equals(govN.getPos()) && rel.equals(ae.getRel()) && dep.equals(depN.getPos()) && prev == depN.getId()) {
							contains.set(components-1, true);
							prev = govN.getId();
						}
					} else {
						if (gov.equals(govN.getPos()) && rel.equals(ae.getRel()) && dep.equals(depN.getPos()) && prev == govN.getId()) {
							contains.set(components-1, true);
							prev = depN.getId();
						}
					}
				} else {
					//System.out.println(govN.getPos()+" "+ae.getRel()+" "+depN.getPos());
					if (gov.equals(govN.getPos()) && rel.equals(ae.getRel()) && dep.equals(depN.getPos())) {
						contains.set(components-1, true);
						
						if ( dir == "left" )
							prev = govN.getId();
						else 
							prev = depN.getId();
					}
				}
			}
		}
		
		boolean flag = true;
		for (Boolean b: contains) 
			flag = flag && b;
		
		return flag;
	}
	
	// VBZ-dobj>-NN
	// VBZ-xcomp>-VB-dobj>-NN
	// NNP-<nsubj-VBZ-dobj>-NN
	public ArrayList<String> extract( String pattern ) {
		String rel, dir, dep, gov;

		String parts[] = pattern.split("-");
		ArrayList<String> tokens = new ArrayList<String>();
		
		int prev = -1;
		int components = 0;
		for (int i=0;i<parts.length-1;i+=2) {
			components++;
			
			rel = parts[i+1];
			dir = (rel.charAt(0) == '<') ? "left" : "right";
			rel = rel.replaceAll("<", "").replaceAll(">", "");
			
			if ( dir.equals("right") ) {
				gov = parts[i];
				dep = parts[i+2];
			} else {
				gov = parts[i+2];
				dep = parts[i];
			}
			
			for (AnaEdge ae: this.edges.values()) {
				AnaNode govN = this.nodes.get(ae.getGov());
				AnaNode depN = this.nodes.get(ae.getDep());
				
				if ( components > 1  ) {
					if ( dir.equals("left") ) {
						if (gov.equals(govN.getPos()) && rel.equals(ae.getRel()) && dep.equals(depN.getPos()) && prev == depN.getId()) {
							tokens.add(govN.getWord()+"#"+govN.getPos());
							prev = govN.getId();
						}
					} else {
						if (gov.equals(govN.getPos()) && rel.equals(ae.getRel()) && dep.equals(depN.getPos()) && prev == govN.getId()) {
							tokens.add(depN.getWord()+"#"+depN.getPos());
							prev = depN.getId();
						}
					}
				} else {
					if (gov.equals(govN.getPos()) && rel.equals(ae.getRel()) && dep.equals(depN.getPos())) {
						
						if ( dir == "left" ) {
							tokens.add(depN.getWord()+"#"+depN.getPos());
							tokens.add(govN.getWord()+"#"+govN.getPos());
							prev = govN.getId();
						} else {
							tokens.add(govN.getWord()+"#"+govN.getPos());
							tokens.add(depN.getWord()+"#"+depN.getPos());
							prev = depN.getId();
						}
					}
				}
			}
		}
		
		return tokens;
	}
	
	// Tomorrow is John's party. -> John's party -> NN-POS>-NNP
	// Anna's graduation is next week. -> Anna's graduation -> NN-POS>-NNP
	// We are going to see a movie tonight. -> movie -> VB-dobj>-NN
	// returns the first one found
	public ArrayList<String> extract_nounphrase() {
		ArrayList<String> noun_patterns = new ArrayList<String>();
		noun_patterns.add("NN-poss>-NNP");
		noun_patterns.add("VB-dobj>-NN");
		noun_patterns.add("NN-nn>-NNP");
		String rel, dir, dep, gov;

		for(String pattern: noun_patterns) {
			if (!this.contain(pattern)) {
				continue;
			}
			String parts[] = pattern.split("-");
			ArrayList<String> tokens = new ArrayList<String>();
			
			int prev = -1;
			int components = 0;
			for (int i=0;i<parts.length-1;i+=2) {
				components++;
				
				rel = parts[i+1];
				dir = (rel.charAt(0) == '<') ? "left" : "right";
				rel = rel.replaceAll("<", "").replaceAll(">", "");
				
				if ( dir.equals("right") ) {
					gov = parts[i];
					dep = parts[i+2];
				} else {
					gov = parts[i+2];
					dep = parts[i];
				}
				
				for (AnaEdge ae: this.edges.values()) {
					AnaNode govN = this.nodes.get(ae.getGov());
					AnaNode depN = this.nodes.get(ae.getDep());
					
					if ( components > 1  ) {
						if ( dir.equals("left") ) {
							if (gov.equals(govN.getPos()) && rel.equals(ae.getRel()) && dep.equals(depN.getPos()) && prev == depN.getId()) {
								tokens.add(govN.getWord()+"#"+govN.getPos());
								prev = govN.getId();
							}
						} else {
							if (gov.equals(govN.getPos()) && rel.equals(ae.getRel()) && dep.equals(depN.getPos()) && prev == govN.getId()) {
								tokens.add(depN.getWord()+"#"+depN.getPos());
								prev = depN.getId();
							}
						}
					} else {
						if (gov.equals(govN.getPos()) && rel.equals(ae.getRel()) && dep.equals(depN.getPos())) {
							
							if ( dir == "left" ) {
								tokens.add(depN.getWord()+"#"+depN.getPos());
								tokens.add(govN.getWord()+"#"+govN.getPos());
								prev = govN.getId();
							} else {
								tokens.add(govN.getWord()+"#"+govN.getPos());
								tokens.add(depN.getWord()+"#"+depN.getPos());
								prev = depN.getId();
							}
						}
					}
				}
			}
			return tokens;
		}
		return null;
	}
}
