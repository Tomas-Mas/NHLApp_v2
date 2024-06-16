package com.tom.nhl.dao;

import java.util.List;

import com.tom.nhl.entity.view.GameBasicDataView;
import com.tom.nhl.enums.RegulationScope;

public interface GameBasicDataDAO {

	public GameBasicDataView getById(int id);
	public List<GameBasicDataView> getBySeasonWithPeriodGoals(int season);
	public List<GameBasicDataView> getH2hByGame(int gameId);
	public List<GameBasicDataView> getPlayoffBySeason(int season);
	public List<GameBasicDataView> getLastGamesByAbr(int gameId, String teamAbr, RegulationScope regScope, int gamesCount);
}
