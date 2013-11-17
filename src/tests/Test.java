package tests;

import java.io.BufferedWriter;
import java.io.FileWriter;

import agents.Ana;

public class Test {
	
	public static void main(String args[]) throws Exception {

		Ana ana = new Ana();
		ana.initKB(0);
		
		// test adding person attributes
		
		// test speaker age
		ana.ask("I am 25 years old.", false);
		String age = ana.getKnowledge().getSpeaker().get("age");
		if (age == "25") {
			System.out.println("Correct");
		}
		
		// test speaker education
		ana.ask("I am a student at the University of Alberta.", false);
		String edu = ana.getKnowledge().getSpeaker().get("education_institute");
		if (edu == "University of Alberta") {
			System.out.println("Correct");
		}
	}
}
