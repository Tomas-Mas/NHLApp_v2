package com.tom.nhl.dto;

import java.util.List;

public class PlayoffBracket {
	
	private String conference;
	private List<PlayoffMatch> firstRound;
	private List<PlayoffMatch> secondThirdRound;
	
	public PlayoffBracket(String conference, List<PlayoffMatch> firstRound, List<PlayoffMatch> secondThirdRound) {
		this.conference = conference;
		this.firstRound = firstRound;
		this.secondThirdRound = secondThirdRound;
	}

	public String getConference() {
		return conference;
	}

	public void setConference(String conference) {
		this.conference = conference;
	}

	public List<PlayoffMatch> getFirstRound() {
		return firstRound;
	}

	public void setFirstRound(List<PlayoffMatch> firstRound) {
		this.firstRound = firstRound;
	}

	public List<PlayoffMatch> getSecondThirdRound() {
		return secondThirdRound;
	}

	public void setSecondThirdRound(List<PlayoffMatch> secondThirdRound) {
		this.secondThirdRound = secondThirdRound;
	}
}
