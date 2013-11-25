package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import attributes.PersonMatcher;

import config.Settings;

import jnisvmlight.SVMLightModel;
import kb.KnowledgeBase;
import kb.Person;
import kb.Question;

import relations.RelationExtract;
import relations.Entity;

import methods.Single;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefChain.CorefMention;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;
import events.AnaEventModel;

public class Helpers {
	
	public static String femaleRegex = "(daughter|mother|grandmother|neice|aunt|wife)";
	
	public static String familyTitles[] = {"daughter", "son", "father", "dad", "mom", "mother", "grandfather", "grandmother", "niece", "nephew", "cousin", "uncle", "aunt", "wife", "husband", "grandson", "granddaughter", "friend", "brother", "sister", "grandma", "grandpa"};
	
	public static String greetingWords[] = {"hello", "hey", "hi", "howdy", "yo"};
	
	public static String greetingPhrases[] = {"hello", "hey", "hi", "good morning", "whats up", "how are you"};
	
	// could be call doctor, reminder, scheduling, call family member, etc
	public static String requestPhrases[] = {"call", "remind", "schedule", "arrange", "find", "open", "ask", "tell"};
	
	public static String keyWords[] = {"concert", "class", "party", "graduation", "game", "event", "potluck", "gathering",
		"klatch", "breakfast", "lunch", "dinner", "supper", "barbeque", "gala", "function", "seminar", "yoga", "lecture",
		"meeting", "date", "trip", "conference", "dance", "shopping", "function", "wedding", "funeral", "appointment",
		"mall", "movie", "visited", "visiting", "bowling", "skiing", "skating", "mahjiang", "cards"};
	
	// removed 'i' and 'my' and 'me' and 'myself'
	public static String[] pronouns = {"you", "he", "she", "it", "we", "they", "me", "him",
		"her", "us", "them", "what", "who", "whom", "mine", "yours", "his", "hers",
		"ours", "theirs", "this", "that", "these", "those", "who", "which", "whose", "whoever",
		"whatever", "whichever", "whomever", "yourself", " himself", " herself", "itself",
		"ourselves", "themselves"};
	
	public static void main(String args[]) throws IOException, InterruptedException, JSONException, ParseException {
		System.out.println(ummPhrase());
		//System.out.println(loadDrugNames());
		//System.out.println(predictGender("Kevin"));
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
		//props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP( props );
		String line = "I went shopping with Jana on Tuesday.";
		
		ArrayList<Entity> ents = getYingEntities(pipeline, line);
		for (Entity e: ents) {
			System.out.println(e.getName());
		}
		
//		HashMap<String, String> relations = getRelations( pipeline, "She is my sister.",  0 );
//		for (String k: relations.keySet()) {
//			System.out.println(k + ": " + relations.get(k));
//		}

//		ArrayList<Entity> entitiesInText = Helpers.getYingEntities( pipeline, "I need to buy a gift for my grandson's birthday." );
//		for(Entity e: entitiesInText) 
//			System.out.println(e.getName() + ":" + e.getType());
//		
//		sentenceFunction( pipeline, "What do you want?" );
		
//		ArrayList<String> dialogue = new ArrayList<String>();
//		dialogue.add("Phil is my father.");
//		dialogue.add("He is a good teacher.");
//		
//		String line = "He is a good teacher.";
//		System.out.println(pronounResolution( pipeline, line, dialogue ));
		
//		String line = "Phil is my father.";
//		HashMap<String, String> relations = getRelations( pipeline, line );
//		for(String key: relations.keySet()) {
//			System.out.println(key + "::::: " + relations.get(key));
//		}
		
//		ArrayList<String> ana = new ArrayList<String>();
//		ana.add("Phil is my father.");
//		ana.add("He likes to cook.");
//		ana.add("Kevin is his son.");
//		System.out.println(pronounResolution( pipeline, "Kevin is his son.", ana ));
		
//		ArrayList<String> ana = new ArrayList<String>();
//		ana.add("I went shopping with Sarah today.");
//		ana.add("Who is Sarah?");
//		ana.add("She is my daughter.");
//		ana.add("How old is she?");
//		ana.add("She is 24.");
//		System.out.println(pronounResolution( pipeline, "She is 24.", ana ));
		
//		System.out.println(predictGender("Sarah"));
//		Response r = new Response(0);
//		System.out.println(r.get("created"));
		
//		String line = "I went to lunch with my daughter.";
//		HashMap<String, String> relations = Helpers.getRelations( pipeline, line, 0 );
//		for(String r: relations.keySet()) {
//			System.out.println(r + ": " + relations.get(r));
//		}
		
//		KnowledgeBase kb = new KnowledgeBase();
//		kb.initKB();
//		System.out.println(kb.getPerson(0).get("education_institute"));
//		
//		String ques = "Where does she go to school?";
//		String line = "The University of Alberta.";
//		ArrayList<String> tkn = getTokens(pipeline, line);
//		ArrayList<String> pos = getPOS(pipeline, line);
//		ArrayList<Entity> ent = getYingEntities(pipeline, line);
//		ExtractOrganization ed = new ExtractOrganization();
//		ed.execute(0, "person", "education_institute", kb, tkn, ent, pos);
//		
//		System.out.println(kb.getPerson(0).get("education_institute"));
	}
	
