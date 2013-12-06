package kb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import answer.Extract;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import tools.Helpers;
import tools.Text;

public class Question {
	
	private int oid;
	private double distance;
	private String obj, atr, val, question;
	private Callback callback;

	public Question( StanfordCoreNLP pipeline, ArrayList<String> tkns, int oid, String obj, String atr, String question, String genderortense, Extract function, String val, HashMap<String, ArrayList<Double>> vectors, ArrayList<String> stp, ArrayList<String> pos, ArrayList<String> ent ) throws IOException, JSONException {
		this.obj = obj;
		this.atr = atr;
		this.oid = oid;
		this.val = val;
		
		Text gQuestion = null;
		String line = Helpers.join(tkns, " ");
		
		if (question == null) {
			if (oid == 0 && obj.equals("person"))
				gQuestion = Helpers.genQuestion(tkns, pos, ent, obj, atr, genderortense, 2, pipeline, vectors, stp);
			else
				gQuestion = Helpers.genQuestion(tkns, pos, ent, obj, atr, genderortense, 3, pipeline, vectors, stp);
			ArrayList<String> filteredTkns1 = Helpers.filterString(tkns, pos, ent, stp);
			ArrayList<String> filteredTkns2 = Helpers.filterString(gQuestion.getTkn(), gQuestion.getPos(), gQuestion.getEnt(), stp);

			this.question = gQuestion.getLine();
			this.distance = Helpers.sDistance(filteredTkns1, filteredTkns2, vectors);
		} else {
			this.question = question;
			this.distance = -3.14;
		}
		
		if (genderortense != null && genderortense.equals("female")) 
			this.question = Helpers.replacePronouns(Helpers.getTokens(pipeline, this.question));
		
		if (line.contains("sister") || line.contains("mother") || line.contains("mom") || line.contains("grandmother") || line.contains("grandma") || line.contains("aunt") || line.contains("daughter") || line.contains("granddaughter") || line.contains("neice"))
			this.question = Helpers.replacePronouns(Helpers.getTokens(pipeline, this.question));
		
		this.callback = new Callback(line, question, oid, obj, atr, new DateTime(), function, val);
	}
	
	public double genDistance() {
		return this.distance;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public Callback getCallback() {
		return callback;
	}
	
	public String getObj() {
		return obj;
	}
	
	public String getAtr() {
		return atr;
	}
	
	public String getVal() {
		return val;
	}
	
	public int getOID() {
		return oid;
	}
}
