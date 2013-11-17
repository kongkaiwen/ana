function initGraph() {
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
            clickNode(e);
        });

        cy.on('tap', 'edge', function(e) {
            clickEdge(e);
        });
        
        cy.on('tap', function(e){
          if( e.cyTarget === cy ){
            cy.elements().removeClass('faded');
          }
        });
      }
    });
}

function clickNode( e ) {
    var node = e.cyTarget;
    var json = node.json();

    output = "";
    for( var key in json["data"] ) {
        output += key + ": " + json["data"][key] + "\n";
    }

    alert(output);
}

function clickEdge( e ) {
    var edge = e.cyTarget;
    alert("relation: " + edge.json()["data"]["relation"]);
}
