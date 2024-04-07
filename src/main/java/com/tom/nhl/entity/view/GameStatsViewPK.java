package com.tom.nhl.entity.view;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GameStatsViewPK implements Serializable {
	
	private static final long serialVersionUID = -4464897616795454552L;
	
	private int gameId;
	private int periodNumber;
	private String team;
	
	@Column(name = "gameId")
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	@Column(name = "periodNumber")
	public int getPeriodNumber() {
		return periodNumber;
	}
	public void setPeriodNumber(int periodNumber) {
		this.periodNumber = periodNumber;
	}
	
	@Column(name = "team")
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(gameId, periodNumber, team);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(this.getClass() != o.getClass()) 
			return false;
		GameStatsViewPK stats = (GameStatsViewPK) o;
		return gameId == stats.getGameId() && periodNumber == stats.getPeriodNumber() && team.equals(stats.getTeam());
	}
	
}
