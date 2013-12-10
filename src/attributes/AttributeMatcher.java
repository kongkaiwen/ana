package attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONException;

import tools.Helpers;

import kb.KnowledgeBase;
import kb.Person;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class AttributeMatcher {
	
	public static void main(String args[]) throws IOException, JSONException {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		String line = "Kevin is a software developer at Google.	";
		
		KnowledgeBase kb = new KnowledgeBase();
		kb.initKB(1);
		
		ArrayList<Person> people = new ArrayList<Person>();
		people.add(kb.getPerson(2));
		
		check(Helpers.getTokens(pipeline, line), Helpers.getPOS(pipeline, line), Helpers.getEntities(pipeline, line), people, Helpers.getDependencies(pipeline, line), kb, true);

		System.out.println(kb.toJSON());
	}

	public static boolean check(ArrayList<String> tkns, ArrayList<String> pos, ArrayList<String> entities, ArrayList<Person> people, SemanticGraph dep, KnowledgeBase kb, boolean add) throws IOException {
		
		boolean age = AgePattern.match(tkns, pos, entities, people, dep, kb, add);
//		boolean profession_position = PositionPattern.match(tkns, pos, entities, people, dep, kb, add);
		boolean profession_institute = ProfessionPattern.match(tkns, pos, entities, people, dep, kb, add);
//		boolean school_degree = DegreePattern.match(tkns, pos, entities, people, dep, kb, add);
		boolean school_institute = SchoolPattern.match(tkns, pos, entities, people, dep, kb, add);
		boolean dislikes = DislikesPattern.match(tkns, pos, entities, people, dep, kb, add);
		boolean likes = LikesPattern.match(tkns, pos, entities, people, dep, kb, add);
		
		//boolean flag = age || profession_position || profession_institute || school_degree || school_institute || dislikes || likes;
		boolean flag = age || profession_institute || school_institute || dislikes || likes;
		
		return flag;
	}
}
