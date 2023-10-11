package com.tom.nhl.entity.view;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "MainPageGameBasicData")
public class MainPageGameBasicData {

	private int id;
	private Date gameDate;
	private String homeTeam;
	private String homeAbr;
	private int homeScore;
	private String awayTeam;
	private String awayAbr;
	private int awayScore;
	private Integer endPeriod;
	private String endPeriodType;
	
	private Set<GoalsPerPeriod> goalsPerPeriod;
	
	@Id
	@Column(name = "g_id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "gameDate")
	public Date getGameDate() {
		return gameDate;
	}
	public void setGameDate(Date gameDate) {
		this.gameDate = gameDate;
	}
	
	@Column(name = "homeTeam")
	public String getHomeTeam() {
		return homeTeam;
	}
	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}
	
	@Column(name = "homeAbr")
	public String getHomeAbr() {
		return homeAbr;
	}
	public void setHomeAbr(String homeAbr) {
		this.homeAbr = homeAbr;
	}
	
	@Column(name = "homeScore")
	public int getHomeScore() {
		return homeScore;
	}
	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}
	
	@Column(name = "awayTeam")
	public String getAwayTeam() {
		return awayTeam;
	}
	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}
	
	@Column(name = "awayAbr")
	public String getAwayAbr() {
		return awayAbr;
	}
	public void setAwayAbr(String awayAbr) {
		this.awayAbr = awayAbr;
	}
	
	@Column(name = "awayScore")
	public int getAwayScore() {
		return awayScore;
	}
	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}
	
	@Column(name = "periodNum")
	public int getEndPeriod() {
		return endPeriod;
	}
	public void setEndPeriod(int endPeriod) {
		this.endPeriod = endPeriod;
	}
	
	@Column(name = "periodType")
	public String getEndPeriodType() {
		return endPeriodType;
	}
	public void setEndPeriodType(String endPeriodType) {
		this.endPeriodType = endPeriodType;
	}
	
	@OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
	public Set<GoalsPerPeriod> getGoalsPerPeriod() {
		return goalsPerPeriod;
	}
	public void setGoalsPerPeriod(Set<GoalsPerPeriod> goalsPerPeriod) {
		this.goalsPerPeriod = goalsPerPeriod;
	}
}
