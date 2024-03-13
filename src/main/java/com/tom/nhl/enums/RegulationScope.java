package com.tom.nhl.enums;

/** Home / away / overall */
public enum RegulationScope {
	HOME("Home", "home"),
	AWAY("Away", "away"),
	OVERALL("Overall", null);
	
	private String type;
	private String value;
	
	private RegulationScope(String type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public static RegulationScope valueOfType(String type) {
		type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
		for(RegulationScope s : values()) {
			if(s.type.equals(type)) {
				return s;
			}
		}
		return RegulationScope.OVERALL;
	}
	
	public String getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	
	public String formatted() {
		return type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
	}
}
