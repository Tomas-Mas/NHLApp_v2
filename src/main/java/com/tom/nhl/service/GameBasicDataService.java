package com.tom.nhl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.dao.GameBasicDataDAO;
import com.tom.nhl.dao.GameDAO;
import com.tom.nhl.dto.GameBasicDataDTO;
import com.tom.nhl.dto.LastGamesNavigationDTO;
import com.tom.nhl.entity.view.GameBasicDataView;
import com.tom.nhl.enums.SeasonScope;
import com.tom.nhl.mapper.GameBasicDataMapper;

@Component
public class GameBasicDataService {

	@Autowired
	private GameBasicDataDAO gameBasicDataDAO;
	@Autowired
	private GameDAO gameDAO;
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
	
	public Map<String, List<GameBasicDataDTO>> getLastGamesByGameId(int gameId, LastGamesNavigationDTO navigation) {
		Map<String, List<GameBasicDataDTO>> lastGamesMap = new LinkedHashMap<String, List<GameBasicDataDTO>>();
		
		Map<String, String> teamsMap = gameDAO.getTeamsByGameId(gameId);
		
		List<GameBasicDataView> homeGames = gameBasicDataDAO.getLastGamesByAbr(gameId, teamsMap.get("homeAbr"), navigation.getHomeScopeEnum(), navigation.getHomeGamesCount());
		List<GameBasicDataView> awayGames = gameBasicDataDAO.getLastGamesByAbr(gameId, teamsMap.get("awayAbr"), navigation.getAwayScopeEnum(), navigation.getAwayGamesCount());
		
		lastGamesMap.put(teamsMap.get("homeAbr"), mapper.toGameBasicDataDTO(homeGames));
		lastGamesMap.put(teamsMap.get("awayAbr"), mapper.toGameBasicDataDTO(awayGames));	
		return lastGamesMap;
	}
}
