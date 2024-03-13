package com.tom.nhl.enums;

/** Teams / players */
public enum DataScope {
	TEAMS("Teams"),
	PLAYERS("Players");
	
	private String type;
	
	private DataScope(String type) {
		this.type = type;
	}
	
	public static DataScope valueOfType(String type) {
		type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
		for(DataScope s : values()) {
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
