package com.tom.nhl.api.stats;

public class TeamStats {

	private String teamName;
	private String teamAbbreviation;
	private int teamId;
	private String conference;
	private String division;
	private int season;
	private int games;
	private int goalsFor;
	private int goalsAgainst;
	private int points;
	private int regWins;
	private int regLoses;
	private int otWins;
	private int otLoses;
	
	public TeamStats(
			String teamName,
			String teamAbbreviation,
			int teamId,
			String conference,
			String division,
			int season,
			int games,
			int goalsFor,
			int goalsAgainst,
			int points,
			int regWins,
			int regLoses,
			int otWins,
			int otLoses) {
		this.teamName = teamName;
		this.teamAbbreviation = teamAbbreviation;
		this.teamId = teamId;
		this.conference = conference;
		this.division = division;
		this.season = season;
		this.games = games;
		this.goalsFor = goalsFor;
		this.goalsAgainst = goalsAgainst;
		this.points = points;
		this.regWins = regWins;
		this.regLoses = regLoses;
		this.otWins = otWins;
		this.otLoses = otLoses;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamAbbreviation() {
		return teamAbbreviation;
	}

	public void setTeamAbbreviation(String teamAbbreviation) {
		this.teamAbbreviation = teamAbbreviation;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getConference() {
		return conference;
	}

	public void setConference(String conference) {
		this.conference = conference;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public int getGames() {
		return games;
	}

	public void setGames(int games) {
		this.games = games;
	}

	public int getGoalsFor() {
		return goalsFor;
	}

	public void setGoalsFor(int goalsFor) {
		this.goalsFor = goalsFor;
	}

	public int getGoalsAgainst() {
		return goalsAgainst;
	}

	public void setGoalsAgainst(int goalsAgainst) {
		this.goalsAgainst = goalsAgainst;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getRegWins() {
		return regWins;
	}

	public void setRegWins(int regWins) {
		this.regWins = regWins;
	}

	public int getRegLoses() {
		return regLoses;
	}

	public void setRegLoses(int regLoses) {
		this.regLoses = regLoses;
	}

	public int getOtWins() {
		return otWins;
	}

	public void setOtWins(int otWins) {
		this.otWins = otWins;
	}

	public int getOtLoses() {
		return otLoses;
	}

	public void setOtLoses(int otLoses) {
		this.otLoses = otLoses;
	}
}
