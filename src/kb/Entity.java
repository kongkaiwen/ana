package kb;

import java.io.IOException;

import answer.Extract;

public interface Entity {

	String get( String attr );
	void update( String attr, String value ) throws IOException;
	int getId();
	String getEmptyAttr();
	Extract getCallback( String attr );
}
