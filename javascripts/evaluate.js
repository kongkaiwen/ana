
silence = 0;

function init() {

	// every second
	// setInterval(function() {
	// 	silence++;
	// }, 1000);


	// setInterval(function() {
	// 	if (silence > 20) {
	// 		addText( null, "silence", "a", "silence.jsp" );
	// 		silence = -10000;
	// 	}
	// }, 1000);

    if (document.contains(document.getElementById("close"))) {
        
    }

	if (document.contains(document.getElementById("home-button"))) {
		home = document.getElementById("home-button");
		next = document.getElementById("next-button");
        kb = document.getElementById("kb-button");
        kb.addEventListener("click", popup, false);
		
		//home.style.left = (window.innerWidth/2 - home.offsetWidth) + "px";
		//next.style.left = (window.innerWidth/2) + "px";
        //kb.style.left = (window.innerWidth/2 + next.offsetWidth) + "px";
	} 

	if (document.contains(document.getElementById("topics"))) {
		theight = window.innerHeight - document.getElementById("evaluation").offsetHeight;

		topics = document.getElementById("topics");
		topics.style.width = "175px";
		topics.style.height =  theight + "px";
		topics.style.top = "0";
		topics.style.left = "0";
	}

	if (document.contains(document.getElementById("model-a"))) {

		// <div id="loading"><img id="loading-gif" src="../../images/big_loading.gif" /></div>

		$("#loading").css("width", window.innerWidth);
		$("#loading").css("height", window.innerHeight);
		$("#loading").css("display", "block");

		$("#loading-gif").css("left", (window.innerWidth/2 - document.getElementById("loading-gif").offsetWidth/2)  + "px");
		$("#loading-gif").css("top", (window.innerHeight/2 - document.getElementById("loading-gif").offsetHeight/2)  + "px");

		$.ajax({
			url: "../load.jsp",
			data: "scenario="+$("#scenario").val(),
			success: function() {
				toggleLoading();
			}
		});

		input_top = 130;
		input_gap = 60;
		
		home.addEventListener("click", send, false);
		next.addEventListener("click", send, false);

		modelA = document.getElementById("model-a");
		modelB = document.getElementById("model-b");

		modelA.style.top = (input_top + input_gap) + "px";
		modelB.style.top = (input_top + input_gap) + "px";
		
		modelA.style.width = "500px";
		modelB.style.width = "500px";
		modelA.style.height = "400px";
		modelB.style.height = "400px";
		
		gapsize = 50;
		boxwidth = parseInt(modelA.style.width.slice(0,-2));
		boxheight = parseInt(modelA.style.height.slice(0,-2));
		bottomgap = parseInt(modelA.style.bottom.slice(0,-2));

		modelA.style.left = (window.innerWidth/2 - boxwidth) - (gapsize/2) + "px";
		modelB.style.left = (window.innerWidth/2) + (gapsize/2) + "px";

		correct = $("#correct").val();

		var model_a_function = function(e) {
			silence = 0;
			if (correct === "a")
				addText( e, $("#model-a-input").val(), "a", "../server.jsp" );
			else
				addText( e, $("#model-a-input").val(), "a", "../bot.jsp" );
		};

		var model_b_function = function(e) {
			if (correct === "b")
				addText( e, $("#model-b-input").val(), "b", "../server.jsp" );
			else
				addText( e, $("#model-b-input").val(), "b", "../bot.jsp" );
		};

		input = document.getElementById("model-a-input");
		input.style.left = (window.innerWidth/2 - boxwidth) - (gapsize/2) + "px";
		input.style.top = input_top + "px";
		input.addEventListener("keypress", model_a_function, false);

		input.addEventListener("keyup", function(e) {
			if (e.type != "click" & e.keyCode != 13) {
				$("#model-b-input").val($("#model-a-input").val());
			}
		}, false);

		button = document.getElementById("model-a-button");
		button.style.left = (window.innerWidth/2 - boxwidth) - (gapsize/2) + (465)+10 + "px";
		button.style.top = input_top + "px";
		button.addEventListener("click", model_a_function, false);

		input = document.getElementById("model-b-input");
		input.style.left = (window.innerWidth/2) + (gapsize/2) + "px";
		input.style.top = input_top + "px";
		input.addEventListener("keypress", model_b_function, false);

		input.addEventListener("keyup", function(e) {
			if (e.type != "click" & e.keyCode != 13) {
				$("#model-a-input").val($("#model-b-input").val());
			}
		}, false);

		button = document.getElementById("model-b-button");
		button.style.left = (window.innerWidth/2) + (gapsize/2)  + (465)+10 + "px";
		button.style.top = input_top + "px";
		button.addEventListener("click", model_b_function, false);

		rating_button = document.getElementById("submit-radio");
		rating_button.addEventListener("click", sendRating, false);

		initGraph();
	}
}

