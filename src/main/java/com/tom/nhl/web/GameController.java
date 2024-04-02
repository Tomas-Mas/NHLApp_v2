package com.tom.nhl.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tom.nhl.api.game.GameBaseData;
import com.tom.nhl.service.GameService;
import com.tom.nhl.service.SeasonService;

@Controller
@RequestMapping("/game")
public class GameController {
	
	@Autowired
	private GameService gameService;
	@Autowired
	private SeasonService seasonService;
	
	@RequestMapping("{gameId}")
	public String loadGamePage(@PathVariable int gameId) {

		return "redirect:/game/" + gameId + "/overview";
	}

	@RequestMapping("{gameId}/overview")
	public ModelAndView loadOverview(@PathVariable int gameId) {
		ModelAndView model = new ModelAndView();
		
		GameBaseData gameData = gameService.getGameBaseDataById(gameId);
		
		model.addObject("pageNavigation", "Overview");
		model.addObject("gameId", gameId);
		model.addObject("seasons", seasonService.getSeasons());
		model.addObject("selectedSeason", seasonService.getSeasonByGameId(gameId));
		model.addObject("gameData", gameData);
		model.addObject("gameEvents", gameService.getGameKeyEventsData(gameId));
		model.addObject("headToHead", gameService.getHeadToHeadMapByTeams(gameId, gameData.getHomeTeamId(), gameData.getAwayTeamId()));
		
		model.setViewName("game_page/overview");
		return model;
	}
}