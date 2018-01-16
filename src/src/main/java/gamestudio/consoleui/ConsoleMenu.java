package gamestudio.consoleui;

import java.util.Date;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import gamestudio.entity.Comment;
import gamestudio.entity.Favorites;
import gamestudio.entity.Rating;
import gamestudio.entity.Score;
import gamestudio.service.CommentService;
import gamestudio.service.FavoritesService;
import gamestudio.service.RatingService;
import gamestudio.service.ScoreService;

public class ConsoleMenu {
	private ConsoleGameUI[] games;

	@Autowired
	private RatingService ratingService;

	@Autowired
	private ScoreService scoreService;

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private FavoritesService favoritesService;

	private Scanner scanner = new Scanner(System.in);

	public ConsoleMenu(ConsoleGameUI[] games) {
		this.games = games;
	}

	public void show() {
//		
//		scoreService.addScore(new Score("miro", "mines", 123));
//		System.out.println(scoreService.getTopScores("mines"));
//		
//				

//		commentService.addComment(
//				new Comment("miro", "mines", "good game", new java.sql.Timestamp(new java.util.Date().getTime())));
//		commentService.addComment(
//				new Comment("mira", "mines", "bad game", new java.sql.Timestamp(new java.util.Date().getTime())));
//		commentService.addComment(new Comment("michaela", "mines", "awesome game",
//				new java.sql.Timestamp(new java.util.Date().getTime())));
		//System.out.println(commentService.getComments("mines"));
		
		
		
//		ratingService.setRating(new Rating("miro", "mines", 5));
//		ratingService.setRating(new Rating("mira", "mines", 6));
//		ratingService.setRating(new Rating("michaela", "mines", 3));
//		System.out.println(ratingService.getAverageRating("mines"));
		
		favoritesService.addFavorite(new Favorites("alfred", "mines"));
		favoritesService.addFavorite(new Favorites("alfred", "puzzle"));
		favoritesService.addFavorite(new Favorites("jonatan", "mines"));
		System.out.println(favoritesService.getFavorites("username"));
		
		

		System.out.println("-------------------------------------");
		System.out.println("List of games: ");
		int index = 1;

		for (ConsoleGameUI game : games) {
			double rating = ratingService.getAverageRating(game.getName());
			System.out.printf("%d. %s (%.2f/10)\n", index, game.getName(), rating);
			index++;

		}
		System.out.println("-------------------------------------");

		do {
			System.out.println("select a game : ");
			String line = scanner.nextLine().trim();
			for (ConsoleGameUI game : games) {
				if (game.getName().equals(line)) {
					game.run();
				} else if ("x".toLowerCase().equals(line)) {
					System.exit(0);
				}

				try {
					index = Integer.parseInt(line);
				} catch (NumberFormatException e) {
					index = -1;
				}
			}
		} while (index < 1 || index > games.length);
		ConsoleGameUI game = games[index - 1];
		game.run();
		
	}

}
