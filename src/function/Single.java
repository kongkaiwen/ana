package function;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jnisvmlight.FeatureVector;
import jnisvmlight.LabeledFeatureVector;
import jnisvmlight.SVMLightInterface;
import jnisvmlight.SVMLightModel;
import jnisvmlight.TrainingParameters;

import tools.Data;
import tools.Helpers;

public class Single {
	
	private String root_path;
	
	private final static String questionWords = "^(.*)(can|could|would|will|how|who|what|where|when|did)(.*)$";
	private final static String sentimentWords  = "^(.*)(like|great|child|good|kind|best|love|nice|live|money|recommend|romance|amazing|respect|worst|nightmare|fuck|favorite|fantastic|beautiful|generous|horrible|shitty|free|excellent|sucks|waste|awesome|ridiculous)(.*)$";

	// this is for tag interrogative sentences
	private final static String[] tagPhrases = {"don’t you", "isn't it", "do you", "doesn't she", "aren't you", "can't he", "shouldn't he", "how about you", "right ."};
	private final static String[] ummPhrases = {"umm", "ok", "sure", "okay", "good", "yeah", "yes", "oh", "well", "alright", "right", "uh", "ugh"};
		
	public Single( String root_path ) {
		this.root_path = root_path;
	}
	
	public SVMLightModel createModel( ArrayList<Data> data, ArrayList<String> links, ArrayList<String> pos_bigram, ArrayList<String> trigrams, HashMap<String, ArrayList<String>> clusters, double model_type, double set ) throws IOException {
		
		// initialize and create proper training vectors
		SVMLightInterface trainer = new SVMLightInterface();
		ArrayList<LabeledFeatureVector> traindata = new ArrayList<LabeledFeatureVector>();
		SVMLightInterface.SORT_INPUT_VECTORS = true;
		
		for (Data d: data) {
			ArrayList<String> ff = getSVMFeatures(d, pos_bigram, trigrams, links, clusters, set);
			//System.out.println(ff);
			if (ff.size() == 0)
				continue;
			int label = (d.getLabel() == model_type) ? 1 : -1; 
			traindata.add(listToLabFeatVec( ff, label ));
		}
		
		int sID = 0;
		LabeledFeatureVector[] svmtraindata = new LabeledFeatureVector[traindata.size()];
		for (LabeledFeatureVector lfv: traindata) {
			svmtraindata[sID] = lfv;
			sID++;
		}
		
		TrainingParameters tp = new TrainingParameters();
//		LearnParam lp = new LearnParam();
//		lp.svm_costratio = 1;
//		tp.setLearningParameters(lp);
		return trainer.trainModel(svmtraindata, tp);
	}
	
	public double classify( Data d, SVMLightModel[] models, HashMap<String, ArrayList<String>> clusters, double set ) throws IOException {
		String line;
		
		ArrayList<String> links = new ArrayList<String>();
		ArrayList<String> pos_bigrams = new ArrayList<String>();
		ArrayList<String> trigrams = new ArrayList<String>();
		
		// read features
		BufferedReader br = new BufferedReader(new FileReader(root_path+"single_features"));
		while( (line = br.readLine()) != null)
			pos_bigrams.add(line);
		br.close();
		BufferedReader br2 = new BufferedReader(new FileReader(root_path+"single_features_links"));
		while( (line = br2.readLine()) != null)
			links.add(line);
		br2.close();
		BufferedReader br3 = new BufferedReader(new FileReader(root_path+"single_trigrams"));
		while( (line = br3.readLine()) != null)
			trigrams.add(line);
		br3.close();
		
		ArrayList<String> tst_pnt = getSVMFeatures(d, pos_bigrams, trigrams, links, clusters, set);
		FeatureVector fv = listToFeatVec(tst_pnt);
		
		if (fv == null || fv.size() == 0)
			return 0.0; // default to declarative if unknown
		
		//double[] results = new double [4];
		double[] results = new double [3];
		results[0] = models[0].classify(fv);
		results[1] = models[1].classify(fv);
		results[2] = models[2].classify(fv);
		//results[3] = models[3].classify(fv);
		
		int max_index = -1;
		double max_value = -100;
		for(int i=0;i<results.length;i++) {
			if (results[i] > max_value) {
				max_value = results[i];
				max_index = i;
			}
		}
		
		return (double) max_index;
	}
	
