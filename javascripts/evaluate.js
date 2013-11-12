
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

		submitResult = document.getElementById("submit-result");
		submitResult.style.left = (window.innerWidth/2 - submitResult.offsetWidth/2 + 10) + "px";

		home.addEventListener("click", send, false);
		next.addEventListener("click", send, false);

		modelA = document.getElementById("model-a");
		modelB = document.getElementById("model-b");

		modelA.style.bottom = "175px";
		modelB.style.bottom = "175px";

		//modelA.style.width = (window.innerWidth/2) + "px";
		//modelB.style.width = (window.innerWidth/2) + "px";
		
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


		
		/*
		input = document.getElementById("text1");
		input.style.left = (window.innerWidth/2) - ((input.offsetWidth+45)/2) + "px";
		input.style.bottom = (75 + boxwidth) + "px";
		input.addEventListener("keypress", addText, false);
		
		button = document.getElementById("text2");
		button.style.left = (window.innerWidth/2) - ((input.offsetWidth+45)/2) + (500)+10 + "px";
		button.style.bottom = (75 + boxwidth) + "px";
		button.addEventListener("click", addText, false);
		*/

		var model_a_function = function(e) {
			silence = 0;
			addText( e, $("#model-a-input").val(), "a", "server.jsp" );
		};

		var model_b_function = function(e) {
			addText( e, $("#model-b-input").val(), "b", "bot.jsp" );
		};

		input = document.getElementById("model-a-input");
		input.style.left = (window.innerWidth/2 - boxwidth) - (gapsize/2) + "px";
		//input.style.bottom = (75 + boxwidth) + "px";
		input.style.bottom = (bottomgap + boxheight + input.offsetHeight - 10) + "px";
		input.addEventListener("keypress", model_a_function, false);

		input.addEventListener("keyup", function(e) {
			if (e.type != "click" & e.keyCode != 13) {
				$("#model-b-input").val($("#model-a-input").val());
			}
		}, false);

		button = document.getElementById("model-a-button");
		button.style.left = (window.innerWidth/2 - boxwidth) - (gapsize/2) + (465)+10 + "px";
		button.style.bottom = (bottomgap + boxheight + input.offsetHeight - 10) + "px";
		button.addEventListener("click", model_a_function, false);

		input = document.getElementById("model-b-input");
		input.style.left = (window.innerWidth/2) + (gapsize/2) + "px";
		input.style.bottom = (bottomgap + boxheight + input.offsetHeight - 10) + "px";
		input.addEventListener("keypress", model_b_function, false);

		button = document.getElementById("model-b-button");
		button.style.left = (window.innerWidth/2) + (gapsize/2)  + (465)+10 + "px";
		button.style.bottom = (bottomgap + boxheight + input.offsetHeight - 10) + "px";
		button.addEventListener("click", model_b_function, false);

		rating_button = document.getElementById("submit-radio");
		rating_button.addEventListener("click", sendRating, false);

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

    w = 1000;
    h = 500;
	popup.style.width = w + "px";
    popup.style.height = h + "px";

	popup.style.top = (window.innerHeight/2) - (h/2) + document.body.scrollTop + "px";
    popup.style.left = (window.innerWidth/2) - (w/2) + "px";

	close = document.getElementById("close");
    close.addEventListener('click', closePopup, false);

    $.ajax({
        url: "events.jsp",
        success: function(data) {	
            json = JSON.parse(data);

            table = "<tr><th>ID</th><th>Event Name</th><th>Created On</th></tr>";
            for(var i=0;i<json.length;i++) {
                table += "<tr><td>ID</td><td>" + json[i]["name"] +"</td><td>"+ json[i]["created"] + "</td></tr>";
            }

            $("#events").html(table);
        }
    });
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

		$.ajax({
			url: server,
			data: "a=" + text,
			success: function(data) {
				//var json = JSON.parse(data);
				
				//$("#model-a-body").prepend(prettyText(json["modela"], "Model", "odd"));
				//$("#model-b-body").prepend(prettyText(json["modelb"], "Model", "odd"));
				$("#model-"+model+"-body").prepend(prettyText(data, "Model", "odd"));
			}
		});
		
		
		$("#model-"+model+"-input").val("");
	}
}

function prettyText( txt, who, cls ) {
	return "<p class='"+cls+"'><b>"+who+":</b>  "+txt+"</p>";
}
		
		
window.onload = init;
