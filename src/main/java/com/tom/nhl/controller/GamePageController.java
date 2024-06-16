package com.tom.nhl.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.service.GameBasicDataService;
import com.tom.nhl.service.GameEventService;
import com.tom.nhl.service.GameService;
import com.tom.nhl.service.GameStatsService;

@Controller
@RequestMapping("/game")
public class GamePageController {
	
	@Autowired
	private GameService gameService;
	@Autowired
	private GameBasicDataService gameBasicDataService;
	@Autowired
	private GameEventService gameEventService;
	@Autowired
	private GameStatsService gameStatsService;
	
	@RequestMapping("{gameId}")
	public ModelAndView loadGamePage(@PathVariable int gameId) {
		ModelAndView model = new ModelAndView();
		
		model.addObject("seasons", gameService.getSeasons());
		model.addObject("selectedSeason", gameService.getSeasonByGame(gameId));
		
		model.setViewName("pages/game-page");
		return model;
	}

	/*@RequestMapping("{gameId}/overview")
	public ModelAndView loadOverview(@PathVariable int gameId) {
		ModelAndView model = new ModelAndView();
		
		model.addObject("pageNavigation", "Overview");
		model.addObject("seasons", gameService.getSeasons());
		model.addObject("selectedSeason", gameService.getSeasonByGame(gameId));
		model.addObject("gameData", gameBasicDataService.getById(gameId));
		model.addObject("gameEvents", gameEventService.getKeyEventsByGame(gameId));
		model.addObject("headToHead", gameBasicDataService.getHeadToHeadMapByGame(gameId));
		
		model.setViewName("game_page/overview");
		return model;
	}
	
	@RequestMapping({"{gameId}/gameStats", "{gameId}/gameStats/{periodNum}"})
	public ModelAndView loadGameStats(@PathVariable(name = "gameId") int gameId, 
			@PathVariable(name = "periodNum", required = false) Optional<Integer> periodNum) {
		ModelAndView model = new ModelAndView();
		
		List<String> periods = gameEventService.getPeriodTypesByGame(gameId);
		
		//prevent showing empty data by going from stats of game with more periods, than previous one due to overtimes/shootouts.
		if(periodNum.orElse(0) > periods.size()) {
			periodNum = Optional.of(periods.size());
			model.setViewName("redirect:/game/" + gameId + "/gameStats/" + periodNum.orElse(0));
			return model;
		}
		
		model.addObject("pageNavigation", "GameStats");
		model.addObject("periodNavigation", periodNum);
		model.addObject("seasons", gameService.getSeasons());
		model.addObject("selectedSeason", gameService.getSeasonByGame(gameId));
		model.addObject("gameData", gameBasicDataService.getById(gameId));
		model.addObject("periods", periods);
		model.addObject("gameStatsMap", gameStatsService.getByIdAndPeriod(gameId, periodNum.orElse(0)));
		model.addObject("headToHead", gameBasicDataService.getHeadToHeadMapByGame(gameId));
		
		model.setViewName("game_page/game-stats");
		return model;
	}*/
}