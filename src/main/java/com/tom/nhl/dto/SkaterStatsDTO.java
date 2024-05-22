package com.tom.nhl.dto;

import com.tom.nhl.enums.PlayerPosition;

public class SkaterStatsDTO {
	
	private int season;
	private String gameType;
	private int playerId;
	private String firstName;
	private String lastName;
	private PlayerPosition position;
	private int gamesPlayed;
	private String timeOnIceAvg;
	private int plusMinus;
	private int goals;
	private int assists;
	private int points;
	private int ppGoals;
	private int ppPoints;
	private int shGoals;
	private int shPoints;
	private int otGoals;
	private int shootoutTaken;
	private int shootoutGoals;
	private int penaltyMinutes;
	private int shots;
	private int blockedShots;
	private int faceoffs;
	private int faceoffsWon;
	private int hits;
	private int hitsTaken;
	private int takeaway;
	private int giveaway;
	
	public SkaterStatsDTO(int season, String gameType, int playerId, String firstName, String lastName, String position, long gamesPlayed, double timeOnIceAvg,
			long plusMinus, long goals, long assists, long points, long ppGoals, long ppPoints, long shGoals, long shPoints, long otGoals,
			long shootoutTaken, long shootoutGoals, long penaltyMinutes, long shots, long blockedShots, long faceoffs, long faceoffsWon,
			long hits, long hitsTaken, long takeaway, long giveaway) {
		this.season = season;
		this.gameType = gameType;
		this.playerId = playerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.position = PlayerPosition.valueOfName(position);
		this.gamesPlayed = (int) gamesPlayed;
		this.timeOnIceAvg = timeOnIceToString(timeOnIceAvg);
		this.plusMinus = (int) plusMinus;
		this.goals = (int) goals;
		this.assists = (int) assists;
		this.points = (int) points;
		this.ppGoals = (int) ppGoals;
		this.ppPoints = (int) ppPoints;
		this.shGoals = (int) shGoals;
		this.shPoints = (int) shPoints;
		this.otGoals = (int) otGoals;
		this.shootoutTaken = (int) shootoutTaken;
		this.shootoutGoals = (int) shootoutGoals;
		this.penaltyMinutes = (int) penaltyMinutes;
		this.shots = (int) shots;
		this.blockedShots = (int) blockedShots;
		this.faceoffs = (int) faceoffs;
		this.faceoffsWon = (int) faceoffsWon;
		this.hits = (int) hits;
		this.hitsTaken = (int) hitsTaken;
		this.takeaway = (int) takeaway;
		this.giveaway = (int) giveaway;
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public PlayerPosition getPosition() {
		return position;
	}

	public void setPosition(PlayerPosition position) {
		this.position = position;
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}
	
	public String getTimeOnIceAvg() {
		return timeOnIceAvg;
	}
	
	public void setTimeOnIceAvg(String timeOnIceAvg) {
		this.timeOnIceAvg = timeOnIceAvg;
	}
	
	public int getPlusMinus() {
		return plusMinus;
	}
	
	public void setPlusMinus(int plusMinus) {
		this.plusMinus = plusMinus;
	}
	
	public int getGoals() {
		return goals;
	}

	public void setGoals(int goals) {
		this.goals = goals;
	}

	public int getAssists() {
		return assists;
	}

	public void setAssists(int assists) {
		this.assists = assists;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getPpGoals() {
		return ppGoals;
	}

	public void setPpGoals(int ppGoals) {
		this.ppGoals = ppGoals;
	}

	public int getPpPoints() {
		return ppPoints;
	}

	public void setPpPoints(int ppPoints) {
		this.ppPoints = ppPoints;
	}

	public int getShGoals() {
		return shGoals;
	}

	public void setShGoals(int shGoals) {
		this.shGoals = shGoals;
	}

	public int getShPoints() {
		return shPoints;
	}

	public void setShPoints(int shPoints) {
		this.shPoints = shPoints;
	}

	public int getOtGoals() {
		return otGoals;
	}

	public void setOtGoals(int otGoals) {
		this.otGoals = otGoals;
	}

	public int getShootoutTaken() {
		return shootoutTaken;
	}

	public void setShootoutTaken(int shootoutTaken) {
		this.shootoutTaken = shootoutTaken;
	}

	public int getShootoutGoals() {
		return shootoutGoals;
	}

	public void setShootoutGoals(int shootoutGoals) {
		this.shootoutGoals = shootoutGoals;
	}

	public int getPenaltyMinutes() {
		return penaltyMinutes;
	}

	public void setPenaltyMinutes(int penaltyMinutes) {
		this.penaltyMinutes = penaltyMinutes;
	}

	public int getShots() {
		return shots;
	}

	public void setShots(int shots) {
		this.shots = shots;
	}

	public int getBlockedShots() {
		return blockedShots;
	}

	public void setBlockedShots(int blockedShots) {
		this.blockedShots = blockedShots;
	}

	public int getFaceoffs() {
		return faceoffs;
	}

	public void setFaceoffs(int faceoffs) {
		this.faceoffs = faceoffs;
	}

	public int getFaceoffsWon() {
		return faceoffsWon;
	}

	public void setFaceoffsWon(int faceoffsWon) {
		this.faceoffsWon = faceoffsWon;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getHitsTaken() {
		return hitsTaken;
	}

	public void setHitsTaken(int hitsTaken) {
		this.hitsTaken = hitsTaken;
	}

	public int getTakeaway() {
		return takeaway;
	}

	public void setTakeaway(int takeaway) {
		this.takeaway = takeaway;
	}

	public int getGiveaway() {
		return giveaway;
	}

	public void setGiveaway(int giveaway) {
		this.giveaway = giveaway;
	}
	
	public float getPointsPerGamesPlayed() {
		return (float)Math.round(((float)points / gamesPlayed) * 100) / 100;
	}
	
	public float getShootingPercentage() {
		return (float)Math.round(((float)goals / shots) * 1000) / 10;
	}
	
	public float getFaceoffsWinPercentage() {
		return (float)Math.round(((float)faceoffsWon / faceoffs) * 1000) / 10;
	}

	private String timeOnIceToString(double toi) {
		int t = (int) Math.round(toi);
		int min = t / 60;
		int s = t % 60;
		
		if(s == 0)
			return min + ":00";
		if(s < 10)
			return min + ":0" + s;
		if(s >= 10)
			return min + ":" + s;
		return "--:--";
	}

	@Override
	public String toString() {
		//System.out.println("GP \t TOI \t +- \t G \t A \t P \t ppG \t ppP \t shG \t shP \t otG \t SHO \t SHG \t pim \t S \t bS \t foO \t foW \t HF \t HA \t TA \t GA \t bordel\n");
		return gamesPlayed + "\t" + timeOnIceAvg + "\t" + plusMinus + "\t" + goals + "\t" + assists + "\t" + points + "\t" + ppGoals + "\t" + ppPoints
				+ "\t" + shGoals + "\t" + shPoints + "\t" + otGoals + "\t" + shootoutTaken + "\t" + shootoutGoals + "\t" + penaltyMinutes
				+ "\t" + shots + "\t" + blockedShots + "\t" + faceoffs + "\t" + faceoffsWon + "\t" + hits + "\t" + hitsTaken
				+ "\t" + takeaway + "\t" + giveaway
				+ "\t" + season + "  " + gameType + "  " + playerId + "  " + firstName + "  " + lastName + "  " + position.getName();
	}

}
