package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import kb.Callback;
import kb.Frame;
import kb.Medical;
import kb.Question;
import kb.Daily;
import kb.Event;
import kb.KnowledgeBase;
import kb.Person;
import medical.AnaDrugPattern;
import medical.AnaForgotPattern;
import medical.AnaIllnessPattern;
import medical.AnaSymptomPattern;

import org.json.JSONException;
import org.json.JSONObject;

import answer.ExtractBinary;
import answer.ExtractName;
import answer.ExtractRelation;
import attributes.AttributeMatcher;

import relations.Entity;
import tools.Helpers;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import entities.AnaEntity;
import events.EventMatcher;
import graph.AnaParseGraph;

public class Ana {

	public KnowledgeBase knowledge;
	public StanfordCoreNLP pipeline;
	
	private ArrayList<String> stp, drugs;
	private HashMap<String, ArrayList<Double>> vectors;
	public static long startTime;
	
	public static void main(String[] args) throws Exception {
		startTime = System.currentTimeMillis();
		
		Ana ana = new Ana();
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
		ana.initKB(3);
		
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
//		System.out.println("response: " + ana.ask("I need to buy a gift for my nephew's birthday party.", false));
//		System.out.println("response: " + ana.ask("Nathan.", false));
//		System.out.println("response: " + ana.ask("Saturday.", false));
//		System.out.println("response: " + ana.ask("He is turning 5.", false));
		
//		System.out.println("response: " + ana.ask("I'm going to a concert on Saturday with Jana.", false));
//		System.out.println("response: " + ana.ask("She is my sister.", false));
//		System.out.println("response: " + ana.ask("It's at the Shaw Conference.", false));
		
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
		
//		System.out.println("response: " + ana.ask("Phil was educated well.", false));
//		System.out.println("response: " + ana.ask("He is a computer programmer.", false));
		
//		System.out.println("response: " + ana.ask("Phil is thinking about buying a house.", false));
//		System.out.println("response: " + ana.ask("Phil lives by the beach.", false));
//		System.out.println("response: " + ana.ask("I am Kevin.", false));
//		System.out.println("response: " + ana.ask("I need to sell my house.", false));
		
//		System.out.println("response: " + ana.ask("Wendy likes school.", false));
//		System.out.println("response: " + ana.ask("She is with Alphavector.", false));
//		System.out.println("response: " + ana.ask("University of Alphavector.", false));
//		System.out.println("response: " + ana.ask("No.", false));
		
//		System.out.println("response: " + ana.ask("Hello.", false));
//		System.out.println("response: " + ana.ask("My son is visiting me.", false));
//		System.out.println("response: " + ana.ask("Phil.", false));
//		System.out.println("response: " + ana.ask("5pm tonight.", false));
		
//		System.out.println("response: " + ana.ask("I went to lunch with Jana yesterday.", false));
//		System.out.println("response: " + ana.ask("The Olive Garden.", false));
//		System.out.println("response: " + ana.ask("Around 1pm.", false));
		
//		System.out.println("response: " + ana.ask("I went to lunch with Jana yesterday.", false));
//		System.out.println("response: " + ana.ask("My sister.", false));
//		System.out.println("response: " + ana.ask("Olive Garden.", false));
		
//		System.out.println("response: " + ana.ask("I went to lunch with my sister yesterday.", false));
//		System.out.println("response: " + ana.ask("Jana.", false));
//		System.out.println("response: " + ana.ask("Olive Garden.", false));
		
//		System.out.println("response: " + ana.ask("I went to lunch with my granddaughter yesterday.", false));
//		System.out.println("response: " + ana.ask("Jana.", false));
//		System.out.println("response: " + ana.ask("Olive Garden.", false));
		
//		System.out.println("response: " + ana.ask("Hello.", false));
//		System.out.println("response: " + ana.ask("I'm Kevin.", false));
//		System.out.println("response: " + ana.ask("I'm hungry.", false));
//		System.out.println("response: " + ana.ask("Pizza.", false));
		//System.out.println("response: " + ana.ask("What time is it?", false));
		//System.out.println("response: " + ana.ask("How is the weather?", false));
		
//		System.out.println("response: " + ana.ask("Hello.", false));
//		System.out.println("response: " + ana.ask("I'm Kevin.", false));
//		System.out.println("response: " + ana.ask("I went shopping today.", false));
//		System.out.println("response: " + ana.ask("My sister.", false));
//		System.out.println("response: " + ana.ask("Jana.", false));
		
		// I can't remember when my nephew's birthday is.	
		
//		System.out.println("response: " + ana.ask("Hello.", false));
//		System.out.println("response: " + ana.ask("How are you?", false));
//		System.out.println("response: " + ana.ask("I am bored of school.", false));
//		System.out.println("response: " + ana.ask("I didn't go to university.", false));

		System.out.println("response: " + ana.ask("Hello.", false));
		System.out.println("response: " + ana.ask("I'm Kevin.", false));
		System.out.println("response: " + ana.ask("I am a student of the UofA.", false));
		
		System.out.println(ana.knowledge.toJSON());
		System.out.println(ana.knowledge.toTableJSON());
	}
	
