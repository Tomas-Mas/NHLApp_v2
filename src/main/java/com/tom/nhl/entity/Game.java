package com.tom.nhl.entity;

import java.sql.Timestamp;
import java.util.List;

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
@Table(name = "Games")
public class Game {

	private int id;
	private int jsonId;
	private String gameType;
	private int season;
	private Timestamp gameDate;
	private int awayScore;
	private Team awayTeam;
	private int homeScore;
	private Team homeTeam;
	private Venue venue;
	private GameStatus gameStatus;
	private List<GameEvent> events;
	
	/*private String resultDetail;
	private String formattedGameDate;
	private List<Integer> homePeriodScore;
	private List<Integer> awayPeriodScore;
	private List<List<GameEvent>> eventsPerPeriod;*/
	
	@Id
	@SequenceGenerator(name = "gameIdGenerator", sequenceName = "SEQ_GAMES_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gameIdGenerator")
	@Column(name = "g_id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "g_jsonId", unique = true)
	public int getJsonId() {
		return jsonId;
	}
	public void setJsonId(int jsonId) {
		this.jsonId = jsonId;
	}
	
	@Column(name = "gameType", length = 5)
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	
	@Column(name = "season")
	public int getSeason() {
		return season;
	}
	public void setSeason(int season) {
		this.season = season;
	}
	
	@Column(name = "gameDate")
	public Timestamp getGameDate() {
		return gameDate;
	}
	public void setGameDate(Timestamp gameDate) {
		this.gameDate = gameDate;
	}
	
	@Column(name = "awayScore")
	public int getAwayScore() {
		return awayScore;
	}
	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "awayTeamId")
	public Team getAwayTeam() {
		return awayTeam;
	}
	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}
	
	@Column(name = "homeScore")
	public int getHomeScore() {
		return homeScore;
	}
	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "homeTeamId")
	public Team getHomeTeam() {
		return homeTeam;
	}
	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "venueId")
	public Venue getVenue() {
		return venue;
	}
	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gameStatus")
	public GameStatus getGameStatus() {
		return gameStatus;
	}
	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}
	
	@OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
	public List<GameEvent> getEvents() {
		return events;
	}
	public void setEvents(List<GameEvent> events) {
		this.events = events;
	}
	
	/*
	@Transient
	public String getFormattedGameDate() {
		formattedGameDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(getGameDate());
		return formattedGameDate;
	}
	public void setFormattedGameDate(String formattedGameDate) {
		this.formattedGameDate = formattedGameDate;
	}
	
	@Transient
	public String getResultDetail() {
		if(events == null || events.size() == 0)
			return "";
		
		String lastPeriodType = events.get(events.size() -1).getPeriodType();
		if(lastPeriodType.equals("REGULAR"))
			setResultDetail("");
		else if(lastPeriodType.equals("OVERTIME"))
			setResultDetail("OT");
		else if(lastPeriodType.equals("SHOOTOUT"))
			setResultDetail("SO");
		else
			setResultDetail("N/A");
		
		return resultDetail;
	}
	public void setResultDetail(String resultDetail) {
		this.resultDetail = resultDetail;
	}
	
	@Transient
	public List<Integer> getHomePeriodScore() {
		return homePeriodScore;
	}
	public void setHomePeriodScore(List<Integer> homePeriodScore) {
		this.homePeriodScore = homePeriodScore;
	}
	
	@Transient
	public List<Integer> getAwayPeriodScore() {
		return awayPeriodScore;
	}
	public void setAwayPeriodScore(List<Integer> awayPeriodScore) {
		this.awayPeriodScore = awayPeriodScore;
	}
	
	@Transient
	public List<List<GameEvent>> getEventsPerPeriod() {
		return this.eventsPerPeriod;
	}
	public void setEventsPerPeriod(List<List<GameEvent>> eventsPerPeriod) {
		this.eventsPerPeriod = eventsPerPeriod;
	}
	*/
	
}
