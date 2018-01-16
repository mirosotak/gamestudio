package gamestudio.server.controller;

import java.util.Random;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class GuessNumberController {
	
	Random rand = new Random();
	
	Integer number = null;

	@RequestMapping("/guess-number/start")
	public String startGame(@RequestParam(value = "maxNumber", required = false) Integer maxNumber, Model model) {

		handleStartGameAction(maxNumber);
		return "guessNumber";

	}

	@RequestMapping("/guess-number")
	public String guess(@RequestParam(value = "guessNumber", required = true) Integer guessNumber, Model model) {
		
		handleGuessAction(guessNumber, model);
		return "guessNumber";
		
	}
	
	private void handleStartGameAction(Integer maxNumber) {	
		if (maxNumber == null) {
			maxNumber = 100;
		}
		
		generateAndSetRandomNumber(maxNumber);
	}
	
	private void handleGuessAction(Integer guessNumber, Model model) {
		
		GuessResult result = null;
		if (guessNumber.equals(number)) {
			result = GuessResult.WON;
		} else if (guessNumber.compareTo(number) > 0) {
			result = GuessResult.GREATER;
		} else {
			result = GuessResult.LESS;
		}
		
		model.addAttribute("result", result);
	}
	
	private void generateAndSetRandomNumber(int maxNumber) {
		number = rand.nextInt(maxNumber) + 1;
	}
	
	enum GuessResult {
		WON, GREATER, LESS;
	}
}
