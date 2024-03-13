package com.tom.nhl.enums;

/** division / conference */
public enum StandingsScope {
	DIVISION("Division"),
	CONFERENCE("Conference");
	
	private String type;
	
	private StandingsScope(String type) {
		this.type = type;
	}
	
	public static StandingsScope valueOfType(String type) {
		type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
		for(StandingsScope s : values()) {
			if(s.type.equals(type)) {
				return s;
			}
		}
		return null;
	}
	
	public String getType() {
		return type;
	}
	
	public String formatted() {
		return type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
	}
}
