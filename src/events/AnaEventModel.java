package events;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import config.Settings;

import stanford.Entity;
import stanford.Mention;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.util.CoreMap;
import entities.AnaEntity;
import entities.AnaEntityFactory;

import jnisvmlight.FeatureVector;
import jnisvmlight.LabeledFeatureVector;
import jnisvmlight.SVMLightInterface;
import jnisvmlight.SVMLightModel;
import jnisvmlight.TrainingParameters;

public class AnaEventModel {
	
	public static Properties props;
	public static Annotation document;
	public static StanfordCoreNLP pipeline;
	public static String stpWords[] = {"a", "to", "the"};
	
	public static String keyWords[] = {"concert", "class", "party", "graduation", "game", "event", "potluck", "gathering",
		"klatch", "breakfast", "lunch", "dinner", "supper", "barbeque", "gala", "function", "seminar", "yoga", "lecture",
		"meeting", "date", "trip", "conference", "dance", "shopping", "wedding", "funeral", "appointment",
		"mall", "movie", "visited", "visiting", "bowling", "skiing", "skating", "mahjiang", "cards"};

	public static void main(String args[]) throws IOException {
		
		ArrayList<String> raw = new ArrayList<String>();
		HashMap<String, String> lbl = new HashMap<String, String>();
		
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "event_train"));
		String line = br.readLine();
		while (line != null) {
			String data[] = line.split("\t");
			raw.add(data[0]);
			
			if (data[1].contains("@E")) {
				//System.out.println(data[0] + ":" + "1");
				lbl.put(data[0], "1");
			} else {
				lbl.put(data[0], "-1");
				//System.out.println(data[0] + ":" + "-1");
			}

			line = br.readLine();
		}
		br.close();
		
		// training structure
		SVMLightInterface trainer = new SVMLightInterface();
		ArrayList<LabeledFeatureVector> traindata = new ArrayList<LabeledFeatureVector>();
		//SVMLightInterface.SORT_INPUT_VECTORS = true;
				
		// context word features
		ArrayList<String> wrdFtrs = new ArrayList<String>();
		ArrayList<String> cxtFtrs = new ArrayList<String>();
		ArrayList<String> new_entities = new ArrayList<String>();
		ArrayList<AnaEntity> ana_entities = new ArrayList<AnaEntity>();
		HashMap<String, Integer> wrdContextCount = new HashMap<String, Integer>();
		HashMap<String, Integer> posContextCount = new HashMap<String, Integer>();
		
		// process  
	    props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
		pipeline = new StanfordCoreNLP(props);
		
		int sID = 0;
		for(String r: raw) {
			ArrayList<String> neList = new ArrayList<String>();
		    ArrayList<String> posList = new ArrayList<String>();
		    ArrayList<String> tknList = new ArrayList<String>();
		    
			document = new Annotation(r);
		    pipeline.annotate(document);
		    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		    
		    for(CoreMap sentence: sentences) {
		        for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
					String word = token.get(TextAnnotation.class);
					String pos = token.get(PartOfSpeechAnnotation.class);
					String ne = token.get(NamedEntityTagAnnotation.class);  
					
					neList.add(ne);
					posList.add(pos);
					tknList.add(word);
		        }
		    }
		    
			for (String k: keyWords) { // what if raw has multiple keywords?
				if (r.contains(k)) {

					int k_index = tknList.indexOf(k);
					ArrayList<String> wrdContext = getWindow(k_index, tknList);
					ArrayList<String> posContext = getWindow(k_index, posList);
					
					for (String cw: wrdContext) {
						if (wrdContextCount.keySet().contains(cw)) {
							wrdContextCount.put(cw, wrdContextCount.get(cw) + 1);
						} else {
							wrdContextCount.put(cw, 1);
						}
					}
					
					for (String cp: posContext) {
						if (posContextCount.keySet().contains(cp)) {
							posContextCount.put(cp, posContextCount.get(cp) + 1);
						} else {
							posContextCount.put(cp, 1);
						}
					}
				}
			}	
		}
		
		// write features to file
		File file = new File(Settings.path + "event_features.txt");
		if (!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		// add features that have at least a count of 2
		for(String contextWrd: wrdContextCount.keySet()) {
			if (wrdContextCount.get(contextWrd) > 1 && !contextWrd.equals(".")) {
				bw.write(contextWrd+"\n");
				wrdFtrs.add(contextWrd);
			}
		}
		
		// add features that have at least a count of 2
		for(String contextPos: posContextCount.keySet()) {
			if (posContextCount.get(contextPos) > 1 && !contextPos.equals(".")) {
				bw.write(contextPos+"\n");
				cxtFtrs.add(contextPos);
			}
		}
		
		bw.close();
					
		for(String r: raw) {
			ArrayList<String> neList = new ArrayList<String>();
			ArrayList<String> posList = new ArrayList<String>();
		    ArrayList<String> tknList = new ArrayList<String>();
		    
		    document = new Annotation(r);
		    pipeline.annotate(document);
		    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		    
		    for(CoreMap sentence: sentences) {
		        for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		        	String word = token.get(TextAnnotation.class);
					String pos = token.get(PartOfSpeechAnnotation.class);
					String ne = token.get(NamedEntityTagAnnotation.class);  
					
					neList.add(ne);
					posList.add(pos);
					tknList.add(word);
		        }
		    }
		    
			ArrayList<Integer> features = eventFeatures(tknList, posList, wrdFtrs, cxtFtrs, new_entities);
			
			//System.out.print(tknList + " ");
			//System.out.print(Integer.parseInt(lbl.get(r)) + ": ");
			//System.out.println(features + " ");
			if (features.size() < 1)
				continue;
			traindata.add(listToLabFeatVec( features, Integer.parseInt(lbl.get(r)) )); //traindata[sID].normalizeL2();
		    sID++;
		}
		
		// convert array-list to array
		LabeledFeatureVector[] td = new LabeledFeatureVector[traindata.size()];
		for(int i=0;i<traindata.size();i++) 
			td[i] = traindata.get(i);
		
		// create the model
		TrainingParameters tp = new TrainingParameters();
		SVMLightModel model = trainer.trainModel(td, tp);
		model.writeModelToFile("jni_event_model.dat");
		
		ArrayList<String> tst = new ArrayList<String>();
		tst.add("I want to eat pizza later.");
		tst.add("I will go to the game tonight.");
		
		HashMap<String, FeatureVector> mapping = new HashMap<String, FeatureVector>();
		
		int stID = 0;
		for (String t: tst) {
			ArrayList<String> neList = new ArrayList<String>();
			ArrayList<String> posList = new ArrayList<String>();
		    ArrayList<String> tknList = new ArrayList<String>();
		    document = new Annotation(t);
		    pipeline.annotate(document);
		    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		    
		    for(CoreMap sentence: sentences) {
		        for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		        	String word = token.get(TextAnnotation.class);
					String pos = token.get(PartOfSpeechAnnotation.class);
					String ne = token.get(NamedEntityTagAnnotation.class);  
					
					neList.add(ne);
					posList.add(pos);
					tknList.add(word);
		        }
		    }
			
			ArrayList<Integer> features = eventFeatures(tknList, posList, wrdFtrs, cxtFtrs, new_entities);
			if (features.size() < 1)
				continue;
			FeatureVector tfv = listToFeatVec( features ); //testdata[stID].normalizeL2();
			if (tfv != null) {
				mapping.put(t, tfv);
				stID++;
			}
		}
		
		System.out.println(wrdFtrs);
		System.out.println(cxtFtrs);
		for(String d: mapping.keySet()) {
			System.out.print(d + " ");
			System.out.println( model.classify(mapping.get(d)) );
		}
	}
	
	public static double classify(String input, ArrayList<String> posList, ArrayList<String> tknList, ArrayList<String> neList) throws ParseException, IOException {
		// read features
		ArrayList<String> wrdFtrs = new ArrayList<String>();
		ArrayList<String> cxtFtrs = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "event_features.txt"));
        String line = br.readLine();

        while (line != null) {
        	wrdFtrs.add(line);
            line = br.readLine();
        }
        br.close();
		
		SVMLightModel model = SVMLightModel.readSVMLightModelFromURL(new java.io.File(Settings.path + "jni_event_model.dat").toURL());
		
		ArrayList<String> tst = new ArrayList<String>();
		tst.add(input);
		
		HashMap<String, FeatureVector> mapping = new HashMap<String, FeatureVector>();
		
		int stID = 0;
		for (String t: tst) {
	
			ArrayList<Integer> features = eventFeatures(tknList, posList, wrdFtrs, cxtFtrs, neList);
			if (features.size() < 1)
				continue;
			FeatureVector tfv = listToFeatVec( features ); //testdata[stID].normalizeL2();
			if (tfv != null) {
				mapping.put(t, tfv);
				stID++;
			}
		}
		
		for(String d: mapping.keySet()) 
			return model.classify(mapping.get(d));
		return -1;
	}
	
	// [1, 7, 13] 1, We can't come because we're going to the hockey game next week
	// [to, attend, I, The, hosting, the, 's, will, IN, TO, DT, POS, VBG, VBD, PRP$, NN, VB, VBP, NNP, PRP, MD]
	public static ArrayList<Integer> eventFeatures(ArrayList<String> tknList, ArrayList<String> posList,
			ArrayList<String> wrdFtrs, ArrayList<String> posFtrs, ArrayList<String> neList) {

		// feature 1 is the keyword feature
		int num_features = 1;
		ArrayList<Integer> features = new ArrayList<Integer>();
		for(String k: keyWords) {
			if (tknList.contains(k)) {
				features.add(1);
				break;
			}
		}
		
		// feature 2 is the date or time feature
		for(String ne: neList) {
			if (ne.equals("DATE") || ne.equals("TIME")) {
				features.add(2);
				break;
			}
		}
		num_features++;
		
		// features 2 - wrdFtrs.length
		for(String k: keyWords) {
			if (tknList.contains(k)) {
				int k_index = tknList.indexOf(k);
				ArrayList<String> wrdContext = getWindow(k_index, tknList);
				for(String wf: wrdFtrs) {
					if (wrdContext.contains(wf)) {
						// add word feature
						num_features++;
						features.add(num_features);
					}
					num_features++;
				}
			}
		}
		
		// features wrdFtrs.length - posFtrs.length
		for(String k: keyWords) {
			if (tknList.contains(k)) {
				int k_index = tknList.indexOf(k);
				ArrayList<String> posContext = getWindow(k_index, posList);
				for(String pf: posFtrs) {
					if (posContext.contains(pf)) {
						// add pos feature
						num_features++;
						features.add(num_features);
					}
					num_features++;
				}
			}
		}
		
		return features;
	}
	
	// get a window of size 2
	public static ArrayList<String> getWindow(int k_index, ArrayList<String> tokens ) {
		ArrayList<String> context = new ArrayList<String>();
		
		List<String> stpList = Arrays.asList(stpWords);
		
		if (k_index > 1) {
			String tkn = tokens.get(k_index-2);
			if (!stpList.contains(tkn))
				context.add(tkn);
		}
		
		if (k_index > 0) {
			String tkn = tokens.get(k_index-1);
			if (!stpList.contains(tkn))
				context.add(tkn);
		}
		
		if (k_index < tokens.size() - 1) {
			String tkn = tokens.get(k_index+1);
			if (!stpList.contains(tkn))
				context.add(tkn);
		}
		
		if (k_index < tokens.size() - 2) {
			String tkn = tokens.get(k_index+2);
			if (!stpList.contains(tkn))
				context.add(tkn);
		}
		
		return context;
	}
	
	// f: 1 qid:1 42:1 54:1 12:1 ...
	public static LabeledFeatureVector listToLabFeatVec( ArrayList<Integer> f, int label ) {
		int index;
		int nDims = f.size();
		int[] dims = new int[nDims];
        double[] values = new double[nDims];
		
		for (int col: f) {
			index = f.indexOf(col);
			dims[index] = col;
			values[index] = 1;
		}
		
		if (dims.length == 0) {
			System.out.println("dims null");
			return null;
		}
		
		return new LabeledFeatureVector(label, dims, values);
	}
	
	// f: 1 qid:1 42:1 54:1 12:1 ...
	public static FeatureVector listToFeatVec( ArrayList<Integer> f ) {
		int index;
		int nDims = f.size();
		int[] dims = new int[nDims];
        double[] values = new double[nDims];
		
        for (int col: f) {
			index = f.indexOf(col);
			dims[index] = col;
			values[index] = 1;
		}
		
		if (dims.length == 0) {
			System.out.println("dims null");
			return null;
		}
		
		return new FeatureVector(dims, values);
	}
	
	public static ArrayList<Entity> yingEntity( ArrayList<String> entities, ArrayList<String> tokenList ) {

		ArrayList<Mention> mentions = new ArrayList<Mention>();
		ArrayList<Entity> new_entities = new ArrayList<Entity>();
		int sentStart = 0, position = 0, startEntity = 0;
		
		String name = "";
		String type = "";
		String lastNe = "O";
		
		for(int i=0;i<entities.size();i++) {
			String ne = entities.get(i);
			String word = tokenList.get(i);
			//System.out.println(ne+"/"+word);
			if(lastNe.equals("O")){
				if(!ne.equals("O")){
					startEntity = position;
					name = word;
					type = ne;
				}
			} else {
				
				if(ne.equals("O")){
					int endEntity = position-1;
					Entity entity = createEntity(name, lastNe);
					Mention mention = new Mention(entity, lastNe, startEntity+sentStart, endEntity+sentStart,tokenList);
					mentions.add(mention);
					//System.out.println("e: " + entity.toString() + ";" + mention.start);
					new_entities.add(entity);
				} else {
					if(ne.equals(lastNe)) {
						name += " " + word;
					}
				}
			
				if(!ne.equals(lastNe) && !ne.equals("O")){
					int endEntity = position-1;
					Entity entity = createEntity(name,  lastNe);
					
					Mention mention = new Mention(entity,lastNe, startEntity+sentStart, endEntity+sentStart,tokenList);
					mentions.add(mention);
					//System.out.println("e: " + entity.toString() + ";" + mention.start);
					new_entities.add(entity);
					startEntity=position;
					name = word;
				}
			}
			lastNe = ne;
			position++;
		}
		
		// verify mention ending at the last token
		if(!lastNe.equals("O") && !lastNe.equals(".")){
			int endEntity = position-1;
			Entity entity = createEntity(name,  lastNe);
			
			Mention mention = new Mention(entity, lastNe,startEntity+sentStart, endEntity+sentStart, tokenList);
			mentions.add(mention);
			//System.out.println("e: " + entity.toString() + ";" + mention.start);
			new_entities.add(entity);
		}
		
		return new_entities;
	  }
	
	 public static Entity createEntity(String name, String stanfordType){
		
		String myType=Entity.NONE;
		
		if(stanfordType.equals("PERSON"))
			myType = Entity.PERSON;
		
		if(stanfordType.equals("ORGANIZATION"))
			myType = Entity.ORGANIZATION;
		
		if(stanfordType.equals("LOCATION"))
			myType = Entity.LOCATION;
		
		if(stanfordType.equals("DATE"))
			myType = Entity.DATE;
		
		if(stanfordType.equals("GPE"))
			myType = Entity.GPE;
		
		if(stanfordType.equals("MISC"))
			myType = Entity.MISC;

		if(stanfordType.equals("MONEY"))
			myType = Entity.MONEY;
		
		if(stanfordType.equals("PERCENT"))
			myType = Entity.PERCENT;
		
		if(stanfordType.equals("TIME"))
			myType = Entity.TIME;
		
		if(stanfordType.equals("NUMBER"))
			myType = "NUM";
		
		if(stanfordType.equals("DURATION"))
			myType = "DUR";
		
		
		String id = name.toUpperCase() + "#" + myType;
		
		return new Entity(id, name, myType);
	}
}
