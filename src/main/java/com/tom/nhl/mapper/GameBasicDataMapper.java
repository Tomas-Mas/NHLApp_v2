package com.tom.nhl.mapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tom.nhl.dto.GameBasicDataDTO;
import com.tom.nhl.entity.view.GameBasicDataView;
import com.tom.nhl.entity.view.GoalsPerPeriod;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.enums.SeasonScope;
import com.tom.nhl.util.LogUtil;

@Component
public class GameBasicDataMapper {

	public GameBasicDataDTO toGameBasicDataDTO(GameBasicDataView game) {
		return entityToDTO(game);
	}
	
	public List<GameBasicDataDTO> toGameBasicDataDTO(List<GameBasicDataView> games) {
		List<GameBasicDataDTO> gamesDTO = new ArrayList<GameBasicDataDTO>();
		for(GameBasicDataView g : games) {
			gamesDTO.add(entityToDTO(g));
		}
		return gamesDTO;
	}
	
	public List<GameBasicDataDTO> toGameBasicDataWithPeriodGoalsDTO(List<GameBasicDataView> games) {
		List<GameBasicDataDTO> gameList = new ArrayList<GameBasicDataDTO>();
		int[] homePeriodScore;
		int[] awayPeriodScore;
		for(GameBasicDataView entityGame : games) {
			homePeriodScore = new int[entityGame.getEndPeriod()];
			awayPeriodScore = new int[entityGame.getEndPeriod()];
			for(GoalsPerPeriod goals : entityGame.getGoalsPerPeriod()) {
				if(goals.getTeam().equals(RegulationScope.HOME.formatted())) 
					homePeriodScore[goals.getPeriod() - 1] = goals.getGoals();
				else if(goals.getTeam().equals(RegulationScope.AWAY.formatted()))
					awayPeriodScore[goals.getPeriod() - 1] = goals.getGoals();
				else {
					LogUtil.writeLog("game id: " + entityGame.getId() + 
							"mapping of main page games basic info has an oopsie; sql didn't find correct team type for goals per periods");
				}
			}
			gameList.add(new GameBasicDataDTO(
					entityGame.getId(),
					new SimpleDateFormat("dd.MM.yyyy HH:mm").format(entityGame.getGameDate()),
					SeasonScope.valueOfValue(entityGame.getGameType()),
					entityGame.getGameStatus(),
					entityGame.getHomeId(),
					entityGame.getHomeTeam(),
					entityGame.getHomeAbr(),
					entityGame.getHomeScore(),
					entityGame.getAwayId(),
					entityGame.getAwayTeam(),
					entityGame.getAwayAbr(),
					entityGame.getAwayScore(),
					entityGame.getEndPeriodType(),
					homePeriodScore,
					awayPeriodScore
					));
		}
		return gameList;
	}
	
	private GameBasicDataDTO entityToDTO(GameBasicDataView game) {
		return new GameBasicDataDTO(
				game.getId(),
				new SimpleDateFormat("dd.MM.yyyy HH:mm").format(game.getGameDate()),
				SeasonScope.valueOfValue(game.getGameType()),
				game.getGameStatus(),
				game.getHomeId(),
				game.getHomeTeam(),
				game.getHomeAbr(),
				game.getHomeScore(),
				game.getAwayId(),
				game.getAwayTeam(),
				game.getAwayAbr(),
				game.getAwayScore(),
				game.getEndPeriodType(),
				new int[] {},
				new int[] {}
				);
	}
}
