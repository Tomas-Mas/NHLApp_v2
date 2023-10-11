package com.tom.nhl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.stereotype.Component;

import com.tom.nhl.entity.Event;
import com.tom.nhl.entity.EventPlayer;
import com.tom.nhl.entity.EventPlayerPK;
import com.tom.nhl.entity.Game;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.entity.Roster;
import com.tom.nhl.entity.view.MainPageGameBasicData;
import com.tom.nhl.entity.wrapper.GameBasicInfo;
import com.tom.nhl.util.HibernateUtil;

@Component
public class GameDAO {
	
	private static final int currentPage = 0;
	private static final int pageSize = 20;

	public List<Integer> getSeasons() {
		EntityManager em = HibernateUtil.createEntityManager();
		List<Integer> seasons = em.createQuery("select distinct g.season from Game g", Integer.class)
				.getResultList();
		em.close();
		return seasons;
	}
	
	public List<Game> getGamesBySeasonWithKeyEvents(int season) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		EntityGraph<Game> fetchGraph = em.createEntityGraph(Game.class);
		fetchGraph.addSubgraph("homeTeam");
		fetchGraph.addSubgraph("awayTeam");
		fetchGraph.addSubgraph("venue");
		fetchGraph.addSubgraph("gameStatus");
		
		CriteriaQuery<Game> query = cb.createQuery(Game.class);
		Root<Game> gameRoot = query.from(Game.class);
		
		query.select(gameRoot)
				.where(cb.equal(gameRoot.get("season"), season))
				.orderBy(cb.desc(gameRoot.get("gameDate")));
		
		List<Game> games = em.createQuery(query)
				.setHint("javax.persistence.loadgraph", fetchGraph)
				.setFirstResult(currentPage * pageSize)
				.setMaxResults(pageSize)
				.getResultList();
		
		for(Game game : games) {
			em.detach(game);
			game.setEvents(fetchKeyEvents(game));
		}
		
