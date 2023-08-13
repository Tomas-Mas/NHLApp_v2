package com.tom.nhl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "SkaterStats")
public class SkaterStats {

	private int id;
	private String timeOnIce;
	private String powerPlayTimeOnIce;
	private String shortHandedTimeOnIce;
	private int penaltyMinutes;
	private int plusMinus;
	
	@Id
	@SequenceGenerator(name = "skaterStatsIdGenerator", sequenceName = "SEQ_SKATERSTATS_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skaterStatsIdGenerator")
	@Column(name = "ss_id")
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
	
	@Column(name = "ppTimeOnIce", length = 10)
	public String getPowerPlayTimeOnIce() {
		return powerPlayTimeOnIce;
	}
	public void setPowerPlayTimeOnIce(String powerPlayTimeOnIce) {
		this.powerPlayTimeOnIce = powerPlayTimeOnIce;
	}
	
	@Column(name = "shTimeOnIce", length = 10)
	public String getShortHandedTimeOnIce() {
		return shortHandedTimeOnIce;
	}
	public void setShortHandedTimeOnIce(String shortHandedTimeOnIce) {
		this.shortHandedTimeOnIce = shortHandedTimeOnIce;
	}
	
	@Basic
	public int getPenaltyMinutes() {
		return penaltyMinutes;
	}
	public void setPenaltyMinutes(int penaltyMinutes) {
		this.penaltyMinutes = penaltyMinutes;
	}
	
	@Basic
	public int getPlusMinus() {
		return plusMinus;
	}
	public void setPlusMinus(int plusMinus) {
		this.plusMinus = plusMinus;
	}
	
}
