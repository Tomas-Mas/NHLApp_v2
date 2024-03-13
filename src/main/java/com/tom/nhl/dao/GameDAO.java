package com.tom.nhl.dao;

import java.util.List;

import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.entity.view.GameBasicDataView;

public interface GameDAO {

	public List<Integer> getSeasons();
	public List<GameBasicDataView> fetchGamesBasicData(int season);
	public List<GameBasicDataView> fetchGamesGoalsPerPeriodData(List<GameBasicDataView> games);
	public List<GameBasicDataView> fetchPlayoffGamesBasicData(int season);
	public List<GameEvent> fetchGamesKeyEvents(int id);
}
