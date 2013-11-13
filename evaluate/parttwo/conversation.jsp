<%@ page import="evaluation.Evaluate" %>

<%

if ( request.getSession().getAttribute("eval") != null ) {
    Evaluate e = (Evaluate)session.getAttribute("eval");
    e.resetKB(1);
}

%>

<!DOCTYPE html>

<html>
    <head>
        <title>Evaluate KB</title>
        <link rel="stylesheet" media="screen" href="../../stylesheets/kbeval.css">
        <link rel="shortcut icon" type="image/png" href="../../images/favicon.png">
        <script src="../../javascripts/jquery-1.7.1.min.js"></script>
        <!--<script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>-->
        <meta charset=utf-8 />
        <script src="http://cytoscape.github.io/cytoscape.js/api/cytoscape.js-latest/cytoscape.min.js"></script>
        <script src="../../javascripts/kbeval.js"></script>++
    </head>
	<body>
        <div id="wrapper">
            <div id="sidebar">
                <h2 style="margin-left: 15px;color: #eee;">KB Evaluation</h2>

                <div id="buttons">
                    <input class="ana-button" id="home-button" type="button" value="Home" href="parttwo.html" />
                    <input class="ana-button" id="next-button" type="button" value="Next" href="scenario2.jsp" />
                </div>

                <div id="kb-desc">
                    Please enter your text here.  Attempt to converse with Ana and check which information is captured.
                </div>

                <input type="text" id="kb-dialogue-input" placeholder="Ask" x-webkit-speech/>
                <input type="button" id="kb-dialogue-button" />

                <div id="kb-dialogue">
                    <p class="title">Ana <img id="ana-img" src="" style="width:15px;height:15px;" /></p>
                    <div id="kb-dialogue-body"></div>
                </div>
            </div>
            <div id="content">
                <div id="cy"></div>
                <div id="events-div"><table id="event"></table></div>
                <div id="daily-div"><table id="daily"></table></div>
                <div id="medical-div"><table id="medcl"></table></div>
                <!--<input id="load" type="button" value="Refresh"/>-->
                <div id="evaluation">
                    <form id="rating-form">
                        <table id="evaluation-table">
                            <tr>
                                <th>Category</th><th>Yes</th><th>No</th><th>Description</th>
                            </tr>
                            <tr>
                                <td>Anything missing?</td><td><input type="radio" name="missing" value="yes"></td><td><input type="radio" name="missing" value="no" ></td><td><input type="text" id="missing_txt" /></td>
                            </tr>
                            <tr>
                                <td>Too much added?</td><td><input type="radio" name="incorrect" value="yes"></td><td><input type="radio" name="incorrect" value="no" ></td><td><input type="text" id="incorrect_txt" /></td>
                            </tr>
                            <tr>
                                <td><input style="width:100px;" type="button" value="Submit" id="submit-ratings" /></td><td></td><td></td><td></td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>