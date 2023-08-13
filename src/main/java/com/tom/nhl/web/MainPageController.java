package com.tom.nhl.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tom.nhl.service.GameService;

@Controller
@RequestMapping("/mainpage")
public class MainPageController {
	
	//@Autowired
	//private TestSeasonService seasonService;
	@Autowired
	private GameService gameService;

	@RequestMapping("")
	public String processSeasonlessUrl(@CookieValue(value = "season", defaultValue = "0") Integer season, HttpServletResponse response) {
		if(season == 0) {
			season = gameService.getDefaultSeason();
			addCookie("season", String.valueOf(season), response);
		}
		return "redirect:/mainpage/" + season;
	}
	
	@RequestMapping("/{season}")
	public String loadPage(@PathVariable int season, @CookieValue(value = "season", defaultValue = "0") Integer seasonCookie, Model model, HttpServletResponse response) {
		if(season != seasonCookie) {
			addCookie("season", String.valueOf(season), response);
		}
		model.addAttribute("seasons", gameService.getSeasons());
		model.addAttribute("selectedSeason", season);
		
		model.addAttribute("games", gameService.getGamesBySeasonWithOrderedKeyEvents(season));
		
		return "/mainpage.jsp";
	}
	
	private void addCookie(String name, String value, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(60 * 60 * 24 * 30);
		cookie.setPath("/NHL");
		response.addCookie(cookie);
	}
}
