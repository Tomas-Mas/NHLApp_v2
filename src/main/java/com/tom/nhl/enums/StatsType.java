package com.tom.nhl.enums;

public enum StatsType {
	HOME("Home"),
	AWAY("Away"),
	OVERALL("Overall");
	
	private String type;
	
	private StatsType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
