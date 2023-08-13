package com.tom.nhl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Teams")
public class Team {

	private int id;
	private int jsonId;
	private String name;
	private String abbreviation;
	private String teamName;
	private String shortName;
	private Venue venue;
	private TimeZone timeZone;
	private String location;
	private int firstYear;
	//private DivisionTeam division;
	//private int conference;
	private String active;
	
	@Id
	@SequenceGenerator(name = "teamIdGenerator", sequenceName = "SEQ_TEAMS_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teamIdGenerator")
	@Column(name = "t_id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "t_jsonId", unique = true)
	public int getJsonId() {
		return jsonId;
	}
	public void setJsonId(int jsonId) {
		this.jsonId = jsonId;
	}
	
	@Column(name = "name", length = 50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "abbreviation", length = 5)
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	@Column(name = "teamName", length = 15)
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	@Column(name = "shortName", length = 15)
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "venueId")
	public Venue getVenue() {
		return venue;
	}
	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "timeZoneId")
	public TimeZone getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	
	@Column(length = 25)
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	@Basic
	public int getFirstYear() {
		return firstYear;
	}
	public void setFirstYear(int firstYear) {
		this.firstYear = firstYear;
	}
	
	@Column(length = 5)
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
}
