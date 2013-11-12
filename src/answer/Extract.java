package answer;

import java.io.IOException;
import java.util.ArrayList;

import relations.Entity;

import kb.KnowledgeBase;

public interface Extract {
	
	public boolean execute( int oid, String object, String attr, KnowledgeBase kb, ArrayList<String> tkns, ArrayList<Entity> ent, ArrayList<String> pos  ) throws IOException;
}
