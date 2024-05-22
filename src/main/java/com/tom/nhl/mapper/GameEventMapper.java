package com.tom.nhl.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.tom.nhl.dto.GameEventPlayer;
import com.tom.nhl.dto.GamePeriodKeyEventsDTO;
import com.tom.nhl.dto.KeyEvent;
import com.tom.nhl.entity.EventPlayer;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.enums.RegulationScope;

@Component
public class GameEventMapper {

	public List<GamePeriodKeyEventsDTO> toGamePeriodKeyEvents(List<GameEvent> events) {
		List<GamePeriodKeyEventsDTO> eventsPerPeriod = new ArrayList<GamePeriodKeyEventsDTO>();
		Map<Integer, List<GameEvent>> periodEventsMap = new HashMap<Integer, List<GameEvent>>();
		
		//initialize periods in map
		for(int i = 1; i <= events.get(events.size() - 1).getPeriodNumber(); i++) {
			periodEventsMap.put(i, new ArrayList<GameEvent>());
		}
		
		//add events to map
		for(GameEvent event : events) {
			periodEventsMap.get(event.getPeriodNumber()).add(event);
		}
		
		//map events for each period
		for(Integer period : periodEventsMap.keySet()) {
			List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();
			int homeScore = 0;
			int awayScore = 0;
			RegulationScope actedBy = null;
			GameEventPlayer mainActor = null;
			List<GameEventPlayer> secondaryActors = null;
			KeyEvent mappedEvent = null;
			GamePeriodKeyEventsDTO mappedPeriodEvents = null;
			
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
				mappedEvent = new KeyEvent();
				mappedEvent.setActedBy(actedBy);
				mappedEvent.setPeriodTime(event.getPeriodTime());
				mappedEvent.setName(event.getEvent().getName());
				mappedEvent.setSecondaryType(event.getEvent().getSecondaryType());
				mappedEvent.setMainActor(mainActor);
				mappedEvent.setSecondaryActors(secondaryActors);
				mappedEvent.setStrength(event.getEvent().getStrength());
				mappedEvent.setPenaltyMinutes(event.getEvent().getPenaltyMinutes());
				mappedEvent.setPenaltySeverity(event.getEvent().getPenaltySeverity());
				
				keyEvents.add(mappedEvent);
			}
			mappedPeriodEvents = new GamePeriodKeyEventsDTO();
			mappedPeriodEvents.setPeriodNumber(period);
			mappedPeriodEvents.setPeriodScore(homeScore + " - " + awayScore);
			mappedPeriodEvents.setEvents(keyEvents);
			
			eventsPerPeriod.add(mappedPeriodEvents);
		}
		return eventsPerPeriod;
	}
}
