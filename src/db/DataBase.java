package db;

import java.sql.*;

import config.Settings;

public class DataBase {

	private Statement stmt;
	private Connection con;
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		DataBase db = new DataBase();
		//db.insertChat("a", "herrrrrro", "a");
		db.insertKB("yes", "herrrrrro", "no", "hhh", "ffff");
		db.close();
	}
	
	/*
	Contains two tables: questions and conversation.
	*/
	public DataBase() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Settings.database, Settings.username, Settings.password); 
		stmt = con.createStatement();
	}
	
	public void insertChat( String model, String desc, String corr ) throws SQLException {
		PreparedStatement pstmt = con.prepareStatement("insert into chat (Model, Description, Correct) values (?, ?, ?)");
		pstmt.setString(1, model);
		pstmt.setString(2, desc);
		pstmt.setString(3, corr);
		pstmt.executeUpdate();
		//stmt.executeUpdate("insert into chat (Model, Description, Correct) values ('"+model+"', '"+desc+"', '"+corr+"')");
	}
	
	public void insertKB( String miss, String miss_text, String incorr, String incorr_text, String conv ) throws SQLException {
		int miss_val = (miss.equals("yes")) ? 1 : 0;
		int inco_val = (incorr.equals("yes")) ? 1 : 0;
		
		PreparedStatement pstmt = con.prepareStatement("insert into kbeval (Missing, MissingTXT, Incorrect, IncorrectTXT, Conversation) values (?, ?, ?, ?, ?)");
		pstmt.setInt(1, miss_val);
		pstmt.setString(2, miss_text);
		pstmt.setInt(3, inco_val);
		pstmt.setString(4, incorr_text);
		pstmt.setString(5, conv);
		pstmt.executeUpdate();
		//stmt.executeUpdate("insert into kbeval (Missing, MissingTXT, Incorrect, IncorrectTXT, Conversation) values ('"+miss_val+"', '"+miss_text+"', '"+inco_val+"', '"+incorr_text+"', '"+conv+"')");
	}
	
	public void close() throws SQLException {
		con.close();
	}
}
