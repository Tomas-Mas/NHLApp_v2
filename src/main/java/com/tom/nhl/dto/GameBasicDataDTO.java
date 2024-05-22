package com.tom.nhl.dto;

import com.tom.nhl.enums.SeasonScope;

public class GameBasicDataDTO {

	private int id;
	private String gameDate;
	private SeasonScope gameType;
	private String gameStatus;
	private int homeTeamId;
	private String homeTeamName;
	private String homeTeamAbr;
	private int homeScore;
	private int awayTeamId;
	private String awayTeamName;
	private String awayTeamAbr;
	private int awayScore;
	private String endPeriodType;
	private int[] homePeriodsScore;
	private int[] awayPeriodsScore;
	
	public GameBasicDataDTO(int id, String gameDate, SeasonScope gameType, String gameStatus, int homeTeamId, String homeTeamName, String homeTeamAbr, int homeScore,
			int awayTeamId, String awayTeamName, String awayTeamAbr, int awayScore, String endPeriodType, int[] homePeriodsScore,
			int[] awayPeriodsScore) {
		this.id = id;
		this.gameDate = gameDate;
		this.gameType = gameType;
		this.gameStatus = gameStatus;
		this.homeTeamId = homeTeamId;
		this.homeTeamName = homeTeamName;
		this.homeTeamAbr = homeTeamAbr;
		this.homeScore = homeScore;
		this.awayTeamId = awayTeamId;
		this.awayTeamName = awayTeamName;
		this.awayTeamAbr = awayTeamAbr;
		this.awayScore = awayScore;
		this.endPeriodType = endPeriodType;
		this.homePeriodsScore = homePeriodsScore;
		this.awayPeriodsScore = awayPeriodsScore;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGameDate() {
		return gameDate;
	}

	public void setGameDate(String gameDate) {
		this.gameDate = gameDate;
	}
	
	public SeasonScope getGameType() {
		return gameType;
	}
	
	public void setGameType(SeasonScope gameType) {
		this.gameType = gameType;
	}
	
	public String getGameStatus() {
		return gameStatus;
	}
	
	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}
	
	public int getHomeTeamId() {
		return homeTeamId;
	}
	
	public void setHomeTeamId(int homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}

	public String getHomeTeamAbr() {
		return homeTeamAbr;
	}

	public void setHomeTeamAbr(String homeTeamAbr) {
		this.homeTeamAbr = homeTeamAbr;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}
	
	public int getAwayTeamId() {
		return awayTeamId;
	}
	
	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}

	public String getAwayTeamName() {
		return awayTeamName;
	}

	public void setAwayTeamName(String awayTeamName) {
		this.awayTeamName = awayTeamName;
	}

	public String getAwayTeamAbr() {
		return awayTeamAbr;
	}

	public void setAwayTeamAbr(String awayTeamAbr) {
		this.awayTeamAbr = awayTeamAbr;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	public String getEndPeriodType() {
		//return endPeriodType;
		
		if(endPeriodType.equals("REG"))
			return "";
		else if(endPeriodType.equals("OT"))
			return "OT";
		else if(endPeriodType.equals("SO"))
			return "SO";
		else
			return "N/A";
	}

	public void setEndPeriodType(String endPeriodType) {
		this.endPeriodType = endPeriodType;
	}

	public int[] getHomePeriodsScore() {
		return homePeriodsScore;
	}

	public void setHomePeriodsScore(int[] homePeriodsScore) {
		this.homePeriodsScore = homePeriodsScore;
	}

	public int[] getAwayPeriodsScore() {
		return awayPeriodsScore;
	}

	public void setAwayPeriodsScore(int[] awayPeriodsScore) {
		this.awayPeriodsScore = awayPeriodsScore;
	}
}
