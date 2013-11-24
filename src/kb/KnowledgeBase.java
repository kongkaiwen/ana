package kb;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import relations.Entity;
import tools.Helpers;
import edu.stanford.nlp.util.StringUtils;
import entities.AnaEntity;
import entities.AnaEntityFactory;

public class KnowledgeBase {
	
	private Person speaker;
	
	private int num_events;
	private int num_dailies;
	private int num_medicals;
	private int num_requests;
	private int num_entities;
	private int num_relations;
	private int num_responses;
	
	private ArrayList<Event> events;
	private ArrayList<Person> people;
	private ArrayList<Medical> medicals;
	private ArrayList<Request> requests;
	private ArrayList<Relation> relations;
	private ArrayList<Response> responses;
	
	private ArrayList<String> dialogue;
	private HashMap<String, Daily> dailies;
	
	private QuestionBuffer qBuffer;
	private CallbackBuffer cBuffer;
	
	public static void main(String args[]) throws JSONException, IOException {
		KnowledgeBase kb = new KnowledgeBase();
		kb.initKB(1);
		System.out.println(kb.toJSON());
	}
	
	public KnowledgeBase() {
		
		this.num_events = 0;
		this.num_dailies = 0;
		this.num_medicals = 0;
		this.num_requests = 0;
		this.num_entities = 0;
		this.num_relations = 0;
		this.num_responses = 0;
		
		this.events = new ArrayList<Event>();
		this.people = new ArrayList<Person>();
		this.requests = new ArrayList<Request>();
		this.medicals = new ArrayList<Medical>();	
		this.relations = new ArrayList<Relation>();
		this.responses = new ArrayList<Response>();
		
		this.dialogue = new ArrayList<String>();
		this.dailies = new HashMap<String, Daily>();
		
		this.qBuffer = new QuestionBuffer();
		this.cBuffer = new CallbackBuffer();
		this.speaker = null;
	}
	
	public void initKB( int scenario ) throws IOException {
		
		// debugging scenario
		if (scenario == 0) {
			addPerson("Kevin");
			setSpeaker(0);
			addPerson("Phil");
			updatePerson("Phil", "age", "56");
			updatePerson("Phil", "likes", "cook, cookies");
			addRelation("son", 0, 1);
			addEvent("hockey game");
			addMedical("chest pain");
			addDialogue("I am Kevin.");
		}
		
		// Scenario: Your name is Irene. You have a two granddaughter's named Jana and Wendy. You just spent the day with Jana yesterday.   Discuss what you did with Jana.
		if (scenario == 1) {
			addPerson("Irene");
			setSpeaker(0);
			addDialogue("I am Irene.");
			addPerson("Jana");
			updatePerson("Jana", "age", "25");
			updatePerson("Jana", "likes", "pasta, basketball");
			addPerson("Wendy");
			addRelation("granddaughter", 0, 1);
			addRelation("granddaughter", 0, 2);
		}
		
		// Your name is Irene.  Your son Phil is coming over.  You need to cook for him but you don't know what to make. 
		if (scenario == 2) {
			addPerson("Irene");
			setSpeaker(0);
			addDialogue("I am Irene.");
			addPerson("Phil");
			updatePerson("Phil", "likes", "fish, pasta");
			updatePerson("Phil", "dislikes", "pork, swimming");
			addPerson("David");
			addPerson("Al");
			addRelation("son", 0, 1);
			addRelation("son", 0, 2);
			addRelation("son", 0, 3);
		}
		
		// Scenario: Introduce yourself (your real name).  Talk about yourself. 
		if (scenario == 3) {
			
		}
		
		// Scenario: Your name is Irene.  You think your nephew's birthday is coming up.  Find out when and what you should buy.
		if (scenario == 4) {
			addPerson("Irene");
			setSpeaker(0);
			addDialogue("I am Irene.");
			addPerson("Jacob");
			updatePerson(1, "date_of_birth", "November 11, 2009");
			updatePerson(1, "likes", "lego");
			addRelation("nephew", 0, 1);
		}

		// Scenario: Your name is Ed.  An unknown member of your family has visited.  Introduce and describe them. 
		if (scenario == 5) {
			addPerson("Ed");
			setSpeaker(0);
			addDialogue("I am Ed.");
		}
	}	
	
