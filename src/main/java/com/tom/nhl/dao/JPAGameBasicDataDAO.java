package com.tom.nhl.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.stereotype.Component;

import com.tom.nhl.config.DefaultConstants;
import com.tom.nhl.entity.Game;
import com.tom.nhl.entity.Team;
import com.tom.nhl.entity.view.GameBasicDataView;
import com.tom.nhl.enums.RegulationScope;
import com.tom.nhl.enums.SeasonScope;
import com.tom.nhl.util.HibernateUtil;

@Component
public class JPAGameBasicDataDAO implements GameBasicDataDAO {
	
	private static final int currentPage = 0;
	private static final int pageSize = DefaultConstants.MAIN_PAGE_GAME_LIST_SIZE;
	
	public GameBasicDataView getById(int id) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameBasicDataView> query = cb.createQuery(GameBasicDataView.class);
		Root<GameBasicDataView> gameViewRoot = query.from(GameBasicDataView.class);
		
		query.select(gameViewRoot)
				.where(cb.equal(gameViewRoot.<Integer>get("id"), id));
		
		GameBasicDataView game = em.createQuery(query).getSingleResult();
		em.close();
		return game;
	}
	
	public List<GameBasicDataView> getBySeasonWithPeriodGoals(int season) {
		List<GameBasicDataView> gameList = getBySeason(season);
		return getWithGoalsPerPeriodByGames(gameList);
	}
	
	public List<GameBasicDataView> getH2hByGame(int gameId) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameBasicDataView> query = cb.createQuery(GameBasicDataView.class);
		Root<GameBasicDataView> gameViewRoot = query.from(GameBasicDataView.class);
		
		//game's season subquery
		Subquery<Integer> sqSeason = query.subquery(Integer.class);
		Root<Game> sqSeasonGameRoot = sqSeason.from(Game.class);
		sqSeason.select(sqSeasonGameRoot.<Integer>get("season"))
				.where(cb.equal(sqSeasonGameRoot.get("id"), gameId));
		
		//home team id subquery
		Subquery<Integer> sqTeam1Id = query.subquery(Integer.class);
		Root<Game> sqTeam1GameRoot = sqTeam1Id.from(Game.class);
		sqTeam1GameRoot.join("homeTeam", JoinType.INNER);
		sqTeam1Id.select(sqTeam1GameRoot.<Team>get("homeTeam").<Integer>get("id"))
				.where(cb.equal(sqTeam1GameRoot.get("id"), gameId));
		
		//away team id subquery
		Subquery<Integer> sqTeam2Id = query.subquery(Integer.class);
		Root<Game> sqTeam2GameRoot = sqTeam2Id.from(Game.class);
		sqTeam2GameRoot.join("homeTeam", JoinType.INNER);
		sqTeam2Id.select(sqTeam2GameRoot.<Team>get("awayTeam").<Integer>get("id"))
				.where(cb.equal(sqTeam2GameRoot.get("id"), gameId));
		
		//game ids for teams and season by gameId parameter
		Subquery<Integer> sqGames = query.subquery(Integer.class);
		Root<Game> sqGameRoot = sqGames.from(Game.class);
		Join<Game, Team> homeTeam = sqGameRoot.join("homeTeam", JoinType.INNER);
		Join<Game, Team> awayTeam = sqGameRoot.join("awayTeam", JoinType.INNER);
		
		Predicate team1Predicate = cb.or(cb.equal(homeTeam.get("id"), sqTeam1Id), cb.equal(awayTeam.get("id"), sqTeam1Id));
		Predicate team2Predicate = cb.or(cb.equal(homeTeam.get("id"), sqTeam2Id), cb.equal(awayTeam.get("id"), sqTeam2Id));
		Predicate teamsPredicate = cb.and(team1Predicate, team2Predicate);
		Predicate seasonPredicate = cb.equal(sqGameRoot.get("season"), sqSeason);
		
		sqGames.select(sqGameRoot.<Integer>get("id"))
				.where(teamsPredicate, seasonPredicate);
		
		//final select for game views by game ids
		query.select(gameViewRoot)
				.where(cb.in(gameViewRoot.get("id")).value(sqGames))
				.orderBy(cb.desc(gameViewRoot.get("gameDate")));
		
		List<GameBasicDataView> games = em.createQuery(query).getResultList();
		em.close();
		return games;
	}
	
	public List<GameBasicDataView> getPlayoffBySeason(int season) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameBasicDataView> query = cb.createQuery(GameBasicDataView.class);
		Root<GameBasicDataView> gameViewRoot = query.from(GameBasicDataView.class);
		
		Subquery<Integer> sqFilter = query.subquery(Integer.class);
		Root<Game> sqGameRoot = sqFilter.from(Game.class);
		sqFilter.select(sqGameRoot.<Integer>get("id"))
				.where(cb.and(cb.equal(sqGameRoot.get("season"), season), cb.equal(sqGameRoot.get("gameType"), SeasonScope.PLAYOFF.getValue())));
		
		query.select(gameViewRoot)
				.where(cb.in(gameViewRoot.get("id")).value(sqFilter))
				.orderBy(cb.asc(gameViewRoot.get("gameDate")));
		List<GameBasicDataView> playoffGames = em.createQuery(query).getResultList();
		em.close();
		return playoffGames;
	}
	
	public List<GameBasicDataView> getLastGamesByAbr(int gameId, String teamAbr, RegulationScope regScope, int gamesCount) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameBasicDataView> query = cb.createQuery(GameBasicDataView.class);
		Root<GameBasicDataView> gameViewRoot = query.from(GameBasicDataView.class);
		
		Subquery<Integer> sqGameList = query.subquery(Integer.class);
		Root<Game> gameRoot = sqGameList.from(Game.class);
		gameRoot.join("homeTeam", JoinType.INNER);
		gameRoot.join("awayTeam", JoinType.INNER);
		
		Subquery<Timestamp> sqGameDate = sqGameList.subquery(Timestamp.class);
		Root<Game> sqGameRoot = sqGameDate.from(Game.class);
		sqGameDate.select(sqGameRoot.<Timestamp>get("gameDate"))
				.where(cb.equal(sqGameRoot.get("id"), gameId));
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(DefaultConstants.GAME_PAGE_LAST_GAMES_FROM_SELECTED_GAME) {
			predicates.add(cb.lessThan(gameRoot.<Timestamp>get("gameDate"), sqGameDate));
		}
		
		if(regScope == RegulationScope.OVERALL) {
			predicates.add(cb.or(
					cb.equal(gameRoot.get("homeTeam").get("abbreviation"), teamAbr),
					cb.equal(gameRoot.get("awayTeam").get("abbreviation"), teamAbr)
					));
		} else if(regScope == RegulationScope.HOME) {
			predicates.add(cb.equal(gameRoot.get("homeTeam").get("abbreviation"), teamAbr));
		} else if(regScope == RegulationScope.AWAY) {
			predicates.add(cb.equal(gameRoot.get("awayTeam").get("abbreviation"), teamAbr));
		}
		
		sqGameList.select(gameRoot.<Integer>get("id"))
		.where(predicates.toArray(new Predicate[] {}));
		
		query.select(gameViewRoot)
		.where(cb.in(gameViewRoot.get("id")).value(sqGameList))
		.orderBy(cb.desc(gameViewRoot.get("gameDate")));
		
		List<GameBasicDataView> games = em.createQuery(query)
				.setMaxResults((gamesCount + DefaultConstants.GAME_PAGE_LAST_X_GAMES_COUNT))
				.getResultList();
		
		em.close();
		return games;
	}

	private List<GameBasicDataView> getBySeason(int season) {
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
		
		em.close();
		return gameList;
	}
	
	private List<GameBasicDataView> getWithGoalsPerPeriodByGames(List<GameBasicDataView> games) {
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
		
		em.close();
		return games;
	}

}