	public static String join(ArrayList<String> list, String delimiter) {
	    StringBuilder result = new StringBuilder();
	    for (Iterator<String> i = list.iterator(); i.hasNext();) {
	        result.append(i.next());
	        if (i.hasNext()) {
	            result.append(delimiter);
	        }
	    }
	    return result.toString();
	}
	
	public static double sentenceFunction( StanfordCoreNLP pipeline, String line ) throws ParseException, IOException {
		
		Data d = new Data(line, "0.0");
		
		ArrayList<String> pos = getPOS(pipeline, line);
		//pos.set(0, "<START>");
		d.setPos(pos);
		d.setTkns(getTokens(pipeline, line));
		
		SVMLightModel[] models = new SVMLightModel[3];
		models[0] = SVMLightModel.readSVMLightModelFromURL(new java.io.File(Settings.path + "single_model0.dat").toURL());
		models[1] = SVMLightModel.readSVMLightModelFromURL(new java.io.File(Settings.path + "single_model1.dat").toURL());
		models[2] = SVMLightModel.readSVMLightModelFromURL(new java.io.File(Settings.path + "single_model2.dat").toURL());
		
		Single single = new Single(Settings.path);
		return single.classify(d, models, Helpers.loadBrownClusters(), 4.0);
	}
	
	public static ArrayList<String> getTokens( StanfordCoreNLP pipeline, String line ) {
		
		ArrayList<String> tokens = new ArrayList<String>();
		
		Annotation document = new Annotation(line);
	    pipeline.annotate(document);
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	    for(CoreMap sentence: sentences) {
	        for (CoreLabel token: sentence.get(TokensAnnotation.class))
	        	tokens.add(token.get(TextAnnotation.class));
	    }
	        
		return tokens;
	}

	public static ArrayList<String> getPOS( StanfordCoreNLP pipeline, String line ) {
		
		ArrayList<String> pos = new ArrayList<String>();
		
		Annotation document = new Annotation(line);
	    pipeline.annotate(document);
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	    for(CoreMap sentence: sentences) {
	        for (CoreLabel token: sentence.get(TokensAnnotation.class))
	        	pos.add(token.get(PartOfSpeechAnnotation.class));
	    }
	        
		return pos;
	}

	public static ArrayList<String> getEntities( StanfordCoreNLP pipeline, String line ) {
		ArrayList<String> entities = new ArrayList<String>();
		
		Annotation document = new Annotation(line);
	    pipeline.annotate(document);
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	    for(CoreMap sentence: sentences) {
	        for (CoreLabel token: sentence.get(TokensAnnotation.class))
				entities.add(token.get(NamedEntityTagAnnotation.class));
	    }
	        
		return entities;
	}
	
