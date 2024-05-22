package com.tom.nhl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.tom.nhl.dto.GameStatsDTO;
import com.tom.nhl.entity.view.GameStatsView;
import com.tom.nhl.util.HibernateUtil;

@Component
public class JPAGameStatsDAO implements GameStatsDAO {

	public List<GameStatsDTO> getByIdAndPeriod(int gameId, int periodNum) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<GameStatsDTO> query = cb.createQuery(GameStatsDTO.class);
		Root<GameStatsView> statsRoot = query.from(GameStatsView.class);
		
		Expression<String> groupByExp = statsRoot.get("id").get("team");
		
		List<Expression<?>> aggregateExp = new ArrayList<Expression<?>>();
		aggregateExp.add(cb.sum(statsRoot.<Integer>get("faceoffs")));
		aggregateExp.add(cb.sum(statsRoot.<Integer>get("penalties")));
		aggregateExp.add(cb.sum(statsRoot.<Integer>get("penaltyMinutes")));
		aggregateExp.add(cb.sum(statsRoot.<Integer>get("giveaways")));
		aggregateExp.add(cb.sum(statsRoot.<Integer>get("takeaways")));
		aggregateExp.add(cb.sum(statsRoot.<Integer>get("hits")));
		aggregateExp.add(cb.sum(statsRoot.<Integer>get("shots")));
		aggregateExp.add(cb.sum(statsRoot.<Integer>get("shotsOnGoal")));
		aggregateExp.add(cb.sum(statsRoot.<Integer>get("blockedShots")));
		aggregateExp.add(cb.sum(statsRoot.<Integer>get("missedShots")));
		aggregateExp.add(cb.sum(statsRoot.<Integer>get("goals")));
		aggregateExp.add(cb.sum(statsRoot.<Integer>get("ppGoals")));
		
		List<Expression<?>> selectExp = new ArrayList<Expression<?>>();
		selectExp.add(groupByExp);
		selectExp.addAll(aggregateExp);
		
		Predicate predicate = null;
		Predicate gamePredicate = cb.equal(statsRoot.get("id").get("gameId"), gameId);
		if(periodNum == 0) {
			predicate = gamePredicate;
		} else {
			Predicate periodPredicate = cb.equal(statsRoot.get("id").get("periodNumber"), periodNum);
			predicate = cb.and(gamePredicate, periodPredicate);
		}
		
		query.groupBy(groupByExp)
				.select(cb.construct(GameStatsDTO.class, selectExp.toArray(new Expression<?>[] {} )))
				.where(predicate);
		
		List<GameStatsDTO> stats = em.createQuery(query).getResultList();
		em.close();
		return stats;
	}
}
