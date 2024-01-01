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
import com.tom.nhl.api.stats.RegulationTeamStandings;
import com.tom.nhl.api.stats.TeamStats;
import com.tom.nhl.dao.JPAGameDAO;
import com.tom.nhl.entity.EventPlayer;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.entity.view.GoalsPerPeriod;
import com.tom.nhl.entity.view.MainPageGameBasicData;
import com.tom.nhl.entity.view.RegulationTeamStats;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.util.LogUtil;

@Component
public class GameService {

	@Autowired
	private JPAGameDAO gameDAO;
	//@Autowired
	//private GameEventDAO gameEventDAO;
	
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
	
	public List<GameBaseData> getGamesBaseData(int season) {
		long start = System.currentTimeMillis();
		List<GameBaseData> games = mapGameData(gameDAO.fetchGamesBasicData(season));
		System.out.println("fancy fetching of games took: " + (System.currentTimeMillis() - start));
		
		return games;
	}
	
	public List<GamePeriodKeyEventsData> getGameKeyEventsData(int gameId) {
		long start = System.currentTimeMillis();
		List<GameEvent> events = gameDAO.fetchGamesKeyEvents(gameId);
		List<GamePeriodKeyEventsData> eventsPerPeriod = mapGameEvents(events);
		System.out.println("fancy fetching of events took: " + (System.currentTimeMillis() - start));
		
		return eventsPerPeriod;
	}
	
	public RegulationTeamStandings getTeamStandings(int season, RegulationScope statsType) {
		long start = System.currentTimeMillis();
		List<RegulationTeamStats> dbStats = gameDAO.fetchTeamStandings(season);
		RegulationTeamStandings teamStandings = mapTeamStandings(season, dbStats, statsType);
		System.out.println("fetching of team standings took: " + (System.currentTimeMillis() - start));
		
		return teamStandings;
	}
	
	private List<GameBaseData> mapGameData(List<MainPageGameBasicData> games) {
		List<GameBaseData> gameList = new ArrayList<GameBaseData>();
		int[] homePeriodScore;
		int[] awayPeriodScore;
		for(MainPageGameBasicData entityGame : games) {
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
					entityGame.getHomeTeam(),
					entityGame.getHomeAbr(),
					entityGame.getHomeScore(),
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
	
	private RegulationTeamStandings mapTeamStandings(int season, List<RegulationTeamStats> dbStats, RegulationScope statsType) {
		ArrayList<TeamStats> teamStats = new ArrayList<TeamStats>();
		for(RegulationTeamStats team : dbStats) {
			if(statsType == RegulationScope.OVERALL) {
				teamStats.add(new TeamStats(
						team.getTeamName(),
						team.getTeamAbbreviation(),
						team.getId().getTeamId(),
						team.getConference(),
						team.getDivision(),
						team.getId().getSeason(),
						team.getHomeGames() + team.getAwayGames(),
						team.getHomeGoalsFor() + team.getAwayGoalsFor(),
						team.getHomeGoalsAgainst() + team.getAwayGoalsAgainst(),
						team.getHomePoints() + team.getAwayPoints(),
						team.getHomeRegWins() + team.getAwayRegWins(),
						team.getHomeRegLoses() + team.getAwayRegLoses(),
						team.getHomeOtWins() + team.getAwayOtWins(),
						team.getHomeOtLoses() + team.getAwayOtLoses()
				));
			} else if(statsType == RegulationScope.HOME) {
				teamStats.add(new TeamStats(
						team.getTeamName(),
						team.getTeamAbbreviation(),
						team.getId().getTeamId(),
						team.getConference(),
						team.getDivision(),
						team.getId().getSeason(),
						team.getHomeGames(),
						team.getHomeGoalsFor(),
						team.getHomeGoalsAgainst(),
						team.getHomePoints(),
						team.getHomeRegWins(),
						team.getHomeRegLoses(),
						team.getHomeOtWins(),
						team.getHomeOtLoses()
				));
			} else if(statsType == RegulationScope.AWAY) {
				teamStats.add(new TeamStats(
						team.getTeamName(),
						team.getTeamAbbreviation(),
						team.getId().getTeamId(),
						team.getConference(),
						team.getDivision(),
						team.getId().getSeason(),
						team.getAwayGames(),
						team.getAwayGoalsFor(),
						team.getAwayGoalsAgainst(),
						team.getAwayPoints(),
						team.getAwayRegWins(),
						team.getAwayRegLoses(),
						team.getAwayOtWins(),
						team.getAwayOtLoses()
				));
			}
		}
		return new RegulationTeamStandings(season, teamStats);
	}
}
