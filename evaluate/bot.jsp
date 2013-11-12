<%@ page import="agents.Bot" %>

<%
String a = request.getParameter("a");
out.print(Bot.ask(a));
%>
