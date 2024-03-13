package com.tom.nhl.entity.view;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "SkaterStatsPerGame")
public class SkaterStatsPerGame {
	
	private int season;
	private String gameType;
	private SkaterStatsPerGamePK id;
	private String firstName;
	private String lastName;
	private String position;
	private String team;
	private int timeOnIce;
	private int plusMinus;
	private int gamesPlayed;
	private int goals;
	private int assists;
	private int points;
	private int ppGoals;
	private int ppPoints;
	private int shGoals;
	private int shPoints;
	private int otGoals;
	private int shootoutTaken;
	private int shootoutGoals;
	private int pim;
	private int shots;
	private int blockedShots;
	private int faceoffs;
	private int faceoffsWon;
	private int hits;
	private int hitsTaken;
	private int takeaway;
	private int giveaway;
	
	@Basic
	public int getSeason() {
		return season;
	}
	public void setSeason(int season) {
		this.season = season;
	}
	
	@Basic
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	
	@Id
	public SkaterStatsPerGamePK getId() {
		return id;
	}
	public void setId(SkaterStatsPerGamePK id) {
		this.id = id;
	}
	
	@Basic
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Basic
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Basic
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	@Basic
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	
	@Basic
	public int getTimeOnIce() {
		return timeOnIce;
	}
	public void setTimeOnIce(int timeOnIce) {
		this.timeOnIce = timeOnIce;
	}
	
	@Basic
	public int getPlusMinus() {
		return plusMinus;
	}
	public void setPlusMinus(int plusMinus) {
		this.plusMinus = plusMinus;
	}
	
	@Basic
	public int getGamesPlayed() {
		return gamesPlayed;
	}
	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}
	
	@Basic
	public int getGoals() {
		return goals;
	}
	public void setGoals(int goals) {
		this.goals = goals;
	}
	
	@Basic
	public int getAssists() {
		return assists;
	}
	public void setAssists(int assists) {
		this.assists = assists;
	}
	
	@Basic
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	
	@Basic
	public int getPpGoals() {
		return ppGoals;
	}
	public void setPpGoals(int ppGoals) {
		this.ppGoals = ppGoals;
	}
	
	@Basic
	public int getPpPoints() {
		return ppPoints;
	}
	public void setPpPoints(int ppPoints) {
		this.ppPoints = ppPoints;
	}
	
	@Basic
	public int getShGoals() {
		return shGoals;
	}
	public void setShGoals(int shGoals) {
		this.shGoals = shGoals;
	}
	
	@Basic
	public int getShPoints() {
		return shPoints;
	}
	public void setShPoints(int shPoints) {
		this.shPoints = shPoints;
	}
	
	@Basic
	public int getOtGoals() {
		return otGoals;
	}
	public void setOtGoals(int otGoals) {
		this.otGoals = otGoals;
	}
	
	@Basic
	public int getShootoutTaken() {
		return shootoutTaken;
	}
	public void setShootoutTaken(int shootoutTaken) {
		this.shootoutTaken = shootoutTaken;
	}
	
	@Basic
	public int getShootoutGoals() {
		return shootoutGoals;
	}
	public void setShootoutGoals(int shootoutGoals) {
		this.shootoutGoals = shootoutGoals;
	}
	
	@Basic
	public int getPim() {
		return pim;
	}
	public void setPim(int pim) {
		this.pim = pim;
	}
	
	@Basic
	public int getShots() {
		return shots;
	}
	public void setShots(int shots) {
		this.shots = shots;
	}
	
	@Basic
	public int getBlockedShots() {
		return blockedShots;
	}
	public void setBlockedShots(int blockedShots) {
		this.blockedShots = blockedShots;
	}
	
	@Basic
	public int getFaceoffs() {
		return faceoffs;
	}
	public void setFaceoffs(int faceoffs) {
		this.faceoffs = faceoffs;
	}
	
	@Basic
	public int getFaceoffsWon() {
		return faceoffsWon;
	}
	public void setFaceoffsWon(int faceoffsWon) {
		this.faceoffsWon = faceoffsWon;
	}
	
	@Basic
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	
	@Basic
	public int getHitsTaken() {
		return hitsTaken;
	}
	public void setHitsTaken(int hitsTaken) {
		this.hitsTaken = hitsTaken;
	}
	
	@Basic
	public int getTakeaway() {
		return takeaway;
	}
	public void setTakeaway(int takeaway) {
		this.takeaway = takeaway;
	}
	
	@Basic
	public int getGiveaway() {
		return giveaway;
	}
	public void setGiveaway(int giveaway) {
		this.giveaway = giveaway;
	}
	
}
