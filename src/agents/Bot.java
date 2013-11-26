package agents;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;

public class Bot {
	
	public static void main(String args[]) throws Exception {
		System.out.println(ask("Hello"));
	}

	public static String ask( String line ) throws Exception {
		ChatterBotFactory factory = new ChatterBotFactory();
		
		String response = null;
		ChatterBot bot = null;
		ChatterBotSession botsession = null;
		
		if (bot == null || botsession == null) {
			try {
			    bot = factory.create(ChatterBotType.CLEVERBOT);
			    botsession = bot.createSession();
			    response = botsession.think(line).replace("Apollo", "Model").replace("Cleverbot", "Model").replace("Cleverbot", "Amy");
			} catch(Exception e) {
		    	e.printStackTrace();
		    }
		}
		
		if (bot == null || botsession == null || response == null) {
			try {
			    bot = factory.create(ChatterBotType.PANDORABOTS, "838c59c76e36816b");
			    botsession = bot.createSession();
			    response = botsession.think(line).replace("Apollo", "Model").replace("Cleverbot", "Model").replace("Cleverbot", "Amy");
			} catch(Exception e) {
		    	e.printStackTrace();
		    }
		}
		
		if (bot == null || botsession == null || response == null) {
			try {
				bot = factory.create(ChatterBotType.PANDORABOTS, "86289ef30e345c10");
			    botsession = bot.createSession();
			    response = botsession.think(line).replace("Apollo", "Model").replace("Cleverbot", "Model").replace("Cleverbot", "Amy");
			} catch(Exception e) {
		    	e.printStackTrace();
		    }
		}
		
		if (response == null)
			response = "There is a problem...";
		
		return response;
	}
}
