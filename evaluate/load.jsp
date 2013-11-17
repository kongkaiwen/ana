<%@ page import="evaluation.Evaluate" %>

<%

int scenario = Integer.parseInt(request.getParameter("scenario"));

if ( session.getAttribute("eval") == null ) {
	Evaluate e = new Evaluate(scenario);	
	session.setAttribute("eval", e);
}

%>
