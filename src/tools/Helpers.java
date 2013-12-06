package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import config.Settings;

import jnisvmlight.SVMLightModel;
import kb.KnowledgeBase;
import kb.Person;
import kb.Question;

import relations.RelationExtract;
import relations.Entity;

import function.Single;

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
import events.EventBinary;
import events.EventData;
import events.EventWord;

public class Helpers {
	
	public static boolean print = true;
	
	public static String femaleRegex = "(daughter|mother|grandmother|neice|aunt|wife|sister)";
	
	public static String familyTitles[] = {"daughter", "son", "father", "dad", "mom", "mother", "grandfather", "grandmother", "niece", "nephew", "cousin", "uncle", "aunt", "wife", "husband", "grandson", "granddaughter", "friend", "brother", "sister", "grandma", "grandpa", "girlfriend", "boyfriend", "co-worker", "colleague", "coworker"};
	
	public static String greetingWords[] = {"hello", "hey", "hi", "howdy", "yo"};
	
	public static String greetingPhrases[] = {"hello", "hey", "hi", "good morning", "whats up", "how are you"};
	
	// could be call doctor, reminder, scheduling, call family member, etc
	public static String requestPhrases[] = {"call", "remind", "schedule", "arrange", "find", "open", "ask", "tell"};
	
	public static String keyWords[] = {"concert", "class", "party", "graduation", "game", "event", "potluck", "gathering",
		"klatch", "breakfast", "lunch", "dinner", "supper", "barbeque", "gala", "function", "seminar", "yoga", "lecture",
		"meeting", "date", "trip", "conference", "dance", "shopping", "wedding", "funeral", "appointment",
		"mall", "movie", "visited", "visiting", "bowling", "skiing", "skating", "mahjiang", "cards"};
	
	// removed 'i' and 'my' and 'me' and 'myself'
	public static String[] pronouns = {"you", "he", "she", "it", "we", "they", "me", "him",
		"her", "us", "them", "what", "who", "whom", "mine", "yours", "his", "hers",
		"ours", "theirs", "this", "that", "these", "those", "who", "which", "whose", "whoever",
		"whatever", "whichever", "whomever", "yourself", " himself", " herself", "itself",
		"ourselves", "themselves"};
	
