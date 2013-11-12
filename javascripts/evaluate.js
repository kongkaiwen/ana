
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

	if (document.contains(document.getElementById("model-a"))) {

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

		var model_a_function = function(e) {
			silence = 0;
			addText( e, $("#model-a-input").val(), "a", "server.jsp" );
		};

		var model_b_function = function(e) {
			addText( e, $("#model-b-input").val(), "b", "bot.jsp" );
		};

		input = document.getElementById("model-a-input");
		input.style.left = (window.innerWidth/2 - boxwidth) - (gapsize/2) + "px";
		input.style.top = input_top + "px";
		input.addEventListener("keypress", model_a_function, false);

		button = document.getElementById("model-a-button");
		button.style.left = (window.innerWidth/2 - boxwidth) - (gapsize/2) + (465)+10 + "px";
		button.style.top = input_top + "px";
		button.addEventListener("click", model_a_function, false);

		input = document.getElementById("model-b-input");
		input.style.left = (window.innerWidth/2) + (gapsize/2) + "px";
		input.style.top = input_top + "px";
		input.addEventListener("keypress", model_b_function, false);

		button = document.getElementById("model-b-button");
		button.style.left = (window.innerWidth/2) + (gapsize/2)  + (465)+10 + "px";
		button.style.top = input_top + "px";
		button.addEventListener("click", model_b_function, false);

		rating_button = document.getElementById("submit-radio");
		rating_button.addEventListener("click", sendRating, false);

		input.addEventListener("keyup", function(e) {
			if (e.type != "click" & e.keyCode != 13) {
				$("#model-b-input").val($("#model-a-input").val());
			}
		}, false);

        $('#cy').cytoscape({
          style: cytoscape.stylesheet()
            .selector('node')
              .css({
                'content': 'data(name)',
                'text-valign': 'center',
                'color': 'white',
                'text-outline-width': 2,
                'text-outline-color': '#888'
              })
            .selector('edge')
              .css({
                'target-arrow-shape': 'triangle'
              })
            .selector(':selected')
              .css({
                'background-color': 'black',
                'line-color': 'black',
                'target-arrow-color': 'black',
                'source-arrow-color': 'black'
              })
            .selector('.faded')
              .css({
                'opacity': 0.25,
                'text-opacity': 0
              }),
          
          elements: {
            nodes: [
              { data: { id: 'j', name: 'Jerry' } },
              { data: { id: 'e', name: 'Elaine' } },
              { data: { id: 'k', name: 'Kramer' } },
              { data: { id: 'g', name: 'George' } }
            ],
            edges: [
              { data: { source: 'j', target: 'e' } },
              { data: { source: 'j', target: 'k' } },
              { data: { source: 'j', target: 'g' } },
              { data: { source: 'e', target: 'j' } },
              { data: { source: 'e', target: 'k' } },
              { data: { source: 'k', target: 'j' } },
              { data: { source: 'k', target: 'e' } },
              { data: { source: 'k', target: 'g' } },
              { data: { source: 'g', target: 'j' } }
            ]
          },
          
          ready: function(){
            window.cy = this;
            
            cy.elements().unselectify();

            cy.on('tap', 'node', function(e) {
                var node = e.cyTarget;
                alert(node.json()["data"]["age"] + "\n" + node.json()["data"]["likes"]);
            });

            cy.on('tap', 'edge', function(e) {
                var edge = e.cyTarget;
                alert(edge.json()["data"]["relation"]);
            });
            
            cy.on('tap', function(e){
              if( e.cyTarget === cy ){
                cy.elements().removeClass('faded');
              }
            });
          }
        });

		var getGraph = function() {$.ajax({url: "../kb.jsp",success: function(data) {	cy.load(JSON.parse(data));}})};
        $("#load").click(getGraph);

	} else {
		thumbs = document.getElementsByClassName("scenario-thumb");
		for(var i=0;i<thumbs.length;i++) {
			thumbs[i].addEventListener("click", go, false);
		}
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

	for (var obj in json) {

		data = json[obj];

		table = "<tr>";
		for(var key in data[0])
			table += "<th>" + cap(key) + "</th>";
		table += "</tr>"

	    for(var i=0;i<data.length;i++) {
	        row = "<tr>";
	        for(var key in data[i])
	        	row += "<td>" + data[i][key] + "</td>";
	        table += row + "</tr>";
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
	// {"model": value, "description": text} 

	checked = $('input[name="Rating"]:checked').val();

	$.ajax({
		url: "db.jsp",
		data: "text=" + $("#submit-text").val() + "&model=" +checked + "&type=chat",
		success: function(data) {
			//var json = JSON.parse(data);
			alert(data);
		}
	});
}

function addText( e, text, model, server ) {

	if (e == null || e.type == "click" || e.keyCode == 13) {
		//$("#model-a-body").prepend(prettyText($("#text1").val(), "You", "even"));
		//$("#model-b-body").prepend(prettyText($("#text1").val(), "You", "even"));
		if (text != "silence")
			$("#model-"+model+"-body").prepend(prettyText(text, "You", "even"));

		$("#model-"+model+"-img").attr("src", "../images/loading.gif");
		
		$.ajax({
			url: server,
			data: "a=" + text,
			success: function(data) {
				//var json = JSON.parse(data);
				
				//$("#model-a-body").prepend(prettyText(json["modela"], "Model", "odd"));
				//$("#model-b-body").prepend(prettyText(json["modelb"], "Model", "odd"));
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
		
		
window.onload = init;
