package com.tom.nhl.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.tom.nhl.dto.GameEventPlayerDTO;
import com.tom.nhl.dto.GamePeriodKeyEventsDTO;
import com.tom.nhl.dto.PenaltyBoxDTO;
import com.tom.nhl.dto.ScoreboardDTO;
import com.tom.nhl.dto.GameEventDTO;
import com.tom.nhl.entity.EventPlayer;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.enums.PenaltyBoxType;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.util.LogUtil;

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
			List<GameEventDTO> keyEvents = new ArrayList<GameEventDTO>();
			int homeScore = 0;
			int awayScore = 0;
			RegulationScope actedBy = null;
			GameEventPlayerDTO mainActor = null;
			List<GameEventPlayerDTO> secondaryActors = null;
			GameEventDTO mappedEvent = null;
			GamePeriodKeyEventsDTO mappedPeriodEvents = null;
			
			for(GameEvent event : periodEventsMap.get(period)) {
				secondaryActors = new ArrayList<GameEventPlayerDTO>();
				mainActor = null;
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
						mainActor = new GameEventPlayerDTO(
								player.getId().getRoster().getPlayer().getId(),
								player.getId().getRoster().getPlayer().getFirstName(),
								player.getId().getRoster().getPlayer().getLastName()
								);
						
						if(player.getId().getRoster().getTeam().getId() == event.getGame().getHomeTeam().getId())
							actedBy = RegulationScope.HOME;
						else
							actedBy = RegulationScope.AWAY;
					} else if(player.getRole().equals("Assist")) {
						secondaryActors.add(new GameEventPlayerDTO(
								player.getId().getRoster().getPlayer().getId(),
								player.getId().getRoster().getPlayer().getFirstName(),
								player.getId().getRoster().getPlayer().getLastName()
								));
					}
				}
				
				//bench penalty
				if(mainActor == null) {
					for(EventPlayer player : event.getPlayers()) {
						if(player.getRole().equals("ServedBy")) {
							mainActor = new GameEventPlayerDTO(
									player.getId().getRoster().getPlayer().getId(),
									player.getId().getRoster().getPlayer().getFirstName(),
									player.getId().getRoster().getPlayer().getLastName()
									);
							if(player.getId().getRoster().getTeam().getId() == event.getGame().getHomeTeam().getId())
								actedBy = RegulationScope.HOME;
							else
								actedBy = RegulationScope.AWAY;
						}
					}
				}
				
				mappedEvent = new GameEventDTO();
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
	
	public List<GameEventDTO> toGameEventsList(List<GameEvent> events) {
		List<GameEventDTO> mappedEvents = new ArrayList<GameEventDTO>();
		
		ScoreboardDTO scoreboard = new ScoreboardDTO(
				events.get(0).getGame().getHomeTeam().getName(),
				events.get(0).getGame().getHomeTeam().getAbbreviation(),
				0,
				events.get(0).getGame().getAwayTeam().getName(),
				events.get(0).getGame().getAwayTeam().getAbbreviation(),
				0);
		int previousEventTime = 0;
		PenaltyBoxDTO penalty = null;
		
		for(GameEvent ge : events) {
			RegulationScope actedBy = null;
			GameEventPlayerDTO mainActor = null;
			List<GameEventPlayerDTO> secondaryActors = new ArrayList<GameEventPlayerDTO>();
			penalty = null;
			
			if(ge.getEvent().getName().equals("Giveaway") || ge.getEvent().getName().equals("Takeaway")) {
				
				if(ge.getPlayers().size() > 1)
					LogUtil.writeLog("jsonId: " + ge.getGame().getJsonId() + "jsonEventId: " + ge.getJsonId() + " - oopsie alert: one-man event has more than 1 player");
				
				mainActor = new GameEventPlayerDTO(
						ge.getPlayers().get(0).getId().getRoster().getPlayer().getId(),
						ge.getPlayers().get(0).getId().getRoster().getPlayer().getFirstName(),
						ge.getPlayers().get(0).getId().getRoster().getPlayer().getLastName(),
						ge.getPlayers().get(0).getId().getRoster().getPlayer().getPrimaryNumber(),
						ge.getPlayers().get(0).getRole()
						);
				
				if(ge.getPlayers().get(0).getId().getRoster().getTeam().getId() == ge.getGame().getHomeTeam().getId()) {
					actedBy = RegulationScope.HOME;
				} else {
					actedBy = RegulationScope.AWAY;
				}
			}
			
			for(EventPlayer ep : ge.getPlayers()) {
				if(ge.getEvent().getName().equals("Giveaway") || ge.getEvent().getName().equals("Takeaway")) {
					break;
				}
				
				if(ge.getEvent().getName().equals("Goal") && ep.getRole().equals("Scorer")) {
					mainActor = new GameEventPlayerDTO(
							ep.getId().getRoster().getPlayer().getId(),
							ep.getId().getRoster().getPlayer().getFirstName(),
							ep.getId().getRoster().getPlayer().getLastName(),
							ep.getId().getRoster().getPlayer().getPrimaryNumber(),
							ep.getRole()
							);
					
					if(ep.getId().getRoster().getTeam().getId() == ge.getGame().getHomeTeam().getId()) {
						scoreboard.setHomeScore(scoreboard.getHomeScore() + 1);
						actedBy = RegulationScope.HOME;
					} else {
						scoreboard.setAwayScore(scoreboard.getAwayScore() + 1);
						actedBy = RegulationScope.AWAY;
					}
				} else if((ge.getEvent().getName().equals("Faceoff") && ep.getRole().equals("Winner")) || 
						(ge.getEvent().getName().equals("Hit") && ep.getRole().equals("Hitter")) ||
						(ge.getEvent().getName().equals("Shot On Goal") && ep.getRole().equals("Shooter")) ||
						(ge.getEvent().getName().equals("Missed Shot") && ep.getRole().equals("Shooter")) ||
						(ge.getEvent().getName().equals("Blocked Shot") && ep.getRole().equals("Blocker")) ||
						(ge.getEvent().getName().equals("Penalty") && ep.getRole().equals("PenaltyOn"))) {
					
					mainActor = new GameEventPlayerDTO(
							ep.getId().getRoster().getPlayer().getId(),
							ep.getId().getRoster().getPlayer().getFirstName(),
							ep.getId().getRoster().getPlayer().getLastName(),
							ep.getId().getRoster().getPlayer().getPrimaryNumber(),
							ep.getRole()
							);
					
					if(ep.getId().getRoster().getTeam().getId() == ge.getGame().getHomeTeam().getId()) {
						actedBy = RegulationScope.HOME;
					} else {
						actedBy = RegulationScope.AWAY;
					}
				} else {
					secondaryActors.add(new GameEventPlayerDTO(
							ep.getId().getRoster().getPlayer().getId(),
							ep.getId().getRoster().getPlayer().getFirstName(),
							ep.getId().getRoster().getPlayer().getLastName(),
							ep.getId().getRoster().getPlayer().getPrimaryNumber(),
							ep.getRole()
							));
				}
			}
			
			//bench penalties, etc have different role name
			if(ge.getEvent().getName().equals("Penalty") && mainActor == null) {
				for(GameEventPlayerDTO actor : secondaryActors) {
					if(actor.getRole().equals("ServedBy")) {
						mainActor = actor;
						for(EventPlayer ep : ge.getPlayers()) {
							if(ep.getId().getRoster().getPlayer().getId() == mainActor.getId()) {
								if(ep.getId().getRoster().getTeam().getId() == ge.getGame().getHomeTeam().getId()) {
									actedBy = RegulationScope.HOME;
								} else {
									actedBy = RegulationScope.AWAY;
								}
							}
						}
					}
				}
				
				if(mainActor == null) {
					LogUtil.writeLog("jsonId: " + ge.getGame().getJsonId() + "jsonEventId: " + ge.getJsonId() + " - oopsie alert: penalty event has no main actor");
				} else {
					secondaryActors.remove(mainActor);
				}
			}
			
			//penalty box object
			int gameSecs = gameTimeToSeconds(ge.getPeriodNumber(), ge.getPeriodTime());
			scoreboard.passTime(gameSecs - previousEventTime);
			previousEventTime = gameSecs;
			
			if(ge.getEvent().getName().equals("Penalty")) {
				penalty = new PenaltyBoxDTO(
						null,
						new GameEventPlayerDTO(mainActor),
						ge.getEvent().getPenaltyMinutes() + " minutes for " + ge.getEvent().getSecondaryType(),
						gameSecs,
						ge.getEvent().getPenaltyMinutes() * 60
				);
				
				if(ge.getEvent().getPenaltySeverity().equals("Minor") || ge.getEvent().getPenaltySeverity().equals("Bench")) {
					penalty.setPenaltyType(PenaltyBoxType.NORMAL);
				} else if(ge.getEvent().getPenaltySeverity().equals("Major") && ge.getEvent().getSecondaryType().equals("Fighting")) {
					penalty.setPenaltyType(PenaltyBoxType.COINCIDENTAL);
				} else if(ge.getEvent().getPenaltySeverity().equals("Major")) {
					penalty.setPenaltyType(PenaltyBoxType.MAJOR);
				} else if(ge.getEvent().getPenaltySeverity().equals("Misconduct")) {
					penalty.setPenaltyType(PenaltyBoxType.MISCONDUCT);
					for(GameEventPlayerDTO p : secondaryActors) {
						//lacklaster api not showing additional penalties that are sometimes added to misconducts, so logging this might be usefull in future
						if(p.getRole().equals("Served By")) {
							LogUtil.writeLog("served by in Misconduct game json id: " + ge.getGame().getJsonId() + " - my id: " + ge.getGame().getId());
						}
					}
				} else if(ge.getEvent().getPenaltySeverity().equals("Game Misconduct")) {
					penalty.setPenaltyType(PenaltyBoxType.MATCHMISCONDUCT);
					for(GameEventPlayerDTO p : secondaryActors) {
						if(p.getRole().equals("Served By")) {
							LogUtil.writeLog("served by in Game Misconduct game json id: " + ge.getGame().getJsonId() + " - my id: " + ge.getGame().getId());
						}
					}
				} else if(ge.getEvent().getPenaltySeverity().equals("Match")) {
					penalty.setPenaltyType(PenaltyBoxType.MATCHMISCONDUCT);
					for(GameEventPlayerDTO p : secondaryActors) {
						if(p.getRole().equals("Served By")) {
							LogUtil.writeLog("served by in Match game json id: " + ge.getGame().getJsonId() + " - my id: " + ge.getGame().getId());
						}
					}
				}
			}
			
			mappedEvents.add(
					new GameEventDTO(
							actedBy,
							ge.getPeriodNumber(),
							ge.getPeriodType(),
							ge.getPeriodTime(),
							ge.getCoordX(),
							ge.getCoordY(),
							ge.getEvent().getName(),
							ge.getEvent().getSecondaryType(),
							mainActor,
							secondaryActors,
							ge.getEvent().getStrength(),
							ge.getEvent().getEmptyNet(),
							ge.getEvent().getPenaltyMinutes(),
							ge.getEvent().getPenaltySeverity(),
							new ScoreboardDTO(scoreboard)
					)
			);
			//add penalty to scoreboard
			if(penalty != null) 
				scoreboard.addPlayerToPenaltyBox(ge.getGame().getJsonId(), actedBy, penalty);
			
			//remove penaltee after goal scored
			if(ge.getEvent().getName().equals("Goal") && ge.getEvent().getStrength().equals("Power Play"))
				scoreboard.removePenaltyAfterGoalScored(actedBy);
		}
		return mappedEvents;
	}
	
	private int gameTimeToSeconds(int periodNum, String periodTime) {
		return (periodNum - 1) * 20 * 60 + periodTimeToSeconds(periodTime);
	}
	
	private int periodTimeToSeconds(String time) {
		int minutes = Integer.valueOf(time.substring(0, 2));
		int seconds = Integer.valueOf(time.substring(3, 5));
		return 60 * minutes + seconds;
	}
}
