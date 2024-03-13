package com.tom.nhl.enums;

public enum StatsColumn {
	GAMESPLAYED("GamesPlayed"),
	GOALS("Goals"),
	ASSISTS("Assists"),
	POINTS("Points"),
	PLUSMINUS("PlusMinus"),
	PENALTYMINUTES("PenaltyMinutes"),
	SHOTS("Shots"),
	BLOCKEDSHOTS("BlockedShots"),
	TIMEONICEAVG("TimeOnIceAvg"),
	DEFAULT("None");
	
	private String description;
	
	private StatsColumn(String description) {
		this.description = description;
	}
	
	public static StatsColumn valueOfDescription(String description) {
		for(StatsColumn s : values()) {
			if(s.getDescription().equals(description))
				return s;
		}
		return StatsColumn.DEFAULT;
	}
	
	public String getDescription() {
		return description;
	}
}
