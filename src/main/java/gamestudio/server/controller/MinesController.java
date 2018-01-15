package gamestudio.server.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import gamestudio.entity.Comment;
import gamestudio.entity.Favorites;
import gamestudio.entity.Rating;
import gamestudio.entity.Score;
import gamestudio.game.minesweeper.core.Clue;
import gamestudio.game.minesweeper.core.Field;
import gamestudio.game.minesweeper.core.GameState;
import gamestudio.game.minesweeper.core.Tile;
import gamestudio.game.minesweeper.core.TileState;
import gamestudio.service.CommentService;
import gamestudio.service.FavoritesService;
import gamestudio.service.RatingService;
import gamestudio.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)

public class MinesController {
	private Field field = new Field(9, 9, 3);
	private boolean marking;
	private String message;
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
	private FavoritesService favoriteService;

	@Autowired
	private UserController userController;

	public String getMessage() {
		return message;
	}

	public double getRating() {
		rating = ratingService.getAverageRating("mines");
		return rating;
	}

	public boolean isMarking() {
		return marking;
	}

	@RequestMapping("/mines_mark")
	public String minesMark(Model model) {
		marking = !marking;

		fillModel(model);
		return "mines";

	}

	@RequestMapping("/favorite")
	public String handleFavorite(@RequestParam(value = "gameName", required = true) String gameName, Model model) {
		
		Favorites favorites = new Favorites(userController.getLoggedPlayer().getLogin(), gameName);
		favoriteService.addFavorite(favorites);
		return "homePage";
		
	}

	@RequestMapping("/mines")
	public String mines(@RequestParam(value = "row", required = false) String row,
			@RequestParam(value = "column", required = false) String column, Model model) {

		processCommand(row, column);
		fillModel(model);
		return "mines";

	}

	@RequestMapping("/minesComment")
	public String comment(@RequestParam(value = "newComment", required = false) String newComment, Model model) {
		commentService.addComment(new Comment(userController.getLoggedPlayer().getLogin(), "mines", newComment, date));
		fillModel(model);
		return "mines";
	}

	@RequestMapping("/minesRating")
	public String rating(@RequestParam(value = "newRating", required = false) String newRating, Model model) {
		ratingService.setRating(
				new Rating(userController.getLoggedPlayer().getLogin(), "mines", Integer.parseInt(newRating)));
		fillModel(model);
		return "mines";
	}

	public void processCommand(String row, String column) {

		try {
			if (marking)
				field.markTile(Integer.parseInt(row), Integer.parseInt(column));
			else
				field.openTile(Integer.parseInt(row), Integer.parseInt(column));

			if (field.getState() == GameState.FAILED) {
				message = "FAILED";
			} else if (field.getState() == GameState.SOLVED) {
				message = "YOU WON";
				if (field.isSolved()) {
					message = "YOU WON";
					if (userController.isLogged()) {
						scoreService.addScore(
								new Score(userController.getLoggedPlayer().getLogin(), "mines", computeFinalScore()));

					}
				}
			}
		} catch (NumberFormatException e) {
			startNewGame();

		}
	}

	public void fillModel(Model model) {
		model.addAttribute("minesController", this);
		model.addAttribute("scores", scoreService.getTopScores("mines"));
		model.addAttribute("comment", commentService.getComments("mines"));
		model.addAttribute("rating", ratingService.getAverageRating("mines"));
	}

	public String render() {
		StringBuilder sb = new StringBuilder();

		sb.append("<table class = 'tableMines'>\n");
		for (int row = 0; row < field.getRowCount(); row++) {
			sb.append("<tr>\n");
			for (int column = 0; column < field.getColumnCount(); column++) {
				Tile tile = field.getTile(row, column);
				String image = null;

				switch (tile.getState()) {
				case CLOSED:
					image = "closed";
					break;
				case MARKED:
					image = "marked";
					break;
				case OPEN:
					if (tile instanceof Clue)
						image = "open" + ((Clue) tile).getValue();
					else
						image = "mine";
					break;

				}

				sb.append("<td>\n");
				if (tile.getState() != TileState.OPEN && field.getState() == GameState.PLAYING)
					sb.append(String.format("<a href='/mines?row=%d&column=%d'>\n", row, column));
				sb.append("<img src='/obr/mines/" + image + ".png'>\n");
				if (tile.getState() != TileState.OPEN && field.getState() == GameState.PLAYING)
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
		field = new Field(9, 9, 2);
		message = "";
	}

	private int computeFinalScore() {
		int gameDuration = (int) ((System.currentTimeMillis() - startGameDate.getTime()) / 1000);
		return (1000 - gameDuration);
	}

}
