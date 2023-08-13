package com.tom.nhl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.tom.nhl.entity.Game;
import com.tom.nhl.entity.GameEvent;
import com.tom.nhl.util.HibernateUtil;

@Component
public class GameEventDAO {

	public List<GameEvent> getOrderedCoreEvents(Game game) {
		EntityManager em = HibernateUtil.createEntityManager();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GameEvent> query = cb.createQuery(GameEvent.class);
		
		Root<GameEvent> rootEvent = query.from(GameEvent.class);
		rootEvent.fetch("event", JoinType.INNER);
		query.where(cb.equal(rootEvent.get("game"), game));
		query.orderBy(cb.asc(rootEvent.get("jsonId")));
		
		List<GameEvent> events = em.createQuery(query).getResultList();
		for(GameEvent event : events) {
			em.detach(event);
		}
		return events;
	}
}
