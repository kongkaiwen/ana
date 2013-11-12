package entities;

import org.json.JSONObject;

public interface AnaEntity {

	String getId();
	String getName();
	String getType();
	String toString();
	
	JSONObject toJSON() throws Exception;
	void updateAtr(String atr, String val);
}
