package medical;

import java.io.IOException;
import java.util.ArrayList;

import tools.Helpers;

import jnisvmlight.FeatureVector;
import jnisvmlight.LabeledFeatureVector;
import jnisvmlight.SVMLightInterface;
import jnisvmlight.SVMLightModel;
import jnisvmlight.TrainingParameters;

public class AnaMedicalClassifier {

	public static void main(String args[]) throws IOException {
		
		AnaMedicalClassifier amc = new AnaMedicalClassifier();
		SVMLightModel model = amc.createModel();
		model.writeModelToFile("medication.dat");
		
		String line = "I am taking my medication right now.";
		amc.classify(model, line);
	}
	
	public SVMLightModel createModel() {
		
		// initialize and create proper training vectors
		SVMLightInterface trainer = new SVMLightInterface();
		ArrayList<LabeledFeatureVector> traindata = new ArrayList<LabeledFeatureVector>();
		SVMLightInterface.SORT_INPUT_VECTORS = true;
				
		TrainingParameters tp = new TrainingParameters();
		return trainer.trainModel(null, tp);
	}
	
	public double classify( SVMLightModel model, String line ) throws IOException {
		
		ArrayList<String> drugs = Helpers.loadDrugNames();
		
		ArrayList<String> tst_pnt = getSVMFeatures(line, drugs);
		FeatureVector fv = listToFeatVec(tst_pnt);
		double result = model.classify(fv);
		
		return result;
	}
	
	public ArrayList<String> getSVMFeatures( String line,  ArrayList<String> drugs ) {
		ArrayList<String> features = new ArrayList<String>();
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
}
