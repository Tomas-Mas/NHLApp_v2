package com.tom.nhl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.stereotype.Component;

import com.tom.nhl.dto.GameEventDTO;
import com.tom.nhl.entity.EventPlayer;
import com.tom.nhl.entity.EventPlayerPK;
import com.tom.nhl.entity.Game;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.entity.Roster;
import com.tom.nhl.util.HibernateUtil;

@Component
public class JPAGameEventDAO implements GameEventDAO {

	public List<GameEvent> getKeyEventsByGame(int gameId) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameEvent> query = cb.createQuery(GameEvent.class);
		Root<GameEvent> eventRoot = query.from(GameEvent.class);
		eventRoot.fetch("game", JoinType.LEFT);
		eventRoot.fetch("event", JoinType.LEFT);
		Fetch<GameEvent,EventPlayer> eventPlayerFetch = eventRoot.fetch("players", JoinType.LEFT);
		Fetch<EventPlayer,EventPlayerPK> eventPlayerPKFetch = eventPlayerFetch.fetch("id");
		Fetch<EventPlayerPK,Roster> rosterFetch = eventPlayerPKFetch.fetch("roster", JoinType.LEFT);
		rosterFetch.fetch("player", JoinType.LEFT);
		rosterFetch.fetch("team", JoinType.LEFT);
		
		Subquery<Game> gameSelectionSubquery = query.subquery(Game.class);
		Root<Game> sqGameRoot = gameSelectionSubquery.from(Game.class);
		gameSelectionSubquery.select(sqGameRoot)
				.where(cb.equal(sqGameRoot.get("id"), gameId));
		
		Predicate gamePredicate = cb.equal(eventRoot.get("game"), gameSelectionSubquery);
		Predicate eventNamePredicate = eventRoot.get("event").get("name").in(getKeyEventFilter());
		
		query.select(eventRoot)
				.distinct(true)
				.where(cb.and(gamePredicate, eventNamePredicate))
				//.orderBy(cb.asc(eventRoot.get("jsonId")));
				.orderBy(cb.asc(eventRoot.get("periodNumber")), cb.asc(eventRoot.get("periodTime")), cb.asc(eventRoot.get("jsonId")));
		
		List<GameEvent> events = em.createQuery(query)
				.getResultList();
		em.close();
		return events;
	}
	
	public List<GameEvent> getFullEventsByGame(int gameId) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameEvent> query = cb.createQuery(GameEvent.class);
		Root<GameEvent> eventRoot = query.from(GameEvent.class);
		//eventRoot.fetch("game", JoinType.LEFT);
		Fetch<GameEvent, Game> gameFetch = eventRoot.fetch("game", JoinType.LEFT);
		gameFetch.fetch("homeTeam", JoinType.LEFT);
		gameFetch.fetch("awayTeam", JoinType.LEFT);
		eventRoot.fetch("event", JoinType.LEFT);
		Fetch<GameEvent,EventPlayer> eventPlayerFetch = eventRoot.fetch("players", JoinType.LEFT);
		Fetch<EventPlayer,EventPlayerPK> eventPlayerPKFetch = eventPlayerFetch.fetch("id");
		Fetch<EventPlayerPK,Roster> rosterFetch = eventPlayerPKFetch.fetch("roster", JoinType.LEFT);
		rosterFetch.fetch("player", JoinType.LEFT);
		rosterFetch.fetch("team", JoinType.LEFT);
		rosterFetch.fetch("position", JoinType.LEFT);
		
		Subquery<Game> gameSelectionSubquery = query.subquery(Game.class);
		Root<Game> sqGameRoot = gameSelectionSubquery.from(Game.class);
		gameSelectionSubquery.select(sqGameRoot)
				.where(cb.equal(sqGameRoot.get("id"), gameId));
		
		Predicate gamePredicate = cb.equal(eventRoot.get("game"), gameSelectionSubquery);
		
		query.select(eventRoot)
				.distinct(true)
				.where(cb.and(gamePredicate))
				//.orderBy(cb.asc(eventRoot.get("jsonId")));
				.orderBy(cb.asc(eventRoot.get("periodNumber")), cb.asc(eventRoot.get("periodTime")), cb.asc(eventRoot.get("jsonId")));
		
		List<GameEvent> events = em.createQuery(query)
				.getResultList();
		em.close();
		return events;
	}
	
	public List<GameEventDTO> getEventsBasicDataByGame(int gameId) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameEventDTO> query = cb.createQuery(GameEventDTO.class);
		Root<GameEvent> eventRoot = query.from(GameEvent.class);
		eventRoot.join("event", JoinType.LEFT);
		
		List<Expression<?>> selectExp = new ArrayList<Expression<?>>();
		selectExp.add(eventRoot.get("periodNumber"));
		selectExp.add(eventRoot.get("periodTime"));
		selectExp.add(eventRoot.get("event").get("name"));
		selectExp.add(eventRoot.get("event").get("secondaryType"));
		
		Subquery<Game> gameSelectionSubquery = query.subquery(Game.class);
		Root<Game> sqGameRoot = gameSelectionSubquery.from(Game.class);
		gameSelectionSubquery.select(sqGameRoot)
				.where(cb.equal(sqGameRoot.get("id"), gameId));
		
		Predicate gamePredicate = cb.equal(eventRoot.get("game"), gameSelectionSubquery);
		
		query.select(cb.construct(GameEventDTO.class, selectExp.toArray(new Expression<?>[] {})))
				.where(cb.and(gamePredicate))
				//.orderBy(cb.asc(eventRoot.get("jsonId")));
				.orderBy(cb.asc(eventRoot.get("periodNumber")), cb.asc(eventRoot.get("periodTime")), cb.asc(eventRoot.get("jsonId")));
		
		List<GameEventDTO> events = em.createQuery(query)
				.getResultList();
		em.close();
		return events;
	}
	
	public List<String> getPeriodTypesByGame(int gameId) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class);
		Root<GameEvent> eventRoot = query.from(GameEvent.class);
		eventRoot.join("game", JoinType.INNER);
		
		query.multiselect(eventRoot.<Integer>get("periodNumber").alias("periodNumber"), eventRoot.<String>get("periodType").alias("periodType"))
				.distinct(true)
				.where(cb.equal(eventRoot.get("game").get("id"), gameId))
				.orderBy(cb.asc(eventRoot.get("periodNumber")));
		
		List<Tuple> periodsTuple = em.createQuery(query).getResultList();
		List<String> periods = new ArrayList<String>();
		for(Tuple p : periodsTuple) {
			periods.add((String) p.get("periodType"));
		}
		em.close();
		return periods;
	}
	
	private List<String> getKeyEventFilter() {
		List<String> filter = new ArrayList<String>();
		filter.add("Goal");
		filter.add("Penalty");
		return filter;
	}

}
