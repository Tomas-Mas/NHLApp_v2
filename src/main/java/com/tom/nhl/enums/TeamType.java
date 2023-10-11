package com.tom.nhl.enums;

public enum TeamType {
	HOME("Home"),
	AWAY("Away"),
	UNKNOWN("N/A");
	
	private String title;
	
	private TeamType(String title) {
		this.title = title;
	}

	public String formatted() {
		return title;
	}
}
