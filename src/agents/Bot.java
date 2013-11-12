package agents;

import java.io.File;

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
	    ChatterBot bot = factory.create(ChatterBotType.CLEVERBOT);
	    ChatterBotSession botsession = bot.createSession();
		//ChatterBot bot = factory.create(ChatterBotType.PANDORABOTS, "d689f7b8de347251");
	    //ChatterBotSession botsession = bot.createSession();     
	    String response = botsession.think(line).replace("Cleverbot", "Ana");
		return response;
	}
}
