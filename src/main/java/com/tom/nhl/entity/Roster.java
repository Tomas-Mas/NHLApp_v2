package com.tom.nhl.entity;

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

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "Rosters")
public class Roster {

	private int id;
	private Game game;
	private Team team;
	private Player player;
	private SkaterStats skaterStats;
	private GoalieStats goalieStats;
	
	@Id
	@SequenceGenerator(name = "rosterIdGenerator", sequenceName = "SEQ_ROSTERS_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rosterIdGenerator")
	@Column(name = "r_id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "g_id")
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "t_id")
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "p_id")
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ss_id")
	public SkaterStats getSkaterStats() {
		return skaterStats;
	}
	public void setSkaterStats(SkaterStats skaterStats) {
		this.skaterStats = skaterStats;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gs_id")
	public GoalieStats getGoalieStats() {
		return goalieStats;
	}
	public void setGoalieStats(GoalieStats goalieStats) {
		this.goalieStats = goalieStats;
	}
	
}
