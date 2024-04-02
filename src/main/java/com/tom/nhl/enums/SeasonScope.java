package com.tom.nhl.enums;

/** Regulation / playoff */
public enum SeasonScope {
	REGULATION("Regulation", "R"),
	PLAYOFF("Playoff", "P"),
	DEFAULT("Default", "N/A");
	
	private String type;
	private String value;
	
	private SeasonScope(String type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public static SeasonScope valueOfType(String type) {
		type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
		for(SeasonScope s : values()) {
			if(s.type.equals(type)) {
				return s;
			}
		}
		return SeasonScope.DEFAULT;
	}
	
	public static SeasonScope valueOfValue(String value) {
		value = value.toUpperCase();
		for(SeasonScope s : values()) {
			if(s.value.equals(value)) {
				return s;
			}
		}
		return SeasonScope.DEFAULT;
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
