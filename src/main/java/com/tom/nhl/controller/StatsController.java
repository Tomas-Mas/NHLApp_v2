package com.tom.nhl.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tom.nhl.config.DefaultConstants;
import com.tom.nhl.dto.StatsNavigationDTO;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.enums.SeasonScope;
import com.tom.nhl.service.PlayoffSpiderService;
import com.tom.nhl.service.SkaterStatsService;
import com.tom.nhl.service.TeamStatsService;

@Controller
@RequestMapping("/c/stats")
public class StatsController {
	
	@Autowired
	PlayoffSpiderService playoffSpiderService;
	@Autowired
	TeamStatsService teamStatsService;
	@Autowired
	SkaterStatsService skaterStatsService;

	@RequestMapping(value = {"/sidebarBySeasonScope/{season}", "/sidebarBySeasonScope/{season}/{seasonScope}"}, method = RequestMethod.GET)
	public ModelAndView getSidebarStats(@PathVariable int season, @PathVariable(required = false) Optional<String> seasonScope) {
		ModelAndView model = new ModelAndView();
		
		SeasonScope scope = SeasonScope.valueOfType(seasonScope.orElse(DefaultConstants.SIDEBAR_STATS_DEFAULT_SEASON_SCOPE.getType()));
		model.addObject("seasonScope", scope);
		
		if(scope == SeasonScope.PLAYOFF) {
			model.addObject("playoff", playoffSpiderService.getBySeason(season));
			model.setViewName("components/sidebar-playoff-spiders");
		} else if(scope == SeasonScope.REGULATION) {
			model.addObject("standings", teamStatsService.getStandingsBySeason(season));
			model.addObject("regulationScope", DefaultConstants.TEAM_STANDINGS_DEFAULT_REGULATION_SCOPE);
			model.setViewName("components/sidebar-stats-standings");
		}
		return model;
	}
	
	@RequestMapping(value = "/regulation/{season}/{regulationScope}", method = RequestMethod.GET)
	public ModelAndView getRegulationStats(@PathVariable int season, @PathVariable String regulationScope) {
		ModelAndView model = new ModelAndView();
		RegulationScope scope = RegulationScope.valueOfType(regulationScope);
		model.addObject("seasonScope", SeasonScope.REGULATION);
		model.addObject("regulationScope", scope);
		model.addObject("standings", teamStatsService.getStandingsBySeasonAndType(season, scope));
		model.setViewName("components/sidebar-stats-standings");
		return model;
	}
	
	@RequestMapping(value = "/playoff/{season}", method = RequestMethod.GET)
	public ModelAndView getPlayoffStats(@PathVariable int season) {
		ModelAndView model = new ModelAndView();
		model.addObject("seasonScope", SeasonScope.PLAYOFF);
		model.addObject("playoff", playoffSpiderService.getBySeason(season));
		model.setViewName("components/sidebar-playoff-spiders");
		return model;
	}
	
	@RequestMapping(value = "/fullStats/{season}")
	public ModelAndView getFullStatistics(@PathVariable int season, @ModelAttribute("statsNavigation") StatsNavigationDTO statsNavigation) {
		ModelAndView model = new ModelAndView();
		
		switch (statsNavigation.getDataScopeEnum()) {
		case TEAMS:
			model.addObject("teamStandings", teamStatsService.getStandingsBySeasonAndType(season, statsNavigation.getRegulationScopeEnum()));
			break;
		case PLAYERS:
			statsNavigation.setResCount(skaterStatsService.getCountBySeason(season, statsNavigation.getSeasonScopeEnum(), statsNavigation.getRegulationScopeEnum()));
			model.addObject("skatersStats",skaterStatsService.getBySeasonAndNavigationDTO(season, statsNavigation));
			break;
		}
		
		model.setViewName("components/statistics");
		return model;
	}
}
