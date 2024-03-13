package com.tom.nhl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.api.game.GameBaseData;
import com.tom.nhl.api.playoffspider.PlayoffBracket;
import com.tom.nhl.api.playoffspider.PlayoffMatch;
import com.tom.nhl.api.playoffspider.PlayoffSpider;
import com.tom.nhl.api.stats.RegulationTeamStandings;
import com.tom.nhl.api.stats.SkaterStats;
import com.tom.nhl.api.stats.TeamStats;
import com.tom.nhl.dao.JPAStatsDAO;
import com.tom.nhl.dto.StatsNavigationDTO;
import com.tom.nhl.entity.view.RegulationTeamStats;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.enums.SeasonScope;
import com.tom.nhl.util.Tiebreakers;

@Component
public class StatsService {
	
	@Autowired
	private JPAStatsDAO statsDAO;
	
	public RegulationTeamStandings getRegulationTeamStandings(int season, RegulationScope statsType) {
		long start = System.currentTimeMillis();
		List<RegulationTeamStats> dbStats = statsDAO.fetchRegulationTeamStandings(season);
		RegulationTeamStandings teamStandings = mapRegulationTeamStandings(season, dbStats, statsType);
		System.out.println("fetching of team standings took: " + (System.currentTimeMillis() - start) + "\n");
		return teamStandings;
	}
	
	public int getSkaterStatsCount(int season, SeasonScope seasonScope, RegulationScope regulationScope) {
		long start = System.currentTimeMillis();
		int count = statsDAO.fetchSkaterStatsCount(season, seasonScope, regulationScope);
		System.out.println("fetching skater stats count took: " + (System.currentTimeMillis() - start) + "\n");
		return count;
	}
	
	public List<SkaterStats> getSkaterStats(int season, StatsNavigationDTO statsNavigation) {
		long start = System.currentTimeMillis();
		System.out.println("fetching skaters");
		List<SkaterStats> stats = statsDAO.fetchSkaterStats(season, statsNavigation);
		System.out.println("fetching skater stats took: " + (System.currentTimeMillis() - start) + "\n");
		return stats;
	}
	
	public PlayoffSpider getPlayoffSpider(RegulationTeamStandings teamStandings, List<GameBaseData> playoffGames) {
		PlayoffSpider playoffSpider = mapPlayoffSpider(teamStandings, playoffGames);
		return playoffSpider;
	}
	
	private PlayoffSpider mapPlayoffSpider(RegulationTeamStandings teamStandings, List<GameBaseData> playoffGames) {
		PlayoffSpider playoffSpider;
		List<PlayoffBracket> brackets = new ArrayList<PlayoffBracket>();
		List<PlayoffMatch> firstRoundMatches;
		List<PlayoffMatch> secondThirdRoundMatches;
		PlayoffMatch finals;
		
		for(String conference : teamStandings.getConferences()) {
			firstRoundMatches = new ArrayList<PlayoffMatch>();
			secondThirdRoundMatches = new ArrayList<PlayoffMatch>();
			
			List<TeamStats> seeding = teamStandings.getTeamsByConference(conference);
			List<TeamStats> divWinners = teamStandings.getDivisionWinners(conference);
			List<TeamStats> winningDivRunnerUps = teamStandings.getDivisionRunnerUps(divWinners.get(0).getDivision());
			List<TeamStats> secondDivRunnerUps = teamStandings.getDivisionRunnerUps(divWinners.get(1).getDivision());
			List<TeamStats> wildCards = teamStandings.getConferenceWildCards(conference);
			
			firstRoundMatches.add(mapFirstRoundPlayoffMatch(divWinners.get(0), wildCards.get(1), seeding, playoffGames));
			firstRoundMatches.add(mapFirstRoundPlayoffMatch(winningDivRunnerUps.get(0), winningDivRunnerUps.get(1), seeding, playoffGames));
			firstRoundMatches.add(mapFirstRoundPlayoffMatch(secondDivRunnerUps.get(0), secondDivRunnerUps.get(1), seeding, playoffGames));
			firstRoundMatches.add(mapFirstRoundPlayoffMatch(divWinners.get(1), wildCards.get(0), seeding, playoffGames));
			
			secondThirdRoundMatches.add(mapSecondThirdRoundPlayoffMatch(firstRoundMatches.get(0), firstRoundMatches.get(1), seeding, playoffGames));
			secondThirdRoundMatches.add(mapSecondThirdRoundPlayoffMatch(firstRoundMatches.get(2), firstRoundMatches.get(3), seeding, playoffGames));
			
			secondThirdRoundMatches.add(mapSecondThirdRoundPlayoffMatch(secondThirdRoundMatches.get(0), secondThirdRoundMatches.get(1), seeding, playoffGames));
			
			brackets.add(new PlayoffBracket(conference, firstRoundMatches, secondThirdRoundMatches));
		}
		finals = mapSecondThirdRoundPlayoffMatch(
				brackets.get(0).getSecondThirdRound().get(brackets.get(0).getSecondThirdRound().size() - 1), 
				brackets.get(1).getSecondThirdRound().get(brackets.get(1).getSecondThirdRound().size() - 1),
				Tiebreakers.tiebreakerSort(teamStandings.getTeams()),
				playoffGames);
				
				
		playoffSpider = new PlayoffSpider(brackets, finals);
		
		return playoffSpider;
	}
	
