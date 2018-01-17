package gamestudio.server.controller;

import java.util.Date;
import java.util.Random;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class GuessNumberControllerS {
	
	Random rand = new Random();
	
	Integer number = null;
	GameState gameState = new GameState();

	@RequestMapping("/guess-number")
	public String handleGamePage(Model model) {
		return "guessNumber";
	}
	
	@RequestMapping(path="/guess-number/start", method=RequestMethod.POST)
	public String startGame(@RequestParam(value = "maxNumber", required = false) Integer maxNumber, Model model) {

		handleStartGameAction(maxNumber, model);
		return "guessNumber";

	}

	@RequestMapping(path="/guess-number", method=RequestMethod.POST)
	public String guess(@RequestParam(value = "guessNumber", required = true) Integer guessNumber, Model model) {
		
		handleGuessAction(guessNumber, model);
		return "guessNumber";
		
	}
	
	private void handleStartGameAction(Integer maxNumber, Model model) {	
		if (maxNumber == null) {
			maxNumber = 100;
		}
		
		generateAndSetRandomNumber(maxNumber);
		
		gameState.setAttempts(0);
		gameState.setActive(true);
		gameState.setLastAttemptResult(null);
		gameState.setStartedDate(new Date());
		
		model.addAttribute("gameState", gameState);
	}
	
	private void handleGuessAction(Integer guessNumber, Model model) {
		
		GuessResult result = null;
		if (guessNumber.equals(number)) {
			result = GuessResult.WON;
			gameState.setActive(false);
		} else if (guessNumber.compareTo(number) > 0) {
			result = GuessResult.GREATER;
		} else {
			result = GuessResult.LESS;
		}
		
		gameState.setAttempts(gameState.getAttempts() + 1);
		gameState.setLastAttemptResult(result.name());
		
		model.addAttribute("gameState", gameState);
	}
	
	private void generateAndSetRandomNumber(int maxNumber) {
		number = rand.nextInt(maxNumber) + 1;
	}
	
	public enum GuessResult {
		WON, GREATER, LESS;
	}
	
	public class GameState {
		private boolean active;
		private int attempts;
		private Date startedDate;
		private String lastAttemptResult;
		
		
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
		
		
		
	}
}
