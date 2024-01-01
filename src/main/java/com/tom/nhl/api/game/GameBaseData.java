package com.tom.nhl.api.game;

public class GameBaseData {

	private int id;
	private String gameDate;
	private String homeTeamName;
	private String homeTeamAbr;
	private int homeScore;
	private String awayTeamName;
	private String awayTeamAbr;
	private int awayScore;
	private String endPeriodType;
	private int[] homePeriodsScore;
	private int[] awayPeriodsScore;
	
	public GameBaseData(int id, String gameDate, String homeTeamName, String homeTeamAbr, int homeScore,
			String awayTeamName, String awayTeamAbr, int awayScore, String endPeriodType, int[] homePeriodsScore,
			int[] awayPeriodsScore) {
		this.id = id;
		this.gameDate = gameDate;
		this.homeTeamName = homeTeamName;
		this.homeTeamAbr = homeTeamAbr;
		this.homeScore = homeScore;
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
		
		if(endPeriodType.equals("REGULAR"))
			return "";
		else if(endPeriodType.equals("OVERTIME"))
			return "OT";
		else if(endPeriodType.equals("SHOOTOUT"))
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
