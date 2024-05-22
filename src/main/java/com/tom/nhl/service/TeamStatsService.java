package com.tom.nhl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.config.DefaultConstants;
import com.tom.nhl.dao.RegulationTeamStatsDAO;
import com.tom.nhl.dto.TeamStandingsDTO;
import com.tom.nhl.entity.view.RegulationTeamStats;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.mapper.TeamStandingsMapper;

@Component
public class TeamStatsService {
	
	@Autowired
	private RegulationTeamStatsDAO regulationTeamStatsDAO;
	
	@Autowired
	private TeamStandingsMapper teamStandingsMapper;

	public TeamStandingsDTO getStandingsBySeason(int season) {
		List<RegulationTeamStats> teamStats = regulationTeamStatsDAO.getBySeason(season);
		return teamStandingsMapper.toTeamStandingsDTO(season, teamStats, DefaultConstants.TEAM_STANDINGS_DEFAULT_REGULATION_SCOPE);
	}
	
	public TeamStandingsDTO getStandingsBySeasonAndType(int season, RegulationScope statsType) {
		List<RegulationTeamStats> teamStats = regulationTeamStatsDAO.getBySeason(season);
		return teamStandingsMapper.toTeamStandingsDTO(season, teamStats, statsType);
	}
}
