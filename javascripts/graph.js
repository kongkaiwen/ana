function init_graph( nodes, edges ) {
    return $('#cy').cytoscape({
        elements: { // TODO specify some elements like http://cytoscapeweb.cytoscape.org/demos/simple
            nodes: nodes,
            edges: edges
        },
        
        pan: { x: 100, y: 100 },
/*
        // TODO specify a nice style like http://cytoscapeweb.cytoscape.org/demos/simple
        style: cytoscape.stylesheet()
          .selector("node")
                .css({
	                "content": "data(label)",
	                "shape": "data(shape)",
	                "border-width": 3,
	                "background-color": "#DDD",
	                "border-color": "#555",
                    "text-valign": "center",
                    "text-halign": "center",
                    "width": "75",
                    "height": "75",
                })
            .selector("edge")
                .css({
	                "content": "data(label)",
	                "width": "mapData(weight, 0, 100, 1, 4)",
	                "target-arrow-shape": "triangle",
	                "source-arrow-shape": "circle",
	                "line-color": "#aaa",
                    "text-outline-width": "1px",
                })
            .selector(":selected")
                .css({
	                "background-color": "#000",
	                "line-color": "#000",
	                "source-arrow-color": "#000",
	                "target-arrow-color": "#000"
                })
            .selector(".ui-cytoscape-edgehandles-source"    )
                .css({
	                "border-color": "#5CC2ED",
	                "border-width": 3
                })
            .selector(".ui-cytoscape-edgehandles-target, node.ui-cytoscape-edgehandles-preview")
                .css({
	                "background-color": "#5CC2ED"
                })
            .selector("edge.ui-cytoscape-edgehandles-preview")
                .css({
	                "line-color": "#5CC2ED"
                })
            .selector("node.ui-cytoscape-edgehandles-preview, node.intermediate")
                .css({
	                "shape": "rectangle",
	                "width": 15,
	                "height": 15
                })
        ,
*/	
        //zoom: 0,

        ready: function(){
            window.cy = this; // for debugging
          
          	var nodeCount = cy.nodes().length;
          	for (var i = 0; i < nodeCount; i++) { 		
          		var center = [cy.container().clientWidth / 2, cy.container().clientHeight / 2];  		
          		var angle = i / nodeCount * Math.PI * 2;
          		var radius = Math.min(cy.container().clientWidth, cy.container().clientHeight) / 2 * 0.6;
          		var nodePos = [Math.cos(angle) * radius + center[0], Math.sin(angle) * radius + center[1]]
          		cy.nodes()[i].position({x: nodePos[0], y : nodePos[1]});
        	}
        }
    });
}



$(document).ready(function(){ 

	$("#text1").bind("webkitspeechchange", function(evt) {
	  	alert($(this).val()); 
	 });  
	
	//aa = init_graph( [], [] );
             
    //alert(cy.nodes().data("id"));
	//cy.add({ group: "nodes", data: { id: "n0" }, position: { x: 100, y: 100 } });
    //alert(cy.nodes().data("id"));
    	
    	/*
	cy.on('click', function(event){
	  // cyTarget holds a reference to the originator
	  // of the event (core or element)
	  var evtTarget = event.cyTarget;
	
	  if( evtTarget === cy ){
	      //alert('click on background');
	  } else {
	      //alert('click on some element');
	  }
	});
	*/
	$('#text1').keyup(function(e){
		if(e.keyCode == 13) {
			$("#conversation").prepend("<p><img class='icon' src='images/question2.png'/>  You: "+$("#text1").val()+"</p>");
			$.ajax({
				url: "server.jsp",
				data: "a=" + $("#text1").val(),
				success: function(data) {

	                var json = JSON.parse(data);
			        
			        $("#knowledge-base").empty();
			        
			        if ( parseFloat(json["question"]) > 0 )
			        	$("#conversation p:first-child img").css("visibility","visible");
			         
			        var entityList = $("<ul>");
			        $("#knowledge-base").append(entityList);	
	                entityList.append($("<li>").text("Entities"));	
	                $.each( json['entities'], function( key, value ) {
	                	
						var e = $("<ul>");
						var attr = $("<ul>");
						e.append($("<li>").text(key));
						
						$.each( value, function( k, v ) {
							attr.append($("<li>").text(k + ": " + v));
						});
						
						e.append(attr);
						entityList.append(e);
	                });

					//"relations":[{"e2_id":"e2","id":"r1","relation":"famy","e1_id":"e1"},{"e2_id":"e2","id":"r2","relation":"famy","e1_id":"e1"}],
					
					var relationList = $("<ul>");
					$("#knowledge-base").append(relationList);
	                relationList.append($("<li>").text("Relations"));	
	                $.each( json['relations'], function( index, dict ) {
						var r = $("<ul>");
						var attr = $("<ul>");
						r.append($("<li>").text(dict["id"]));
						
						$.each( dict, function( k, v ) {
							attr.append($("<li>").text(k + ": " + v));
						});
						
						r.append(attr);
						relationList.append(r);	                
	                });
	                
	                $("#conversation").prepend("<p class='indent'>Ana: "+json['response']+"</p>").fadeIn();
	
				}
			});
		}
	});

    $("#clear").click(function() {
        $.ajax({
			url: "delete.jsp",
			success: function() {
                $("#cy").html("");
                $(".spoken").remove();
            }
        });
        
        $("#text1").val('');
        $("#conversation").empty();
        $("#knowledge-base").empty();
    });
    
    /*
    $('#text1').keyup(function(e){
		if(e.keyCode == 13) {
			$("#conversation").prepend("<p>You: "+$("#text1").val()+"</p>");
			$.ajax({
				url: "server.jsp",
				data: "a=" + $("#text1").val(),
				success: function(data) {
	
	                var kbNodes = [];
	                var kbEdges = [];
	
	                var json = JSON.parse(data);
	
	                $.each( json['entities'], function( key, value ) {
	                    kbNodes.push({
	                        data: {
		                        id: key,
		                        weight: Math.round( Math.random() * 100 ),
	                            shape: "circle",
	                            label: value["name"]           
	                        }
	                    });
	                });
	
	                $.each( json['relations'], function( index, dict ) {
	                    kbEdges.push({
	                        data: {
		                        id: dict['id'],
		                        source: dict['e1_id'],
		                        target: dict['e2_id'],
		                        weight: 30,
	                            label: dict['relation']
	                        }
	                    });
	                });
	                
	                $("#conversation").prepend("<p>Ana: "+json['response']+"</p>");
	                $("#cy").html("");
	                init_graph( kbNodes, kbEdges );
	
				}
			});
		}
	});
    */

});