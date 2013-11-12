package relations;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import jnisvmtree.SVMLightInterface2;

import localDPLink.DpLinkTranEx;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

import config.Settings;

import parseFilter.DataTransformSVMFlat;
import parseFilter.Feature;
import parseFilter.NPExStanford;
import parseFilter.trainEx.SentExTreebank;
import parseFilter.trainEx.StanfordParser;
import parseFilter.trainEx.Treebank2StanfLabeled;
import patternEX.lucene.PatternOffsetAttribute;
import patternEX.lucene.PatternTypeAttribute;
import patternEX.lucene.PhrasePAnalyzerWithOffsetwithDup;
import patternEX.lucene.PhrasePTokenizerWithOffset;
import relationEx.DPPair;
import relationEx.DPType;
import relationEx.YingClustering;
import stanford.Mention;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import graph.DPPath2TBTrad;
import graph.DPPath2TBwithNEordered;
import graph.LabeledEdge;
import graph.relation.DPPath2TBwithRordered;
import graph.relation.FeatureExR;

/**
 * This RE needs StanfordCoreNLP pipeline as input.
 * 
 * @author ying
 *
 */
public class RelationExtract {
	int maxEntryTokDist = 15; //the max distance between two named entities is 15.
	int maxDpDist = 6; //the max dependency path distance between relation words and entities.
	
	public StanfordCoreNLP pipeline= null;
	
	public Analyzer contextAnalyzer = null;//to get relation patterns.
	HashMap featureIdMap = null;
	SVMLightInterface2 svmlight = null;
	long svmModelOpen = -1;
	SVMLightInterface2[] svmlightTrad = null;
	long[] svmModelTrad = null;
	String[] svmTradOffset = {"loc","buss", "famy", "citizen"};
	boolean OpenIE = false;
	public RelationExtract(String stopFile, String flatFeatureMap, String svmModelPath, StanfordCoreNLP pipeline, boolean OpenIE) throws IOException{
		this.OpenIE = OpenIE;
		initRest(stopFile,flatFeatureMap, svmModelPath, OpenIE);
		this.pipeline = pipeline;
	}
	
