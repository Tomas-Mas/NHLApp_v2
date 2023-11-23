package com.tom.nhl.enums;

public enum SeasonScope {
	REGULATION("Regulation"),
	PLAYOFF("Playoff");
	
	private String type;
	
	private SeasonScope(String type) {
		this.type = type;
	}
	
	public static SeasonScope valueOfType(String type) {
		type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
		for(SeasonScope s : values()) {
			if(s.type.equals(type)) {
				return s;
			}
		}
		return null;
	}
	
	public String getType() {
		return type;
	}
}
