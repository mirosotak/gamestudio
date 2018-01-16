drawScoreTable();

function drawScoreTable() {
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/rest/score/mines",
		dataType : "json"
	}

	).done(function(receivedData) {
		var scoreTemplate = $("#tmplScores").html();
		var htmlCode = Mustache.render(scoreTemplate, receivedData);
		$("#scoreTable").html(htmlCode);

	}).fail(function() {
		$("#scoreTable").html("<p>Sorry, unable to get data</p>");
	});
}

$("#btSaveScore").click(function() {
	var score = parseInt($("#inNewScore").val().trim());
	console.log(score);

	if (isNaN(score)) {
		window.alert("Bad input!");
		return;
	}

	var data2send = {
		username : "Anonymous",
		game : "mines",
		value : score
	}

	$.ajax({
		type : "POST",
		url : "http://localhost:8080/rest/score",
		contentType : "application/json",
		data : JSON.stringify(data2send)
	}

	).done(function() {
		drawScoreTable();
	}).fail(function() {
		window.alert("Unable to send data!");
	});
}

);