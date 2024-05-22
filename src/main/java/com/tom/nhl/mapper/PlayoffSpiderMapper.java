package com.tom.nhl.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tom.nhl.dto.GameBasicDataDTO;
import com.tom.nhl.dto.PlayoffBracket;
import com.tom.nhl.dto.PlayoffMatch;
import com.tom.nhl.dto.PlayoffSpiderDTO;
import com.tom.nhl.dto.TeamStandingsDTO;
import com.tom.nhl.dto.TeamStats;
import com.tom.nhl.util.Tiebreakers;

@Component
public class PlayoffSpiderMapper {

	public PlayoffSpiderDTO toPlayoffSpider(TeamStandingsDTO teamStandings, List<GameBasicDataDTO> playoffGames) {
		PlayoffSpiderDTO playoffSpider;
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
				
				
		playoffSpider = new PlayoffSpiderDTO(brackets, finals);
		
		return playoffSpider;
	}
	
	private PlayoffMatch mapFirstRoundPlayoffMatch(TeamStats higherSeedTeam, TeamStats lowerSeedTeam, List<TeamStats> seeding, List<GameBasicDataDTO> playoffGames) {
		int higherSeedScore = 0;
		int lowerSeedScore = 0;
		String gameWinner = "";
		TeamStats matchWinner = null;
		List<GameBasicDataDTO> matchGames = new ArrayList<GameBasicDataDTO>();
		for(GameBasicDataDTO game : playoffGames) {
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
	
	private PlayoffMatch mapSecondThirdRoundPlayoffMatch(PlayoffMatch higherSeedMatch, PlayoffMatch lowerSeedMatch, List<TeamStats> seeding, List<GameBasicDataDTO> playoffGames) {
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
					new ArrayList<GameBasicDataDTO>(),
					null);
		} else {
			match = mapFirstRoundPlayoffMatch(higherSeedTeam, lowerSeedTeam, seeding, playoffGames);
		}
		
		return match;
	}
}
