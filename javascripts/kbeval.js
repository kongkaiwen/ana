function init() {

	if (document.contains(document.getElementById("home-button"))) {
		home = document.getElementById("home-button");
		next = document.getElementById("next-button");
		home.addEventListener("click", send, false);
		next.addEventListener("click", send, false);
	}

	if (document.contains(document.getElementById("kb-dialogue"))) {

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

		content = document.getElementById("content");
		content.style.width = (window.innerWidth - 400 - 40) + "px";
		content.style.height = (window.innerHeight - 40) + "px";

		dialogue = document.getElementById("kb-dialogue");
		//dialogue.style.bottom = "175px";
		
		dialogue.style.width = "350px";
		dialogue.style.height = (window.innerHeight - 200 - 30) + "px";

		side = document.getElementById("sidebar");
		side.style.height = sidebarheight + "px";

		cydiv = document.getElementById("cy");
		cydiv.style.width = (window.innerWidth - 400 - 40)/2 + "px";
		cydiv.style.height = graphheight + "px";

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
			addText(e, $("#kb-dialogue-input").val(), "");
		}, false);

		button = document.getElementById("kb-dialogue-button");
		button.style.top = "150px";
		//button.style.left = (window.innerWidth/2) - (500/2) + (input.offsetWidth) + "px";
		//button.addEventListener("click", model_a_function, false);

		$.ajax({
	        url: "events.jsp",
	        success: function(data) {	
	            json = JSON.parse(data);

	            table = "<tr><th>ID</th><th>Event Name</th><th>Created On</th></tr>";
	            for(var i=0;i<json.length;i++) {
	                table += "<tr><td>ID</td><td>" + json[i]["name"] +"</td><td>"+ json[i]["created"] + "</td></tr>";
	            }

	            $("#events").html(table);
	            $("#daily").html(table);
	            $("#medical").html(table);
	        }
	    });

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
            
            cy.on('tap', function(e){
              if( e.cyTarget === cy ){
                cy.elements().removeClass('faded');
              }
            });
          }
        });

		var getGraph = function() {$.ajax({url: "kb.jsp",success: function(data) {	cy.load(JSON.parse(data));}})};
        $("#load").click(getGraph);

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

	// {"missing": {"boolean": true, "text": text}, "incorrect": {"boolean": true, "text": text}}
		
	$.ajax({
		url: "db.jsp",
		data: "type=kbeval&missing="+checked1+"&incorrect="+checked2+"&missing_text="+missing_txt+"&incorrect_text="+incorrect_txt,
		success: function(data) {
			//var json = JSON.parse(data);
			alert(data);
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

		if (text != "silence")
			$("#kb-dialogue-body").prepend(prettyText(text, "You", "even"));

/*		$.ajax({
			url: server,
			data: "a=" + text,
			success: function(data) {
				$("#kb-dialogue-body").prepend(prettyText(data, "Model", "odd"));
			}
		});*/
		
		
		$("#kb-dialogue-input").val("");
	}
}

window.onload = init;