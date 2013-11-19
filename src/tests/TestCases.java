package tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import agents.Ana;

public class TestCases {

	public static void main(String args[]) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter("testing_output"));

		Ana ana = new Ana();
		ana.initKB(0);
		
		executeExample( ana, "Jacob is my nephew.", bw );
		executeExample( ana, "He is 5 years old.", bw );
		bw.write("\n");
		
		executeExample( ana, "I'm going to a concert on Saturday with Jana.", bw );
		executeExample( ana, "She is my sister.", bw );
		executeExample( ana, "It's at the Shaw Conference.", bw );
		bw.write("\n");
		
		executeExample( ana, "I went shopping with Sarah.", bw );
		executeExample( ana, "She is my sister.", bw );
		executeExample( ana, "She is 24 years old.", bw );	
		bw.write("\n");
		
		executeExample( ana, "I just went to lunch with my son.", bw );
		executeExample( ana, "Yes.", bw );
		executeExample( ana, "12.", bw );
		bw.write("\n");
		
		executeExample( ana, "I'm not feeling well.", bw );
		executeExample( ana, "I think I have a runny nose and a fever.", bw );
		executeExample( ana, "Yes please.", bw );
		bw.write("\n");
		
		executeExample( ana, "I need to buy a gift for my grandson's birthday party.", bw );
		executeExample( ana, "Nathan.", bw );
		executeExample( ana, "Saturday.", bw );
		executeExample( ana, "He is turning 5.", bw );
		bw.write("\n");
		
		bw.close();
	}
	
	public static void executeExample( Ana ana, String line, BufferedWriter bw ) throws Exception {
		bw.write("You: " + line + "\n");
		bw.write("Ana: " + ana.ask(line, false) + "\n");
	}
}
