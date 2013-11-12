package db;

import java.sql.*;

public class DataBase {

	private Statement stmt;
	private Connection con;
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		DataBase db = new DataBase();
		//db.insertChat("a", "herrrrrro");
		db.insertKB("yes", "herrrrrro", "no", "hhh");
		db.close();
	}
	
	/*
	Contains two tables: questions and conversation.
	*/
	public DataBase() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/thesis", "root", "VenWed58"); 
		stmt = con.createStatement();
	}
	
	public void insertChat( String model, String desc ) throws SQLException {
		stmt.executeUpdate("insert into chat (Model, Description) values ('"+model+"', '"+desc+"')");
	}
	
	public void insertKB( String miss, String miss_text, String incorr, String incorr_text ) throws SQLException {
		int miss_val = (miss.equals("yes")) ? 1 : 0;
		int inco_val = (incorr.equals("yes")) ? 1 : 0;
		stmt.executeUpdate("insert into kbeval (Missing, MissingTXT, Incorrect, IncorrectTXT) values ('"+miss_val+"', '"+miss_text+"', '"+inco_val+"', '"+incorr_text+"')");
	}
	
	public void close() throws SQLException {
		con.close();
	}
}