	public static ArrayList<String> getSVMFeatures( Data d, ArrayList<String> pos_bigrams, ArrayList<String> trigrams, ArrayList<String> links, HashMap<String, ArrayList<String>> clusters, double set ) throws IOException {
		
		/*
		Feature Sets 
		0 - POS Bigrams
		1 - POS Bigrams + Ngrams
		2 - POS Bigrams + Ngrams + Dependency Links 
		3 - POS Bigrams + Ngrams + Dependency Links + Word Clusters
		*/
		ArrayList<String> features = new ArrayList<String>();
		
		ArrayList<String> pos = d.getPos();
		ArrayList<String> tkns = d.getTkns();
		
		String pos_str = Helpers.join(pos, " ").toLowerCase();
		String mention = d.getMention();
		
		//SemanticGraph dependencies = d.getDependencies();
		ArrayList<String> depLinks = new ArrayList<String>();
		
		String temp = "";
		
//		if (dependencies != null) {
//	  		for( SemanticGraphEdge e1: dependencies.edgeIterable() )
//	        	depLinks.add(e1.getRelation().toString());	
//		}
  		
		int curr_features = 1;
		int feature_weight = 1;
		
		// is the first word a verb?
		if ( pos.get(0).equals("VB") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// is the first word a wh-word?
		if ( pos.get(0).equals("WRB") || pos.get(0).equals("WDT") || pos.get(0).equals("WP") ) 
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// presence of question words
		if ( mention.toLowerCase().matches(questionWords) )
			features.add(curr_features+":"+feature_weight);
		curr_features++;
		
		// question in constituent
//		String top = Helpers.getTopS(d.getConstituent());
//        if ( top != null && top.endsWith("Q") )
//        	features.add(curr_features+":"+feature_weight);
//    	curr_features++;
		
//		// the presence of a positive or negative word
//		if ( mention.toLowerCase().matches(sentimentWords) )
//			features.add(curr_features+":"+feature_weight);
//		curr_features++;
		
		// the presence of interrogative tag phrases
		for (String tp: tagPhrases) {
			if (mention.toLowerCase().contains(tp)) {
				features.add(curr_features+":"+feature_weight);
				break;
			}
		}
		curr_features++;
		
		// the presence of declarative meaningless phrases
		for (String up: ummPhrases) {
			if (tkns.get(0).toLowerCase().equals(up)) {
				features.add(curr_features+":"+feature_weight);
				break;
			}
		}
		curr_features++;
		
		// how many tokens are there?
//		if ( tkns.size() < 4 ) 
//			features.add(curr_features+":1");
//		curr_features++;
		
		// part of speech ngram features
		for (String pos_bi: pos_bigrams) {
			temp = "<start> "+pos_str;
			if (temp.contains(pos_bi)) 
				features.add(curr_features+":"+feature_weight);
			curr_features++;
		}
		
		// word ngram features 238
		if ( set == 1.0 || set == 2.0 || set == 3.0 || set == 4.0 ) {
			for (String tri: trigrams) {
				temp = "<start> "+mention;
				if (temp.toLowerCase().contains(tri.toLowerCase())) 
					features.add(curr_features+":"+feature_weight);
				curr_features++;
			}
		}
		
//		// dependency link features
//		if ( set == 2.0 || set == 3.0 ) {
//			ArrayList<String> dep_pairs = Helpers.getRootLinkPairs(d.getDependencies());
//			for (String link: links) {
//				if (dep_pairs.contains(link)) {
//					//System.out.println("added feature");
//					features.add(curr_features+":"+(feature_weight*2));
//				}
//				curr_features++;
//			}
//		}
		
		// word cluster features
		if ( set == 3.0 || set == 4.0 ) {
			for (String cluster: clusters.keySet()) {
				ArrayList<String> words = clusters.get(cluster);
				for (String word: words) {
					if (tkns.contains(word)) {
						features.add(curr_features+":"+feature_weight);
						break;
					}
				}
				curr_features++;
			}
		}
		
		//System.out.println("Features: " + curr_features);
		
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
