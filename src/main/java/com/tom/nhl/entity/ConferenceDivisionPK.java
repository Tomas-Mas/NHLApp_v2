package com.tom.nhl.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ConferenceDivisionPK implements Serializable {
	private static final long serialVersionUID = 5708749014633229283L;

	private Conference conference;
	private Division division;
	private int season;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conference_id")
	public Conference getConference() {
		return conference;
	}
	public void setConference(Conference conference) {
		this.conference = conference;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "division_id")
	public Division getDivision() {
		return division;
	}
	public void setDivision(Division division) {
		this.division = division;
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
		return Objects.hash(conference.getJsonId(), division.getJsonId(), season);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(this.getClass() != obj.getClass())
			return false;
		ConferenceDivisionPK pk = (ConferenceDivisionPK) obj;
		return conference.getJsonId() == pk.getConference().getJsonId() && division.getJsonId() == pk.getDivision().getJsonId() && season == pk.getSeason();
	}
	
}
