package events;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import jnisvmlight.FeatureVector;
import jnisvmlight.LabeledFeatureVector;
import jnisvmlight.LearnParam;
import jnisvmlight.SVMLightInterface;
import jnisvmlight.SVMLightModel;
import jnisvmlight.TrainingParameters;

import config.Settings;

import tools.Helpers;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class EventWord {
	
	public static String keyWords[] = {"concert", "class", "party", "graduation", "game", "event", "potluck", "gathering",
		"klatch", "breakfast", "lunch", "dinner", "supper", "barbeque", "gala", "function", "seminar", "yoga", "lecture",
		"meeting", "date", "trip", "conference", "dance", "shopping", "wedding", "funeral", "appointment",
		"mall", "movie", "visited", "visiting", "bowling", "skiing", "skating", "mahjiang", "cards"};

	public static void main(String args[]) throws ParseException, IOException {
		
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
		StanfordCoreNLP pipeline = new StanfordCoreNLP( props );
		
		ArrayList<EventData> data = new ArrayList<EventData>();
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "event_train"));
		
		String line;
		while( (line = br.readLine()) != null ) {

			String tokens[] = line.split("\t");
			String label = "-1";
			String mention = tokens[0];
			
			if (tokens[1].contains("@NE"))
				continue;
			
			// @E-dinner
			if (tokens[1].contains("@E"))
				label = tokens[1].split("-")[1];
			
			EventData d = new EventData(mention, label);
	
			d.setPos(Helpers.getPOS(pipeline, mention));
			d.setTkns(Helpers.getTokens(pipeline, mention));
			d.setNER(Helpers.getEntities(pipeline, mention));
			data.add(d);
		}
		
		br.close();
		
		EventWord ew = new EventWord();	
		SVMLightModel model= ew.createModel(data);
		model.writeModelToFile("event_word_model.dat");	
		
		double pos = 0.0;
		double neg = 0.0;
		for (EventData d: data) {
			String word = ew.extract(d, Helpers.loadBrownClusters("100.txt"));
			System.out.println(d.getMention() + " -> " + word);
			
			if (word.equals(d.getLabel())) {
				pos++;
			} else {
				neg++;
			}
		}
		
		System.out.println(pos/(pos+neg));
	}
	
	public SVMLightModel createModel( ArrayList<EventData> data ) throws IOException {
		
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
		
//		for (String c: clusterCounts.keySet()) {
//			System.out.println(clusterCounts.get(c));
//			clusters.add(c);
//		}
	
//		for (String c: clusterCounts.keySet()) {
//			if (!clusters.contains(c) && clusterCounts.get(c) > 3)
//				clusters.add(c);
//		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(Settings.path + "event_word_features.txt"));
        for (String c: clusters)
        	bw.write(c+"\n");
        bw.close();
		
		for (EventData d: data) {
			ArrayList<String> tkns = d.getTkns();
			
			for (String tkn: tkns) {
				
				String tknCluster = null;
				
				// find cluster
				for(String clster: brownClusters.keySet()) {
					if (brownClusters.get(clster).contains(tkn))
						tknCluster = clster;
				}
				
				int index = tkns.indexOf(tkn);
				ArrayList<String> ff = getSVMFeatures( index, d, tknCluster, clusters );
	
				if (ff.size() == 0)
					continue;
				
				int label = (d.getLabel().equals(tkn)) ? 1 : -1;
				traindata.add(listToLabFeatVec( ff, label ));
			}
		}
		//System.exit(0);
		
		int sID = 0;
		LabeledFeatureVector[] svmtraindata = new LabeledFeatureVector[traindata.size()];
		for (LabeledFeatureVector lfv: traindata) {
			svmtraindata[sID] = lfv;
			sID++;
		}
		
		TrainingParameters tp = new TrainingParameters();
		LearnParam lp = new LearnParam();
		lp.type = 3;
		tp.setLearningParameters(lp);
		return trainer.trainModel(svmtraindata, tp);
	}
	
	public String extract( EventData d, HashMap<String, ArrayList<String>> brownClusters ) throws ParseException, IOException {
		// read features
		
		ArrayList<String> clusters = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "event_word_features.txt"));
        String line = br.readLine();

        while ((line = br.readLine()) != null)
        	clusters.add(line);
        br.close();
		
		SVMLightModel model = SVMLightModel.readSVMLightModelFromURL(new java.io.File(Settings.path + "event_word_model.dat").toURL());

		ArrayList<String> tkns = d.getTkns();
		
		double high = -1;
		String high_word = "";
		for (String tkn: tkns) {
			
			String tknCluster = null;
			
			// find cluster
			for(String clster: brownClusters.keySet()) {
				if (brownClusters.get(clster).contains(tkn))
					tknCluster = clster;
			}
			
			int index = tkns.indexOf(tkn);
			ArrayList<String> ff = getSVMFeatures( index, d, tknCluster, clusters);
			FeatureVector tfv = listToFeatVec( ff );
			
			if (tfv == null)
				continue;

			double val = model.classify(tfv);
			if (val > high) {
				high = val;
				high_word = tkn;
			}
		}	
		
		return high_word;
	}
	
	public static ArrayList<String> getSVMFeatures( int index, EventData d, String tknCluster, ArrayList<String> clusters ) throws IOException {
		
		ArrayList<String> features = new ArrayList<String>();
		
		ArrayList<String> ners = d.getNER();
		ArrayList<String> poss = d.getPos();
		ArrayList<String> tkns = d.getTkns();
		
		String tkn = tkns.get(index);
		String pos = poss.get(index);
		String ner = ners.get(index);
		
		String prev_tkn = null;
		String prev_pos = null;
		String prev_ner = null;
		
		if (index > 0) {
			prev_tkn = tkns.get(index-1);
			prev_pos = tkns.get(index-1);
			prev_ner = tkns.get(index-1);
		}
		
		//System.out.println(tkn +":"+pos+":"+ner);
		
		int curr_features = 1;
		int feature_weight = 1;
		
		/**************************************************************
		  current word features
		**************************************************************/
		
		for (String kw: keyWords) {
			if (tkn.toLowerCase().equals(kw)) {
				features.add(curr_features+":"+feature_weight);
				break;
			}
		}
		curr_features++;
		
		// word clusters
		if (tknCluster != null) {
			for (String clster: clusters) {
				if (tknCluster.equals(clster))
					features.add(curr_features+":"+feature_weight);
				curr_features++;
			}
		}
		
		// is the pos a verb?
		if ( pos.contains("VB") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( pos.contains("NN") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( pos.contains("PRP") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( pos.contains("IN") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( tkn.length() < 4 ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( ner.equals("DATE") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( ner.equals("TIME") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( ner.equals("PERSON") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( ner.equals("LOCATION") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		/**************************************************************
		  previous word features
		**************************************************************/
		
		// is the pos a verb?
		if ( prev_pos != null && prev_pos.contains("VB") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( prev_pos != null && prev_pos.contains("NN") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( prev_pos != null && prev_pos.contains("PRP") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( prev_pos != null && prev_pos.contains("IN") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if (prev_tkn != null &&  prev_tkn.length() < 4 ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( prev_ner != null && prev_ner.equals("DATE") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( prev_ner != null && prev_ner.equals("TIME") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( prev_ner != null && prev_ner.equals("PERSON") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the pos word a verb?
		if ( prev_ner != null && prev_ner.equals("LOCATION") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		return features;
	}
	
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
