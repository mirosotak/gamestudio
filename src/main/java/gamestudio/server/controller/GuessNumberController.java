package gamestudio.server.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
import gamestudio.service.CommentService;
import gamestudio.service.FavoritesService;
import gamestudio.service.RatingService;
import gamestudio.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class GuessNumberController {

	Random rand = new Random();
	Integer number = null;
	GameState gameState = new GameState();
	Date date = new Date();
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	public double rating;
	public String message;

	public String getMessage() {
		return message;
	}

	@Autowired
	private ScoreService scoreService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private RatingService ratingService;
	@Autowired
	private UserController userController;
	@Autowired
	private FavoritesService favoriteService;

	@RequestMapping("/guessNumber")
	public String guess(@RequestParam(value = "maxNumber", required = false) String maxNumberInput,
			@RequestParam(value = "guessNumber", required = false) String guessNumberInput, Model model) {

		fillModel(model);

		if (maxNumberInput != null && !isPositiveInteger(maxNumberInput)) {
			model.addAttribute("error", "Invalid number: " + maxNumberInput);
			return "guessNumber";
		}
		if (guessNumberInput != null && !isPositiveInteger(guessNumberInput)) {
			model.addAttribute("error", "Invalid number: " + guessNumberInput);
			return "guessNumber";
		}

		if (maxNumberInput != null) {
			int maxNumber = Integer.parseInt(maxNumberInput);

			if (maxNumber == 0) {
				model.addAttribute("error", "Invalid number: " + maxNumberInput);
				return "guessNumber";
			}

			handleStartGameAction(maxNumber, model);
		}

		if (guessNumberInput != null) {
			int guessNumber = Integer.parseInt(guessNumberInput);
			handleGuessNumberAction(guessNumber, model);
		}

		return "guessNumber";

	}

	private boolean isPositiveInteger(String input) {
		return input.matches("([0-9])*");
	}

	@RequestMapping("/guessNumberComment")
	public String comment(@RequestParam(value = "newComment", required = false) String newComment, Model model) {
		commentService
				.addComment(new Comment(userController.getLoggedPlayer().getLogin(), "guessNumber", newComment, date));
		fillModel(model);
		return "guessNumber";
	}

	@RequestMapping("/guessNumberRating")
	public String rating(@RequestParam(value = "newRating", required = false) String newRating, Model model) {
		ratingService.setRating(
				new Rating(userController.getLoggedPlayer().getLogin(), "guessNumber", Integer.parseInt(newRating)));

		fillModel(model);
		return "guessNumber";
	}

	@RequestMapping("/guessNumberFavorite")
	public String handleFavorite(Model model) {

		Favorites favorites = new Favorites(userController.getLoggedPlayer().getLogin(), "guessNumber");
		favoriteService.addFavorite(favorites);

		fillModel(model);
		return "guessNumber";
	}

	public Boolean isFavorite() {
		if (userController.isLogged()) {
			Favorites favorites = new Favorites(userController.getLoggedPlayer().getLogin(), "guessNumber");
			return favoriteService.isFavorite(favorites);
		}
		return null;

	}

	public void fillModel(Model model) {
		model.addAttribute("guessNumberController", this);
		model.addAttribute("scores", scoreService.getTopScores("guessNumber"));
		model.addAttribute("comment", commentService.getComments("guessNumber"));
		model.addAttribute("rating", ratingService.getAverageRating("guessNumber"));
	}

	private void handleStartGameAction(Integer maxNumber, Model model) {
		if (maxNumber == null) {
			maxNumber = 1000;
		}

		generateAndSetRandomNumber(maxNumber);

		gameState.setAttempts(0);
		gameState.setActive(true);
		gameState.setLastAttemptResult(null);
		gameState.setStartedDate(new Date());
		gameState.setMaxNumber(maxNumber);

		model.addAttribute("gameState", gameState);

	}

	private void handleGuessNumberAction(Integer guessNumber, Model model) {

		GuessResult result = null;

		if (guessNumber.equals(number)) {
			result = GuessResult.WON;

			if (userController.isLogged()) {
				scoreService.addScore(
						new Score(userController.getLoggedPlayer().getLogin(), "guessNumber", computeFinalScore()));

			}

		} else if (guessNumber.compareTo(number) > 0) {
			result = GuessResult.GREATER;

		} else {
			result = GuessResult.LESS;

		}

		gameState.setAttempts(gameState.getAttempts() + 1);
		gameState.setLastAttemptResult(result.name());
		gameState.setLastAttemptNumber(guessNumber);

		model.addAttribute("gameState", gameState);
	}

	private int computeFinalScore() {
		return (this.gameState.maxNumber - this.gameState.attempts) * 100;
	}

	private void generateAndSetRandomNumber(int maxNumber) {
		number = rand.nextInt(maxNumber) + 1;
	}

	enum GuessResult {
		WON, GREATER, LESS;
	}

	public class GameState {
		private boolean active;
		private int attempts;
		private Date startedDate;
		private String lastAttemptResult;
		private Integer maxNumber;
		private Integer lastAttemptNumber;

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public int getAttempts() {
			return attempts;
		}

		public void setAttempts(int attempts) {
			this.attempts = attempts;
		}

		public Date getStartedDate() {
			return startedDate;
		}

		public void setStartedDate(Date startedDate) {
			this.startedDate = startedDate;
		}

		public String getLastAttemptResult() {
			return lastAttemptResult;
		}

		public void setLastAttemptResult(String lastAttemptResult) {
			this.lastAttemptResult = lastAttemptResult;
		}

		public Integer getMaxNumber() {
			return maxNumber;
		}

		public void setMaxNumber(Integer maxNumber) {
			this.maxNumber = maxNumber;
		}

		public Integer getLastAttemptNumber() {
			return lastAttemptNumber;
		}

		public void setLastAttemptNumber(Integer lastAttemptNumber) {
			this.lastAttemptNumber = lastAttemptNumber;
		}

	}

}
