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

	public Question( StanfordCoreNLP pipeline, String line, int oid, String obj, String atr, String question, String genderortense, Extract function, boolean silence ) throws IOException, JSONException {
		this.obj = obj;
		this.atr = atr;
		
		

		if (question == null)
			this.question = Helpers.genQuestion(obj, atr, genderortense);
		else 
			this.question = question;
		
		if (genderortense != null && genderortense.equals("female")) 
			this.question = Helpers.replacePronouns(Helpers.getTokens(pipeline, this.question));
		
		this.callback = new Callback(line, question, oid, obj, atr, new DateTime(), function);
		System.out.println("Question: " + this.question + ", for: " + oid);
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
