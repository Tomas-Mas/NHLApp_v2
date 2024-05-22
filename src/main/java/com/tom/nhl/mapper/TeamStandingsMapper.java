package com.tom.nhl.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tom.nhl.dto.TeamStandingsDTO;
import com.tom.nhl.dto.TeamStats;
import com.tom.nhl.entity.view.RegulationTeamStats;
import com.tom.nhl.enums.RegulationScope;

@Component
public class TeamStandingsMapper {

	public TeamStandingsDTO toTeamStandingsDTO(int season, List<RegulationTeamStats> regulationTeamStats, RegulationScope statsType) {
		ArrayList<TeamStats> teamStats = new ArrayList<TeamStats>();
		for(RegulationTeamStats team : regulationTeamStats) {
			TeamStats stats = new TeamStats();
			stats.setTeamName(team.getTeamName());
			stats.setTeamAbbreviation(team.getTeamAbbreviation());
			stats.setTeamId(team.getId().getTeamId());
			stats.setConference(team.getConference());
			stats.setDivision(team.getDivision());
			stats.setSeason(team.getId().getSeason());

			if(statsType == RegulationScope.HOME) {
				stats.setGames(team.getHomeGames());
				stats.setGoalsFor(team.getHomeGoalsFor());
				stats.setGoalsAgainst(team.getHomeGoalsAgainst());
				stats.setPoints(team.getHomePoints());
				stats.setPointPercentage((team.getHomePoints() * 1000) / (team.getHomeGames() * 2));
				stats.setRegWins(team.getHomeRegWins());
				stats.setRegLoses(team.getHomeRegLoses());
				stats.setOtWins(team.getHomeOtWins());
				stats.setOtLoses(team.getHomeOtLoses());
			} else if(statsType == RegulationScope.AWAY) {
				stats.setGames(team.getAwayGames());
				stats.setGoalsFor(team.getAwayGoalsFor());
				stats.setGoalsAgainst(team.getAwayGoalsAgainst());
				stats.setPoints(team.getAwayPoints());
				stats.setPointPercentage((team.getAwayPoints() * 1000) / (team.getAwayGames() * 2));
				stats.setRegWins(team.getAwayRegWins());
				stats.setRegLoses(team.getAwayRegLoses());
				stats.setOtWins(team.getAwayOtWins());
				stats.setOtLoses(team.getAwayOtLoses());
			} else {
				stats.setGames(team.getHomeGames() + team.getAwayGames());
				stats.setGoalsFor(team.getHomeGoalsFor() + team.getAwayGoalsFor());
				stats.setGoalsAgainst(team.getHomeGoalsAgainst() + team.getAwayGoalsAgainst());
				stats.setPoints(team.getHomePoints() + team.getAwayPoints());
				stats.setPointPercentage(((team.getHomePoints() + team.getAwayPoints()) * 1000) / ((team.getHomeGames() + team.getAwayGames()) * 2));
				stats.setRegWins(team.getHomeRegWins() + team.getAwayRegWins());
				stats.setRegLoses(team.getHomeRegLoses() + team.getAwayRegLoses());
				stats.setOtWins(team.getHomeOtWins() + team.getAwayOtWins());
				stats.setOtLoses(team.getHomeOtLoses() + team.getAwayOtLoses());
			}
			teamStats.add(stats);
		}
		return new TeamStandingsDTO(season, teamStats);
	}
}
