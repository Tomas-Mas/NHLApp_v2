package com.tom.nhl.dao;

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
	
	public List<GameBasicDataView> getByGameAndTeams(int gameId, int team1Id, int team2Id) {
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
