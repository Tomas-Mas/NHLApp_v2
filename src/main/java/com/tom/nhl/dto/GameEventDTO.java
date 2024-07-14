package com.tom.nhl.dto;

import java.util.List;

import com.tom.nhl.enums.RegulationScope;

public class GameEventDTO {

	private RegulationScope actedBy;
	private int periodNumber;
	private String periodType;
	private String periodTime;
	private int coordX;
	private int coordY;
	private String name;
	private String secondaryType;
	private GameEventPlayerDTO mainActor;
	private List<GameEventPlayerDTO> secondaryActors;
	private String strength;
	private String emptyNet;
	private int penaltyMinutes;
	private String penaltySeverity;
	private ScoreboardDTO scoreboard;
	
	public GameEventDTO() {	}
	
	//key event contructor
	public GameEventDTO(
			RegulationScope actedBy,
			String periodTime,
			String name,
			String secondaryType,
			GameEventPlayerDTO mainActor,
			List<GameEventPlayerDTO> secondaryActors,
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
	
	//event base data contructor
	public GameEventDTO(int periodNumber, String periodTime, String name, String secondaryType) {
		this.periodNumber = periodNumber;
		this.periodTime = periodTime;
		this.name = name;
		this.secondaryType = secondaryType;
	}
	
	//full event contructor
	public GameEventDTO(RegulationScope actedBy, int periodNumber, String periodType, String periodTime, int coordX,
			int coordY, String name, String secondaryType, GameEventPlayerDTO mainActor,
			List<GameEventPlayerDTO> secondaryActors, String strength, String emptyNet, int penaltyMinutes,
			String penaltySeverity, ScoreboardDTO scoreboard) {
		this.actedBy = actedBy;
		this.periodNumber = periodNumber;
		this.periodType = periodType;
		this.periodTime = periodTime;
		this.coordX = coordX;
		this.coordY = coordY;
		this.name = name;
		this.secondaryType = secondaryType;
		this.mainActor = mainActor;
		this.secondaryActors = secondaryActors;
		this.strength = strength;
		this.emptyNet = emptyNet;
		this.penaltyMinutes = penaltyMinutes;
		this.penaltySeverity = penaltySeverity;
		this.scoreboard = scoreboard;
	}

	public RegulationScope getActedBy() {
		return actedBy;
	}
	
	public void setActedBy(RegulationScope actedBy) {
		this.actedBy = actedBy;
	}
	
	public int getPeriodNumber() {
		return periodNumber;
	}
	
	public void setPeriodNumber(int periodNumber) {
		this.periodNumber = periodNumber;
	}
	
	public String getPeriodType() {
		return periodType;
	}
	
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
	
	public String getPeriodTime() {
		return periodTime;
	}
	
	public void setPeriodTime(String periodTime) {
		this.periodTime = periodTime;
	}
	
	public int getCoordX() {
		return coordX;
	}
	
	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}
	
	public int getCoordY() {
		return coordY;
	}
	
	public void setCoordY(int coordY) {
		this.coordY = coordY;
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
	
	public GameEventPlayerDTO getMainActor() {
		return mainActor;
	}
	
	public void setMainActor(GameEventPlayerDTO mainActor) {
		this.mainActor = mainActor;
	}
	
	public List<GameEventPlayerDTO> getSecondaryActors() {
		return secondaryActors;
	}
	
	public void setSecondaryActors(List<GameEventPlayerDTO> secondaryActors) {
		this.secondaryActors = secondaryActors;
	}
	
	public String getStrength() {
		return strength;
	}
	
	public void setStrength(String strength) {
		this.strength = strength;
	}
	
	public String getEmptyNet() {
		return emptyNet;
	}
	
	public void setEmptyNet(String emptyNet) {
		this.emptyNet = emptyNet;
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
	
	public ScoreboardDTO getScoreboard() {
		return scoreboard;
	}
	
	public void setScoreboard(ScoreboardDTO scoreboard) {
		this.scoreboard = scoreboard;
	}
}
