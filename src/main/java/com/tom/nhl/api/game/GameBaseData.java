package com.tom.nhl.api.game;

import java.text.SimpleDateFormat;

import com.tom.nhl.entity.view.GoalsPerPeriod;
import com.tom.nhl.entity.view.MainPageGameBasicData;
import com.tom.nhl.enums.TeamType;
import com.tom.nhl.util.LogUtil;

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
	
	public GameBaseData(MainPageGameBasicData gameData) {
		this.id = gameData.getId();
		this.gameDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(gameData.getGameDate());
		this.homeTeamName = gameData.getHomeTeam();
		this.homeTeamAbr = gameData.getHomeAbr();
		this.homeScore = gameData.getHomeScore();
		this.awayTeamName = gameData.getAwayTeam();
		this.awayTeamAbr = gameData.getAwayAbr();
		this.awayScore = gameData.getAwayScore();
		this.endPeriodType = gameData.getEndPeriodType();
		
		homePeriodsScore = new int[gameData.getEndPeriod()];
		awayPeriodsScore = new int[gameData.getEndPeriod()];
		
		for(GoalsPerPeriod goals : gameData.getGoalsPerPeriod()) {
			if(goals.getTeam().equals(TeamType.HOME.formatted())) {
				homePeriodsScore[goals.getPeriod() - 1] = goals.getGoals();
			} else if(goals.getTeam().equals(TeamType.AWAY.formatted())) {
				awayPeriodsScore[goals.getPeriod() - 1] = goals.getGoals();
			} else {
				LogUtil.writeLog("game id: " + id + "mapping of main page games basic info has an oopsie; sql didn't find correct team type for goals per periods");
				System.out.println("!!! oopsie has been logged");
			}
		}
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