	public static ArrayList<Entity> getYingEntities( StanfordCoreNLP pipeline, String line ) {
	    ArrayList<String> neList = new ArrayList<String>();
	    ArrayList<String> posList = new ArrayList<String>();
	    ArrayList<String> tokenList = new ArrayList<String>();
	    ArrayList<Entity> new_entities = new ArrayList<Entity>();
	    
	    Annotation document = new Annotation(line);
	    pipeline.annotate(document);
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	    for(CoreMap sentence: sentences) {
	        for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String pos = token.get(PartOfSpeechAnnotation.class);
				String ne = token.get(NamedEntityTagAnnotation.class);  
				
				neList.add(ne);
				posList.add(pos);
				tokenList.add(word);
	        }
	        
	        // ying's NE, consider multi word entities
			
	        int position = 0;
			String name = "";
			String lastStanfordType = "O";
			for(int i=0;i<neList.size();i++) {
				String ne = neList.get(i);
				String word = tokenList.get(i);
				
				if ( lastStanfordType.equals("O") ) {
					if(!ne.equals("O")){
						name = word;
					}
				} else {
					if( ne.equals("O") ) {
						new_entities.add(createEntity(name, lastStanfordType));
					} else {
						if( ne.equals(lastStanfordType) && !word.equals(".") ) {
							name += " " + word;
						}
					}
				
					if( !ne.equals(lastStanfordType) && !ne.equals("O") ) {
						new_entities.add(createEntity(name,  lastStanfordType));
						name = word;
					}
				}
				lastStanfordType = ne;
				position++;
			}
			// verify mention ending at the last token
			if(!lastStanfordType.equals("O") && !lastStanfordType.equals(".")){
				int endEntity = position-1;
				Entity entity = createEntity(name,  lastStanfordType);
				new_entities.add(entity);
			}
	    }
	    
