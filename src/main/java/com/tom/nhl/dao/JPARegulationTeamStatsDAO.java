package com.tom.nhl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.tom.nhl.entity.view.RegulationTeamStats;
import com.tom.nhl.util.HibernateUtil;

@Component
public class JPARegulationTeamStatsDAO implements RegulationTeamStatsDAO {

	public List<RegulationTeamStats> getBySeason(int season) {
		EntityManager em = HibernateUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<RegulationTeamStats> query = cb.createQuery(RegulationTeamStats.class);
		Root<RegulationTeamStats> teamStatsRoot = query.from(RegulationTeamStats.class);
		
		Predicate seasonPredicate = cb.equal(teamStatsRoot.get("id").get("season"), season);
		
		query.select(teamStatsRoot)
				.where(seasonPredicate);
		
		List<RegulationTeamStats> stats = em.createQuery(query)
				.getResultList();
		
		em.close();
		return stats;
	}
}
