package events;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import tools.Data;
import tools.Helpers;

import jnisvmlight.FeatureVector;
import jnisvmlight.LabeledFeatureVector;
import jnisvmlight.SVMLightInterface;
import jnisvmlight.SVMLightModel;
import jnisvmlight.TrainingParameters;

import config.Settings;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import entities.AnaEntity;

public class EventBinary {
	
	public static String stpWords[] = {"a", "to", "the"};
	
	public static String keyWords[] = {"concert", "class", "party", "graduation", "game", "event", "potluck", "gathering",
		"klatch", "breakfast", "lunch", "dinner", "supper", "barbeque", "gala", "function", "seminar", "yoga", "lecture",
		"meeting", "date", "trip", "conference", "dance", "shopping", "wedding", "funeral", "appointment",
		"mall", "movie", "visited", "visiting", "bowling", "skiing", "skating", "mahjiang", "cards"};

	public static void main(String args[]) throws IOException, ParseException {
		
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos");
		StanfordCoreNLP pipeline = new StanfordCoreNLP( props );
		
		ArrayList<EventData> data = new ArrayList<EventData>();
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "event_train"));
		
		String line;
		
		while( (line = br.readLine()) != null ) {

			String tokens[] = line.split("\t");
			String label = "-1";
			String mention = tokens[0];
			
			if (tokens[1].contains("@E"))
				label = "1";
			
			EventData d = new EventData(mention, label);
	
			d.setPos(Helpers.getPOS(pipeline, mention));
			d.setTkns(Helpers.getTokens(pipeline, mention));
			
			data.add(d);
		}
		
		br.close();
		
		ArrayList<String> wrdFtrs = new ArrayList<String>();
		ArrayList<String> cxtFtrs = new ArrayList<String>();
		HashMap<String, ArrayList<String>> clusters = Helpers.loadBrownClusters("100.txt");
		
		EventBinary eb = new EventBinary();	
		SVMLightModel model= eb.createModel(data, wrdFtrs, cxtFtrs );
		model.writeModelToFile("event_binary_model.dat");
		
		double right = 0.0;
		double wrong = 0.0;
		double total = 0.0;
		for (EventData d: data) {
			double val = eb.classify(d, clusters);
			System.out.println(d.getMention() + ": " + " (" + val + " // " + d.getLabel() + ")");
			
			if ( val > 0.7 && Double.parseDouble(d.getLabel()) == 1 ) {
				right++;
			} else if ( val < 0.7 && Double.parseDouble(d.getLabel()) == 1 ) {
				wrong++;
			}
			
			if ( val > 0.7 ) 
				total++;
		}
		
		//Phil is thinking about moving.
		//Phil is thinking about buying a house.
		//Phil lives by the beach.
		//Phil wants to travel.
		//Well I'm 25 years old.
		//I just went to dinner with Jana.
		String linee = "I'm 25 years old.";
		EventData dd = new EventData(linee, "");
		dd.setPos(Helpers.getPOS(pipeline, linee));
		dd.setTkns(Helpers.getTokens(pipeline, linee));
		double val = eb.classify(dd, clusters);
		
		System.out.println(val);
		
		System.out.println("precision: " + right/(total));
		System.out.println("recall: " + right/(right+wrong));
	}
	
	public SVMLightModel createModel(ArrayList<EventData> data, ArrayList<String> wrdFtrs, ArrayList<String> posFtrs ) throws IOException {
		
		SVMLightInterface trainer = new SVMLightInterface();
		ArrayList<LabeledFeatureVector> traindata = new ArrayList<LabeledFeatureVector>();
		
		ArrayList<String> clusters = new ArrayList<String>();
		HashMap<String, Integer> clusterCounts = new HashMap<String, Integer>();
		HashMap<String, ArrayList<String>> brownClusters = Helpers.loadBrownClusters("100.txt");
		
		for (String kw: keyWords) {
			for (String cluster: brownClusters.keySet()) {
				ArrayList<String> wrds = brownClusters.get(cluster);
				if (wrds.contains(kw)) {
					if (clusterCounts.containsKey(cluster))
						clusterCounts.put(cluster, clusterCounts.get(cluster)+1);
					else
						clusterCounts.put(cluster, 1);
				}
			}
		}
		
		for (String c: clusterCounts.keySet()) {
			if (!clusters.contains(c) && clusterCounts.get(c) > 2)
				clusters.add(c);
		}
		
		ArrayList<String> wrdNgrams = Helpers.getDescriminativeNgrams(data, 2, false);
		ArrayList<String> posNgrams = Helpers.getDescriminativeNgrams(data, 2, true);
		
//		HashMap<String, Integer> gdVerbs = new HashMap<String, Integer>();
//		HashMap<String, Integer> bdVerbs = new HashMap<String, Integer>();
//		
//		// good verbs vs bad verbs
//		for (EventData d: data) {
//			String lab = d.getLabel();
//			ArrayList<String> dPos = d.getPos();
//			ArrayList<String> dTkn = d.getTkns();
//			for (String p: dPos) {
//				int pindex = dPos.indexOf(p);
//				if (p.contains("VB")) {
//					String addWord = dTkn.get(pindex).toLowerCase();
//					if (lab.equals("1")) {
//						if (gdVerbs.containsKey(addWord)) {
//							gdVerbs.put(addWord, gdVerbs.get(addWord)+1);
//						} else {
//							gdVerbs.put(addWord, 1);
//						}
//					} else {
//						if (bdVerbs.containsKey(addWord)) {
//							bdVerbs.put(addWord, bdVerbs.get(addWord)+1);
//						} else {
//							bdVerbs.put(addWord, 1);
//						}
//					}
//				}
//			}
//		}
//		
//		for(String v: gdVerbs.keySet()) {
//			System.out.println(v+": "+gdVerbs.get(v));
//		}
//		System.out.println("-------");
//		for(String v: bdVerbs.keySet()) {
//			System.out.println(v+": "+bdVerbs.get(v));
//		}
//		
//		System.exit(0);
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(Settings.path + "event_binary_clusters.txt"));
        for (String c: clusters)
        	bw.write(c+"\n");
        bw.close();
        
        bw = new BufferedWriter(new FileWriter(Settings.path + "event_binary_wrd_features.txt"));
        for (String c: wrdNgrams)
        	bw.write(c+"\n");
        bw.close();
        
        bw = new BufferedWriter(new FileWriter(Settings.path + "event_binary_pos_features.txt"));
        for (String c: posNgrams)
        	bw.write(c+"\n");
        bw.close();
		
		for (EventData d: data) {
			ArrayList<String> ff = eventFeatures(d, wrdNgrams, posNgrams, clusters, brownClusters);

			if (ff.size() == 0)
				continue;
			traindata.add(listToLabFeatVec( ff, Integer.parseInt(d.getLabel()) ));
		}
		
		int sID = 0;
		LabeledFeatureVector[] svmtraindata = new LabeledFeatureVector[traindata.size()];
		for (LabeledFeatureVector lfv: traindata) {
			svmtraindata[sID] = lfv;
			sID++;
		}
		
		TrainingParameters tp = new TrainingParameters();
		return trainer.trainModel(svmtraindata, tp);
	}
	
	public double classify(EventData d, HashMap<String, ArrayList<String>> brownClusters) throws ParseException, IOException {
		// read features
		ArrayList<String> wrdFtrs = new ArrayList<String>();
		ArrayList<String> posFtrs = new ArrayList<String>();
		ArrayList<String> clusters = new ArrayList<String>();
		
		String line;
		BufferedReader br;
		
		br = new BufferedReader(new FileReader(Settings.path + "event_binary_wrd_features.txt"));
        while ((line = br.readLine()) != null)
        	wrdFtrs.add(line);
        br.close();
        
        br = new BufferedReader(new FileReader(Settings.path + "event_binary_pos_features.txt"));
        while ((line = br.readLine()) != null)
        	posFtrs.add(line);
        br.close();
        
        br = new BufferedReader(new FileReader(Settings.path + "event_binary_clusters.txt"));
        while ((line = br.readLine()) != null)
        	clusters.add(line);
        br.close();
		
		SVMLightModel model = SVMLightModel.readSVMLightModelFromURL(new java.io.File(Settings.path + "event_binary_model.dat").toURL());
		ArrayList<String> ff = eventFeatures(d, wrdFtrs, posFtrs, clusters, brownClusters);
		FeatureVector tfv = listToFeatVec( ff );

		if (tfv == null || tfv.size() == 0)
			return -1.0;
		return model.classify(tfv);
	}
	
	public static ArrayList<String> eventFeatures(EventData data, ArrayList<String> wrdFtrs, ArrayList<String> posFtrs, ArrayList<String> clusters, HashMap<String, ArrayList<String>> brownClusters) {

		ArrayList<String> tknList = data.getTkns();
		ArrayList<String> posList = data.getPos();
		ArrayList<String> thisClusters = new ArrayList<String>();
		
		for (String c: brownClusters.keySet()) {
			ArrayList<String> wrds = brownClusters.get(c);
			for (String tkn: tknList) {
				if (wrds.contains(tkn)) {
					if (!thisClusters.contains(c))
						thisClusters.add(c);
				}
			}
		}
		
		// feature 1 is the keyword feature
		int num_features = 1;
		ArrayList<String> features = new ArrayList<String>();
		for(String k: keyWords) {
			if (tknList.contains(k)) {
				features.add(num_features + ":1");
				break;
			}
		}
		num_features++;
		
		// word clusters
		for ( String clstr: clusters ) {
			if (thisClusters.contains(clstr)) 
				features.add(num_features + ":1");
			num_features++;
		}
		
		// features 2 - wrdFtrs.length
		for(String wrd: wrdFtrs) {
			if (data.getMention().toLowerCase().contains(wrd))
				features.add(num_features + ":1");
			num_features++;
		}
		
		// features wrdFtrs.length - posFtrs.length
		for(String pos: posFtrs) {
			if (Helpers.join(posList, " ").toLowerCase().contains(pos))
				features.add(num_features + ":1");
			num_features++;
		}
		
		return features;
	}
	
	// f: 1 qid:1 42:1 54:1 12:1 ...
	public static FeatureVector listToFeatVec( ArrayList<String> f ) {

		int nDims = f.size();
		int[] dims = new int[nDims];
        double[] values = new double[nDims];
		
		for (String col: f) {
			int index = f.indexOf(col);
			String tokens[] = col.split(":");
			dims[index] = Integer.parseInt(tokens[0].trim());
			values[index] = Double.parseDouble(tokens[1].trim());
		}
		
		if (dims.length == 0)
			return null;

		return new FeatureVector(dims, values);
	}
	
	public LabeledFeatureVector listToLabFeatVec( ArrayList<String> f, int label ) {
		int index;
		int nDims = f.size();
		int[] dims = new int[nDims];
        double[] values = new double[nDims];
		
		for (String col: f) {
			index = f.indexOf(col);
			String tokens[] = col.split(":");
			dims[index] = Integer.parseInt(tokens[0].trim());
			values[index] = Double.parseDouble(tokens[1].trim());
		}
		
		if (dims.length == 0) {
			System.out.println("dims null");
			return null;
		}
		
		return new LabeledFeatureVector(label, dims, values);
	}
}
