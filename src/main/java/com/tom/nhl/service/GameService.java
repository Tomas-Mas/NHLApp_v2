package com.tom.nhl.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.api.game.GameBaseData;
import com.tom.nhl.api.game.GameEventPlayer;
import com.tom.nhl.api.game.GamePeriodKeyEventsData;
import com.tom.nhl.api.game.KeyEvent;
import com.tom.nhl.dao.JPAGameDAO;
import com.tom.nhl.entity.EventPlayer;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.entity.view.GoalsPerPeriod;
import com.tom.nhl.entity.view.GameBasicDataView;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.enums.SeasonScope;
import com.tom.nhl.util.LogUtil;

@Component
public class GameService {

	@Autowired
	private JPAGameDAO gameDAO;
	
	public List<GameBaseData> getGamesBaseDataBySeason(int season) {
		long start = System.currentTimeMillis();
		List<GameBaseData> games = mapGameData(gameDAO.fetchGamesGoalsPerPeriodData(gameDAO.fetchGamesBasicDataBySeason(season)));
		System.out.println("fancy fetching of games took: " + (System.currentTimeMillis() - start));
		
		return games;
	}
	
	public GameBaseData getGameBaseDataById(int gameId) {
		long start = System.currentTimeMillis();
		GameBaseData game = mapGameData(gameDAO.fetchGameBasicDataById(gameId));
		System.out.println("fetching of base game data took: " + (System.currentTimeMillis() - start));
		
		return game;
	}
	
	public Map<String, List<GameBaseData>> getHeadToHeadMapByTeams(int gameId, int team1Id, int team2Id) {
		long start = System.currentTimeMillis();
		Map<String, List<GameBaseData>> gameMap = new HashMap<String, List<GameBaseData>>();
		List<GameBaseData> regulationGames = new ArrayList<GameBaseData>();
		List<GameBaseData> playoffGames = new ArrayList<GameBaseData>();
		
		List<GameBaseData> games = mapGamesData(gameDAO.fetchGamesBasicDataByTeams(gameId, team1Id, team2Id));
		for(GameBaseData g : games) {
			if(g.getGameType() == SeasonScope.REGULATION) {
				regulationGames.add(g);
			} else if(g.getGameType() == SeasonScope.PLAYOFF) {
				playoffGames.add(g);
			}
		}
		gameMap.put("Regulation", regulationGames);
		gameMap.put("Playoff", playoffGames);
		System.out.println("fetching of games data for given teams took: " + (System.currentTimeMillis() - start));
		return gameMap;
	}
	
	public List<GameBaseData> getPlayoffBaseData(int season) {
		long start = System.currentTimeMillis();
		List<GameBaseData> games = mapGamesData(gameDAO.fetchPlayoffGamesBasicDataBySeason(season));
		System.out.println("fetching playoff game list took: " + (System.currentTimeMillis() - start));
		return games;
	}
	
	public List<GamePeriodKeyEventsData> getGameKeyEventsData(int gameId) {
		long start = System.currentTimeMillis();
		List<GameEvent> events = gameDAO.fetchGamesKeyEventsById(gameId);
		List<GamePeriodKeyEventsData> eventsPerPeriod = mapGameEvents(events);
		System.out.println("fancy fetching of events took: " + (System.currentTimeMillis() - start));
		
		return eventsPerPeriod;
	}
	
	private List<GameBaseData> mapGamesData(List<GameBasicDataView> games) {
		List<GameBaseData> gameList = new ArrayList<GameBaseData>();
		for(GameBasicDataView game : games) {
			gameList.add(new GameBaseData(
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
					new int[] {}));
		}
		return gameList;
	}
	
	private GameBaseData mapGameData(GameBasicDataView game) {
		return new GameBaseData(
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
	
	private List<GameBaseData> mapGameData(List<GameBasicDataView> games) {
		List<GameBaseData> gameList = new ArrayList<GameBaseData>();
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
					System.out.println("!!! oopsie has been logged");
				}
			}
			gameList.add(new GameBaseData(
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
	
	private List<GamePeriodKeyEventsData> mapGameEvents(List<GameEvent> events) {
		Map<Integer, List<GameEvent>> periodEventsMap = new HashMap<Integer, List<GameEvent>>();
		for(int i = 1; i <= events.get(events.size() - 1).getPeriodNumber(); i++) {
			periodEventsMap.put(i, new ArrayList<GameEvent>());
		}
		
		for(GameEvent event : events) {
			periodEventsMap.get(event.getPeriodNumber()).add(event);
		}
		
		List<GamePeriodKeyEventsData> eventsPerPeriod = new ArrayList<GamePeriodKeyEventsData>();
		for(Integer period : periodEventsMap.keySet()) {
			//eventsPerPeriod.add(new GamePeriodKeyEventsData(period, periodEventsMap.get(period)));
			//periodNum = period ; eventList = periodEventsMap.get(period)
			
			List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();
			int homeScore = 0;
			int awayScore = 0;
			RegulationScope actedBy = null;
			GameEventPlayer mainActor = null;
			List<GameEventPlayer> secondaryActors = null;
			
			for(GameEvent event : periodEventsMap.get(period)) {
				secondaryActors = new ArrayList<GameEventPlayer>();
				for(EventPlayer player : event.getPlayers()) {
					//setting scores
					if(player.getRole().equals("Scorer")) {
						if(player.getId().getRoster().getTeam().getId() == event.getGame().getHomeTeam().getId())
							homeScore++;
						else
							awayScore++;
					}
					//setting actors and acted by
					if(player.getRole().equals("Scorer") || player.getRole().equals("PenaltyOn")) {
						mainActor = new GameEventPlayer(
								player.getId().getRoster().getPlayer().getId(),
								player.getId().getRoster().getPlayer().getFirstName(),
								player.getId().getRoster().getPlayer().getLastName()
								);
						
						if(player.getId().getRoster().getTeam().getId() == event.getGame().getHomeTeam().getId())
							actedBy = RegulationScope.HOME;
						else
							actedBy = RegulationScope.AWAY;
					} else if(player.getRole().equals("Assist")) {
						secondaryActors.add(new GameEventPlayer(
								player.getId().getRoster().getPlayer().getId(),
								player.getId().getRoster().getPlayer().getFirstName(),
								player.getId().getRoster().getPlayer().getLastName()
								));
					}
				}
				keyEvents.add(new KeyEvent(
						actedBy,
						event.getPeriodTime(),
						event.getEvent().getName(),
						event.getEvent().getSecondaryType(),
						mainActor,
						secondaryActors,
						event.getEvent().getStrength(),
						event.getEvent().getPenaltyMinutes(),
						event.getEvent().getPenaltySeverity()
						));
			}
			eventsPerPeriod.add(new GamePeriodKeyEventsData(
					period,
					homeScore + " - " + awayScore,
					keyEvents
					));
		}
		
		return eventsPerPeriod;
	}
}
