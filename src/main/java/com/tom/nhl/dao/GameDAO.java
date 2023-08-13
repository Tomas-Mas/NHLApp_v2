package com.tom.nhl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.tom.nhl.entity.Game;
import com.tom.nhl.util.HibernateUtil;

@Component
public class GameDAO {

	public List<Integer> getSeasons() {
		EntityManager em = HibernateUtil.createEntityManager();
		List<Integer> seasons = em.createQuery("select distinct g.season from Game g", Integer.class)
				.getResultList();
		em.close();
		return seasons;
	}
	
	public List<Game> getGamesBySeason(int season) {
		EntityManager em = HibernateUtil.createEntityManager();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Game> criteria = cb.createQuery(Game.class);
		
		Root<Game> root = criteria.from(Game.class);
		criteria.where(cb.equal(root.get("season"), season));
		criteria.orderBy(cb.desc(root.get("gameDate")));
		List<Game> games = em.createQuery(criteria).setMaxResults(10).getResultList();
		
		for(Game game : games) {
			em.detach(game);
		}
		return games;
		//return em.createQuery("select g from Game g where season = :s", Game.class).setParameter("s", season).setMaxResults(10).getResultList();
	}
	
	public List<Game> getGamesBySeasonWithOrderedKeyEvents(int season) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Game> query = cb.createQuery(Game.class);
		
		Root<Game> game = query.from(Game.class);
		
		game.fetch("gameStatus", JoinType.INNER);
		game.fetch("venue", JoinType.INNER);
		game.fetch("homeTeam", JoinType.INNER);
		game.fetch("awayTeam", JoinType.INNER);
		
		query.where(cb.equal(game.get("season"), season));
		query.orderBy(cb.desc(game.get("gameDate")));
		List<Game> games = em.createQuery(query).setMaxResults(10).getResultList();
		
		for(Game g : games) {
			em.detach(g);
		}
		
		return games;
	}
}
