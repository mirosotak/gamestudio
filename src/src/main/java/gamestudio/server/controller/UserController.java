package gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import gamestudio.entity.Player;
import gamestudio.service.PlayerService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {

	private Player loggedPlayer;

	@Autowired
	private PlayerService playerService;

	@RequestMapping("/")
	public String index(Model model) {
		return "index";
	}

	@RequestMapping("/user")
	public String user(Model model) {
		return "login";
	}

	@RequestMapping("/login")
	public String login(Player player, Model model) {
		loggedPlayer = playerService.login(player.getLogin(), player.getPassword());
		return isLogged() ? "forward:/homePage" : "login";
	}

	@RequestMapping("/register")
	public String register(Player player, Model model) {
		playerService.register(player);
		loggedPlayer = playerService.login(player.getLogin(), player.getPassword());
		return "login";
	}

	@RequestMapping("/logout")
	public String logout(Model model) {
		loggedPlayer = null;
		return "login";
	}

	public Player getLoggedPlayer() {
		return loggedPlayer;
	}

	public boolean isLogged() {
		return loggedPlayer != null;
	}

}
