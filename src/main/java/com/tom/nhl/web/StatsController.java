package com.tom.nhl.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tom.nhl.dto.StatsNavigationDTO;
import com.tom.nhl.enums.DataScope;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.service.GameService;
import com.tom.nhl.service.SeasonService;
import com.tom.nhl.service.StatsService;

@Controller
@RequestMapping("/stats")
public class StatsController {

	@Autowired
	private SeasonService seasonService;
	@Autowired
	private StatsService statsService;
	@Autowired
	private GameService gameService;
	
	@RequestMapping(value = "/{season}")
	public ModelAndView loadPage(@PathVariable int season, @ModelAttribute("statsNavigation") StatsNavigationDTO statsNavigation) {
		ModelAndView model = new ModelAndView();
		model.addObject("seasons", seasonService.getSeasons());
		model.addObject("selectedSeason", season);
		
		if(statsNavigation.getDataScopeEnum() == DataScope.TEAMS) {
			model.addObject("teamStandings", statsService.getRegulationTeamStandings(season, statsNavigation.getRegulationScopeEnum()));
		} else if(statsNavigation.getDataScopeEnum() == DataScope.PLAYERS) {
			statsNavigation.setResCount(statsService.getSkaterStatsCount(season, statsNavigation.getSeasonScopeEnum(), statsNavigation.getRegulationScopeEnum()));
			model.addObject("skatersStats", statsService.getSkaterStats(season, statsNavigation));
		}
		
		model.addObject("playoff", statsService.getPlayoffSpider(
				statsService.getRegulationTeamStandings(season, RegulationScope.OVERALL),
				gameService.getPlayoffBaseData(season)));
		
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
