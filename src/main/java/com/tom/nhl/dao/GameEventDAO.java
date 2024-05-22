package com.tom.nhl.dao;

import java.util.List;

import com.tom.nhl.entity.GameEvent;

public interface GameEventDAO {

	public List<GameEvent> getKeyEventsByGame(int gameId);
	public List<String> getPeriodTypesByGame(int gameId);
}
