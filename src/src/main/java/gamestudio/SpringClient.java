package gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import gamestudio.consoleui.ConsoleGameUI;
import gamestudio.consoleui.ConsoleMenu;
import gamestudio.game.minesweeper.consoleUI.ConsoleUI;
import gamestudio.service.CommentService;
import gamestudio.service.FavoritesService;
import gamestudio.service.RatingService;
import gamestudio.service.ScoreService;
import gamestudio.service.impl.CommentServiceJPA;
import gamestudio.service.impl.CommentServiceRestClient;
import gamestudio.service.impl.FavoritesServiceJPA;
import gamestudio.service.impl.RatingServiceJPA;
import gamestudio.service.impl.RatingServiceRestClient;
import gamestudio.service.impl.ScoreServiceRestClient;

@SpringBootApplication
@ComponentScan(basePackages = {
		"gamestudio" }, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "gamestudio.server.*"))

public class SpringClient {

	public static void main(String[] args) throws Exception {
		// SpringApplication.run(SpringClient.class, args);
		new SpringApplicationBuilder(SpringClient.class).web(false).run(args);

	}

	@Bean
	public CommandLineRunner runner(ConsoleMenu menu) {
		return args -> menu.show();
	}

	@Bean
	public ConsoleMenu menu(ConsoleGameUI[] games) {
		return new ConsoleMenu(games);

	}

	@Bean
	public ConsoleUI consoleUIMines() {
		return new ConsoleUI();
	}

	// public CommandLineRunner runner(gamestudio.puzzle.UI.ConsoleUI ui) {
	// return args -> ui.run();
	// }
	@Bean
	public ConsoleGameUI consoleUIPuzzle() {
		return new gamestudio.puzzle.UI.ConsoleUI();
	}

	@Bean
	public ConsoleGameUI guessNumber() {
		return new guessNumber.guessNumber();
	}

	@Bean
	public RatingService ratingService() {
		return new RatingServiceRestClient();
	}

	@Bean
	public CommentService commentService() {
		return new CommentServiceRestClient();
	}

	@Bean
	public ScoreService scoreService() {
		return new ScoreServiceRestClient();
	}

	@Bean
	public FavoritesService favoritesService() {
		return new FavoritesServiceJPA();
	}
}
