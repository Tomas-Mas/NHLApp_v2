package com.tom.nhl.dao;

import java.util.List;

import com.tom.nhl.entity.view.GameBasicDataView;

public interface GameBasicDataDAO {

	public GameBasicDataView getById(int id);
	public List<GameBasicDataView> getBySeasonWithPeriodGoals(int season);
	public List<GameBasicDataView> getH2hByGame(int gameId);
	public List<GameBasicDataView> getPlayoffBySeason(int season);
}
