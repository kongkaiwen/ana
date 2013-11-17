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

import jnisvmtree.SVMLightInterface;
import jnisvmtree.SVMLightInterface2;
import relationEx.DPathUtil;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleGraph;

import parseFilter.NPExStanford;
import parseFilter.trainEx.StanfordParser;
import parseFilter.trainEx.Treebank2StanfLabeled;
import relationEx.DPPair;
import relationEx.DPType;
import stanford.Mention;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import graph.DPPath2TBTrad;
import graph.DPPath2TBTradNew;
import graph.LabeledEdge;

/**
 * This RE needs StanfordCoreNLP pipeline as input.
 * 
 * @author ying
 *
 */
public class RelationExtract {
	int maxEntryTokDist = 15; //the max distance between two named entities is 15.
	int maxDpDist = 4; //the max dependency path distance between relation words and entities.
	public StanfordCoreNLP pipeline= null;
	
	SVMLightInterface2 svmlight = null;
	//long svmModelOpen = -1;
	SVMLightInterface2[] svmlightTrad = null;
	long[] svmModelTrad = null;
	//String[] svmTradOffset = {"loc","buss", "famy", "citizen"};
	String[] svmTradOffset = {"famy"};
	
	public RelationExtract(String stopFile,  String svmModelPath, StanfordCoreNLP pipeline) throws IOException{
		initRest(stopFile, svmModelPath);
		this.pipeline = pipeline;
	}
	
	public RelationExtract( StanfordCoreNLP pipeline) throws IOException{
		this.pipeline = pipeline;
	}
	
	private void initRest(String stopFile,  String svmModelPath) throws IOException{
		svmlight = new SVMLightInterface2();



		this.svmlightTrad = new SVMLightInterface2[this.svmTradOffset.length];
		this.svmModelTrad = new long[this.svmTradOffset.length];

		for(int i=0;i<svmTradOffset.length;i++){
			this.svmlightTrad[i] = new SVMLightInterface2();
			String svmTestFExample = svmModelPath+"svm.test."+this.svmTradOffset[i];
			String svmModelF = svmModelPath+"model."+this.svmTradOffset[i];
			String[] args3 = {"svm_classify", "-f", "1","-v","0",svmTestFExample, svmModelF};
			int argc = args3.length;
			this.svmModelTrad[i] = this.svmlightTrad[i].loadModel(argc, args3);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testOpenIE();
		testTradIE();
	}
	
	public static void testTradIE(){
		String path = "/media/ying/My Passport/Ying/2012treebankRelationTestData/";
		String stopFile = "smallStopWords.txt";
		String path2 = "/home/ying/study/tools/SVM-Light-1.5-to-be-released/";
		//String modelPath = path2+"kevin_model/noNoiseFilt/"; old model
		String modelPath = path2+"kevin_only_family/";
		
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
				Properties props = new Properties();
				props.put("annotators", "tokenize, ssplit, pos, lemma, ner,parse");
				//props.put("annotators", "tokenize, ssplit, pos,parse");
				StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
				
		try {
			RelationExtract relationExtractor = new RelationExtract(stopFile,  modelPath, pipeline);
			//String sent = "My mom is Ann, and my father is Phil.";
			String sent = "Kevin's mother Ann lives in Edmonton.";
			//String sent = "Winnette  Hi, this is Winnette, in Michigan.";
			//String sent = "She is 24 years old.";
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
			HashMap govListMap = DPathUtil.getGovList(dpPairList);

			DPPath2TBTradNew dpPath2TB_trad = new DPPath2TBTradNew(dpPairList,
					dpTypeList, tokenList,
					posList,  graph,
					null);
			
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
				for(int j=i+1;j<mentionList.size();j++){
					Mention mention2 = (Mention)mentionList.get(j);
					String type2 = mention2.getEntity().getType();
					int start2 = mention2.getStartToken();
					int end2 = mention2.getEndToken();
					if(start2-end<=this.maxEntryTokDist){//in a certain distance
						
						//now check their dp distance
						int dpDist = pairDpDist(graph, tokenList, start, end, start2, end2);
						if(dpDist<=this.maxDpDist){
							
							//filter out by svm
							String relation = this.extractREPair_trad(dpPath2TB_trad, govListMap, tokenList, posList, start, end, start2, end2, mention.getType(), mention2.getType());
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
	
	public String extractREPair_trad(DPPath2TBTradNew dpPath2TB_Trad, 
			HashMap govListMap, ArrayList tokenList, ArrayList posList,
			int start1, int end1, int start2, int end2, String type1, String type2 ) throws IOException{
		String relation = null;
		int head1 = DPathUtil.getHead(govListMap, tokenList, posList, start1, end1);
		int head2 = DPathUtil.getHead(govListMap, tokenList, posList, start2, end2);
		


		//then using the traditional svm model to decide which it is.
		String result = dpPath2TB_Trad.getTreePath(head1, head2, type1, type2);
		if(result!=null){
			String line = "1\t|BT| "+result+" |ET|";
			double v1 = Double.MIN_VALUE;
			for(int i=0;i<this.svmlightTrad.length;i++){
				double v = this.svmlightTrad[i].classifyNative(line, this.svmModelTrad[i]);
				if(v>0&& v>v1){
					relation = this.svmTradOffset[i];
				}
			}
		}
		return relation;
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
	
	public void clearModels(){
		
		for(int i=0;i<this.svmlightTrad.length;i++){
			this.svmlightTrad[i].clearModel(this.svmModelTrad[i]);
		}
	}
	
	/**
	 * Get the min distance between two entities, with offset (start1, end1) and (start2, end2).
	 * @param graph
	 * @param start1
	 * @param end1
	 * @param start2
	 * @param end2
	 * @return
	 */
	public static int pairDpDist(UndirectedGraph<String, ? extends DefaultEdge> graph, ArrayList tokenList ,int start1, int end1, int start2, int end2){
		int dist = Integer.MAX_VALUE;
		for(int i=start1;i<=end1;i++){
			String token1 = i+"_"+(String)tokenList.get(i);
			for(int j=start2;j<=end2;j++){
				String token2 = j+"_"+(String)tokenList.get(j);
				try{
					java.util.List list =  DijkstraShortestPath.findPathBetween(graph,token1, token2);
					if(list!=null){
						int distTemp = list.size();
						if(dist>distTemp){
							dist = distTemp;
						}
					}
				}catch(java.lang.IllegalArgumentException e){
					e.printStackTrace();
					System.exit(0);
				}
			}
		}
		return dist;
	}

}
