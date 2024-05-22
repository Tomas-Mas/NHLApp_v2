package com.tom.nhl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.dao.GameStatsDAO;
import com.tom.nhl.dto.GameStatsDTO;

@Component
public class GameStatsService {
	
	@Autowired
	private GameStatsDAO gameStatsDAO;

	/**
	 * if periodNum is 0 return game stats for entire game
	 * returns map where key is team name and value is GameStats object
	 */
	public Map<String, GameStatsDTO> getByIdAndPeriod(int gameId, int periodNum) {
		List<GameStatsDTO> stats = gameStatsDAO.getByIdAndPeriod(gameId, periodNum);
		Map<String, GameStatsDTO> statsMap = new HashMap<String, GameStatsDTO>();
		for(GameStatsDTO s : stats) {
			statsMap.put(s.getTeam(), s);
		}
		return statsMap;
	}
}