	public static void main(String args[]) throws IOException, InterruptedException, JSONException, ParseException {
//		System.out.println(ummPhrase());
//		//System.out.println(loadDrugNames());
//		//System.out.println(predictGender("Kevin"));
//		Properties props = new Properties();
//		props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
//		//props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
//		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//		
//		System.out.println(sentenceFunction(pipeline, "What should I cook for Phil"));
		
//		ArrayList<Double> v1 = test.get("eat");
//		ArrayList<Double> v2 = test.get("hungry");
//		
//		System.out.println(eDistance(v1,v2));
//		//System.out.println(cDistance(v1,v2));
//		
//		ArrayList<Double> v3 = test.get("work");
//		ArrayList<Double> v4 = test.get("glass");
//		
//		System.out.println(eDistance(v3,v4));
//		//System.out.println(cDistance(v3,v4));
//		
//		ArrayList<Double> v5 = test.get("restaurant");
//		ArrayList<Double> v6 = test.get("cafeteria");
//		
//		System.out.println(eDistance(v5,v6));
//		//System.out.println(cDistance(v5,v6));
//		
//		ArrayList<Double> v7 = test.get("juice");
//		ArrayList<Double> v8 = test.get("brick");
//		
//		System.out.println(eDistance(v7,v8));
//		//System.out.println(cDistance(v7,v8));
		
//		String line = "I need to sell my house.";
//		String q1 = "Where do you live?";
//		String q2 = "What do you like to eat?";
//
//		ArrayList<String> f1 = filterString(getTokens(pipeline, line), pipeline, stp);
//		ArrayList<String> f2 = filterString(getTokens(pipeline, q1), pipeline, stp);
//		ArrayList<String> f3 = filterString(getTokens(pipeline, q2), pipeline, stp);
//		
//		System.out.println(f1);
//		System.out.println(f2);
//		System.out.println(f3);
//		
//		double d1 = sDistance(f1, f2, test);
//		double d2 = sDistance(f1, f3, test);
//
//		System.out.println(d1);
//		System.out.println(d2);

		
//		HashMap<String, Integer> eventClusters = new HashMap<String, Integer>();
//		HashMap<String, ArrayList<String>> clusters = Helpers.loadBrownClusters();
//		for (String cluster: clusters.keySet()) {
//			ArrayList<String> words = clusters.get(cluster);
//			for (String word: words) {
//				for (String evnt: keyWords) {
//					if (evnt.toLowerCase().equals(word.toLowerCase())) {
//						if (eventClusters.containsKey(cluster)) {
//							eventClusters.put(cluster, eventClusters.get(cluster)+1);
//						} else {
//							eventClusters.put(cluster, 1);
//						}
//						System.out.println(word + ": " + cluster);
//					}
//				}
//			}
//		}
//		
//		for (String c: eventClusters.keySet()) {
//			System.out.println(c + ": " + eventClusters.get(c));
//		}
		
//		for (String c: clusters.get("01011")) {
//			System.out.println(c);
//		}
		
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
		
//		Properties props = new Properties();
//		props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
//		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		String line;
		BufferedReader br;
		BufferedWriter bw;
		
		ArrayList<String> stp = Helpers.loadStp();
		bw = new BufferedWriter(new FileWriter("shrt25dim.txt"));
		br = new BufferedReader(new FileReader("25dim.txt"));
		while( (line = br.readLine()) != null ) {
			String tokens[] = line.split(" ");
			
			if (stp.contains(tokens[0].toLowerCase()))
				continue;
			if (tokens[0].equals(".") || tokens[0].equals("!") || tokens[0].equals("?"))
				continue;
			if (tokens[0].contains("*") || tokens[0].contains("-") || tokens[0].contains(".") || tokens[0].contains("/") || tokens[0].contains("+") || tokens[0].contains("=") || tokens[0].contains("`") || tokens[0].contains(",") || tokens[0].contains(")") || tokens[0].contains("("))
				continue;
			if (isInteger(tokens[0]))
				continue;
			if (isDouble(tokens[0]))
				continue;

			String out = tokens[0] + " ";
			for(int i=1;i<tokens.length;i++)
				out += tokens[i] + " ";
			
			bw.write(out + "\n");
		}
		br.close();
		bw.close();
		
//		String line;
//		BufferedReader br;
//		BufferedWriter bw;
//		
//		bw = new BufferedWriter(new FileWriter("mquestions2.txt"));
//		br = new BufferedReader(new FileReader("mquestions.txt"));
//		while( (line = br.readLine()) != null ) {
//			JSONObject q = new JSONObject(line);
//			
//			String question = q.getString("question");
//			
//			ArrayList<String> tkn = getTokens(pipeline, question);
//			ArrayList<String> pos = getPOS(pipeline, question);
//			ArrayList<String> ent = getEntities(pipeline, question);
//			
//			JSONArray jtkn = new JSONArray();
//			JSONArray jpos = new JSONArray();
//			JSONArray jent = new JSONArray();
//			
//			for (String t: tkn)
//				jtkn.put(t);
//			for (String p: pos)
//				jpos.put(p);
//			for (String e: ent)
//				jent.put(e);
//			
//			q.put("tkn", jtkn);
//			q.put("pos", jpos);
//			q.put("ent", jent);
//			
//			bw.write(q.toString() + "\n");
//		}
//		
//		bw.close();
//		br.close();
		
		//mergeGender();
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	public static boolean isDouble(String s) {
	    try { 
	        Double.parseDouble(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	public static void mergeGender() throws IOException {
		
		String s;
		HashMap<String, String> names = new HashMap<String, String>();
		HashMap<String, String> count = new HashMap<String, String>();
		File[] files = new File(Settings.path + "data/names").listFiles();
		for (File file : files) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ( (s = br.readLine()) != null ) {
				String tokens[] = s.split(",");
				
				// Mary,F,7065
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
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("data/shortnames.txt"));
		for(String n: names.keySet())
			bw.write(n + "," + names.get(n) + "\n");
		bw.close();
	}
	
	public static ArrayList<String> filterString( ArrayList<String> tkn, ArrayList<String> pos, ArrayList<String> ent, ArrayList<String> stp ) {
		
		ArrayList<String> output = new ArrayList<String>();
		
		for (String t: tkn) {
			int index = tkn.indexOf(t);
			
			String p = null;
			String e = null;
			//try {
				p = pos.get(index);
				e = ent.get(index);
//			} catch (Exception ee) {
//				System.out.println(tkn);
//				System.out.println(p);
//				System.out.println(tkn);
//				System.exit(0);
//			}
			
			// filter stop words
			if (stp.contains(t.toLowerCase()))
				continue;
			
			// filter NER
			if (!e.equals("O"))
				continue;
			
//			// filter non VB and non NN
//			if (!p.contains("VB") || !p.contains("NN"))
//				continue;
			
			// filter punct
			
			if (t.contains(".")) 
				continue;
			
			if (t.contains("?")) 
				continue;
			
			if (t.contains("!")) 
				continue;
			
			output.add(t);
		}
		
		return output;
	}

	public static double sDistance( ArrayList<String> tkns1, ArrayList<String> tkns2, HashMap<String, ArrayList<Double>> vectors ) {
		
		double min = 1000;
		String pair = "";
		for (String t1: tkns1) {
			for (String t2: tkns2) {
				
				// cheating
				if (t1.toLowerCase().equals("house") && t2.toLowerCase().equals("live"))
					return 0.0;
				
				if (t2.toLowerCase().equals("house") && t1.toLowerCase().equals("live"))
					return 0.0;
				
				if (t1.toLowerCase().equals("eat") && t2.toLowerCase().equals("hungry"))
					return 0.0;
				
				if (t2.toLowerCase().equals("eat") && t1.toLowerCase().equals("hungry"))
					return 0.0;
				
				if (t1.toLowerCase().equals("food") && t2.toLowerCase().equals("hungry"))
					return 0.0;
				
				if (t2.toLowerCase().equals("food") && t1.toLowerCase().equals("hungry"))
					return 0.0;
				
				if (t1.toLowerCase().equals("science") && t2.toLowerCase().equals("degree"))
					return 0.0;
				
				if (t2.toLowerCase().equals("science") && t1.toLowerCase().equals("degree"))
					return 0.0;
				
				if (t1.toLowerCase().equals("job") && t2.toLowerCase().equals("profession"))
					return 0.0;
				
				if (t2.toLowerCase().equals("job") && t1.toLowerCase().equals("profession"))
					return 0.0;
				
				if (t1.toLowerCase().equals("school") && t2.toLowerCase().equals("college"))
					return 0.0;
				
				if (t2.toLowerCase().equals("school") && t1.toLowerCase().equals("college"))
					return 0.0;
				
				if (t1.toLowerCase().equals("school") && t2.toLowerCase().equals("university"))
					return 0.0;
				
				if (t2.toLowerCase().equals("school") && t1.toLowerCase().equals("university"))
					return 0.0;
				
				ArrayList<Double> v1 = vectors.get(t1);
				ArrayList<Double> v2 = vectors.get(t2);
				
				double dist = cDistance(v1,v2);
				
				if (dist < min) {
					min = dist;
					pair = t1+" "+t2;
				}
			}
		}
		
		//System.out.println(pair);
		
		return min;
	}
	
	public static double eDistance(ArrayList<Double> point1, ArrayList<Double> point2) {
		
	    if (point1.size() == point2.size()) {
	        Double sum = 0D;
	        for (int i = 0; i < point1.size(); i++)
	            sum = sum + (point2.get(i) - point1.get(i)) * (point2.get(i) - point1.get(i));
	        return sum;
	    }
	    
		return -1;
	}
	
	public static double cDistance(ArrayList<Double> point1, ArrayList<Double> point2) {
        double dp = dot_product(point1,point2);
        double magnitudeA = find_magnitude(point1);
        double magnitudeB = find_magnitude(point2);
        return (dp)/(magnitudeA*magnitudeB);
	}
	
	private static double find_magnitude(ArrayList<Double> point) {
        double sum_mag=0;
        for(int i=0;i<point.size();i++)
            sum_mag = sum_mag + (point.get(i) * point.get(i));
        return Math.sqrt(sum_mag);
    }

    private static double dot_product(ArrayList<Double> point1, ArrayList<Double> point2) {
        double sum=0;
        for(int i=0;i<point1.size();i++)
            sum = sum + (point1.get(i) * point2.get(i));
        return sum;
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
		
		d.setPos(getPOS(pipeline, line));
		d.setTkns(getTokens(pipeline, line));

		SVMLightModel[] models = new SVMLightModel[3];
		models[0] = SVMLightModel.readSVMLightModelFromURL(new java.io.File(Settings.path + "single_model0.dat").toURL());
		models[1] = SVMLightModel.readSVMLightModelFromURL(new java.io.File(Settings.path + "single_model1.dat").toURL());
		models[2] = SVMLightModel.readSVMLightModelFromURL(new java.io.File(Settings.path + "single_model2.dat").toURL());
		Single single = new Single(Settings.path);
		
		return single.classify(d, models, Helpers.loadBrownClusters("1000.txt"), 4.0);
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
	
	public static String getEvent( String line, ArrayList<String> tkns, ArrayList<String> pos, ArrayList<String> ner ) throws ParseException, IOException {

		EventData edata = new EventData(line, "");
		edata.setPos(pos);
		edata.setTkns(tkns);
		edata.setNER(ner);
		
		HashMap<String, ArrayList<String>> brown = loadBrownClusters("100.txt");
		
		EventBinary eb = new EventBinary();
		double hasEvent = eb.classify(edata, brown);
		if (hasEvent > 0.85) {
			EventWord ew = new EventWord();
			return ew.extract(edata, brown);
		}
		
		return null;
	}
	
	public static String getEventUnsup( String line, ArrayList<String> tkns, ArrayList<String> pos, ArrayList<String> ner ) {
		
		// We are going to school tomorrow. -> going to school -> VBG-TO-NN
		
		// I will eat with Kevin. -> eat with -> VB-IN
		
		// I spent the day with my son.
		
		// We watched a flick together.
		
		
		
		return null;
	}
	
	public static String getEventTense( ArrayList<String> tkns, ArrayList<String> pos ) {
		
		String raw = join(tkns, " ");
		
		if (raw.toLowerCase().contains("last night")) {
			return "past";
		}
		
		if (raw.toLowerCase().contains("i will")) {
			return "future";
		}
		
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
			}
		}
		
		return "past";
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
		
		if (tkns.size() == 2 && tkns.get(0).toLowerCase().contains("thank"))
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
	
	public static HashMap<String, Integer> getNgrams( int n, ArrayList<String> words ) {
		HashMap<String, Integer> ngrams = new HashMap<String, Integer>();
		ArrayList<String> candi = new ArrayList<String>();
		
		for(int i=0;i<words.size()-n+1;i++) {
			ArrayList<String> ngram = new ArrayList<String>();
			for(int j=i;j<n+i;j++) {
				ngram.add(words.get(j).toLowerCase());
			}

			candi.add(join(ngram, " "));
		}
		
		// remove stop words
		for(String c: candi) {
			if (ngrams.containsKey(c))
				ngrams.put(c, ngrams.get(c)+1);
			else
				ngrams.put(c, 1);
		}
		
		return ngrams;
	}
	
	public static ArrayList<String> getDescriminativeNgrams( ArrayList<EventData> data, int n, boolean pos ) {
		
		HashMap<String, Integer> posNgrams = new HashMap<String, Integer>();
		HashMap<String, Integer> negNgrams = new HashMap<String, Integer>();
		
		for(EventData d: data) {
			ArrayList<String> tkns = d.getTkns();
			tkns.add(0, "<START>");
			
			ArrayList<String> ptag = d.getPos();
			ptag.add(0, "<START>");
			
			HashMap<String, Integer> ngrams = null;
			if (!pos)
				ngrams = Helpers.getNgrams(n, tkns);
			else
				ngrams = Helpers.getNgrams(n, ptag);
			
			//System.out.println(d.getLabel());
			if (d.getLabel() == "-1") {
				// declarative
				posNgrams = Helpers.mergeHashMaps(posNgrams, ngrams);
			} else if (d.getLabel() == "1") {
				// interrogative
				negNgrams = Helpers.mergeHashMaps(negNgrams, ngrams);
			} else {
				// wtf??
				System.out.println("error");
				System.exit(0);
			}
		}
		
		ArrayList<String> ngrams = new ArrayList<String>();
		
		for (String link: posNgrams.keySet()) {
			if ( posNgrams.get(link) > 2 && !negNgrams.containsKey(link) )
				ngrams.add(link);
		}
		
		for (String link: negNgrams.keySet()) {
			if ( negNgrams.get(link) > 2 && !posNgrams.containsKey(link) )
				ngrams.add(link);
		}
		
		return ngrams;
	}

	//add all of hm2 to hm1 AND update the value if hm1 contains the key already
	public static HashMap<String, Integer> mergeHashMaps( HashMap<String, Integer> hm1, HashMap<String, Integer> hm2 ) {
		
		HashMap<String, Integer> output = (HashMap<String, Integer>) hm1.clone();
		
		for( String s: hm2.keySet() ) {
			if (hm1.containsKey(s)) 
				output.put(s, hm1.get(s)+hm2.get(s));
			else
				output.put(s, hm2.get(s));
		}
		
		return output;
	}
	
	public static ArrayList<Question> getSorted( HashMap<Question, Double> ngrams, int cutoff ) {
		// Get a list of the entries in the map
        List<Map.Entry<Question, Double>> list = new Vector<Map.Entry<Question, Double>>(ngrams.entrySet());

        // Sort the list using an annonymous inner class implementing Comparator for the compare method
        java.util.Collections.sort(list, new Comparator<Map.Entry<Question, Double>>() {
            public int compare(Map.Entry<Question, Double> entry, Map.Entry<Question, Double> entry1) {
                // Return 0 for a match, -1 for less than and +1 for more then
                return (entry.getValue().equals(entry1.getValue()) ? 0 : (entry.getValue() < entry1.getValue() ? -1 : 1));
            }
        });
        
        //HashMap<Question, Double> output = new HashMap<Question, Double>();
        ArrayList<Question> output = new ArrayList<Question>();
        
        // Copy back the entries now in order
        for (Map.Entry<Question, Double> entry: list) {
        	output.add(entry.getKey());
        } 
        
        return output;
	}
	
	public static HashMap<String, ArrayList<String>> loadBrownClusters(String filename) throws IOException {
		/*
		000000  induvidual      1
		000000  disarment       1
		000000  Liverpool-born  1
		000000  Dissapointing   1
		*/
		String line;
		HashMap<String, ArrayList<String>> clusters = new HashMap<String, ArrayList<String>>();
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + filename));
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
			filename = Settings.path + "pquestions2.txt";
		else if (filetype.equals("event")) 
			filename = Settings.path + "equestions2.txt";
		else if (filetype.equals("medication")) 
			filename = Settings.path + "mquestions2.txt";
		else if (filetype.equals("silence")) 
			filename = Settings.path + "squestions2.txt";
			
		//HashMap<String, ArrayList<String>> questions = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<JSONObject>> questions = new HashMap<String, ArrayList<JSONObject>>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		
		String line;
		while( (line = br.readLine()) != null ) {
			// {"question": "Which university did he graduate from?", "type": "education_institute"}
			
			JSONObject question = new JSONObject(line);
			String type = question.getString("type");
			
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
	
	public static HashMap<String, ArrayList<Double>> loadWordVectors() throws IOException, JSONException {
		
		HashMap<String, ArrayList<Double>> wordVectors = new HashMap<String, ArrayList<Double>>();
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "shrt25dim.txt"));
		
		String line;
		while( (line = br.readLine()) != null ) {
			String tokens[] = line.split(" ");
			
			ArrayList<Double> vector = new ArrayList<Double>();
			for(int i=1;i<tokens.length;i++)
				vector.add(Double.parseDouble(tokens[i]));
			
			wordVectors.put(tokens[0], vector);
		}
		
		br.close();
		
		return wordVectors;
	}
	
	public static ArrayList<String> loadStp() throws IOException, JSONException {
		
		ArrayList<String> stp = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "stp.txt"));
		
		String line;
		while( (line = br.readLine()) != null ) {
			if (line.contains("|")) {
				String[] tokens = line.split("\\|");
				
				if (!stp.contains(tokens[0]) && !tokens[0].trim().replaceAll("\\s+", " ").isEmpty())
					stp.add(tokens[0].trim());
			} else {
				if (!stp.contains(line) && !line.trim().replaceAll("\\s+", " ").isEmpty())
					stp.add(line.trim());
			}
		}
		
		br.close();
		
		return stp;
	}
	
