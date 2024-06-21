package com.tom.nhl.config;

import com.tom.nhl.enums.PlayerPosition;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.enums.SeasonScope;
import com.tom.nhl.enums.StatsColumn;

public class DefaultConstants {

	public static final int MAIN_PAGE_GAME_LIST_SIZE = 20;
	
	public static final RegulationScope TEAM_STANDINGS_DEFAULT_REGULATION_SCOPE = RegulationScope.OVERALL;
	
	public static final SeasonScope SIDEBAR_STATS_DEFAULT_SEASON_SCOPE = SeasonScope.REGULATION;
	
	public static final int GAME_PAGE_LAST_X_GAMES_COUNT = 5;
	
	//if true last games will be last games from currently viewed game, otherwise shows last games in database
	public static final boolean GAME_PAGE_LAST_GAMES_FROM_SELECTED_GAME = true;
	
	public static final RegulationScope LAST_GAMES_DEFAULT_HOME_GAMES_SCOPE = RegulationScope.HOME;
	public static final RegulationScope LAST_GAMES_DEFAULT_AWAY_GAMES_SCOPE = RegulationScope.AWAY;
	
	public static final RegulationScope PLAYERS_STATS_DEFAULT_REGULATION_SCOPE = RegulationScope.OVERALL;
	public static final PlayerPosition PLAYERS_STATS_DEFAULT_PLAYER_POSITION = PlayerPosition.SKATERS;
	public static final boolean PLAYERS_STATS_DEFAULT_ONLY_PRODUCTIVE_PLAYERS = true;
	public static final StatsColumn PLAYER_STATS_DEFAULT_ORDERED_COLUMN = StatsColumn.POINTS;
	public static final boolean PLAYER_STATS_DEFAULT_IS_DESC_ORDER = true;
}
