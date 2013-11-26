package kb;

import java.io.IOException;

import org.joda.time.DateTime;
import org.json.JSONException;

import answer.Extract;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import tools.Helpers;

public class Question {
	
	private String obj, atr, question;
	private Callback callback;

	public Question( StanfordCoreNLP pipeline, String line, int oid, String obj, String atr, String question, String genderortense, Extract function ) throws IOException, JSONException {
		this.obj = obj;
		this.atr = atr;

		if (question == null) {
			if (oid == 0)
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
		
		this.callback = new Callback(line, question, oid, obj, atr, new DateTime(), function);
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
}
