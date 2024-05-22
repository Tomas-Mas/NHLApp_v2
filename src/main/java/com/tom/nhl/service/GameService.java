package com.tom.nhl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.dao.JPAGameDAO;

@Component
public class GameService {

	@Autowired
	private JPAGameDAO gameDAO;
	
	public List<Integer> getSeasons() {
		return gameDAO.getSeasons();
	}
	
	public int getDefaultSeason() {
		List<Integer> seasons = gameDAO.getSeasons();
		return seasons.get(0);
	}
	
	public boolean isSeasonValid(int season) {
		List<Integer> seasons = gameDAO.getSeasons();
		if(seasons.contains(season))
			return true;
		else
			return false;
	}
	
	public int getSeasonByGame(int gameId) {
		return gameDAO.getSeasonByGameId(gameId);
	}
}
