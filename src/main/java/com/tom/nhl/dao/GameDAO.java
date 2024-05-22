package com.tom.nhl.dao;

import java.util.List;

public interface GameDAO {

	public List<Integer> getSeasons();
	public int getSeasonByGameId(int gameId);
}
