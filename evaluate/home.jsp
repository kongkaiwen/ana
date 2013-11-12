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
%>

<!DOCTYPE html>

<html>
    <head>
        <title>Chat</title>
        <link rel="stylesheet" media="screen" href="stylesheets/evaluate.css">
        <link rel="shortcut icon" type="image/png" href="images/favicon.png">
        <script src="javascripts/jquery-1.7.1.min.js"></script>
        <script src="javascripts/evaluate.js"></script>
    </head>
	<body>
	What
	</body>
</html>
