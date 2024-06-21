package com.tom.nhl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tom.nhl.service.GameService;

@Controller
@RequestMapping("/game")
public class GamePageController {
	
	@Autowired
	private GameService gameService;
	
	@RequestMapping("{gameId}")
	public ModelAndView loadGamePage(@PathVariable int gameId) {
		ModelAndView model = new ModelAndView();
		
		model.addObject("seasons", gameService.getSeasons());
		model.addObject("selectedSeason", gameService.getSeasonByGame(gameId));
		
		model.setViewName("pages/game-page");
		return model;
	}
}