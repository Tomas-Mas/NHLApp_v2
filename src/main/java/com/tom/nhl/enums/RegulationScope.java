package com.tom.nhl.enums;

public enum RegulationScope {
	HOME("Home"),
	AWAY("Away"),
	OVERALL("Overall");
	
	private String type;
	
	private RegulationScope(String type) {
		this.type = type;
	}
	
	public static RegulationScope valueOfType(String type) {
		type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
		for(RegulationScope s : values()) {
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
		return type.substring(0, 1).toUpperCase() + type.substring(1);
	}
}
