package com.tom.nhl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "GoalieStats")
public class GoalieStats {

	private int id;
	private String timeOnIce;
	private int penaltyMinutes;
	private int shots;
	private int saves;
	private int powerPlayShots;
	private int powerPlaySaves;
	private int shortHandedShots;
	private int shortHandedSaves;
	
	@Id
	@SequenceGenerator(name = "goalieStatsIdGenerator", sequenceName = "SEQ_GOALIESTATS_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "goalieStatsIdGenerator")
	@Column(name = "gs_id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(length = 10)
	public String getTimeOnIce() {
		return timeOnIce;
	}
	public void setTimeOnIce(String timeOnIce) {
		this.timeOnIce = timeOnIce;
	}
	
	@Basic
	public int getPenaltyMinutes() {
		return penaltyMinutes;
	}
	public void setPenaltyMinutes(int penaltyMinutes) {
		this.penaltyMinutes = penaltyMinutes;
	}
	
	@Basic
	public int getShots() {
		return shots;
	}
	public void setShots(int shots) {
		this.shots = shots;
	}
	
	@Basic
	public int getSaves() {
		return saves;
	}
	public void setSaves(int saves) {
		this.saves = saves;
	}
	
	@Column(name = "ppShots")
	public int getPowerPlayShots() {
		return powerPlayShots;
	}
	public void setPowerPlayShots(int powerPlayShots) {
		this.powerPlayShots = powerPlayShots;
	}
	
	@Column(name = "ppSaves")
	public int getPowerPlaySaves() {
		return powerPlaySaves;
	}
	public void setPowerPlaySaves(int powerPlaySaves) {
		this.powerPlaySaves = powerPlaySaves;
	}
	
	@Column(name = "shShots")
	public int getShortHandedShots() {
		return shortHandedShots;
	}
	public void setShortHandedShots(int shortHandedShots) {
		this.shortHandedShots = shortHandedShots;
	}
	
	@Column(name = "shSaves")
	public int getShortHandedSaves() {
		return shortHandedSaves;
	}
	public void setShortHandedSaves(int shortHandedSaves) {
		this.shortHandedSaves = shortHandedSaves;
	}
}
