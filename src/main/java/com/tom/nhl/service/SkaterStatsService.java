package com.tom.nhl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.dao.SkaterStatsDAO;
import com.tom.nhl.dto.PlayerStatsFilterDTO;
import com.tom.nhl.dto.SkaterStatsDTO;
import com.tom.nhl.dto.StatsNavigationDTO;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.enums.SeasonScope;

@Component
public class SkaterStatsService {
	
	@Autowired
	private SkaterStatsDAO skaterStatsDAO;
	
	/**
	 * seasonScope - enum separating season into regulation/playoff part
	 * regulationScope - enum separating games into home/away/all
	 */
	public int getCountBySeason(int season, SeasonScope seasonScope, RegulationScope regulationScope) {
		return skaterStatsDAO.getCount(season, seasonScope, regulationScope);
	}
	
	public List<SkaterStatsDTO> getBySeasonAndNavigationDTO(int season, StatsNavigationDTO statsNavigation) {
		return skaterStatsDAO.getBySeasonAndNavigationDTO(season, statsNavigation);
	}
	
	public List<SkaterStatsDTO> getByGameId(int gameId, PlayerStatsFilterDTO statsFilter) {
		return skaterStatsDAO.getByGameId(gameId, statsFilter);
	}
}
