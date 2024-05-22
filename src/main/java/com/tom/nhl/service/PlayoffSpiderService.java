package com.tom.nhl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.dto.GameBasicDataDTO;
import com.tom.nhl.dto.PlayoffSpiderDTO;
import com.tom.nhl.dto.TeamStandingsDTO;
import com.tom.nhl.mapper.PlayoffSpiderMapper;

@Component
public class PlayoffSpiderService {
	
	@Autowired
	private TeamStatsService teamStatsService;
	@Autowired
	private GameBasicDataService gameBasicDataService;
	@Autowired
	private PlayoffSpiderMapper playoffSpiderMapper;

	public PlayoffSpiderDTO getBySeason(int season) {
		TeamStandingsDTO teamStandings = teamStatsService.getStandingsBySeason(season);
		List<GameBasicDataDTO> playoffGames = gameBasicDataService.getPlayoffGamesBySeason(season);
		return playoffSpiderMapper.toPlayoffSpider(teamStandings, playoffGames);
	}
}
