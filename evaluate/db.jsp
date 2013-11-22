<%@ page import="java.sql.*" %>
<%@ page import="db.DataBase" %>

<%
String scenario = request.getParameter("scenario");
String type = request.getParameter("type");

DataBase db = new DataBase();

if ( type.equals("chat") ) {

	String model = request.getParameter("model");
	String desc = request.getParameter("text");
	String correct = request.getParameter("correct");

	db.insertChat( model, desc, correct );
} else {
	
	String missing = request.getParameter("missing");
	String missing_text = request.getParameter("missing_text");
	String incorrect = request.getParameter("incorrect");
	String incorrect_text = request.getParameter("incorrect_text");

	String conv = request.getParameter("conv");

	db.insertKB( missing, missing_text, incorrect, incorrect_text, conv );
}

db.close();

%>

Hello	