	private PlayoffMatch mapFirstRoundPlayoffMatch(TeamStats higherSeedTeam, TeamStats lowerSeedTeam, List<TeamStats> seeding, List<GameBaseData> playoffGames) {
		int higherSeedScore = 0;
		int lowerSeedScore = 0;
		String gameWinner = "";
		TeamStats matchWinner = null;
		List<GameBaseData> matchGames = new ArrayList<GameBaseData>();
		for(GameBaseData game : playoffGames) {
			if((game.getHomeTeamName().equals(higherSeedTeam.getTeamName()) && game.getAwayTeamName().equals(lowerSeedTeam.getTeamName())) ||
					(game.getAwayTeamName().equals(higherSeedTeam.getTeamName()) && game.getHomeTeamName().equals(lowerSeedTeam.getTeamName()))) {
				matchGames.add(game);
				if(game.getHomeScore() > game.getAwayScore()) {
					gameWinner = game.getHomeTeamName();
				} else {
					gameWinner = game.getAwayTeamName();
				}
				
				if(gameWinner.equals(higherSeedTeam.getTeamName())) {
					higherSeedScore++;
				} else {
					lowerSeedScore++;
				}
			}
		}
		
		if(higherSeedScore == 4) {
			matchWinner = higherSeedTeam;
		} else if(lowerSeedScore == 4) {
			matchWinner = lowerSeedTeam;
		}
		
		PlayoffMatch match = new PlayoffMatch(
				higherSeedTeam,
				seeding.indexOf(higherSeedTeam) + 1, 
				higherSeedScore,
				lowerSeedTeam,
				seeding.indexOf(lowerSeedTeam) + 1,
				lowerSeedScore,
				null,
				null,
				matchGames,
				matchWinner);
		
		return match;
	}
	
	private PlayoffMatch mapSecondThirdRoundPlayoffMatch(PlayoffMatch higherSeedMatch, PlayoffMatch lowerSeedMatch, List<TeamStats> seeding, List<GameBaseData> playoffGames) {
		PlayoffMatch match;
		TeamStats higherSeedTeam = null;
		TeamStats lowerSeedTeam = null;
		if(higherSeedMatch.getWinner() != null)
			higherSeedTeam = higherSeedMatch.getWinner();
		if(lowerSeedMatch.getWinner() != null)
			lowerSeedTeam = lowerSeedMatch.getWinner();
		
		if(higherSeedTeam == null || lowerSeedTeam == null) {
			match = new PlayoffMatch(
					higherSeedTeam,
					seeding.indexOf(higherSeedTeam),
					0,
					lowerSeedTeam,
					seeding.indexOf(lowerSeedTeam),
					0,
					higherSeedMatch,
					lowerSeedMatch,
					new ArrayList<GameBaseData>(),
					null);
		} else {
			match = mapFirstRoundPlayoffMatch(higherSeedTeam, lowerSeedTeam, seeding, playoffGames);
		}
		
		return match;
	}
	
	private RegulationTeamStandings mapRegulationTeamStandings(int season, List<RegulationTeamStats> dbStats, RegulationScope statsType) {
		ArrayList<TeamStats> teamStats = new ArrayList<TeamStats>();
		for(RegulationTeamStats team : dbStats) {
			if(statsType == RegulationScope.OVERALL) {
				teamStats.add(new TeamStats(
						team.getTeamName(),
						team.getTeamAbbreviation(),
						team.getId().getTeamId(),
						team.getConference(),
						team.getDivision(),
						team.getId().getSeason(),
						team.getHomeGames() + team.getAwayGames(),
						team.getHomeGoalsFor() + team.getAwayGoalsFor(),
						team.getHomeGoalsAgainst() + team.getAwayGoalsAgainst(),
						team.getHomePoints() + team.getAwayPoints(),
						((team.getHomePoints() + team.getAwayPoints()) * 1000) / ((team.getHomeGames() + team.getAwayGames()) * 2),
						team.getHomeRegWins() + team.getAwayRegWins(),
						team.getHomeRegLoses() + team.getAwayRegLoses(),
						team.getHomeOtWins() + team.getAwayOtWins(),
						team.getHomeOtLoses() + team.getAwayOtLoses()
				));
			} else if(statsType == RegulationScope.HOME) {
				teamStats.add(new TeamStats(
						team.getTeamName(),
						team.getTeamAbbreviation(),
						team.getId().getTeamId(),
						team.getConference(),
						team.getDivision(),
						team.getId().getSeason(),
						team.getHomeGames(),
						team.getHomeGoalsFor(),
						team.getHomeGoalsAgainst(),
						team.getHomePoints(),
						(team.getHomePoints() * 1000) / (team.getHomeGames() * 2),
						team.getHomeRegWins(),
						team.getHomeRegLoses(),
						team.getHomeOtWins(),
						team.getHomeOtLoses()
				));
			} else if(statsType == RegulationScope.AWAY) {
				teamStats.add(new TeamStats(
						team.getTeamName(),
						team.getTeamAbbreviation(),
						team.getId().getTeamId(),
						team.getConference(),
						team.getDivision(),
						team.getId().getSeason(),
						team.getAwayGames(),
						team.getAwayGoalsFor(),
						team.getAwayGoalsAgainst(),
						team.getAwayPoints(),
						(team.getAwayPoints() * 1000) / (team.getAwayGames() * 2),
						team.getAwayRegWins(),
						team.getAwayRegLoses(),
						team.getAwayOtWins(),
						team.getAwayOtLoses()
				));
			}
		}
		return new RegulationTeamStandings(season, teamStats);
	}
}
