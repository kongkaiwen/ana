function init() {

	if (document.contains(document.getElementById("home-button"))) {
		home = document.getElementById("home-button");
		home.addEventListener("click", send, false);
	}

	if (document.contains(document.getElementById("kb-dialogue"))) {

		loffset = 400;
		lw = window.innerWidth - loffset;
		lh = window.innerHeight;
		
		$("#loading").css("width", lw);
		$("#loading").css("height", lh);
		$("#loading").css("display", "block");
		$("#loading").css("left", loffset + "px");

		$("#loading-gif").css("left", (lw/2 - document.getElementById("loading-gif").offsetWidth/2)  + "px");
		$("#loading-gif").css("top", (lh/2 - document.getElementById("loading-gif").offsetHeight/2)  + "px");

		$.ajax({
			url: "../load.jsp",
			data: "scenario="+$("#scenario").val(),
			success: function() {
				toggleLoading();
			}
		});

		evaluationheight = document.getElementById("evaluation-table").offsetHeight;
		sidebarheight = window.innerHeight;
		graphheight = window.innerHeight - evaluationheight;
		tableheight = Math.round(graphheight/3);

		evaluation = document.getElementById("evaluation");
		evaluation.style.height = evaluationheight + "px";

		frm = document.getElementById("rating-form");
		frm.style.height = evaluationheight + "px";

		evaltable = document.getElementById("evaluation-table");
		evaltable.style.height = evaluationheight + "px";
		//evaltable.style.width = (window.innerWidth - 400 - 40) + "px";

		content = document.getElementById("content");
		content.style.width = (window.innerWidth - 400 - 40) + "px";
		content.style.height = (window.innerHeight - 40) + "px";

		//eval = document.getElementById("evaluation");
		//eval.style.width = (window.innerWidth - 400 - 40) + "px";

		dialogue = document.getElementById("kb-dialogue");
		//dialogue.style.bottom = "175px";
		
		dialogue.style.width = "350px";
		dialogue.style.height = (window.innerHeight - 200 - 30) + "px";

		side = document.getElementById("sidebar");
		side.style.height = sidebarheight + "px";

		cydiv = document.getElementById("cy");
		cydiv.style.width = (window.innerWidth - 400 - 40)/2 + "px";
		cydiv.style.height = graphheight - 20 + "px";

		table1 = document.getElementById("events-div");
		table1.style.width = ((window.innerWidth - 400 - 40) /2) + "px";
		table1.style.height = tableheight + "px";
		table1.style.maxHeight = tableheight + "px";
		table1.style.top = "0";

		table2 = document.getElementById("medical-div");
		table2.style.width = ((window.innerWidth - 400 - 40) /2) + "px";
		table2.style.height = tableheight + "px";
		table2.style.maxHeight = tableheight + "px";
		table2.style.top = tableheight + "px";

		table3 = document.getElementById("daily-div");
		table3.style.width = ((window.innerWidth - 400 - 40) /2) + "px";
		table3.style.height = tableheight + "px";
		table3.style.maxHeight = tableheight + "px";
		table3.style.top = (2*tableheight) + "px";

		input = document.getElementById("kb-dialogue-input");
		input.style.top = "150px";
		dialogue.style.top = "200px";

		//input.style.left = (window.innerWidth/2) - (500/2) + "px";
		//input.addEventListener("keypress", model_a_function, false);
		input.addEventListener("keypress", function(e) {
			addText(e, $("#kb-dialogue-input").val(), "../server.jsp");
		}, false);

		button = document.getElementById("kb-dialogue-button");
		button.style.top = "150px";
		//button.style.left = (window.innerWidth/2) - (500/2) + (input.offsetWidth) + "px";
		button.addEventListener("click", function(e) {
			addText(e, $("#kb-dialogue-input").val(), "../server.jsp");
		}, false);

		initGraph();

		loadKB();
        $("#submit-ratings").click(sendRating);
	}
}

function sendRating() {

	checked1 = $('input[name="missing"]:checked').val();
	checked2 = $('input[name="incorrect"]:checked').val();

	if (checked1 == undefined || checked2 == undefined)
		return;

	missing_txt = $("#missing_txt").val();
	incorrect_txt = $("#incorrect_txt").val();
	conversation = $("#kb-dialogue-body").text();

	// {"missing": {"boolean": true, "text": text}, "incorrect": {"boolean": true, "text": text}}

	$.ajax({
		url: "../db.jsp",
		data: "type=kbeval&missing="+checked1+"&incorrect="+checked2+"&missing_text="+missing_txt+"&incorrect_text="+incorrect_txt+"&conv="+conversation,
		success: function(data) {
			alert("Success!");
		}
	});
}

function send( e ) {
	window.location.href = e.target.getAttribute("href");
}

function prettyText( txt, who, cls ) {
	return "<p class='"+cls+"'><b>"+who+":</b>  "+txt+"</p>";
}

function addText( e, text, server ) {
	
	if (e == null || e.type == "click" || e.keyCode == 13) {

		toggleLoading();

		if (text != "silence")
			$("#kb-dialogue-body").prepend(prettyText(text, "You", "even"));

		$("#ana-img").attr("src", "../../images/loading.gif");

		$.ajax({
			url: server,
			data: "a=" + text + "&scenario=3",
			success: function(data) {
				$("#kb-dialogue-body").prepend(prettyText(data, "Ana", "odd"));
				loadKB();
				$("#ana-img").attr("src", "");
				toggleLoading();
			}
		});
		
		$("#kb-dialogue-input").val("");
	}
}

function cap(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

function toTable( json ) {

	lookup = {"event": "events", "medcl": "medical issues", "daily": "requests"};

	for (var obj in json) {

		data = json[obj];

		table = "<tr>";
		for(var key in data[0])
			table += "<th>" + cap(key) + "</th>";
		table += "</tr>"

	    for(var i=0;i<data.length;i++) {
	        row = "<tr>";
	        for(var key in data[i]) {
                if (data[i][key] === "") 
	        	    row += "<td>N/A</td>";
                else
                    row += "<td>" + data[i][key] + "</td>";
            }
	        table += row + "</tr>";
	    }

	    if (table === "<tr></tr>") {
	    	table = "<tr><td colspan='6'>No "+lookup[obj]+" recorded.</td></tr>";
	    }

	    $("#"+obj).html(table);
	}	
}

function toggleLoading() {

	display = $("#loading").css("display");
	if (display == "block")
		$("#loading").css("display", "none");
	else 
		$("#loading").css("display", "block");
}

function loadKB() {

    $.ajax({
    	url: "../kb.jsp",
    	data: "scenario=3",
    	success: function(data) {
    		json = JSON.parse(data);	

    		graph = json["graph"];
    		table = json["table"];

    		toTable( table );
    		cy.load( graph );
    	}
    });
      
}

window.onload = init;
