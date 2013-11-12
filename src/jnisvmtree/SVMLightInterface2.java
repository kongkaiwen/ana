package jnisvmtree;

import config.Settings;

public class SVMLightInterface2 {

	long modelAddress;
	
	static {
		//System.loadLibrary("svmtree");
		System.load(Settings.path + "models/jniLib/libsvmtree.so");
	}
	/**
	 * Load models. 
	 * example of argsv "svm_classify -f 1 some_test_file(Just for format use) modelfile"
	 * @param argc
	 * @param args
	 * @return
	 */
	public native long loadModel(int argc, String[] args);
	
	/**
	 * Performs a classification step as a native call to SVM-light. 
	 */
	public native double classifyNative(String line, long modeladd);
	
	
	public native boolean clearModel(long model);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String line = "-1	|BT| (NN (R )(prep_like (NN (NE ))(conj_and (NN (NE ))))) |ET| 39:1 66:1 105:1 150:1 161:1";
		String line2 = "-1	|BT| (NN (R )(prep_like (NN (NE ))(conj_and (NN (NE ))))) |ET| 39:1 63:1 66:1 105:1 150:1 161:1";
		String path = Settings.path + "models/kevin_model/";
		String model = path+"model_TandV_5";
		String testFile = path+"tb_test100_judged_withRpt_TandV_5.txt3";
		SVMLightInterface2 svmlight = new SVMLightInterface2();
		String[] args2 = {"svm_classify", "-f", "1",testFile, model};
		int argc = args2.length;
		
		long modeladd = svmlight.loadModel(argc, args2);
		if(modeladd!=-1){
			//System.out.println("read model succeed.");
		}
		double v1 = svmlight.classifyNative(line, modeladd);
		double v2 = svmlight.classifyNative(line2,modeladd);
		//System.out.println("v1="+v1+"\t"+"v2="+v2);
		svmlight.clearModel(modeladd);
	}

}
