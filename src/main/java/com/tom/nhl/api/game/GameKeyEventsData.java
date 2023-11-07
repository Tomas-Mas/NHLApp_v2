package com.tom.nhl.api.game;

import java.util.ArrayList;
import java.util.List;

import com.tom.nhl.entity.EventPlayer;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.enums.TeamType;

public class GameKeyEventsData {

	private int periodNumber;
	private String periodScore;
	private List<KeyEvent> events;
	
	public GameKeyEventsData(int periodNumber, List<GameEvent> events) {
		System.out.println("construct");
		this.periodNumber = periodNumber;
		
		this.events = new ArrayList<KeyEvent>();
		int homeScore = 0;
		int awayScore = 0;
		TeamType actedBy = null;
		GameEventPlayer mainActor = null;
		
		for(GameEvent event : events) {
			List<GameEventPlayer> secondaryActors = new ArrayList<GameEventPlayer>();
			for(EventPlayer player : event.getPlayers()) {
				if(player.getRole().equals("Scorer")) {
					if(player.getId().getRoster().getTeam().getId() == event.getGame().getHomeTeam().getId())
						homeScore++;
					else
						awayScore++;
				}
				if(player.getRole().equals("Scorer") || player.getRole().equals("PenaltyOn")) {
					mainActor = new GameEventPlayer(
							player.getId().getRoster().getPlayer().getId(),
							player.getId().getRoster().getPlayer().getFirstName(),
							player.getId().getRoster().getPlayer().getLastName()
							);
					
					if(player.getId().getRoster().getTeam().getId() == event.getGame().getHomeTeam().getId())
						actedBy = TeamType.HOME;
					else
						actedBy = TeamType.AWAY;
				} else if(player.getRole().equals("Assist")) {
					secondaryActors.add(new GameEventPlayer(
							player.getId().getRoster().getPlayer().getId(),
							player.getId().getRoster().getPlayer().getFirstName(),
							player.getId().getRoster().getPlayer().getLastName()
							));
				}
			}
			
			this.events.add(new KeyEvent(
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
		
		periodScore = homeScore + " - " + awayScore;
	}
	
	public int getPeriodNumber() {
		return periodNumber;
	}
	public void setPeriodNumber(int periodNumber) {
		this.periodNumber = periodNumber;
	}
	public String getPeriodScore() {
		return periodScore;
	}
	public void setPeriodScore(String periodScore) {
		this.periodScore = periodScore;
	}
	public List<KeyEvent> getEvents() {
		return events;
	}
	public void setEvents(List<KeyEvent> events) {
		this.events = events;
	}
}
