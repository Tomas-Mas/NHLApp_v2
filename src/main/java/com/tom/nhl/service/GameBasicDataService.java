package com.tom.nhl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.dao.GameBasicDataDAO;
import com.tom.nhl.dto.GameBasicDataDTO;
import com.tom.nhl.entity.view.GameBasicDataView;
import com.tom.nhl.enums.SeasonScope;
import com.tom.nhl.mapper.GameBasicDataMapper;

@Component
public class GameBasicDataService {

	@Autowired
	private GameBasicDataDAO gameBasicDataDAO;
	@Autowired
	private GameBasicDataMapper mapper;
	
	public GameBasicDataDTO getById(int id) {
		GameBasicDataView game = gameBasicDataDAO.getById(id);
		return mapper.toGameBasicDataDTO(game);
	}
	
	public List<GameBasicDataDTO> getBySeasonWithPeriodGoals(int season) {
		List<GameBasicDataView> games = gameBasicDataDAO.getBySeasonWithPeriodGoals(season);
		return mapper.toGameBasicDataWithPeriodGoalsDTO(games);
	}
	
	public Map<String, List<GameBasicDataDTO>> getHeadToHeadMapByGame(int gameId) {
		Map<String, List<GameBasicDataDTO>> gameMap = new HashMap<String, List<GameBasicDataDTO>>();
		List<GameBasicDataDTO> regulationGames = new ArrayList<GameBasicDataDTO>();
		List<GameBasicDataDTO> playoffGames = new ArrayList<GameBasicDataDTO>();
		
		List<GameBasicDataView> gameEntities = gameBasicDataDAO.getH2hByGame(gameId);
		List<GameBasicDataDTO> games = mapper.toGameBasicDataDTO(gameEntities);
		
		for(GameBasicDataDTO g : games) {
			if(g.getGameType() == SeasonScope.REGULATION) {
				regulationGames.add(g);
			} else if(g.getGameType() == SeasonScope.PLAYOFF) {
				playoffGames.add(g);
			}
		}
		gameMap.put("Regulation", regulationGames);
		gameMap.put("Playoff", playoffGames);
		return gameMap;
	}
	
	public List<GameBasicDataDTO> getPlayoffGamesBySeason(int season) {
		List<GameBasicDataView> games = gameBasicDataDAO.getPlayoffBySeason(season);
		return mapper.toGameBasicDataDTO(games);
	}
}
