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

	@RequestMapping("/guessNumberStart")
	public String startGame(@RequestParam(value = "maxNumber", required = false) Integer maxNumber, Model model) {
		handleStartGameAction(maxNumber);
		fillModel(model);
		return "guessNumber";

	}

	@RequestMapping("/guessNumber")
	public String guess(@RequestParam(value = "guessNumber", required = false) Integer guessNumber, Model model) {
		//handleGuessNumberAction(guessNumber, model);
		fillModel(model);
		return "guessNumber";

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

//	 public void processCommand(Integer maxNumber) {
//	 int turns = 0;
//	 	 try {
//			 if (userController.isLogged()) {
//				  handleStartGameAction(maxNumber);
//				  handleGuessNumberAction(guessNumber, model);
//				  
//				 }
//				 turns++;
//			 }
//	 	} catch (NumberFormatException e) {
//	 		 handleStartGameAction(maxNumber);
//	 		 }
//	 		 
//			 
			 
	

	public void fillModel(Model model) {
		model.addAttribute("guessNumberController", this);
		model.addAttribute("scores", scoreService.getTopScores("guessNumber"));
		model.addAttribute("comment", commentService.getComments("guessNumber"));
		model.addAttribute("rating", ratingService.getAverageRating("guessNumber"));
	}

	private void handleStartGameAction(Integer maxNumber) {
		if (maxNumber == null) {
			maxNumber = 1000;
		}
		generateAndSetRandomNumber(maxNumber);
	}

	private void handleGuessNumberAction(Integer guessNumber, Model model) {

		GuessResult result = null;
		if (guessNumber.equals(number)) {
			result = GuessResult.WON;
			message = "Congrats, you are the winner";

		} else if (guessNumber.compareTo(number) > 0) {
			result = GuessResult.GREATER;
			message = "Number is to high, tray again";

		} else {
			result = GuessResult.LESS;
			message = "Number is to low, tray again";
		}

		model.addAttribute("result", result);
	}

	private void generateAndSetRandomNumber(int maxNumber) {
		number = rand.nextInt(maxNumber) + 1;
	}

	enum GuessResult {
		WON, GREATER, LESS;
	}

	public Boolean isFavorite() {
		if (userController.isLogged()) {
			Favorites favorites = new Favorites(userController.getLoggedPlayer().getLogin(), "guessNumber");
			return favoriteService.isFavorite(favorites);
		}
		return null;

	}

}
