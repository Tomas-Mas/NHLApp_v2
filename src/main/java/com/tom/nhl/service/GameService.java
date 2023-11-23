package com.tom.nhl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.api.game.GameBaseData;
import com.tom.nhl.api.game.GameKeyEventsData;
import com.tom.nhl.api.stats.RegulationTeamStandings;
import com.tom.nhl.api.stats.TeamStats;
import com.tom.nhl.dao.JPAGameDAO;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.entity.view.MainPageGameBasicData;
import com.tom.nhl.entity.view.RegulationTeamStats;
import com.tom.nhl.enums.StatsType;

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
		List<GameBaseData> games = new ArrayList<GameBaseData>();
		for(MainPageGameBasicData game : gameDAO.fetchGamesBasicData(season)) {
			games.add(new GameBaseData(game));
		}
		System.out.println("fancy fetching of games took: " + (System.currentTimeMillis() - start));
		
		return games;
	}
	
	public List<GameKeyEventsData> getGameKeyEventsData(int gameId) {
		long start = System.currentTimeMillis();
		List<GameEvent> events = gameDAO.fetchGamesKeyEvents(gameId);
		List<GameKeyEventsData> eventsPerPeriod = mapGameEvents(events);
		System.out.println("fancy fetching of events took: " + (System.currentTimeMillis() - start));
		
		return eventsPerPeriod;
	}
	
	public RegulationTeamStandings getTeamStandings(int season, StatsType statsType) {
		long start = System.currentTimeMillis();
		List<RegulationTeamStats> dbStats = gameDAO.fetchTeamStandings(season);
		RegulationTeamStandings teamStandings = mapTeamStandings(season, dbStats, statsType);
		System.out.println("fetching of team standings took: " + (System.currentTimeMillis() - start));
		
		return teamStandings;
	}
	
	private List<GameKeyEventsData> mapGameEvents(List<GameEvent> events) {
		Map<Integer, List<GameEvent>> periodEventsMap = new HashMap<Integer, List<GameEvent>>();
		for(int i = 1; i <= events.get(events.size() - 1).getPeriodNumber(); i++) {
			periodEventsMap.put(i, new ArrayList<GameEvent>());
		}
		
		for(GameEvent event : events) {
			periodEventsMap.get(event.getPeriodNumber()).add(event);
		}
		
		List<GameKeyEventsData> eventsPerPeriod = new ArrayList<GameKeyEventsData>();
		for(Integer period : periodEventsMap.keySet()) {
			eventsPerPeriod.add(new GameKeyEventsData(period, periodEventsMap.get(period)));
		}
		
		return eventsPerPeriod;
	}
	
	private RegulationTeamStandings mapTeamStandings(int season, List<RegulationTeamStats> dbStats, StatsType statsType) {
		ArrayList<TeamStats> teamStats = new ArrayList<TeamStats>();
		for(RegulationTeamStats team : dbStats) {
			if(statsType == StatsType.OVERALL) {
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
			} else if(statsType == StatsType.HOME) {
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
			} else if(statsType == StatsType.AWAY) {
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
