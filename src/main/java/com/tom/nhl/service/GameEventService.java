package com.tom.nhl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tom.nhl.dao.GameEventDAO;
import com.tom.nhl.dto.GamePeriodKeyEventsDTO;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.mapper.GameEventMapper;

@Component
public class GameEventService {
	
	@Autowired
	private GameEventDAO gameEventDAO;
	
	@Autowired
	private GameEventMapper gameEventMapper;

	public List<GamePeriodKeyEventsDTO> getKeyEventsByGame(int gameId) {
		List<GameEvent> events = gameEventDAO.getKeyEventsByGame(gameId);
		return gameEventMapper.toGamePeriodKeyEvents(events);
	}
	
	public List<String> getPeriodTypesByGame(int gameId) {
		return gameEventDAO.getPeriodTypesByGame(gameId);
	}
}
