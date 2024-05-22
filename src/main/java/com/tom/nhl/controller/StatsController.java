package com.tom.nhl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tom.nhl.dto.StatsNavigationDTO;
import com.tom.nhl.enums.DataScope;
import com.tom.nhl.service.GameService;
import com.tom.nhl.service.PlayoffSpiderService;
import com.tom.nhl.service.SkaterStatsService;
import com.tom.nhl.service.TeamStatsService;

@Controller
@RequestMapping("/stats")
public class StatsController {

	@Autowired
	private TeamStatsService teamStatsService;
	@Autowired
	private SkaterStatsService skaterStatsService;
	@Autowired
	private GameService gameService;
	@Autowired
	private PlayoffSpiderService playoffSpiderService;
	
	@RequestMapping(value = "/{season}")
	public ModelAndView loadPage(@PathVariable int season, @ModelAttribute("statsNavigation") StatsNavigationDTO statsNavigation) {
		ModelAndView model = new ModelAndView();
		model.addObject("seasons", gameService.getSeasons());
		model.addObject("selectedSeason", season);
		
		if(statsNavigation.getDataScopeEnum() == DataScope.TEAMS) {
			model.addObject("teamStandings", teamStatsService.getStandingsBySeasonAndType(season, statsNavigation.getRegulationScopeEnum()));
		} else if(statsNavigation.getDataScopeEnum() == DataScope.PLAYERS) {
			statsNavigation.setResCount(skaterStatsService.getCountBySeason(season, statsNavigation.getSeasonScopeEnum(), statsNavigation.getRegulationScopeEnum()));
			model.addObject("skatersStats",skaterStatsService.getBySeasonAndNavigationDTO(season, statsNavigation));
		}
		
		model.addObject("playoff", playoffSpiderService.getBySeason(season));
		
		model.setViewName("stats_page/stats-page");
		return model;
	}
	
	/*
	@RequestMapping(value = "/{season}/navigate", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String loadPage2(@PathVariable int season, @RequestBody StatsNavigationDTO statsNavigation) {
		System.out.println("navigate!!!!");
		System.out.println(statsNavigation.getDataScope());
		
		return "forward:/stats/" + season;
	}*/
}
