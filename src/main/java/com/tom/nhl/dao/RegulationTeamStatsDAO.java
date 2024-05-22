package com.tom.nhl.dao;

import java.util.List;

import com.tom.nhl.entity.view.RegulationTeamStats;

public interface RegulationTeamStatsDAO {

	public List<RegulationTeamStats> getBySeason(int season);
}
