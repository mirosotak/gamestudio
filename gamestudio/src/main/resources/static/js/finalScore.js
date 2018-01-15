
drawScoreTableMines();
drawScoreTable15Puzzle();

//var gameName = "";
//var game = "";

// function valueFromGameSelector() {
// gameName = document.getElementById("#gameSelector");
// game = gameName.options[gameName.selectedIndex].text;
// }

function drawScoreTableMines() {
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/rest/score/mines",
		dataType : "json"
	}

	).done(function(receivedData) {
		var scoreTemplate = $("#tmplScores").html();
		
		var htmlCode = Mustache.render(scoreTemplate, receivedData);
		console.log(htmlCode);
		$("#scoreTableMines").html(htmlCode);
		console.log("konec");

	}).fail(function() {
		$("#scoreTableMines").html("<p>Sorry, unable to get data</p>");
	});
}



function drawScoreTable15Puzzle() {
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/rest/score/puzzle",
		dataType : "json"
	}

	).done(function(receivedData) {
		console.log(receivedData);
		var scoreTemplate = $("#tmplScores").html();
		var htmlCode = Mustache.render(scoreTemplate, receivedData);
		$("#scoreTablePuzzle").html(htmlCode);

	}).fail(function() {
		$("#scoreTablePuzzle").html("<p>Sorry, unable to get data</p>");
	});
}



$("#btSaveScore").click(function() {
	var score = parseInt($("#newScore").val().trim());
	var userName = $("#userName").val().trim();
	var game = $("#gameSelector").val().trim();

	console.log(score);
	console.log(userName);

	if (isNaN(score)) {
		window.alert("Wrong score input!");
		return;
	}

	var data2send = {
		username : userName,
		game : game,
		value : score
	}

	$.ajax({
		type : "POST",
		url : "http://localhost:8080/rest/score/mines",
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