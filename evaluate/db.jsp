<%@ page import="java.sql.*" %>
<%@ page import="db.DataBase" %>

<%

String type = request.getParameter("type");

DataBase db = new DataBase();

if ( type.equals("chat") ) {

	String model = request.getParameter("model");
	String desc = request.getParameter("text");

	db.insertChat( model, desc );
} else {
	
	String missing = request.getParameter("missing");
	String missing_text = request.getParameter("missing_text");
	String incorrect = request.getParameter("incorrect");
	String incorrect_text = request.getParameter("incorrect_text");

	db.insertKB( missing, missing_text, incorrect, incorrect_text );
}

db.close();

%>

Hello	