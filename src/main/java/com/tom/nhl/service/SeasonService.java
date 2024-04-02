package com.tom.nhl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.dao.JPAGameDAO;

@Component
public class SeasonService {

	@Autowired
	private JPAGameDAO gameDAO;
	
	public int getDefaultSeason() {
		List<Integer> seasons = gameDAO.fetchSeasons();
		return seasons.get(0);
	}
	
	public List<Integer> getSeasons() {
		List<Integer> seasons = gameDAO.fetchSeasons();
		return seasons;
	}
	
	public boolean isValidSeason(int season) {
		List<Integer> seasons = gameDAO.fetchSeasons();
		if(seasons.contains(season))
			return true;
		else
			return false;
	}
	
	public int getSeasonByGameId(int gameId) {
		return gameDAO.fetchSeasonByGameId(gameId);
	}
}