	    return new_entities;
	}
	
	public static SemanticGraph getDependencies( StanfordCoreNLP pipeline, String line ) {
		
		SemanticGraph dependencies = null;
		
		Annotation document = new Annotation(line);
	    pipeline.annotate(document);
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	    for(CoreMap sentence: sentences) {
	    	dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
	    }
		
	    return dependencies;
	}
	
	/*
	input: utterance
	output: map of [ mention1:start:end;mention2:start:end => relation ] 
	example: "My father is Phil" => {my:0:0;Phil:3:3=famy}
	*/
	public static HashMap<String, String> getRelations( StanfordCoreNLP pipeline, String line, int linenum ) {

	    String path = Settings.path + "models/kevin_model/";
		String stopFile = path+"smallStopWords.txt";
		String modelPath = path;
		
		HashMap<String, String> reMap = null;		
		try {
			RelationExtract relationExtractor = new RelationExtract(stopFile, modelPath, pipeline);
			Annotation newCOREFdocument = new Annotation( line );
		    pipeline.annotate(newCOREFdocument);
			reMap = relationExtractor.extractRE_trad(newCOREFdocument, null);
			relationExtractor.clearModels();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HashMap<String, String>();
		}
		
		// add line number
		HashMap<String, String> addlinenum = new HashMap<String, String>();
		for(String k: reMap.keySet()) {
			String nkey = k + ";" + String.valueOf(linenum);
			String val = reMap.get(k);
			addlinenum.put(nkey, val);
		}

		return addlinenum;	
	}
	
	// assumes there is only one person :P
	public static String getPersonEntity( StanfordCoreNLP pipeline, String line ) {
		
		Annotation document = new Annotation(line);
	    pipeline.annotate(document);
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	    for(CoreMap sentence: sentences) {
	        for (CoreLabel token: sentence.get(TokensAnnotation.class))
				if (token.get(NamedEntityTagAnnotation.class).equals("PERSON"))
					return token.get(TextAnnotation.class);
	    }
	        
		return null;
	}
	
	public static String getFamilyTitle( ArrayList<String> tkns, ArrayList<String> pos ) {
		
		for(String tkn: tkns) {
			for(String title: familyTitles) {
				if (tkn.toLowerCase().equals(title))
					return tkn;
			}
		}
		
		return null;
	}
	
	public static String replaceFamilyTitle( String person, ArrayList<String> tkns, ArrayList<String> pos ) {

		int cindex = 0;
		for(String p: pos) {
			cindex = pos.indexOf(p);
			
			// for example: my son -> Kevin
			if (p.equals("PRP$") && pos.get(cindex+1).equals("NN")) {
				tkns.remove(cindex);
				tkns.set(cindex, person);
				break;
			}
		}
		
		return join(tkns, " ");
	}
	
	public static String containsFamilyTitle( String tkn ) {
		
		for(String title: familyTitles) {
			if (tkn.toLowerCase().contains(title))
				return title;
		}
		
		return null;
	}
	
	public static boolean hasFamilyTitles( ArrayList<String> tkns, ArrayList<String> pos ) {
		
		boolean hasTitle = false;
		for(String tkn: tkns) {
			for(String title: familyTitles) {
				if (tkn.toLowerCase().equals(title))
					hasTitle = true;
			}
		}
		
		return hasTitle;
	}
	
	public static boolean hasEvent( StanfordCoreNLP pipeline, String line ) throws ParseException, IOException {
		
		ArrayList<String> neList = new ArrayList<String>();
		ArrayList<String> posList = new ArrayList<String>();
	    ArrayList<String> tknList = new ArrayList<String>();
	    
	    Annotation document = new Annotation(line);
	    pipeline.annotate(document);
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    for(CoreMap sentence: sentences) {
	        for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	        	String word = token.get(TextAnnotation.class);
				String pos = token.get(PartOfSpeechAnnotation.class);
				String ne = token.get(NamedEntityTagAnnotation.class);  
				
				neList.add(ne);
				posList.add(pos);
				tknList.add(word);
	        }
	    }
		
		AnaEventModel eve = new AnaEventModel();
		double value = eve.classify(line, posList, tknList, neList);
		
		if (value > 0)
			return true;
		return false;
	}
	
	public static String getEvent( String line ) {
		for (String e: keyWords) {
			if (line.toLowerCase().contains(e)) {
				return e;
			}
		}
		return null;
	}
	
	public static String getEventTense( ArrayList<String> tkns, ArrayList<String> pos ) {
		
		
		for (String t: tkns) {
			if (t.toLowerCase().equals("yesterday")) {
				return "past";
			} else if (t.toLowerCase().equals("tomorrow")) {
				return "future";
			}
		}
		
		for (String e: pos) {
			if (e.toLowerCase().equals("vbd")) {
				return "past";
			} else if (e.toLowerCase().equals("vbg")) {
				return "future";
			} else if (e.toLowerCase().equals("vb")) {
				return "future";
			}
		}
		
		return null;
	}
	
	public static Entity createEntity(String name, String stanfordType){
		
		String myType=Entity.NONE;
		
		if(stanfordType.equals("PERSON"))
			myType = Entity.PERSON;
		
		if(stanfordType.equals("ORGANIZATION"))
			myType = Entity.ORGANIZATION;
		
		if(stanfordType.equals("LOCATION"))
			myType = Entity.LOCATION;
		
		if(stanfordType.equals("DATE"))
			myType = Entity.DATE;
		
		if(stanfordType.equals("GPE"))
			myType = Entity.GPE;
		
		if(stanfordType.equals("MISC"))
			myType = Entity.MISC;

		if(stanfordType.equals("MONEY"))
			myType = Entity.MONEY;
		
		if(stanfordType.equals("PERCENT"))
			myType = Entity.PERCENT;
		
		if(stanfordType.equals("TIME"))
			myType = Entity.TIME;
		
		if(stanfordType.equals("NUMBER"))
			myType = "NUM";
		
		if(stanfordType.equals("DURATION"))
			myType = "DUR";
		
		
		String id = name.toUpperCase() + "#" + myType;
		
		return new Entity(id, name, myType);
	}
	
	public static boolean isGreeting( ArrayList<String> tkns ) {
		
		String raw = join(tkns, " ").toLowerCase();
		for(String gp: greetingPhrases) {
			if (raw.equals(gp))
				return true;
		}
		
		for(String tkn: tkns) {
			for(String gp: greetingWords) {
				if (tkn.toLowerCase().equals(gp))
					return true;
			}
		}
		
		for(String gw: greetingWords) {
			if (tkns.size() < 4 && tkns.contains(gw)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isThanks( ArrayList<String> tkns ) {
		
		String raw = join(tkns, " ").toLowerCase();
		
		if (raw.toLowerCase().contains("thank") && raw.toLowerCase().contains("ana")) 
			return true;
		
		if (raw.toLowerCase().contains("thank") && raw.toLowerCase().contains("you")) 
			return true;
		
		return false;
	}
	
	public static String genGreeting() {
		String phrases[] = {"Hello", "Hey", "Hi", "Hiya"};
		
		Random random = new Random();
		int index = random.nextInt(phrases.length);
		
		return phrases[index];
	}
	
	public static String genWhoIs() {
		String phrases[] = {"Who are you?", "What is your name?"};
		
		Random random = new Random();
		int index = random.nextInt(phrases.length);
		
		return phrases[index];
	}
	
	public static boolean isMorning() {
		int from = 500;
	    int to = 1100;
	    Date date = new Date();
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
	    return to > from && t >= from && t <= to || to < from && (t >= from || t <= to);
	}
	
	public static ArrayList<ArrayList<String>> pronounResolution ( StanfordCoreNLP pipeline, String line, ArrayList<String> dialogue ) {
		/*
		For pronoun resolution, everytime a pronoun is found a "guess" should be made as to which
		entity that mention belongs to.  Once this guess is made we substitute the pronoun with
		the proposed entity.
		*/
		
	    boolean has_pronoun = false;
		for(String i: pronouns) {
			if ( line.toLowerCase().contains(i) ) {
				has_pronoun = true;
			}
		}
		
		ArrayList<ArrayList<String>> anaMentions = new ArrayList<ArrayList<String>>();
	    if ( has_pronoun ) { 	
	    	
		    Annotation COREFdocument = new Annotation(StringUtils.join(dialogue, " "));
		    pipeline.annotate(COREFdocument);
		    
		    
		    /*
		    My guess: each index of graph.get(i) is an entity, and the graph.get(i).getCorefMentions() are all of the entity mentions
		    */
		    
		    int count = 0;
		    Map<Integer, CorefChain> graph = COREFdocument.get(CorefChainAnnotation.class);
		    for(Integer key: graph.keySet()) {
		    	CorefChain crc = graph.get(key);

		    	if ( crc != null && crc.getMentionsInTextualOrder().size() != 0 ) {
		    		ArrayList<String> mentions = new ArrayList<String>();
		    		for (CorefMention c: crc.getMentionsInTextualOrder())
		    			mentions.add(c.mentionSpan + ":" +c.sentNum+":"+c.headIndex);
		    		anaMentions.add(mentions);
		    	}
		    } 
	    }
	    
	    return anaMentions;
	}
	
	public static ArrayList<String> loadFacts() throws IOException {
		String line;
		ArrayList<String> output = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "data/facts.txt"));
		while ( (line = br.readLine()) != null ) {		
			if (line.equals(""))
				continue;
			output.add(line.trim());
		}
		
		br.close();
		return output;
	}
	
	public static ArrayList<String> loadGreetings() throws IOException {
		String line;
		ArrayList<String> output = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "data/greetings.txt"));
		while ( (line = br.readLine()) != null ) {		
			if (line.equals(""))
				continue;
			output.add(line.trim());
		}
		
		br.close();
		return output;
	}
	
	public static ArrayList<String> loadSymptoms() throws IOException {
		String line;
		ArrayList<String> output = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "data/symptoms.txt"));
		while ( (line = br.readLine()) != null ) {		
			if (line.equals(""))
				continue;
			output.add(line.trim());
		}
		
		br.close();
		return output;
	}
	
	public static ArrayList<String> loadDrugNames() throws IOException {
		String line;
		ArrayList<String> output = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "data/drugs.txt"));
		while ( (line = br.readLine()) != null ) {		
			if (line.equals(""))
				continue;
			output.add(line.trim());
		}
		
		br.close();
		return output;
	}
	
	public static HashMap<String, ArrayList<String>> loadBrownClusters() throws IOException {
		/*
		000000  induvidual      1
		000000  disarment       1
		000000  Liverpool-born  1
		000000  Dissapointing   1
		*/
		String line;
		HashMap<String, ArrayList<String>> clusters = new HashMap<String, ArrayList<String>>();
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "1000.txt"));
		while ( (line = br.readLine()) != null ) {
			String[] tokens = line.split("\t");
			
			int count = Integer.parseInt(tokens[2]);
			if (count < 5)
				continue;
			String cluster = tokens[0];
			String word = tokens[1];
			
			ArrayList<String> words;
			if (clusters.containsKey(cluster)) {
				words = clusters.get(cluster);
			} else {
				words = new ArrayList<String>();
			}
			words.add(word);
			clusters.put(cluster, words);
		}
		br.close();
		return clusters;
	}
	
	public static HashMap<String, ArrayList<JSONObject>> loadQuestionsFromFile( String filetype ) throws IOException, JSONException {
		
		String filename = null;
		if (filetype.equals("person")) 
			filename = Settings.path + "pquestions.txt";
		else if (filetype.equals("event")) 
			filename = Settings.path + "equestions.txt";
		else if (filetype.equals("medication")) 
			filename = Settings.path + "mquestions.txt";
		else if (filetype.equals("silence")) 
			filename = Settings.path + "squestions.txt";
			
		//HashMap<String, ArrayList<String>> questions = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<JSONObject>> questions = new HashMap<String, ArrayList<JSONObject>>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		
		String line;
		while( (line = br.readLine()) != null ) {
			// {"question": "Which university did he graduate from?", "type": "education_institute"}
			
			JSONObject question = new JSONObject(line);
			String type = question.getString("type");
//			String text = question.getString("question");
//			String tense = question.getString("tense");
//			String event = question.getString("event");
			
			ArrayList<JSONObject> qs = ( questions.containsKey(type) ) ? questions.get(type) : new ArrayList<JSONObject>();
			qs.add(question);
			questions.put(type, qs);
		}
		
		br.close();
		
		return questions;
	}
	
	public static HashMap<String, ArrayList<String>> loadAttributes() throws IOException, JSONException {
			
		//HashMap<String, ArrayList<String>> questions = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> attributes = new HashMap<String, ArrayList<String>>();
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "data/attributes.txt"));
		
		String line;
		while( (line = br.readLine()) != null ) {
			// {"education_institute": ["school", "attend", "go"]}
			
			JSONObject attribute = new JSONObject(line);
			for (Iterator iter = attribute.keys(); iter.hasNext(); ) {
				Object obj = iter.next();
				
				String key = (String) obj;
				JSONArray jsonWords = attribute.getJSONArray(key);
				ArrayList<String> strWords = new ArrayList<String>();
				
				for ( int i = 0; i < jsonWords.length(); i++ )
					strWords.add(jsonWords.getString(i));
				
				attributes.put(key, strWords);
			}
		}
		
		br.close();
		
		return attributes;
	}
	
	public static String genQuestion( String type, String attribute, String tense ) throws IOException, JSONException {
		// the input would be [person, age], [person, education_institute], or maybe [event, who]
		
		HashMap<String, ArrayList<JSONObject>> questions = loadQuestionsFromFile( type );
		
		ArrayList<JSONObject> possible = questions.get(attribute);
		ArrayList<JSONObject> filtered = new ArrayList<JSONObject>();
		for (JSONObject p: possible) {
			if ( !type.equals("event"))
				filtered.add(p);
			
			if ( type.equals("event") && p.getString("tense").equals(tense))
				filtered.add(p);
		}
		
		Random rand = new Random();
		int index = rand.nextInt(possible.size());
		
		JSONObject question = possible.get(index);
		return question.getString("question");
	}
	
	public static String askQuestion( KnowledgeBase kb, ArrayList<String> tkns, Person person ) throws IOException, JSONException {
			
		String line = join(tkns, " ");
		
		/*
		What should I cook?  
		*/
		if (line.toLowerCase().contains("what") && line.contains("cook")) {
			return Helpers.foodSuggestion();
		}
		
		/*
		What is your name? 
		*/
		if (line.toLowerCase().contains("what") && line.contains("your") && line.contains("name")) {
			return Helpers.introduceAna();
		}
		
		/*
		How are you?
		*/
		if (line.toLowerCase().contains("how") && line.contains("are") && line.contains("you")) {
			return "Good!";
		}
		
		/*
		A: Phil is coming here for dinner tomorrow.
		B: What will you eat?
		A: I'm not sure.  What does he like?
		
		A: Is Kevin's birthday tomorrow? -> When is Kevin's birthday?
		*/
		
		
		if (line.toLowerCase().contains("what") && line.toLowerCase().contains("buy")) {
			String answer = kb.get(person.getId(), "person", "likes");
			String prettyAnswer = person.formulateResponse("likes", answer);
			
			kb.addResponse(line, "", prettyAnswer, "answer");
			return prettyAnswer;
		}
		
		// attr has to be precise
		String attr = getAttr(tkns);
		
		if (attr == null) {
			kb.addResponse(line, "", "I'm not sure.", "answer");
			return "I'm not sure.";
		}
		
		String answer = kb.get(person.getId(), "person", attr);
		if (answer.equals("")) {
			kb.addResponse(line, "", "I don't know, sorry.", "answer");
			return "I don't know, sorry.";
		}
		
		String prettyAnswer = person.formulateResponse(attr, answer);
		
		// add response
		kb.addResponse(line, "", prettyAnswer, "answer");
		return prettyAnswer;
	}
	
	public static String genSilenceQuestion( String attribute ) throws IOException, JSONException {
		// the input would be [person, age], [person, education_institute], or maybe [event, who]
		
		HashMap<String, ArrayList<JSONObject>> questions = loadQuestionsFromFile( "silence" );
		
		Random rand = new Random();
		ArrayList<JSONObject> possible = questions.get(attribute);
		int index = rand.nextInt(possible.size());
		
		JSONObject question = possible.get(index);
		return question.getString("question");
	}
	
	public static String predictGender( String name ) throws IOException {
		
		String s;
		HashMap<String, String> names = new HashMap<String, String>();
		HashMap<String, String> count = new HashMap<String, String>();
		File[] files = new File(Settings.path + "data/names").listFiles();
		for (File file : files) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ( (s = br.readLine()) != null ) {
				String tokens[] = s.split(",");
				if (names.containsKey(tokens[0])) {
					if ( Integer.parseInt(tokens[2]) > Integer.parseInt(count.get(tokens[0]))) {
						names.put(tokens[0], tokens[1]);
						count.put(tokens[0], tokens[2]);
					}
				} else {
					names.put(tokens[0], tokens[1]);
					count.put(tokens[0], tokens[2]);
				}
			}
			br.close();
	    }
		
		if ( names.keySet().contains(name) ) {
			return names.get(name).equals("F") ? "female" : "male";
		}
        return "male";
	}
	
	public static String replacePronouns( ArrayList<String> tkns ) {

		String output = "";	
		for(String tkn: tkns) {
			if (tkn.equals("he")) {
				output += "she ";
			} else if (tkn.equals("his")) {
				output += "her ";
			} else if (tkn.equals("him")) {
				output += "her ";
			} else {
				output += tkn + " ";
			}	
		}
		return output;
	}
	
	public static String replacePronounsYou( ArrayList<String> tkns ) {

		String output = "";	
		for(String tkn: tkns) {
			if (tkn.equals("he")) {
				output += "you ";
			} else if (tkn.equals("his")) {
				output += "your ";
			} else if (tkn.equals("she")) {
				output += "you ";
			} else if (tkn.equals("her")) {
				output += "your ";
			} else {
				output += tkn + " ";
			}	
		}
		return output;
	}
	
	public static int minuteDiff( Date d1, Date d2 ) {
		DateTime sDate = new DateTime(d1); 
		DateTime eDate = new DateTime(d2); //current date
		Minutes diff = Minutes.minutesBetween(sDate, eDate);
		return diff.getMinutes();
	}
	
	public static boolean isFemaleTitle( String title ) {
		if (title.matches(femaleRegex))
			return true;
		return false;
	}
	
	public static String getAttr( ArrayList<String> tkns ) throws IOException, JSONException {
		
		/*
		What does he like?
		Is Kevin's birthday tomorrow? -> birthday -> date_of_birth
		*/
		
		HashMap<String, ArrayList<String>> attributes = Helpers.loadAttributes();
		for (String key: attributes.keySet()) {
			ArrayList<String> keywords = attributes.get(key);
			for (String tkn: tkns) {
				for (String kw: keywords) {
					if (tkn.toLowerCase().equals(kw)) {
						return key;
					}
				}
			}
		}
		
		return null;
	}
	
	public static ArrayList<Question> rank( ArrayList<Question> potential ) {
		
		ArrayList<Question> medQ = new ArrayList<Question>();
		ArrayList<Question> perQ = new ArrayList<Question>();
		ArrayList<Question> eveQ = new ArrayList<Question>();
		
		for(Question q: potential) {
			if (q.getObj().equals("event"))
				eveQ.add(q);
			if (q.getObj().equals("person"))
				perQ.add(q);
			if (q.getObj().equals("medical"))
				medQ.add(q);
		}
		
		int person_index = 0;
		for(Question pq: perQ) {
			if (pq.getAtr().equals("who")) {
				person_index = perQ.indexOf(pq);
			}
		}

		ArrayList<Question> willask = new ArrayList<Question>();
		
		// choose at most two questions, if theres a medical question it must be included
		if (medQ.size() > 0) {
			willask.add(medQ.get(0));
		
			if ( perQ.size() > 0 && eveQ.size() > 0) {
				willask.add(perQ.get(person_index));
			} else if (perQ.size() > 0 && eveQ.size() == 0) {
				willask.add(perQ.get(person_index));
			} else if (perQ.size() == 0 && eveQ.size() > 0) {
				willask.add(eveQ.get(0));
			}
		} else {
			if ( perQ.size() > 0 && eveQ.size() > 0) {
				willask.add(perQ.get(person_index));
				willask.add(eveQ.get(0));
			} else if (perQ.size() > 0 && eveQ.size() == 0) {
				willask.add(perQ.get(person_index));
				perQ.remove(person_index);
				for (Question pq: perQ) {
					if (willask.size() == 2)
						break;
					willask.add(pq);
				}
			} else if (perQ.size() == 0 && eveQ.size() > 0) {
				for (Question eq: eveQ) {
					if (willask.size() == 2)
						break;
					willask.add(eq);
				}
			}
		}
		
		return willask;
	}
	
	public static String ummPhrase() {
		String ummPhrases[] = {"I see.", "Nice.", "Oh ok.", "Interesting.", "Sure.", "Ooook.", "Alright."};
		Random random = new Random();
		int index = random.nextInt(ummPhrases.length);
		
		return ummPhrases[index];
	}
	
	public static String getReqPhrase( String line ) {
		
		for(String s: requestPhrases) {
			if (line.toLowerCase().contains(s))
				return s;
		}
		
		return null;
	}
	
	public static String reqPhrase() {
		String reqPhrases[] = {"Yes I can.", "Sure thing.", "Okie Dokie.", "Right away.", "Sure.", "Ok.", "If I must."};
		Random random = new Random();
		int index = random.nextInt(reqPhrases.length);
		
		return reqPhrases[index];
	}
	
	public static String foodSuggestion() {
		String reqPhrases[] = {"How about chicken?", "Fish?", "Beef?", "How about pasta?"};
		Random random = new Random();
		int index = random.nextInt(reqPhrases.length);
		
		return reqPhrases[index];
	}
	
	public static String introduceAna() {
		String intro[] = {"I'm Ana.", "Ana.", "My name is Ana.", "Most call me Ana."};
		Random random = new Random();
		int index = random.nextInt(intro.length);
		
		return intro[index];
	}
	
	public static String noProblem() {
		String reqPhrases[] = {"You are welcome.", "No problem.", "You're welcome.", "Any time.", "No worries."};
		Random random = new Random();
		int index = random.nextInt(reqPhrases.length);
		
		return reqPhrases[index];
	}
}
