package com.tom.nhl.entity.view;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SkaterStatsPerGamePK implements Serializable {
	private static final long serialVersionUID = 3479818815148038729L;
	
	private int gameId;
	private int playerId;
	
	@Column(name = "g_id")
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	@Column(name = "p_id")
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(gameId, playerId);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(this.getClass() != o.getClass())
			return false;
		SkaterStatsPerGamePK stats = (SkaterStatsPerGamePK) o;
		return gameId == stats.getGameId() && playerId == stats.getPlayerId();
	}
}
