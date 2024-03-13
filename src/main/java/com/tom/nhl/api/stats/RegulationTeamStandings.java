package com.tom.nhl.api.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tom.nhl.util.Tiebreakers;

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
		return Tiebreakers.tiebreakerSort(teamsByConference);
	}
	
	public ArrayList<TeamStats> getTeamsByDivision(String division) {
		ArrayList<TeamStats> teamsByDivision = new ArrayList<TeamStats>();
		for(TeamStats team : teams) {
			if(team.getDivision().equals(division)) {
				teamsByDivision.add(team);
			}
		}
		return Tiebreakers.tiebreakerSort(teamsByDivision);
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
	
	public Set<String> getConferences() {
		return confDiv.keySet();
	}
	
	public ArrayList<String> getDivisions() {
		ArrayList<String> divisions = new ArrayList<String>();
		for(String conf : confDiv.keySet()) {
			divisions.addAll(confDiv.get(conf));
		}
		return divisions;
	}
	
	public List<TeamStats> getDivisionWinners(String conference) {
		ArrayList<TeamStats> divWinners = new ArrayList<TeamStats>();
		for(String division : confDiv.get(conference)) {
			divWinners.add(getTeamsByDivision(division).get(0));
		}
		return Tiebreakers.tiebreakerSort(divWinners);
	}
	
	public List<TeamStats> getDivisionRunnerUps(String division) {
		List<TeamStats> divRunnerUps = new ArrayList<TeamStats>();
		List<TeamStats> divTeams = getTeamsByDivision(division);
		divRunnerUps.add(divTeams.get(1));
		divRunnerUps.add(divTeams.get(2));
		return divRunnerUps;
	}
	
	public List<TeamStats> getConferenceWildCards(String conference) {
		ArrayList<TeamStats> wildCards = new ArrayList<TeamStats>();
		List<TeamStats> divTeams;
		for(String division : confDiv.get(conference)) {
			divTeams = getTeamsByDivision(division);
			wildCards.add(divTeams.get(3));
			wildCards.add(divTeams.get(4));
		}
		return Tiebreakers.tiebreakerSort(wildCards);
	}
	
	public Map<String, List<TeamStats>> getConferenceTeamMap() {
		Map<String, List<TeamStats>> conferenceTeamMap = new HashMap<String, List<TeamStats>>();
		for(String conference : confDiv.keySet()) {
			conferenceTeamMap.put(conference, getTeamsByConference(conference));
		}
		return conferenceTeamMap;
	}
}