		return games;
	}
	
	private List<String> getKeyEventFilter() {
		List<String> filter = new ArrayList<String>();
		filter.add("Goal");
		filter.add("Penalty");
		return filter;
	}
	
	private List<GameEvent> fetchKeyEvents(Game game) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameEvent> query = cb.createQuery(GameEvent.class);
		Root<GameEvent> eventRoot = query.from(GameEvent.class);
		eventRoot.fetch("event", JoinType.LEFT);
		Fetch<GameEvent,EventPlayer> eventPlayerFetch = eventRoot.fetch("players", JoinType.LEFT);
		Fetch<EventPlayer,EventPlayerPK> eventPlayerPKFetch = eventPlayerFetch.fetch("id");
		Fetch<EventPlayerPK,Roster> rosterFetch = eventPlayerPKFetch.fetch("roster", JoinType.LEFT);
		rosterFetch.fetch("player", JoinType.LEFT);
		rosterFetch.fetch("team", JoinType.LEFT);
		
		Predicate gamePredicate = cb.equal(eventRoot.get("game"), game);
		Predicate eventNamePredicate = eventRoot.get("event").get("name").in(getKeyEventFilter());
		
		query.select(eventRoot)
				.distinct(true)
				.where(cb.and(gamePredicate, eventNamePredicate))
				.orderBy(cb.asc(eventRoot.get("jsonId")));
		
		List<GameEvent> events = em.createQuery(query)
				.getResultList();
		
		for(GameEvent e : events) {
			em.detach(e);
		}
		
		return events;
	}
	
	public List<GameBasicInfo> getGamesBasicInfo(int season) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<MainPageGameBasicData> query = cb.createQuery(MainPageGameBasicData.class);
		Root<MainPageGameBasicData> gameViewRoot = query.from(MainPageGameBasicData.class);
		
		Subquery<Integer> sqSeasonFilter = query.subquery(Integer.class);
		Root<Game> sqGameRoot = sqSeasonFilter.from(Game.class);
		sqSeasonFilter.select(sqGameRoot.<Integer>get("id"))
				.where(cb.equal(sqGameRoot.get("season"), season));
		
		query.select(gameViewRoot)
				.where(cb.in(gameViewRoot.get("id")).value(sqSeasonFilter))
				.orderBy(cb.desc(gameViewRoot.get("gameDate")));
		List<MainPageGameBasicData> gameList = em.createQuery(query)
				.setFirstResult(currentPage * pageSize)
				.setMaxResults(pageSize)
				.getResultList();
		
		CriteriaQuery<MainPageGameBasicData> finalQuery = cb.createQuery(MainPageGameBasicData.class);
		Root<MainPageGameBasicData> gameRoot = finalQuery.from(MainPageGameBasicData.class);
		gameRoot.fetch("goalsPerPeriod");
		finalQuery.select(gameRoot)
				.distinct(true)
				.where(gameRoot.in(gameList))
				.orderBy(cb.desc(gameRoot.get("gameDate")));
		List<MainPageGameBasicData> resList = em.createQuery(finalQuery).getResultList();
		
		List<GameBasicInfo> games = new ArrayList<GameBasicInfo>();
		for(MainPageGameBasicData g : resList) {
			games.add(new GameBasicInfo(g));
		}
		
		return games;
	}
	
	/*
	 * well, Fetch objects cant access attributes, so they are useless for conditions for m:n and 1:n relationships
	 * fetchGraphs and subgraphs can replace fetch objects, so Join object can be used instead to access attributes but
	 * it doesn't work for embedded ids, so I guess only way around it is to break complex queries to multiple ones or views
	 */
	public void test(int season) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		EntityGraph<Game> fetchGraph = em.createEntityGraph(Game.class);
		fetchGraph.addSubgraph("homeTeam");
		fetchGraph.addSubgraph("awayTeam");
		fetchGraph.addSubgraph("gameStatus");
		Subgraph<GameEvent> eventsGraph = fetchGraph.addSubgraph("events");
		eventsGraph.addSubgraph("event");
		
		CriteriaQuery<Game> query = cb.createQuery(Game.class);
		Root<Game> gameRoot = query.from(Game.class);
		Join<Game,GameEvent> gameEventsJoin = gameRoot.join("events", JoinType.LEFT);
		Join<GameEvent,Event> eventJoin = gameEventsJoin.join("event", JoinType.LEFT);
		
		Predicate gameFilter = gameRoot.get("id").in(getFilteredGamesIds(season, em, cb));
		Predicate eventFilter = eventJoin.get("name").in(getKeyEventFilter());
		
		query.select(gameRoot)
				.distinct(true)
				.where(cb.and(gameFilter, eventFilter))
				.orderBy(cb.desc(gameRoot.get("gameDate")))
				.orderBy(cb.asc(gameEventsJoin.get("jsonId")));
		
		TypedQuery<Game> q = em.createQuery(query).setHint("javax.persistence.loadgraph", fetchGraph);
		List<Game> res = q.getResultList();
		
		System.out.println("end of queries" + res.size());
		for(Game g : res) {
			System.out.println(g.getId() + " - " + g.getHomeTeam().getAbbreviation() + " vs " + g.getAwayTeam().getAbbreviation() + " - " + g.getGameStatus().getName() + " events: "
					+ g.getEvents().size());
			for(GameEvent event : g.getEvents()) {
				System.out.println(event.getJsonId() + " - " + event.getEvent().getName() + " - " + event.getPlayers().size());
				/*for(EventPlayer player : event.getPlayers()) {
					System.out.println(player.getRole());
				}*/
			}
		}
	}
	private List<Integer> getFilteredGamesIds(int season, EntityManager em, CriteriaBuilder cb) {
		CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
		Root<Game> game = query.from(Game.class);
		query.select(game.<Integer>get("id"))
				.where(cb.equal(game.get("season"), season))
				.orderBy(cb.desc(game.get("gameDate")));
		List<Integer> resList = em.createQuery(query)
				.setFirstResult(currentPage * pageSize)
				.setMaxResults(pageSize)
				.getResultList();
		return resList;
	}
}
