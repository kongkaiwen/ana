package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import kb.Callback;
import kb.Medical;
import kb.Question;
import kb.Daily;
import kb.Event;
import kb.KnowledgeBase;
import kb.Person;
import medical.AnaDrugPattern;
import medical.AnaForgotPattern;
import medical.AnaIllnessPattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import attributes.PatternMatcher;

import relations.Entity;
import tools.Helpers;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.semgraph.SemanticGraph;
import entities.AnaEntity;

public class Ana {

	public static KnowledgeBase knowledge;
	public static StanfordCoreNLP pipeline;
	
	public static void main(String[] args) throws Exception {
		Ana ana = new Ana();
		ana.knowledge.initKB(1);
//		System.out.println(ana.ask("I went shopping with Sarah.", false));
//		System.out.println(ana.ask("She is my sister.", false));
//		System.out.println(ana.ask("She is 24 years old.", false));
//		System.out.println(ana.ask("I'm taking my medication.",false));
//		System.out.println(ana.ask("Have i taken my meds this morning.",false));
//		System.out.println(ana.ask("I just went to lunch with my son.",false));
//		System.out.println(ana.ask("Nathan.",false));
//		System.out.println(ana.ask("12.",false));
//		System.out.println(ana.ask("Hello.",false));
//		System.out.println(ana.ask("I'm taking my medication.",false));
//		System.out.println(ana.ask("Nathan.",false));
//		System.out.println(ana.ask("I'm not feeling well.",false));
//		System.out.println(ana.ask("I think I have a runny nose and a fever.",false));
		System.out.println(ana.ask("Nathan is my son.", false));
	}
	
	public Ana () {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		
		pipeline = new StanfordCoreNLP(props);
		knowledge = new KnowledgeBase();
	}

