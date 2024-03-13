package com.tom.nhl.dto;

import com.tom.nhl.enums.DataScope;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.enums.SeasonScope;
import com.tom.nhl.enums.StandingsScope;
import com.tom.nhl.enums.StatsColumn;

public class StatsNavigationDTO {
	
	private final int[] ROWS_PER_PAGE_OPTIONS = {5, 10, 20, 50, 100};
	private final int DEFAULT_ROWS_PER_PAGE = 20;
	
	private DataScope dataScope;				//teams players
	private SeasonScope seasonScope;			//regulation playoff
	private StandingsScope standingsScope;		//conference division
	private RegulationScope regulationScope;	//overall home away
	
	private StatsColumn orderedBy;
	private boolean reverseOrder;
	
	private int resCount;
	private int maxPage;
	private int selectedPageNumber;
	private int rowsPerPage;
	
	public StatsNavigationDTO() {
		this.dataScope = DataScope.TEAMS;
		this.seasonScope = SeasonScope.REGULATION;
		this.standingsScope = StandingsScope.CONFERENCE;
		this.regulationScope = RegulationScope.OVERALL;
		this.orderedBy = StatsColumn.POINTS;
		this.reverseOrder = false;
		this.resCount = 0;
		this.maxPage = 1;
		this.selectedPageNumber = 1;
		this.rowsPerPage = DEFAULT_ROWS_PER_PAGE;
	}
	
	public int[] getRowsPerPageOptions() {
		return this.ROWS_PER_PAGE_OPTIONS;
	}
	
	public String getDataScope() {
		if(dataScope == null)
			return null;
		else
			return dataScope.formatted();
	}
	public void setDataScope(String dataScope) {
		this.dataScope = DataScope.valueOfType(dataScope);
	}
	
	public String getSeasonScope() {
		return seasonScope.formatted();
	}
	public void setSeasonScope(String seasonScope) {
		this.seasonScope = SeasonScope.valueOfType(seasonScope);
	}
	
	public String getStandingsScope() {
		return standingsScope.formatted();
	}
	public void setStandingsScope(String standingsScope) {
		this.standingsScope = StandingsScope.valueOfType(standingsScope);
	}
	
	public String getRegulationScope() {
		return regulationScope.formatted();
	}
	public void setRegulationScope(String regulationScope) {
		this.regulationScope = RegulationScope.valueOfType(regulationScope);
	}
	
	public String getOrderedBy() {
		return orderedBy.getDescription();
	}
	public void setOrderedBy(String orderedBy) {
		this.orderedBy = StatsColumn.valueOfDescription(orderedBy);
	}
	
	public boolean getReverseOrder() {
		return reverseOrder;
	}
	public void setReverseOrder(boolean reverseOrder) {
		this.reverseOrder = reverseOrder;
	}
	
	public int getResCount() {
		return resCount;
	}
	public void setResCount(int resCount) {
		this.resCount = resCount;
		this.maxPage = (int) Math.ceil((double)resCount / rowsPerPage);
		if(this.maxPage < 1)
			this.maxPage = 1;
		if(selectedPageNumber > maxPage)
			selectedPageNumber = maxPage;
	}
	
	public int getMaxPage() {
		return maxPage;
	}
	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}
	
	public String getSelectedPageNumber() {
		return String.valueOf(selectedPageNumber);
	}
	public void setSelectedPageNumber(String selectedPageNumber) {
		int pages = 0;
		
		try {
			pages = Integer.valueOf(selectedPageNumber);
		} catch(NumberFormatException e) {
			pages = 1;
		}
		
		if(pages < 1)
			pages = 1;
		this.selectedPageNumber = pages;
	}
	
	public int getRowsPerPage() {
		return rowsPerPage;
	}
	public void setRowsPerPage(int rowsPerPage) {
		for(Integer option : this.ROWS_PER_PAGE_OPTIONS) {
			if(option == rowsPerPage) {
				this.rowsPerPage = rowsPerPage;
				return;
			}
		}
		this.rowsPerPage = DEFAULT_ROWS_PER_PAGE;
	}
	
	public DataScope getDataScopeEnum() {
		return dataScope;
	}
	
	public SeasonScope getSeasonScopeEnum() {
		return seasonScope;
	}
	
	public StandingsScope getStandingsScopeEnum() {
		return standingsScope;
	}
	
	public RegulationScope getRegulationScopeEnum() {
		return regulationScope;
	}
	
	public StatsColumn getOrderedByEnum() {
		return orderedBy;
	}
}
