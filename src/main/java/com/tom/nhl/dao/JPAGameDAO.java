package com.tom.nhl.dao;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.criteria.Subquery;

import org.springframework.stereotype.Component;

import com.tom.nhl.entity.Event;
import com.tom.nhl.entity.EventPlayer;
import com.tom.nhl.entity.EventPlayerPK;
import com.tom.nhl.entity.Game;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.entity.Roster;
import com.tom.nhl.entity.Team;
import com.tom.nhl.entity.view.GameBasicDataView;
import com.tom.nhl.util.HibernateUtil;

@Component
public class JPAGameDAO implements GameDAO {
	
	private static final int currentPage = 0;
	private static final int pageSize = 20;

	public List<Integer> fetchSeasons() {
		EntityManager em = HibernateUtil.createEntityManager();
		List<Integer> seasons = em.createQuery("select distinct g.season from Game g order by g.season desc", Integer.class)
				.getResultList();
		em.close();
		return seasons;
	}
	
	public int fetchSeasonByGameId(int gameId) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
		Root<Game> gameRoot = query.from(Game.class);
		
		query.select(gameRoot.<Integer>get("season"))
				.where(cb.equal(gameRoot.get("id"), gameId));
		Integer season = em.createQuery(query).getSingleResult();
		return season;
	}
	
	public List<GameBasicDataView> fetchGamesBasicDataBySeason(int season) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameBasicDataView> query = cb.createQuery(GameBasicDataView.class);
		Root<GameBasicDataView> gameViewRoot = query.from(GameBasicDataView.class);
		
		Subquery<Integer> sqSeasonFilter = query.subquery(Integer.class);
		Root<Game> sqGameRoot = sqSeasonFilter.from(Game.class);
		sqSeasonFilter.select(sqGameRoot.<Integer>get("id"))
				.where(cb.equal(sqGameRoot.get("season"), season));
		
		query.select(gameViewRoot)
				.where(cb.in(gameViewRoot.get("id")).value(sqSeasonFilter))
				.orderBy(cb.desc(gameViewRoot.get("gameDate")));
		List<GameBasicDataView> gameList = em.createQuery(query)
				.setFirstResult(currentPage * pageSize)
				.setMaxResults(pageSize)
				.getResultList();
		
		return gameList;
	}
	
	public GameBasicDataView fetchGameBasicDataById(int gameId) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameBasicDataView> query = cb.createQuery(GameBasicDataView.class);
		Root<GameBasicDataView> gameViewRoot = query.from(GameBasicDataView.class);
		
		query.select(gameViewRoot)
				.where(cb.equal(gameViewRoot.<Integer>get("id"), gameId));
		GameBasicDataView game = em.createQuery(query).getSingleResult();
		return game;
	}
	
	public List<GameBasicDataView> fetchGamesBasicDataByTeams(int gameId, int team1Id, int team2Id) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameBasicDataView> query = cb.createQuery(GameBasicDataView.class);
		Root<GameBasicDataView> gameViewRoot = query.from(GameBasicDataView.class);
		
		//get game's season subquery
		Subquery<Integer> sqSeason = query.subquery(Integer.class);
		Root<Game> sqSeasonGameRoot = sqSeason.from(Game.class);
		sqSeason.select(sqSeasonGameRoot.<Integer>get("season"))
				.where(cb.equal(sqSeasonGameRoot.get("id"), gameId));
		
		//get list of game ids with given teams for season subquery
		Subquery<Integer> sqGames = query.subquery(Integer.class);
		Root<Game> sqGameRoot = sqGames.from(Game.class);
		Join<Game, Team> homeTeam = sqGameRoot.join("homeTeam", JoinType.INNER);
		Join<Game, Team> awayTeam = sqGameRoot.join("awayTeam", JoinType.INNER);
		
		Predicate team1Predicate = cb.or(cb.equal(homeTeam.get("id"), team1Id), cb.equal(awayTeam.get("id"), team1Id));
		Predicate team2Predicate = cb.or(cb.equal(homeTeam.get("id"), team2Id), cb.equal(awayTeam.get("id"), team2Id));
		Predicate teamsPredicate = cb.and(team1Predicate, team2Predicate);
		Predicate seasonPredicate = cb.equal(sqGameRoot.get("season"), sqSeason);
		
		sqGames.select(sqGameRoot.<Integer>get("id"))
				.where(teamsPredicate, seasonPredicate);
		
		query.select(gameViewRoot)
				.where(cb.in(gameViewRoot.get("id")).value(sqGames))
				.orderBy(cb.desc(gameViewRoot.get("gameDate")));
		
		List<GameBasicDataView> games = em.createQuery(query).getResultList();
		return games;
	}
	
	public List<GameBasicDataView> fetchGamesGoalsPerPeriodData(List<GameBasicDataView> games) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameBasicDataView> finalQuery = cb.createQuery(GameBasicDataView.class);
		Root<GameBasicDataView> gameRoot = finalQuery.from(GameBasicDataView.class);
		gameRoot.fetch("goalsPerPeriod");
		finalQuery.select(gameRoot)
				.distinct(true)
				.where(gameRoot.in(games))
				.orderBy(cb.desc(gameRoot.get("gameDate")));
		games = em.createQuery(finalQuery).getResultList();
		
		return games;
	}
	
	public List<GameBasicDataView> fetchPlayoffGamesBasicDataBySeason(int season) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameBasicDataView> query = cb.createQuery(GameBasicDataView.class);
		Root<GameBasicDataView> gameViewRoot = query.from(GameBasicDataView.class);
		
		Subquery<Integer> sqFilter = query.subquery(Integer.class);
		Root<Game> sqGameRoot = sqFilter.from(Game.class);
		sqFilter.select(sqGameRoot.<Integer>get("id"))
				.where(cb.and(cb.equal(sqGameRoot.get("season"), season), cb.equal(sqGameRoot.get("gameType"), "P")));
		
		query.select(gameViewRoot)
				.where(cb.in(gameViewRoot.get("id")).value(sqFilter))
				.orderBy(cb.asc(gameViewRoot.get("gameDate")));
		List<GameBasicDataView> playoffGames = em.createQuery(query).getResultList();
		return playoffGames;
	}
	
	public List<GameEvent> fetchGamesKeyEventsById(int id) {
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
				.where(cb.equal(sqGameRoot.get("id"), id));
		
		Predicate gamePredicate = cb.equal(eventRoot.get("game"), gameSelectionSubquery);
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
	
	public List<String> fetchPeriodTypesByGame(int gameId) {
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
		return periods;
	}
	
	private List<String> getKeyEventFilter() {
		List<String> filter = new ArrayList<String>();
		filter.add("Goal");
		filter.add("Penalty");
		return filter;
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
}