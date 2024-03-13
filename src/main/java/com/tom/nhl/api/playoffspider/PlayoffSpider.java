package com.tom.nhl.api.playoffspider;

import java.util.List;

public class PlayoffSpider {

	private List<PlayoffBracket> brackets;
	private PlayoffMatch finals;
	
	public PlayoffSpider(List<PlayoffBracket> brackets, PlayoffMatch finals) {
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
