package com.tom.nhl.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Subgraph;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.tom.nhl.config.DefaultConstants;
import com.tom.nhl.entity.Event;
import com.tom.nhl.entity.EventPlayer;
import com.tom.nhl.entity.EventPlayerPK;
import com.tom.nhl.entity.Game;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.entity.Roster;
import com.tom.nhl.util.HibernateUtil;

@Component
public class JPAGameDAO implements GameDAO {
	
	private static final int currentPage = 0;
	private static final int pageSize = DefaultConstants.MAIN_PAGE_GAME_LIST_SIZE;

	public List<Integer> getSeasons() {
		EntityManager em = HibernateUtil.createEntityManager();
		List<Integer> seasons = em.createQuery("select distinct g.season from Game g order by g.season desc", Integer.class)
				.getResultList();
		em.close();
		return seasons;
	}
	
	public int getSeasonByGameId(int gameId) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
		Root<Game> gameRoot = query.from(Game.class);
		
		query.select(gameRoot.<Integer>get("season"))
				.where(cb.equal(gameRoot.get("id"), gameId));
		Integer season = em.createQuery(query).getSingleResult();
		em.close();
		return season;
	}
	
	public Map<String, String> getTeamsByGameId(int gameId) {
		Map<String, String> teamsMap = new HashMap<String, String>();
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Tuple> query = cb.createTupleQuery();
		Root<Game> gameRoot = query.from(Game.class);
		gameRoot.join("homeTeam", JoinType.INNER);
		gameRoot.join("awayTeam", JoinType.INNER);
		
		query.select(cb.tuple(gameRoot.get("homeTeam").<String>get("abbreviation").alias("homeAbr"), gameRoot.get("awayTeam").<String>get("abbreviation").alias("awayAbr")))
			.where(cb.equal(gameRoot.get("id"), gameId));
		
		Tuple result = em.createQuery(query).getSingleResult();
		
		em.close();
		
		teamsMap.put("homeAbr", result.get("homeAbr", String.class));
		teamsMap.put("awayAbr", result.get("awayAbr", String.class));
		return teamsMap;
	}
	
	////////////////////////////////////////////////////////
	
	/*
	 * well, Fetch objects cant access attributes, so they are useless for conditions for m:n and 1:n relations
	 * fetchGraphs and subgraphs can replace fetch objects, so Join object can be used instead to access attributes but
	 * it doesn't work for embedded ids, so I guess only way around it is to break complex queries to multiple ones or views
	 */
	@Deprecated
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
	
	/*
	 * replaced by fetchGamesBasicInfo
	*/
	@Deprecated
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
	
	@Deprecated
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
	
	private List<String> getKeyEventFilter() {
		List<String> filter = new ArrayList<String>();
		filter.add("Goal");
		filter.add("Penalty");
		return filter;
	}
}