package com.tom.nhl.entity.view;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "GoalsPerPeriod")
public class GoalsPerPeriod {

	private String id;
	private GameBasicDataView game;
	private String team;
	private int period;
	private int goals;
	
	@Id
	@Column(name = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gameId")
	public GameBasicDataView getGame() {
		return game;
	}
	public void setGame(GameBasicDataView game) {
		this.game = game;
	}
	
	@Basic
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	
	@Basic
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	
	@Basic
	public int getGoals() {
		return goals;
	}
	public void setGoals(int goals) {
		this.goals = goals;
	}
}
