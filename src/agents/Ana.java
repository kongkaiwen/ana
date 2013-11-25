package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kb.Callback;
import kb.Medical;
import kb.Question;
import kb.Daily;
import kb.Event;
import kb.KnowledgeBase;
import kb.Person;
import kb.Request;
import medical.AnaDrugPattern;
import medical.AnaForgotPattern;
import medical.AnaIllnessPattern;
import medical.AnaSymptomPattern;

import org.json.JSONException;
import org.json.JSONObject;

import answer.ExtractName;
import answer.ExtractRelation;
import attributes.PersonMatcher;

import relations.Entity;
import tools.Helpers;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import entities.AnaEntity;
import entities.AnaPer;
import events.EventMatcher;
import graph.AnaParseGraph;

public class Ana {

	public static KnowledgeBase knowledge;
	public static StanfordCoreNLP pipeline;
	
	public static void main(String[] args) throws Exception {
		Ana ana = new Ana();
		ana.initKB(3);
		
//		System.out.println("response: " + ana.ask("I need to buy a gift for my grandson's birthday party.", false));
//		System.out.println("response: " + ana.ask("Nathan.", false));
//		System.out.println("response: " + ana.ask("Saturday.", false));
//		System.out.println("response: " + ana.ask("He is turning 5.", false));
		
//		System.out.println("response: " + ana.ask("I'm going to a concert on Saturday with Jana.", false));
//		System.out.println("response: " + ana.ask("She is my sister.", false));
//		System.out.println("response: " + ana.ask("It's at the Shaw Conference.", false));
//		System.out.println("response: " + ana.ask("Hello.", false));
		
//		System.out.println("response: " + ana.ask("I visited my nephew yesterday.", false));
//		System.out.println("response: " + ana.ask("No.", false));
//		System.out.println("response: " + ana.ask("Jared.", false));
		
//		System.out.println("response: " + ana.ask("I visited my granddaughter yesterday.", false));
//		System.out.println("response: " + ana.ask("Jana.", false));
		
//		System.out.println("response: " + ana.ask("When is my nephew's birthday?", false));
//		System.out.println("response: " + ana.ask("Yes.", false));
		
//		System.out.println("response: " + ana.ask("I am Kevin.", false));
//		System.out.println("response: " + ana.ask("Well I'm 25 years old and I am a student at the University of Alberta.", false));
		
//		System.out.println("response: " + ana.ask("Phil is coming over for dinner.", false));
//		System.out.println("response: " + ana.ask("56.", false));
//		System.out.println("response: " + ana.ask("He will come at 6pm.", false));
		
//		System.out.println("response: " + ana.ask("My name is Kevin.", false));
//		System.out.println("response: " + ana.ask("I have a brother.", false));
//		System.out.println("response: " + ana.ask("Nathan.", false));
//		System.out.println("response: " + ana.ask("My mom's name is Ann.", false));
//		System.out.println("response: " + ana.ask("My mother.", false));
//		System.out.println("response: " + ana.ask("She is 60 years old.", false));
		
//		System.out.println("response: " + ana.ask("Hello.", false));
//		System.out.println("response: " + ana.ask("I went shopping with Jana on Tuesday.", false));
		
//		System.out.println("response: " + ana.ask("Hello.", false));
//		System.out.println("response: " + ana.ask("When is my nephew's birthday?", false));
		
//		System.out.println("response: " + ana.ask("Hello.", false));
//		System.out.println("response: " + ana.ask("When is my nephew's birthday?", false));
//		System.out.println("response: " + ana.ask("His party is this week.", false));
//		System.out.println("response: " + ana.ask("Just myself.", false));
		
//		System.out.println("response: " + ana.ask("Hello", false));
//		System.out.println("response: " + ana.ask("My father is here.", false));
//		System.out.println("response: " + ana.ask("Phil.", false));
//		System.out.println("response: " + ana.ask("He just came from Boston.", false));
		
//		System.out.println("response: " + ana.ask("I need to buy a gift for my nephew's birthday.", false));
//		System.out.println("response: " + ana.ask("He is turning 5.", false));
//		System.out.println("response: " + ana.ask("What does he like?", false));
		
//		System.out.println("response: " + ana.ask("Hello.", false));
//		System.out.println("response: " + ana.ask("I am Kevin.", false));
//		System.out.println("response: " + ana.ask("My nephew's name is Jacob.", false));
		
//		System.out.println("response: " + ana.ask("I had lunch with my granddaughter.", false));
//		System.out.println("response: " + ana.ask("Jana.", false));
//		System.out.println("response: " + ana.ask("We ate at 11am yesterday.", false));
//		System.out.println("response: " + ana.ask("Olive Garden.", false));
		
//		System.out.println("response: " + ana.ask("My son is coming over for dinner.", false));
//		System.out.println("response: " + ana.ask("Phil.", false));
//		System.out.println("response: " + ana.ask("He is 50.", false));
//		System.out.println("response: " + ana.ask("Tomorrow night.", false));
		
//		System.out.println("response: " + ana.ask("I am Kevin.", false));
//		System.out.println("response: " + ana.ask("I am a research assistant.", false));
		
//		System.out.println("response: " + ana.ask("I am Kevin.", false));
//		System.out.println("response: " + ana.ask("I'm going to a game tomorrow.", false));
//		System.out.println("response: " + ana.ask("I am 25.", false));
//		System.out.println("response: " + ana.ask("My sister.", false));
		
		System.out.println("response: " + ana.ask("I am Kevin.", false));
		System.out.println("response: " + ana.ask("I have a brother.", false));
		System.out.println("response: " + ana.ask("Nathan.", false));
		
		System.out.println(ana.knowledge.toJSON());
		System.out.println(ana.knowledge.toTableJSON());
	}
	
