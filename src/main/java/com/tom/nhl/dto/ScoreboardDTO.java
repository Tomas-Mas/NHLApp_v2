package com.tom.nhl.dto;

import java.util.ArrayList;
import java.util.List;

import com.tom.nhl.enums.PenaltyBoxType;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.util.LogUtil;

public class ScoreboardDTO {

	private String homeName;
	private String homeAbr;
	private int homeScore;
	private String awayName;
	private String awayAbr;
	private int awayScore;
	private List<PenaltyBoxDTO> homeBox;
	private List<PenaltyBoxDTO> awayBox;
	
	public ScoreboardDTO(String homeName, String homeAbr, int homeScore, String awayName, String awayAbr, int awayScore) {
		this.homeName = homeName;
		this.homeAbr = homeAbr;
		this.homeScore = homeScore;
		this.awayName = awayName;
		this.awayAbr = awayAbr;
		this.awayScore = awayScore;
		this.homeBox = new ArrayList<PenaltyBoxDTO>();
		this.awayBox = new ArrayList<PenaltyBoxDTO>();
	}
	
	public ScoreboardDTO(ScoreboardDTO scoreboard) {
		this.homeName = scoreboard.getHomeName();
		this.homeAbr = scoreboard.getHomeAbr();
		this.homeScore = scoreboard.getHomeScore();
		this.awayName = scoreboard.getAwayName();
		this.awayAbr = scoreboard.getAwayAbr();
		this.awayScore = scoreboard.getAwayScore();
		
		this.homeBox = new ArrayList<PenaltyBoxDTO>();
		for(PenaltyBoxDTO penalty : scoreboard.getHomeBox()) {
			this.homeBox.add(new PenaltyBoxDTO(penalty));
		}
		
		this.awayBox = new ArrayList<PenaltyBoxDTO>();
		for(PenaltyBoxDTO penalty : scoreboard.getAwayBox()) {
			this.awayBox.add(new PenaltyBoxDTO(penalty));
		}
	}
	
	public void addPlayerToPenaltyBox(int gameId, RegulationScope team, PenaltyBoxDTO penalty) {
		if(team == RegulationScope.HOME) {
			this.homeBox.add(penalty);
		} else if(team == RegulationScope.AWAY) {
			this.awayBox.add(penalty);
		} else {
			LogUtil.writeLog("game json id: " + gameId + " - penalty with empty side skipped in scoreboard");
		}
	}
	
	public void removePenaltyAfterGoalScored(RegulationScope scoringTeam) {
		if(scoringTeam == RegulationScope.AWAY) {
			if(homeBox.size() == 0)
				return;
			
			for(PenaltyBoxDTO pen : homeBox) {
				if(pen.getPenaltyType() == PenaltyBoxType.NORMAL) {
					homeBox.remove(pen);
					break;
				}
			}
		} else if(scoringTeam == RegulationScope.HOME) {
			if(awayBox.size() == 0)
				return;
			
			for(PenaltyBoxDTO pen : awayBox) {
				if(pen.getPenaltyType() == PenaltyBoxType.NORMAL) {
					awayBox.remove(pen);
					break;
				}
			}
		}
	}
	
	public void passTime(int seconds) {
		List<PenaltyBoxDTO> removedPenalties = new ArrayList<PenaltyBoxDTO>();
		List<Integer> processedPlayers = new ArrayList<Integer>();
		
		if(this.homeBox.size() > 0) {
			for(PenaltyBoxDTO pen : homeBox) {
				//match misconducts lasts for the remainder of the game
				if(pen.getPenaltyType() == PenaltyBoxType.MATCHMISCONDUCT)
					continue;
				
				//misconducts are passing by no matter how many players are in the box
				//coincidental penalties are same penalties given to both teams that cancel out
				if(pen.getPenaltyType() == PenaltyBoxType.MISCONDUCT || pen.getPenaltyType() == PenaltyBoxType.COINCIDENTAL) {
					if(pen.getDuration() - seconds <= 0) {
						removedPenalties.add(pen);
					} else {
						pen.setDuration(pen.getDuration() - seconds);
					}
					continue;
				}
				
				//only 2 players can serve penalty at the same time (if more is in the box, their penalty start after end of previous 2)
				if(processedPlayers.size() >= 2)
					continue;

				//in case of one player serving double minor don't pass time on the second one until the first is over
				if(processedPlayers.contains(pen.getPlayer().getId()))
					continue;
				
				pen.setDuration(pen.getDuration() - seconds);
				if(pen.getDuration() <= 0) {
					//TODO if more than 2 players are penalised, surplus of released player time should be removed from them
					removedPenalties.add(pen);
				} else {
					processedPlayers.add(pen.getPlayer().getId());
				}
			}
			this.homeBox.removeAll(removedPenalties);
		}
		
		processedPlayers = new ArrayList<Integer>();
		removedPenalties = new ArrayList<PenaltyBoxDTO>();
		
		if(this.awayBox.size() > 0) {
			for(PenaltyBoxDTO pen : awayBox) {
				if(pen.getPenaltyType() == PenaltyBoxType.MATCHMISCONDUCT)
					continue;
				
				if(pen.getPenaltyType() == PenaltyBoxType.MISCONDUCT || pen.getPenaltyType() == PenaltyBoxType.COINCIDENTAL) {
					if(pen.getDuration() - seconds <= 0) {
						removedPenalties.add(pen);
					} else {
						pen.setDuration(pen.getDuration() - seconds);
					}
					continue;
				}
				
				if(processedPlayers.size() >= 2)
					continue;

				if(processedPlayers.contains(pen.getPlayer().getId()))
					continue;
				
				pen.setDuration(pen.getDuration() - seconds);
				if(pen.getDuration() <= 0) {
					//TODO if more than 2 players are penalised, surplus of released player time should be removed from them
					removedPenalties.add(pen);
				} else {
					processedPlayers.add(pen.getPlayer().getId());
				}
			}
			this.awayBox.removeAll(removedPenalties);
		}
	}
	
	public String getHomeName() {
		return homeName;
	}
	
	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}
	
	public String getHomeAbr() {
		return homeAbr;
	}
	
	public void setHomeAbr(String homeAbr) {
		this.homeAbr = homeAbr;
	}
	
	public int getHomeScore() {
		return homeScore;
	}
	
	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}
	
	public String getAwayName() {
		return awayName;
	}
	
	public void setAwayName(String awayName) {
		this.awayName = awayName;
	}
	
	public String getAwayAbr() {
		return awayAbr;
	}
	
	public void setAwayAbr(String awayAbr) {
		this.awayAbr = awayAbr;
	}
	
	public int getAwayScore() {
		return awayScore;
	}
	
	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	public List<PenaltyBoxDTO> getHomeBox() {
		return homeBox;
	}

	public void setHomeBox(List<PenaltyBoxDTO> homeBox) {
		this.homeBox = homeBox;
	}

	public List<PenaltyBoxDTO> getAwayBox() {
		return awayBox;
	}

	public void setAwayBox(List<PenaltyBoxDTO> awayBox) {
		this.awayBox = awayBox;
	}
}
