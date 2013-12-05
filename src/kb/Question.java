package kb;

import java.io.IOException;

import org.joda.time.DateTime;
import org.json.JSONException;

import answer.Extract;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import tools.Helpers;

public class Question {
	
	private int oid;
	private String obj, atr, val, question;
	private Callback callback;

	public Question( StanfordCoreNLP pipeline, String line, int oid, String obj, String atr, String question, String genderortense, Extract function, String val ) throws IOException, JSONException {
		this.obj = obj;
		this.atr = atr;
		this.oid = oid;
		this.val = val;

		if (question == null) {
			if (oid == 0 && obj.equals("person"))
				this.question = Helpers.genQuestion(obj, atr, genderortense, 2);
			else
				this.question = Helpers.genQuestion(obj, atr, genderortense, 3);
		} else {
			this.question = question;
		}
		
		if (genderortense != null && genderortense.equals("female")) 
			this.question = Helpers.replacePronouns(Helpers.getTokens(pipeline, this.question));
		
		if (line.contains("sister") || line.contains("mother") || line.contains("mom") || line.contains("grandmother") || line.contains("grandma") || line.contains("aunt") || line.contains("daughter") || line.contains("granddaughter") || line.contains("neice"))
			this.question = Helpers.replacePronouns(Helpers.getTokens(pipeline, this.question));
		
		this.callback = new Callback(line, question, oid, obj, atr, new DateTime(), function, val);
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
