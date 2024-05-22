package com.tom.nhl.dto;

import java.util.List;

public class PlayoffSpiderDTO {

	private List<PlayoffBracket> brackets;
	private PlayoffMatch finals;
	
	public PlayoffSpiderDTO(List<PlayoffBracket> brackets, PlayoffMatch finals) {
		this.brackets = brackets;
		this.finals = finals;
	}

	public List<PlayoffBracket> getBrackets() {
		return brackets;
	}

	public void setBrackets(List<PlayoffBracket> brackets) {
		this.brackets = brackets;
	}

	public PlayoffMatch getFinals() {
		return finals;
	}

	public void setFinals(PlayoffMatch finals) {
		this.finals = finals;
	}
}
