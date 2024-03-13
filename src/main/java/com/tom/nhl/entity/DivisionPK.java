package com.tom.nhl.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class DivisionPK implements Serializable {
	private static final long serialVersionUID = -4345325470073077595L;

	private Division division;
	private Team team;
	private int season;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "division_id")
	public Division getDivision() {
		return division;
	}
	public void setDivision(Division division) {
		this.division = division;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	
	@Basic
	public int getSeason() {
		return season;
	}
	public void setSeason(int season) {
		this.season = season;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(division.getJsonId(), team.getJsonId(), season);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null) 
			return false;
		if(this.getClass() != obj.getClass()) 
			return false;
		DivisionPK div = (DivisionPK) obj;
		return division.getJsonId() == div.getDivision().getJsonId() && team.getJsonId() == div.getTeam().getJsonId() && season == div.getSeason();
	}
	
	
}
