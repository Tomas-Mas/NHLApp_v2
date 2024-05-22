package com.tom.nhl.dao;

import java.util.List;

import com.tom.nhl.entity.view.GameBasicDataView;

public interface GameBasicDataDAO {

	public GameBasicDataView getById(int id);
	public List<GameBasicDataView> getBySeasonWithPeriodGoals(int season);
	public List<GameBasicDataView> getByGameAndTeams(int gameId, int team1Id, int team2Id);
	public List<GameBasicDataView> getPlayoffBySeason(int season);
}
