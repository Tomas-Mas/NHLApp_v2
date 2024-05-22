package com.tom.nhl.dao;

import java.util.List;

import com.tom.nhl.dto.SkaterStatsDTO;
import com.tom.nhl.dto.StatsNavigationDTO;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.enums.SeasonScope;

public interface SkaterStatsDAO {

	public int getCount(int season, SeasonScope seasonScope, RegulationScope regulationScope);
	public List<SkaterStatsDTO> getBySeasonAndNavigationDTO(int season, StatsNavigationDTO statsNavigation);
}
