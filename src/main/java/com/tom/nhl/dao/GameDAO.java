package com.tom.nhl.dao;

import java.util.List;

import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.entity.view.MainPageGameBasicData;
import com.tom.nhl.entity.view.RegulationTeamStats;

public interface GameDAO {

	public List<Integer> getSeasons();
	public List<MainPageGameBasicData> fetchGamesBasicData(int season);
	public List<GameEvent> fetchGamesKeyEvents(int id);
	public List<RegulationTeamStats> fetchTeamStandings(int season);
}