	public Ana () {
		Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    pipeline = new StanfordCoreNLP(props);
		knowledge = new KnowledgeBase();
	}
	
	public void initKB( int scenario ) throws IOException {
		knowledge.initKB(scenario);
	}

	public String ask ( String line, boolean silence ) throws Exception {
		
		// check for punctuation
		if (line.charAt(line.length()-1) != '.' && line.charAt(line.length()-1) != '!' && line.charAt(line.length()-1) != '?')
			line = line + ".";
		
		// init data
		int eid = -1;
		String response = "";
		knowledge.addDialogue(line);
		int linenum = knowledge.getDialogue().size();
		ArrayList<String> pos = Helpers.getPOS(pipeline, line);
		ArrayList<String> tkns = Helpers.getTokens(pipeline, line);
		ArrayList<String> drugs = Helpers.loadDrugNames();
		ArrayList<Question> potential = new ArrayList<Question>();
		SemanticGraph dependencies = Helpers.getDependencies(pipeline, line);
		AnaParseGraph apg = new AnaParseGraph( dependencies );
		
		// variables
		boolean addedPersonAttr = false;
		
		// is the utterance a statement, request, or question
		double function = Helpers.sentenceFunction( pipeline, line );
		
		// pronoun resolution
  		ArrayList<ArrayList<String>> resolutions = Helpers.pronounResolution(pipeline, line, knowledge.getDialogue());

  		// extract entities 
		ArrayList<Entity> entitiesInText = Helpers.getYingEntities( pipeline, line );
		
		// new person?
  		boolean newperson = knowledge.newEntity(tkns, entitiesInText);
  		String newpersonname = knowledge.getnewPerson(tkns, entitiesInText);

		// check the buffers
  		Callback buffer = knowledge.getCallback();
  		if (buffer != null) {

  			// asked a "which" question
  			if (buffer.getAtr().equals("which")) {
  				
  				// can be either a name or yes/no
  				ArrayList<String> whichTkns = Helpers.getTokens(pipeline, buffer.getLine());
  				ArrayList<String> whichPOS = Helpers.getPOS(pipeline, buffer.getLine());
  				
  				String modifi = "";
  				String person = "";
  				
  				for(Entity e: entitiesInText) {
  					if (e.getType().equals("PER"))
  						person = e.getName();
  				}
  				
  				// they answered with a name
  				if (!person.equals("")) {
  					Person correctPerson = knowledge.getPerson(buffer.getOID());
  					modifi = Helpers.replaceFamilyTitle(correctPerson.get("name"), whichTkns, whichPOS);
  					
  					// delete buffer
  	  				knowledge.delBuffer();
  	  				knowledge.clnBuffer();
  	  				
  	  				// reask with title replaced
  					return ask(modifi, false);
  				}
  				
  			} else {
  				boolean hasspeaker = (knowledge.getSpeaker() == null) ? false : true;
  				boolean askq = false;
  				String last_line = buffer.getLine();
  				boolean found = buffer.executeCallback(response, knowledge, tkns, entitiesInText, pos);
  				
  				// delete buffer
  				knowledge.delBuffer();
  				
  				//  q2.ask, RBuffer.add(q2.callback), QBuffer.pop()
  				if (found) {
  					Question question = knowledge.getQuestion();

  					if (question != null) {
  						// found answer, and question in buffer
  						knowledge.addResponse(line, "", question.getQuestion(), "question");
  						knowledge.addCallback(question.getCallback());
  						return question.getQuestion();
  					} else {
  						// no question in buffer
  						boolean hasspeakernow = (knowledge.getSpeaker() == null) ? false : true;
  						if (!hasspeaker && hasspeakernow) {
  							String umm = "Nice to meet you!";
  	  		  				knowledge.addResponse(line, "", "Nice to meet you!", "umm");
  	  		  				return umm;
  						}
  						
  						// check is unknown title is present
  						
  						// check for family titles ( nephew, daughter, etc )
  						boolean hasTitle = Helpers.hasFamilyTitles(tkns, pos);
  						
  						// if contains family title, try and check if that relation exists
  						if (hasTitle) {
  							// get the speaker (assume the speaker)
  							Person focus = knowledge.getSpeaker();
  							
  							// get the title
  							String title = Helpers.getFamilyTitle(tkns, pos);
  							
  							// check if the speaker has the relation
  							boolean hasRelation = knowledge.hasRelation(focus.getId(), title);
  							
  							if (hasRelation) {
  								String umm = Helpers.ummPhrase();
  		  		  				knowledge.addResponse(line, "", umm, "umm");
  		  		  				return umm;
  							}
  						} else {
  							String umm = Helpers.ummPhrase();
  	  		  				knowledge.addResponse(line, "", umm, "umm");
  	  		  				return umm;
  						}
  					}
  				} else {
  					// may have changed subject
//  					String umm = Helpers.ummPhrase();
//  	  				knowledge.addResponse(line, "", umm, "umm");
//  	  				return umm;
  				}
  			}
  		}
  		
  		// if there is silence for a period of time, ask the user a "daily" question
		if (silence) {
			Daily today = knowledge.getDaily();
			String attr = today.getEmptyAttr();
			String ques = Helpers.genSilenceQuestion(attr);
			
			Question question = new Question(pipeline, line, today.getId(), "daily", attr, ques, null, today.getCallback(attr) );
			
			knowledge.addResponse(line, knowledge.getSpeaker().get("name"), question.getQuestion(), "question");
			return ques;
		}
		
		// we don't know the speaker
		if (knowledge.getSpeaker() == null) {
			
			int pid = knowledge.addPerson();
			boolean isGreeting = Helpers.isGreeting(tkns);
			
			// check if line contains an intro (i am kevin, my name is kevin, etc)
			if ( (line.toLowerCase().contains("i am") || line.toLowerCase().contains("my name is") || line.toLowerCase().contains("i'm")) && newperson ) {
				knowledge.update(pid, "person", "name", newpersonname);
				knowledge.setSpeaker(pid);
				return "Nice to meet you!";
			}
			
			if (!isGreeting) {
				String r =  Helpers.genWhoIs();
				Question question = new Question(pipeline, line, pid, "person", "name", r, null, new ExtractName() );
				//knowledge.addQuestion(question);
				
				knowledge.addResponse(line, "", r, "question");
				knowledge.addCallback(question.getCallback());
				return r;
			} else {
				String r = Helpers.genGreeting() + "! " + Helpers.genWhoIs();
				Question question = new Question(pipeline, line, pid, "person", "name", r, null, new ExtractName() );
				//knowledge.addQuestion(question);
				
				knowledge.addResponse(line, "", r, "question");
				knowledge.addCallback(question.getCallback());
				return r;
			}
		}
		
		// check for family titles ( nephew, daughter, etc )
		boolean hasTitle = Helpers.hasFamilyTitles(tkns, pos);
		
		// if contains family title, try and check if that relation exists
		if (hasTitle) {
			// get the speaker (assume the speaker)
			Person focus = knowledge.getSpeaker();
			
			// get the title
			String title = Helpers.getFamilyTitle(tkns, pos);
			
			// check if the speaker has the relation
			boolean hasRelation = knowledge.hasRelation(focus.getId(), title);
			
			if (hasRelation) {
				
				// check if the title is linked with a name
				boolean has_link = false;
				String person_name = "";
				for(Entity e: entitiesInText) {
					if (e.getType().equals("PER")) {
						if (apg.hasLink(title, e.getName())) {
							has_link = true;
							person_name = e.getName();
						} else if ( (line.contains("name")) && (apg.hasLink(title, "name")) && (apg.hasLink("name", e.getName())) ) {
							has_link = true;
							person_name = e.getName();
						}
					}
				}
				
				if (!has_link) {
					// what if you know one son, but don't know the other?
					Person relPerson = knowledge.getPersonFromRelation(focus.getId(), title);
					
					// how many relations are there?
					int amount = knowledge.howManyRelations(focus.getId(), title);
					
					if (amount > 1) {
						// which person? 
						Question question = new Question(pipeline, line, relPerson.getId(), "person", "which", "Which <RELATION>?".replace("<RELATION>", title), relPerson.get("sex"), null );
						potential.add(question);
					} else {
						// confirm or assume
						
	//					Question question = new Question(pipeline, line, relPerson.getId(), "person", "which", relPerson.get("name")+"?".replace("<RELATION>", title), relPerson.get("sex"), null );
	//					potential.add(question);
						
						if (function == 0.0) {
							// ask about attribute
							String attr = relPerson.getEmptyAttr();
							if (attr != null) {
								Question question = new Question(pipeline, line, relPerson.getId(), "person", attr, null, relPerson.get("sex"), relPerson.getCallback(attr));		
								potential.add(question);
							}
						}
	
						if (function == 1.0) {
							String rsp = Helpers.askQuestion(knowledge, tkns, relPerson);
							return rsp;
						}
						
						if (function == 2.0) {
							
						}
						
					}
				}
			} else {
				
				int pid = -1;
				
				// check if the title is linked with a name
				boolean has_link = false;
				String person_name = "";
				for(Entity e: entitiesInText) {
					if (e.getType().equals("PER")) {
						if (apg.hasLink(title, e.getName())) {
							has_link = true;
							person_name = e.getName();
						} else if ( (line.contains("name")) && (apg.hasLink(title, "name")) && (apg.hasLink("name", e.getName())) ) {
							has_link = true;
							person_name = e.getName();
						}
					}
				}
				
				// if title is not linked to a person, add the person
				if (!has_link) {
					// My mother is Ann.
					pid = knowledge.addPerson();
					if (Helpers.isFemaleTitle(title))
						knowledge.updatePerson(pid, "sex", "female");
					
					Person p = knowledge.getPerson(pid);
					
					if (p != null) {
						// add the relation
						knowledge.addRelation(title, focus.getId(), pid);
						
						// get empty attr
						String attr = p.getEmptyAttr();
						
						// clear question buffer
						knowledge.clnBuffer();
						
						if (attr != null) {
							// add the potential question
							Question question = new Question(pipeline, line, pid, "person", attr, null, p.get("sex"), p.getCallback(attr) );
							potential.add(question);
						}
					}
				}
			}
		}
		
  		// disambiguation entities ( add the entities from the KB that represent those found in text )
		ArrayList<AnaEntity> entitiesInKB = knowledge.disambiguate( tkns, entitiesInText, resolutions, linenum );
		
		// people from the line in kb
		ArrayList<Person> peopleInKB = new ArrayList<Person>();
		for(AnaEntity ae: entitiesInKB) {
			if (ae.getType().equals("PER")) {
				Person newp = knowledge.getPerson(Integer.parseInt(ae.getId()));
				if (newp == null) {
					System.out.println("null added1");
					System.out.println(ae.getName() + ":" + ae.getId()	);
				}
				peopleInKB.add(newp);
			}
		}
		
  		// extract relations
		HashMap<String, String> relations = Helpers.getRelations( pipeline, line, linenum );
		
		// disambiguate relations
		knowledge.disambiguate(tkns, pos, resolutions, relations);
		
		// extract person attributes
		if ((peopleInKB.size() == 0 && line.toLowerCase().startsWith("i ")) || (peopleInKB.size() == 0 && (line.toLowerCase().contains("i am") || line.toLowerCase().contains("i'm"))) ) {
			// add speaker
			Person pp = knowledge.getSpeaker();
			if (pp == null) System.out.println("null added2");
			peopleInKB.add(pp);
			
			HashMap<String, String> attr = new HashMap<String, String>();
			attr.put("id", String.valueOf(pp.getId()));
			attr.put("name", pp.get("name"));
			entitiesInKB.add(new AnaPer(attr));
		}
		
		// extract person attributes
		if (peopleInKB.size() == 1) {
			ArrayList<String> matches = PersonMatcher.check(line, peopleInKB, entitiesInKB, pos, tkns, dependencies);
			for(String match: matches) {
				addedPersonAttr = true;
				// find the person in our db and assign them the school ( eid # attr # val )
				String tokens[] = match.split("#");
				knowledge.updatePerson(Integer.parseInt(tokens[0]), tokens[1], tokens[2]);
			}
			
		    if (line.toLowerCase().startsWith("i ")) {
		    	entitiesInKB.remove(entitiesInKB.size() - 1);
		    	peopleInKB.remove(0);
		    }
		}
  		
  		// detect events ( extract single word events )
		String eventName = null;
		boolean hasEvent = Helpers.hasEvent( pipeline, line );
		
		// extract event word
		if (hasEvent) {
			eventName = Helpers.getEvent(line);
			if (eventName != null) {
				
				Event event = knowledge.getEvent(eventName);  // doesn't consider duplicate events...
				if (event == null) {
					eid = knowledge.addEvent( eventName );
				} else {
					eid = event.getId();
				}
				
				Event e = knowledge.getEvent(eid);
				e.update("tense", Helpers.getEventTense(tkns, pos));
				
				// extract event attributes
				boolean updated = EventMatcher.check(e, tkns, entitiesInText, pos, dependencies);
			}
		}
		
		// user isn't feeling well
		boolean hasSymptom = AnaIllnessPattern.match(line, pos);
		if (hasSymptom) {
			Medical medical = knowledge.addMedical();
			Question question = new Question(pipeline, line, medical.getId(), "medical", "issue", "What are your symptoms?", null, medical.getCallback("issue") );
			potential.add(question);
		}
		
		// detect symptoms, I think I have a runny nose and a fever.
		String symptoms = AnaSymptomPattern.match(line);
		if (!symptoms.equals("")) {
			Medical medical = knowledge.addMedical();
			medical.update("issue", symptoms);

			return "Would you like me to call your doctor?";
		}
		
		// has the user taken medication
		boolean takenMeds = AnaDrugPattern.match(line, pos, drugs);
		if (takenMeds) {
			knowledge.addMedical( "took meds" );
			knowledge.updateDaily( "tookmeds", "true" );
			return "Noted";
		}
		
		// user forgot if medication was taken
		boolean forgotMeds = AnaForgotPattern.match(line, pos);
		if (forgotMeds) {
			String tookMeds = knowledge.getDaily("tookmeds");
			knowledge.addMedical( "forgot meds" );
			if (Boolean.valueOf(tookMeds))
				return "I have recorded that you have taken your medication today.";
			else
				return "I have no record of you taking your medication today.  Can you check with the nurse?";
		}
		
		// is the input a thanks?
		if ( Helpers.isThanks(tkns) ) {
			return Helpers.noProblem();
		}
		
		// is the input a greeting?
		if ( Helpers.isGreeting(tkns) ) {
			
			if ( Helpers.isMorning() ) {
				String replace = "";
				if (knowledge.getSpeaker() != null)
					replace = knowledge.getSpeaker().get("name");
				return "Good morning <NAME>.".replace("<NAME>", replace);
			}
			
			String replace = "";
			if (knowledge.getSpeaker() != null)
				replace = knowledge.getSpeaker().get("name");
			//response = Helpers.genGreeting() + " <NAME>.";
			//response = response.replace("<NAME>", replace);
			response = Helpers.genGreeting();
			knowledge.addResponse(line, knowledge.getSpeaker().get("name"), response, "greeting");
			
			return response;
		}
		
		// is there an event to ask about?
		if ( hasEvent ) {
			if (eventName != null) {
				String tense = Helpers.getEventTense(tkns, pos);
				boolean askq = true;
				
				// which attribute to ask about? get the first person?
				Event e = knowledge.getEvent(eid);
				knowledge.updateEvent(e.getId(), "tense", tense);
				if (askq) {
					// add potential question
					String emptyAttr1 = e.getEmptyAttr();
					if (emptyAttr1 != null) {
						Question question = new Question(pipeline, line, e.getId(), "event", emptyAttr1, null, tense, e.getCallback(emptyAttr1) );
						potential.add(question);
						
						e.update(emptyAttr1, "temp");
						
						// add another potential question
						String emptyAttr2 = e.getEmptyAttr();
						if (emptyAttr2 != null) {
							Question question2 = new Question(pipeline, line, e.getId(), "event", emptyAttr2, null, tense, e.getCallback(emptyAttr2) );
							potential.add(question2);
						}
						
						e.update(emptyAttr1, "");
					}
				}
			}
		}
		
		// is there an person to ask about?
		if ( peopleInKB.size() > 0 || (peopleInKB.size() == 0 && line.toLowerCase().startsWith("i") && !hasTitle)  ) {
			
			peopleInKB.add(knowledge.getPerson(0));

			boolean askq = true;
			
			ArrayList<String> people = new ArrayList<String>();
			
			// Are there any people in the utterance?
			for (AnaEntity ae: entitiesInKB) {
				if (ae.getType().equals("PER")) {
					people.add(ae.getName());
				}
			}
			
			// which attribute to ask about? get the first person?
			if (askq) {
						
				// get first person in line
				Person target = knowledge.getPerson(peopleInKB.get(0).get("name"));

				if (target != null) {
					// add potential question
					String attr1 = target.getEmptyAttr();
					if (attr1 != null) {
						Question question = new Question(pipeline, line, target.getId(), "person", attr1, null, target.get("sex"), target.getCallback(attr1) );
						potential.add(question);
						
						target.update(attr1, "temp");
						
						// add another potential question
						String attr2 = target.getEmptyAttr();
						if (attr2 != null) {
							Question question2 = new Question(pipeline, line, target.getId(), "person", attr2, null, target.get("sex"), target.getCallback(attr2) );
							potential.add(question2);
						}
						
						target.update(attr1, "");
					}
				}
			}
		}
		
		// is there a new person?
		if ( newperson ) {
			if ( !(Helpers.join(pos, " ").contains("PRP$ NN") && hasTitle) ) {
				Person cand = knowledge.getPerson(newpersonname);
				Question question = new Question(pipeline, line, cand.getId(), "person", "who", "Who is <NAME>?".replace("<NAME>", newpersonname), cand.get("sex"), new ExtractRelation() );
				potential.add(question);
			}
		}
		
		// get default response
		//Bot bot = new Bot();
		//response = bot.ask(line);
		
  		// if request -> attempt to resolve request
		if ( function == 2.0 ) {
			// could be call doctor, reminder, scheduling, call family member, etc
			String phrase = Helpers.getReqPhrase(line);
			
			if (phrase == null)
				knowledge.addRequest("request");
			else
				knowledge.addRequest(phrase);
			
			return Helpers.reqPhrase();
		}
		
		
		// if question -> attempt to answer
		if ( function == 1.0 ) {
			
			System.out.println("question");
			
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
			
			if (peopleInKB.size() > 0) { 
				// assume question is about person in KB
				
				if (line.toLowerCase().contains("what") && line.toLowerCase().contains("buy")) {
					String answer = knowledge.get(peopleInKB.get(0).getId(), "person", "likes");
					String prettyAnswer = peopleInKB.get(0).formulateResponse("likes", answer);
					
					knowledge.addResponse(line, "", prettyAnswer, "answer");
					return prettyAnswer;
				}
				
				// attr has to be precise
				String attr = Helpers.getAttr(tkns);
				
				if (attr == null) {
					knowledge.addResponse(line, "", "I'm not sure.", "answer");
					return "I'm not sure.";
				}
				
				String answer = knowledge.get(peopleInKB.get(0).getId(), "person", attr);
				if (answer.equals("")) {
					knowledge.addResponse(line, "", "I don't know, sorry.", "answer");
					return "I don't know, sorry.";
				}
				
				String prettyAnswer = peopleInKB.get(0).formulateResponse(attr, answer);
				
				// add response
				knowledge.addResponse(line, "", prettyAnswer, "answer");
				return prettyAnswer;
			}
		}
		
  		// if statement -> attempt to response or send to chatterbot
		if ( function == 0.0 ) {
			//response = getFact();
			
			// if no people mentioned, no relations, no events, then...
			
			if (!hasEvent && relations.size() == 0 && peopleInKB.size() == 0 && !addedPersonAttr) {
				// try to move the conversation
				
				// return "Tell me about yourself.";
			}
		}
		
		// rank questions
		//System.out.println("potential.size(): " + potential.size());
		ArrayList<Question> willask = Helpers.rank(potential);
		//System.out.println("willask.size(): " + willask.size());
		
		// if no questions
		if (willask.size() == 0)
			return Helpers.ummPhrase();
		
		// q1.ask, RBuffer.add(q1.callback), QBuffer.pop()
		Question qone = willask.get(0);
		if (willask.size() > 1) {
			// add to buffer
			Question qtwo = willask.get(1);
			knowledge.addQuestion(qtwo);
		}
		
		// add to buffer
		knowledge.addCallback(qone.getCallback());

		// add response
		knowledge.addResponse(line, "", qone.getQuestion(), "question");
		
		//System.out.println("here");
		return qone.getQuestion();
	}
	
	public String getFact() throws IOException {
		return knowledge.getFact();
	}
	
	public JSONObject getKB() throws JSONException {
		return knowledge.toJSON();
	}
	
	public JSONObject getTables() throws JSONException {
		return knowledge.toTableJSON();
	}
	
	public KnowledgeBase getKnowledge() {
		return knowledge;
	}
}