	public Ana () throws IOException, JSONException {
		Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    pipeline = new StanfordCoreNLP(props);
		knowledge = new KnowledgeBase();
		
		stp = Helpers.loadStp();
		drugs = Helpers.loadDrugNames();
		vectors = Helpers.loadWordVectors();
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
		SemanticGraph dependencies = Helpers.getDependencies(pipeline, line);
		HashMap<Question, Double> potential = new HashMap<Question, Double>();

		Helpers.printTime(System.currentTimeMillis() - startTime);
		
		// variables
		boolean addedPersonAttr = false;
		
		// is the utterance a statement, request, or question
		double function = Helpers.sentenceFunction( pipeline, line );
		
		// pronoun resolution
  		ArrayList<ArrayList<String>> resolutions = Helpers.pronounResolution(pipeline, line, knowledge.getDialogue());

  		// all entities 
  		ArrayList<String> allEntities = Helpers.getEntities(pipeline, line);
  		
  		// extract entities 
		ArrayList<Entity> entitiesInText = Helpers.getYingEntities( pipeline, line );
		
		// new person?
  		String newpersonname = knowledge.getnewPerson(tkns, entitiesInText);
  		
  		// load parse graph
		AnaParseGraph apg = new AnaParseGraph( dependencies );
  		
  		Helpers.printTime(System.currentTimeMillis() - startTime);

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
  					Person correctPerson = knowledge.getPerson(person);
  					modifi = Helpers.replaceFamilyTitle(correctPerson.get("name"), whichTkns, whichPOS);
  					
  					// delete buffers
  	  				knowledge.delBuffer();
  	  				knowledge.clnBuffer();
  	  				
  	  				// reask with title replaced
  					return ask(modifi, false);
  				}
  				
  			} else {
  				boolean hasspeaker = (knowledge.getSpeaker() == null) ? false : true;
  				boolean askq = false;
  				String last_line = buffer.getLine();
  				Callback cbCopy = new Callback(buffer);
  				boolean found = buffer.executeCallback(response, knowledge, tkns, entitiesInText, pos);
  				
  				// delete callback buffer
  				knowledge.delBuffer();
  				
  				//  q2.ask, RBuffer.add(q2.callback), QBuffer.pop()
  				if (found) {
					
					// if contains family title, try and check if that relation exists
					if (Helpers.hasFamilyTitles(tkns, pos)) {
						
						// get the speaker (assume the speaker)
						Person focus = knowledge.getSpeaker();
						
						// get the title
						String title = Helpers.getFamilyTitle(tkns, pos);
						
						// check if the speaker has the relation
						boolean hasRelation = knowledge.hasRelation(focus.getId(), title);
						
						if (!hasRelation) {
							
							if (!Helpers.titleLinkWithName(line, title, allEntities, entitiesInText, allEntities, apg)) {
								
								// My sister. No sister link. In the middle of question asking.
									
								int pid = knowledge.addPerson();
								if (Helpers.isFemaleTitle(title))
									knowledge.updatePerson(pid, "sex", "female");
								
								Person p = knowledge.getPerson(pid);
								
								if (p != null) {
									// add the relation
									knowledge.addRelation(title, focus.getId(), pid);
									
									// get empty attr
									String attr = p.getEmptyAttr();
									
									if (attr != null) {
										// add the potential question
										Question qq = new Question(pipeline, tkns, pid, "person", attr, null, p.get("sex"), p.getCallback(attr), null, vectors, stp, pos, allEntities );
										//potential.put(qq, -3.14);
										knowledge.addCallback(qq.getCallback());
										knowledge.decayFrames();
										return qq.getQuestion();
									}
								}
							} else {
								// My sister Jana.  Doesn't have sister relation. In the middle of question asking.
								
								String personname = Helpers.getPersonEntity(pipeline, line);
								int pid = knowledge.addPerson(personname);
								if (Helpers.isFemaleTitle(title))
									knowledge.updatePerson(pid, "sex", "female");
								
								Person p = knowledge.getPerson(pid);
								
								if (p != null) {
									// add the relation
									knowledge.addRelation(title, focus.getId(), pid);
								}
							}
						}
					}
					
  					Question question = knowledge.getQuestion();

  					if (question != null) {
  						// question in buffer
  						knowledge.addCallback(question.getCallback());
  						knowledge.decayFrames();
						return question.getQuestion();
  					} else {
  						// no question in buffer
  						boolean hasspeakernow = (knowledge.getSpeaker() == null) ? false : true;
  						if (!hasspeaker && hasspeakernow) {
  							String umm = "Nice to meet you!";
  	  		  				knowledge.addResponse(line, "", "Nice to meet you!", "umm");
  	  		  				knowledge.decayFrames();
  	  		  				return umm;							
  						} else {
  							
  							// event specific responses
  							if (cbCopy.getObj().equals("event")) {
  								Event cbEvent = knowledge.getEvent(cbCopy.getOID());
  								String ger = Helpers.genericEventResponse(cbEvent.get("tense"));
  	  	  		  				knowledge.addResponse(line, "", ger, "ger");
  	  	  		  				knowledge.decayFrames();
  	  	  		  				return ger;
  							} else {
  								String umm = Helpers.ummPhrase();
  	  	  		  				knowledge.addResponse(line, "", umm, "umm");
  	  	  		  				knowledge.decayFrames();
  	  	  		  				return umm;
  							}
  						}
  					}
  				} else {
  					// may have changed subject
  					
  					if (cbCopy.getAtr().equals("medical")) {
  						// didn't find medical callback
  						String umm = "I'm sorry, I didn't get that.";
	  		  			knowledge.addResponse(line, "", umm, "umm");
  						knowledge.decayFrames();
	  		  			return umm;
  					}

  					// check all frames
  					
  					ArrayList<Frame> frames = knowledge.getFrames();
  					for (Frame f: frames) {
  						found = f.executeCallback(response, knowledge, tkns, entitiesInText, pos);
  						if (found) {
  							knowledge.removeFrame(f.getFID());
  							
  							String umm;
  							if (f.getObj().equals("person")) {
  								Person pr = knowledge.getPerson(f.getOID());
  								umm = pr.frameResponse(f.getAtr(), pr.get(f.getAtr()));
  								
  								Question qq2 = new Question(pipeline, tkns, pr.getId(), "person", f.getAtr(), umm, null, new ExtractBinary(), pr.get(f.getAtr()), vectors, stp, pos, allEntities );
  								//knowledge.addQuestion(question);
  								
  								knowledge.addResponse(line, "", umm, "question");
  								knowledge.addCallback(qq2.getCallback());
  							} else {
  								umm = Helpers.ummPhrase();
  	  	  		  				knowledge.addResponse(line, "", umm, "umm");
  							}

  	  		  				knowledge.decayFrames();
  	  		  				return umm;
  						}
  					}
  				}	
  			}
  		}
  		Helpers.printTime(System.currentTimeMillis() - startTime);
  		
  		// if there is silence for a period of time, ask the user a "daily" question
		if (silence) {
			Daily today = knowledge.getDaily();
			String attr = today.getEmptyAttr();
			String ques = Helpers.genSilenceQuestion(attr);
			
			Question question = new Question(pipeline, tkns, today.getId(), "daily", attr, ques, null, today.getCallback(attr), null, vectors, stp, pos, allEntities );
			
			knowledge.addResponse(line, knowledge.getSpeaker().get("name"), question.getQuestion(), "question");
			knowledge.decayFrames();
			return ques;
		}
		
		// we don't know the speaker
		if (knowledge.getSpeaker() == null) {
			
			int pid = knowledge.addPerson();
			boolean isGreeting = Helpers.isGreeting(tkns);
			
			// check if line contains an intro (i am kevin, my name is kevin, etc)
			if ( (line.toLowerCase().contains("i am") || line.toLowerCase().contains("my name is") || line.toLowerCase().contains("i'm")) && newpersonname != null ) {
				knowledge.update(pid, "person", "name", newpersonname);
				knowledge.setSpeaker(pid);
				knowledge.decayFrames();
				return "Nice to meet you!";
			}
			
			if (!isGreeting) {
				String r =  Helpers.genWhoIs();
				Question question = new Question(pipeline, tkns, pid, "person", "name", r, null, new ExtractName(), null, vectors, stp, pos, allEntities );
				//knowledge.addQuestion(question);
				
				knowledge.addResponse(line, "", r, "question");
				knowledge.addCallback(question.getCallback());
				knowledge.decayFrames();
				return r;
			} else {
				String r = Helpers.genGreeting() + "! " + Helpers.genWhoIs();
				Question question = new Question(pipeline, tkns, pid, "person", "name", r, null, new ExtractName(), null, vectors, stp, pos, allEntities );
				//knowledge.addQuestion(question);
				
				knowledge.addResponse(line, "", r, "question");
				knowledge.addCallback(question.getCallback());
				knowledge.decayFrames();
				return r;
			}
		}
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
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

				if (!Helpers.titleLinkWithName(line, title, allEntities, entitiesInText, allEntities, apg)) {
					// what if you know one son, but don't know the other?
					Person relPerson = knowledge.getPersonFromRelation(focus.getId(), title);
					
					// how many relations are there?
					int amount = knowledge.howManyRelations(focus.getId(), title);
					
					if (amount > 1) {
						// which person? 
						Question question = new Question(pipeline, tkns, relPerson.getId(), "person", "which", "Which <RELATION>?".replace("<RELATION>", title), relPerson.get("sex"), null, null, vectors, stp, pos, allEntities );
						potential.put(question, -3.14);
						
						knowledge.clnBuffer();
					} else {
						// confirm or assume
						
						if (function == 0.0) {
							// ask about attribute
							String attr = relPerson.getEmptyAttr();
							if (attr != null) {
								Question question = new Question(pipeline, tkns, relPerson.getId(), "person", attr, null, relPerson.get("sex"), relPerson.getCallback(attr), null, vectors, stp, pos, allEntities);		
								potential.put(question, question.genDistance());
							}
						}
	
						if (function == 1.0) {
							String rsp = Helpers.askQuestion(knowledge, tkns, relPerson);
							knowledge.decayFrames();
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
				
				// check if title is beside name
				int tindx = tkns.indexOf(title);
				for(String e: allEntities) {
					int eindx = allEntities.indexOf(e);
					if (e.equals("PERSON")) {
						if (tindx == eindx-1) {
							has_link = true;
						}
					}
				}
				
				// if title is not linked to a person, add the person
				if (!has_link) {
					
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
							Question question = new Question(pipeline, tkns, pid, "person", attr, null, p.get("sex"), p.getCallback(attr), null, vectors, stp, pos, allEntities );
							potential.put(question, -3.14);
						}
					}
				}
			}
		}
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
  		// disambiguation entities ( add the entities from the KB that represent those found in text )
		ArrayList<AnaEntity> entitiesInKB = knowledge.disambiguate( tkns, entitiesInText, resolutions, linenum );
		
		// people from the line in kb
		ArrayList<Person> peopleInKB = new ArrayList<Person>();
		for(AnaEntity ae: entitiesInKB) {
			if (ae.getType().equals("PER")) {
				Person newp = knowledge.getPerson(Integer.parseInt(ae.getId()));
				peopleInKB.add(newp);
			}
		}
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
  		// extract relations
		HashMap<String, String> relations = Helpers.getRelations( pipeline, line, linenum );
		
		// disambiguate relations
		knowledge.disambiguate(tkns, pos, resolutions, relations);
		
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
		// extract person attributes for speaker
		if ((peopleInKB.size() == 0 && line.toLowerCase().startsWith("i ") && !hasTitle) || ( !hasTitle && peopleInKB.size() == 0 && (line.toLowerCase().contains("i am") || line.toLowerCase().contains("i'm"))) ) {
			
			// new person list
			ArrayList<Person> attrPeople = new ArrayList<Person>();
			
			// add speaker
			Person pp = knowledge.getSpeaker();
			if (pp != null) { 
				attrPeople.add(pp);
				addedPersonAttr = AttributeMatcher.check(tkns, pos, allEntities, attrPeople, dependencies, knowledge, true);
			}
		}
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
		// extract person attributes for everyone else
		if (peopleInKB.size() > 0) {
			addedPersonAttr = AttributeMatcher.check(tkns, pos, allEntities, peopleInKB, dependencies, knowledge, true);
		}
		Helpers.printTime(System.currentTimeMillis() - startTime);
  		
  		// detect events ( extract single word events )
		String eventName = Helpers.getEvent(line, tkns, pos, Helpers.getEntities(pipeline, line));
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
		// extract event word
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
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
		// user isn't feeling well
		boolean hasSymptom = AnaIllnessPattern.match(line, pos);
		if (hasSymptom) {
			Medical medical = knowledge.addMedical();
			Question question = new Question(pipeline, tkns, medical.getId(), "medical", "issue", "What are your symptoms?", null, medical.getCallback("issue"), null, vectors, stp, pos, allEntities );
			potential.put(question, -3.14);
		}
		
		// detect symptoms, I think I have a runny nose and a fever.
		String symptoms = AnaSymptomPattern.match(line);
		if (!symptoms.equals("")) {
			Medical medical = knowledge.addMedical();
			medical.update("issue", symptoms);
			knowledge.decayFrames();
			return "Would you like me to call your doctor?";
		}
		
		// has the user taken medication
		boolean takenMeds = AnaDrugPattern.match(line, pos, drugs);
		if (takenMeds) {
			knowledge.addMedical( "took meds" );
			knowledge.updateDaily( "tookmeds", "true" );
			knowledge.decayFrames();
			return "Noted";
		}
		
		// user forgot if medication was taken
		boolean forgotMeds = AnaForgotPattern.match(line, pos);
		if (forgotMeds) {
			String tookMeds = knowledge.getDaily("tookmeds");
			knowledge.addMedical( "forgot meds" );
			knowledge.decayFrames();
			if (Boolean.valueOf(tookMeds))
				return "I have recorded that you have taken your medication today.";
			else
				return "I have no record of you taking your medication today.  Can you check with the nurse?";
		}
		
		// is the input a thanks?
		if ( Helpers.isThanks(tkns) ) {
			knowledge.decayFrames();
			return Helpers.noProblem();
		}
		
		// is the input a greeting?
		if ( Helpers.isGreeting(tkns) ) {
			
			if ( Helpers.isMorning() ) {
				knowledge.decayFrames();
				return "Good morning.";
			}

			response = Helpers.genGreeting() + ".";
			knowledge.addResponse(line, knowledge.getSpeaker().get("name"), response, "greeting");
			knowledge.decayFrames();
			return response;
		}
		
		// is there an event to ask about?
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
					Question question = new Question(pipeline, tkns, e.getId(), "event", emptyAttr1, null, tense, e.getCallback(emptyAttr1), null, vectors, stp, pos, allEntities );
					potential.put(question, -3.15);
					
					e.update(emptyAttr1, "temp");
					
					// add another potential question
					String emptyAttr2 = e.getEmptyAttr();
					if (emptyAttr2 != null) {
						Question question2 = new Question(pipeline, tkns, e.getId(), "event", emptyAttr2, null, tense, e.getCallback(emptyAttr2), null, vectors, stp, pos, allEntities );
						potential.put(question2, -3.14);
					}
					
					e.update(emptyAttr1, "");
				}
			}
		}
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
		// check for specific phrases
		if ( eventName == null ) {
			// I had a great day with Jana yesterday.
			// I spent the day with Jana yesterday.
			
			if ( (line.toLowerCase().contains("had") && line.contains("day") && line.toLowerCase().contains("with")) || (line.toLowerCase().contains("spent") && line.contains("day") && line.toLowerCase().contains("with")) || (line.toLowerCase().contains("time") && line.contains("had") && line.toLowerCase().contains("with")) )
				return "What did you do?";
		}
		
		// is there an person to ask about?
		if ( peopleInKB.size() > 0 || (peopleInKB.size() == 0 && line.toLowerCase().startsWith("i") && !hasTitle)  ) {
			
			// Add speaker
			if (peopleInKB.size() == 0 && line.toLowerCase().startsWith("i") && !hasTitle)
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
					// add all empty person questions
					String curr_attr = "";
					while ( target.getEmptyAttr() != null ) {
						
						curr_attr = target.getEmptyAttr();
								
						Question question = new Question(pipeline, tkns, target.getId(), "person", curr_attr, null, target.get("sex"), target.getCallback(curr_attr), null, vectors, stp, pos, allEntities );
						potential.put(question, question.genDistance());
						
						target.update(curr_attr, "temp");
					}
					
					HashMap<String, String> attributes = target.getAttr();
					for(String attr: attributes.keySet()) {
						if (attributes.get(attr).contains("temp"))
							target.clear(attr);
					}
				}
			}
		}
		
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
		// is there a new person?
		if ( newpersonname != null ) {
			if ( !(Helpers.join(pos, " ").contains("PRP$ NN") && hasTitle) ) {
				Person cand = knowledge.getPerson(newpersonname);
				Question question = new Question(pipeline, tkns, cand.getId(), "person", "who", "Who is <NAME>?".replace("<NAME>", newpersonname), cand.get("sex"), new ExtractRelation(), null, vectors, stp, pos, allEntities );
				potential.put(question, -3.14);
			}
		}
		
  		// if request -> attempt to resolve request
		if ( function == 2.0 ) {
			
			// Give me a suggestion
			
			// could be call doctor, reminder, scheduling, call family member, etc
			String phrase = Helpers.getReqPhrase(line);
			
			
			if (phrase == null)
				knowledge.addRequest("request");
			else
				knowledge.addRequest(phrase);
			
			knowledge.decayFrames();
			return Helpers.reqPhrase();
		}
		
		
		// if question -> attempt to answer
		if ( function == 1.0 ) {
			
			System.out.println("question");
			
			/*
			What should I cook?  
			*/
			if ( (line.toLowerCase().contains("what") && line.contains("cook")) || (line.toLowerCase().contains("suggestion") && line.contains("cook")) || (line.toLowerCase().contains("suggestion") && line.contains("eat")) ) {
				knowledge.decayFrames();
				return Helpers.foodSuggestion();
			}
			
			/*
			What is your name? 
			*/
			if ((line.toLowerCase().contains("what") && line.contains("your") && line.contains("name")) || (line.toLowerCase().contains("who") && line.contains("are") && line.contains("you"))) {
				knowledge.decayFrames();
				return Helpers.introduceAna();
			}
			
			/*
			How are you?
			*/
			if (line.toLowerCase().contains("how") && line.contains("are") && line.contains("you")) {
				knowledge.decayFrames();
				return Helpers.anaMood();
			}
			
			/*
			A: Phil is coming here for dinner tomorrow.
			B: What will you eat?
			A: I'm not sure.  What does he like?
			
			A: Is Kevin's birthday tomorrow? -> When is Kevin's birthday?
			*/
			
			if (peopleInKB.size() > 0) { 
				// assume question is about person in KB
				
				if (line.toLowerCase().contains("what") && (line.toLowerCase().contains("buy") || line.toLowerCase().contains("purchase") || line.toLowerCase().contains("get him"))) {
					String answer = knowledge.get(peopleInKB.get(0).getId(), "person", "likes");
					String prettyAnswer = peopleInKB.get(0).formulateResponse("likes", answer);
					
					knowledge.addResponse(line, "", prettyAnswer, "answer");
					knowledge.decayFrames();
					return prettyAnswer;
				}
				
				// attr has to be precise
				String attr = Helpers.getAttr(tkns);
				
				if (attr == null) {
					knowledge.addResponse(line, "", "I'm not sure.", "answer");
					knowledge.decayFrames();
					return "I'm not sure.";
				}
				
				String answer = knowledge.get(peopleInKB.get(0).getId(), "person", attr);
				if (answer.equals("")) {
					knowledge.addResponse(line, "", "I don't know, sorry.", "answer");
					knowledge.decayFrames();
					return "I don't know, sorry.";
				}
				
				String prettyAnswer = peopleInKB.get(0).formulateResponse(attr, answer);
				
				// add response
				knowledge.addResponse(line, "", prettyAnswer, "answer");
				knowledge.decayFrames();
				return prettyAnswer;
			}
		}
		
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
  		// if statement -> attempt to response or send to chatterbot
		if ( function == 0.0 ) {
			//response = getFact();
			
			//  I can't remember when my nephew's birthday is.
			if (line.toLowerCase().contains("can't") && line.toLowerCase().contains("remember") && line.toLowerCase().contains("birthday")) {
				knowledge.addResponse(line, "", "I'm sorry to hear that.", "answer");
				knowledge.decayFrames();
				return "I'm sorry to hear that.";
			}
			
			// Now I have to cook for him
			if ( (line.toLowerCase().contains("what") && line.contains("cook")) || (line.toLowerCase().contains("suggestion") && line.contains("cook")) || (line.toLowerCase().contains("suggestion") && line.contains("eat")) ) {
				knowledge.decayFrames();
				return Helpers.foodSuggestion();
			}
						
			// if no people mentioned, no relations, no events, then...
			
			if (eventName == null && relations.size() == 0 && peopleInKB.size() == 0 && !addedPersonAttr) {
				
				// try to move the conversation
				
				// Tell me about yourself;
				
				// what to cook for him
				
				// We went out together
				
				// I watched a movie yesterday.
				if (line.toLowerCase().contains("watch") && line.toLowerCase().contains("movie")) {
					String umm = "Which one?.";
	  				knowledge.addResponse(line, "", umm, "umm");
	  				knowledge.decayFrames();
	  				return umm;
				}
				
				// Introduction
				if (knowledge.getDialogue().size() < 4 && !hasTitle) {
					String umm = "Tell me about yourself.";
	  				knowledge.addResponse(line, "", umm, "umm");
	  				knowledge.decayFrames();
	  				return umm;
				}
			}
		}
		
		for(Question q: potential.keySet())
			System.out.println(q.getQuestion() + ": " + potential.get(q));
		System.out.println("---");
		
		// rank questions
		//System.out.println("potential.size(): " + potential.size());
		ArrayList<Question> willask = Helpers.rank(potential);
		//System.out.println("willask.size(): " + willask.size());
		
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
		// if no questions
		if (willask.size() == 0) {
			knowledge.decayFrames();
			return Helpers.ummPhrase();
		}
		
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
		
		Helpers.printTime(System.currentTimeMillis() - startTime);
		
		String finalResponse = qone.getQuestion();
		
		knowledge.decayFrames();
		knowledge.addFrame(line, finalResponse, qone);
		return finalResponse;
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