	public void resetKB( int scenario ) throws IOException {
		this.num_events = 0;
		this.num_dailies = 0;
		this.num_requests = 0;
		this.num_medicals = 0;
		this.num_entities = 0;
		this.num_relations = 0;
		this.num_responses = 0;
		
		this.people = new ArrayList<Person>();
		this.dialogue = new ArrayList<String>();
		this.relations = new ArrayList<Relation>();
		this.events = new ArrayList<Event>();
		this.medicals = new ArrayList<Medical>();
		this.responses = new ArrayList<Response>();
		this.requests = new ArrayList<Request>();
		
		this.dailies = new HashMap<String, Daily>();
		
		this.qBuffer = new QuestionBuffer();
		this.cBuffer = new CallbackBuffer();
		this.speaker = null;
		
		initKB(scenario);
	}
	
	public JSONObject toJSON() throws JSONException {
		/*
		Format for Cytoscape Graph Software:
		
		obj = {
			nodes: [
			    { data: { id: "n1" } },
			    { data: { id: "n2" } }
			],
	
		    edges: [
		        { data: { id: "e1", source: "n1", target: "n2" } }
		    ]
		}
	    */
		
		JSONObject obj = new JSONObject();
		JSONArray nodes = new JSONArray();
		JSONArray edges = new JSONArray();
		
		for(Person p: this.people) {
			nodes.put(p.toJSON());
		}
		
		for(Relation r: this.relations) {
			edges.put(r.toJSON());
		}
		
		obj.put("nodes", nodes);
		obj.put("edges", edges);
		
		return obj;
	}
	
	public JSONObject toTableJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		
		JSONArray evnts = new JSONArray();
		JSONArray medcl = new JSONArray();
		JSONArray daily = new JSONArray();
		
		for (Event e: events)
			evnts.put(e.toTableJSON());

		for (Medical m: medicals)
			medcl.put(m.toTableJSON());
		
		for (Request r: requests)
			daily.put(r.toTableJSON());
		
		//daily.put(getDaily().toTableJSON());
		
		obj.put("event", evnts);
		obj.put("medcl", medcl);
		obj.put("daily", daily);
		
