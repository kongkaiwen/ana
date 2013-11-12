package evaluation;

import java.io.IOException;

import agents.Ana;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Evaluate {
	
	public static Ana ana;
	
	public static void main(String args[]) throws Exception {
		ana = new Ana();
//		ana.ask("I am Kevin.", false);
//		ana.ask("Phil is my father.", false);
//		ana.ask("He likes to cook.", false);
//		ana.ask("He is 56 years old.", false);
//		ana.ask("Phil likes cookies.", false);
//		System.out.println(ana.getKB());
	}
	
	public Evaluate() throws IOException {
		ana = new Ana();
		ana.knowledge.initKB();
	}
	
	public String analyze(String line) throws Exception {
		
		if (line.equals("silence"))
			return ana.ask(line, true); 
		return ana.ask(line, false); 
	}
	
	public String getFact() throws IOException {
		return ana.getFact();
	}
	
	public JSONObject getKB() throws JSONException {
		JSONObject kb = new JSONObject();
		kb.put("graph", ana.getKB());
		kb.put("table", ana.getTables());
		return kb;
	}
}
