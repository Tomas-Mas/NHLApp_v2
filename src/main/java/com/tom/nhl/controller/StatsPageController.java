package com.tom.nhl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tom.nhl.dto.StatsNavigationDTO;
import com.tom.nhl.service.GameService;

@Controller
@RequestMapping("/stats")
public class StatsPageController {

	@Autowired
	private GameService gameService;
	
	@RequestMapping(value = "/{season}")
	public ModelAndView loadPage(@PathVariable int season, @ModelAttribute("statsNavigation") StatsNavigationDTO statsNavigation) {
		ModelAndView model = new ModelAndView();
		model.addObject("seasons", gameService.getSeasons());
		model.addObject("selectedSeason", season);
		model.setViewName("pages/stats-page");
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
