<%@ page import="evaluation.Evaluate" %>

<%

int scenario = Integer.parseInt(request.getParameter("scenario"));

if ( session.getAttribute("eval") == null ) {
	Evaluate e = new Evaluate(scenario);	
	session.setAttribute("eval", e);
}

// this.getServletConfig().getServletContext().setAttribute("sharedId", shared);

// if ( this.getServletConfig() != null ) {
// 	out.println("here");
// }

// if ( this.getServletConfig().getServletContext() != null ) {
// 	out.println("here2");
// }

// if ( this.getServletConfig().getServletContext().getAttribute("kb") == null ) {
// 	out.println("here3");
// 	this.getServletConfig().getServletContext().setAttribute("kb", new Evaluate(1));
// }

// if ( this.getServletConfig().getServletContext().getAttribute("kb") != null ) {
// 	Evaluate e = (Evaluate) this.getServletConfig().getServletContext().getAttribute("kb");
// 	out.println(e.getKB());
// }

%>
