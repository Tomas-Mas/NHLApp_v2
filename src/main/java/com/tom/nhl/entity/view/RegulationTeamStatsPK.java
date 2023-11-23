package com.tom.nhl.entity.view;

import java.io.Serializable;

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
}
