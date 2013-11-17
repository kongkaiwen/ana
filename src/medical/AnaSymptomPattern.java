package medical;

import java.io.IOException;
import java.util.ArrayList;

import tools.Helpers;

public class AnaSymptomPattern {

	public static String match( String line ) throws IOException {
		
		boolean found = false;
		ArrayList<String> symptoms = Helpers.loadSymptoms();
		ArrayList<String> problems = new ArrayList<String>();
		
		for (String sym: symptoms) {
			if (line.contains(sym)) {
				found = true;
				problems.add(sym);
			}
		}
		
		return Helpers.join(problems, ", ");
	}
}
