package com.tom.nhl.test;

import java.util.ArrayList;
import java.util.List;

public class TestService {

	public List<Integer> getSeason() {
		List<Integer> seasons = new ArrayList<Integer>();
		seasons.add(20152016);
		seasons.add(20162017);
		seasons.add(20172018);
		return seasons;
	}
}
