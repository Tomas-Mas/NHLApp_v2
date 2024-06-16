package com.tom.nhl.dao;

import java.util.List;
import java.util.Map;

public interface GameDAO {

	public List<Integer> getSeasons();
	public int getSeasonByGameId(int gameId);
	public Map<String, String> getTeamsByGameId(int gameId);
}
