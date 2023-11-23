package com.tom.nhl.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tom.nhl.enums.SeasonScope;
import com.tom.nhl.enums.StatsType;
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
		
		model.addAttribute("games", gameService.getGamesBaseData(season));
		model.addAttribute("standings", gameService.getTeamStandings(season, StatsType.OVERALL));
		model.addAttribute("standingsScope", StatsType.OVERALL);
		model.addAttribute("seasonScope", SeasonScope.REGULATION);
		
		return "main_page/mainpage";
	}
	
	@RequestMapping(value = "/changeSeasonScope/{season}/{seasonScope}", method = RequestMethod.GET)
	public String showStatsData(@PathVariable int season, @PathVariable String seasonScope, Model model) {
		SeasonScope scope = SeasonScope.valueOfType(seasonScope);
		System.out.println("season: " + season + " - " + scope.getType());
		if(scope == SeasonScope.PLAYOFF) {
			model.addAttribute("seasonScope", scope);
			return "main_page/playoff-table";
		} else {
			model.addAttribute("standings", gameService.getTeamStandings(season, StatsType.OVERALL));
			model.addAttribute("standingsScope", StatsType.OVERALL);
			model.addAttribute("seasonScope", SeasonScope.REGULATION);
			return "main_page/regulation-table";
		}
	}
	
	@RequestMapping(value = "/showGameDetail/{id}", method = RequestMethod.GET)
	public String showGameDetail(@PathVariable int id, Model model) {
		model.addAttribute("gameEvents", gameService.getGameKeyEventsData(id));
		return "main_page/game-detail";
	}
	
	private void addCookie(String name, String value, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(60 * 60 * 24 * 30);
		cookie.setPath("/NHL");
		response.addCookie(cookie);
	}
}
