package com.tom.nhl.dto;

import java.util.List;

public class PlayoffMatch {

	private TeamStats higherSeedTeam;
	private int higherSeedSeeding;
	private int higherSeedScore;
	private TeamStats lowerSeedTeam;
	private int lowerSeedSeeding;
	private int lowerSeedScore;
	
	private PlayoffMatch higherSeedMatch;
	private PlayoffMatch lowerSeedMatch;
	
	private List<GameBasicDataDTO> games;
	private TeamStats winner;
	
	public PlayoffMatch(
			TeamStats higherSeedTeam, 
			int higherSeedSeeding,
			int higherSeedScore, 
			TeamStats lowerSeedTeam, 
			int lowerSeedSeeding,
			int lowerSeedScore, 
			PlayoffMatch higherSeedMatch, 
			PlayoffMatch lowerSeedMatch, 
			List<GameBasicDataDTO> games,
			TeamStats winner) {
		this.higherSeedTeam = higherSeedTeam;
		this.higherSeedSeeding = higherSeedSeeding;
		this.higherSeedScore = higherSeedScore;
		this.lowerSeedTeam = lowerSeedTeam;
		this.lowerSeedSeeding = lowerSeedSeeding;
		this.lowerSeedScore = lowerSeedScore;
		this.higherSeedMatch = higherSeedMatch;
		this.lowerSeedMatch = lowerSeedMatch;
		this.games = games;
		this.winner = winner;
	}

	public TeamStats getHigherSeedTeam() {
		return higherSeedTeam;
	}

	public void setHigherSeedTeam(TeamStats higherSeedTeam) {
		this.higherSeedTeam = higherSeedTeam;
	}

	public int getHigherSeedSeeding() {
		return higherSeedSeeding;
	}

	public void setHigherSeedSeeding(int higherSeedSeeding) {
		this.higherSeedSeeding = higherSeedSeeding;
	}

	public int getHigherSeedScore() {
		return higherSeedScore;
	}

	public void setHigherSeedScore(int higherSeedScore) {
		this.higherSeedScore = higherSeedScore;
	}

	public TeamStats getLowerSeedTeam() {
		return lowerSeedTeam;
	}

	public void setLowerSeedTeam(TeamStats lowerSeedTeam) {
		this.lowerSeedTeam = lowerSeedTeam;
	}

	public int getLowerSeedSeeding() {
		return lowerSeedSeeding;
	}

	public void setLowerSeedSeeding(int lowerSeedSeeding) {
		this.lowerSeedSeeding = lowerSeedSeeding;
	}

	public int getLowerSeedScore() {
		return lowerSeedScore;
	}

	public void setLowerSeedScore(int lowerSeedScore) {
		this.lowerSeedScore = lowerSeedScore;
	}

	public PlayoffMatch getHigherSeedMatch() {
		return higherSeedMatch;
	}

	public void setHigherSeedMatch(PlayoffMatch higherSeedMatch) {
		this.higherSeedMatch = higherSeedMatch;
	}

	public PlayoffMatch getLowerSeedMatch() {
		return lowerSeedMatch;
	}

	public void setLowerSeedMatch(PlayoffMatch lowerSeedMatch) {
		this.lowerSeedMatch = lowerSeedMatch;
	}

	public List<GameBasicDataDTO> getGames() {
		return games;
	}

	public void setGames(List<GameBasicDataDTO> games) {
		this.games = games;
	}

	public TeamStats getWinner() {
		return winner;
	}

	public void setWinner(TeamStats winner) {
		this.winner = winner;
	}
}
