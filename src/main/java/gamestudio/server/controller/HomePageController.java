package gamestudio.server.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import gamestudio.entity.Favorites;
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
	public String handleHomePage(Model model) {

		List<GameInfo> gameInfos = new ArrayList<>();
		gameInfos.add(prepareGameInfoData("mines"));
		gameInfos.add(prepareGameInfoData("puzzle"));
		gameInfos.add(prepareGameInfoData("guessNumber"));

		System.out.println("gameInfos:" + gameInfos);

		model.addAttribute("gameInfos", gameInfos);
		model.addAttribute("isLogged", userController.isLogged());

		return "homePage";

	}

	private GameInfo prepareGameInfoData(String gameName) {
		GameInfo gameInfo = new GameInfo();

		if (userController.isLogged()) {
			Favorites favorites = new Favorites(userController.getLoggedPlayer().getLogin(), gameName);
			gameInfo.setFavorite(favoriteService.isFavorite(favorites));
		}

		gameInfo.setGameName(gameName);
		gameInfo.setBestScore(scoreService.getBestScore(gameInfo.getGameName()));
		gameInfo.setAverageRating(ratingService.getAverageRating(gameInfo.getGameName()));

		return gameInfo;

	}
}