		return obj;
	}
	
	public String get( int oid, String obj, String atr ) {
		
		if (obj.equals("daily")) {
			return getDaily().get(atr);
		} else if (obj.equals("person")) {
			return getPerson(oid).get(atr);
		} else if (obj.equals("medical")) {
			return getMedical(oid).get(atr);
		} else if (obj.equals("event")) {
			return getEvent(oid).get(atr);
		} else {
			return null;
		}
	}
	
	public void setSpeaker( int id ) {
		Person p = getPerson(id);
		speaker = p;
	}
	
	public Person getSpeaker() {
		return speaker;
	}
	
	public Callback getCallback() {
		return this.cBuffer.get();
	}
	
	public void delBuffer() {
		this.cBuffer = new CallbackBuffer();
	}
	
	public void clnBuffer() {
		this.qBuffer = new QuestionBuffer();
	}
	
	public void update( int oid, String object, String attr, String value ) throws IOException {
		
		if (object.equals("daily")) {
			getDaily().update(attr, value);
		} else if (object.equals("person")) {
			updatePerson(oid, attr, value);
		} else if (object.equals("medical")) {
			updateMedical(oid, attr, value);
		} else if (object.equals("event")) {
			updateEvent(oid, attr, value);
		}
	}
	
	public int addPerson() throws IOException {
		
		Person person = new Person(num_entities);
		num_entities++;
		
		// does this person already exist?
		this.people.add(person);
		
		return person.getId();
	}
	
	public int addPerson( String name ) throws IOException {
		
		Person person = new Person(num_entities);
		num_entities++;
		
		person.update("name", name);
		
		// does this person already exist?
		this.people.add(person);
		
		return person.getId();
	}
	
	public void updatePerson( String name, String attr, String value ) throws IOException {
		Person p = getPerson(name);
		p.update(attr,value);
	}
	
	public void updatePerson( int eid, String attr, String value ) throws IOException {
		Person p = getPerson(eid);
		p.update(attr,value);
	}
	
	public boolean hasPerson( String name ) {
		
		for(Person p: this.people) {
			if (p.get("name").equals(name))
				return true;
		}
		
		return false;
	}
	
	public Person getPerson( String name ) { 
		for(Person p: this.people) {
			if (p.get("name").equals(name))
				return p;
		}
		
		return null;
	}
	
	public Person getPerson( int id ) {
		
		for(Person p: this.people) {
			if (p.getId() == id)
				return p;
		}
		
		return null;
	}
	
	public int addRequest( String name ) throws IOException {
		
		Request request = new Request(num_requests);
		num_requests++;
		
		request.update("name", name);
		
		// does this person already exist?
		this.requests.add(request);
		
		return request.getId();
	}
	
	public void updateRequest( int id, String attr, String value ) throws IOException {
		Request r = getRequest(id);
		r.update(attr,value);
	}
	
	public Request getRequest( int id ) {
		
		for(Request r: this.requests) {
			if (r.getId() == id)
				return r;
		}
		
		return null;
	}
	
	public int entityExists( Entity e ) throws Exception {
		
		for ( Person p: people ) {
			if ( p.get("name").toLowerCase().equals(e.getName().toLowerCase()) ) {
				return p.getId();
			}
		}
  		
  		return -1;
  	}
	
	public boolean hasRelation( int n1, int n2 ) {
		
		for(Relation r: relations) {
			int id1 = r.getE1();
			int id2 = r.getE2();
			
			if ( n1 == id1 && n2 == id2 )
				return true;
		}
		
		return false;
	}
	
	public boolean hasRelation( int id, String relation ) {
		
		for(Relation r: relations) {
			int id1 = r.getE1();
			int id2 = r.getE2();
			String rel = r.getType();
			
			if ( (id == id1 || id == id2) && rel.equals(relation) )
				return true;
		}
		
		return false;
	}
	
	public boolean hasRelation( int id1, int id2, String relation ) {
		
		for(Relation r: relations) {
			int rid1 = r.getE1();
			int rid2 = r.getE2();
			String rel = r.getType();
			
			if ( id1 == rid1 && id2 == rid2 && rel.equals(relation) )
				return true;
			
			if ( id1 == rid2 && id2 == rid1 && rel.equals(relation) ) 
				return true;
		}
		
		return false;
	}
	
	public Person getPersonFromRelation( int id, String relation ) {
		
		for(Relation r: relations) {
			int id1 = r.getE1();
			int id2 = r.getE2();
			String rel = r.getType();
			
			if ( (id == id1 || id == id2) && rel.equals(relation) ) {
				int pid = (id == id1) ?  id2 : id1;
				Person p = getPerson(pid);
				return p;
			}
		}
		
		return null;
	}
	
	public int howManyRelations( int id, String relation ) {
		int count = 0;
		for(Relation r: relations) {
			int id1 = r.getE1();
			int id2 = r.getE2();
			String rel = r.getType();
			
			if ( (id == id1 || id == id2) && rel.equals(relation) )
				count++;
		}
		
		return count;
	}
	
	public void addRelation( String rel, int n1, int n2 ) {
		
		if (n1 == n2) 
			return;
		
		if (hasRelation(n1,n2,rel))
			return;
		
		Relation r1 = new Relation(num_relations, n1, n2, rel);
		num_relations++;
		this.relations.add(r1);
		
	}
	
	public void addDialogue( String line ) {
		this.dialogue.add(line);
	}
	
	public ArrayList<String> getDialogue() {
		return this.dialogue;
	}
	
	public void replaceDialogue( String oldLine, String newLine ) {
		for (String d: dialogue) {
			int index = dialogue.indexOf(d);
			if (d.equals(oldLine))
				dialogue.set(index, newLine);
		}
	}
	
	public void addResponse( String line, String entity, String text, String type ) {
		
		Response r = new Response( num_responses, text, type, entity );
		num_responses++;
		
		this.responses.add(r);
	}
	
	public void addQuestion( Question question ) {
		//System.out.println("ADDING Q: " + question.getQuestion());
		this.qBuffer.add(question);
	}
	
	public Question getQuestion() {
		
//		for(Question q: qBuffer.getQuestions()) {
//			System.out.print(q.getQuestion() +":::");
//		}
//		System.out.println("////");
		return this.qBuffer.pop();
	}
	
	public int getNumQuestions() {
		return this.qBuffer.questions.size();
	}
	
	public void addCallback( Callback callback ) {
		this.cBuffer.add(callback);
	}
	
	public Event getEvent( String name ) {
		
		for(Event e: this.events) {
			if (e.get("name").equals(name))
				return e;
		}
		
		return null;
	}
	
	public Event getEvent( int eid ) {
		
		for(Event e: this.events) {
			if (e.getId() == eid)
				return e;
		}
		
		return null;
	}
	
	public void updateEvent( String name, String attr, String value ) throws IOException {
		Event e = getEvent(name);
		e.update(attr,value);
	}
	
	public void updateEvent( int eid, String attr, String value ) throws IOException {
		Event e = getEvent(eid);
		e.update(attr,value);
	}
	
	public ArrayList<Response> getResponses() {
		Collections.sort(responses);
		return responses;
	}
	
	public ArrayList<Response> getResponses( String entity ) {
		ArrayList<Response> output = new ArrayList<Response>();
		for(Response r: responses) {
			if (r.get("entity").equals(entity))
				output.add(r);
		}
		Collections.sort(output);
		return output;
	}
	
	public ArrayList<Response> getResponses( String type, String entity ) {
		ArrayList<Response> output = new ArrayList<Response>();
		for(Response r: responses) {
			if (r.get("type").equals(type) && r.get("entity").equals(entity))
				output.add(r);
		}
		Collections.sort(output);
		return output;
	}
	
	public Daily getDaily() {
		String current = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		if (dailies.containsKey(current))
			return dailies.get(current);
		else {
			Daily newDaily = new Daily(num_dailies);
			num_dailies++;
			dailies.put(current, newDaily);
			return newDaily;
		}
	}
	
	public String getDaily( String attr ) {
		String current = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		if (dailies.containsKey(current))
			return dailies.get(current).get(attr);
		else {
			Daily newDaily = new Daily(num_dailies);
			num_dailies++;
			dailies.put(current, newDaily);
			return newDaily.get(attr);
		}
	}
	
	public void updateDaily( String attr, String val) {
		Daily daily;
		String current = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		
		if (dailies.containsKey(current))
			daily = dailies.get(current);
		else {
			daily = new Daily(num_dailies);
			num_dailies++;
		}
		
		System.out.println("updating...");
		daily.update(attr, val);
		dailies.put(current, daily);
	}
	
	public int addEvent( String name ) {
		Event event = new Event(num_events);
		num_events++;
		
		event.update("name", name);
		
		// does this event already exist?
		this.events.add(event);
		
		return event.getId();
	}
	
	public void updateMedical( int mid, String attr, String val ) {
		Medical m = getMedical(mid);
		m.update(attr,val);
	}
	
	public Medical getMedical( int mid ) {
			
		for(Medical m: this.medicals) {
			if (m.getId() == mid)
				return m;
		}
		
		return null;
	}
	
	public Medical addMedical() {
		Medical medical = new Medical(num_medicals);
		num_medicals++;
		
		// does this event already exist?
		this.medicals.add(medical);
		
		return medical;
	}
	
	public Medical addMedical( String name ) {
		Medical medical = new Medical(num_medicals);
		num_medicals++;
		
		medical.update("issue", name);
		
		// does this event already exist?
		this.medicals.add(medical);
		
		return medical;
	}
	
	public ArrayList<Medical> getMedicals() {
		return this.medicals;
	}
	
	// my, (I, my, Kevin) // output id#name
	public String correctEntity(String name, String offset, String linenum, ArrayList<ArrayList<String>> mentions) {
		  
		// ref: He:2:0
		// mentions: [ [Phil, my father, He], [my], [Kevin, his son, his] ]
		String ref = name+":"+linenum+":"+offset;
		String capref = StringUtils.capitalize(name+":"+linenum+":"+offset);
		
		//System.out.println("\nlooking for: "+ref);
		//System.out.println(mentions+"\n");
		
//		for(ArrayList<String> resolutions: mentions) {
//			// check if the ref is in this anaphora set
//			if ( resolutions.contains(ref)) {
//				// name is in this set, now check if there is an entity in this set (I, my, Kevin)
//			    for(String e3: resolutions) {
//			    	String perName = e3.split(":")[0];
//				    if (hasPerson(perName)) {
//					    Person person = getPerson(perName);
//			    	    return person.getId() + "#" + person.get("name");
//				    }
//			    }
//			}
//		}
		
		// my nephew 's:2:9::He:3:1::he:4:3::
		for(ArrayList<String> resolutions: mentions) {
			// check if the ref is in this anaphora set
			if ( resolutions.contains(ref) || resolutions.contains(capref)) {
				// name is in this set, now check if there is an entity in this set (I, my, Kevin)
			    for(String e3: resolutions) {
			    	String perName = e3.split(":")[0];
			    	
				    if (hasPerson(perName)) {
					    Person person = getPerson(perName);
			    	    return person.getId() + "#" + person.get("name");
				    }
				    
				    String title = Helpers.containsFamilyTitle(perName);
				    if ( title != null ) {
				    	for(Relation r: relations) {
				    		if (r.getE1() == 0 && r.getType() == title) {
				    			return r.getE2() + "#" + getPerson(r.getE2());
				    		}
				    		
				    		if (r.getE2() == 0 && r.getType() == title) {
				    			return r.getE1() + "#" + getPerson(r.getE1());
				    		}
				    	}
				    }
			    }
			}
		}
		
		
		return null;
	}
	
	public ArrayList<AnaEntity> disambiguate( ArrayList<String> tkns, ArrayList<Entity> entities, ArrayList<ArrayList<String>> resolutions, int linenum ) throws IOException {
		
		// go through each entity and check if it is already in the KB or needs to be added
		ArrayList<String> ana_names = new ArrayList<String>();
		ArrayList<AnaEntity> ana_entities = new ArrayList<AnaEntity>();
		for (Entity e: entities) {
        	
        	// check for weird NE
			if (e.getType() == "NONE")
        		continue;
        		
        	// check if entity exists already, if exists add to collection and continue
        	boolean hasperson = hasPerson(e.getName());
        	if ( hasperson ) {
        		Person p = getPerson(e.getName());
        		AnaEntity ae = AnaEntityFactory.createEntity("PER", p.getAttr());
        		ae.updateAtr("id", String.valueOf(p.getId()));
        		ana_names.add(e.getName());
        		ana_entities.add(ae);
        		continue;
        	}
        	
        	// if the entity doesn't exist, add new entity
        	if (e.getType() == "PER") {
        		// new person entity add to KB
        		addPerson(e.getName());
        		Person p = getPerson(e.getName());
            	AnaEntity ae = AnaEntityFactory.createEntity("PER", p.getAttr());
            	ae.updateAtr("id", String.valueOf(p.getId()));
            	ana_names.add(e.getName());
            	ana_entities.add(ae);
        	} else {
        		// not person entity, don't add to KB
        		HashMap<String, String> attributes = new HashMap<String, String>();
        		attributes.put("name", e.getName());
            	AnaEntity ae = AnaEntityFactory.createEntity(e.getType(), attributes);
            	ae.updateAtr("id", "-1");
            	ana_names.add(e.getName());
            	ana_entities.add(ae);
        	}
        }
		
		// check for pronouns and attempt to resolve them
	    for(String tkn: tkns) {
	    	for(String i: Helpers.pronouns) {
				if ( tkn.toLowerCase().equals(i) ) {
					
					// resolve this pronoun if possible
					String resolved = correctEntity(tkn, String.valueOf(tkns.indexOf(tkn)+1), String.valueOf(linenum), resolutions);
					//System.out.println(resolved);
					if ( resolved != null ) {
						// found a match!
						String tokens[] = resolved.split("#");
						Person p = getPerson(Integer.parseInt(tokens[0]));
			        	AnaEntity ae = AnaEntityFactory.createEntity("PER", p.getAttr());
			        	if (ana_names.contains(p.get("name")))
			        		continue;
			        	ae.updateAtr("id", String.valueOf(p.getId()));
			        	ana_entities.add(ae);
					}
				}
			}
	    }
	    
	    //System.out.println("aftersize: " + ana_entities.size());
		return ana_entities;
	}
	
	public void disambiguate( ArrayList<String> tkns, ArrayList<String> pos, ArrayList<ArrayList<String>> entities, HashMap<String, String> relations ) {
		
		// relations: key(Phil:0:0;my:2:2), val(famy)
		if ( relations != null ) {
			for (String r: relations.keySet()) {
				String tmp1[] = r.split(";");
				String tmp2[] = tmp1[0].split(":");
				String tmp3[] = tmp1[1].split(":");
				String entity1 = tmp2[0];
				String entity2 = tmp3[0];
				String offset1 = String.valueOf(Integer.parseInt(tmp2[1])+1); 
				String offset2 = String.valueOf(Integer.parseInt(tmp3[1])+1);
				String relation = relations.get(r).toString();
				
				if (relation.equals("buss"))
					continue;
				
				// replace1 & replace2 are: id#name
				String replace1 = correctEntity(entity1, offset1, tmp1[2], entities);
				//System.out.println("replace: " + entity1 + " " + replace1);
				String replace2 = correctEntity(entity2, offset2, tmp1[2], entities);
				//System.out.println("replace: " + entity2 + " " + replace2);
				
				// couldn't match the string to an entity in KB
				if (replace1 == null || replace2 == null)
					continue;
				
				String r1id = replace1.split("#")[0];
				String r2id = replace2.split("#")[0];
				
				// check if relation exists
				boolean hasrelation = hasRelation(Integer.parseInt(r1id), Integer.parseInt(r2id));
				if (!hasrelation) {
					// should check how many titles
					String title = Helpers.getFamilyTitle(tkns, pos);
					if (title == null) 
						addRelation(relation, Integer.parseInt(r1id), Integer.parseInt(r2id));
					else
						addRelation(title, Integer.parseInt(r1id), Integer.parseInt(r2id));
				}
			}
		}
	}
	
	public String getFact() throws IOException {
		ArrayList<String> facts = Helpers.loadFacts();
		Random rand = new Random();
		
		return facts.get(rand.nextInt(facts.size()));
	}
	
	public boolean newEntity( ArrayList<String> tkns, ArrayList<Entity> entities ) {
		
		ArrayList<String> people = new ArrayList<String>();
		// go through each entity and check if it is already in the KB
		for (Entity e: entities) {
        	
        	// check for weird NE
			if (e.getType() == "PER") {
        		people.add(e.getName());
			}
        }
		
		if (people.size() == 0)
			return false;
		
		for (String name: people) {
			if (hasPerson(name))
				return false;
		}
		
		return true;
	}	
	
	public String getnewPerson( ArrayList<String> tkns, ArrayList<Entity> entities) {
		
		ArrayList<String> people = new ArrayList<String>();
		
		// go through each entity and check if it is already in the KB
		for (Entity e: entities) {

			if (e.getType() == "PER") {
        		people.add(e.getName());
			}
        }
		
		if (people.size() == 0)
			return null;
		
		for (String name: people) {
			if (!hasPerson(name))
				return name;
		}
		
		return null;
	}
}
