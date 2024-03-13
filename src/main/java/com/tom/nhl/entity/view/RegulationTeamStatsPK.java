package com.tom.nhl.entity.view;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

@Embeddable
public class RegulationTeamStatsPK implements Serializable {
	private static final long serialVersionUID = 4201595556713241738L;
	
	private int teamId;
	private int season;
	
	@Basic
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
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
		return Objects.hash(teamId, season);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(this.getClass() != o.getClass())
			return false;
		RegulationTeamStatsPK stats = (RegulationTeamStatsPK) o;
		return teamId == stats.getTeamId() && season == stats.getSeason();
	}
}
