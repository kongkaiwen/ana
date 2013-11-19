<%@ page import="evaluation.Evaluate" %>

<%

if ( request.getSession().getAttribute("eval") != null ) {
    Evaluate e = (Evaluate)session.getAttribute("eval");
    e.resetKB(2);
}

%>

<!DOCTYPE html>

<html>
    <head>
        <title>Conversation</title> 
        <link rel="stylesheet" media="screen" href="../../stylesheets/evaluate.css">
        <link rel="shortcut icon" type="image/png" href="../../images/favicon.png">
        <script src="../../javascripts/jquery-1.7.1.min.js"></script>
        <meta charset=utf-8 />
        <script src="http://cytoscape.github.io/cytoscape.js/api/cytoscape.js-latest/cytoscape.min.js"></script>
        <script src="../../javascripts/kb.js"></script>
        <script src="../../javascripts/evaluate.js"></script>
    </head>
	<body>
        <div id="loading"><img align="center" id="loading-gif" src="../../images/big_loading.gif" /></div>
		<div id="wrapper">
			
			<h1 style="margin-top: 5px;margin-bottom:10px;" align="center">Scenario Two</h1>

            <div align="center">
			<input class="ana-button" id="home-button" type="button" value="Home" href="partone.html" />
			<input class="ana-button" id="next-button" type="button" value="Next" href="scenario3.jsp" />
            <input class="ana-button" id="kb-button" type="button" value="KB" />
            </div>

            <div id="mask"><div id="popup">
                <div id="cy"></div>
                <input id="load" type="button" value="Refresh"/>
                <a id="close"><img src="../../images/close.png" /></a>
                <div id="events-div"><table id="event"></table></div>
                <div id="daily-div"><table id="daily"></table></div>
                <div id="medical-div"><table id="medcl"></table></div>
		    </div></div>

		    <input type="text" id="model-a-input" placeholder="Ask" x-webkit-speech/>
		    <input type="button" id="model-a-button" />

            <input type="text" id="model-b-input" placeholder="Ask" x-webkit-speech/>
            <input type="button" id="model-b-button" />
		    
		    <p align="center" class="scenario"><b>Scenario:</b> Your name is Irene.  Your son Phil is coming over.  You need to cook for him but you don't know what to make. </p>
			
		    <div id="model-a">
		    	<p class="title">Model A <img id="model-a-img" src="" style="width:15px;height:15px;" /></p>
		    	<div id="model-a-body"></div>
		    </div>
		    <div id="model-b">
		    	<p class="title">Model B <img id="model-b-img" src="" style="width:15px;height:15px;"/></p>
		    	<div id="model-b-body"></div>
		    </div>

            <div id="evaluation">
                <form id="rating-form">
                    <table id="evaluation-table">
                        <tr>
                            <th>Question</th><th>A</th><th>B</th><th>Equal</th><th>Why?</th>
                        </tr>
                        <tr>
                            <td>Better? (more human)</td><td><input type="radio" name="Rating" value="a"></td><td><input type="radio" name="Rating" value="b"></td><td><input type="radio" name="Rating" value="c"></td><td><input type="text" id="submit-text" /></td>
                        </tr>
                        <tr>
                            <td><input type="button" value="Submit" id="submit-radio"/></td><td></td><td></td><td></td>
                        </tr>
                    </table>
                </form>
            </div>

            <input id="scenario" type="hidden" value="2" />
            <input id="correct" type="hidden" value="b" />
		</div>
	</body>
</html>
