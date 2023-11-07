package com.tom.nhl.api.game;

import java.util.List;

import com.tom.nhl.enums.TeamType;

public class KeyEvent {

	//private int periodNumber;
	private TeamType actedBy;
	private String periodTime;
	private String name;	//TODO enum
	private String secondaryType;
	private GameEventPlayer mainActor;
	private List<GameEventPlayer> secondaryActors;
	//goal stuff
	private String strength;
	//penalty stuff
	private int penaltyMinutes;
	private String penaltySeverity;
	
	public KeyEvent(/*int periodNumber,*/
			TeamType actedBy,
			String periodTime,
			String name,
			String secondaryType,
			GameEventPlayer mainActor,
			List<GameEventPlayer> secondaryActors,
			String strength,
			int penaltyMinutes,
			String penaltySeverity) {
		this.actedBy = actedBy;
		this.periodTime = periodTime;
		this.name = name;
		this.secondaryType = secondaryType;
		this.mainActor = mainActor;
		this.secondaryActors = secondaryActors;
		this.strength = strength;
		this.penaltyMinutes = penaltyMinutes;
		this.penaltySeverity = penaltySeverity;
	}
	
	/*public int getPeriodNumber() {
		return periodNumber;
	}
	public void setPeriodNumber(int periodNumber) {
		this.periodNumber = periodNumber;
	}*/
	public TeamType getActedBy() {
		return actedBy;
	}
	public void setActedBy(TeamType actedBy) {
		this.actedBy = actedBy;
	}
	public String getPeriodTime() {
		return periodTime;
	}
	public void setPeriodTime(String periodTime) {
		this.periodTime = periodTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSecondaryType() {
		return secondaryType;
	}
	public void setSecondaryType(String secondaryType) {
		this.secondaryType = secondaryType;
	}
	public GameEventPlayer getMainActor() {
		return mainActor;
	}
	public void setMainActor(GameEventPlayer mainActor) {
		this.mainActor = mainActor;
	}
	public List<GameEventPlayer> getSecondaryActors() {
		return secondaryActors;
	}
	public void setSecondaryActors(List<GameEventPlayer> secondaryActors) {
		this.secondaryActors = secondaryActors;
	}
	public String getStrength() {
		return strength;
	}
	public void setStrength(String strength) {
		this.strength = strength;
	}
	public int getPenaltyMinutes() {
		return penaltyMinutes;
	}
	public void setPenaltyMinutes(int penaltyMinutes) {
		this.penaltyMinutes = penaltyMinutes;
	}
	public String getPenaltySeverity() {
		return penaltySeverity;
	}
	public void setPenaltySeverity(String penaltySeverity) {
		this.penaltySeverity = penaltySeverity;
	}
}
