package com.tom.nhl.dto;

import com.tom.nhl.enums.PenaltyBoxType;

public class PenaltyBoxDTO {
	private PenaltyBoxType penaltyType;
	private GameEventPlayerDTO player;
	private String description;
	private int start;
	private int duration;
	
	public PenaltyBoxDTO(PenaltyBoxType penaltyType, GameEventPlayerDTO player, String description, int start, int duration) {
		this.penaltyType = penaltyType;
		this.player = player;
		this.description = description;
		this.start = start;
		this.duration = duration;
	}
	
	public PenaltyBoxDTO(PenaltyBoxDTO penaltyBox) {
		this.penaltyType = penaltyBox.getPenaltyType();
		this.player = penaltyBox.getPlayer();
		this.description = penaltyBox.getDescription();
		this.start = penaltyBox.start;
		this.duration = penaltyBox.getDuration();
	}
	
	public PenaltyBoxType getPenaltyType() {
		return penaltyType;
	}
	
	public void setPenaltyType(PenaltyBoxType penaltyType) {
		this.penaltyType = penaltyType;
	}

	public GameEventPlayerDTO getPlayer() {
		return player;
	}

	public void setPlayer(GameEventPlayerDTO player) {
		this.player = player;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
