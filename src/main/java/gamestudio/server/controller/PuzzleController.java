package gamestudio.server.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.id.enhanced.StandardOptimizerDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.format.datetime.joda.MillisecondInstantPrinter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import gamestudio.entity.Comment;
import gamestudio.entity.Rating;
import gamestudio.entity.Score;
import gamestudio.puzzle.core.Field;
import gamestudio.service.CommentService;
import gamestudio.service.RatingService;
import gamestudio.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)

public class PuzzleController {
	private Field field;
	private String message;
	public static final int EMPTY_TILE = 0;
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	Date date = new Date();
	Date startGameDate = null;
	public double rating;

	@Autowired
	private ScoreService scoreService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private RatingService ratingService;
	@Autowired
	private UserController userController;

	public String getMessage() {
		return message;
	}

	public double getRating() {
		rating = ratingService.getAverageRating("mines");
		return rating;
	}

	@RequestMapping("/puzzle")
	public String puzzle(@RequestParam(value = "tile", required = false) String tile, Model model) {
		processCommand(tile);
		fillModel(model);
		return "puzzle";
	}

	@RequestMapping("/puzzleComment")
	public String comment(@RequestParam(value = "newComment", required = false) String newComment, Model model) {
		commentService.addComment(new Comment(userController.getLoggedPlayer().getLogin(), "puzzle", newComment, date));
		fillModel(model);
		return "puzzle";
	}

	@RequestMapping("/puzzleRating")
	public String rating(@RequestParam(value = "newRating", required = false) String newRating, Model model) {		
		ratingService.setRating(new Rating(userController.getLoggedPlayer().getLogin(), "puzzle", Integer.parseInt(newRating)));
		
		fillModel(model);
		return "puzzle";
	}

	public void processCommand(String tile) {
		try {
			field.moveTile(Integer.parseInt(tile));

			if (field.isSolved()) {
				message = "YOU WON";
				if (userController.isLogged()) {
					scoreService.addScore(
							new Score(userController.getLoggedPlayer().getLogin(), "puzzle", computeFinalScore()));
				}
			}
		} catch (NumberFormatException e) {
			startNewGame();
		}
	}

	public void fillModel(Model model) {
		model.addAttribute("puzzleController", this);
		model.addAttribute("scores", scoreService.getTopScores("puzzle"));
		model.addAttribute("comment", commentService.getComments("puzzle"));
		model.addAttribute("rating", ratingService.getAverageRating("puzzle"));
	}

	public String render() {

		StringBuilder sb = new StringBuilder();

		sb.append("<table class = 'tablePuzzle'>\n");
		for (int row = 0; row < field.getRowCount(); row++) {
			sb.append("<tr>\n");
			for (int column = 0; column < field.getColumnCount(); column++) {
				int tile = field.getTile(row, column);
				sb.append("<td>\n");
				if (!field.isSolved())
					sb.append(String.format("<a href='/puzzle?tile=%d'>\n", tile));
				if (tile != EMPTY_TILE)
					sb.append(tile);
				sb.append("</a>\n");
				sb.append("</td>\n");
			}
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		return sb.toString();
	}

	private void startNewGame() {
		startGameDate = new Date();
		createField();
	}

	private void createField() {
		field = new Field(2, 2);
		message = "";
	}

	private int computeFinalScore() {
		int gameDuration = (int) ((System.currentTimeMillis() - startGameDate.getTime()) / 1000);
		return (1000 - gameDuration);
	}

}
