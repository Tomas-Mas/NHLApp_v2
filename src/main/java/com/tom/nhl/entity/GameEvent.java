package com.tom.nhl.entity;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "gameEvents")
public class GameEvent {

	private int id;
	private Game game;
	private int jsonId;
	private Event event;
	private int periodNumber;
	private String periodType;
	private String periodTime;
	private int coordX;
	private int coordY;
	private List<EventPlayer> players;
	
	/*private TeamType actedBy;
	private EventPlayer mainActor;*/
	
	@Id
	@SequenceGenerator(name = "gameEventIdGenerator", sequenceName = "SEQ_GAMEEVENTS_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gameEventIdGenerator")
	@Column(name = "ge_id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gameId")
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	@Column(name = "gameEventId", unique = true)
	public int getJsonId() {
		return jsonId;
	}
	public void setJsonId(int jsonId) {
		this.jsonId = jsonId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "eventId")
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	
	@Basic
	public int getPeriodNumber() {
		return periodNumber;
	}
	public void setPeriodNumber(int periodNumber) {
		this.periodNumber = periodNumber;
	}
	
	@Column(length = 15)
	public String getPeriodType() {
		return periodType;
	}
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
	
	@Column(length = 10)
	public String getPeriodTime() {
		return periodTime;
	}
	public void setPeriodTime(String periodTime) {
		this.periodTime = periodTime;
	}
	
	@Basic
	public int getCoordX() {
		return coordX;
	}
	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}
	
	@Basic
	public int getCoordY() {
		return coordY;
	}
	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}
	
	@OneToMany(mappedBy = "id.event", fetch = FetchType.LAZY)
	public List<EventPlayer> getPlayers() {
		return players;
	}
	public void setPlayers(List<EventPlayer> players) {
		this.players = players;
	}
	
	/*
	@Transient
	public TeamType getActedBy() {
		return actedBy;
	}
	public void setActedBy(TeamType causedBy) {
		this.actedBy = causedBy;
	}
	
	@Transient
	public EventPlayer getMainActor() {
		return mainActor;
	}
	public void setMainActor(EventPlayer mainActor) {
		this.mainActor = mainActor;
	}
	*/
}
