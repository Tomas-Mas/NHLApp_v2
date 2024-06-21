package com.tom.nhl.dto;

import com.tom.nhl.config.DefaultConstants;
import com.tom.nhl.enums.PlayerPosition;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.enums.StatsColumn;

public class PlayerStatsFilterDTO {

	private RegulationScope regulationScope;
	private PlayerPosition playerPosition;
	private boolean onlyProductive;
	private StatsColumn orderByColumn;
	private boolean isDescOrder;
	
	public PlayerStatsFilterDTO() {
		this.regulationScope = DefaultConstants.PLAYERS_STATS_DEFAULT_REGULATION_SCOPE;
		this.playerPosition = DefaultConstants.PLAYERS_STATS_DEFAULT_PLAYER_POSITION;
		this.onlyProductive = DefaultConstants.PLAYERS_STATS_DEFAULT_ONLY_PRODUCTIVE_PLAYERS;
		this.orderByColumn = DefaultConstants.PLAYER_STATS_DEFAULT_ORDERED_COLUMN;
		this.isDescOrder = DefaultConstants.PLAYER_STATS_DEFAULT_IS_DESC_ORDER;
	}
	
	public String getRegulationScope() {
		return regulationScope.formatted();
	}
	
	public void setRegulationScope(String regulationScope) {
		this.regulationScope = RegulationScope.valueOfType(regulationScope);
	}
	
	public String getPlayerPosition() {
		return playerPosition.getName();
	}
	
	public void setPlayerPosition(String playerPosition) {
		this.playerPosition = PlayerPosition.valueOfName(playerPosition);
	}
	
	public boolean getOnlyProductive() {
		return onlyProductive;
	}
	
	public void setOnlyProductive(boolean onlyProductive) {
		this.onlyProductive = onlyProductive;
	}
	
	public String getOrderByColumn() {
		return orderByColumn.getDescription();
	}
	
	public void setOrderByColumn(String orderByColumn) {
		this.orderByColumn = StatsColumn.valueOfDescription(orderByColumn);
	}
	
	public boolean getIsDescOrder() {
		return isDescOrder;
	}
	
	public void setIsDescOrder(boolean isDescOrder) {
		this.isDescOrder = isDescOrder;
	}
	
	public RegulationScope getRegulationScopeEnum() {
		return regulationScope;
	}
	
	public PlayerPosition getPlayerPositionEnum() {
		return playerPosition;
	}
	
	public StatsColumn getOrderByColumnEnum() {
		return orderByColumn;
	}
}
