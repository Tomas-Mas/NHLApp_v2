package com.tom.nhl.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tom.nhl.dto.LastGamesNavigationDTO;
import com.tom.nhl.dto.PlayerStatsFilterDTO;
import com.tom.nhl.service.GameBasicDataService;
import com.tom.nhl.service.GameEventService;
import com.tom.nhl.service.GameStatsService;
import com.tom.nhl.service.SkaterStatsService;

@Controller
@RequestMapping("/c/game")
public class GameController {

	@Autowired
	GameBasicDataService gameBasicDataService;
	@Autowired
	GameEventService gameEventService;
	@Autowired
	GameStatsService gameStatsService;
	@Autowired
	SkaterStatsService skaterStatsService;
	
	@RequestMapping(value = "/list/{season}", method = RequestMethod.GET)
	public ModelAndView getListBySeason(@PathVariable int season) {
		ModelAndView model = new ModelAndView();
		model.addObject("games", gameBasicDataService.getBySeasonWithPeriodGoals(season));
		model.setViewName("components/main-page-games-tbl");
		return model;
	}
	
	@RequestMapping(value = "/keyEvents/{gameId}", method = RequestMethod.GET)
	public ModelAndView getKeyEventsById(@PathVariable int gameId) {
		ModelAndView model = new ModelAndView();
		model.addObject("gameEvents", gameEventService.getKeyEventsByGame(gameId));
		model.setViewName("components/game-keyevents-td");
		return model;
	}
	
	@RequestMapping(value = "/h2h/{gameId}", method = RequestMethod.GET)
	public ModelAndView getHeadToHead(@PathVariable int gameId) {
		ModelAndView model = new ModelAndView();
		model.addObject("headToHead", gameBasicDataService.getHeadToHeadMapByGame(gameId));
		model.setViewName("components/game-h2h");
		return model;
	}
	
	@RequestMapping(value = "lastGames/{gameId}", method = RequestMethod.GET)
	public ModelAndView getLastGames(@PathVariable int gameId, @ModelAttribute("lastGamesNavigation") LastGamesNavigationDTO navigation) {
		ModelAndView model = new ModelAndView();
		model.addObject("lastGamesMap", gameBasicDataService.getLastGamesByGameId(gameId, navigation));
		model.setViewName("components/game-last-games");
		return model;
	}
	
	@RequestMapping(value = "/overview/{gameId}", method = RequestMethod.GET)
	public ModelAndView getOverview(@PathVariable int gameId) {
		ModelAndView model = new ModelAndView();
		model.addObject("pageNavigation", "Overview");
		model.addObject("gameData", gameBasicDataService.getById(gameId));
		model.addObject("gameEvents", gameEventService.getKeyEventsByGame(gameId));
		model.setViewName("components/game-overview");
		return model;
	}
	
	@RequestMapping(value = {"/stats/{gameId}", "/stats/{gameId}/{periodNum}"}, method = RequestMethod.GET)
	public ModelAndView getGameStats(@PathVariable int gameId, @PathVariable(name = "periodNum", required = false) Optional<Integer> periodNum) {
		ModelAndView model = new ModelAndView();
		model.addObject("pageNavigation", "GameStats");
		
		List<String> periods = gameEventService.getPeriodTypesByGame(gameId);
		//prevent showing empty data by going from stats of game with more periods, than previous one due to overtimes/shootouts.
		if (periodNum.orElse(0) > periods.size()) {
			periodNum = Optional.of(periods.size());
			model.setViewName("redirect:/c/game/stats/" + gameId + "/" + periodNum.orElse(0));
			return model;
		}
		
		model.addObject("gameData", gameBasicDataService.getById(gameId));
		model.addObject("periods", periods);
		model.addObject("gameStatsMap", gameStatsService.getByIdAndPeriod(gameId, periodNum.orElse(0)));
		
		model.setViewName("components/game-stats");
		return model;
	}
	
	@RequestMapping(value = "/players/{gameId}", method = RequestMethod.GET)
	public ModelAndView getPlayerStats(@PathVariable int gameId, @ModelAttribute("playerStatsFilter") PlayerStatsFilterDTO statsFilter) {
		ModelAndView model = new ModelAndView();
		
		model.addObject("pageNavigation", "PlayersStats");
		model.addObject("gameData", gameBasicDataService.getById(gameId));
		model.addObject("skaterStats", skaterStatsService.getByGameId(gameId, statsFilter));
		
		model.setViewName("components/game-players-stats");
		return model;
	}
}
