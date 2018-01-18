package gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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

		if (!validateLogin(player, model)) {
			return "login";
		}

		if (!validateEmail(player, model)) {
			return "login";
		}

		if (!validatePassword(player, model)) {
			return "login";
		}

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

	private boolean validateLogin(Player player, Model model) {
		if (StringUtils.isEmpty(player.getLogin())) {
			model.addAttribute("error", "Missing login");
			return false;
		}

		Player existingPlayer = playerService.findByLogin(player.getLogin());
		if (existingPlayer != null) {
			model.addAttribute("error", "Already registered login");
			return false;
		}

		return true;
	}

	private boolean validatePassword(Player player, Model model) {
		if (StringUtils.isEmpty(player.getPassword()) || StringUtils.isEmpty(player.getRepeatPassword())) {
			model.addAttribute("error", "InvalidPassword");
			return false;
		}

		if (!player.getPassword().equals(player.getRepeatPassword())) {
			model.addAttribute("error", "InvalidPassword - repeat password doesn`t match the password");
			return false;
		}
		return true;

	}

	private boolean validateEmail(Player player, Model model) {
		if (StringUtils.isEmpty(player.getEmail())) {
			model.addAttribute("error", "Missing email");
			return false;
		}

		if (!player.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
			model.addAttribute("error", "Invalid email");
			return false;
		}

		return true;
	}

}
