package com.tom.nhl.enums;

public enum PlayerPosition {
	RIGHT_WING("Right Wing", "RW"),
	LEFT_WING("Left Wing", "LW"),
	CENTER("Center", "C"),
	DEFENSEMAN("Defenseman", "D"),
	GOALIE("Goalie", "G"),
	SKATERS("Skaters", "-");
	
	private String name;
	private String abbr;
	
	private PlayerPosition(String name, String abbr) {
		this.name = name;
		this.abbr = abbr;
	}
	
	public static PlayerPosition valueOfName(String name) {
		for(PlayerPosition pos : values()) {
			if(pos.name.equals(name)) {
				return pos;
			}
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAbbr() {
		return abbr;
	}
}
