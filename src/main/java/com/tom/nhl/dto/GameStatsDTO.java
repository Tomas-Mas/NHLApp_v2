package com.tom.nhl.dto;

public class GameStatsDTO {

	private String team;
	private int faceoffs;
	private int penalties;
	private int penaltyMinutes;
	private int giveaways;
	private int takeaways;
	private int hits;
	private int shots;
	private int shotsOnGoal;
	private int blockedShots;
	private int missedShots;
	private int goals;
	private int powerPlayGoals;
	
	public GameStatsDTO(String team, long faceoffs, long penalties, long penaltyMinutes, long giveaways, long takeaways, 
			long hits, long shots, long shotsOnGoal, long blockedShots, long missedShots, long goals, long powerPlayGoals) {
		super();
		this.team = team;
		this.faceoffs = (int) faceoffs;
		this.penalties = (int) penalties;
		this.penaltyMinutes = (int) penaltyMinutes;
		this.giveaways = (int) giveaways;
		this.takeaways = (int) takeaways;
		this.hits = (int) hits;
		this.shots = (int) shots;
		this.shotsOnGoal = (int) shotsOnGoal;
		this.blockedShots = (int) blockedShots;
		this.missedShots = (int) missedShots;
		this.goals = (int) goals;
		this.powerPlayGoals = (int) powerPlayGoals;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public int getFaceoffs() {
		return faceoffs;
	}

	public void setFaceoffs(int faceoffs) {
		this.faceoffs = faceoffs;
	}

	public int getPenalties() {
		return penalties;
	}

	public void setPenalties(int penalties) {
		this.penalties = penalties;
	}

	public int getPenaltyMinutes() {
		return penaltyMinutes;
	}

	public void setPenaltyMinutes(int penaltyMinutes) {
		this.penaltyMinutes = penaltyMinutes;
	}

	public int getGiveaways() {
		return giveaways;
	}

	public void setGiveaways(int giveaways) {
		this.giveaways = giveaways;
	}

	public int getTakeaways() {
		return takeaways;
	}

	public void setTakeaways(int takeaways) {
		this.takeaways = takeaways;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getShots() {
		return shots;
	}

	public void setShots(int shots) {
		this.shots = shots;
	}

	public int getShotsOnGoal() {
		return shotsOnGoal;
	}

	public void setShotsOnGoal(int shotsOnGoal) {
		this.shotsOnGoal = shotsOnGoal;
	}

	public int getBlockedShots() {
		return blockedShots;
	}

	public void setBlockedShots(int blockedShots) {
		this.blockedShots = blockedShots;
	}

	public int getMissedShots() {
		return missedShots;
	}

	public void setMissedShots(int missedShots) {
		this.missedShots = missedShots;
	}

	public int getGoals() {
		return goals;
	}

	public void setGoals(int goals) {
		this.goals = goals;
	}
	
	public int getPowerPlayGoals() {
		return powerPlayGoals;
	}
	
	public void setPowerPlayGoals(int powerPlayGoals) {
		this.powerPlayGoals = powerPlayGoals;
	}
}
