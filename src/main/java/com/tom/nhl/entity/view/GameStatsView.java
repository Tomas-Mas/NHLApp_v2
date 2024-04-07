package com.tom.nhl.entity.view;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "gameStatsView")
public class GameStatsView {
	
	private GameStatsViewPK id;
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
	private int ppGoals;
	
	@Id
	public GameStatsViewPK getId() {
		return id;
	}
	public void setId(GameStatsViewPK id) {
		this.id = id;
	}
	
	@Basic
	public int getFaceoffs() {
		return faceoffs;
	}
	public void setFaceoffs(int faceoffs) {
		this.faceoffs = faceoffs;
	}
	
	@Basic
	public int getPenalties() {
		return penalties;
	}
	public void setPenalties(int penalties) {
		this.penalties = penalties;
	}
	
	@Basic
	public int getPenaltyMinutes() {
		return penaltyMinutes;
	}
	public void setPenaltyMinutes(int penaltyMinutes) {
		this.penaltyMinutes = penaltyMinutes;
	}
	
	@Basic
	public int getGiveaways() {
		return giveaways;
	}
	public void setGiveaways(int giveaways) {
		this.giveaways = giveaways;
	}
	
	@Basic
	public int getTakeaways() {
		return takeaways;
	}
	public void setTakeaways(int takeaways) {
		this.takeaways = takeaways;
	}
	
	@Basic
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	
	@Basic
	public int getShots() {
		return shots;
	}
	public void setShots(int shots) {
		this.shots = shots;
	}
	
	@Basic
	public int getShotsOnGoal() {
		return shotsOnGoal;
	}
	public void setShotsOnGoal(int shotsOnGoal) {
		this.shotsOnGoal = shotsOnGoal;
	}
	
	@Basic
	public int getBlockedShots() {
		return blockedShots;
	}
	public void setBlockedShots(int blockedShots) {
		this.blockedShots = blockedShots;
	}
	
	@Basic
	public int getMissedShots() {
		return missedShots;
	}
	public void setMissedShots(int missedShots) {
		this.missedShots = missedShots;
	}
	
	@Basic
	public int getGoals() {
		return goals;
	}
	public void setGoals(int goals) {
		this.goals = goals;
	}
	
	@Basic
	public int getPpGoals() {
		return ppGoals;
	}
	public void setPpGoals(int ppGoals) {
		this.ppGoals = ppGoals;
	}
}