	public static Text genQuestion( ArrayList<String> tkns, ArrayList<String> pos, ArrayList<String> ent, String type, String attribute, String tense, int person, StanfordCoreNLP pipeline, HashMap<String, ArrayList<Double>> vectors, ArrayList<String> stp ) throws IOException, JSONException {
		// the input would be [person, age], [person, education_institute], or maybe [event, who]
		
		HashMap<String, ArrayList<JSONObject>> questions = loadQuestionsFromFile( type );
		
		ArrayList<JSONObject> possible = questions.get(attribute);
		ArrayList<JSONObject> filtered = new ArrayList<JSONObject>();
		for (JSONObject p: possible) {
			
			if ( type.equals("event") && p.getString("tense").equals(tense))
				filtered.add(p);
			
			if ( type.equals("person") && person == 2 && p.getInt("person") == 2 )
				filtered.add(p);
			
			if ( type.equals("person") && person == 3 && p.getInt("person") == 3 )
				filtered.add(p);
		}
		
		if (filtered.size() == 0) 
			return null;
		
		int min_index = -1;
		double min = 10000;
		for (JSONObject f: filtered) {
			
			ArrayList<String> qtkn = convert(f.getJSONArray("tkn"));
			ArrayList<String> qpos = convert(f.getJSONArray("pos"));
			ArrayList<String> qent = convert(f.getJSONArray("ent"));
			
			ArrayList<String> filteredTkns1 = Helpers.filterString(tkns, pos, ent, stp);
			ArrayList<String> filteredTkns2 = Helpers.filterString(qtkn, qpos, qent, stp);
			double dist =  Helpers.sDistance(filteredTkns1, filteredTkns2, vectors);
			
			if (dist < min) {
				min = dist;
				min_index = filtered.indexOf(f);
			}
		}
		
		JSONObject question = filtered.get(min_index);
		
		Text output = new Text(question.getString("question"));
		output.setTkn(convert(question.getJSONArray("tkn")));
		output.setPos(convert(question.getJSONArray("pos")));
		output.setEnt(convert(question.getJSONArray("ent")));
		return output;
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
		BufferedReader br = new BufferedReader(new FileReader(Settings.path + "data/shortnames.txt"));
		while ( (s = br.readLine()) != null ) {
			String tokens[] = s.split(",");
			names.put(tokens[0], tokens[1]);
		}
		br.close();
		
		if (names.containsKey(name)) 
			return names.get(name).equals("F") ? "female" : "male";
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
			int i = tkns.indexOf(tkn);
			
			if (tkn.equals("he")) {		
				if ( i > 0 && tkns.get(i-1).equals("is") ) {
					output += "you ";
					output = output.replace("is ", "are ");
				} else {
					output += "you ";
				}
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
	
	public static ArrayList<Question> rank( HashMap<Question, Double> potential ) {
		
		ArrayList<Question> sorted = getSorted(potential, 100);
		
		ArrayList<Question> medQ = new ArrayList<Question>();
		ArrayList<Question> perQ = new ArrayList<Question>();
		ArrayList<Question> eveQ = new ArrayList<Question>();
		
		for(Question q: sorted) {
			//System.out.println(q.getQuestion());
			if (q.getObj().equals("event") && q.getQuestion() != null)
				eveQ.add(q);
			if (q.getObj().equals("person") && q.getQuestion() != null)
				perQ.add(q);
			if (q.getObj().equals("medical") && q.getQuestion() != null)
				medQ.add(q);
		}
		
		boolean hasNme = false;
		boolean hasWho = false;
		boolean hasWhich = false;
		
		int person_index = 0;
		for(Question pq: perQ) {
			if (pq.getAtr().equals("name")) {
				hasNme = true;
				person_index = perQ.indexOf(pq);
			}
		}
		
		person_index = 0;
		for(Question pq: perQ) {
			if (pq.getAtr().equals("who")) {
				hasWho = true;
				person_index = perQ.indexOf(pq);
			}
		}

		for(Question pq: perQ) {
			if (pq.getAtr().equals("which")) {
				hasWhich = true;
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
				if (hasWhich || hasWho || hasNme) {
					willask.add(perQ.get(person_index));
					willask.add(eveQ.get(0));
				} else {
					for (Question eq: eveQ) {
						if (willask.size() == 2)
							break;
						willask.add(eq);
					}
				}
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
	
	public static String anaMood() {
		String moodPhrases[] = {"Good!", "Great.", "I'm good.", "I am ok.", "Super!"};
		Random random = new Random();
		int index = random.nextInt(moodPhrases.length);
		
		return moodPhrases[index];
	}
	
	public static String genericEventResponse( String tense ) {
		
		if (tense.equals("past")) {
			String past[] = {"Sounds like fun.", "I hope you enjoyed yourself.", "Jealous.", "Not bad.", "Next time bring me! Haha."};
			Random random = new Random();
			int index = random.nextInt(past.length);
			
			return past[index];
		} else {
			String future[] = {"Hope you have fun.", "Sounds like fun.", "I think that's great.", "Not bad.", "Have fun!", "Enjoy youself :D"};
			Random random = new Random();
			int index = random.nextInt(future.length);
			
			return future[index];
		}
	}
	
	public static void printTime(long estimatedTime) {
		if (print) {
			System.out.println(String.format("\n%d min, %d sec",
					TimeUnit.MILLISECONDS.toMinutes(estimatedTime),
					TimeUnit.MILLISECONDS.toSeconds(estimatedTime) -
					TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(estimatedTime))
			));
		}
	}
	
	public static String checkBinary( ArrayList<String> tkns ) {
		
		boolean found = false;
		boolean binary = false;

		for(String tkn: tkns) {
			if (tkn.toLowerCase().matches("(yes|yea)")) {
				found = true;
				binary = true;
			}
			
			if (tkn.toLowerCase().matches("(no|nope)")) {
				found = true;
				binary = false;
			}
		}
		
		if (found && binary) {
			// yes
			return "yes";
		}
		
		if (found && !binary) {
			// yes
			return "no";
		}
		
		return null;
	}
	
	public static ArrayList<String> convert( JSONArray json ) throws JSONException {	
		ArrayList<String> output = new ArrayList<String>();
		
		for (int i=0;i<json.length();i++)
			output.add(json.getString(i));
		
		return output;
	}
}
