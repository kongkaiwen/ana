function init2() {

	if (document.contains(document.getElementById("dialogue"))) {
		modelA = document.getElementById("dialogue");
		modelB = document.getElementById("questions");
		
		//modelA.style.width = (window.innerWidth/2) + "px";
		//modelB.style.width = (window.innerWidth/2) + "px";
		
		w = 750;
		h = 750;
		
		modelA.style.width = w + "px";
		modelB.style.width = h + "px";
		
		//gapsize = 50;
		//boxsize = parseInt(modelA.style.width.slice(0,-2));
		
		//modelA.style.left = (window.innerWidth/2 - boxsize) - (gapsize/2) + "px";
		//modelB.style.left = (window.innerWidth/2) + (gapsize/2) + "px";
		
		modelA.style.left = (window.innerWidth/2 - w/2) + "px";
		modelB.style.left = (window.innerWidth/2 - w/2) + "px";

		home = document.getElementById("home-button");
		next = document.getElementById("next-button");

		//home.style.left = (window.innerWidth/2 - w/2) + "px";
		//next.style.left = (window.innerWidth/2 + w/2) + "px";

		home.style.left = (window.innerWidth/2 - home.offsetWidth) + "px";
		next.style.left = (window.innerWidth/2) + "px";

		home.addEventListener("click", send, false);
		next.addEventListener("click", send, false);

		submit = document.getElementById("submit-results");
		submit.addEventListener("click", sendQuestionRating, false);
	}
		
}

function send( e ) {
	window.location.href = e.target.getAttribute("href");
}

function sendQuestionRating() {

	docid = document.getElementById("docid").value;

	/*
		{
		    {docid: 1, qid: 1, good: 0, best: 0},
		    {docid: 1, qid: 2, good: 0, best: 0},
		    {docid: 1, qid: 3, good: 1, best: 1},
		    {docid: 1, qid: 4, good: 0, best: 0}
		}
	*/
	frm = document.getElementById("eval-form");
	
	//alert(frm.Rating.length);
	if (frm.kevin.length == 0) 
		return;
		
	radio_hsh = {};
	for(var i=0;i<frm.length;i++) {

		if (frm.kevin[i] == undefined)
			continue;

		qid = frm.kevin[i].getAttribute("qid");
		if (frm.kevin[i].checked) {
			radio_hsh[qid] = 1;
		} else {
			radio_hsh[qid] = 0;
		}
	}

	checkboxes = document.getElementsByName("kevin-check");
	check_hsh = {};
	for(var i=0; i<checkboxes.length; i++) {

		if (checkboxes[i] == undefined)
			continue;

		qid = frm.kevin[i].getAttribute("qid");
		if (checkboxes[i].checked) {
			check_hsh[qid] = 1;
		} else {
			check_hsh[qid] = 0;
		}
	}

	//alert(Object.keys(radio_hsh).map(function(x){return x+": "+radio_hsh[x];}).join(', '));
	//alert(Object.keys(check_hsh).map(function(x){return x+": "+check_hsh[x];}).join(', '));

	output = [];
	for(var key in radio_hsh) {
		ajax_hsh = {};
		ajax_hsh['docid'] = docid;
		ajax_hsh['qid'] = key;
		ajax_hsh['good'] = check_hsh[key];
		ajax_hsh['best'] = radio_hsh[key];
		output.push(ajax_hsh);
	}


	//alert(output.length);
	//alert(output.map(function(hsh) {return hashToString(hsh)}).join("::"));
	//alert(output.map(function(hsh) {return Object.keys(hsh).map(function(x){return x+": "+hsh[x];}).join(', ')}).join('::'));
		
	$.ajax({
		url: "ratings.jsp",
		data: {json:JSON.stringify(output)},
		success: function(data) {
			//var json = JSON.parse(data);
			alert(data);
		}
	});
}

window.onload = init2;