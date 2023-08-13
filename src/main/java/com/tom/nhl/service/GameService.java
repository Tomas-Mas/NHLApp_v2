package com.tom.nhl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.dao.GameDAO;
import com.tom.nhl.dao.GameEventDAO;
import com.tom.nhl.entity.Game;

@Component
public class GameService {

	@Autowired
	private GameDAO gameDAO;
	@Autowired
	private GameEventDAO gameEventDAO;
	
	private List<Integer> seasons;
	
	public List<Integer> getSeasons() {
		if(seasons == null)
			seasons = gameDAO.getSeasons();
		return seasons;
	}
	
	public int getDefaultSeason() {
		List<Integer> seasons = getSeasons();
		return seasons.get(seasons.size() - 1);
	}
	
	public boolean isActiveSeasonValid(int season) {
		List<Integer> seasons = getSeasons();
		for(int s : seasons) {
			if(s == season)
				return true;
		}
		return false;
	}
	
	public List<Game> getGamesBySeasonWithOrderedKeyEvents(int season) {
		List<Game> games = gameDAO.getGamesBySeasonWithOrderedKeyEvents(season);
		for(Game game : games) {
			game.setEvents(gameEventDAO.getOrderedCoreEvents(game));
		}
		return games;
	}
}