var popup;
function popup( e ) {
    mask = document.getElementById("mask");
    popup = document.getElementById("popup");

    mask.style.width = window.innerWidth  + "px";
    mask.style.height = window.innerHeight + "px";
    mask.style.display = "block";
    
    //popup.style.backgroundColor = "rgba(195, 201, 191, 1)";

	close = document.getElementById("close");

    w = 1200;
    h = 700;

    graphwidth = Math.round(w/2) - 50;
    graphheight = h - 50;
    tablewidth = graphwidth;
	tableheight = Math.round(graphheight/3);

	popup.style.width = w + "px";
    popup.style.height = h + "px";

	popup.style.top = (window.innerHeight/2) - (h/2) + document.body.scrollTop + "px";
    popup.style.left = (window.innerWidth/2) - (w/2) + "px";

	close = document.getElementById("close");
    close.addEventListener('click', closePopup, false);

    cydiv = document.getElementById("cy");
	cydiv.style.width = graphwidth + "px";
	cydiv.style.height = graphheight + "px";

	table1 = document.getElementById("events-div");
	table1.style.width = graphwidth + "px";
	table1.style.height = tableheight + "px";
	table1.style.maxHeight = tableheight + "px";
	table1.style.top = "0";

	table2 = document.getElementById("medical-div");
	table2.style.width = tablewidth + "px";
	table2.style.height = tableheight + "px";
	table2.style.maxHeight = tableheight + "px";
	table2.style.top = tableheight + "px";

	table3 = document.getElementById("daily-div");
	table3.style.width = tablewidth + "px";
	table3.style.height = tableheight + "px";
	table3.style.maxHeight = tableheight + "px";
	table3.style.top = (2*tableheight) + "px";

    $.ajax({
    	url: "../kb.jsp",
    	data: "scenario="+$("#scenario").val(),
    	success: function(data) {
    		json = JSON.parse(data);	

    		graph = json["graph"];
    		table = json["table"];

    		toTable( table );
    		cy.load( graph );
    	}
    });
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

function closePopup() {
    mask = document.getElementById("mask");
    mask.style.display = 'none';
}

function send( e ) {
	window.location.href = e.target.getAttribute("href");
}

function sendRating() {
	// {"model": value, "description": text, "correct": value} 

	checked = $('input[name="Rating"]:checked').val();
	correct = $("#correct").val();

	conversation = $("#model-"+correct+"-body").text();

	$.ajax({
		url: "../db.jsp",
		data: "text=" + $("#submit-text").val() + "&model=" +checked + "&type=chat&correct="+correct+"&conv="+conversation,
		success: function(data) {
			alert("Success!");
		}
	});
}

function addText( e, text, model, server ) {

	if (e == null || e.type == "click" || e.keyCode == 13) {
		if (text != "silence")
			$("#model-"+model+"-body").prepend(prettyText(text, "You", "even"));

		$("#model-"+model+"-img").attr("src", "../../images/loading.gif");
		scenario = $("#scenario").val();

		$.ajax({
			url: server,
			data: "a=" + text + "&scenario=" + scenario,
			success: function(data) {
				$("#model-"+model+"-body").prepend(prettyText(data, "Model", "odd"));
				$("#model-"+model+"-img").attr("src", "");
			}
		});
		
		
		$("#model-"+model+"-input").val("");
	}
}

function prettyText( txt, who, cls ) {
	return "<p class='"+cls+"'><b>"+who+":</b>  "+txt+"</p>";
}

function cap(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

function toggleLoading() {

	display = $("#loading-gif").css("display");
	if (display == "block")
		$("#loading").css("display", "none");
	else 
		$("#loading").css("display", "block");
}
		
		
window.onload = init;
