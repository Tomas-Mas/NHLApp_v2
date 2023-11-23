package com.tom.nhl.api.stats;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.tom.nhl.util.LogUtil;

public class RegulationTeamStandings {

	private int season;
	private ArrayList<TeamStats> teams;
	private HashMap<String, ArrayList<String>> confDiv;
	
	public RegulationTeamStandings(int season, ArrayList<TeamStats> teams) {
		this.season = season;
		this.teams = teams;
		
		this.confDiv = new HashMap<String, ArrayList<String>>();
		for(TeamStats team : teams) {
			if(!confDiv.keySet().contains(team.getConference())) {
				confDiv.put(team.getConference(), new ArrayList<String>());
			}
			if(confDiv.get(team.getConference()).indexOf(team.getDivision()) == -1) {
				confDiv.get(team.getConference()).add(team.getDivision());
			}
		}
	}
	
	public ArrayList<TeamStats> getTeamsByConference(String conference) {
		ArrayList<TeamStats> teamsByConference = new ArrayList<TeamStats>();
		for(TeamStats team : teams) {
			if(team.getConference().equals(conference)) {
				teamsByConference.add(team);
			}
		}
		return tiebreakerSort(teamsByConference);
	}
	
	public ArrayList<TeamStats> getTeamsByDivision(String division) {
		ArrayList<TeamStats> teamsByDivision = new ArrayList<TeamStats>();
		for(TeamStats team : teams) {
			if(team.getDivision().equals(division)) {
				teamsByDivision.add(team);
			}
		}
		return tiebreakerSort(teamsByDivision);
	}
	
	private ArrayList<TeamStats> tiebreakerSort(ArrayList<TeamStats> teams) {
		teams.sort(new Comparator<TeamStats>() {
			
			public int compare(TeamStats t1, TeamStats t2) {
				//points
				int res = Integer.valueOf(t2.getPoints()).compareTo(t1.getPoints());
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
				
				LogUtil.writeLog(season + " Unresolved tiebreakers between teams: " + t1.getTeamName() + " - " + t2.getTeamName());
				// TODO from nhl.com about tiebreakers
				/*
				The greater number of points earned in games against each other among two or more tied clubs. 
				For the purpose of determining standing for two or more Clubs that have not played an even number of games 
				with one or more of the other tied Clubs, the first game played in the city that has the extra game (the "odd game") 
				shall not be included. When more than two Clubs are tied, the percentage of available points earned in games among each other 
				(and not including any "odd games") shall be used to determine standing.
				*/
				
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
	
	public int getSeason() {
		return season;
	}
	
	public void setSeason(int season) {
		this.season = season;
	}
	
	public ArrayList<TeamStats> getTeams() {
		return teams;
	}
	
	public void setTeamStats(ArrayList<TeamStats> teams) {
		this.teams = teams;
	}
	
	public HashMap<String,ArrayList<String>> getConfDiv() {
		return confDiv;
	}
	
	public Map<String, List<TeamStats>> getConferenceTeamMap() {
		Map<String, List<TeamStats>> conferenceTeamMap = new HashMap<String, List<TeamStats>>();
		for(String conference : confDiv.keySet()) {
			conferenceTeamMap.put(conference, getTeamsByConference(conference));
		}
		return conferenceTeamMap;
	}
}
