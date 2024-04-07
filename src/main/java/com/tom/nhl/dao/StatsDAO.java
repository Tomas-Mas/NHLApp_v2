package com.tom.nhl.dao;

import java.util.List;

import com.tom.nhl.api.stats.GameStats;
import com.tom.nhl.api.stats.SkaterStats;
import com.tom.nhl.dto.StatsNavigationDTO;
import com.tom.nhl.entity.view.RegulationTeamStats;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.enums.SeasonScope;

public interface StatsDAO {
	
	public List<RegulationTeamStats> fetchRegulationTeamStandings(int season);
	public int fetchSkaterStatsCount(int season, SeasonScope seasonScope, RegulationScope regulationScope);
	public List<SkaterStats> fetchSkaterStats(int season, StatsNavigationDTO statsNavigation);
	public List<GameStats> fetchGameStatsById(int gameId, int periodNum);
}
