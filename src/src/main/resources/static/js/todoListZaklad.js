var staticTasks = '[{"id":"1","task":"Write Europass CV as web page","isDone":true},{"id":"2","task":"Implement TODO list","isDone":false},{"id":"3","task":"Do final assignment","isDone":false}]';

var tasks = JSON.parse(staticTasks);

//localStorage.setItem("myToDoList", staticTasks)
var tasks = JSON.parse(localStorage.getItem("myToDoList"));

if (tasks == null)
	tasks = [];

var taskTemplate = $("#tmplTask").html();

function addTaskToHtml(task) {
	if (task) {

		$element = $(Mustache.render(taskTemplate, task));

		$("#frmTasks").append($element);

		if (task.isDone) {
			$element.removeClass("activeTask");
			$element.addClass("completedTask");
		}

		$element.click(function() {
			$(this).toggleClass("activeTask");
			$(this).toggleClass("completedTask");
			for (var i = 0, len = tasks.length; i < len; i++) {
				if ($(this).attr('data-id') == tasks[i].id) {
					tasks[i].isDone = !tasks[i].isDone;
				}
			}
		});
	}
}

// --------------------------------------

// 1.nacitanie uloh
for (var i = 0, len = tasks.length; i < len; i++) {
	addTaskToHtml(tasks[i]);
}
/*
 * //Alternatíva predch. cyklu pre EcmaScript 6 for(var task of tasks){
 * addTaskToHtml(task); }
 */
$("#btAddTask").click(function() {
	$newTaskInput = $("#inNewTask");

	var text = $newTaskInput.val().trim();

	if (text == "")
		return;

	var newTask = {
		id : Date.now(),
		task : text,
		isDone : false
	};

	tasks.push(newTask);

	addTaskToHtml(newTask);

	$newTaskInput.val("");

	// console.log(newTask);

}

);

$("#btRemCmpl").click(function() {

	tasks = tasks.filter(function(task) {
		return !task.isDone;
	});
	// tasks = tasks.filter(task => !task.isDone);

	// for (var i = 0; i < tasks.length; i++) {
	// addTaskToHtml(tasks[i]);
	// if (tasks[i].isDone) {
	// tasks.splice(i, 1);
	// i--;
	// }

	// console.log(i + " done.");

	// $("#frmTasks").html("");
	$("#frmTasks").empty();
	for (var i = 0; i < tasks.length; i++) {
		addTaskToHtml(tasks[i]);
	}

});
