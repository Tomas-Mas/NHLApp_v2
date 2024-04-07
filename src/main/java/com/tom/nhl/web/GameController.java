package com.tom.nhl.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tom.nhl.api.game.GameBaseData;
import com.tom.nhl.service.GameService;
import com.tom.nhl.service.SeasonService;
import com.tom.nhl.service.StatsService;

@Controller
@RequestMapping("/game")
public class GameController {
	
	@Autowired
	private GameService gameService;
	@Autowired
	private StatsService statsService;
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
		model.addObject("seasons", seasonService.getSeasons());
		model.addObject("selectedSeason", seasonService.getSeasonByGameId(gameId));
		model.addObject("gameData", gameData);
		model.addObject("gameEvents", gameService.getGameKeyEventsData(gameId));
		model.addObject("headToHead", gameService.getHeadToHeadMapByTeams(gameId, gameData.getHomeTeamId(), gameData.getAwayTeamId()));
		
		model.setViewName("game_page/overview");
		return model;
	}
	
	@RequestMapping({"{gameId}/gameStats", "{gameId}/gameStats/{periodNum}"})
	public ModelAndView loadGameStats(@PathVariable(name = "gameId") int gameId, 
			@PathVariable(name = "periodNum", required = false) Optional<Integer> periodNum) {
		ModelAndView model = new ModelAndView();
		
		List<String> periods = gameService.getPeriodTypesByGame(gameId);
		
		//prevent showing empty data by going from stats of game with more periods, than previous one due to overtimes/shootouts.
		if(periodNum.orElse(0) > periods.size()) {
			periodNum = Optional.of(periods.size());
			model.setViewName("redirect:/game/" + gameId + "/gameStats/" + periodNum.orElse(0));
			return model;
		}
		
		GameBaseData gameData = gameService.getGameBaseDataById(gameId);
		
		model.addObject("pageNavigation", "GameStats");
		model.addObject("periodNavigation", periodNum);
		model.addObject("seasons", seasonService.getSeasons());
		model.addObject("selectedSeason", seasonService.getSeasonByGameId(gameId));
		model.addObject("gameData", gameData);
		model.addObject("periods", periods);
		model.addObject("gameStatsMap", statsService.getGameStatsById(gameId, periodNum.orElse(0)));
		model.addObject("headToHead", gameService.getHeadToHeadMapByTeams(gameId, gameData.getHomeTeamId(), gameData.getAwayTeamId()));
		
		model.setViewName("game_page/game-stats");
		return model;
	}
}