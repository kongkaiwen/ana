var helpers = {

	goto: function ( e ) {
		window.location.href = e.target.getAttribute("href");
	},

	prettyText: function ( txt, who, cls ) {
		eturn "<p class='"+cls+"'><b>"+who+":</b>  "+txt+"</p>";
	}
};