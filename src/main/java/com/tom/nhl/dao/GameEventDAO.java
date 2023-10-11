package com.tom.nhl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.tom.nhl.entity.EventPlayer;
import com.tom.nhl.entity.EventPlayerPK;
import com.tom.nhl.entity.Game;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.entity.Roster;
import com.tom.nhl.entity.Team;
import com.tom.nhl.enums.TeamType;
import com.tom.nhl.util.HibernateUtil;

@Component
public class GameEventDAO {

	/*
	public List<GameEvent> getKeyEvents(Game game) {
		EntityManager em = HibernateUtil.createEntityManager();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> query = cb.createTupleQuery();
		
		Root<GameEvent> rootEvent = query.from(GameEvent.class);
		rootEvent.fetch("event", JoinType.INNER);
		
		Join<GameEvent, EventPlayer> players = rootEvent.join("players", JoinType.INNER);
		Join<EventPlayer, EventPlayerPK> eventPlayerPK = players.join("id", JoinType.INNER);
		Join<EventPlayerPK, Roster> roster = eventPlayerPK.join("roster", JoinType.INNER);
		Join<Roster, Team> team = roster.join("team", JoinType.INNER);
		
		Predicate gamePredicate = cb.equal(rootEvent.get("game"), game);
		Predicate goalEventPredicate = cb.and(cb.equal(rootEvent.get("event").get("name"), "Goal"), cb.equal(players.get("role"), "Scorer"));
		Predicate penaltyEventPredicate = cb.and(cb.equal(rootEvent.get("event").get("name"), "Penalty"), cb.equal(players.get("role"), "PenaltyOn"));
		Predicate eventPredicate = cb.or(goalEventPredicate, penaltyEventPredicate);
		
		Expression<Object> actedByCase = cb.selectCase()
				.when(cb.equal(team.get("id"), game.getHomeTeam().getId()), "HOME")
				.when(cb.equal(team.get("id"), game.getAwayTeam().getId()), "AWAY")
				.otherwise("UNKNOWN");
		
		query.multiselect(rootEvent, actedByCase);
		query.where(cb.and(gamePredicate, eventPredicate));
		query.orderBy(cb.asc(rootEvent.get("jsonId")));
		List<Tuple> events = em.createQuery(query).getResultList();
		
		List<GameEvent> keyEvents = new ArrayList<GameEvent>();
		for(Tuple t : events) {
			GameEvent e = t.get(0, GameEvent.class);
			e.setActedBy(TeamType.valueOf(t.get(1, String.class)));
			em.detach(e);
			keyEvents.add(e);
		}
		return keyEvents;
	}*/
}
