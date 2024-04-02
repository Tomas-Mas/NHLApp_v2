package com.tom.nhl.dao;

import java.util.List;

import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.entity.view.GameBasicDataView;

public interface GameDAO {

	public List<Integer> fetchSeasons();
	public int fetchSeasonByGameId(int gameId);
	public List<GameBasicDataView> fetchGamesBasicDataBySeason(int season);
	public GameBasicDataView fetchGameBasicDataById(int gameId);
	public List<GameBasicDataView> fetchGamesBasicDataByTeams(int gameId, int team1Id, int team2Id);
	public List<GameBasicDataView> fetchGamesGoalsPerPeriodData(List<GameBasicDataView> games);
	public List<GameBasicDataView> fetchPlayoffGamesBasicDataBySeason(int season);
	public List<GameEvent> fetchGamesKeyEventsById(int id);
}