	public String ask ( String line, boolean silence ) throws Exception {
		
		// if there is silence for a period of time, ask the user a "daily" question
		if (silence) {
			
			Daily today = knowledge.getDaily();
			String attr = today.getEmptyAttr();
			String ques = Helpers.genSilenceQuestion(attr);
			
			Question question = new Question(pipeline, line, today.getId(), "daily", attr, ques, null, today.getCallback(attr), false);
			knowledge.addQuestion(question);
			
			return ques;
		}
			
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
		
		// pronoun resolution
  		ArrayList<ArrayList<String>> resolutions = Helpers.pronounResolution(pipeline, line, knowledge.getDialogue());
  			
  		// extract entities 
		ArrayList<Entity> entitiesInText = Helpers.getYingEntities( pipeline, line );
		
		// check the buffers
  		Callback buffer = knowledge.getCallback();
  		if (buffer != null) {
  			
  			// asked a "which" question
  			if (buffer.getAtr().equals("which")) {
  				
  				String person = "";
  				for(Entity e: entitiesInText) {
  					if (e.getType().equals("PER"))
  						person = e.getName();
  				}
  				
  				ArrayList<String> whichTkns = Helpers.getTokens(pipeline, buffer.getLine());
  				ArrayList<String> whichPOS = Helpers.getPOS(pipeline, buffer.getLine());
  				
  				int cindex = 0;
  				for(String p: whichPOS) {
  					cindex = whichPOS.indexOf(p);
  					
  					// for example: my son -> Kevin
  					if (p.equals("PRP$") && whichPOS.get(cindex+1).equals("NN")) {
  						whichTkns.remove(cindex);
  						whichTkns.set(cindex, person);
  						break;
  					}
  				}
  				
  				// replace PRP$ NN with person's name.
  				String dialogueLine = Helpers.join(whichTkns, " ");
  				
  				// set the current line
  				line = dialogueLine;
  				
  				// replace dialogue with new line
  				knowledge.addDialogue(dialogueLine);
  				
  				// delete buffer
  				knowledge.delBuffer();
  				
  			} else {
  				boolean askq = false;
  				String last_line = buffer.getLine();
  				boolean found = buffer.executeCallback(response, knowledge, tkns, entitiesInText, pos);
  				
  				// delete buffer
  				knowledge.delBuffer();
  				
  				//  q2.ask, RBuffer.add(q2.callback), QBuffer.pop()
  				if (found) {
  					Question question = knowledge.getQuestion();
  					if (question != null) {
  						knowledge.addResponse(line, "", question.getQuestion(), "question");
  						knowledge.addCallback(question.getCallback());
  						return question.getQuestion();
  					} else {
  						return "I see.";
  					}
  				} else {
  					// may have changed subject
  				}

  				return "I see.";
  			}
  		}
  		
  		// is the utterance a statement, request, or question
		double function = Helpers.sentenceFunction( pipeline, line );
		
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
				
				// what if you know one son, but don't know the other?
				Person relPerson = knowledge.getPersonFromRelation(focus.getId(), title);
				
				// how many relations are there?
				int amount = knowledge.howManyRelations(focus.getId(), title);
				
				if (amount > 1) {
					// which person? 
					Question question = new Question(pipeline, line, relPerson.getId(), "person", "which", "Which <RELATION>?".replace("<RELATION>", title), relPerson.get("sex"), null,  false);
					potential.add(question);
					
				} else {
					// ask about attribute
					Question question = new Question(pipeline, line, relPerson.getId(), "person", relPerson.getEmptyAttr(), null, relPerson.get("sex"), relPerson.getCallback(relPerson.getEmptyAttr()), false);		
					potential.add(question);
					
				}
			} else {
				// add the person
				int pid = knowledge.addPerson();
				if (Helpers.isFemaleTitle(title))
					knowledge.updatePerson(pid, "sex", "female");
				Person p = knowledge.getPerson(pid);
				
				// add the relation
				knowledge.addRelation(title, focus.getId(), pid);
				
				// add the potential question
				Question question = new Question(pipeline, line, p.getId(), "person", p.getEmptyAttr(), null, p.get("sex"), p.getCallback(p.getEmptyAttr()), false);
				potential.add(question);
				
			}
		}
		
		// new person?
  		boolean newperson = knowledge.newEntity(tkns, entitiesInText);
  		String name = knowledge.getnewPerson(tkns, entitiesInText);
		
  		// disambiguation entities ( add the entities from the KB that represent those found in text )
		ArrayList<AnaEntity> entitiesInKB = knowledge.disambiguate( tkns, entitiesInText, resolutions, linenum );
		
		// people from the line in kb
		ArrayList<Person> peopleInKB = new ArrayList<Person>();
		for(AnaEntity ae: entitiesInKB) {
			if (ae.getType().equals("PER")) {
				peopleInKB.add(knowledge.getPerson(Integer.parseInt(ae.getId())));
			}
		}
		
  		// extract relations
		HashMap<String, String> relations = Helpers.getRelations( pipeline, line, linenum );
		
		// disambiguate relations
		knowledge.disambiguate(resolutions, relations);
		
		// extract entity attributes
		ArrayList<String> matches = PatternMatcher.check(line, entitiesInKB, pos, dependencies);
		for(String match: matches) {
			// find the person in our db and assign them the school ( eid # attr # val )
			String tokens[] = match.split("#");
			knowledge.updatePerson(Integer.parseInt(tokens[0]), tokens[1], tokens[2]);
		}
  		
  		// detect events ( extract single word events )
		boolean hasEvent = Helpers.hasEvent( pipeline, line );

		// extract event word
		if (hasEvent) {
			String ename = Helpers.getEvent(line);
			Event event = knowledge.getEvent(ename);  // doesn't consider duplicate events...
			if (event == null) {
				eid = knowledge.addEvent( ename );
			} else {
				eid = event.getId();
			}
		}
		
		// user isn't feeling well
		boolean hasSymptom = AnaIllnessPattern.match(line, pos);
		if (hasSymptom) {
			Medical medical = knowledge.addMedical();
			Question question = new Question(pipeline, line, medical.getId(), "medical", "issue", "What are your symptoms?", null, medical.getCallback("issue"), false);
			potential.add(question);
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
			response = "Hello <NAME>.";
			response = response.replace("<NAME>", replace);
			knowledge.addResponse(line, name, response, "greeting");
			
			return response;
		}
		
		// is there a new event or person?
		if ( newperson ) {
			// add the person
			int pid = knowledge.addPerson(name);
			Person cand = knowledge.getPerson(pid);
			if (cand.get("sex").equals("female"))
				knowledge.updatePerson(pid, "sex", "female");
			
			Question question = new Question(pipeline, line, pid, "person", "who", "Who is <NAME>?".replace("<NAME>", name), cand.get("sex"), null, false);
			potential.add(question);
			
		}
		
		// is there an event to ask about?
		if ( hasEvent ) {
			String event = Helpers.getEvent(line);
			String tense = Helpers.getEventTense(tkns, pos);
			
			boolean askq = true;
			
			// which attribute to ask about? get the first person?
			Event e = knowledge.getEvent(eid);
			knowledge.updateEvent(e.getId(), "tense", tense);
			if (askq) {
				// add potential question
				Question question = new Question(pipeline, line, e.getId(), "event", e.getEmptyAttr(), null, tense, e.getCallback(e.getEmptyAttr()), false);
				potential.add(question);
			}
		}
		
		// is there an person to ask about?
		if ( peopleInKB.size() > 0 ) {
			
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
				Person target = knowledge.getPerson(people.get(0));

				// add potential question
				Question question = new Question(pipeline, line, target.getId(), "person", target.getEmptyAttr(), null, target.get("sex"), target.getCallback(target.getEmptyAttr()), false);
				potential.add(question);
			}
		}
		
		// get default response
		Bot bot = new Bot();
		response = bot.ask(line);
		
  		// if request -> attempt to resolve request
		if ( function == 2.0 ) {
			//return "As you wish.";
			
			// could be call doctor, reminder, scheduling, call family member, etc
		}
  		
  		// if question -> attempt to answer
		if ( function == 1.0 ) {
			//return "I'll try and find an answer for you.";
			
			/*
			A: Phil is coming here for dinner tomorrow.
			B: What will you eat?
			A: I'm not sure.  What does he like?
			
			A: Is Kevin's birthday tomorrow? -> When is Kevin's birthday?
			 
			*/
			
			if (peopleInKB.size() > 0) { 
				// assume question is about person in KB
				
				String attr = Helpers.getAttr(tkns);
				
				if (attr == null) {
					knowledge.addResponse(line, "", "I'm not sure.", "answer");
					return "Could you repeat that?";
				}
				
				String answer = knowledge.get(peopleInKB.get(0).getId(), "person", attr);
		
				// add response
				knowledge.addResponse(line, "", answer, "answer");
				return answer;
			}
		}
		
  		// if statement -> attempt to response or send to chatterbot
		if ( function == 0.0 ) {
			//response = getFact();
		}
		
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

		ArrayList<Question> willask = new ArrayList<Question>();
		
		// choose at most two questions, if theres a medical question it must be included
		if (medQ.size() > 0) {
			willask.add(medQ.get(0));
		
			if ( perQ.size() > 0 && eveQ.size() > 0) {
				willask.add(perQ.get(0));
			} else if (perQ.size() > 0 && eveQ.size() == 0) {
				willask.add(perQ.get(0));
			} else if (perQ.size() == 0 && eveQ.size() > 0) {
				willask.add(eveQ.get(0));
			}
		} else {
			if ( perQ.size() > 0 && eveQ.size() > 0) {
				willask.add(perQ.get(0));
				willask.add(eveQ.get(0));
			} else if (perQ.size() > 0 && eveQ.size() == 0) {
				for (Question pq: perQ)
					willask.add(pq);
			} else if (perQ.size() == 0 && eveQ.size() > 0) {
				for (Question eq: eveQ) 
					willask.add(eq);
			}
		}
		
		//for (Question q: willask)
			//System.out.println("QASK: "+ q.getQuestion());
		
		// if no questions
		if (willask.size() == 0)
			return "I see.";
		
		// q1.ask, RBuffer.add(q1.callback), QBuffer.pop()
		Question qone = willask.get(0);
		if (willask.size() > 1) {
			// add to buffer
			Question qtwo = willask.get(1);
			knowledge.addQuestion(qtwo);
		}
		
		System.out.println(potential.size());
		
		// add to buffer
		knowledge.addCallback(qone.getCallback());

		// add response
		knowledge.addResponse(line, "", qone.getQuestion(), "question");
		
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
}
