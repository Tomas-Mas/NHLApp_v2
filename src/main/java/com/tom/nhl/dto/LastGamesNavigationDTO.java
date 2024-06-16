package com.tom.nhl.dto;

import com.tom.nhl.config.DefaultConstants;
import com.tom.nhl.enums.RegulationScope;

public class LastGamesNavigationDTO {

	private RegulationScope homeScope;
	private RegulationScope awayScope;
	private int homeGamesCount;
	private int awayGamesCount;
	
	public LastGamesNavigationDTO() {
		homeScope = DefaultConstants.LAST_GAMES_DEFAULT_HOME_GAMES_SCOPE;
		awayScope = DefaultConstants.LAST_GAMES_DEFAULT_AWAY_GAMES_SCOPE;
		homeGamesCount = 0;
		awayGamesCount = 0;
	}

	public String getHomeScope() {
		return homeScope.formatted();
	}

	public void setHomeScope(String homeScope) {
		this.homeScope = RegulationScope.valueOfType(homeScope);
	}

	public String getAwayScope() {
		return awayScope.formatted();
	}

	public void setAwayScope(String awayScope) {
		this.awayScope = RegulationScope.valueOfType(awayScope);
	}
	
	public int getHomeGamesCount() {
		return homeGamesCount;
	}
	
	public void setHomeGamesCount(int homeGamesCount) {
		this.homeGamesCount = homeGamesCount;
	}
	
	public int getAwayGamesCount() {
		return awayGamesCount;
	}
	
	public void setAwayGamesCount(int awayGamesCount) {
		this.awayGamesCount = awayGamesCount;
	}
	
	public RegulationScope getHomeScopeEnum() {
		return homeScope;
	}
	
	public RegulationScope getAwayScopeEnum() {
		return awayScope;
	}
}
