package gamestudio.server.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import gamestudio.entity.GameInfo;
import gamestudio.service.FavoritesService;
import gamestudio.service.RatingService;
import gamestudio.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class HomePageController {

	@Autowired
	private ScoreService scoreService;
	
	@Autowired
	private UserController userController;
	
	@Autowired
	private FavoritesService favoriteService;
	
	@Autowired
	private RatingService ratingService;
	
	@RequestMapping("/homePage")
	public String minesMark(Model model) {

		List<GameInfo> gameInfos = new ArrayList<>(2);
		
		
		GameInfo gameMines = new GameInfo();
		gameMines.setGameName("mines");
		gameMines.setBestScore(scoreService.getBestScore(gameMines.getGameName()));
		gameMines.setFavorite(true);
		gameMines.setAverageRating(ratingService.getAverageRating(gameMines.getGameName()));
		
		
		GameInfo gamePuzzle = new GameInfo();
		gamePuzzle.setGameName("puzzle");
		gamePuzzle.setBestScore(scoreService.getBestScore(gamePuzzle.getGameName()));
		gamePuzzle.setFavorite(false);
		gamePuzzle.setAverageRating(ratingService.getAverageRating(gamePuzzle.getGameName()));
		
		gameInfos.add(gameMines);
		gameInfos.add(gamePuzzle);
		
		System.out.println("gameInfos:" + gameInfos);
		
		model.addAttribute("gameInfos", gameInfos);
		model.addAttribute("isLogged", userController.isLogged());
		
		return "homePage";

	}
}
