package com.tom.nhl.dao;

import java.util.List;

import com.tom.nhl.dto.GameStatsDTO;

public interface GameStatsDAO {

	public List<GameStatsDTO> getByIdAndPeriod(int gameId, int periodNum);
}
