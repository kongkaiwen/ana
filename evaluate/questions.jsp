<%@page import="java.io.File"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>

<!DOCTYPE html>

<html>
    <head>
        <title>Question Evaluation</title>
        <link rel="stylesheet" media="screen" href="../stylesheets/evaluate.css">
        <link rel="stylesheet" media="screen" href="../stylesheets/questions.css">
        <link rel="shortcut icon" type="image/png" href="../images/favicon.png">
        <script src="../javascripts/jquery-1.7.1.min.js"></script>
        <meta charset = utf-8 />
        <script src="../javascripts/evaluate.js"></script>
        <script src="../javascripts/questions.js"></script>
    </head>
	<body>
		<input id="docid" type="hidden" value="1" />
		<div id="wrapper">
			
			<h1 align="center">Question Evaluation</h1>

			<input id="home-button" type="button" value="Home" href="partone.html" />
			<input id="next-button" type="button" value="Next" href="questions2.jsp" />
		    
		    <div id="dialogue">
		    	<p class="title">Dialogue</p>
		    	<div id="dialogue-body">
		    	<%
		            //String jspPath = session.getServletContext().getRealPath("/home/kfquinn/Web/chat/evaluate");
		            //String txtFilePath = jspPath+ "/conv1.txt";
		            BufferedReader reader = new BufferedReader(new FileReader("/home/kfquinn/Web/chat/evaluate/conv1.txt"));
		            StringBuilder sb = new StringBuilder();
		            String line;
		
		            while((line = reader.readLine())!= null){
		                sb.append("<p>" + line + "</p>\n");
		            }

		            out.println(sb.toString());
		            reader.close();
        		%>
		    	</div>
		    	
		    </div>
		    
		    <div id="questions">
		    	<p class="title">Ranked Questions</p>
		    	<input id="submit-results" type="button" value="Submit" />
		    	<div id="questions-body">
		    	<%
		            reader = new BufferedReader(new FileReader("/home/kfquinn/Web/chat/evaluate/quest1.txt"));
		            sb = new StringBuilder();
		
					int i = 1;
					sb.append("<form id='eval-form'><table id='question-table'>\n");
					sb.append("<tr><th></th><th>Question</th><th>Best?</th><th>Good?</th></tr>");
		            while((line = reader.readLine())!= null){
		            	String tokens[] = line.split("\t");
		            	String tmp = "<td>"+i+". </td><td>"+tokens[2]+"</td><td><input class='submit-radio' type='radio' name='kevin' qid='"+tokens[1]+"'></td><td><input name='kevin-check' type='checkbox' qid='"+tokens[1]+"'></td>";
		                sb.append("<tr>" + tmp + "</tr>\n");
		                i++;
		            }
		            sb.append("</table></form>\n");
		            out.println(sb.toString());
		            reader.close();
        		%>
		    	</div>
		    </div>
		</div>
	</body>
</html>
