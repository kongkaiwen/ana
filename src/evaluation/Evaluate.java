package evaluation;

import java.io.IOException;

import agents.Ana;

import org.json.JSONException;
import org.json.JSONObject;

public class Evaluate {
	
	public Ana ana;
	
	public Evaluate(int scenario) throws IOException, JSONException {
		ana = new Ana();
		ana.knowledge.initKB(scenario);
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
	
	public void resetKB( int scenario ) throws IOException {
		ana.knowledge.resetKB(scenario);
	} 
}
