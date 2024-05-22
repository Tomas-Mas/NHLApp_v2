package com.tom.nhl.dto;

import java.util.List;

public class GamePeriodKeyEventsDTO {

	private int periodNumber;
	private String periodScore;
	private List<KeyEvent> events;
	
	public GamePeriodKeyEventsDTO() { }
	
	public GamePeriodKeyEventsDTO(int periodNumber, String periodScore, List<KeyEvent> events) {
		this.periodNumber = periodNumber;
		this.periodScore = periodScore;
		this.events = events;
	}
	
	public int getPeriodNumber() {
		return periodNumber;
	}
	
	public void setPeriodNumber(int periodNumber) {
		this.periodNumber = periodNumber;
	}
	
	public String getPeriodScore() {
		return periodScore;
	}
	
	public void setPeriodScore(String periodScore) {
		this.periodScore = periodScore;
	}
	
	public List<KeyEvent> getEvents() {
		return events;
	}
	
	public void setEvents(List<KeyEvent> events) {
		this.events = events;
	}
}
