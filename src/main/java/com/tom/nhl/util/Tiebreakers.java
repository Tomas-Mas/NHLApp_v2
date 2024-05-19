package com.tom.nhl.util;

import java.util.ArrayList;
import java.util.Comparator;

import com.tom.nhl.api.stats.TeamStats;

public class Tiebreakers {

	public static ArrayList<TeamStats> tiebreakerSort(ArrayList<TeamStats> teams) {
		teams.sort(new Comparator<TeamStats>() {
			public int compare(TeamStats t1, TeamStats t2) {
				//manual tiebreaker
				int res = manualTiebreaker(t1, t2);
				
				if(res != 0) return res;
				//points
				res = Integer.valueOf(t2.getPoints()).compareTo(t1.getPoints());
				if(res != 0) return res;
				//games played
				res = Integer.valueOf(t2.getGames()).compareTo(t1.getGames());
				if(res != 0) return res;
				//regulation wins
				res = Integer.valueOf(t2.getRegWins()).compareTo(t1.getRegWins());
				if(res != 0) return res;
				//Ot wins
				res = Integer.valueOf(t2.getOtWins()).compareTo(t1.getOtWins());
				if(res != 0) return res;
				//total wins
				res = Integer.valueOf(t2.getRegWins() + t2.getOtWins()).compareTo(t1.getRegWins() + t1.getOtWins());
				if(res != 0) return res;
				
				/*
				from nhl.com:
				The greater number of points earned in games against each other among two or more tied clubs. 
				For the purpose of determining standing for two or more Clubs that have not played an even number of games 
				with one or more of the other tied Clubs, the first game played in the city that has the extra game (the "odd game") 
				shall not be included. When more than two Clubs are tied, the percentage of available points earned in games among each other 
				(and not including any "odd games") shall be used to determine standing.
				*/
				
				//manually set tiebreaker for now
				/*res = manualTiebreaker(t1, t2);
				if(res != 0) return res;*/
				LogUtil.writeLog(t1.getSeason() + " Unresolved tiebreakers between teams: " + t1.getTeamName() + " - " + t2.getTeamName());
				
				
				//goal difference
				res = Integer.valueOf(t2.getGoalsFor() - t2.getGoalsAgainst()).compareTo(t1.getGoalsFor() - t2.getGoalsAgainst());
				if(res != 0) return res;
				//scored goals
				res = Integer.valueOf(t2.getGoalsFor()).compareTo(t1.getGoalsFor());
				return res;
			}
		});
		return teams;
	}
	
	private static int manualTiebreaker(TeamStats t1, TeamStats t2) {
		//2015 - 2016
		if(teamNameAndSeasonCheck(20152016, t1, t2, "Winnipeg Jets", "Arizona Coyotes"))
			return manualResForTiebreaker(t1, t2, "Arizona Coyotes");
		if(teamNameAndSeasonCheck(20152016, t1, t2, "Boston Bruins", "Detroit Red Wings"))
			return manualResForTiebreaker(t1, t2, "Detroit Red Wings");
		
		//2016 - 2017
		if(teamNameAndSeasonCheck(20162017, t1, t2, "San Jose Sharks", "St. Louis Blues"))
			return manualResForTiebreaker(t1, t2, "St. Louis Blues");
		if(teamNameAndSeasonCheck(20162017, t1, t2, "Nashville Predators", "Calgary Flames"))
			return manualResForTiebreaker(t1, t2, "Calgary Flames");
		
		//2017 - 2018
		if(teamNameAndSeasonCheck(20172018, t1, t2, "New Jersey Devils", "Columbus Blue Jackets"))
			return manualResForTiebreaker(t1, t2, "Columbus Blue Jackets");
			
		return 0;
	}
	
	private static boolean teamNameAndSeasonCheck(int season, TeamStats t1, TeamStats t2, String team1, String team2) {
		if(t1.getSeason() == season &&
				((t1.getTeamName().equals(team1) && t2.getTeamName().equals(team2)) ||
				(t2.getTeamName().equals(team1) || t2.getTeamName().equals(team1))))
			return true;
		else
			return false;
	}
	
	private static int manualResForTiebreaker(TeamStats t1, TeamStats t2, String higherSeedTeam) {
		if(t1.getTeamName().equals(higherSeedTeam))
			return -1;
		else if(t2.getTeamName().equals(higherSeedTeam))
			return 1;
		return 0;
	}
}
