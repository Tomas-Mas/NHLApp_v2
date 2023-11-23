package com.tom.nhl.entity.view;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "RegulationTeamStats")
public class RegulationTeamStats {

	private RegulationTeamStatsPK id;
	private String teamName;
	private String teamAbbreviation;
	private String conference;
	private String division;
	private int homeGames;
	private int homeGoalsFor;
	private int homeGoalsAgainst;
	private int homePoints;
	private int homeRegWins;
	private int homeRegLoses;
	private int homeOtWins;
	private int homeOtLoses;
	private int awayGames;
	private int awayGoalsFor;
	private int awayGoalsAgainst;
	private int awayPoints;
	private int awayRegWins;
	private int awayRegLoses;
	private int awayOtWins;
	private int awayOtLoses;
	
	@Id
	public RegulationTeamStatsPK getId() {
		return id;
	}
	public void setId(RegulationTeamStatsPK id) {
		this.id = id;
	}
	
	@Basic
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	@Column(name = "teamAbrName")
	public String getTeamAbbreviation() {
		return teamAbbreviation;
	}
	public void setTeamAbbreviation(String teamAbbreviation) {
		this.teamAbbreviation = teamAbbreviation;
	}
	
	@Basic
	public String getConference() {
		return conference;
	}
	public void setConference(String conference) {
		this.conference = conference;
	}
	
	@Basic
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	
	@Column(name = "home_games")
	public int getHomeGames() {
		return homeGames;
	}
	public void setHomeGames(int homeGames) {
		this.homeGames = homeGames;
	}
	
	@Column(name = "home_goalsFor")
	public int getHomeGoalsFor() {
		return homeGoalsFor;
	}
	public void setHomeGoalsFor(int homeGoalsFor) {
		this.homeGoalsFor = homeGoalsFor;
	}
	
	@Column(name = "home_goalsAgainst")
	public int getHomeGoalsAgainst() {
		return homeGoalsAgainst;
	}
	public void setHomeGoalsAgainst(int homeGoalsAgainst) {
		this.homeGoalsAgainst = homeGoalsAgainst;
	}
	
	@Column(name = "home_points")
	public int getHomePoints() {
		return homePoints;
	}
	public void setHomePoints(int homePoints) {
		this.homePoints = homePoints;
	}
	
	@Column(name = "home_regWins")
	public int getHomeRegWins() {
		return homeRegWins;
	}
	public void setHomeRegWins(int homeRegWins) {
		this.homeRegWins = homeRegWins;
	}
	
	@Column(name = "home_regLoses")
	public int getHomeRegLoses() {
		return homeRegLoses;
	}
	public void setHomeRegLoses(int homeRegLoses) {
		this.homeRegLoses = homeRegLoses;
	}
	
	@Column(name = "home_otWins")
	public int getHomeOtWins() {
		return homeOtWins;
	}
	public void setHomeOtWins(int homeOtWins) {
		this.homeOtWins = homeOtWins;
	}
	
	@Column(name = "home_otLoses")
	public int getHomeOtLoses() {
		return homeOtLoses;
	}
	public void setHomeOtLoses(int homeOtLoses) {
		this.homeOtLoses = homeOtLoses;
	}
	
	@Column(name = "away_games")
	public int getAwayGames() {
		return awayGames;
	}
	public void setAwayGames(int awayGames) {
		this.awayGames = awayGames;
	}
	
	@Column(name = "away_goalsFor")
	public int getAwayGoalsFor() {
		return awayGoalsFor;
	}
	public void setAwayGoalsFor(int awayGoalsFor) {
		this.awayGoalsFor = awayGoalsFor;
	}
	
	@Column(name = "away_goalsAgainst")
	public int getAwayGoalsAgainst() {
		return awayGoalsAgainst;
	}
	public void setAwayGoalsAgainst(int awayGoalsAgainst) {
		this.awayGoalsAgainst = awayGoalsAgainst;
	}
	
	@Column(name = "away_points")
	public int getAwayPoints() {
		return awayPoints;
	}
	public void setAwayPoints(int awayPoints) {
		this.awayPoints = awayPoints;
	}
	
	@Column(name = "away_regWins")
	public int getAwayRegWins() {
		return awayRegWins;
	}
	public void setAwayRegWins(int awayRegWins) {
		this.awayRegWins = awayRegWins;
	}
	
	@Column(name = "away_regLoses")
	public int getAwayRegLoses() {
		return awayRegLoses;
	}
	public void setAwayRegLoses(int awayRegLoses) {
		this.awayRegLoses = awayRegLoses;
	}
	
	@Column(name = "away_otWins")
	public int getAwayOtWins() {
		return awayOtWins;
	}
	public void setAwayOtWins(int awayOtWins) {
		this.awayOtWins = awayOtWins;
	}
	
	@Column(name = "away_otLoses")
	public int getAwayOtLoses() {
		return awayOtLoses;
	}
	public void setAwayOtLoses(int awayOtLoses) {
		this.awayOtLoses = awayOtLoses;
	}
}