	private void initRest(String stopFile, String flatFeatureMap, String svmModelPath, boolean OpenIE) throws IOException{
		this.contextAnalyzer = new PhrasePAnalyzerWithOffsetwithDup(4, stopFile, 5);
		featureIdMap = DataTransformSVMFlat.readInFeatureId(flatFeatureMap);
		svmlight = new SVMLightInterface2();
		
		if(OpenIE){
			String svmTestFExample = svmModelPath+"tb_test100_judged_withRpt_TandV_5.txt3";
			String svmModelF = svmModelPath+"model_TandV_5";
			String[] args2 = {"svm_classify", "-f", "1","-v","0",svmTestFExample, svmModelF};
			int argc = args2.length;

			this.svmModelOpen = svmlight.loadModel(argc, args2);
			if(this.svmModelOpen!=-1){
				//System.out.println("reading svm model success.");
			}else{
				//System.out.println("reading svm model failed.");
				System.exit(0);
			}
		}
		if(!OpenIE){
			String svmTestFExample = svmModelPath+"new1000sent_wsj3_dist20_test100_judged_5.txt";
			String svmModelF = svmModelPath+"model_5";
			String[] args2 = {"svm_classify", "-f", "1","-v","0",svmTestFExample, svmModelF};
			int argc = args2.length;

			this.svmModelOpen = svmlight.loadModel(argc, args2);
			if(this.svmModelOpen!=-1){
				//System.out.println("reading svm model success.");
			}else{
				//System.out.println("reading svm model failed.");
				System.exit(0);
			}
			
			this.svmlightTrad = new SVMLightInterface2[4];
			this.svmModelTrad = new long[4];

			for(int i=0;i<svmTradOffset.length;i++){
				this.svmlightTrad[i] = new SVMLightInterface2();
				svmTestFExample = svmModelPath+"svm.test."+this.svmTradOffset[i];
				svmModelF = svmModelPath+"model."+this.svmTradOffset[i];
				String[] args3 = {"svm_classify", "-f", "1","-v","0",svmTestFExample, svmModelF};
				argc = args3.length;
				this.svmModelTrad[i] = this.svmlightTrad[i].loadModel(argc, args3);
				if(this.svmModelTrad[i]!=-1){
					//System.out.println("reading svm model success.");
				}else{
					//System.out.println("reading svm model failed.");
					System.exit(0);
				}
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testOpenIE();
		testTradIE();
	}
	
	public static void testOpenIE(){
		String path = "/media/My Passport/Ying/LDC/LDC99T42_treebank/parsed/mrg/wsj_process/";
		String flatFeatureMap = path+"svm_tk/final/treebank/tb_judged_withRpt_flat.txt.featureMap3";
		String stopFile = "smallStopWords.txt";
		String path2 = "/home/ying/study/tools/SVM-Light-1.5-to-be-released/";
		String modelPath = path2+"kevin_model/";
		
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
				Properties props = new Properties();
				props.put("annotators", "tokenize, ssplit, pos, lemma, ner,parse");
				//props.put("annotators", "tokenize, ssplit, pos,parse");
				StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
				
		try {
			RelationExtract relationExtractor = new RelationExtract(stopFile, flatFeatureMap, modelPath, pipeline, true);
			String sent = "Ann is Kevin 's mother. She is mom of Sarah too.";
			Annotation document = new Annotation(sent);

			// run all Annotators on this text
			pipeline.annotate(document);
			relationExtractor.extractRE(document, System.out);
			relationExtractor.clearModels();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testTradIE(){
		String path = "/media/My Passport/Ying/LDC/LDC99T42_treebank/parsed/mrg/wsj_process/";
		String flatFeatureMap = path+"svm_tk/final/treebank/tb_judged_withRpt_flat.txt.featureMap3";
		String stopFile = "smallStopWords.txt";
		String path2 = "/home/ying/study/tools/SVM-Light-1.5-to-be-released/";
		String modelPath = path2+"kevin_model/";
		
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
				Properties props = new Properties();
				props.put("annotators", "tokenize, ssplit, pos, lemma, ner,parse");
				//props.put("annotators", "tokenize, ssplit, pos,parse");
				StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
				
		try {
			RelationExtract relationExtractor = new RelationExtract(stopFile, flatFeatureMap, modelPath, pipeline, false);
			//String sent = "Ann is Kevin 's mother. She is mom of Sarah too.";
			String sent = "Kevin's mother Ann lives in Edmonton.";
			//String sent = "Winnette  Hi, this is Winnette, in Michigan.";
			Annotation document = new Annotation(sent);

			// run all Annotators on this text
			pipeline.annotate(document);
			relationExtractor.extractRE_trad(document, System.out);
			relationExtractor.clearModels();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * traditional IE: Extract relations from one sentence.
	 * @param sent
	 * @throws IOException 
	 */
	public HashMap extractRE_trad(Annotation document, PrintStream output) throws IOException{
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		HashMap allRelations = new HashMap();
		for(CoreMap sentence: sentences) {
			ArrayList tokenList = new ArrayList();
			ArrayList posList = new ArrayList();
			ArrayList lemmaList = new ArrayList();
			ArrayList<DPPair> dpPairList = new ArrayList();
			ArrayList<DPType> dpTypeList= new ArrayList();
			UndirectedGraph<String, LabeledEdge> graph  = new SimpleGraph<String, LabeledEdge>(LabeledEdge.class);
			Treebank2StanfLabeled.transforGetToken(sentence, tokenList, posList, lemmaList, dpPairList, dpTypeList, graph);
			HashMap govListMap = DpLinkTranEx.getGovList(dpPairList);
			//System.out.println("here");
			DPPath2TBwithNEordered dpPath2TB = new DPPath2TBwithNEordered(dpPairList, dpTypeList,  tokenList,  posList,
					graph,null);
			dpPath2TB.getFuncWords(Settings.path + "func_verb.txt");
			dpPath2TB.setNewPOS();
			//System.out.println("here4");
			DPPath2TBTrad dpPath2TB_trad = new DPPath2TBTrad(dpPairList,
					dpTypeList, tokenList,
					posList,  graph,
					null);
			
			ArrayList anotherTokenList = new ArrayList();//not useful, just for the function call
			ArrayList anotherPOSList = new ArrayList();//not useful, just for the function call
			
			ArrayList mentionList = new ArrayList();
			//System.out.println("here3");
			NPExStanford.extractOneSentWithProp(0, mentionList, anotherTokenList, anotherPOSList, sentence);
			anotherTokenList.clear();
			anotherPOSList.clear();
			//System.out.println("here2");
			ArrayList mentionOffsetList = this.getMentionOffsets(mentionList);
			for(int i=0;i<mentionList.size();i++){
				Mention mention = (Mention)mentionList.get(i);
				String type1 = mention.getEntity().getType();
				int start = mention.getStartToken();
				int end = mention.getEndToken();
				//System.out.println(type1+"\t"+start+"\t"+end);
				for(int j=i+1;j<mentionList.size();j++){
					Mention mention2 = (Mention)mentionList.get(j);
					String type2 = mention2.getEntity().getType();
					int start2 = mention2.getStartToken();
					int end2 = mention2.getEndToken();
					//System.out.println("\t"+type2+"\t"+start2+"\t"+end2+"\t");
					if(start2-end<=this.maxEntryTokDist){//in a certain distance
						//output.println(mention.getEntity().getName()+":"+start+":"+end+";"+
								//mention2.getEntity().getName()+":"+start2+":"+end2);
						//now check their dp distance
						int dpDist = SentExTreebank.pairDpDist(graph, tokenList, start, end, start2, end2);
						if(dpDist<=this.maxDpDist){
							
							//filter out by svm
							String relation = this.extractREPair_trad(dpPath2TB, dpPath2TB_trad, govListMap, tokenList, posList, start, end, start2, end2, mention.getType(), mention2.getType());
							if(relation==null){
								//output.println("\tno relation.");
							}else{
								//output.println("relation:"+relation);
								allRelations.put(mention.getEntity().getName()+":"+start+":"+end+";"+
								mention2.getEntity().getName()+":"+start2+":"+end2, relation);
							}
						}
					}
				}
			}
		}
		return allRelations;
	}
	
	public String extractREPair_trad(DPPath2TBwithNEordered dpPath2TB,DPPath2TBTrad dpPath2TB_Trad, 
			HashMap govListMap, ArrayList tokenList, ArrayList posList,
			int start1, int end1, int start2, int end2, String type1, String type2 ) throws IOException{
		String relation = null;
		int head1 = DpLinkTranEx.getHead(govListMap, tokenList, posList, start1, end1);
		int head2 = DpLinkTranEx.getHead(govListMap, tokenList, posList, start2, end2);
		String result = dpPath2TB.getTreePath(head1, head2);
		if(result!=null){
			String line = "1\t|BT| "+result+" |ET|";
			double v1 = 1;//svmlight.classifyNative(line, this.svmModelOpen);
			if(v1>0){//there is a relation
				//then using the traditional svm model to decide which it is.
				result = dpPath2TB_Trad.getTreePath(head1, head2, type1, type2);
				if(result!=null){
					line = "1\t|BT| "+result+" |ET|";
					//System.out.println(line);
					v1 = Double.MIN_VALUE;
					for(int i=0;i<this.svmlightTrad.length;i++){
						double v = this.svmlightTrad[i].classifyNative(line, this.svmModelTrad[i]);
						//System.out.println("v:"+this.svmTradOffset[i]+" "+v);
						if(v>0&& v>v1){
							relation = this.svmTradOffset[i];
						}
					}
				}
			}
		}
		return relation;
	}
	
	
	/**
	 * Open IE: Extract relations from one sentence.
	 * @param sent
	 * @throws IOException 
	 */
	public void extractRE(Annotation document, PrintStream output) throws IOException{
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for(CoreMap sentence: sentences) {
			ArrayList tokenList = new ArrayList();
			ArrayList posList = new ArrayList();
			ArrayList lemmaList = new ArrayList();
			ArrayList<DPPair> dpPairList = new ArrayList();
			ArrayList<DPType> dpTypeList= new ArrayList();
			UndirectedGraph<String, LabeledEdge> graph  = new SimpleGraph<String, LabeledEdge>(LabeledEdge.class);
			Treebank2StanfLabeled.transforGetToken(sentence, tokenList, posList, lemmaList, dpPairList, dpTypeList, graph);
			HashMap govListMap = DpLinkTranEx.getGovList(dpPairList);

			DPPath2TBwithRordered dpPath2TB = new DPPath2TBwithRordered(dpPairList, dpTypeList,  tokenList,  posList,
					graph,null);
			dpPath2TB.getFuncWords("func_verb.txt");
			dpPath2TB.setNewPOS();

			ArrayList posForPath = new ArrayList();//afraid that the function will chage pos.
			posForPath.addAll(posList);
			FeatureExR dpPath2TBFlat = new FeatureExR(dpPairList,
					dpTypeList, tokenList,
					posForPath,  graph,
					null);
			dpPath2TB.getFuncWords("func_verb.txt");
			dpPath2TB.setNewPOS();

			ArrayList anotherTokenList = new ArrayList();//not useful, just for the function call
			ArrayList anotherPOSList = new ArrayList();//not useful, just for the function call
			
			ArrayList mentionList = new ArrayList();
			NPExStanford.extractOneSentWithProp(0, mentionList, anotherTokenList, anotherPOSList, sentence);
			anotherTokenList.clear();
			anotherPOSList.clear();
			ArrayList mentionOffsetList = this.getMentionOffsets(mentionList);
			for(int i=0;i<mentionList.size();i++){
				Mention mention = (Mention)mentionList.get(i);
				String type1 = mention.getEntity().getType();
				int start = mention.getStartToken();
				int end = mention.getEndToken();
				//System.out.println(type1+"\t"+start+"\t"+end);
				for(int j=i+1;j<mentionList.size();j++){
					Mention mention2 = (Mention)mentionList.get(j);
					String type2 = mention2.getEntity().getType();
					int start2 = mention2.getStartToken();
					int end2 = mention2.getEndToken();
					//System.out.println("\t"+type2+"\t"+start2+"\t"+end2+"\t");
					if(start2-end<=this.maxEntryTokDist){//in a certain distance
						//output.println(mention.getEntity().getName()+":"+start+":"+end+";"+
								//mention2.getEntity().getName()+":"+start2+":"+end2);
						//now check their dp distance
						int dpDist = SentExTreebank.pairDpDist(graph, tokenList, start, end, start2, end2);
						if(dpDist<=this.maxDpDist){
							//now for every pair, I need to extract relations.
							ArrayList relations = this.extractREPair(start, end, start2, end2, tokenList, posList, lemmaList,mentionOffsetList);
							//filter out by svm
							relations = this.relationFilterSVMTandV(dpPath2TB, dpPath2TBFlat, govListMap, tokenList, posList, relations, start, end, start2, end2);
							if(relations.size()==0){
								//output.println("\tno relation.");
							}else{
								//output.println("possible relations:");
								for(int in=0;in<relations.size();in++){
									Relation rtemp = (Relation)relations.get(in);
									//output.println(rtemp.rWords);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Return arrayList of Relation instances
	 * @param start1
	 * @param end1
	 * @param start2
	 * @param end2
	 * @param tokenList
	 * @param posList
	 * @param mentionOffsetList
	 * @return
	 * @throws IOException
	 */
	public ArrayList extractREPair(int start1, int end1,int start2, int end2, ArrayList tokenList, ArrayList posList, ArrayList lemmaList, ArrayList mentionOffsetList) throws IOException{
		//get the form required by Analyzer
		String sent = this.getTaggedSent(tokenList, posList, lemmaList);
		//offset is in the form of 4-5;7-7
		int[] pairOffset = {start1,end1,start2,end2};
		String sentNew = getPTExForm(sent,  start1, end1, start2, end2);
		
		//get patterns in the sentence, with all the information: pattern offset, pattern type
		boolean isRelation = false;
		StringReader reader = new StringReader(sentNew); 
		TokenStream tokens = contextAnalyzer.tokenStream("context",reader);
		TermAttribute termAtt = (TermAttribute) tokens.addAttribute(TermAttribute.class);
		PatternTypeAttribute typeAtt = (PatternTypeAttribute) tokens.addAttribute(PatternTypeAttribute.class);
		PatternOffsetAttribute offsetAtt = (PatternOffsetAttribute)tokens.addAttribute(PatternOffsetAttribute.class);
		tokens.reset();
		// print all tokens until stream is exhausted
		ArrayList relation = new ArrayList();

		while (tokens.incrementToken()) {
			String term = termAtt.term();
				String types = typeAtt.getType();
				String[] typeChunk = types.split(";");
				int[] type = new int[3];
				for(int tempI = 0;tempI<typeChunk.length;tempI++)
					type[tempI] = Integer.parseInt(typeChunk[tempI]);
				String offset = offsetAtt.getOffset();
				int[] relationOffsets = PhrasePTokenizerWithOffset.parseOffset(offset);
				//System.out.println("candidate pattern:"+term);
				//System.out.println("candidate offset:"+offset);
				//the term can not be in one named entity
				boolean isMention = false;
				for(int i=0;i<3;i++){
					for(int j=relationOffsets[2*i];j<=relationOffsets[2*i+1];j++){
					//need to find the relation extraction set, check if I used this one
						if(mentionOffsetList.contains(j))
							isMention = true;
					}
				}
				if(!isMention){
					//now check if the relation is correct with SVM tree kernel.
					boolean clsLabel = true;//DependEx.dpFilter(dpPairSet, pairOffset, relationOffsets, type);
					//System.out.println("clsLabel:"+clsLabel);
					if(clsLabel){
						isRelation = true;
						relation.add(new Relation(term,relationOffsets, type));
					}
				}
		}
		tokens.end();
		tokens.close();
		return relation;
	}
	
	public ArrayList relationFilterSVMTandV(DPPath2TBwithRordered dpPath2TB,FeatureExR dpPath2TBFlat, HashMap govListMap, ArrayList tokenList, ArrayList posList, ArrayList relations, int start1, int end1, int start2, int end2 ){
		int head1 = DpLinkTranEx.getHead(govListMap, tokenList, posList, start1, end1);
		int head2 = DpLinkTranEx.getHead(govListMap, tokenList, posList, start2, end2);
		ArrayList newRList = new ArrayList();
		for(int i=0;i<relations.size();i++){
			Relation r =(Relation) relations.get(i);
			String result =  dpPath2TB.getTreePath(head1, head2,r.rOffsets);
			int[] pairOffset = new int[]{start1,end1,start2, end2};
			dpPath2TBFlat.extractFeature(tokenList, posList, head1, head2, pairOffset, r.rOffsets, r.type);
			HashMap features = dpPath2TBFlat.getFeatures();
			String featureV = this.getFeatureVector(features);
			String line = "1\t|BT| "+result+" |ET| "+featureV;
			//System.out.println(line);
			double v1 = svmlight.classifyNative(line, this.svmModelOpen);
			if(v1>0)
				newRList.add(r);
		}
		return newRList;
	}
	
	public String getFeatureVector(HashMap features){
		ArrayList tempFVector = new ArrayList();
		StringBuffer fVector = new StringBuffer();
		Iterator iter = features.keySet().iterator();
		while(iter.hasNext()){
			String feature = (String)iter.next();
			if(feature.indexOf("E12E2_null")<0&&feature.indexOf("E1POS_")<0 && feature.indexOf("E2POS_")<0){
				Object idO = featureIdMap.get(feature);
				if(idO!=null){
					String id = (String)idO;
					tempFVector.add(new Feature(Integer.parseInt(id),"1"));
				}
			}
		}
		Collections.sort(tempFVector);
		for(int i=0;i<tempFVector.size();i++){
			Feature tempF = (Feature)tempFVector.get(i);
			fVector.append(" ").append(tempF.getID()).append(":").append(tempF.getV());
		}
		return fVector.toString().trim();
	}
	
	/**
	 * Return all the offset whose words are mentions.
	 * @param mentionList
	 * @return
	 */
	public ArrayList getMentionOffsets( ArrayList mentionList){
		ArrayList mentionOffset = new ArrayList();
		if(mentionList!=null && mentionList.size()>0){
			for(int i=0;i<mentionList.size();i++){
				Mention mention = (Mention)mentionList.get(i);
				int start = mention.getStartToken();
				int end = mention.getEndToken();
				for(int j=start;j<=end;j++)
					mentionOffset.add(j);
			}
		}
		return mentionOffset;
	}
	
	/**
	 * Get the new sentence form for relation pattern extraction
	 * @param sent
	 * @return
	 */
	public String getPTExForm(String sent,int start1, int end1, int start2, int end2){
		//offset is in the form of 4-5;7-7
		String offsets = start1+"-"+end1+";"+start2+"-"+end2;
		String[] segments = YingClustering.getSegment(sent,start1, end1, start2,end2);
		if(segments==null)
			return null;
		String before = segments[0];
		String between = segments[1];
		String after = segments[2];
		String sentNew = before +"<PER>"+between+"<PER>"+after+"<OFFSET>"+offsets;
		return sentNew;	
	}
	
	
	/**
	 * Get the sentence in the form of word/pos/lemma word/pos/lemma; 
	 * 
	 * @param tokenList
	 * @param posList
	 * @return
	 */
	public String getTaggedSent(ArrayList tokenList, ArrayList posList, ArrayList lemmaList){
		StringBuffer sent = new StringBuffer();
		for(int i=0;i<tokenList.size();i++){
			String token = (String)tokenList.get(i);
			String pos = (String)posList.get(i);
			String lemma = (String)lemmaList.get(i);
			sent.append(token).append("/").append(pos).append("/").append(lemma).append(" ");
		}
		return sent.toString().trim();
	}
	
	public void clearModels(){
		this.svmlight.clearModel(this.svmModelOpen);
		if(!this.OpenIE){
			for(int i=0;i<this.svmlightTrad.length;i++){
				this.svmlightTrad[i].clearModel(this.svmModelTrad[i]);
			}
		}
	}

}
