package com.tom.nhl.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class TestSeasonService {

	private List<Integer> seasons;
	
	public TestSeasonService() {
		seasons = new ArrayList<Integer>();
		seasons.add(20152016);
		seasons.add(20162017);
		seasons.add(20172018);
		seasons.add(20182019);
		seasons.add(20192020);
	}
	
	public List<Integer> getSeasons() {
		return seasons;
	}
	
	public int checkActiveSeason(int season) {
		for(int s : seasons) {
			if(s == season)
				return season;
		}
		return getDefaultSeason();
	}
	
	public int getDefaultSeason() {
		return seasons.get(seasons.size() - 1);
	}
}
