package com.tom.nhl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.api.game.GameBaseData;
import com.tom.nhl.api.game.GameKeyEventsData;
import com.tom.nhl.dao.GameDAO;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.entity.view.MainPageGameBasicData;

@Component
public class GameService {

	@Autowired
	private GameDAO gameDAO;
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
		
		/*
		for(GameKeyEventsData res : eventsPerPeriod) {
			System.out.println(res.getPeriodNumber() + " ; " + res.getPeriodScore());
			for(KeyEvent event : res.getEvents()) {
				System.out.println(event.getActedBy().formatted() + " - " + event.getPeriodTime() + " - " + event.getName());
			}
		}*/
		
		return eventsPerPeriod;
	}
	
	/*
	private void setTransientVariables(List<Game> games) {
		long start = System.currentTimeMillis();
		for(Game game : games) {
			//finding event's actor (actor = home or away team)
			for(GameEvent event : game.getEvents()) {
				for(EventPlayer player : event.getPlayers()) {
					String role = player.getRole();
					if(role.equals("Scorer") || role.equals("PenaltyOn")) {	//Scorer and Assists for goal events, PenaltyOn for Penalty events
						event.setMainActor(player);
						if(game.getHomeTeam().getId() == player.getId().getRoster().getTeam().getId())
							event.setActedBy(TeamType.HOME);
						else if(game.getAwayTeam().getId() == player.getId().getRoster().getTeam().getId())
							event.setActedBy(TeamType.AWAY);
						else 
							LogUtil.writeLog("event actor wasn't found");
						
						break;
					}
				}
			}
			
			int lastPeriodNum = game.getEvents().get(game.getEvents().size() -1).getPeriodNumber();
			List<List<GameEvent>> eventsPerPeriod = new ArrayList<List<GameEvent>>(lastPeriodNum);
			List<Integer> homeScore = new ArrayList<Integer>(lastPeriodNum);
			List<Integer> awayScore = new ArrayList<Integer>(lastPeriodNum);
			for(int i = 0; i < lastPeriodNum; i++) {
				homeScore.add(i, 0);
				awayScore.add(i, 0);
				eventsPerPeriod.add(i, new ArrayList<GameEvent>());
			}
			
			for(GameEvent event : game.getEvents()) {
				int periodIndex = event.getPeriodNumber() - 1;
				List<GameEvent> events = eventsPerPeriod.get(periodIndex);
				events.add(event);
				eventsPerPeriod.set(periodIndex, events);
				
				if(event.getEvent().getName().equals("Goal")) {
					if(event.getActedBy() == TeamType.HOME) {
						homeScore.set(periodIndex, homeScore.get(periodIndex) + 1);
					} else {
						awayScore.set(periodIndex, awayScore.get(periodIndex) + 1);
					}
				}
			}

			game.setEventsPerPeriod(eventsPerPeriod);
			game.setHomePeriodScore(homeScore);
			game.setAwayPeriodScore(awayScore);
		}
		System.out.println("setting transient variables took: " + (System.currentTimeMillis() - start));
	}
	*/
}
