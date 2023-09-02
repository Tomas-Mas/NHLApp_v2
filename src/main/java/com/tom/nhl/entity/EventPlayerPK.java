package com.tom.nhl.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class EventPlayerPK implements Serializable {
	private static final long serialVersionUID = -8486669463720979112L;
	
	private GameEvent event;
	private Roster roster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	public GameEvent getEvent() {
		return event;
	}
	public void setEvent(GameEvent event) {
		this.event = event;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roster_id")
	public Roster getRoster() {
		return roster;
	}
	public void setRoster(Roster roster) {
		this.roster = roster;
	}
	
}
