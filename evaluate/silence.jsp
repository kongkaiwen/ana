<%@ page import="evaluation.Evaluate" %>

<%
session = request.getSession();
Evaluate e;

if ( session.getAttribute("eval") == null ) {
	// If the Parameter Values are not null
	// then add the name/value pair to the HttpSession
	e = new Evaluate();	
	session.setAttribute("eval", e);
} else {
	e = (Evaluate)session.getAttribute("eval");
}

String a = request.getParameter("a");
String output = e.analyze(a);
out.print(output);
%>
